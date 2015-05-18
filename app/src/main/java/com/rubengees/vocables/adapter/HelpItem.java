package com.rubengees.vocables.adapter;

/**
 * Created by ruben on 18.05.15.
 */
public class HelpItem {

    private String title;
    private String text;

    public HelpItem(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}
