package com.example.sashok.easylearner.model;

import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sashok on 23.9.17.
 */

public class Word extends RealmObject {
    private List<String> translation;
    private String enWord;
    private Boolean favourite;
    private Folder folderName;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    @PrimaryKey
    private int ID;

    public List<String> getTranslation() {
        return translation;
    }

    public void setTranslation(List<String> translation) {
        this.translation = translation;
    }

    public String getEnWord() {
        return enWord;
    }

    public void setEnWord(String enWord) {
        this.enWord = enWord;
    }

    public Boolean getFavourite() {
        return favourite;
    }

    public void setFavourite(Boolean favourite) {
        this.favourite = favourite;
    }

    public Folder getFolderName() {
        return folderName;
    }

    public void setFolderName(Folder folderName) {
        this.folderName = folderName;
    }
}
