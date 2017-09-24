package com.example.sashok.easylearner.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sashok on 23.9.17.
 */

public class Folder extends RealmObject {
    public String getName() {
        return name;
    }

    public  Folder(){
        words=new RealmList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
    @PrimaryKey
    private int ID;
    private RealmList<Word> words;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setWord(Word word){
        if (!words.contains(word))words.add(word);
    }

    public RealmList<Word> getWords() {
        return words;
    }

    public void setWords(RealmList<Word> words) {
        this.words = words;
    }
}
