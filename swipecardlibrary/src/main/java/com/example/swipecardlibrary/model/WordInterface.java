package com.example.swipecardlibrary.model;

import java.util.AbstractList;
import java.util.List;

/**
 * Created by sashok on 28.9.17.
 */
public interface WordInterface {
    public String getEnWord();

    public AbstractList<Object> getTranslations();

    public String getTranslation();

    public String getTranscription();

    public String getExample();

    public String getExampleTrans();


}