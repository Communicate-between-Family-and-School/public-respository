package com.example.myapplication;

public class commutecomment {
    String name; //留言者
    String content; //留言内容

    public commutecomment(){

    }

    public commutecomment(String name, String content){
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

