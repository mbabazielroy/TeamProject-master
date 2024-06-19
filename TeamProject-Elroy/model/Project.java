package model;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import View.UserInfo;
import View.WindowFrame;

/**
 * The {@code Project} class represents a project with file management functionality.
 * It allows the user to load, save, and display files in a graphical user interface.
 * @author Elroy
 */
public class Project {
    private static final String JSON_FILE_EXTENSION = ".json";
    private JPanel filePanel;
    public static JFrame projectFrame = new JFrame("Project");
    public JTextArea fileTextArea;
    private WindowFrame window;
    private static final int LIST_VIEW = 1;
    private static final int GRID_VIEW = 2;
    public JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    public Color jetStream = new Color(169, 179, 189);
    private int currentViewOption = LIST_VIEW;

    /**
     * Constructs a {@code Project} object with the specified window frame.
     *
     * @param window the window frame associated with the project
     */
    public Project(WindowFrame window) {
        this.window = window;
        //this.folderFrame = folderFrame;
    }


    /**
     * Opens the project frame and initializes the graphical user interface.
     */
    public void openProjectFrame() {
        toolbarPanel.removeAll();
        projectFrame.setSize(1200, 900);
        projectFrame.setLocationRelativeTo(null);

        JButton loadButton = new JButton("Load Project");
        JButton saveButton = new JButton("Save Project");
        JButton backButton = new JButton("Back");
        JButton createFolderButton = new JButton("Create Folder");

        loadButton.addActionListener(e -> loadFile());
        saveButton.addActionListener(e -> saveFile());
        backButton.addActionListener(e -> {
            projectFrame.setVisible(false);
            window.showWindow();
        });
        createFolderButton.addActionListener(e -> createFolder());

        // JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        toolbarPanel.add(loadButton);
        toolbarPanel.add(saveButton);
        toolbarPanel.add(createFolderButton);
        toolbarPanel.add(backButton);

        filePanel = new JPanel();
        filePanel.setBackground(jetStream);
        filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.Y_AXIS));
        fileTextArea = new JTextArea(10, 30);
        JScrollPane scrollPane = new JScrollPane(fileTextArea);
        filePanel.add(scrollPane);

        projectFrame.add(toolbarPanel, BorderLayout.SOUTH);
        projectFrame.add(filePanel, BorderLayout.CENTER);

        projectFrame.setVisible(true);

        displayFiles(fileTextArea);
        createViewOptions();
    }

     /**
     * Displays the files in the project on the specified text area.
     *
     * @param fileTextArea the text area to display the files on
     */
    public void displayFiles(JTextArea fileTextArea) {
        JSONParser parser = new JSONParser();

        try (FileReader fileReader = new FileReader(UserInfo.JSON_FILE_PATH)) {
            Object obj = parser.parse(fileReader);
            if (obj instanceof JSONArray) {
                JSONArray projectArray = (JSONArray) obj;
                if (!projectArray.isEmpty()) {
                    projectArray.sort((a, b) -> {
                        String fileNameA = (String) ((JSONObject) a).get("fileName");
                        String fileNameB = (String) ((JSONObject) b).get("fileName");
                        if (fileNameA != null && fileNameB != null) {
                            return fileNameA.compareToIgnoreCase(fileNameB);
                        } else {
                            return 0;
                        }
                    });

                    // Clear the existing components from the filePanel
                    filePanel.removeAll();

                    // Create a new panel based on the current view option
                    JPanel contentPanel = null;
                    if (currentViewOption == LIST_VIEW) {
                        contentPanel = new JPanel(new GridLayout(projectArray.size(), 1, 10, 10));
                    } else if (currentViewOption == GRID_VIEW) {
                        contentPanel = new JPanel(new GridLayout(0, 4, 10, 10));
                    }

                    for (Object projectObj : projectArray) {
                        if (projectObj instanceof JSONObject) {
                            JSONObject projectJson = (JSONObject) projectObj;
                            String fileName = (String) projectJson.get("fileName");
                            String filePath = (String) projectJson.get("filePath");
                            if (fileName != null && filePath != null) {
                                if (isFolder(filePath)) {
                                    JButton folderButton = createFolderButton(fileName + " (Folder)");
                                    folderButton.addActionListener(e -> openFolder(filePath));

                                    JButton deleteButton = new JButton("Delete");
                                    deleteButton.addActionListener(e -> deleteFile(fileName, filePath));

                                    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                                    buttonPanel.add(deleteButton);

                                    JPanel folderPanel = new JPanel(new BorderLayout());
                                    folderPanel.add(createMediumButton(folderButton), BorderLayout.CENTER);
                                    folderPanel.add(buttonPanel, BorderLayout.SOUTH);

                                    contentPanel.add(folderPanel);
                                } else {
                                    String fileExtension = getFileExtension(fileName);
                                    Icon fileIcon = getFileIcon(fileExtension);
                                    JButton fileButton = createFileButton(fileName, fileIcon);
                                    fileButton.addActionListener(e -> openFile(filePath));

                                    JButton deleteButton = new JButton("Delete");
                                    deleteButton.addActionListener(e -> deleteFile(fileName, filePath));

                                    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                                    buttonPanel.add(deleteButton);

                                    JPanel filePanel = new JPanel(new BorderLayout());
                                    filePanel.add(createMediumButton(fileButton), BorderLayout.CENTER);
                                    filePanel.add(buttonPanel, BorderLayout.SOUTH);

                                    contentPanel.add(filePanel);
                                }
                            }
                        }
                    }

                    JScrollPane scrollPane = new JScrollPane(contentPanel);

                    // Add the scroll pane to the filePanel
                    filePanel.add(scrollPane);

                    // Revalidate the filePanel to update the layout
                    filePanel.revalidate();
                } else {
                    fileTextArea.setText("No projects available.");
                }
            } else {
                fileTextArea.setText("No projects available.");
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the icon associated with a specific file extension.
     *
     * @param fileExtension The file extension for which to retrieve the icon.
     * @return The icon associated with the file extension, or null if not found.
     */
    private Icon getFileIcon(String fileExtension) {
        FileSystemView fileSystemView = FileSystemView.getFileSystemView();
        File tempFile = null;
        try {
            tempFile = File.createTempFile("temp", fileExtension);
            return fileSystemView.getSystemIcon(tempFile);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (tempFile != null) {
                tempFile.delete();
            }
        }
        return null;
    }

    /**
     * Creates a button with a folder icon.
     *
     * @param buttonText The text to display on the button.
     * @return The created folder button.
     */
    private JButton createFolderButton(String buttonText) {
        JButton folderButton = new JButton(buttonText);

        // Set custom folder icon
        Icon folderIcon = UIManager.getIcon("FileView.directoryIcon");
        folderButton.setIcon(folderIcon);

        // Set button appearance
        folderButton.setBorderPainted(false);
        folderButton.setFocusPainted(false);
        folderButton.setContentAreaFilled(false);
        folderButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add mouse listener to highlight the folder button on hover
        folderButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                folderButton.setContentAreaFilled(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                folderButton.setContentAreaFilled(false);
            }
        });

        return folderButton;
    }

    /**
     * Creates a button with a file icon.
     *
     * @param buttonText The text to display on the button.
     * @param icon       The icon to display on the button.
     * @return The created file button.
     */
    private JButton createFileButton(String buttonText, Icon icon) {
        JButton fileButton = new JButton(buttonText);

        // Set custom file icon
        fileButton.setIcon(icon);

        // Set button appearance
        fileButton.setBorderPainted(false);
        fileButton.setFocusPainted(false);
        fileButton.setContentAreaFilled(false);
        fileButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add mouse listener to highlight the file button on hover
        fileButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                fileButton.setContentAreaFilled(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                fileButton.setContentAreaFilled(false);
            }
        });

        return fileButton;
    }

    /**
     * Creates a medium-sized button.
     *
     * @param button The button to resize.
     * @return The resized button.
     */
    private JButton createMediumButton(JButton button) {
        // Set medium tile size
        Dimension buttonSize = new Dimension(20, 20);
        button.setPreferredSize(buttonSize);
        button.setMaximumSize(buttonSize);
        button.setMinimumSize(buttonSize);

        return button;
    }


    /**
     * Creates the view options panel and button.
     */
    public void createViewOptions() {
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton viewOptionsButton = new JButton("View Options");

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem listViewItem = new JMenuItem("List View");
        JMenuItem gridViewItem = new JMenuItem("Grid View");

        listViewItem.addActionListener(e -> {
            currentViewOption = LIST_VIEW;
            displayFiles(fileTextArea);
        });

        gridViewItem.addActionListener(e -> {
            currentViewOption = GRID_VIEW;
            displayFiles(fileTextArea);
        });

        popupMenu.add(listViewItem);
        popupMenu.add(gridViewItem);

        viewOptionsButton.addActionListener(e -> popupMenu.show(viewOptionsButton, 0, viewOptionsButton.getHeight()));

        optionsPanel.add(viewOptionsButton);

        // Add optionsPanel to your main UI frame or panel
        projectFrame.add(optionsPanel, BorderLayout.NORTH);
    }

    /**
     * Checks if a given path corresponds to a folder.
     *
     * @param filePath The path to check.
     * @return true if the path corresponds to a folder, false otherwise.
     */
    private boolean isFolder(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.isDirectory();
    }

    /**
     * Checks if a given path corresponds to a file.
     *
     * @param filePath The path to check.
     * @return true if the path corresponds to a file, false otherwise.
     */
    public void openFile(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            String fileExtension = getFileExtension(file.getName());

            if (fileExtension != null) {
                switch (fileExtension) {
                    case "txt":
                        openTextFile(file);
                        break;
                    case "docx":
                        openFileWithDefaultApplication(file);
                        break;
                    case "xlsx":
                        openFileWithDefaultApplication(file);
                        break;
                    case "pptx":
                        openFileWithDefaultApplication(file);
                        break;
                    case "pdf":
                        openFileWithDefaultApplication(file);
                        break;
                    case "png":
                        openFileWithDefaultApplication(file);
                        break;
                    case "jpg":
                        openFileWithDefaultApplication(file);
                        break;
                    case "jpeg":
                        openFileWithDefaultApplication(file);
                        break;
                    case "gif":
                        openFileWithDefaultApplication(file);
                        break;
                    case "java":
                        openTextFile(file);
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Unsupported file format: " + fileExtension);
                        break;
                }
            } else {
                JOptionPane.showMessageDialog(null, "Unknown file extension.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "File not found: " + filePath);
        }
    }

    /**
     * Opens a file with the default application.
     *
     * @param file The file to open.
     */
    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return null;
    }

        /**
     * Opens a text file and displays its content in a separate frame.
     *
     * @param file The text file to open.
     */
    private void openTextFile(File file) {
        try (FileReader fileReader = new FileReader(file)) {
            StringBuilder fileContent = new StringBuilder();
            int data;
            while ((data = fileReader.read()) != -1) {
                fileContent.append((char) data);
            }

            JTextArea textArea = new JTextArea(fileContent.toString());
            JScrollPane scrollPane = new JScrollPane(textArea);

            JButton saveButton = new JButton("Save");
            saveButton.addActionListener(e -> saveTextFile(file, textArea.getText()));

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.add(saveButton);

            JPanel contentPanel = new JPanel(new BorderLayout());
            contentPanel.add(scrollPane, BorderLayout.CENTER);
            contentPanel.add(buttonPanel, BorderLayout.SOUTH);

            JFrame frame = new JFrame(file.getName());
            frame.setSize(400, 300);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.getContentPane().add(contentPanel);
            frame.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Saves the content of a text file.
     *
     * @param file The text file to save.
     * @param content The new content to write to the file.
     */
    private void saveTextFile(File file, String content) {
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(content);
            fileWriter.flush();
            JOptionPane.showMessageDialog(null, "File saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a file with the default application.
     *
     * @param file The file to open.
     */
    private void openFileWithDefaultApplication(File file) {
        try {
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a file and adds it to the project list.
     */
    public void loadFile() {
        JSONParser parser = new JSONParser();
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(UserInfo.JSON_FILE_PATH);
            Object obj = parser.parse(fileReader);
            JSONArray projectArray = (JSONArray) obj;
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String selectedFilePath = selectedFile.getAbsolutePath();
                String selectedFileName = selectedFile.getName();
                boolean fileIsFolder = isFolder(selectedFilePath);

                if (!fileIsFolder && !selectedFilePath.equals(UserInfo.JSON_FILE_PATH)) {
                    System.out.println("Selected file: " + selectedFilePath);
                    displayFiles(fileTextArea);
                } else if (fileIsFolder) {
                    JOptionPane.showMessageDialog(null, "Selected item is a folder. Please select a file.");
                } else if (selectedFilePath.equals(UserInfo.JSON_FILE_PATH)) {
                    JOptionPane.showMessageDialog(null, "Cannot select the project file itself. Please select another file.");
                }

                boolean fileExists = false;
                for (Object projectObj : projectArray) {
                    if (projectObj instanceof JSONObject) {
                        JSONObject projectJson = (JSONObject) projectObj;
                        String fileName = (String) projectJson.get("fileName");
                        if (fileName != null && selectedFileName.equals(fileName)) {
                            fileExists = true;
                            break;
                        }
                    }
                }

                if (!fileExists && !fileIsFolder && !selectedFilePath.equals(UserInfo.JSON_FILE_PATH)) {
                    JSONObject projectJson = new JSONObject();
                    projectJson.put("fileName", selectedFileName);
                    projectJson.put("filePath", selectedFilePath);
                    projectArray.add(projectJson);

                    try (FileWriter fileWriter = new FileWriter(UserInfo.JSON_FILE_PATH)) {
                        fileWriter.write(projectArray.toJSONString());
                        fileWriter.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (fileIsFolder || selectedFilePath.equals(UserInfo.JSON_FILE_PATH)) {
                    // Do nothing, as the file selected is not valid for adding to the project list.
                } else {
                    System.out.println("File with the same name already exists.");
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Saves the project list to a file.
     */
    public void saveFile() {
        JSONParser parser = new JSONParser();
        JSONArray projectArray;
        boolean fileExists = false;
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String fileName = selectedFile.getName();
            if (!fileName.endsWith(JSON_FILE_EXTENSION)) {
                JOptionPane.showMessageDialog(null, "Invalid file extension. Please save with a '.json' extension.");
                return;
            }

            FileReader fileReader = null;
            FileWriter fileWriter = null;
            try {
                fileReader = new FileReader(UserInfo.JSON_FILE_PATH);
                Object obj = parser.parse(fileReader);
                projectArray = (JSONArray) obj;

                for (Object projectObj : projectArray) {
                    if (projectObj instanceof JSONObject) {
                        JSONObject projectJson = (JSONObject) projectObj;
                        String existingFileName = (String) projectJson.get("fileName");
                        if (existingFileName != null && fileName.equals(existingFileName)) {
                            fileExists = true;
                            break;
                        }
                    }
                }

                if (!fileExists) {
                    JSONObject projectJson = new JSONObject();
                    projectJson.put("fileName", fileName);
                    projectJson.put("filePath", selectedFile.getAbsolutePath());
                    projectArray.add(projectJson);

                    fileWriter = new FileWriter(UserInfo.JSON_FILE_PATH);
                    fileWriter.write(projectArray.toJSONString());
                    fileWriter.flush();
                    JOptionPane.showMessageDialog(null, "Project saved successfully.");

                    displayFiles(fileTextArea);
                } else {
                    JOptionPane.showMessageDialog(null, "Project with the same name already exists.");
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            } finally {
                if (fileReader != null) {
                    try {
                        fileReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fileWriter != null) {
                    try {
                        fileWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Deletes a file from the project list.
     */
    private void deleteFile(String fileName, String filePath) {
        JSONParser parser = new JSONParser();
        try (FileReader fileReader = new FileReader(UserInfo.JSON_FILE_PATH)) {
            Object obj = parser.parse(fileReader);
            JSONArray projectArray = (JSONArray) obj;

            // Find the JSON object corresponding to the selected file
            for (Object projectObj : projectArray) {
                if (projectObj instanceof JSONObject) {
                    JSONObject projectJson = (JSONObject) projectObj;
                    String existingFileName = (String) projectJson.get("fileName");
                    String existingFilePath = (String) projectJson.get("filePath");
                    if (existingFileName != null && existingFilePath != null && fileName.equals(existingFileName) && filePath.equals(existingFilePath)) {
                        // Remove the JSON object from the array
                        projectArray.remove(projectObj);
                        break;
                    }
                }
            }

            // Write the updated JSON array to the file
            try (FileWriter fileWriter = new FileWriter(UserInfo.JSON_FILE_PATH)) {
                fileWriter.write(projectArray.toJSONString());
                fileWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Refresh the display of files
            displayFiles(fileTextArea);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new folder and adds it to the project list.
     */
    public void createFolder() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnValue = fileChooser.showDialog(null, "Create Folder");
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String folderName = selectedFile.getName();
            String folderPath = selectedFile.getAbsolutePath();

            if (isFolder(folderPath)) {
                JOptionPane.showMessageDialog(null, "Folder with the same name already exists.");
            } else {
                if (selectedFile.mkdir()) {
                    JSONObject projectJson = new JSONObject();
                    projectJson.put("fileName", folderName );
                    projectJson.put("filePath", folderPath);
                    saveFolderToJSON(projectJson);
                    displayFiles(fileTextArea);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to create folder.");
                }
            }
        }
    }

    /**
     * Saves the information of a newly created folder to the project list.
     *
     * @param folderJson The JSON object representing the folder.
     */
    private void saveFolderToJSON(JSONObject folderJson) {
        JSONParser parser = new JSONParser();
        JSONArray projectArray;
        boolean fileExists = false;

        FileReader fileReader = null;
        FileWriter fileWriter = null;
        try {
            fileReader = new FileReader(UserInfo.JSON_FILE_PATH);
            Object obj = parser.parse(fileReader);
            projectArray = (JSONArray) obj;

            for (Object projectObj : projectArray) {
                if (projectObj instanceof JSONObject) {
                    JSONObject projectJson = (JSONObject) projectObj;
                    String existingFileName = (String) projectJson.get("fileName");
                    if (existingFileName != null && folderJson.get("fileName").equals(existingFileName)) {
                        fileExists = true;
                        break;
                    }
                }
            }

            if (!fileExists) {
                projectArray.add(folderJson);

                fileWriter = new FileWriter(UserInfo.JSON_FILE_PATH);
                fileWriter.write(projectArray.toJSONString());
                fileWriter.flush();
                JOptionPane.showMessageDialog(null, "Folder created and added to the project list.");
            } else {
                JOptionPane.showMessageDialog(null, "Project with the same name already exists.");
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * makes the folder frame visible .
     *
     * @param folderPath The path of the folder.
     * @return True if the folder exists, false otherwise.
     */
    public void openFolder(String folderPath) {
        if (isFolder(folderPath)) {
            FolderFrame folderFrame = new FolderFrame();
            folderFrame.setVisible(true);
            projectFrame.setVisible(false);
        } else {
            JOptionPane.showMessageDialog(null, "Folder not found: " + folderPath);
        }
    }
}
