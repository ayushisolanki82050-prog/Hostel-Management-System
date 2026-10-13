import javax.swing.*;
import java.awt.*;

public class LandingPage {

    public static void main(String[] args) {

        // Create Frames -Frames provide the window
        JFrame frame = new JFrame();// Frame created
        frame.setTitle("Hostel Complaint Management System");// Title
        frame.setSize(1200, 700);// SIze of the window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// Stop java program on clicking on Cross
        frame.setLocationRelativeTo(null);// window position to center

        // MAIN PANEL 
        JPanel mainPanel = new JPanel(new BorderLayout());
        // NAVBAR
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));//create JPanel
        navPanel.setBackground(new Color(20, 83, 45));  //sets Background
        navPanel.setPreferredSize(new Dimension(1200, 70));//sets size

        // LOGO TEXT
        JLabel logoText = new JLabel("HostelCare");// logo name
        logoText.setForeground(Color.WHITE);// font color of logo
        logoText.setFont(new Font("Arial", Font.BOLD, 30));// font family and the size
        logoText.setBorder(
                BorderFactory.createEmptyBorder(10, 0, 0, 0)); //provides extra spacing
        ImageIcon logoIcon = new ImageIcon("hostel.png");//load the image

        Image logoImage = logoIcon.getImage()
                .getScaledInstance(30, 30, Image.SCALE_SMOOTH); //size of image

        JLabel iconLabel = new JLabel(new ImageIcon(logoImage)); 
        iconLabel.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 0, 0));
        navPanel.add(iconLabel);//add the icon 
        navPanel.add(logoText);//add the text of logo

        // NAVBAR AT TOP
        mainPanel.add(navPanel, BorderLayout.NORTH);// fix it to the top

        // ADD MAIN PANEL TO FRAME
        JScrollPane scrollPane = new JScrollPane(mainPanel);  //makes the page scroolable
        scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        frame.add(scrollPane);

        // HERO SECTION
        JPanel heroPanel = new JPanel(new GridLayout(1, 2, 0, 0)); //Divides in two parts
        heroPanel.setBackground(new Color(240, 253, 244));//sets background
        mainPanel.add(heroPanel, BorderLayout.CENTER); //add to the center


        // LEFT SIDE OF HERO
        JPanel leftHero = new JPanel();
        leftHero.setLayout(new BoxLayout(leftHero, BoxLayout.Y_AXIS)); //Vertically space
        leftHero.setBackground(new Color(235, 245, 255));//Bcakground color

        // Padding: TOP, LEFT, BOTTOM, RIGHT
        leftHero.setBorder(
                BorderFactory.createEmptyBorder(30, 40, 25, 20));

        // WELCOME TEXT
        JLabel welcome = new JLabel("W E L C O M E   T O");
        welcome.setFont(new Font("Arial", Font.BOLD, 15));
        welcome.setForeground(new Color(82, 115, 90));

        // MAIN HEADING
        JLabel title1 = new JLabel("Hostel Complaint");
        title1.setFont(new Font("Arial", Font.BOLD, 32));
        title1.setForeground(new Color(20, 83, 45));

        JLabel title2 = new JLabel("Management System");
        title2.setFont(new Font("Arial", Font.BOLD, 32));
        title2.setForeground(new Color(20, 83, 45));

        // TAGLINE
        JLabel tagline = new JLabel("Report. Track. Resolve.");
        tagline.setFont(new Font("Arial", Font.BOLD, 23));
        tagline.setForeground(new Color(82, 115, 90));

        // DESCRIPTION
        JLabel description = new JLabel(
                "<html>Easily submit and track your hostel complaints<br>" +
                        "and get faster resolutions.</html>");
        description.setFont(new Font("Arial", Font.PLAIN, 17));
        description.setForeground(new Color(82, 115, 90));

        // GET STARTED BUTTON
        JButton getStarted = new JButton("Get Started  →");
        getStarted.addActionListener(e -> {
            new RoleSelection();
        });
        getStarted.setFont(new Font("Arial", Font.BOLD, 18));
        getStarted.setForeground(Color.WHITE);
        getStarted.setBackground(new Color(22, 163, 74));
        getStarted.setFocusPainted(false);
        getStarted.setBorderPainted(false);
        getStarted.setCursor(new Cursor(Cursor.HAND_CURSOR));
        getStarted.setAlignmentX(Component.LEFT_ALIGNMENT);
        getStarted.setPreferredSize(new Dimension(220, 50));
        getStarted.setMaximumSize(new Dimension(220, 50));

        leftHero.add(Box.createVerticalStrut(20));

        // ADD COMPONENTS IN ORDER
        leftHero.add(welcome);
        leftHero.add(Box.createVerticalStrut(10));

        leftHero.add(title1);
        leftHero.add(title2);
        leftHero.add(Box.createVerticalStrut(10));

        leftHero.add(tagline);
        leftHero.add(Box.createVerticalStrut(10));

        leftHero.add(description);
        leftHero.add(Box.createVerticalStrut(18));

        leftHero.add(getStarted);

        // ADD LEFT SIDE TO HERO
        heroPanel.add(leftHero);

        // RIGHT SIDE(IMAGE AREA)
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(null);
        rightPanel.setBackground(new Color(235, 245, 255));

        ImageIcon originalIcon = new ImageIcon("RightSide.png");

        Image resizedImage = originalIcon.getImage()
                .getScaledInstance(650, 400, Image.SCALE_SMOOTH);

        JLabel hostelImage = new JLabel(new ImageIcon(resizedImage));
        hostelImage.setHorizontalAlignment(SwingConstants.CENTER);

        rightPanel.add(hostelImage, BorderLayout.CENTER);
        heroPanel.add(rightPanel);

        // ================= HOW IT WORKS =================

        JPanel howWorks = new JPanel();
        howWorks.setLayout(new BoxLayout(howWorks, BoxLayout.Y_AXIS));
        howWorks.setBackground(Color.WHITE);
        howWorks.setBorder(
                BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // HEADING
        JLabel howTitle = new JLabel("How It Works");
        howTitle.setFont(new Font("Arial", Font.BOLD, 28));
        howTitle.setForeground(new Color(20, 83, 45));
        howTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // SUBTITLE
        JLabel howSubtitle = new JLabel(
                "A simple process to get your complaints resolved.");
        howSubtitle.setFont(new Font("Arial", Font.PLAIN, 15));
        howSubtitle.setForeground(new Color(90, 115, 140));
        howSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        howWorks.add(howTitle);
        howWorks.add(Box.createVerticalStrut(5));
        howWorks.add(howSubtitle);
        howWorks.add(Box.createVerticalStrut(20));

        // THREE STEP CARDS
        JPanel steps = new JPanel(new GridLayout(1, 3, 20, 0));
        steps.setBackground(Color.WHITE);

        steps.add(createStep(
                "1",
                "Submit Complaint",
                "<html><center>Students submit their hostel<br>"
                        + "complaints with relevant details.</center></html>"));

        steps.add(createStep(
                "2",
                "Assign Complaint",
                "<html><center>Admin assigns complaints<br>"
                        + "to the appropriate staff member.</center></html>"));

        steps.add(createStep(
                "3",
                "Resolve Issue",
                "<html><center>Staff resolves the issue<br>"
                        + "and updates its status.</center></html>"));

        howWorks.add(steps);

        // ================= COMBINE HERO AND HOW IT WORKS =================

        // Remove hero from mainPanel temporarily
        mainPanel.remove(heroPanel);

        // Put hero and How It Works vertically
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);

        centerPanel.add(heroPanel);
        centerPanel.add(howWorks);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        frame.setVisible(true);// Visibility

    }

    // ================= STEP CARD METHOD =================

    public static JPanel createStep(
            String number,
            String title,
            String description) {

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(240, 253, 244));
        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(187, 220, 195)),
                        BorderFactory.createEmptyBorder(15, 10, 15, 10)));

        JLabel numberLabel = new JLabel(number);
        numberLabel.setFont(new Font("Arial", Font.BOLD, 26));
        numberLabel.setForeground(new Color(22, 163, 74));
        numberLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 17));
        titleLabel.setForeground(new Color(20, 83, 45));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descriptionLabel = new JLabel(description, SwingConstants.CENTER);
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        descriptionLabel.setForeground(new Color(82, 115, 90));
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descriptionLabel.setMaximumSize(new Dimension(300, 40));
        card.add(numberLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(descriptionLabel);

        return card;
    }

}