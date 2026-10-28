
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;

public class AdminDashboard {

    private JFrame frame;

    private JTable complaintTable;
    private JTable staffTable;
    private JTable studentTable;

    private DefaultTableModel complaintModel;
    private DefaultTableModel staffModel;
    private DefaultTableModel studentModel;

    private JLabel totalLabel;
    private JLabel pendingLabel;
    private JLabel progressLabel;
    private JLabel resolvedLabel;

    private final Color darkGreen = new Color(20, 83, 45);
    private final Color paleGreen = new Color(240, 253, 244);
    private final Color borderGreen = new Color(187, 220, 195);
    private final Color mutedGreen = new Color(82, 115, 90);

    public AdminDashboard() {
        createDashboard();
    }

    private void createDashboard() {

        frame = new JFrame("HostelCare - Admin Dashboard");
        frame.setSize(1100, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // ================= NAVBAR =================

        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setBackground(darkGreen);
        navPanel.setPreferredSize(new Dimension(1100, 70));
        navPanel.setBorder(
                BorderFactory.createEmptyBorder(5, 15, 5, 20));

        JPanel logoPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 8, 8));
        logoPanel.setBackground(darkGreen);

        ImageIcon logoIcon = new ImageIcon("hostel.png");
        JLabel iconLabel;

        if (logoIcon.getIconWidth() > 0) {
            Image logoImage = logoIcon.getImage()
                    .getScaledInstance(35, 35, Image.SCALE_SMOOTH);
            iconLabel = new JLabel(new ImageIcon(logoImage));
        } else {
            iconLabel = new JLabel();
        }

        JLabel logoText = new JLabel("HostelCare");
        logoText.setForeground(Color.WHITE);
        logoText.setFont(new Font("Arial", Font.BOLD, 28));

        logoPanel.add(iconLabel);
        logoPanel.add(logoText);

