package com.example.passwordsnotesmanager;

public class Password {
    private String title;
    private String content;


    public Password() {

    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title){
        this.title=title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Password(String title, String content){
        this.title=title;
        this.content=content;
    }
}
