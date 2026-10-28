
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private static final String URL = "jdbc:sqlite:hostelcare.db";

    // Create tables and the default admin when Database is first loaded.
    static {
        createTable();
        createDefaultAdmin();
    }

    // Connect to SQLite
    public static Connection connect() throws SQLException {
        Connection connection = DriverManager.getConnection(URL);

        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
        }

        return connection;
    }

    // Create all five tables
    public static void createTable() {

        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS students (" +
                    "student_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL, " +
                    "email TEXT UNIQUE NOT NULL, " +
                    "password TEXT NOT NULL, " +
                    "room_number TEXT, phone TEXT)"
            );

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS admins (" +
                    "admin_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL, " +
                    "email TEXT UNIQUE NOT NULL, " +
                    "password TEXT NOT NULL)"
            );

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS staff (" +
                    "staff_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL, " +
                    "email TEXT UNIQUE NOT NULL, " +
                    "password TEXT NOT NULL, " +
                    "phone TEXT, category TEXT)"
            );

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS complaints (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "student_id INTEGER, " +
                    "room_number TEXT NOT NULL, " +
                    "category TEXT NOT NULL, " +
                    "description TEXT NOT NULL, " +
                    "complaint_date TEXT NOT NULL, " +
                    "status TEXT NOT NULL DEFAULT 'Pending', " +
                    "FOREIGN KEY(student_id) " +
                    "REFERENCES students(student_id))"
            );

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS staff_tasks (" +
                    "task_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "complaint_id INTEGER NOT NULL, " +
                    "staff_id INTEGER NOT NULL, " +
                    "assigned_date TEXT, " +
                    "work_update TEXT, " +
                    "task_status TEXT DEFAULT 'Pending', " +
                    "FOREIGN KEY(complaint_id) REFERENCES complaints(id), " +
                    "FOREIGN KEY(staff_id) REFERENCES staff(staff_id))"
            );

            // Add student_id to an older complaints table if necessary.
            boolean hasStudentId = false;

            try (ResultSet columns =
                         statement.executeQuery("PRAGMA table_info(complaints)")) {
                while (columns.next()) {
                    if ("student_id".equalsIgnoreCase(
                            columns.getString("name"))) {
                        hasStudentId = true;
                        break;
                    }
                }
            }

            if (!hasStudentId) {
                statement.executeUpdate(
                        "ALTER TABLE complaints ADD COLUMN student_id INTEGER"
                );
            }

            System.out.println("HostelCare database tables are ready.");

        } catch (SQLException e) {
            System.err.println("Database setup failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Create default administrator
    public static void createDefaultAdmin() {

        String sql = "INSERT OR IGNORE INTO admins "
                + "(name, email, password) VALUES (?, ?, ?)";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "Hostel Admin");
            statement.setString(2, "admin@hostelcare.com");
            statement.setString(3, "admin123");
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Validate admin login
    public static boolean validateAdmin(String email, String password) {

        String sql = "SELECT admin_id FROM admins "
                + "WHERE email = ? AND password = ?";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);
            statement.setString(2, password);

            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Validate staff login
    public static boolean validateStaff(String email, String password) {

        String sql = "SELECT staff_id FROM staff "
                + "WHERE email = ? AND password = ?";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);
            statement.setString(2, password);

            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get the logged-in staff member's ID
    public static int getStaffId(String email, String password) {

        String sql = "SELECT staff_id FROM staff "
                + "WHERE email = ? AND password = ?";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);
            statement.setString(2, password);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return result.getInt("staff_id");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    // Get the logged-in student's ID
    public static int getStudentId(String email, String password) {

        String sql = "SELECT student_id FROM students "
                + "WHERE email = ? AND password = ?";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);
            statement.setString(2, password);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return result.getInt("student_id");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    // Save a complaint for a specific student
    public static int saveStudentComplaint(
            int studentId, String room, String category,
            String description, String date) {

        String sql = "INSERT INTO complaints "
                + "(student_id, room_number, category, description, "
                + "complaint_date, status) "
                + "VALUES (?, ?, ?, ?, ?, 'Pending')";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, studentId);
            statement.setString(2, room);
            statement.setString(3, category);
            statement.setString(4, description);
            statement.setString(5, date);
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }

            try (Statement idStatement = connection.createStatement();
                 ResultSet result = idStatement.executeQuery(
                         "SELECT last_insert_rowid()")) {

                if (result.next()) {
                    return result.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    // Keep the original complaint-saving method available
    public static boolean saveComplaint(
            String room, String category, String description, String date) {

        String sql = "INSERT INTO complaints "
                + "(room_number, category, description, complaint_date, status) "
                + "VALUES (?, ?, ?, ?, 'Pending')";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, room);
            statement.setString(2, category);
            statement.setString(3, description);
            statement.setString(4, date);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get complaints for existing code that uses this method
    public static List<String[]> getComplaints() {

        List<String[]> complaints = new ArrayList<>();

        String sql = "SELECT id, student_id, room_number, category, "
                + "description, complaint_date, status "
                + "FROM complaints ORDER BY id DESC";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                int studentId = result.getInt("student_id");
                String studentIdValue = result.wasNull()
                        ? "" : String.valueOf(studentId);

                complaints.add(new String[]{
                        String.valueOf(result.getInt("id")),
                        studentIdValue,
                        result.getString("room_number"),
                        result.getString("category"),
                        result.getString("description"),
                        result.getString("complaint_date"),
                        result.getString("status")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return complaints;
    }

    // Return tasks assigned to one staff member.
    // Array columns: task ID, complaint ID, student, room, category,
    // description, date, status, work update.
    public static List<String[]> getStaffTasks(int staffId)
            throws SQLException {

        List<String[]> tasks = new ArrayList<>();

        String sql =
                "SELECT t.task_id, c.id AS complaint_id, " +
                "COALESCE(s.name, 'Unknown') AS student_name, " +
                "c.room_number, c.category, c.description, " +
                "c.complaint_date, " +
                "COALESCE(t.task_status, c.status, 'Pending') AS task_status, " +
                "COALESCE(t.work_update, 'Not started') AS work_update " +
                "FROM staff_tasks t " +
                "JOIN complaints c ON c.id = t.complaint_id " +
                "LEFT JOIN students s ON s.student_id = c.student_id " +
                "WHERE t.staff_id = ? " +
                "AND t.task_id = (" +
                " SELECT MAX(t2.task_id) FROM staff_tasks t2 " +
                " WHERE t2.complaint_id = t.complaint_id) " +
                "ORDER BY t.task_id DESC";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, staffId);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    tasks.add(new String[]{
                            String.valueOf(result.getInt("task_id")),
                            String.valueOf(result.getInt("complaint_id")),
                            result.getString("student_name"),
                            result.getString("room_number"),
                            result.getString("category"),
                            result.getString("description"),
                            result.getString("complaint_date"),
                            result.getString("task_status"),
                            result.getString("work_update")
                    });
                }
            }
        }

        return tasks;
    }

    // Save staff work update and status to SQLite
    public static boolean updateStaffTask(
            int taskId, int staffId, String workUpdate, String status)
            throws SQLException {

        String updateTask =
                "UPDATE staff_tasks SET work_update = ?, task_status = ? " +
                "WHERE task_id = ? AND staff_id = ?";

        try (Connection connection = connect()) {

            connection.setAutoCommit(false);

            try {
                int updated;

                try (PreparedStatement statement =
                             connection.prepareStatement(updateTask)) {

                    statement.setString(1, workUpdate);
                    statement.setString(2, status);
                    statement.setInt(3, taskId);
                    statement.setInt(4, staffId);

                    updated = statement.executeUpdate();
                }

                if (updated == 0) {
                    connection.rollback();
                    return false;
                }

                // Keep the complaint status in sync for the admin dashboard.
                String complaintSql =
                        "UPDATE complaints SET status = ? " +
                        "WHERE id = (SELECT complaint_id FROM staff_tasks " +
                        "WHERE task_id = ? AND staff_id = ?)";

                try (PreparedStatement statement =
                             connection.prepareStatement(complaintSql)) {

                    statement.setString(1, status);
                    statement.setInt(2, taskId);
                    statement.setInt(3, staffId);
                    statement.executeUpdate();
                }

                connection.commit();
                return true;

            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    public static void main(String[] args) {
        createTable();
        createDefaultAdmin();
        System.out.println("Database initialization completed.");
    }
}
