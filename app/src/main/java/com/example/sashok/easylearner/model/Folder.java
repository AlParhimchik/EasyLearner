package com.example.sashok.easylearner.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sashok on 23.9.17.
 */

public class Folder extends RealmObject {
    private String name;
    @PrimaryKey
    private int ID;
    private RealmList<Word> words;
    private String folderURL;
    private String imageURL;
    private String rusName;
    public Folder() {
        words = new RealmList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setWord(Word word) {
        if (!words.contains(word)) words.add(word);
    }

    public RealmList<Word> getWords() {
        return words;
    }

    public void setWords(RealmList<Word> words) {
        this.words = words;
    }

    @Override
    public String toString() {
        return this.getName();
    }

    public boolean isWordInFolder(int word_id) {
        for (Word word : getWords()) {
            if (word.getID() == word_id) return true;
        }
        return false;
    }

    public String getFolderURL() {
        return folderURL;
    }

    public void setFolderURL(String folderURL) {
        this.folderURL = folderURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getRusName() {
        return rusName;
    }

    public void setRusName(String rusName) {
        this.rusName = rusName;
    }
}
