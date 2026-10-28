
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class StaffDashboard {

    private JFrame frame;
    private JTable taskTable;
    private DefaultTableModel taskModel;
    private final int staffId;

    private JLabel totalLabel;
    private JLabel pendingLabel;
    private JLabel progressLabel;
    private JLabel completedLabel;

    private final Color darkGreen = new Color(20, 83, 45);
    private final Color paleGreen = new Color(240, 253, 244);
    private final Color borderGreen = new Color(187, 220, 195);
    private final Color mutedGreen = new Color(82, 115, 90);

    public StaffDashboard(int staffId) {
        this.staffId = staffId;
        createDashboard();
    }

    private void createDashboard() {

        frame = new JFrame("HostelCare - Staff Dashboard");
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

        JLabel roleLabel = new JLabel("Staff Panel");
        roleLabel.setForeground(Color.WHITE);
        roleLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(new Color(185, 28, 28));
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setBorder(
                BorderFactory.createEmptyBorder(10, 20, 10, 20));

        rightPanel.add(roleLabel);
        rightPanel.add(logoutButton);

        navPanel.add(logoPanel, BorderLayout.WEST);
        navPanel.add(rightPanel, BorderLayout.EAST);

        // ================= PAGE CONTENT =================

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(
                BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setBackground(paleGreen);
        welcomePanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(borderGreen),
                        BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        welcomePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel welcomeTitle =
                new JLabel("Welcome to Staff Dashboard!");
        welcomeTitle.setFont(new Font("Arial", Font.BOLD, 25));
        welcomeTitle.setForeground(darkGreen);

        JLabel welcomeDescription = new JLabel(
                "<html>View your assigned hostel maintenance complaints."
                + "<br>Update work progress and keep the administration informed.</html>");
        welcomeDescription.setFont(new Font("Arial", Font.PLAIN, 14));
        welcomeDescription.setForeground(mutedGreen);

        welcomePanel.add(welcomeTitle);
        welcomePanel.add(Box.createVerticalStrut(10));
        welcomePanel.add(welcomeDescription);

        // ================= SUMMARY CARDS =================

        JPanel summaryPanel = new JPanel(new GridLayout(1, 4, 12, 12));
        summaryPanel.setBackground(Color.WHITE);
        summaryPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        summaryPanel.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 105));

        totalLabel = new JLabel("0");
        pendingLabel = new JLabel("0");
        progressLabel = new JLabel("0");
        completedLabel = new JLabel("0");

        summaryPanel.add(createCard("Total Tasks", totalLabel));
        summaryPanel.add(createCard("Pending", pendingLabel));
        summaryPanel.add(createCard("In Progress", progressLabel));
        summaryPanel.add(createCard("Completed", completedLabel));

        JLabel taskHeading = new JLabel("My Assigned Tasks");
        taskHeading.setFont(new Font("Arial", Font.BOLD, 21));
        taskHeading.setForeground(darkGreen);
        taskHeading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel taskPanel = createTaskPanel();
        taskPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPanel.add(welcomePanel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(summaryPanel);
        contentPanel.add(Box.createVerticalStrut(25));
        contentPanel.add(taskHeading);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(taskPanel);

        JScrollPane pageScroll = new JScrollPane(contentPanel);
        pageScroll.setBorder(null);
        pageScroll.getVerticalScrollBar().setUnitIncrement(16);
        pageScroll.getViewport().setBackground(Color.WHITE);

        mainPanel.add(navPanel, BorderLayout.NORTH);
        mainPanel.add(pageScroll, BorderLayout.CENTER);
        frame.add(mainPanel);

        // Load actual tasks from SQLite.
        loadTasks();

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

    // ================= TASK TABLE =================

    private JPanel createTaskPanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        String[] columns = {
                "Task ID", "Complaint ID", "Student", "Room",
                "Category", "Description", "Date", "Status", "Work Update"
        };

        taskModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        taskTable = new JTable(taskModel);
        styleTable(taskTable);

        // Keep the task ID in the model but hide it from the user.
        taskTable.removeColumn(taskTable.getColumnModel().getColumn(0));

        JScrollPane tableScroll = new JScrollPane(taskTable);
        tableScroll.setPreferredSize(new Dimension(1000, 300));

        panel.add(tableScroll, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 8, 8));
        buttonPanel.setBackground(Color.WHITE);

        JButton refreshButton = createButton("Refresh Tasks");
        JButton viewButton = createButton("View Details");
        JButton startButton = createButton("Start Work");
        JButton updateButton = createButton("Update Work");
        JButton completeButton = createButton("Mark Completed");

        refreshButton.addActionListener(e -> loadTasks());
        viewButton.addActionListener(e -> viewDetails());
        startButton.addActionListener(e -> startWork());
        updateButton.addActionListener(e -> updateWork());
        completeButton.addActionListener(e -> completeWork());

        buttonPanel.add(refreshButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(startButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(completeButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    // ================= LOAD ASSIGNED TASKS =================

    private void loadTasks() {

        taskModel.setRowCount(0);

        try {
            List<String[]> tasks = Database.getStaffTasks(staffId);

            for (String[] task : tasks) {
                taskModel.addRow(task);
            }

            updateSummary();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Could not load assigned tasks: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // ================= SELECTED ROW HELPERS =================

    private int getSelectedModelRow() {

        int viewRow = taskTable.getSelectedRow();

        if (viewRow == -1) {
            showMessage("Please select a task first.");
            return -1;
        }

        return taskTable.convertRowIndexToModel(viewRow);
    }

    private int getSelectedTaskId(int modelRow) {
        return Integer.parseInt(
                taskModel.getValueAt(modelRow, 0).toString());
    }

    private String getSelectedStatus(int modelRow) {
        return taskModel.getValueAt(modelRow, 7).toString();
    }

    private String getSelectedWorkUpdate(int modelRow) {
        return taskModel.getValueAt(modelRow, 8).toString();
    }

    // ================= VIEW DETAILS =================

    private void viewDetails() {

        int row = getSelectedModelRow();
        if (row == -1) {
            return;
        }

        String details =
                "Complaint ID: " + taskModel.getValueAt(row, 1)
                + "\nStudent: " + taskModel.getValueAt(row, 2)
                + "\nRoom: " + taskModel.getValueAt(row, 3)
                + "\nCategory: " + taskModel.getValueAt(row, 4)
                + "\nDescription: " + taskModel.getValueAt(row, 5)
                + "\nDate: " + taskModel.getValueAt(row, 6)
                + "\nStatus: " + taskModel.getValueAt(row, 7)
                + "\nWork Update: " + taskModel.getValueAt(row, 8);

        JOptionPane.showMessageDialog(
                frame, details, "Task Details",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // ================= START WORK =================

    private void startWork() {

        int row = getSelectedModelRow();
        if (row == -1) {
            return;
        }

        if ("Resolved".equalsIgnoreCase(getSelectedStatus(row))) {
            showMessage("This task is already completed.");
            return;
        }

        String oldUpdate = getSelectedWorkUpdate(row);
        String update = "Work started on " + LocalDate.now();

        if (oldUpdate != null
                && !oldUpdate.isBlank()
                && !oldUpdate.equals("Not started")) {
            update = oldUpdate + "\nWork started on " + LocalDate.now();
        }

        saveTaskUpdate(row, update, "In Progress");
    }

    // ================= UPDATE WORK =================

    private void updateWork() {

        int row = getSelectedModelRow();
        if (row == -1) {
            return;
        }

        if ("Resolved".equalsIgnoreCase(getSelectedStatus(row))) {
            showMessage("This task is already completed.");
            return;
        }

        JTextArea updateArea = new JTextArea(
                getSelectedWorkUpdate(row), 5, 25);
        updateArea.setLineWrap(true);
        updateArea.setWrapStyleWord(true);
        updateArea.setFont(new Font("Arial", Font.PLAIN, 14));

        int result = JOptionPane.showConfirmDialog(
                frame,
                new JScrollPane(updateArea),
                "Enter Work Update",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {

            String update = updateArea.getText().trim();

            if (update.isEmpty()) {
                showMessage("Please enter a work update.");
                return;
            }

            String status = getSelectedStatus(row);
            if ("Pending".equalsIgnoreCase(status)) {
                status = "In Progress";
            }

            saveTaskUpdate(row, update, status);
        }
    }

    // ================= COMPLETE TASK =================

    private void completeWork() {

        int row = getSelectedModelRow();
        if (row == -1) {
            return;
        }

        if ("Resolved".equalsIgnoreCase(getSelectedStatus(row))) {
            showMessage("This task is already completed.");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                frame,
                "Have you completed this maintenance task?",
                "Confirm Completion",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {

            String oldUpdate = getSelectedWorkUpdate(row);
            String update = "Work completed on " + LocalDate.now();

            if (oldUpdate != null
                    && !oldUpdate.isBlank()
                    && !oldUpdate.equals("Not started")) {
                update = oldUpdate + "\n" + update;
            }

            saveTaskUpdate(row, update, "Resolved");
        }
    }

    // ================= SAVE TO DATABASE =================

    private void saveTaskUpdate(int row, String workUpdate, String status) {

        int taskId = getSelectedTaskId(row);

        try {
            boolean saved = Database.updateStaffTask(
                    taskId, staffId, workUpdate, status);

            if (saved) {
                loadTasks();
                showMessage("Task updated successfully.");
            } else {
                showMessage(
                        "Task could not be updated. Refresh and check the assignment.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Could not save the update: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // ================= TABLE DESIGN =================

    private void styleTable(JTable table) {

        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(30);
        table.setGridColor(borderGreen);
        table.setSelectionBackground(paleGreen);
        table.setSelectionForeground(darkGreen);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        table.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(paleGreen);
        table.getTableHeader().setForeground(darkGreen);
        table.getTableHeader().setReorderingAllowed(false);

        int[] widths = {
                70, 100, 100, 65, 120, 220, 100, 100, 230
        };

        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i)
                    .setPreferredWidth(widths[i]);
        }
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

        int total = taskModel.getRowCount();
        int pending = 0;
        int inProgress = 0;
        int completed = 0;

        for (int i = 0; i < total; i++) {
            String status = taskModel.getValueAt(i, 7).toString();

            if ("Pending".equalsIgnoreCase(status)) {
                pending++;
            } else if ("In Progress".equalsIgnoreCase(status)) {
                inProgress++;
            } else if ("Resolved".equalsIgnoreCase(status)
                    || "Completed".equalsIgnoreCase(status)) {
                completed++;
            }
        }

        totalLabel.setText(String.valueOf(total));
        pendingLabel.setText(String.valueOf(pending));
        progressLabel.setText(String.valueOf(inProgress));
        completedLabel.setText(String.valueOf(completed));
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }

    public void showDashboard() {
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(
                        null,
                        "Open Staff Login from Role Selection to access your assigned tasks."));
    }
}
