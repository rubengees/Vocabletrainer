package com.rubengees.vocables.adapter;

/**
 * A simple pojo for the help articles.
 *
 * @author Ruben Gees
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
