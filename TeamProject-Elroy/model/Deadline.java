package model;

import java.time.LocalDateTime;

public class Deadline {
    LocalDateTime Deadline; 
    String Title;
    String Desc;
    int ProjectID;

    /**
     * Constructor for Deadline class, self explanatory
     * @param Deadline
     * @param Title
     * @param Desc
     * @param ProjectID
     */
    public Deadline(LocalDateTime Deadline, String Title, String Desc, int ProjectID){
        this.Deadline = Deadline;
        this.Title = Title;
        this.Desc = Desc;
        this.ProjectID = ProjectID;
    }

    /**
     * @return returns the DateTime of the Deadline
     */
    public LocalDateTime getDeadline(){
        return this.Deadline;
    }

    /**
     * sets the DateTime of the Deadline
     */
    public void setDeadline(LocalDateTime deadline){
        this.Deadline = deadline;
    }

    /**
     * @return returns the title of the deadline
     */
    public String getTitle(){
        return this.Title;
    }

    /**
     * @param title set the title of the deadline to this string
     */
    public void setTitle(String title){
        this.Title = title;
    }

    /**
     * @return returns the description of the deadline
     */
    public String getDesc(){
        return this.Desc;
    }

    /**
     * @param desc set the description of the deadline to this string
     */
    public void setDesc(String desc){
        this.Desc = desc;
    }

    /**
     * 
     * @return returns the projectID of the deadline
     */
    public int getProjectID(){
        return this.ProjectID;
    }
}
