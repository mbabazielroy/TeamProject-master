package model;/*
class designer is Abdelrahman Abudayyah

 this class where read and write using JSON

 */

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

public class InfoLoaderWriter {
    /* userInfoFilePath is the path where the program  is going to read from  and write to
     you can adjust it to a path on your computer when you read the code
     then when you sign up with a new username it will create a new user in this file path
       on your computer*/

    private String userInfoFilePath = "files_2.json";

    private static Map<String, User> info = new HashMap<>();

    public InfoLoaderWriter(String userName) throws IOException {

        info= getInfo();

        if(!getMap().containsKey(userName))
            updateMapUser(userName);

    }

    public Map<String, User> getMap(){
        return info;
    }

    // read from file and store in map
    private Map<String, User> getInfo() {
        JSONObject jsonObject= parseFile(userInfoFilePath);
        Map<String, User> map = new HashMap<>();
        if (jsonObject != null) {
            JSONArray jsUsers= (JSONArray)jsonObject.get("Users");
            Iterator<JSONObject> iterator= jsUsers.iterator();
            while (iterator.hasNext()){
                JSONObject current = iterator.next();
                User user= getUser(current);
                map.put(user.getName(),user);
            }
        }
        return map;
    }
    private JSONObject parseFile(String userInfoFilePath) {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = null;
        try (Reader reader = new FileReader(userInfoFilePath)) {
            jsonObject = (JSONObject) jsonParser.parse(reader);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
    private User getUser(JSONObject currentUser){
        User u = new User((String) currentUser.get("Name"));
        // u.setName((String) currentUser.get("Name"));
        JSONArray jsFolders= (JSONArray)currentUser.get("folders");
        Iterator<JSONObject> iterator= jsFolders.iterator();
        List<Folder> listFolders= new ArrayList<>();
        while (iterator.hasNext()){
            JSONObject currentFolder = iterator.next();
            Folder folder= getFolder(currentFolder);
            listFolders.add(folder);
        }
        u.setFolders(listFolders);
        return u;
    }
    private Folder getFolder(JSONObject currentFolder){
        Folder folder=new Folder((String) currentFolder.get("Name"));
        JSONArray jsFiles= (JSONArray)currentFolder.get("files");
        Iterator<JSONObject> iterator= jsFiles.iterator();
        List<File> listFiles= new ArrayList<>();
        while (iterator.hasNext()){
            JSONObject currentFile=iterator.next();
            File file=getFile(currentFile);
            listFiles.add(file);
        }
        folder.setFiles(listFiles);
        return folder;
    }
    private File getFile(JSONObject currentFile){
        File file=new File((String) currentFile.get("Name"));
        file.setContent((String) currentFile.get("Content"));
        return file;
    }
///////////////////////////////////////////////////////////////////////

    // update map

    // update map user
    public void updateMapUser(String userName) throws IOException {
        User user= new User(userName);
        info.put(userName,user);
        writeInfoFolder();
    }

    // update map folder
    public void updateMapFolder(String userName, String folderName) throws IOException {
        updateUserFolder(info.get(userName),folderName);
    }
    private void updateUserFolder(User user, String folderName) throws IOException {
        Folder folder= new Folder(folderName);
        user.getFolders().add(folder);
        writeInfoFolder();
    }


    // update map file
    public void updateMapFile(String userName, String folderName, String fileName, String content) throws IOException {
        updateUserFolder(info.get(userName),folderName, fileName, content);
    }
    private void updateUserFolder(User user, String folderName, String fileName, String content) throws IOException {
        Iterator<Folder> iterator=user.getFolders().iterator();
        while (iterator.hasNext()){
            Folder curr= iterator.next();
            if (curr.getName().equals(folderName))
                updateUserFile(curr,fileName,content);
        }
    }
    private void updateUserFile(Folder folder, String fileName, String content) throws IOException {
        File file= new File(fileName);
        file.setContent(content);
        folder.getFiles().add(file);
        writeInfoFolder();
    }


    /////////////////////////////////////////////////////////////////
    // write in file and
    void writeInfoFolder() throws IOException {
        Iterator<Map.Entry<String, User>> iterator=info.entrySet().iterator();
        List<JSONObject> usersList=new ArrayList();
        while (iterator.hasNext()){

            List<JSONObject> foldersList=new ArrayList();
            Map.Entry<String, User> curruntEntry= iterator.next();
            String name =curruntEntry.getKey();
            User value =curruntEntry.getValue();
            Iterator<Folder>iterator1=value.getFolders().iterator();

            while (iterator1.hasNext()){
                List<JSONObject> list3=new ArrayList();

                Folder currentFolder=iterator1.next();
                Iterator<File> iterator2=currentFolder.getFiles().iterator();
                while (iterator2.hasNext()){
                    File currentFile=iterator2.next();
                    JSONObject jsonObjectFiles = new JSONObject();
                    jsonObjectFiles.put("Name",currentFile.getName());
                    jsonObjectFiles.put("Content",currentFile.getContent());
                    list3.add(jsonObjectFiles);
                }//End files loop
                JSONObject jsonObjectFolders = new JSONObject();
                jsonObjectFolders.put("Name",currentFolder.getName());
                jsonObjectFolders.put("files",list3);
                foldersList.add(jsonObjectFolders);
            }//End folder loop
            JSONObject jsonObjectUsers = new JSONObject();
            jsonObjectUsers.put("Name",value.getName());
            jsonObjectUsers.put("folders",foldersList);
            usersList.add(jsonObjectUsers);
        }

        JSONObject jsonObjects=new JSONObject();
        jsonObjects.put("Users",usersList);
        FileWriter file=new FileWriter(userInfoFilePath);
        file.write(jsonObjects.toJSONString());
        file.close();
    }
    //End class
}
