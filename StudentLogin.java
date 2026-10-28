
import javax.swing.*;
import java.awt.*;

public class StudentLogin {

    public StudentLogin() {

        JFrame frame = new JFrame("HostelCare - Student Login");
        frame.setSize(500, 550);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        // Dark-green outer border
        frame.getRootPane().setBorder(
                BorderFactory.createLineBorder(
                        new Color(20, 83, 45), 8)
        );

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(30, 50, 30, 50)
        );

        // HostelCare heading
        JLabel heading = new JLabel("HostelCare");
        heading.setFont(new Font("Arial", Font.BOLD, 30));
        heading.setForeground(new Color(20, 83, 45));

        // Logo
        ImageIcon logoIcon = new ImageIcon("hostel.png");
        JLabel iconLabel;

        if (logoIcon.getIconWidth() > 0) {
            Image logoImage = logoIcon.getImage()
                    .getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            iconLabel = new JLabel(new ImageIcon(logoImage));
        } else {
            iconLabel = new JLabel();
        }

        // Logo and heading in one row
        JPanel logoPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 0)
        );
        logoPanel.setBackground(Color.WHITE);
        logoPanel.add(iconLabel);
        logoPanel.add(heading);
        logoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoPanel.setMaximumSize(new Dimension(350, 40));

        // Subtitle
        JLabel subtitle = new JLabel("Student Login");
        subtitle.setFont(new Font("Arial", Font.BOLD, 20));
        subtitle.setForeground(new Color(22, 163, 74));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Welcome message
        JLabel welcome = new JLabel(
                "Sign in to manage your complaints"
        );
        welcome.setFont(new Font("Arial", Font.PLAIN, 14));
        welcome.setForeground(new Color(82, 115, 90));
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Form panel ensures consistent label and field alignment
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.setMaximumSize(new Dimension(340, 200));

        // Email label
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
        emailLabel.setForeground(new Color(20, 83, 45));
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Email field
        JTextField emailField = new JTextField();
        emailField.setFont(new Font("Arial", Font.PLAIN, 15));
        emailField.setPreferredSize(new Dimension(340, 40));
        emailField.setMinimumSize(new Dimension(340, 40));
        emailField.setMaximumSize(new Dimension(340, 40));
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailField.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(187, 220, 195)),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)
                )
        );

        // Password label
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordLabel.setForeground(new Color(20, 83, 45));
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Password field
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 15));
        passwordField.setPreferredSize(new Dimension(340, 40));
        passwordField.setMinimumSize(new Dimension(340, 40));
        passwordField.setMaximumSize(new Dimension(340, 40));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordField.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(187, 220, 195)),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)
                )
        );

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setBackground(new Color(22, 163, 74));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setPreferredSize(new Dimension(220, 45));
        loginButton.setMinimumSize(new Dimension(220, 45));
        loginButton.setMaximumSize(new Dimension(220, 45));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Back button
        JButton backButton = new JButton("Back to Role Selection");
        backButton.setFont(new Font("Arial", Font.PLAIN, 13));
        backButton.setForeground(new Color(20, 83, 45));
        backButton.setBackground(new Color(240, 253, 244));
        backButton.setFocusPainted(false);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setPreferredSize(new Dimension(220, 40));
        backButton.setMinimumSize(new Dimension(220, 40));
        backButton.setMaximumSize(new Dimension(220, 40));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add heading components
        mainPanel.add(logoPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(subtitle);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(welcome);
        mainPanel.add(Box.createVerticalStrut(25));

        // Add form components
        formPanel.add(emailLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(emailField);
        formPanel.add(Box.createVerticalStrut(18));

        formPanel.add(passwordLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(passwordField);

        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalStrut(25));

        // Add buttons
        mainPanel.add(loginButton);
        mainPanel.add(Box.createVerticalStrut(12));
        mainPanel.add(backButton);

        frame.add(mainPanel);

        // LOGIN ACTION
        loginButton.addActionListener(e -> {

            String email = emailField.getText().trim();
            String password =
                    new String(passwordField.getPassword());

            if (email.isEmpty() || password.isEmpty()) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Please enter both email and password.",
                        "Missing Information",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            int studentId = Database.getStudentId(email, password);

            if (studentId != -1) {

                StudentDashboard dashboard =
                        new StudentDashboard(studentId);

                dashboard.showDashboard();
                frame.dispose();

            } else {

                JOptionPane.showMessageDialog(
                        frame,
                        "Invalid email or password. "
                                + "Please check your student account.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // BACK ACTION
        backButton.addActionListener(e -> {
            frame.dispose();
            new RoleSelection();
        });

        // Press Enter in password field to log in
        passwordField.addActionListener(e -> loginButton.doClick());

        // Display window
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentLogin::new);
    }
}
