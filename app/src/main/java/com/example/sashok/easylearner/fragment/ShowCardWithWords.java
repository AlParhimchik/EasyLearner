package com.example.sashok.easylearner.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sashok.easylearner.R;
import com.example.sashok.easylearner.adapter.CardWordAdapter;
import com.example.sashok.easylearner.lib.SwipeFlingAdapterView;
import com.example.sashok.easylearner.model.Word;
import com.example.sashok.easylearner.realm.RealmController;

import java.util.ArrayList;

/**
 * Created by sashok on 24.9.17.
 */

public class ShowCardWithWords extends Fragment {
    private ArrayList<Word> al;
    private CardWordAdapter arrayAdapter;
    private int i;
    private Toolbar toolbar;
    public SwipeFlingAdapterView swipeCardView;
    public static final String TAG = "TAG";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        swipeCardView = (SwipeFlingAdapterView) getActivity().findViewById(R.id.card_view);
        al = new ArrayList<>();
        RealmController realmController = RealmController.with(getActivity());
        al = realmController.getWords();
        arrayAdapter = new CardWordAdapter(getActivity(), al);

    }

    public ShowCardWithWords() {

    }

    public static ShowCardWithWords newInstance() {

        Bundle args = new Bundle();

        ShowCardWithWords fragment = new ShowCardWithWords();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.show_card_fragment, container, false);
        swipeCardView= (SwipeFlingAdapterView) view.findViewById(R.id.card_view);
        swipeCardView.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                arrayAdapter.onCardClicked();
            }
        });
        swipeCardView.setAdapter(arrayAdapter);
        swipeCardView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                al.remove(0);
                //card_progress.setVisibility(View.INVISIBLE);
                arrayAdapter.notifyDataSetChanged();

            }


            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                arrayAdapter.onDeleteCard();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                arrayAdapter.onDeleteCard();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                Word card = new Word();
//                card.name = "Card1";
//                card.imageId = R.drawable.quila2;
                al.add(card);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onScroll(float v) {
//                arrayAdapter.setVisibility(v);
//                if  (card_progress.getVisibility()==View.INVISIBLE)
//                    card_progress.setVisibility(View.VISIBLE);
//                card_progress.setY(getWindowParams().y*2/3);
//                if (v>0) onSCrollRight(v);
//                else
//                if (v==0) card_progress.setVisibility(View.INVISIBLE);
//                else
//                    onScrollLeft(-v); //go from right
            }
        });

        return view;

    }
}