        JPanel rightPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 12, 10));
        rightPanel.setBackground(darkGreen);

        JLabel adminLabel = new JLabel("Admin Panel");
        adminLabel.setForeground(Color.WHITE);
        adminLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(new Color(185, 28, 28));
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setBorder(
                BorderFactory.createEmptyBorder(10, 20, 10, 20));

        rightPanel.add(adminLabel);
        rightPanel.add(logoutButton);

        navPanel.add(logoPanel, BorderLayout.WEST);
        navPanel.add(rightPanel, BorderLayout.EAST);

        // ================= PAGE CONTENT =================

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(
                new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(
                BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(
                new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setBackground(paleGreen);
        welcomePanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(borderGreen),
                        BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        welcomePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel welcomeTitle =
                new JLabel("Welcome to Admin Dashboard!");
        welcomeTitle.setFont(new Font("Arial", Font.BOLD, 25));
        welcomeTitle.setForeground(darkGreen);

        JLabel welcomeDescription = new JLabel(
                "<html>Manage hostel complaints, staff and students from one place."
                        + "<br>Assign maintenance tasks and monitor complaint progress.</html>");
        welcomeDescription.setFont(new Font("Arial", Font.PLAIN, 14));
        welcomeDescription.setForeground(mutedGreen);

        welcomePanel.add(welcomeTitle);
        welcomePanel.add(Box.createVerticalStrut(10));
        welcomePanel.add(welcomeDescription);

        // ================= SUMMARY CARDS =================

        JPanel summaryPanel = new JPanel(
                new GridLayout(1, 4, 12, 12));
        summaryPanel.setBackground(Color.WHITE);
        summaryPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        summaryPanel.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 105));

        totalLabel = new JLabel("0");
        pendingLabel = new JLabel("0");
        progressLabel = new JLabel("0");
        resolvedLabel = new JLabel("0");

        summaryPanel.add(createCard("Total Complaints", totalLabel));
        summaryPanel.add(createCard("Pending", pendingLabel));
        summaryPanel.add(createCard("In Progress", progressLabel));
        summaryPanel.add(createCard("Resolved", resolvedLabel));

        // ================= TABS =================

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.BOLD, 14));

        tabs.addTab("Complaints", createComplaintsPanel());
        tabs.addTab("Staff Management", createStaffPanel());
        tabs.addTab("Student Management", createStudentPanel());

        JLabel managementHeading = new JLabel("Hostel Management");
        managementHeading.setFont(new Font("Arial", Font.BOLD, 21));
        managementHeading.setForeground(darkGreen);
        managementHeading.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPanel.add(welcomePanel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(summaryPanel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(managementHeading);
        contentPanel.add(Box.createVerticalStrut(10));

        tabs.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(tabs);

        JScrollPane pageScroll = new JScrollPane(contentPanel);
        pageScroll.setBorder(null);
        pageScroll.getVerticalScrollBar().setUnitIncrement(16);
        pageScroll.getViewport().setBackground(Color.WHITE);

        mainPanel.add(navPanel, BorderLayout.NORTH);
        mainPanel.add(pageScroll, BorderLayout.CENTER);
        frame.add(mainPanel);

        refreshAll();

        logoutButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    frame,
                    "Are you sure you want to log out?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                frame.dispose();
                new RoleSelection();
            }
        });

        frame.setVisible(true);
    }

    // ================= DATABASE CONNECTION =================

    private Connection connect() throws SQLException {
        return Database.connect();
    }

    private void refreshAll() {
        loadComplaints();
        loadStaff();
        loadStudents();
        updateSummary();
    }

    // ================= SUMMARY CARD =================

    private JPanel createCard(String heading, JLabel valueLabel) {

        JPanel card = new JPanel(new GridLayout(2, 1, 5, 5));
        card.setBackground(paleGreen);
        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(borderGreen),
                        BorderFactory.createEmptyBorder(12, 15, 12, 15)));

        JLabel headingLabel = new JLabel(heading);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 13));
        headingLabel.setForeground(mutedGreen);

        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(darkGreen);

        card.add(headingLabel);
        card.add(valueLabel);

        return card;
    }

    // ================= COMPLAINTS TAB =================

    private JPanel createComplaintsPanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(
                BorderFactory.createEmptyBorder(15, 10, 10, 10));

        String[] columns = {
                "Complaint ID", "Student", "Room", "Category",
                "Description", "Date", "Status", "Assigned Staff",
                "Work Update"
        };

        complaintModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        complaintTable = new JTable(complaintModel);
        styleTable(complaintTable);

        panel.add(new JScrollPane(complaintTable), BorderLayout.CENTER);

        JPanel buttons = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 8, 8));
        buttons.setBackground(Color.WHITE);

        JButton assignButton = createButton("Assign Staff");
        JButton statusButton = createButton("Update Status");
        JButton updateButton = createButton("Update Work");
        JButton viewButton = createButton("View Work");
        JButton deleteButton = createButton("Delete Complaint");
        JButton refreshButton = createButton("Refresh");

        assignButton.addActionListener(e -> assignStaff());
        statusButton.addActionListener(e -> updateComplaintStatus());
        updateButton.addActionListener(e -> updateWork());
        viewButton.addActionListener(e -> viewWork());
        deleteButton.addActionListener(e -> deleteComplaint());
        refreshButton.addActionListener(e -> refreshAll());

        buttons.add(assignButton);
        buttons.add(statusButton);
        buttons.add(updateButton);
        buttons.add(viewButton);
        buttons.add(deleteButton);
        buttons.add(refreshButton);

        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    private int selectedComplaintId() {

        int row = complaintTable.getSelectedRow();

        if (row == -1) {
            showMessage("Please select a complaint first.");
            return -1;
        }

        String id = complaintModel.getValueAt(row, 0).toString();

        try {
            return Integer.parseInt(id.replace("HC", ""));
        } catch (NumberFormatException e) {
            showMessage("Invalid complaint ID. Please refresh the table.");
            return -1;
        }
    }

    // ================= LOAD COMPLAINTS =================

    private void loadComplaints() {

        if (complaintModel == null) {
            return;
        }

        complaintModel.setRowCount(0);

        // Get the student name by matching complaints.student_id
        // with students.student_id.
        String sql =
                "SELECT c.id, c.student_id, s.name AS student_name, " +
                "c.room_number, c.category, c.description, " +
                "c.complaint_date, c.status, " +
                "COALESCE(st.name, 'Not Assigned') AS staff_name, " +
                "COALESCE(t.work_update, 'No update yet') AS work_update " +
                "FROM complaints c " +
                "LEFT JOIN students s ON c.student_id = s.student_id " +
                "LEFT JOIN staff_tasks t ON t.task_id = (" +
                " SELECT MAX(t2.task_id) FROM staff_tasks t2 " +
                " WHERE t2.complaint_id = c.id) " +
                "LEFT JOIN staff st ON t.staff_id = st.staff_id " +
                "ORDER BY c.id DESC";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {

                int id = rs.getInt("id");
                String studentName = rs.getString("student_name");
                String studentId = rs.getString("student_id");

                if (studentName == null || studentName.trim().isEmpty()) {
                    if (studentId == null || studentId.trim().isEmpty()) {
                        studentName = "Unknown (No Student ID)";
                    } else {
                        studentName = "Unknown (Student ID: "
                                + studentId + ")";
                    }
                }

                complaintModel.addRow(new Object[] {
                        String.format("HC%03d", id),
                        studentName,
                        rs.getString("room_number"),
                        rs.getString("category"),
                        rs.getString("description"),
                        rs.getString("complaint_date"),
                        rs.getString("status"),
                        rs.getString("staff_name"),
                        rs.getString("work_update")
                });
            }

        } catch (SQLException e) {
            showDatabaseError(e);
        }

        updateSummary();
    }

    // ================= ASSIGN STAFF =================

    private void assignStaff() {

        int complaintId = selectedComplaintId();

        if (complaintId == -1) {
            return;
        }

        JComboBox<StaffChoice> staffBox = new JComboBox<>();

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT staff_id, name, category FROM staff ORDER BY name");
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                staffBox.addItem(new StaffChoice(
                        rs.getInt("staff_id"),
                        rs.getString("name"),
                        rs.getString("category")));
            }

        } catch (SQLException e) {
            showDatabaseError(e);
            return;
        }

        if (staffBox.getItemCount() == 0) {
            showMessage("Please add staff members first.");
            return;
        }

        int result = JOptionPane.showConfirmDialog(
                frame,
                staffBox,
                "Select Staff Member",
                JOptionPane.OK_CANCEL_OPTION);

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        StaffChoice selected =
                (StaffChoice) staffBox.getSelectedItem();

        if (selected == null) {
            return;
        }

        String sql =
                "INSERT INTO staff_tasks " +
                "(complaint_id, staff_id, assigned_date, work_update, task_status) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, complaintId);
            pstmt.setInt(2, selected.id);
            pstmt.setString(3, LocalDate.now().toString());
            pstmt.setString(4, "No update yet");
            pstmt.setString(5, "Assigned");

            pstmt.executeUpdate();

            try (PreparedStatement statusStmt = conn.prepareStatement(
                    "UPDATE complaints SET status = " +
                    "CASE WHEN status = 'Resolved' THEN status " +
                    "ELSE 'Pending' END WHERE id = ?")) {

                statusStmt.setInt(1, complaintId);
                statusStmt.executeUpdate();
            }

            loadComplaints();
            showMessage("Staff assigned successfully.");

        } catch (SQLException e) {
            showDatabaseError(e);
        }
    }

    private static class StaffChoice {

        int id;
        String name;
        String category;

        StaffChoice(int id, String name, String category) {
            this.id = id;
            this.name = name;
            this.category = category;
        }

        @Override
        public String toString() {
            if (category == null || category.trim().isEmpty()) {
                return name + " (ID: " + id + ")";
            }

            return name + " - " + category + " (ID: " + id + ")";
        }
    }

    // ================= UPDATE COMPLAINT STATUS =================

    private void updateComplaintStatus() {

        int complaintId = selectedComplaintId();

        if (complaintId == -1) {
            return;
        }

        String[] statuses = {
                "Pending", "In Progress", "Resolved"
        };

        int row = complaintTable.getSelectedRow();
        String current = complaintModel.getValueAt(row, 6).toString();

        String selected = (String) JOptionPane.showInputDialog(
                frame,
                "Select complaint status:",
                "Update Status",
                JOptionPane.QUESTION_MESSAGE,
                null,
                statuses,
                current);

        if (selected == null) {
            return;
        }

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(
                     "UPDATE complaints SET status = ? WHERE id = ?")) {

            pstmt.setString(1, selected);
            pstmt.setInt(2, complaintId);
            pstmt.executeUpdate();

            loadComplaints();
            showMessage("Complaint status updated.");

        } catch (SQLException e) {
            showDatabaseError(e);
        }
    }

    // ================= UPDATE WORK =================

    private void updateWork() {

        int complaintId = selectedComplaintId();

        if (complaintId == -1) {
            return;
        }

        String oldUpdate = "";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT work_update FROM staff_tasks " +
                     "WHERE complaint_id = ? ORDER BY task_id DESC LIMIT 1")) {

            pstmt.setInt(1, complaintId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next() && rs.getString("work_update") != null) {
                    oldUpdate = rs.getString("work_update");
                }
            }

        } catch (SQLException e) {
            showDatabaseError(e);
            return;
        }

        JTextArea area = new JTextArea(oldUpdate, 5, 25);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);

        int result = JOptionPane.showConfirmDialog(
                frame,
                new JScrollPane(area),
                "Enter Staff Work Update",
                JOptionPane.OK_CANCEL_OPTION);

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String update = area.getText().trim();

        try (Connection conn = connect()) {

            int taskId = -1;

            try (PreparedStatement findTask = conn.prepareStatement(
                    "SELECT task_id FROM staff_tasks " +
                    "WHERE complaint_id = ? ORDER BY task_id DESC LIMIT 1")) {

                findTask.setInt(1, complaintId);

                try (ResultSet rs = findTask.executeQuery()) {
                    if (rs.next()) {
                        taskId = rs.getInt("task_id");
                    }
                }
            }

            if (taskId == -1) {
                showMessage("Assign a staff member before adding a work update.");
                return;
            }

            try (PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE staff_tasks SET work_update = ? WHERE task_id = ?")) {

                pstmt.setString(1, update);
                pstmt.setInt(2, taskId);
                pstmt.executeUpdate();
            }

            loadComplaints();
            showMessage("Work update saved successfully.");

        } catch (SQLException e) {
            showDatabaseError(e);
        }
    }

    // ================= VIEW WORK =================

    private void viewWork() {

        int row = complaintTable.getSelectedRow();

        if (row == -1) {
            showMessage("Please select a complaint first.");
            return;
        }

        String details =
                "Complaint ID: " + complaintModel.getValueAt(row, 0)
                + "\nStudent: " + complaintModel.getValueAt(row, 1)
                + "\nRoom: " + complaintModel.getValueAt(row, 2)
                + "\nCategory: " + complaintModel.getValueAt(row, 3)
                + "\nStatus: " + complaintModel.getValueAt(row, 6)
                + "\nAssigned Staff: " + complaintModel.getValueAt(row, 7)
                + "\nWork Update: " + complaintModel.getValueAt(row, 8);

        JOptionPane.showMessageDialog(
                frame,
                details,
                "Complaint Details",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // ================= DELETE COMPLAINT =================

    private void deleteComplaint() {

        int complaintId = selectedComplaintId();

        if (complaintId == -1) {
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                frame,
                "Delete the selected complaint and its assignments?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        try (Connection conn = connect()) {

            conn.setAutoCommit(false);

            try {
                try (PreparedStatement taskStmt = conn.prepareStatement(
                        "DELETE FROM staff_tasks WHERE complaint_id = ?")) {

                    taskStmt.setInt(1, complaintId);
                    taskStmt.executeUpdate();
                }

                try (PreparedStatement complaintStmt = conn.prepareStatement(
                        "DELETE FROM complaints WHERE id = ?")) {

                    complaintStmt.setInt(1, complaintId);
                    complaintStmt.executeUpdate();
                }

                conn.commit();

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }

            loadComplaints();
            showMessage("Complaint deleted successfully.");

        } catch (SQLException e) {
            showDatabaseError(e);
        }
    }

    // ================= STAFF MANAGEMENT =================

    private JPanel createStaffPanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(
                BorderFactory.createEmptyBorder(15, 10, 10, 10));

        String[] columns = {
                "Staff ID", "Name", "Role", "Phone"
        };

        staffModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        staffTable = new JTable(staffModel);
        styleTable(staffTable);

        panel.add(new JScrollPane(staffTable), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        buttons.setBackground(Color.WHITE);

        JButton addButton = createButton("Add Staff");
        JButton editButton = createButton("Edit Staff");
        JButton deleteButton = createButton("Delete Staff");
        JButton refreshButton = createButton("Refresh");

        addButton.addActionListener(e -> addStaff());
        editButton.addActionListener(e -> editStaff());
        deleteButton.addActionListener(e -> deleteStaff());
        refreshButton.addActionListener(e -> loadStaff());

        buttons.add(addButton);
        buttons.add(editButton);
        buttons.add(deleteButton);
        buttons.add(refreshButton);

        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    private void loadStaff() {

        if (staffModel == null) {
            return;
        }

        staffModel.setRowCount(0);

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT staff_id, name, category, phone " +
                     "FROM staff ORDER BY staff_id");
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                staffModel.addRow(new Object[] {
                        rs.getInt("staff_id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getString("phone")
                });
            }

        } catch (SQLException e) {
            showDatabaseError(e);
        }
    }

    private void addStaff() {

        JTextField name = new JTextField();
        JTextField email = new JTextField();
        JPasswordField password = new JPasswordField();
        JTextField role = new JTextField();
        JTextField phone = new JTextField();

        Object[] fields = {
                "Name:", name,
                "Email:", email,
                "Password:", password,
                "Role / Category:", role,
                "Phone:", phone
        };

        int result = JOptionPane.showConfirmDialog(
                frame, fields, "Add Staff",
                JOptionPane.OK_CANCEL_OPTION);

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        if (name.getText().trim().isEmpty()
                || email.getText().trim().isEmpty()
                || password.getPassword().length == 0) {

            showMessage("Name, email and password are required.");
            return;
        }

        String sql =
                "INSERT INTO staff (name, email, password, phone, category) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name.getText().trim());
            pstmt.setString(2, email.getText().trim());
            pstmt.setString(3, new String(password.getPassword()));
            pstmt.setString(4, phone.getText().trim());
            pstmt.setString(5, role.getText().trim());

            pstmt.executeUpdate();

            loadStaff();
            showMessage("Staff member added successfully.");

        } catch (SQLException e) {
            showDatabaseError(e);
        }
    }

    private void editStaff() {

        int row = staffTable.getSelectedRow();

        if (row == -1) {
            showMessage("Please select a staff member.");
            return;
        }

        int id = Integer.parseInt(
                staffModel.getValueAt(row, 0).toString());

        JTextField name = new JTextField(
                staffModel.getValueAt(row, 1).toString());
        JTextField role = new JTextField(
                valueOfCell(staffModel, row, 2));
        JTextField phone = new JTextField(
                valueOfCell(staffModel, row, 3));

        Object[] fields = {
                "Name:", name,
                "Role / Category:", role,
                "Phone:", phone
        };

        int result = JOptionPane.showConfirmDialog(
                frame, fields, "Edit Staff",
                JOptionPane.OK_CANCEL_OPTION);

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        if (name.getText().trim().isEmpty()) {
            showMessage("Name is required.");
            return;
        }

        String sql =
                "UPDATE staff SET name = ?, category = ?, phone = ? " +
                "WHERE staff_id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name.getText().trim());
            pstmt.setString(2, role.getText().trim());
            pstmt.setString(3, phone.getText().trim());
            pstmt.setInt(4, id);

            pstmt.executeUpdate();

            loadStaff();
            loadComplaints();
            showMessage("Staff details updated.");

        } catch (SQLException e) {
            showDatabaseError(e);
        }
    }

    private void deleteStaff() {

        int row = staffTable.getSelectedRow();

        if (row == -1) {
            showMessage("Please select a staff member.");
            return;
        }

        int id = Integer.parseInt(
                staffModel.getValueAt(row, 0).toString());

        int choice = JOptionPane.showConfirmDialog(
                frame,
                "Delete this staff member and their task assignments?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        try (Connection conn = connect()) {

            conn.setAutoCommit(false);

            try {
                try (PreparedStatement taskStmt = conn.prepareStatement(
                        "DELETE FROM staff_tasks WHERE staff_id = ?")) {

                    taskStmt.setInt(1, id);
                    taskStmt.executeUpdate();
                }

                try (PreparedStatement staffStmt = conn.prepareStatement(
                        "DELETE FROM staff WHERE staff_id = ?")) {

                    staffStmt.setInt(1, id);
                    staffStmt.executeUpdate();
                }

                conn.commit();

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }

            loadStaff();
            loadComplaints();
            showMessage("Staff member deleted.");

        } catch (SQLException e) {
            showDatabaseError(e);
        }
    }

    // ================= STUDENT MANAGEMENT =================

    private JPanel createStudentPanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(
                BorderFactory.createEmptyBorder(15, 10, 10, 10));

        String[] columns = {
                "Student ID", "Name", "Room", "Phone"
        };

        studentModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        studentTable = new JTable(studentModel);
        styleTable(studentTable);

        panel.add(new JScrollPane(studentTable), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        buttons.setBackground(Color.WHITE);

        JButton addButton = createButton("Add Student");
        JButton editButton = createButton("Edit Student");
        JButton deleteButton = createButton("Delete Student");
        JButton refreshButton = createButton("Refresh");

        addButton.addActionListener(e -> addStudent());
        editButton.addActionListener(e -> editStudent());
        deleteButton.addActionListener(e -> deleteStudent());
        refreshButton.addActionListener(e -> loadStudents());

        buttons.add(addButton);
        buttons.add(editButton);
        buttons.add(deleteButton);
        buttons.add(refreshButton);

        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    private void loadStudents() {

        if (studentModel == null) {
            return;
        }

        studentModel.setRowCount(0);

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT student_id, name, room_number, phone " +
                     "FROM students ORDER BY student_id");
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                studentModel.addRow(new Object[] {
                        rs.getInt("student_id"),
                        rs.getString("name"),
                        rs.getString("room_number"),
                        rs.getString("phone")
                });
            }

        } catch (SQLException e) {
            showDatabaseError(e);
        }
    }

    private void addStudent() {

        JTextField name = new JTextField();
        JTextField email = new JTextField();
        JPasswordField password = new JPasswordField();
        JTextField room = new JTextField();
        JTextField phone = new JTextField();

        Object[] fields = {
                "Name:", name,
                "Email:", email,
                "Password:", password,
                "Room Number:", room,
                "Phone:", phone
        };

        int result = JOptionPane.showConfirmDialog(
                frame, fields, "Add Student",
                JOptionPane.OK_CANCEL_OPTION);

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        if (name.getText().trim().isEmpty()
                || email.getText().trim().isEmpty()
                || password.getPassword().length == 0) {

            showMessage("Name, email and password are required.");
            return;
        }

        String sql =
                "INSERT INTO students " +
                "(name, email, password, room_number, phone) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name.getText().trim());
            pstmt.setString(2, email.getText().trim());
            pstmt.setString(3, new String(password.getPassword()));
            pstmt.setString(4, room.getText().trim());
            pstmt.setString(5, phone.getText().trim());

            pstmt.executeUpdate();

            loadStudents();
            showMessage("Student added successfully.");

        } catch (SQLException e) {
            showDatabaseError(e);
        }
    }

    private void editStudent() {

        int row = studentTable.getSelectedRow();

        if (row == -1) {
            showMessage("Please select a student.");
            return;
        }

        int id = Integer.parseInt(
                studentModel.getValueAt(row, 0).toString());

        JTextField name = new JTextField(
                studentModel.getValueAt(row, 1).toString());
        JTextField room = new JTextField(
                valueOfCell(studentModel, row, 2));
        JTextField phone = new JTextField(
                valueOfCell(studentModel, row, 3));

        Object[] fields = {
                "Name:", name,
                "Room Number:", room,
                "Phone:", phone
        };

        int result = JOptionPane.showConfirmDialog(
                frame, fields, "Edit Student",
                JOptionPane.OK_CANCEL_OPTION);

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        if (name.getText().trim().isEmpty()) {
            showMessage("Name is required.");
            return;
        }

        String sql =
                "UPDATE students SET name = ?, room_number = ?, phone = ? " +
                "WHERE student_id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name.getText().trim());
            pstmt.setString(2, room.getText().trim());
            pstmt.setString(3, phone.getText().trim());
            pstmt.setInt(4, id);

            pstmt.executeUpdate();

            loadStudents();
            loadComplaints();
            showMessage("Student details updated.");

        } catch (SQLException e) {
            showDatabaseError(e);
        }
    }

    private void deleteStudent() {

        int row = studentTable.getSelectedRow();

        if (row == -1) {
            showMessage("Please select a student.");
            return;
        }

        int id = Integer.parseInt(
                studentModel.getValueAt(row, 0).toString());

        int choice = JOptionPane.showConfirmDialog(
                frame,
                "Delete this student? Students with existing complaints cannot be deleted.",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(
                     "DELETE FROM students WHERE student_id = ?")) {

            pstmt.setInt(1, id);

            int affected = pstmt.executeUpdate();

            if (affected > 0) {
                loadStudents();
                loadComplaints();
                showMessage("Student deleted successfully.");
            }

        } catch (SQLException e) {
            showMessage(
                    "Could not delete this student. They may have existing complaints.\n"
                    + e.getMessage());
        }
    }

    // ================= TABLE DESIGN =================

    private void styleTable(JTable table) {

        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(28);
        table.setSelectionBackground(paleGreen);
        table.setSelectionForeground(darkGreen);
        table.setGridColor(borderGreen);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        table.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setBackground(paleGreen);
        table.getTableHeader().setForeground(darkGreen);
        table.getTableHeader().setReorderingAllowed(false);
    }

    private JButton createButton(String text) {

        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setForeground(darkGreen);
        button.setBackground(paleGreen);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(borderGreen),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        return button;
    }

    // ================= SUMMARY COUNTS =================

    private void updateSummary() {

        if (complaintModel == null) {
            return;
        }

        int total = complaintModel.getRowCount();
        int pending = 0;
        int inProgress = 0;
        int resolved = 0;

        for (int i = 0; i < total; i++) {

            String status =
                    String.valueOf(complaintModel.getValueAt(i, 6));

            if (status.equals("Pending")) {
                pending++;
            } else if (status.equals("In Progress")) {
                inProgress++;
            } else if (status.equals("Resolved")) {
                resolved++;
            }
        }

        totalLabel.setText(String.valueOf(total));
        pendingLabel.setText(String.valueOf(pending));
        progressLabel.setText(String.valueOf(inProgress));
        resolvedLabel.setText(String.valueOf(resolved));
    }

    // ================= HELPER METHODS =================

    private String valueOfCell(
            DefaultTableModel model, int row, int column) {

        Object value = model.getValueAt(row, column);
        return value == null ? "" : value.toString();
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }

    private void showDatabaseError(SQLException e) {
        JOptionPane.showMessageDialog(
                frame,
                "Database error:\n" + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }

    public void showDashboard() {
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminDashboard dashboard = new AdminDashboard();
            dashboard.showDashboard();
        });
    }
}
