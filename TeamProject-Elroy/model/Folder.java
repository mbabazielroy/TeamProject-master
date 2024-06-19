package model;/*
class designer is Abdelrahman Abudayyah
 */
import java.util.ArrayList;
import java.util.List;

public class Folder {
    private String name;
    private List<File> files;

    public Folder(String name){
        this.name=name;
        files=new ArrayList<>();
    }

    public String getName(){
        return name;
    }
    public List<File> getFiles(){
        return files;
    }

    public void setName(String name){
        this.name=name;

    }
    public void setFiles(List<File> files){
        this.files=files;
    }
}
