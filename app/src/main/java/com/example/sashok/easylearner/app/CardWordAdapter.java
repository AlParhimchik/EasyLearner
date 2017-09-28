package com.example.sashok.easylearner.app;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.sashok.easylearner.R;
import com.example.sashok.easylearner.model.Word;

import java.util.ArrayList;

/**
 * Created by sashok on 24.9.17.
 */

public class CardWordAdapter extends ArrayAdapter<Word> {
    private final ArrayList<Word> words;
    private final LayoutInflater layoutInflater;
    private Animation animation1;
    private Animation animation2;
    private CardView card_all;
    private ArrayList<Word> list=new ArrayList<>();
     public CardWordAdapter(Context context, ArrayList<Word> words){
        super(context,0);
        this.words = words;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        Word word = words.get(position);
        View curView= layoutInflater.inflate(R.layout.word_card_item, parent, false);
        TextView cur_text= (TextView) curView.findViewById(R.id.helloText);
        cur_text.setText(word.getEnWord());
        card_all=(CardView) curView.findViewById(R.id.card);
        word.cardView=card_all;
        word.textView=cur_text;
        list.add(word);

        animation1 = AnimationUtils.loadAnimation(getContext(), R.anim.to_middle);
        animation2 = AnimationUtils.loadAnimation(getContext(), R.anim.from_middle);


        return curView;
    }

    @Override public Word getItem(int position) {
        return words.get((position));
    }

    @Override public int getCount() {
        return (words.size());
    }

//    public void setVisibility(float visibility){
//        TextView textView=(TextView) list.get(0).textView;
//        textView.setText(valueOf(Math.round((Math.abs(visibility)*100)))+"progress");
//        //textView.setAlpha(1-Math.abs(visibility));
//
//    }
    public void onDeleteCard(){
        list.remove(0);
    }


    public void onCardClicked(){
        final CardView card_view=list.get(0).cardView;


        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                card_view.setAnimation(animation2);
                card_view.startAnimation(animation2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        card_view.setAnimation(animation1);
        card_view.startAnimation(animation1);
    }

}
