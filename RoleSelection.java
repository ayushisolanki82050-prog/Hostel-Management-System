
import javax.swing.*;
import java.awt.*;

public class RoleSelection {

    public RoleSelection() {

        JFrame frame = new JFrame("HostelCare - Select Role");
        frame.setSize(700, 450);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getRootPane().setBorder(
    BorderFactory.createLineBorder(
        new Color(20, 83, 45), 8
    )
);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel heading = new JLabel("Welcome to HostelCare");
        heading.setFont(new Font("Arial", Font.BOLD, 28));
        heading.setForeground(new Color(20, 83, 45));
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Select your role to continue");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton studentButton = createButton("Student");
        studentButton.addActionListener(e -> {
          new StudentLogin();
});
        JButton adminButton = createButton("Admin");
             adminButton.addActionListener(e -> {
          new AdminLogin();
});
        JButton staffButton = createButton("Staff");
             staffButton.addActionListener(e -> {
          new StaffLogin();
});

        mainPanel.add(heading);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(subtitle);
        mainPanel.add(Box.createVerticalStrut(35));
        mainPanel.add(studentButton);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(adminButton);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(staffButton);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(22, 163, 74));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(250, 45));

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RoleSelection());
    }
}
