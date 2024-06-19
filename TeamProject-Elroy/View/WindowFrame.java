package View;

import javax.swing.*;
import javax.swing.border.Border;

import org.json.simple.JSONObject;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.util.Calendar;

import model.Project;
import model.ValidChecker;

/**
 * The WindowFrame class represents the main window of the application.
 * It provides a graphical user interface for various functionality.
 * @author Elroy
 */
public class WindowFrame {
    static JFrame window;
    private JButton profileButton;
    private JButton exportButton;
    private JButton projectButton;
    private JButton budgetButton;
    private JButton calendarButton;
    private JButton foldersButton;
    private Project project;
    public static boolean userInfoSet = false;
    private boolean calendarVisible = false;
    private CalendarUI Calendar = new CalendarUI();

    /**
     * Constructs a new instance of the WindowFrame class.
     */
    public WindowFrame() {
        window = new JFrame("Home Renovation Tool");
        project = new Project(this);
        profileButton = createIconButton("Profile", "maleprofileicon.png");
        exportButton = createIconButton("Export", "Export-06.png");
        projectButton = createIconButton("Project", "icons8-mac-folder-48.png");
        budgetButton = createIconButton("Budget", "budget.jpg");
        calendarButton = createIconButton("Calendar", "download.png");
        foldersButton= createIconButton("Folders", "folder.png");

        setupUI();
    }

    /**
     * Sets up the user interface of the window.
     */
    public void setupUI() {
        window.setSize(1200, 900);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);

        JPanel toolbarPanel = new JPanel();
        JPanel calendarPanel = Calendar.getPanel();
        calendarPanel.setVisible(calendarVisible);
        toolbarPanel.setLayout(new GridBagLayout());
        toolbarPanel.setBackground(Color.DARK_GRAY);

        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle profile button click
                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

                JLabel nameLabel = new JLabel("Username: ");
                JTextField nameField = new JTextField(UserInfo.getName(), 20);
                panel.add(nameLabel);
                panel.add(nameField);

                JLabel emailLabel = new JLabel("User Email: ");
                JTextField emailField = new JTextField(UserInfo.getEmail(), 20);
                panel.add(emailLabel);
                panel.add(emailField);

                // Additional text
                String additionalText = "\nDeveloped by Team Nullify\n";
                JLabel additionalLabel = new JLabel(additionalText);
                panel.add(additionalLabel);

                String additionalText1 = "\nDevelopers:\n";
                JLabel additionalLabel1 = new JLabel(additionalText1);
                panel.add(additionalLabel1);

                String additionalText2 = "Nate Mann\n";
                JLabel additionalLabel2 = new JLabel(additionalText2);
                panel.add(additionalLabel2);

                String additionalText3 = "Anthony Green\n";
                JLabel additionalLabel3 = new JLabel(additionalText3);
                panel.add(additionalLabel3);

                String additionalText4 = "Christopher Yuan\n";
                JLabel additionalLabel4 = new JLabel(additionalText4);
                panel.add(additionalLabel4);

                String additionalText5 = "Elroy Mbabazi\n";
                JLabel additionalLabel5 = new JLabel(additionalText5);
                panel.add(additionalLabel5);

                String additionalText6 = "Abdel Rahman Abudayyah";
                JLabel additionalLabel6 = new JLabel(additionalText6);
                panel.add(additionalLabel6);

