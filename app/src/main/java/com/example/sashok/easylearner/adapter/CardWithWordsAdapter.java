package com.example.sashok.easylearner.adapter;

import android.content.Context;

import com.example.sashok.easylearner.model.Word;
import com.example.wordscardlibrary.adapter.WordAdapter;

import java.util.List;

/**
 * Created by sashok on 30.9.17.
 */

public class CardWithWordsAdapter extends WordAdapter<Word> {
    private Context context;
    private  List<Word> words;
    @Override
    public Word getItem(int position) {
        return words.get(position);
    }

    @Override
    public int getItemCount() {
        return words.size();
    }
    public CardWithWordsAdapter(Context context,List<Word> wordList){
        this.context=context;
        this.words=wordList;

    }
}
