package View;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.DimensionUIResource;

import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Dimension;

import model.Calendar;
import model.Deadline;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * Represents a frame for user information input.
 * @author Anthony
 */
public class CalendarUI {
    model.Calendar Calendar;
    JPanel MainPanel;
    JPanel CalendarPanel;
    JFrame DateFrame;
    java.util.Calendar CalendarUtil;
    boolean visible = false;

    /**
     * Constructor, initializes all neccesary fields, creates some sample deadlines
     */
    public CalendarUI(){
        Calendar = new Calendar();
        CalendarUtil = new java.util.Calendar.Builder().setInstant(new java.util.Date()).build();
        // int CurrYear = CalendarUtil.get(CalendarUtil.YEAR);
        // System.out.println("\n"+CurrYear+", "+CalendarUtil.getDisplayName(CalendarUtil.MONTH, CalendarUtil.LONG_FORMAT, new java.util.Locale("en"))+", \n");
        Calendar.addDeadline(new Deadline(LocalDateTime.of(2023, 6, 8, 23, 59, 59), "Project DUE!!", "This program is due.", 0));
        Calendar.addDeadline(new Deadline(LocalDateTime.of(2023, 6, 7, 15, 40, 0), "343 Final", "2 hrs, in person.", 0));
        Calendar.addDeadline(new Deadline(LocalDateTime.of(2023, 6, 5, 13, 30, 59), "445 Final", "All day event.", 0));

        CalendarPanel = new JPanel();
        DateFrame = new JFrame();

        this.populateCalendar();
        CalendarPanel.setVisible(true);

    }

    /**
     * 
     * @return CalendarPanel, used in UI to get panel to insert into frame
     */
    public JPanel getPanel(){
        return this.CalendarPanel;
    }

