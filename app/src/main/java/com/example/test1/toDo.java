package com.example.test1;

/**
 * Created by 또리또리 on 2019-03-31.
 */

public class toDo {
    private String day;
    private String title;
    private String contents;

    public toDo(String d,String t, String c){
        this.day=d;
        this.title=t;
        this.contents=c;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
