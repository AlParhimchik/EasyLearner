package com.example.swipecardlibrary.adapter;


import com.example.swipecardlibrary.listener.WordCardAdapterListener;
import com.example.swipecardlibrary.model.WordInterface;

/**
 * Created by sashok on 28.9.17.
 */

public abstract class WordAdapter<T extends WordInterface> extends BaseAdapter<T>  implements WordCardAdapterListener<T> {

    public WordAdapter(){
        position=0;
    }

}
