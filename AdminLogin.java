
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class AdminLogin {

    public AdminLogin() {

        JFrame frame = new JFrame("HostelCare - Admin Login");
        frame.setSize(500, 550);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Green border around the window
        frame.getRootPane().setBorder(
                BorderFactory.createLineBorder(
                        new Color(20, 83, 45), 8));

        // Main panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(240, 253, 244));
        panel.setBorder(
                BorderFactory.createEmptyBorder(35, 45, 35, 45));

        // Heading
        JLabel heading = new JLabel("HostelCare");
        heading.setFont(new Font("Arial", Font.BOLD, 30));
        heading.setForeground(new Color(20, 83, 45));
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Admin Login");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 18));
        subtitle.setForeground(new Color(82, 115, 90));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Email label and field
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField emailField = new JTextField();
        emailField.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 40));
        emailField.setFont(new Font("Arial", Font.PLAIN, 15));
        emailField.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(187, 220, 195)),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        // Password label and field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 40));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 15));
        passwordField.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(187, 220, 195)),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 15));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(new Color(22, 163, 74));
        loginButton.setFocusPainted(false);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 42));
        loginButton.setCursor(
                new Cursor(Cursor.HAND_CURSOR));

        // Back button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setFocusPainted(false);
        backButton.setCursor(
                new Cursor(Cursor.HAND_CURSOR));

        // Authenticate admin using SQLite
        loginButton.addActionListener(e -> {

            String email = emailField.getText().trim();
            String password =
                    new String(passwordField.getPassword());

            // Check empty fields
            if (email.isEmpty() || password.isEmpty()) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Please enter both email and password.",
                        "Missing Details",
                        JOptionPane.WARNING_MESSAGE);

                return;
            }

            try {

                // Check credentials in the admins table
                boolean valid =
                        Database.validateAdmin(email, password);

                if (valid) {

                    JOptionPane.showMessageDialog(
                            frame,
                            "Admin login successful!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Open the Admin Dashboard
                    new AdminDashboard();

                    // Close the login window
                    frame.dispose();

                } else {

                    JOptionPane.showMessageDialog(
                            frame,
                            "Invalid email or password.",
                            "Login Failed",
                            JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception ex) {

    JOptionPane.showMessageDialog(
            frame,
            "Login error: " + ex.getMessage(),
            "Login Error",
            JOptionPane.ERROR_MESSAGE);

    ex.printStackTrace();
}
        });

        // Return to role selection
        backButton.addActionListener(e -> {
            frame.dispose();
            new RoleSelection();
        });

        // Add components to the panel
        panel.add(heading);
        panel.add(Box.createVerticalStrut(8));
        panel.add(subtitle);
        panel.add(Box.createVerticalStrut(40));

        panel.add(emailLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(emailField);
        panel.add(Box.createVerticalStrut(20));

        panel.add(passwordLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(passwordField);
        panel.add(Box.createVerticalStrut(30));

        panel.add(loginButton);
        panel.add(Box.createVerticalStrut(15));
        panel.add(backButton);

        frame.add(panel);
        frame.setVisible(true);
    }
}
