package View;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.JFileChooser;
import java.awt.Desktop;
import java.io.*;
import java.nio.file.Files;



/**
 * The UserInfo class represents user information and provides methods to store, retrieve, and export user info.
 * @author Elroy
 */
public class UserInfo {
    private static String userName;
    private static String userEmail;
    public static final String JSON_FILE_PATH = "user_info.json";

    /**
     * Gets the name of the user.
     *
     * @return The name of the user.
     */
    public static String getName() {
        return userName;
    }

    /**
     * Gets the email of the user.
     *
     * @return The email of the user.
     */
    public static String getEmail() {
        return userEmail;
    }

     /**
     * Sets the name of the user.
     *
     * @param name The name of the user.
     */
    public static void setName(String name) {
        userName = name;
    }

    /**
     * Sets the email of the user.
     *
     * @param email The email of the user.
     */
    public static void setEmail(String email) {
        userEmail = email;
    }

    /**
     * Stores the user information in a JSON file.
     *
     * @param filesObject The JSON object containing file information.
     */
    public static void storeUserInfo(JSONObject filesObject) {
        JSONArray existingUserInfo = retrieveUserInfo();

        JSONObject userObject = new JSONObject();
        userObject.put("name", UserInfo.getName());
        userObject.put("email", UserInfo.getEmail());

        if (existingUserInfo != null) {
            // Check if user information already exists in the array
            boolean userExists = false;
            for (Object obj : existingUserInfo) {
                if (obj instanceof JSONObject) {
                    JSONObject existingUser = (JSONObject) obj;
                    String existingName = (String) existingUser.get("name");
                    String existingEmail = (String) existingUser.get("email");
                    if (existingName != null && existingEmail != null) {
                        if (existingName.equals(UserInfo.getName()) && existingEmail.equals(UserInfo.getEmail())) {
                            userExists = true;
                            break;
                        }
                    }
                }
            }

            if (!userExists) {
                // Add the new user to the existing user info
                existingUserInfo.add(userObject);
            }
        } else {
            // If no existing information found, create a new array with the current user
            existingUserInfo = new JSONArray();
            existingUserInfo.add(userObject);
        }

        try (FileWriter file = new FileWriter(JSON_FILE_PATH)) {
            file.write(existingUserInfo.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the user info from the JSON file.
     *
     * @return The JSON array containing user info.
     */
    public static JSONArray retrieveUserInfo() {
        JSONParser parser = new JSONParser();
        JSONArray userInfoArray = new JSONArray();

        try (FileReader fileReader = new FileReader(JSON_FILE_PATH)) {
            Object obj = parser.parse(fileReader);
            if (obj instanceof JSONArray) {
                userInfoArray = (JSONArray) obj;
            }
        } catch (FileNotFoundException e) {
            // Handle the case when the file doesn't exist
            return userInfoArray;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return userInfoArray;
    }

    /**
     * Exports the user info to a JSON file.
     */
    public static void exportSettingsAndData() {
        // Retrieve user info from the JSON file
        JSONArray userInfoArray = retrieveUserInfo();

        // Check if user info is available
        if (userInfoArray != null && userInfoArray.size() > 0) {
            // Convert the JSON array to a formatted string
            String exportData = userInfoArray.toJSONString();
            String exportFileName = "user_info_export.json";

            try {
                // Create a temporary file
                File tempFile = File.createTempFile("export", ".json");

                // Write the export data to the file
                try (FileWriter writer = new FileWriter(tempFile)) {
                    writer.write(exportData);
                }

                // Show the file chooser dialog for saving the file
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setSelectedFile(new File(exportFileName));

                int result = fileChooser.showSaveDialog(null);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    // Copy the temporary file to the selected destination
                    Files.copy(tempFile.toPath(), selectedFile.toPath());

                    // Open the exported file using the default application
                    Desktop.getDesktop().open(selectedFile);
                }

                // Delete the temporary file
                tempFile.delete();
            } catch (IOException e) {
                e.printStackTrace();
                // Handle any exceptions that occur during export
            }
        } else {
            System.out.println("No user info available to export.");
        }
    }
}
