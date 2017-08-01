package com.nilanjan.notetaker.support;

/**
 * Created by nilanjan on 14-Jun-17.
 * Project NoteTaker
 */

public class NotesData {

    private String id, title, body;

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getId() {
        return id;
    }


    public NotesData(String id, String title, String body) {
        this.id = id;
        this.title = title;
        this.body = body;
    }
}

