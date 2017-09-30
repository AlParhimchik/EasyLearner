package com.example.sashok.easylearner.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sashok.easylearner.R;
import com.example.sashok.easylearner.adapter.CardWithWordsAdapter;
import com.example.sashok.easylearner.model.Folder;
import com.example.sashok.easylearner.model.Word;
import com.example.sashok.easylearner.realm.RealmController;
import com.example.wordscardlibrary.view.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sashok on 24.9.17.
 */

public class ShowCardWithWordsFragment extends AbsFragment {
    private ArrayList<Word> al;
    private CardWithWordsAdapter arrayAdapter;
    private int i;
    private int folderID;
    private Toolbar toolbar;
    public com.example.wordscardlibrary.view.Card swipeCardView;
    public static final String TAG = "TAG";
    public static final String BUNDLE_FOLDER_ID = "folder_id";
    private TextView textError;
    private LinearLayout mainLayout;
    RealmController realmController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realmController = RealmController.with(getActivity());
        al = new ArrayList<>();
        setDataList();
    }

    private void setDataList() {
        al.clear();
        if (getArguments() != null) {
            folderID = getArguments().getInt(BUNDLE_FOLDER_ID);
            if (folderID == -1) {// if clicked words with no folder
                List<Word> words = realmController.getWordsWithoutFolder();
                for (Word word : words) {
                    al.add(word);
                }
            } else if (folderID == -2) { //if clecked favourite words

            } else {
                Folder folder = realmController.getFolderById(folderID);
                for (Word word : folder.getWords()) {
                    al.add(word);
                }
            }
        } else {
            List<Word> words = realmController.getWords();
            for (Word word : words) {
                al.add(word);
            }
        }

    }

    public ShowCardWithWordsFragment() {

    }

    public static ShowCardWithWordsFragment newInstance(int folder_id) {

        Bundle args = new Bundle();
        args.putInt(BUNDLE_FOLDER_ID, folder_id);
        ShowCardWithWordsFragment fragment = new ShowCardWithWordsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.show_card_fragment, container, false);
        textError = (TextView) view.findViewById(R.id.error_text_no_words);
        mainLayout = (LinearLayout) view.findViewById(R.id.add_folder_layout);
        swipeCardView = (Card) view.findViewById(R.id.card_view);

        setVisibility();

//
//        swipeCardView.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClicked(int itemPosition, Object dataObject) {
//                arrayAdapter.onCardClicked();
//            }
//        });

        arrayAdapter = new CardWithWordsAdapter(getContext(), al);

        swipeCardView.setAdapter(arrayAdapter);
//        swipeCardView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
//            @Override
//            public void removeFirstObjectInAdapter() {
//                al.remove(0);
//                //card_progress.setVisibility(View.INVISIBLE);
//                arrayAdapter.notifyDataSetChanged();
//
//            }
//
//
//            @Override
//            public void onLeftCardExit(Object dataObject) {
//                //Do something on the left!
//                //You also have access to the original object.
//                //If you want to use it just cast it (String) dataObject
//                arrayAdapter.onDeleteCard();
//            }
//
//            @Override
//            public void onRightCardExit(Object dataObject) {
//                arrayAdapter.onDeleteCard();
//            }
//
//            @Override
//            public void onAdapterAboutToEmpty(int itemsInAdapter) {
//                Word card = new Word();
////                card.name = "Card1";
////                card.imageId = R.drawable.quila2;
//                al.add(card);
//                arrayAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onScroll(float v) {
////                arrayAdapter.setVisibility(v);
////                if  (card_progress.getVisibility()==View.INVISIBLE)
////                    card_progress.setVisibility(View.VISIBLE);
////                card_progress.setY(getWindowParams().y*2/3);
////                if (v>0) onSCrollRight(v);
////                else
////                if (v==0) card_progress.setVisibility(View.INVISIBLE);
////                else
////                    onScrollLeft(-v); //go from right
//            }
//        });


        return view;

    }


    @Override
    public void OnDataSetChanged() {
        setDataList();
        swipeCardView.onDataSetChanged();
        //arrayAdapter.notifyDataSetChanged();
        setVisibility();
    }

    private void setVisibility() {
        if (al.size() == 0) {
            textError.setText(R.string.Error_no_Words_In_Folder);
            textError.setVisibility(View.VISIBLE);
            swipeCardView.setVisibility(View.INVISIBLE);
        } else {
            textError.setVisibility(View.INVISIBLE);
            swipeCardView.setVisibility(View.VISIBLE);
        }
    }
}
