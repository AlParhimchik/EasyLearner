package com.example.wordscardlibrary.helper;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.wordscardlibrary.R;
import com.example.wordscardlibrary.listener.CardListener;
import com.example.wordscardlibrary.model.WordInterface;
import com.example.wordscardlibrary.view.Card;

/**
 * Created by sashok on 28.9.17.
 */

public class AnimatorHelper {
    private static Animation fadeout_animation;
    private static Animation shrink_animation;
    private static Animation grow_animation;

    public static void flipView(Context context, final View animate_view, final WordInterface word, final CardListener<WordInterface> listener) {
        shrink_animation = AnimationUtils.loadAnimation(context, R.anim.shrink_word_card_animation);
        grow_animation = AnimationUtils.loadAnimation(context, R.anim.grow_word_card_animation);
        shrink_animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                listener.onFlipAnimEnded(word);
                animate_view.startAnimation(grow_animation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animate_view.startAnimation(shrink_animation);
    }

    public static void changeView(Context mContext, Card cardView) {
        fadeout_animation = AnimationUtils.loadAnimation(mContext, R.anim.fadeout_word_card_animation);
        cardView.startAnimation(fadeout_animation);

    }
}