                JButton logoutButton = new JButton("Log Out");
                logoutButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // For example, you can close the current panel and go back to the UserInfoInput frame
                        // Assuming the UserInfoInput frame is stored in a variable called 'userInfoInputFrame'
                        Window myWindow = SwingUtilities.getWindowAncestor(panel);
                        if (myWindow != null) {
                            myWindow.dispose();  // Dispose of the parent window
                        }
                        // userInfoInput.setVisible(true);
                        window.setVisible(false);
                        //new UserInfoInputFrame().setVisible(true);
                        new UserInfoInputFrame().create(); // Show the UserInfoInput frame
                        // window.setVisible(false);
                    }
                });

                panel.add(logoutButton);

                int result = JOptionPane.showConfirmDialog(null, panel, "About",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JFileChooser.APPROVE_OPTION) {
                    String username = nameField.getText();
                    String email = emailField.getText();
                    if (!ValidChecker.isValidUsername(username)) {
                        JOptionPane.showMessageDialog(null, "Invalid username. Please try again.");
                        return;
                    }
                    if (!ValidChecker.isValidEmail(email)) {
                        JOptionPane.showMessageDialog(null, "Invalid email address. Please try again.");
                        return;
                    }
                    UserInfo.setName(username);
                    UserInfo.setEmail(email);
                    JSONObject filesObject = createFilesObject();
                    UserInfo.storeUserInfo(filesObject);
                }
            }

        });
        customizeButton(profileButton);

        projectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle project button click
                window.setVisible(false);
                project.openProjectFrame();
            }
        });
        customizeButton(projectButton);

        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle export button click
                UserInfo.exportSettingsAndData();
            }
        });
        customizeButton(exportButton);

        budgetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle budget button click
                openBudgetPlanner();
            }
        });


        calendarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calendarVisible = !calendarVisible;
                calendarPanel.setVisible(calendarVisible);
            }
        });
        customizeButton(calendarButton);

        foldersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    FileFinder fileFinder=new FileFinder(View.UserInfo.getName());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        customizeButton(foldersButton);

        GridBagConstraints buttonGbc = new GridBagConstraints();
        buttonGbc.gridx = 0;
        buttonGbc.gridy = 0;
        buttonGbc.insets = new Insets(10, 0, 10, 10);
        buttonGbc.anchor = GridBagConstraints.NORTHWEST;
        toolbarPanel.add(profileButton, buttonGbc);

        buttonGbc.gridy = 1;
        toolbarPanel.add(exportButton, buttonGbc);

        buttonGbc.gridy = 2;
        toolbarPanel.add(projectButton, buttonGbc);

        buttonGbc.gridy = 3;
        toolbarPanel.add(budgetButton, buttonGbc);

        buttonGbc.gridy = 4;
        toolbarPanel.add(calendarButton, buttonGbc);

        buttonGbc.gridy=5;
        toolbarPanel.add(foldersButton,buttonGbc);

        window.setUndecorated(true);
        window.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
        window.setLayout(new BorderLayout());
        window.getContentPane().setBackground(new Color(169, 179, 189));
        window.getContentPane().add(toolbarPanel, BorderLayout.WEST);
        buttonGbc.gridx = 1;
        buttonGbc.gridy = 0;

        window.getContentPane().add(calendarPanel, BorderLayout.EAST);

        int cornerRadius = 20;
        window.setShape(new RoundRectangle2D.Double(0, 0, window.getWidth(), window.getHeight(), cornerRadius, cornerRadius));
    }

    protected JSONObject createFilesObject() {
        return null;
    }

    /**
     * Creates a JButton with an icon.
     *
     * @param tooltip   the tooltip for the button
     * @param iconName  the name of the icon file
     * @return          the created JButton
     */
    private JButton createIconButton(String tooltip, String iconName) {
        ImageIcon icon = new ImageIcon(iconName);
        Image image = icon.getImage();
        Image scaledImage = image.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(scaledImage);

        JButton button = new JButton(icon);
        button.setToolTipText(tooltip);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(40, 40));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);

        return button;
    }

    /**
     * Customizes the appearance of the button.
     *
     * @param button    the JButton to customize
     */
    private void customizeButton(JButton button) {
        button.setBackground(Color.DARK_GRAY);
        button.setForeground(Color.WHITE);

        Border emptyBorder = BorderFactory.createEmptyBorder();
        Border lineBorder = BorderFactory.createLineBorder(Color.DARK_GRAY, 1);
        Border compoundBorder = BorderFactory.createCompoundBorder(lineBorder, emptyBorder);
        button.setBorder(compoundBorder);

        button.setMargin(new Insets(5, 10, 5, 10));

        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.GRAY);
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.GRAY, 1),
                        emptyBorder
                ));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.DARK_GRAY);
                button.setBorder(compoundBorder);
            }
        });
    }

    /**
     * Opens the budget planner.
     */
    private void openBudgetPlanner() {
        BudgetPlannerFrame budgetPlanner = new BudgetPlannerFrame(this);
        budgetPlanner.startUp();
        window.dispose();
    }

    /**
     * Shows the window.
     */
    public void showWindow() {
        window.setVisible(true);
    }
}
