package com.example.sashok.easylearner.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sashok on 23.9.17.
 */

public class Word extends RealmObject {
    private RealmList<RealmString> translation;
    private String enWord;
    private Boolean favourite;
    private Folder folderName;
    private Date date;
    private int countOfShow = 1;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    @PrimaryKey
    private int ID;


    public RealmList<RealmString>getTranslation() {

        return translation;
    }

    public void setTranslation(RealmList<RealmString> translation) {
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

    public Date getDate(){
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCountOfShow() {
        return countOfShow;
    }

    public void setCountOfShow(int countOfShow) {
        this.countOfShow = countOfShow;
    }
}