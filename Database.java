
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Database {

    // Connect to the HostelCare SQLite database
    public static Connection connect() throws SQLException {
        String url = "jdbc:sqlite:hostelcare.db";

        Connection conn = DriverManager.getConnection(url);

        // Enable foreign key checking for this connection
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
        }

        return conn;
    }

    // Create all five database tables
    public static void createTable() {

        String studentsSQL =
                "CREATE TABLE IF NOT EXISTS students (" +
                "student_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "email TEXT UNIQUE NOT NULL, " +
                "password TEXT NOT NULL, " +
                "room_number TEXT NOT NULL, " +
                "phone TEXT" +
                ")";

        String adminsSQL =
                "CREATE TABLE IF NOT EXISTS admins (" +
                "admin_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "email TEXT UNIQUE NOT NULL, " +
                "password TEXT NOT NULL" +
                ")";

        String staffSQL =
                "CREATE TABLE IF NOT EXISTS staff (" +
                "staff_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "email TEXT UNIQUE NOT NULL, " +
                "password TEXT NOT NULL, " +
                "phone TEXT, " +
                "category TEXT" +
                ")";

        String complaintsSQL =
                "CREATE TABLE IF NOT EXISTS complaints (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "student_id INTEGER, " +
                "room_number TEXT NOT NULL, " +
                "category TEXT NOT NULL, " +
                "description TEXT NOT NULL, " +
                "complaint_date TEXT, " +
                "status TEXT NOT NULL DEFAULT 'Pending', " +
                "FOREIGN KEY (student_id) REFERENCES students(student_id)" +
                ")";

        String tasksSQL =
                "CREATE TABLE IF NOT EXISTS staff_tasks (" +
                "task_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "complaint_id INTEGER NOT NULL, " +
                "staff_id INTEGER NOT NULL, " +
                "assigned_date TEXT, " +
                "work_update TEXT, " +
                "task_status TEXT NOT NULL DEFAULT 'Assigned', " +
                "FOREIGN KEY (complaint_id) REFERENCES complaints(id), " +
                "FOREIGN KEY (staff_id) REFERENCES staff(staff_id)" +
                ")";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            // Create the tables
            stmt.execute(studentsSQL);
            stmt.execute(adminsSQL);
            stmt.execute(staffSQL);
            stmt.execute(complaintsSQL);
            stmt.execute(tasksSQL);

            // Add student_id to an older complaints table if missing
            boolean hasStudentId = false;

            try (ResultSet rs = stmt.executeQuery(
                    "PRAGMA table_info(complaints)")) {

                while (rs.next()) {
                    if ("student_id".equals(rs.getString("name"))) {
                        hasStudentId = true;
                        break;
                    }
                }
            }

            if (!hasStudentId) {
                stmt.execute(
                    "ALTER TABLE complaints ADD COLUMN student_id INTEGER"
                );
            }

            System.out.println(
                "All five HostelCare tables created successfully!"
            );

        } catch (SQLException e) {
            System.out.println("Database setup error: " + e.getMessage());
        }
    }

    // Save a new student complaint and return its database ID
    public static int saveComplaint(
            String room,
            String category,
            String description,
            String date) throws SQLException {

        String sql =
                "INSERT INTO complaints " +
                "(room_number, category, description, complaint_date, status) " +
                "VALUES (?, ?, ?, ?, 'Pending')";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, room);
            pstmt.setString(2, category);
            pstmt.setString(3, description);
            pstmt.setString(4, date);

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        throw new SQLException("Could not get the complaint ID.");
    }

    // Retrieve all complaints for the student dashboard
    public static List<Object[]> getComplaints() throws SQLException {

        List<Object[]> complaints = new ArrayList<>();

        String sql =
                "SELECT id, room_number, category, description, " +
                "complaint_date, status FROM complaints ORDER BY id DESC";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");

                String complaintId = String.format("HC%03d", id);

                Object[] row = {
                    complaintId,
                    rs.getString("room_number"),
                    rs.getString("category"),
                    rs.getString("description"),
                    rs.getString("complaint_date"),
                    rs.getString("status")
                };

                complaints.add(row);
            }
        }

        return complaints;
    }

    // Run this method to initialize the database
    public static void main(String[] args) {
        createTable();
    }
}