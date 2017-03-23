package com.vroneinc.vrone;

/**
 * Created by avi on 3/20/2017.
 */

public class ForumMessage {

    private String text;
    private String name;
    private String photoUrl;

    public ForumMessage() {
    }

    public ForumMessage(String text, String name, String photoUrl) {
        this.text = text;
        this.name = name;
        this.photoUrl = photoUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}

