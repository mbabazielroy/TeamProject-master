package model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Calendar {
    ArrayList<Deadline> Deadlines;

    /**
     * Contructor, loads initial deadlines
     */
    public Calendar(){
        this.Deadlines = loadDeadlines("temp");
    }

    /**
     * 
     * @param index index of deadline in Deadlines ArrayList
     * @return returns specified Deadline
     */
    public Deadline getDeadline(int index){
        return this.Deadlines.get(index);
    }

    /**
     * 
     * @param day 
     * @param month
     * @param year
     * @return returns all deadlines at a given date, used to populate add/edit JFrame
     */
    public ArrayList<Integer> getDeadlines(int day, int month, int year){
        ArrayList<Integer> returnVal = new ArrayList<Integer>();
        // System.out.println(Deadlines.size());

        for (Deadline deadline : Deadlines) {
            // System.out.println((deadline.Deadline.getYear() == year)+", "+(deadline.Deadline.getDayOfMonth() == day)+", "+(deadline.Deadline.getMonthValue() == month));
            if(deadline.Deadline.getYear() == year &&
               deadline.Deadline.getDayOfMonth() == day &&
               deadline.Deadline.getMonthValue() == month
            )
            { returnVal.add(Integer.valueOf(Deadlines.indexOf(deadline))); }
        }
        return returnVal;
    }

    /**
     *  wrapper for other getDeadlines, converts LocalDate to day, month, year
     * @param date
     * @return
     */
    public ArrayList<Integer> getDeadlines(LocalDate date){
        return getDeadlines(date.getDayOfMonth(), date.getMonthValue(), date.getYear());
    }

    /**
     * Sets Deadline at index to new input Deadline
     * @param index
     * @param deadline
     */
    public void setDeadline(int index, Deadline deadline){
        this.Deadlines.set(index, deadline);
    }

    /**
     * removes deadline at index
     * @param index
     */
    public void removeDeadline(int index){
        this.Deadlines.remove(index);
    }

    /**
     * @return returns length of Deadlines ArrayList
     */
    public int deadlinesLength(){
        return this.Deadlines.size();
    }

    /**
     * Loads intial deadlines
     * @param json
     * @return
     */
    private ArrayList<Deadline> loadDeadlines(String json){
        return new ArrayList<Deadline>(); // todo, create this.Deadlines from json;
    }

    /**
     * adds deadline to end of Deadlines ArrayList
     * @param deadline
     */
    public void addDeadline(Deadline deadline){
        this.Deadlines.add(deadline);
    }
}
