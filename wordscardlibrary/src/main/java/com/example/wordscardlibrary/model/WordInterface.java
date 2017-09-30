package com.example.wordscardlibrary.model;

import java.util.AbstractList;

/**
 * Created by sashok on 28.9.17.
 */
public interface WordInterface {
    public String getEnWord();

    public AbstractList getTranslation();

    public String getTranscription();

    @Override
   public boolean equals(Object obj);
//    {
//        Word word = (Word) obj;
//        if (word.englishWord.equals(this.englishWord) &&
//                word.russianWord.equals(this.russianWord) &&
//                word.transcription.equals(this.transcription))
//            return true;
//        else return false;
//    }
}
