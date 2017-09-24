package com.example.sashok.easylearner.utils;

import android.text.format.DateFormat;

import com.example.sashok.easylearner.model.Folder;
import com.example.sashok.easylearner.model.Word;
import com.example.sashok.easylearner.realm.RealmController;

import java.util.Date;
import java.util.List;

public class WordManager {
    private RealmController mController;
    private final static long INDATE = 24 * 60 * 60 * 1000;

    public void WordManager() {
        mController = RealmController.getInstance();
    }

    public Word getNextWord() {
        List<Word> allWords = mController.getWords();
        Date currentDate = new Date();
        for (Word word : allWords)
            if (word.getDate().equals(currentDate))
                return word;
        return null;
    }

    public Word getNextWord(Folder folder) {
        List<Word> allWords = mController.getWordsinFolder(folder);
        Date currentDate = new Date();
        for (Word word : allWords)
            if (word.getDate().equals(currentDate))
                return word;
        return null;
    }

    public void isTrueAnswered(Word checkedWord, boolean answer) {
        List<Word> allWords = mController.getWords();
        Date currentDate = new Date();
        for (Word word : allWords)
            if (word.equals(checkedWord))
                if (answer) {
                    word.setDate(new Date(currentDate.getTime() + word.getCountOfShow() * INDATE));
                    word.setCountOfShow(word.getCountOfShow() + 1);
                }
                else {
                    word.setDate(currentDate);
                    word.setCountOfShow(1);
                }
    }
}