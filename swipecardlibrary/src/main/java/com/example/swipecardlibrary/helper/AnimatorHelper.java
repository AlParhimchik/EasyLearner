package com.example.swipecardlibrary.helper;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.swipecardlibrary.R;
import com.example.swipecardlibrary.listener.CardListener;


/**
 * Created by sashok on 28.9.17.
 */

public class AnimatorHelper {
    private static final String TAG = "TAG";
    private static Animation fadeout_animation;
    private static Animation shrink_animation;
    private static Animation grow_animation;

    public static void flipView(Context context, final View animate_view, final CardListener listener) {
        shrink_animation = AnimationUtils.loadAnimation(context, R.anim.shrink_word_card_animation);
        grow_animation = AnimationUtils.loadAnimation(context, R.anim.grow_word_card_animation);
        shrink_animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                animate_view.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                listener.onFlipAnimEnded();
                animate_view.startAnimation(grow_animation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        grow_animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animate_view.setClickable(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animate_view.startAnimation(shrink_animation);

    }

    public static void swipeLeftAnim(final View cardView, final float startPosX, final float startPosY, CardListener listener, float window_width) {
        swipeAnimate(cardView, startPosX, startPosY, Direction.LEFT, listener, -cardView.getWidth());
    }

    public static void swipeRightAnim(final View cardView, final float startPosX, final float startPosY, CardListener listener, float window_width) {
        swipeAnimate(cardView, startPosX, startPosY, Direction.RIGHT, listener, window_width);
    }

    public static void swipeAnimate(final View cardView, final float startPosX, final float startPosY, Direction dir, final CardListener listener, float window_width) {
        float x2 = window_width;
        float y2 = 0;
        if (startPosX == cardView.getX()) {
            if (dir == Direction.LEFT) {
                y2 = cardView.getHeight() / 3;
            }
            if (dir == Direction.RIGHT) {
                y2 = cardView.getHeight() / 3;
            }
        } else
            y2 = AnimatorHelper.findPointY(startPosX, startPosY, cardView.getX(), cardView.getY(), x2);
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(cardView, View.X, cardView.getX(), x2);
        objectAnimator1.setDuration(400);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(cardView, View.Y, cardView.getY(), y2);
        objectAnimator2.setDuration(400);
        AnimatorSet animatorSet = new AnimatorSet();
//        set.setDuration(1400);
        animatorSet.playTogether(objectAnimator1, objectAnimator2);

        animatorSet.addListener(new AnimatorListenerAdapter(){
            @Override
            public void onAnimationStart(Animator animation) {
                cardView.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                cardView.setX(startPosX);
                cardView.setY(startPosY);
                listener.onSwipeAnimationEnded();
                cardView.setClickable(true);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.d(TAG, "onAnimationCancel: ");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

    public static float findPointY(float x0, float y0, float x1, float y1, float x2) {
        return ((y1 - y0) / (x1 - x0)) * (x2 - x0) + y0;
    }

    public enum Direction {
        RIGHT, LEFT
    }
}
