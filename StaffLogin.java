
import javax.swing.*;
import java.awt.*;

public class StaffLogin {

    public StaffLogin() {

        JFrame frame = new JFrame("HostelCare - Staff Login");
        frame.setSize(500, 550);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        frame.getRootPane().setBorder(
                BorderFactory.createLineBorder(
                        new Color(20, 83, 45), 8));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(40, 50, 40, 50));

        JLabel heading = new JLabel("HostelCare");
        heading.setFont(new Font("Arial", Font.BOLD, 30));
        heading.setForeground(new Color(20, 83, 45));
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        ImageIcon logoIcon = new ImageIcon("hostel.png");
        JLabel iconLabel;

        if (logoIcon.getIconWidth() > 0) {
            Image logoImage = logoIcon.getImage()
                    .getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            iconLabel = new JLabel(new ImageIcon(logoImage));
        } else {
            iconLabel = new JLabel();
        }

        JPanel logoPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 0));
        logoPanel.setBackground(Color.WHITE);
        logoPanel.add(iconLabel);
        logoPanel.add(heading);
        logoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoPanel.setMaximumSize(new Dimension(300, 35));

        JLabel subtitle = new JLabel("Staff Login");
        subtitle.setFont(new Font("Arial", Font.BOLD, 20));
        subtitle.setForeground(new Color(22, 163, 74));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel welcome = new JLabel("Welcome back! Please log in.");
        welcome.setFont(new Font("Arial", Font.PLAIN, 14));
        welcome.setForeground(new Color(82, 115, 90));
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel emailLabel = new JLabel("Email or Username");
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField emailField = new JTextField();
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        emailField.setFont(new Font("Arial", Font.PLAIN, 15));

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 15));

        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setBackground(new Color(22, 163, 74));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        JButton backButton = new JButton("Back to Role Selection");
        backButton.setFont(new Font("Arial", Font.PLAIN, 13));
        backButton.setForeground(new Color(20, 83, 45));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginButton.addActionListener(e -> {

            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Please enter both email and password.",
                        "Missing Details",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Find the staff member's ID.
            int staffId = Database.getStaffId(email, password);

            if (staffId != -1) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Staff login successful!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                // Pass the authenticated staff ID to the dashboard.
                new StaffDashboard(staffId);
                frame.dispose();

            } else {

                JOptionPane.showMessageDialog(
                        frame,
                        "Invalid email or password. Please check your staff account.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> {
            frame.dispose();
            new RoleSelection();
        });

        mainPanel.add(logoPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(subtitle);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(welcome);
        mainPanel.add(Box.createVerticalStrut(35));

        mainPanel.add(emailLabel);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(emailField);
        mainPanel.add(Box.createVerticalStrut(20));

        mainPanel.add(passwordLabel);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(passwordField);
        mainPanel.add(Box.createVerticalStrut(30));

        mainPanel.add(loginButton);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(backButton);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StaffLogin::new);
    }
}
