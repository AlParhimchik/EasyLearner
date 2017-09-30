package com.example.wordscardlibrary.adapter;

import com.example.wordscardlibrary.listener.WordCardAdapterListener;
import com.example.wordscardlibrary.model.WordInterface;


/**
 * Created by sashok on 28.9.17.
 */

public abstract class WordAdapter<T extends WordInterface> extends BaseAdapter<T>  implements WordCardAdapterListener<T> {

}
