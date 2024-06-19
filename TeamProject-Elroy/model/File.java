package model;

/*
class designer is Abdelrahman Abudayyah
 */
public class File {
    private String name ;
    private String content;

    public File(String name){

        this.name=name;
    }

    public String getName(){
        return name;
    }
    public String getContent(){
        return content;
    }

    public void setName(String name){
        this.name=name;

    }
    public void setContent(String content){
        this.content=content;
    }
}
