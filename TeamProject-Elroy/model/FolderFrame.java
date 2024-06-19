package model;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;

/**
 * The FolderFrame class represents the frame that allows the user to add, delete, and save files to a project.
 * The files are stored in a JSON file.
 * @author Elroy
 */
public class FolderFrame extends JFrame {
    private JPanel fileListPanel;
    private JButton deleteButton;
    private Project project = new Project(null);
    private final String FILE_PATH = "FolderList.json";

    /**
     * Loads the files from the JSON file.
     */
    public FolderFrame() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Folder Actions");
        setSize(1200, 900);
        setLocationRelativeTo(null);
        setResizable(false);

        fileListPanel = new JPanel();
        fileListPanel.setLayout(new GridLayout(0, 1));

        JScrollPane scrollPane = new JScrollPane(fileListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JPanel panel = new JPanel();
        JButton loadButton = new JButton("Add File");
        JButton saveButton = new JButton("Save");
        JButton backButton = new JButton("Back");

        deleteButton = new JButton("Delete");
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteFile();
            }
        });

        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadFile();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    saveFilesToJson();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                project.openProjectFrame();
            }
        });

        panel.add(loadButton);
        panel.add(saveButton);
        panel.add(deleteButton);
        panel.add(backButton);

        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(panel, BorderLayout.SOUTH);

        loadFilesFromJson();
        displayFiles();

        setVisible(true);
    }

    @Override
    public void dispose() {
        try {
            saveFilesToJson();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.dispose();
    }

    /**
     * Loads a file and adds it to the file list.
     */
    private void loadFile() {
        FileDialog fileDialog = new FileDialog(this);
        fileDialog.setVisible(true);
        String selectedFilePath = fileDialog.getFile();
        String selectedDirectory = fileDialog.getDirectory();
        fileDialog.dispose();

        if (selectedFilePath != null) {
            File selectedFile = new File(selectedDirectory, selectedFilePath);
            String fileId = selectedFile.getName();

            JSONObject fileObj = new JSONObject();
            fileObj.put("file_name", fileId);
            fileListPanel.add(createFileButton(fileId, fileObj));
            fileListPanel.revalidate();
            fileListPanel.repaint();
        }
    }

    /**
     * Creates a file button with the given file ID and JSON object.
     * @param fileId the ID of the file
     * @param fileObj the JSON object representing the file
     * @return the created JButton
     */
    private JButton createFileButton(String fileId, JSONObject fileObj) {
        JButton fileButton = new JButton(fileId);
        fileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openFile(fileObj);
            }
        });

        fileButton.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                deleteButton.setEnabled(true);
            }

            @Override
            public void focusLost(FocusEvent e) {
                deleteButton.setEnabled(false);
            }
        });

        return fileButton;
    }

     /**
     * Deletes the selected file from the file list.
     */
    private void deleteFile() {
        Component[] components = fileListPanel.getComponents();
        int numComponents = components.length;

        if (numComponents > 0) {
            int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the selected file?", "Delete File", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                fileListPanel.remove(numComponents - 1);
                fileListPanel.revalidate();
                fileListPanel.repaint();
            }
        }

        deleteButton.setEnabled(numComponents > 0);
    }

    /**
     * Opens the selected file using the default system application.
     * @param fileObj the JSON object representing the file
     */
    private void openFile(JSONObject fileObj) {
        String fileId = (String) fileObj.get("file_name");
        File file = new File(fileId);
        if (file.exists() && file.isFile()) {
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error opening file: " + fileId);
            }
        } else {
            JOptionPane.showMessageDialog(null, "File not found: " + fileId);
        }
    }

    /**
     * Saves the files in the file list to a JSON file.
     * @throws IOException if an I/O error occurs
     */
    private void saveFilesToJson() throws IOException {
        JSONArray fileList = new JSONArray();
        Component[] components = fileListPanel.getComponents();
        for (Component component : components) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                String fileId = button.getText();
                JSONObject fileObj = new JSONObject();
                fileObj.put("file_name", fileId);
                fileList.add(fileObj);
            }
        }

        try (FileWriter fileWriter = new FileWriter(FILE_PATH)) {
            fileWriter.write(fileList.toJSONString());
        }
    }

    /**
     * Loads the files from the JSON file and adds them to the file list.
     */

    private void loadFilesFromJson() {
        fileListPanel.removeAll();
        JSONParser jsonParser = new JSONParser();
        File jsonFile = new File(FILE_PATH);
        if (jsonFile.exists()) {
            try (FileReader fileReader = new FileReader(jsonFile)) {
                Object obj = jsonParser.parse(fileReader);
                if (obj instanceof JSONArray) {
                    JSONArray fileList = (JSONArray) obj;
                    for (Object fileObj : fileList) {
                        if (fileObj instanceof JSONObject) {
                            JSONObject jsonObject = (JSONObject) fileObj;
                            String fileId = (String) jsonObject.get("file_name");
                            fileListPanel.add(createFileButton(fileId, jsonObject));
                        }
                    }
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Displays the files in the file list.
     */
    private void displayFiles() {
        fileListPanel.revalidate();
        fileListPanel.repaint();
    }
}