    /**
     * Creates the calender and populates it with data, called to intially construct calendar, also called whenever data updates or month or year changes
     */
    private void populateCalendar(){
        this.CalendarPanel.removeAll();
        this.CalendarPanel.setLayout(new GridBagLayout());
        GridBagConstraints buttonGbc = new GridBagConstraints();
        // buttonGbc.anchor = GridBagConstraints.NORTH;
        CalendarUI self = this;
        JButton prevYear = new JButton("<");



        prevYear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                self.prevYear();
            }
        });
        JButton nextYear = new JButton(">");
        nextYear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                self.nextYear();
            }
        });
        JButton prevMonth = new JButton("<");
        prevMonth.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                self.prevMonth();
            }
        });
        JButton nextMonth = new JButton(">");
        nextMonth.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                self.nextMonth();
            }
        });

        styleButton(prevYear);
        styleButton(nextYear);
        styleButton(prevMonth);
        styleButton(nextMonth);

        buttonGbc.gridy=0;
        buttonGbc.gridx=1;
        buttonGbc.anchor = GridBagConstraints.WEST;
        this.CalendarPanel.add(prevYear, buttonGbc);

        buttonGbc.anchor = GridBagConstraints.CENTER;

        this.CalendarPanel.add(new JLabel(""+CalendarUtil.get(CalendarUtil.YEAR)), buttonGbc);

        buttonGbc.anchor = GridBagConstraints.EAST;
        this.CalendarPanel.add(nextYear, buttonGbc);
        buttonGbc.gridy=1;
        buttonGbc.anchor = GridBagConstraints.WEST;
        this.CalendarPanel.add(prevMonth, buttonGbc);
        buttonGbc.anchor = GridBagConstraints.CENTER;
        this.CalendarPanel.add(new JLabel(""+this.CalendarUtil.getDisplayName(CalendarUtil.MONTH, CalendarUtil.LONG_FORMAT, new java.util.Locale("en"))), buttonGbc);
        buttonGbc.anchor = GridBagConstraints.EAST;
        this.CalendarPanel.add(nextMonth, buttonGbc);

        buttonGbc.gridy=3;
        buttonGbc.anchor = GridBagConstraints.CENTER;
        JPanel DaysPanel = new JPanel();
        DaysPanel.setBackground(Color.DARK_GRAY);
        DaysPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thur", "Fri", "Sat"};
        for(int i = 0; i<7; i++){
            for(int j = 0; j<7; j++){

                gbc.gridx=j;
                gbc.gridy=i;
                if(i==0){
                    JLabel dayLabel = new JLabel(days[j]);
                    dayLabel.setForeground(Color.WHITE);
                    DaysPanel.add(dayLabel, gbc);
                } else {
                    DaysPanel.add(newDayButton(j+i*7+1 - 7), gbc);
                }
            }
        }
        this.CalendarPanel.add(DaysPanel, buttonGbc);

        this.CalendarPanel.setPreferredSize(new DimensionUIResource(1140, 900));
        this.CalendarPanel.setBackground(Color.WHITE);
        this.CalendarPanel.revalidate();
        this.CalendarPanel.repaint();
    }

    /**
     * Used to give JButtons a certain default style
     * 
     * @param button JButton that we're styling
     */
    private void styleButton(JButton button){
        Border line = new LineBorder(Color.BLACK);
        Border margin = new EmptyBorder(5, 15, 5, 15);
        Border compound = new CompoundBorder(line, margin);
        button.setForeground(Color.BLACK);
        button.setBackground(Color.WHITE);
        button.setBorder(compound);
        button.setFocusPainted(false);

    }

    /**
     * helper for populateCalendar, returns one of the squares/day panels on the calendar 
     * @param day day value somewhere between 1 and 42, 3 filters are applied to correct to actual date
     * @return returns JPanel to insert into calendar grid
     */
    private JPanel newDayButton(int day){

        CalendarUtil.set(CalendarUtil.DAY_OF_MONTH, 1);
        LocalDate OGdate = LocalDate.of(CalendarUtil.get(CalendarUtil.YEAR), CalendarUtil.get(CalendarUtil.MONTH)+1, 1);
        int monthLength = OGdate.lengthOfMonth();
        CalendarUtil.set(CalendarUtil.MONTH, CalendarUtil.get(CalendarUtil.MONTH)-1);
        LocalDate date = LocalDate.of(CalendarUtil.get(CalendarUtil.YEAR), CalendarUtil.get(CalendarUtil.MONTH)+1, 1);

        int prevMonthLength = date.lengthOfMonth();

        CalendarUtil.set(CalendarUtil.MONTH, CalendarUtil.get(CalendarUtil.MONTH)+1);


        // System.out.println(this.CalendarUtil.get(CalendarUtil.DAY_OF_WEEK)+", "+CalendarUtil.getDisplayName(CalendarUtil.MONTH, CalendarUtil.LONG_FORMAT, new java.util.Locale("en"))+", "+CalendarUtil.getDisplayName(CalendarUtil.DAY_OF_WEEK, CalendarUtil.LONG_FORMAT, new java.util.Locale("en")));
        CalendarUI self = this;
        JPanel dayButton = new JPanel();

        Border blackline = BorderFactory.createLineBorder(Color.black);
        dayButton.setBorder(blackline);
        dayButton.setLayout(new GridBagLayout());
        GridBagConstraints buttonGbc = new GridBagConstraints();
        buttonGbc.anchor = GridBagConstraints.NORTHEAST;
        buttonGbc.gridy=0;
        buttonGbc.gridx=0;
        day = day - this.CalendarUtil.get(CalendarUtil.DAY_OF_WEEK) + 1;
        date = OGdate.plusDays(day-1);

        // System.out.println(date);
        // System.out.println(LocalDate.now());

        if(day<1 || day > monthLength){
            Color background = dayButton.getBackground();
            int r = background.getRed();
            int g = background.getGreen();
            int b = background.getBlue();
            double dimmer = 0.8067226890756303;
            dayButton.setBackground(new Color((int)((double)r*dimmer), (int)((double)g*dimmer), (int)((double)b*dimmer)));
        }
        day = day - monthLength*(int) Math.floor(day/(monthLength+1));
        String sampleDeadlineText = "";

        ArrayList<Integer> deadlineIndices = Calendar.getDeadlines(date);
        for (Integer index: deadlineIndices){
            sampleDeadlineText+=(" - "+Calendar.getDeadline(index).getTitle()+"\n");
        }

        if(day < 1){
            day = day + prevMonthLength;
        }
        Color defaultColor = (Color) dayButton.getBackground();
        JLabel dayLabel = new JLabel(day+" ", SwingConstants.RIGHT);
        if(date.equals(LocalDate.now())){
            dayLabel.setText("Today ");
        }
        dayLabel.setPreferredSize(new DimensionUIResource(100, 16));
        dayLabel.setOpaque(false);
        dayButton.add(dayLabel, buttonGbc);
        buttonGbc.anchor = GridBagConstraints.SOUTHWEST;
        buttonGbc.gridy=1;
        buttonGbc.gridx=0;
        // if(Math.random() < 0.3){
        //     sampleDeadlineText += " - Lorem isum\n";
        //     if(Math.random() < 0.65){
        //         sampleDeadlineText += " - Dolor sit\n";
        //         if(Math.random() < 0.65){
        //             sampleDeadlineText += " - Amet cosect\n";
        //             if(Math.random() < 0.65){
        //                 sampleDeadlineText += " - Adiping elit\n";
        //                 if(Math.random() < 0.65){
        //                     sampleDeadlineText += " - Sed do eium";
        //                 }
        //             }
        //         }
        //     }
        // }
        JTextArea deadlines = new JTextArea(sampleDeadlineText);
        // deadlines.setFont()
        deadlines.setWrapStyleWord(true);
        // deadlines.setLineWrap(true);
        // dayLabel.setBorder(blackline);
        // deadlines.setBorder(blackline);
        deadlines.setHighlighter(null);
        deadlines.setEditable(false);
        deadlines.setPreferredSize(new DimensionUIResource(100, 84));
        // deadlines.setMaximumSize(new DimensionUIResource(84, 70));
        deadlines.setOpaque(false);
        dayButton.add(deadlines, buttonGbc);
        final LocalDate finalDate = date;
        MouseListener mouseEvents = new MouseListener() {
            @Override
            public void mouseClicked(final MouseEvent theEvent) {}
            @Override
            public void mousePressed(MouseEvent e) {dayButton.setBackground(new Color(180, 225, 255));}
            @Override
            public void mouseReleased(MouseEvent e) {dayButton.setBackground(defaultColor); OpenDateMenu(finalDate, -1);}
            @Override
            public void mouseEntered(MouseEvent e) {dayButton.setBackground(Color.WHITE);}
            @Override
            public void mouseExited(MouseEvent e) {dayButton.setBackground(defaultColor);}
        };
        dayButton.addMouseListener(mouseEvents);
        dayLabel.addMouseListener(mouseEvents);
        deadlines.addMouseListener(mouseEvents);

        dayButton.setSize(400, 300);
        dayButton.setBounds(0, 0, 300, 300);
        return dayButton;
    }

    /**
     * Creates and populates the JFrame that allows you to add/edit deadlines at a specific date, bit of a monster, would break it up if there was more time, most of the logic is in here, kinda spaghetti code
     * @param date the date we are adding/editing deadlines at
     * @param edit the deadline index that we are editing, -1 if not editing
     */
    private void OpenDateMenu(LocalDate date, int edit){
        DateFrame.getContentPane().removeAll();
        ArrayList<Integer> indices = Calendar.getDeadlines(date);
        JPanel datePanel = new JPanel();
        datePanel.setLayout(null);
        JPanel deadlinePanel = new JPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = gbc.WEST;
        Border border = BorderFactory.createLineBorder(Color.black);
        deadlinePanel.setBorder(border);
        GridBagLayout gbl = new GridBagLayout();
        gbl.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        gbl.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        deadlinePanel.setLayout(gbl);
        deadlinePanel.setBounds( 6, 6, 372, 443);
        setDim(datePanel, new Dimension(384, 1000));
        datePanel.setBackground(Color.lightGray);
        int i = 0;
        gbc.gridx = 0;
        gbc.anchor = gbc.NORTH;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets( 4, 4, 2, 4 );
        for(Integer index: indices){
            JPanel temp = deadlinePanel(index, date);
            deadlinePanel.add(temp, gbc);
            gbc.insets = new Insets( 4, 4, 4, 4 );
            i++;
        }
        datePanel.add(deadlinePanel);
        DateFrame.getContentPane().setLayout(null);
        DateFrame.getContentPane().add(datePanel, gbc);
        JButton addDeadline = new JButton("+");
        styleButton(addDeadline);
        JPanel editPanel = new JPanel();
        datePanel.add(editPanel);
        int editPanelH = 77;
        int editPanelW = 382;
        editPanel.setBackground(Color.lightGray);
        editPanel.setBounds( 6, 455, editPanelW, editPanelH);
        int textWidth = 292;
        int titleHeight = 20;
        int padding = 6;
        editPanel.setLayout(null);
        JTextArea titleBox = new JTextArea("Title");
        titleBox.setBounds( 0, 0, textWidth, titleHeight);
        JTextArea descBox = new JTextArea("Description");
        if(edit != -1){ titleBox.setText(Calendar.getDeadline(edit).getTitle());}
        if(edit != -1){ descBox.setText(Calendar.getDeadline(edit).getDesc());}
        descBox.setBounds( 0, titleHeight+padding, textWidth, editPanelH-(titleHeight+padding));
        descBox.setLineWrap(true);
        titleBox.setBorder(border);
        descBox.setBorder(border);
        editPanel.add(titleBox);
        editPanel.add(descBox);
        JButton saveButton = new JButton("Add");
        if(edit != -1){saveButton.setText("Apply");}
        ComboItem[] projectList = {
            new ComboItem( "Project", 0),
            new ComboItem( "Project1", 1),
            new ComboItem( "Project2",2),
            new ComboItem( "Project3",3),
            new ComboItem( "Project4",4),
            new ComboItem( "Project5",5),
            new ComboItem( "Project6",6),
            new ComboItem( "Project7",7),
            new ComboItem( "Project8",8),
            new ComboItem( "Project9",9),
            new ComboItem( "Project10",10),
            new ComboItem( "Project11",11),
            new ComboItem( "Project12",12),
            new ComboItem( "Project13",13),
            new ComboItem( "Project14",14),
            new ComboItem( "Project15",15),
            new ComboItem( "Project16",16),
            new ComboItem( "Project17",17),
            new ComboItem( "Project18",18),
            new ComboItem( "Project19",19),
            new ComboItem( "Project20",20),
            new ComboItem( "Project21",21),
            new ComboItem( "Project22",22),
            new ComboItem( "Project23",23),
            new ComboItem( "Project24",24),
            new ComboItem( "Project25",25),
            new ComboItem( "Project26",26),
            new ComboItem( "Project27",27),
            new ComboItem( "Project28",28),
            new ComboItem( "Project29",29),
            new ComboItem( "Project30",30),
            new ComboItem( "Project31",31),
            new ComboItem( "Project32", 32)
        };
        JComboBox projects = new JComboBox<ComboItem>(projectList);
        projects.setMaximumRowCount(1);
        BoundsPopupMenuListener listener = new BoundsPopupMenuListener(true, true);
        listener.setScrollBarRequired(false);
        projects.addPopupMenuListener( listener );
        projects.addPopupMenuListener(listener);
        styleButton(saveButton);
        projects.setForeground(Color.BLACK);
        projects.setBackground(Color.WHITE);
        projects.setFocusable(false);
        projects.setVisible(true);
        projects.setBorder(border);
        JButton ampmButton = new JButton("AM");
        ampmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(ampmButton.getText().equals("PM")){
                    ampmButton.setText("AM");
                } else {
                    ampmButton.setText("PM");
                }
            }
        });
        JTextArea hour = new JTextArea("8");
        JLabel colon = new JLabel(":");
        JTextArea minute = new JTextArea("00");
        hour.setBorder(border);
        minute.setBorder(border);
        if(edit != -1){
            int hr = Calendar.getDeadline(edit).getDeadline().getHour();
            hour.setText(hr%12+"");
            minute.setText(Calendar.getDeadline(edit).getDeadline().getMinute()+"");
            if(hr > 11){
                ampmButton.setText("PM");
            }
        }
        // ADD DEADLINE
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean PM = false;
                if(ampmButton.getText() == "PM"){ PM = true;}
                try{
                    String Title = titleBox.getText();
                    String Description = descBox.getText();
                    int hourNum = Integer.parseInt(hour.getText());
                    int minuteNum = Integer.parseInt(minute.getText());
                    String date = DateFrame.getTitle();
                    String[] dateVals = date.split("/");
                    int day = Integer.parseInt(dateVals[1]);
                    int month = Integer.parseInt(dateVals[0]);
                    int year = Integer.parseInt(dateVals[2]);
                    int projectID = ((ComboItem) (projects.getSelectedItem())).getValue();
                    if(edit != -1){
                        projectID = Calendar.getDeadline(edit).getProjectID();
                    }
                    if(hourNum > 11 || hourNum < 1 || minuteNum < 0 || minuteNum > 59){
                        throw new NumberFormatException("Num");
                    }
                    if(PM){hourNum+=12;}
                    if(Title.equals("Title") && Description.equals("Description")){
                        throw new NumberFormatException("Title+Description");
                    } else if(Title.equals("Title")){
                        System.out.println("Please provide a Title AND Descrption");
                        throw new NumberFormatException("Title");
                    } else if(Description.equals("Description")){
                        throw new NumberFormatException("Description");
                    }

                    LocalDate thisDate = LocalDate.of(year, month, day);
                    LocalDateTime thisDateTime = LocalDateTime.of(thisDate, LocalTime.of(hourNum, minuteNum, 0));
                    Deadline deadline = new Deadline(thisDateTime, Title, Description, projectID);
                    if(edit != -1){
                        Calendar.setDeadline(edit, deadline);
                    } else {
                        Calendar.addDeadline(deadline);
                    }
                    OpenDateMenu(thisDate, -1);
                    populateCalendar();
                } catch(NumberFormatException numError){
                    switch(numError.getMessage()){
                        case "Title":    break;
                        case "Description": JOptionPane.showMessageDialog(DateFrame, "Please provide a description."); break;
                        case "Title+Description": JOptionPane.showMessageDialog(DateFrame, "Please provide a title and description."); break;
                        default: JOptionPane.showMessageDialog(DateFrame, "Invalid Time.");
                    }
                }
            }
        });

        setDim(ampmButton, new Dimension(25, 11));
        styleButton(ampmButton);
        saveButton.setBounds( textWidth+padding-2, editPanelH-titleHeight, 76, titleHeight);
        projects.setBounds( textWidth+padding-2, editPanelH-titleHeight*2-padding, 76, titleHeight);
        editPanel.add(saveButton);
        if(edit == -1){
            editPanel.add(projects);
        }
        ampmButton.setBorder(border);
        Border line = new LineBorder(Color.BLACK);
        Border margin = new EmptyBorder(1, 2, 0, 2);
        Border compound = new CompoundBorder(line, margin);
        hour.setBorder(compound);
        minute.setBorder(compound);
        titleBox.setBorder(compound);
        descBox.setBorder(compound);
        hour.setBounds( textWidth+padding-2, 0, 20, titleHeight);
        editPanel.add(hour);
        colon.setBounds( textWidth+padding+20, 0, 20, titleHeight);
        editPanel.add(colon);
        minute.setBounds( textWidth+padding+25, 0, 20, titleHeight);
        editPanel.add(minute);
        ampmButton.setBounds( textWidth+padding+49, 0, 25, titleHeight);
        editPanel.add(ampmButton);
        gbc.insets = new Insets( 0, 0, 0, 0 );
        addDeadline.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        if (!DateFrame.isVisible()) {
            DateFrame.setLocationRelativeTo(CalendarPanel);
        }
        DateFrame.setTitle(date.getMonthValue() + "/" + date.getDayOfMonth() + "/" + date.getYear());
        DateFrame.setSize(400, 577);
        DateFrame.setResizable(false);
        DateFrame.setVisible(true);

    }

    /**
     * Used to stored a String-int pair in JComboBox.
     * String for display, int for projectID
     * There was a planned projectID in the Project class, this projectID was supposed to be a link between a specific deadline and a project, didn't end up being implemented
     */
    private class ComboItem
    {
        private String key;
        private int value;

        public ComboItem(String key, int value)
        {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString()
        {
            return key;
        }

        public String getKey()
        {
            return key;
        }

        public int getValue()
        {
            return value;
        }
    }

    /**
     * Another method used to generate UI, dense and not super interesting
     * @param index index of the deadline in the Calendar's deadline array
     * @param date date of the current add/edit window
     * @return
     */
    private JPanel deadlinePanel(int index, LocalDate date){
        Border border = BorderFactory.createLineBorder(Color.black);
        JPanel returnVal = new JPanel();
        Dimension dim = returnVal.getPreferredSize();
        returnVal.setBorder(border);
        returnVal.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets( 2, 2, 0, 0 );
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        returnVal.add(new JLabel(Calendar.getDeadline(index).getTitle()), gbc);
        gbc.weightx = 0;
        gbc.insets = new Insets(0, 0, 0, 0);
        int hour = Calendar.getDeadline(index).getDeadline().getHour();
        String suffix = " AM";
        if(hour > 11){
            suffix = " PM";
        }
        int minute = Calendar.getDeadline(index).getDeadline().getMinute();
        String minuteString = minute+"";
        if(minute<10){
            minuteString = "0"+minute;
        }
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        JPanel timeAndArrow = new JPanel();
        timeAndArrow.setLayout(new GridBagLayout());
        gbc.insets = new Insets( 0, 2, 0, 2 );
        timeAndArrow.add(new JLabel((hour%12)+":"+minuteString+suffix), gbc);
        gbc.gridx = 1;
        JLabel dropDown = new JLabel("\u25BC ");
        JTextArea desc = new JTextArea(Calendar.getDeadline(index).getDesc());
        desc.setEditable(false);
        desc.setVisible(false);
        desc.setWrapStyleWord(true);
        desc.setLineWrap(true);
        desc.setMaximumSize(new Dimension(50, 200));
        timeAndArrow.add(dropDown, gbc);
        gbc.gridwidth = 3;
        gbc.gridx = 1;
        returnVal.add(timeAndArrow, gbc);
        JButton delete = new JButton("\u2715");
        JButton edit = new JButton("Edit");//"\u270E");
        delete.setVisible(false);
        edit.setVisible(false);
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Calendar.removeDeadline(index);
                populateCalendar();
                OpenDateMenu(date, -1);
            }
        });
        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OpenDateMenu(date, index);
            }
        });
        dropDown.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {if(dropDown.getText().equals("\u25B2 ")) {
                dropDown.setText("\u25BC "); // down arrow
                desc.setVisible(false);
                delete.setVisible(false);
                edit.setVisible(false);
            } else {
                dropDown.setText("\u25B2 "); // up arrow
                desc.setVisible(true);
                delete.setVisible(true);
                edit.setVisible(true);
            }
        }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets( 0, 2, 0, 0 );
        returnVal.add(desc, gbc);
        styleButton(delete);
        styleButton(edit);
        Border line = new LineBorder(Color.BLACK);
        Border margin = new EmptyBorder(0,0,0,1);
        Border compound = new CompoundBorder(line, margin);
        delete.setBorder(compound);
        edit.setBorder(compound);
        setDim(delete, new Dimension(18, 18));
        setDim(edit, new Dimension(48, 18));
        JPanel editAndDelete = new JPanel();
        editAndDelete.setLayout(new GridBagLayout());
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets( 0, 0, 0, 6 );
        gbc.gridwidth = 1;
        editAndDelete.add(edit, gbc);
        gbc.gridx = 2;
        gbc.insets = new Insets( 0, 0, 0, 0 );
        editAndDelete.add(delete, gbc);
        gbc.gridy = 1;
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        returnVal.add(editAndDelete, gbc);
        return returnVal;
    }

    /**
     * Used to try to force a size
     * Sizing is annoying in swing. 
     * There are many functions for setting size, and only some of the work at any given time
     * This throws all sizing methods at the component, usually one sticks
     * @param component JComponent we are trying to size
     * @param dim The size we are trying to apply
     */
    private void setDim(JComponent component, Dimension dim){
        component.setMaximumSize(dim);
        component.setMinimumSize(dim);
        component.setPreferredSize(dim);
        component.setSize(dim);

        component.revalidate();
    }

    /**
     * Sets month of CalendarUtil, then redraws calendar, used to change the month we are viewing
     * @param month the month value we are setting our calendar to
     */
    private void setMonth(int month){
        this.CalendarUtil.set(CalendarUtil.MONTH, month);
        this.populateCalendar();
    }

    /**
     * Sets year of CalendarUtil, then redraws calendar, used to change the year we are viewing
     * @param year the year value we are setting our calendar to
     */
    private void setYear(int year){
        this.CalendarUtil.set(CalendarUtil.YEAR, year);
        this.populateCalendar();

    }

    /**
     * Uses setYear to increment the year of our calendar by 1 and redraw
     */
    private void nextYear(){
        this.setYear(CalendarUtil.get(CalendarUtil.YEAR)+1);
    }

    /**
     * Uses setYear to decrement the year of our calendar by 1 and redraw
     */
    private void prevYear(){
        this.setYear(CalendarUtil.get(CalendarUtil.YEAR)-1);
    }

    /**
     * Uses setMonth to increment the month of our calendar by 1 and redraw
     */
    private void nextMonth(){
        this.setMonth(CalendarUtil.get(CalendarUtil.MONTH)+1);
    }

    /**
     * Uses setMonth to decrement the month of our calendar by 1 and redraw
     */
    private void prevMonth(){
        this.setMonth(CalendarUtil.get(CalendarUtil.MONTH)-1);
    }
}
