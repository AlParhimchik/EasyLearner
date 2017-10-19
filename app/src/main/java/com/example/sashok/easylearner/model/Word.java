package com.example.sashok.easylearner.model;

import android.support.v7.widget.CardView;
import android.widget.TextView;

import com.example.swipecardlibrary.model.WordInterface;

import java.util.AbstractList;
import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sashok on 23.9.17.
 */

public class Word extends RealmObject implements WordInterface {
    private RealmList<RealmString> translation;
    private String enWord;
    private Boolean favourite;
    private String transcription;
    private String example;
    private String exampleTranslate;
    private int folderID;
    private Date date;
    private int countOfShow = 1;

    @PrimaryKey
    private int ID;

    public void setExample(String example) {
        this.example = example;
    }

    public void setExampleTranslate(String exampleTranslate) {
        this.exampleTranslate = exampleTranslate;
    }

    public int getFolderID() {
        return folderID;
    }

    public void setFolderID(int folderID) {
        this.folderID = folderID;
    }

    @Override
    public String getTranscription() {
        return transcription;
    }

    @Override
    public String getExample() {
        return example;
    }

    @Override
    public String getExampleTrans() {
        return exampleTranslate;
    }

    public void setTranscription(String transcription) {
        this.transcription = transcription;
    }

    public Word() {
        translation = new RealmList<>();
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    @Override
    public RealmList getTranslations() {

        return translation;
    }

    public void setTranslation(RealmList<RealmString> translation) {
        for (RealmString string : translation)
            this.translation.add(string);
    }

    public void setTranslation(RealmString string) {
        if (translation == null) translation = new RealmList<>();

        if (!this.translation.contains(string)) this.translation.add(string);
    }

    @Override
    public String getEnWord() {
        return enWord;
    }

    @Override
    public String getTranslation() {
        return translation.get(0).string_name;
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


    public Date getDate() {
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