package com.example.sashok.easylearner.model;

import android.content.Context;
import android.icu.text.IDNA;
import android.widget.TextView;

import com.example.sashok.easylearner.R;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.expand.ChildPosition;
import com.mindorks.placeholderview.annotations.expand.ParentPosition;

/**
 * Created by sashok on 24.9.17.
 */
@Layout(R.layout.words_expansible)
public class WordView {
    @ParentPosition
    private int mParentPosition;

    @ChildPosition
    private int mChildPosition;

    @View(R.id.titleTxt)
    private TextView titleTxt;

    private Word word;
    private Context mContext;

    public WordView(Context context, Word word) {
        mContext = context;
        this.word = word;
    }
    @Resolve
    private void onResolved() {
        titleTxt.setText(word.getEnWord());

    }

}
