
package View;

import javax.swing.*;
import javax.swing.border.Border;

import org.json.simple.JSONObject;

import model.ValidChecker;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


/**
 * Represents a frame for user information input.
 * @author Elroy
 */
public class UserInfoInputFrame extends JFrame {
     /**
     * Indicates whether the user information has been set.
     */
    public static boolean userInfoSet;

    /**
     * Button for signing up.
     */
    private JButton signUpButton;

    /**
     * Text field for name input.
     */
    public JTextField nameInput = new JTextField();

    /**
     * Text field for email input.
     */
    public JTextField emailInput = new JTextField();

    /**
     * Color constant for the background color.
     */
    public Color jetStream = new Color(169, 179, 189);

    /**
     * Creates a new instance of UserInfoInputFrame.
     */
    public UserInfoInputFrame() {
        create();
    }

    /**
     * Creates the user information input frame.
     */
    public void create() {
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        setBackground(null);

        getContentPane().setLayout(null);
        getContentPane().setBackground(jetStream);

        JLabel label = new JLabel();
        ImageIcon icon = new ImageIcon("appface2.jpg");
        
        Image image = icon.getImage();
        Image scaledImage = image.getScaledInstance(250, 400, java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(scaledImage);
        label.setIcon(icon);
        Dimension size = label.getPreferredSize();
        label.setBounds(0, 0, 250, size.height);

        getContentPane().add(label);

        // App Name
        JLabel appName = new JLabel("BuildPlan");
        Font font = new Font("SansSerif", Font.BOLD, 18);
        appName.setFont(font);
        appName.setBounds(320, 28, 200, 20);

        // Welcome message
        JLabel welcomeMsg = new JLabel("Welcome to BuildPlan");
        Font font1 = new Font("SFPro", Font.ITALIC, 13);
        welcomeMsg.setFont(font1);
        welcomeMsg.setBounds(290, 60, 200, 20);

        JLabel nameLabel = new JLabel("Username:");
        nameLabel.setBounds(270, 88, 70, 20);
        nameInput.setBounds(270, 107, 193, 28);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(270, 140, 100, 20);
        emailInput.setBounds(270, 160, 193, 28);

        JButton submitButton = new JButton("Sign In");
        submitButton.setBounds(310, 200, 100, 25);
        submitButton.setBorderPainted(false);
        submitButton.setFocusPainted(false);
        submitButton.setContentAreaFilled(false);
        submitButton.setOpaque(true);
        submitButton.setBackground(new Color(220, 220, 220));
        submitButton.setForeground(Color.BLACK);
        submitButton.setFont(submitButton.getFont().deriveFont(Font.PLAIN));
        submitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent theEvent) {
                String username = nameInput.getText();
                String email = emailInput.getText();

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
                // Store user info
                JSONObject filesObject = new JSONObject();
                UserInfo.storeUserInfo(filesObject);
                userInfoSet = true;
                dispose();
                try{
                new WindowFrame().setupUI();
                }catch(NullPointerException e){
                    new WindowFrame().setupUI();
                }
                new WindowFrame().showWindow();
            }
        });

        // GitHub Button
        JButton githubButton = createIconButton("GitHub", "GitHub.png");
        githubButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String githubUrl = "https://github.com/TeamNullify/TeamProject";
                try {
                    Desktop.getDesktop().browse(java.net.URI.create(githubUrl));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        customizeButton(githubButton);

        JLabel createAccMsg = new JLabel("Don't have an account yet?");
        Font font2 = new Font("SFPro", Font.ITALIC, 13);
        createAccMsg.setFont(font2);
        createAccMsg.setBounds(280, 240, 200, 20);
        add(createAccMsg);

        initializeSignUpButton();
        signUpButton.setBounds(310, 260, 100, 25);
        githubButton.setBounds(300, 300,20,20);

        add(appName);
        add(welcomeMsg);
        add(nameLabel);
        add(nameInput);
        add(emailLabel);
        add(emailInput);
        add(submitButton);
        add(githubButton);

        getContentPane().add(label);
        getContentPane().add(appName);
        getContentPane().add(welcomeMsg);
        getContentPane().add(nameLabel);
        getContentPane().add(nameInput);
        getContentPane().add(emailLabel);
        getContentPane().add(emailInput);
        getContentPane().add(submitButton);
        getContentPane().add(githubButton);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

     /**
     * Creates an icon button with the specified tooltip and image path.
     *
     * @param tooltip    The tooltip text for the button.
     * @param imagePath  The path of the image to be displayed on the button.
     * @return The created icon button.
     */
    private JButton createIconButton(String tooltip, String imagePath) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image image = icon.getImage();
        Image scaledImage = image.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(scaledImage);
        JButton button = new JButton(icon);
        button.setToolTipText(tooltip);
        return button;
    }

    /**
     * Customizes the specified button.
     *
     * @param button  The button to be customized.
     */
    private void customizeButton(JButton button) {
        // Set background and foreground colors
        button.setBackground(Color.DARK_GRAY);
        button.setForeground(Color.WHITE);

        // Set button border
        Border emptyBorder = BorderFactory.createEmptyBorder();
        Border lineBorder = BorderFactory.createLineBorder(Color.DARK_GRAY, 1);
        Border compoundBorder = BorderFactory.createCompoundBorder(lineBorder, emptyBorder);
        button.setBorder(compoundBorder);

        // Set button insets
        button.setMargin(new Insets(5, 10, 5, 10));

        // Set cursor on hover
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Add hover effect
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
     * Initializes the sign-up button.
     */
    private void initializeSignUpButton() {
        signUpButton = new JButton("Sign Up");
        customizeButton(signUpButton);

        // Add ActionListener to handle sign-up button click
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a new frame to request name, email, and password
                JFrame signUpFrame = new JFrame("Sign Up");
                signUpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                signUpFrame.setSize(400, 200);
                signUpFrame.setLayout(null);

                // Create name label and text field
                JLabel nameLabel = new JLabel("Name:");
                nameLabel.setBounds(20, 20, 80, 25);
                JTextField nameField = new JTextField();
                nameField.setBounds(100, 20, 260, 25);

                // Create email label and text field
                JLabel emailLabel = new JLabel("Email:");
                emailLabel.setBounds(20, 50, 80, 25);
                JTextField emailField = new JTextField();
                emailField.setBounds(100, 50, 260, 25);

                // Create password label and text field
                JLabel passwordLabel = new JLabel("Password:");
                passwordLabel.setBounds(20, 80, 80, 25);
                JPasswordField passwordField = new JPasswordField();
                passwordField.setBounds(100, 80, 260, 25);

                // Create back button
                JButton backButton = new JButton("Back");
                backButton.setBounds(100, 120, 80, 25);
                backButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        signUpFrame.dispose();
                        setVisible(true);
                    }
                });

                // Create sign-up button
                JButton submitButton = new JButton("Submit");
                submitButton.setBounds(180, 120, 80, 25);
                submitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String name = nameField.getText();
                        String email = emailField.getText();
                        String password = new String(passwordField.getPassword());
                        UserInfo.setName(name);
                        UserInfo.setEmail(email);

                        // Create a JSONObject to store user info
                        JSONObject userObject = new JSONObject();
                        userObject.put("name", UserInfo.getName());
                        userObject.put("email", UserInfo.getEmail());
                        userObject.put("password", password);

                        // Store user info in JSON file
                        UserInfo.storeUserInfo(userObject);
                        signUpFrame.dispose();
                        new WindowFrame().showWindow();
                    }
                });

                // Add components to the sign-up frame
                signUpFrame.add(nameLabel);
                signUpFrame.add(nameField);
                signUpFrame.add(emailLabel);
                signUpFrame.add(emailField);
                signUpFrame.add(passwordLabel);
                signUpFrame.add(passwordField);
                signUpFrame.add(backButton);
                signUpFrame.add(submitButton);

                signUpFrame.setVisible(true);
                setVisible(false);
            }
        });
        add(signUpButton);
    }
}

