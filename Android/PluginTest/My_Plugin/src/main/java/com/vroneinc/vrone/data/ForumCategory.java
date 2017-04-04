package com.vroneinc.vrone.data;

/**
 * Created by Emre on 28/03/2017.
 */

public class ForumCategory {
    private String text;
    private String description;

    public ForumCategory() { }

    public ForumCategory(String text, String description) {
        this.text = text;
        this.description = description;
    }

    public String getText() {
        return text;
    }

    public String getDescription() {
        return description;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
