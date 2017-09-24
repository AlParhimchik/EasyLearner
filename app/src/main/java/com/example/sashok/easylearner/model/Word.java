package com.example.sashok.easylearner.model;

import android.support.v7.widget.CardView;
import android.widget.TextView;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sashok on 23.9.17.
 */

public class Word extends RealmObject {
    private RealmList<RealmString> translation;
    private String enWord;
    private Boolean favourite;

    public int getFolderID() {
        return folderID;
    }

    public void setFolderID(int folderID) {
        this.folderID = folderID;
    }

    public CardView getCardView() {
        return cardView;
    }

    public void setCardView(CardView cardView) {
        this.cardView = cardView;
    }

    private int folderID;
    private Date date;
    private int countOfShow = 1;
    @Ignore
    public CardView cardView;
    @Ignore
    public TextView textView;
    @PrimaryKey
    private int ID;


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public RealmList<RealmString> getTranslation() {

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