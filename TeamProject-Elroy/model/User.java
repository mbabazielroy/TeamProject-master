package model;/*
class designer is Abdelrahman Abudayyah
 */
import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private List<Folder> folders;
    public User(String name){
        this.name=name;
        folders= new ArrayList<>();
    }

    public String getName(){
        return name;
    }
    public List<Folder> getFolders(){
        return folders;
    }

    public void setName(String name){
        this.name=name;

    }
    public void setFolders(List<Folder> folders){
        this.folders=folders;
    }
}
