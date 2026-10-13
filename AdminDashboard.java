
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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

    // Colrs
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

        // ================= MAIN PANEL =================

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // ================= NAVBAR =================

        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setBackground(darkGreen);
        navPanel.setPreferredSize(new Dimension(1100, 70));
        navPanel.setBorder(
                BorderFactory.createEmptyBorder(5, 15, 5, 20));

        // LOGO SECTION
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

        // NAVBAR RIGHT SECTION
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

        // ================= WELCOME SECTION =================

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

        // ================= MANAGEMENT NAVBAR =================

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.BOLD, 14));

        tabs.addTab("Complaints", createComplaintsPanel());
        tabs.addTab("Staff Management", createStaffPanel());
        tabs.addTab("Student Management", createStudentPanel());

        // Add content
        contentPanel.add(welcomePanel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(summaryPanel);
        contentPanel.add(Box.createVerticalStrut(20));

        JLabel managementHeading =
                new JLabel("Hostel Management");

        managementHeading.setFont(
                new Font("Arial", Font.BOLD, 21));
        managementHeading.setForeground(darkGreen);
        managementHeading.setAlignmentX(Component.LEFT_ALIGNMENT);

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

        // SAMPLE DATA FOR FRONTEND TESTING
        addSampleData();
        updateSummary();

        // LOGOUT ACTION
        logoutButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    frame,
                    "Are you sure you want to log out?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (choice == JOptionPane.YES_OPTION) {
                frame.dispose();
                new RoleSelection();
            }
        });

        frame.setVisible(true);
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

        assignButton.addActionListener(e -> assignStaff());
        statusButton.addActionListener(e -> updateComplaintStatus());
        updateButton.addActionListener(e -> updateWork());
        viewButton.addActionListener(e -> viewWork());
        deleteButton.addActionListener(e -> deleteComplaint());

        buttons.add(assignButton);
        buttons.add(statusButton);
        buttons.add(updateButton);
        buttons.add(viewButton);
        buttons.add(deleteButton);

        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    private void assignStaff() {

        int row = complaintTable.getSelectedRow();

        if (row == -1) {
            showMessage("Please select a complaint first.");
            return;
        }

        if (staffModel.getRowCount() == 0) {
            showMessage("Please add staff members first.");
            return;
        }

        String[] names = new String[staffModel.getRowCount()];

        for (int i = 0; i < staffModel.getRowCount(); i++) {
            names[i] = staffModel.getValueAt(i, 1).toString();
        }

        String selected = (String) JOptionPane.showInputDialog(
                frame,
                "Select staff member:",
                "Assign Complaint",
                JOptionPane.QUESTION_MESSAGE,
                null,
                names,
                names[0]);

        if (selected != null) {
            complaintModel.setValueAt(selected, row, 7);
            showMessage("Staff assigned successfully.");
        }
    }

    private void updateComplaintStatus() {

        int row = complaintTable.getSelectedRow();

        if (row == -1) {
            showMessage("Please select a complaint first.");
            return;
        }

        String[] statuses = {
                "Pending", "In Progress", "Resolved"
        };

        String current = complaintModel.getValueAt(row, 6).toString();

        String selected = (String) JOptionPane.showInputDialog(
                frame,
                "Select complaint status:",
                "Update Status",
                JOptionPane.QUESTION_MESSAGE,
                null,
                statuses,
                current);

        if (selected != null) {
            complaintModel.setValueAt(selected, row, 6);
            updateSummary();
        }
    }

    private void updateWork() {

        int row = complaintTable.getSelectedRow();

        if (row == -1) {
            showMessage("Please select a complaint first.");
            return;
        }

        String oldUpdate =
                complaintModel.getValueAt(row, 8).toString();

        JTextArea area = new JTextArea(oldUpdate, 5, 25);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);

        int result = JOptionPane.showConfirmDialog(
                frame,
                new JScrollPane(area),
                "Enter Staff Work Update",
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            complaintModel.setValueAt(
                    area.getText().trim(), row, 8);

            showMessage("Work update saved.");
        }
    }

    private void viewWork() {

        int row = complaintTable.getSelectedRow();

        if (row == -1) {
            showMessage("Please select a complaint first.");
            return;
        }

        String details =
                "Complaint ID: " + complaintModel.getValueAt(row, 0)
                + "\nStudent: " + complaintModel.getValueAt(row, 1)
                + "\nCategory: " + complaintModel.getValueAt(row, 3)
                + "\nStatus: " + complaintModel.getValueAt(row, 6)
                + "\nAssigned Staff: " + complaintModel.getValueAt(row, 7)
                + "\nWork Update: " + complaintModel.getValueAt(row, 8);

        JOptionPane.showMessageDialog(
                frame, details, "Complaint Details",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteComplaint() {

        int row = complaintTable.getSelectedRow();

        if (row == -1) {
            showMessage("Please select a complaint first.");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                frame,
                "Delete the selected complaint?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            complaintModel.removeRow(row);
            updateSummary();
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

        addButton.addActionListener(e -> addStaff());
        editButton.addActionListener(e -> editStaff());
        deleteButton.addActionListener(e -> deleteStaff());

        buttons.add(addButton);
        buttons.add(editButton);
        buttons.add(deleteButton);

        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    private void addStaff() {

        JTextField id = new JTextField();
        JTextField name = new JTextField();
        JTextField role = new JTextField();
        JTextField phone = new JTextField();

        Object[] fields = {
                "Staff ID:", id,
                "Name:", name,
                "Role:", role,
                "Phone:", phone
        };

        int result = JOptionPane.showConfirmDialog(
                frame, fields, "Add Staff",
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {

            if (id.getText().trim().isEmpty()
                    || name.getText().trim().isEmpty()) {
                showMessage("Staff ID and name are required.");
                return;
            }

            if (staffIdExists(id.getText().trim(), -1)) {
                showMessage("This Staff ID already exists.");
                return;
            }

            staffModel.addRow(new Object[] {
                    id.getText().trim(),
                    name.getText().trim(),
                    role.getText().trim(),
                    phone.getText().trim()
            });
        }
    }

    private void editStaff() {

        int row = staffTable.getSelectedRow();

        if (row == -1) {
            showMessage("Please select a staff member.");
            return;
        }

        JTextField id = new JTextField(
                staffModel.getValueAt(row, 0).toString());
        JTextField name = new JTextField(
                staffModel.getValueAt(row, 1).toString());
        JTextField role = new JTextField(
                staffModel.getValueAt(row, 2).toString());
        JTextField phone = new JTextField(
                staffModel.getValueAt(row, 3).toString());

        Object[] fields = {
                "Staff ID:", id,
                "Name:", name,
                "Role:", role,
                "Phone:", phone
        };

        int result = JOptionPane.showConfirmDialog(
                frame, fields, "Edit Staff",
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {

            if (id.getText().trim().isEmpty()
                    || name.getText().trim().isEmpty()) {
                showMessage("Staff ID and name are required.");
                return;
            }

            if (staffIdExists(id.getText().trim(), row)) {
                showMessage("This Staff ID already exists.");
                return;
            }

            String oldName =
                    staffModel.getValueAt(row, 1).toString();
            String newName = name.getText().trim();

            staffModel.setValueAt(id.getText().trim(), row, 0);
            staffModel.setValueAt(newName, row, 1);
            staffModel.setValueAt(role.getText().trim(), row, 2);
            staffModel.setValueAt(phone.getText().trim(), row, 3);

            // Update complaint assignments if staff name changes.
            for (int i = 0; i < complaintModel.getRowCount(); i++) {
                if (complaintModel.getValueAt(i, 7)
                        .toString().equals(oldName)) {
                    complaintModel.setValueAt(newName, i, 7);
                }
            }
        }
    }

    private boolean staffIdExists(String id, int ignoredRow) {

        for (int i = 0; i < staffModel.getRowCount(); i++) {
            if (i != ignoredRow
                    && staffModel.getValueAt(i, 0).toString()
                    .equalsIgnoreCase(id)) {
                return true;
            }
        }

        return false;
    }

    private void deleteStaff() {

        int row = staffTable.getSelectedRow();

        if (row == -1) {
            showMessage("Please select a staff member.");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                frame,
                "Delete this staff member?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {

            String name = staffModel.getValueAt(row, 1).toString();

            staffModel.removeRow(row);

            for (int i = 0; i < complaintModel.getRowCount(); i++) {
                if (complaintModel.getValueAt(i, 7)
                        .toString().equals(name)) {
                    complaintModel.setValueAt(
                            "Not Assigned", i, 7);
                }
            }
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

        addButton.addActionListener(e -> addStudent());
        editButton.addActionListener(e -> editStudent());
        deleteButton.addActionListener(e -> deleteStudent());

        buttons.add(addButton);
        buttons.add(editButton);
        buttons.add(deleteButton);

        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    private void addStudent() {

        JTextField id = new JTextField();
        JTextField name = new JTextField();
        JTextField room = new JTextField();
        JTextField phone = new JTextField();

        Object[] fields = {
                "Student ID:", id,
                "Name:", name,
                "Room Number:", room,
                "Phone:", phone
        };

        int result = JOptionPane.showConfirmDialog(
                frame, fields, "Add Student",
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {

            if (id.getText().trim().isEmpty()
                    || name.getText().trim().isEmpty()) {
                showMessage("Student ID and name are required.");
                return;
            }

            if (studentIdExists(id.getText().trim(), -1)) {
                showMessage("This Student ID already exists.");
                return;
            }

            studentModel.addRow(new Object[] {
                    id.getText().trim(),
                    name.getText().trim(),
                    room.getText().trim(),
                    phone.getText().trim()
            });
        }
    }

    private void editStudent() {

        int row = studentTable.getSelectedRow();

        if (row == -1) {
            showMessage("Please select a student.");
            return;
        }

        JTextField id = new JTextField(
                studentModel.getValueAt(row, 0).toString());
        JTextField name = new JTextField(
                studentModel.getValueAt(row, 1).toString());
        JTextField room = new JTextField(
                studentModel.getValueAt(row, 2).toString());
        JTextField phone = new JTextField(
                studentModel.getValueAt(row, 3).toString());

        Object[] fields = {
                "Student ID:", id,
                "Name:", name,
                "Room Number:", room,
                "Phone:", phone
        };

        int result = JOptionPane.showConfirmDialog(
                frame, fields, "Edit Student",
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {

            if (id.getText().trim().isEmpty()
                    || name.getText().trim().isEmpty()) {
                showMessage("Student ID and name are required.");
                return;
            }

            if (studentIdExists(id.getText().trim(), row)) {
                showMessage("This Student ID already exists.");
                return;
            }

            studentModel.setValueAt(id.getText().trim(), row, 0);
            studentModel.setValueAt(name.getText().trim(), row, 1);
            studentModel.setValueAt(room.getText().trim(), row, 2);
            studentModel.setValueAt(phone.getText().trim(), row, 3);
        }
    }

    private boolean studentIdExists(String id, int ignoredRow) {

        for (int i = 0; i < studentModel.getRowCount(); i++) {
            if (i != ignoredRow
                    && studentModel.getValueAt(i, 0).toString()
                    .equalsIgnoreCase(id)) {
                return true;
            }
        }

        return false;
    }

    private void deleteStudent() {

        int row = studentTable.getSelectedRow();

        if (row == -1) {
            showMessage("Please select a student.");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                frame,
                "Delete this student?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            studentModel.removeRow(row);
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

        int total = complaintModel.getRowCount();
        int pending = 0;
        int inProgress = 0;
        int resolved = 0;

        for (int i = 0; i < total; i++) {

            String status =
                    complaintModel.getValueAt(i, 6).toString();

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

    // ================= SAMPLE DATA =================

    private void addSampleData() {

        staffModel.addRow(new Object[] {
                "ST001", "Rahul", "Electrician", "9876543210"
        });

        staffModel.addRow(new Object[] {
                "ST002", "Aman", "Plumber", "9876543211"
        });

        studentModel.addRow(new Object[] {
                "S001", "Ayushi", "101", "9876543212"
        });

        complaintModel.addRow(new Object[] {
                "HC001", "Ayushi", "101", "Electricity",
                "Fan is not working", LocalDate.now().toString(),
                "Pending", "Not Assigned", "No update yet"
        });

        complaintModel.addRow(new Object[] {
                "HC002", "Riya", "205", "Plumbing",
                "Water tap is leaking", LocalDate.now().toString(),
                "In Progress", "Aman", "Repair work started"
        });

        complaintModel.addRow(new Object[] {
                "HC003", "Neha", "110", "Cleaning",
                "Room needs cleaning", LocalDate.now().toString(),
                "Resolved", "Rahul", "Work completed"
        });
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }

    // Allows this dashboard to be opened from another class.
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
