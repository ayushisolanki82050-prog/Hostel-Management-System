
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class StudentDashboard {

    private JFrame frame;
    private JTextField roomField;
    private JComboBox<String> categoryBox;
    private JTextArea descriptionArea;
    private DefaultTableModel tableModel;

    // ID of the student who logged in
    private final int studentId;

    // COLOURS
    private final Color darkGreen = new Color(20, 83, 45);
    private final Color emeraldGreen = new Color(22, 163, 74);
    private final Color paleGreen = new Color(240, 253, 244);
    private final Color borderGreen = new Color(187, 220, 195);
    private final Color mutedGreen = new Color(82, 115, 90);

    public StudentDashboard(int studentId) {

        this.studentId = studentId;

        // FRAME
        frame = new JFrame("HostelCare - Student Dashboard");
        frame.setSize(1000, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // MAIN PANEL
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // ================= NAVBAR =================

        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setBackground(darkGreen);
        navPanel.setPreferredSize(new Dimension(1000, 70));
        navPanel.setBorder(
                BorderFactory.createEmptyBorder(5, 15, 5, 20)
        );

        JPanel logoPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 8, 8)
        );
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

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(new Color(185, 28, 28));
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setBorder(
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        );

        JPanel logoutPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT)
        );
        logoutPanel.setBackground(darkGreen);
        logoutPanel.add(logoutButton);

        navPanel.add(logoPanel, BorderLayout.WEST);
        navPanel.add(logoutPanel, BorderLayout.EAST);

        // ================= PAGE CONTENT =================

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(
                new BoxLayout(contentPanel, BoxLayout.Y_AXIS)
        );
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        );

        // WELCOME SECTION
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(
                new BoxLayout(welcomePanel, BoxLayout.Y_AXIS)
        );
        welcomePanel.setBackground(paleGreen);
        welcomePanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(borderGreen),
                        BorderFactory.createEmptyBorder(20, 20, 20, 20)
                )
        );
        welcomePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel welcomeTitle = new JLabel(
                "Welcome to Your Dashboard!"
        );
        welcomeTitle.setFont(new Font("Arial", Font.BOLD, 25));
        welcomeTitle.setForeground(darkGreen);

        JLabel welcomeDescription = new JLabel(
                "<html>Manage your hostel complaints easily from one place."
                + "<br>Submit maintenance requests and track their progress.</html>"
        );
        welcomeDescription.setFont(new Font("Arial", Font.PLAIN, 14));
        welcomeDescription.setForeground(mutedGreen);

        welcomePanel.add(welcomeTitle);
        welcomePanel.add(Box.createVerticalStrut(10));
        welcomePanel.add(welcomeDescription);

        // FORM HEADING
        JLabel formHeading = new JLabel("Submit a Complaint");
        formHeading.setFont(new Font("Arial", Font.BOLD, 21));
        formHeading.setForeground(darkGreen);
        formHeading.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ================= COMPLAINT FORM =================

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(borderGreen),
                        BorderFactory.createEmptyBorder(15, 18, 15, 18)
                )
        );
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 5, 7, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel roomLabel = new JLabel("Room Number");
        roomLabel.setFont(new Font("Arial", Font.BOLD, 14));
        roomLabel.setForeground(darkGreen);

        roomField = new JTextField(20);
        roomField.setFont(new Font("Arial", Font.PLAIN, 14));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        formPanel.add(roomLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(roomField, gbc);

        JLabel categoryLabel = new JLabel("Complaint Category");
        categoryLabel.setFont(new Font("Arial", Font.BOLD, 14));
        categoryLabel.setForeground(darkGreen);

        String[] categories = {
                "Electricity",
                "Plumbing",
                "Cleaning",
                "Furniture",
                "Internet / Wi-Fi",
                "Other"
        };

        categoryBox = new JComboBox<>(categories);
        categoryBox.setFont(new Font("Arial", Font.PLAIN, 14));

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        formPanel.add(categoryLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(categoryBox, gbc);

        JLabel descriptionLabel = new JLabel("Description");
        descriptionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        descriptionLabel.setForeground(darkGreen);

        descriptionArea = new JTextArea(4, 20);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(descriptionLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(descriptionScroll, gbc);

        JButton submitButton = new JButton("Submit Complaint");
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.setForeground(Color.WHITE);
        submitButton.setBackground(emeraldGreen);
        submitButton.setFocusPainted(false);
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 1;
        formPanel.add(submitButton, gbc);

        // ================= COMPLAINT HISTORY =================

        JLabel historyHeading = new JLabel("My Complaint History");
        historyHeading.setFont(new Font("Arial", Font.BOLD, 21));
        historyHeading.setForeground(darkGreen);
        historyHeading.setAlignmentX(Component.LEFT_ALIGNMENT);

        String[] columns = {
                "Complaint ID",
                "Room",
                "Category",
                "Description",
                "Date",
                "Status"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable historyTable = new JTable(tableModel);
        historyTable.setFont(new Font("Arial", Font.PLAIN, 12));
        historyTable.setRowHeight(28);
        historyTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        historyTable.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 12)
        );
        historyTable.getTableHeader().setBackground(paleGreen);
        historyTable.getTableHeader().setForeground(darkGreen);

        historyTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        historyTable.getColumnModel().getColumn(1).setPreferredWidth(65);
        historyTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        historyTable.getColumnModel().getColumn(3).setPreferredWidth(250);
        historyTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        historyTable.getColumnModel().getColumn(5).setPreferredWidth(100);

        JScrollPane historyScroll = new JScrollPane(historyTable);
        historyScroll.setPreferredSize(new Dimension(900, 180));
        historyScroll.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ADD CONTENT
        contentPanel.add(welcomePanel);
        contentPanel.add(Box.createVerticalStrut(22));
        contentPanel.add(formHeading);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(formPanel);
        contentPanel.add(Box.createVerticalStrut(25));
        contentPanel.add(historyHeading);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(historyScroll);

        JScrollPane pageScroll = new JScrollPane(contentPanel);
        pageScroll.setBorder(null);
        pageScroll.getVerticalScrollBar().setUnitIncrement(16);
        pageScroll.getViewport().setBackground(Color.WHITE);

        mainPanel.add(navPanel, BorderLayout.NORTH);
        mainPanel.add(pageScroll, BorderLayout.CENTER);
        frame.add(mainPanel);

        // ================= SUBMIT COMPLAINT =================

        submitButton.addActionListener(e -> {

            String room = roomField.getText().trim();
            String category = (String) categoryBox.getSelectedItem();
            String description = descriptionArea.getText().trim();

            if (room.isEmpty()) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Please enter your room number.",
                        "Missing Information",
                        JOptionPane.WARNING_MESSAGE
                );
                roomField.requestFocus();
                return;
            }

            if (description.isEmpty()) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Please describe your complaint.",
                        "Missing Information",
                        JOptionPane.WARNING_MESSAGE
                );
                descriptionArea.requestFocus();
                return;
            }

            String date = LocalDate.now().toString();

            int complaintId = Database.saveStudentComplaint(
                    studentId,
                    room,
                    category,
                    description,
                    date
            );

            if (complaintId != -1) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Complaint submitted successfully!\n"
                                + "Complaint ID: "
                                + String.format("HC%03d", complaintId)
                                + "\nStatus: Pending",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                roomField.setText("");
                categoryBox.setSelectedIndex(0);
                descriptionArea.setText("");

                loadComplaintHistory();

            } else {

                JOptionPane.showMessageDialog(
                        frame,
                        "Unable to save your complaint.\n"
                                + "Please check your database.",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // ================= LOGOUT ACTION =================

        logoutButton.addActionListener(e -> {

            int choice = JOptionPane.showConfirmDialog(
                    frame,
                    "Are you sure you want to log out?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (choice == JOptionPane.YES_OPTION) {
                frame.dispose();
                new RoleSelection();
            }
        });

        // Load only this student's complaints
        loadComplaintHistory();
    }

    // ================= LOAD THIS STUDENT'S HISTORY =================

    private void loadComplaintHistory() {

        tableModel.setRowCount(0);

        String sql = "SELECT id, room_number, category, description, "
                + "complaint_date, status "
                + "FROM complaints "
                + "WHERE student_id = ? "
                + "ORDER BY id DESC";

        try (Connection connection = Database.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, studentId);

            try (ResultSet result = statement.executeQuery()) {

                while (result.next()) {

                    int id = result.getInt("id");

                    tableModel.addRow(new Object[]{
                            String.format("HC%03d", id),
                            result.getString("room_number"),
                            result.getString("category"),
                            result.getString("description"),
                            result.getString("complaint_date"),
                            result.getString("status")
                    });
                }
            }

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                    frame,
                    "Unable to load your complaint history.\n"
                            + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            ex.printStackTrace();
        }
    }

    // ================= SHOW DASHBOARD =================

    public void showDashboard() {
        frame.setVisible(true);
    }

    // ================= MAIN METHOD =================

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // For testing, replace 1 with an existing student ID.
            StudentDashboard dashboard = new StudentDashboard(1);
            dashboard.showDashboard();
        });
    }
}
