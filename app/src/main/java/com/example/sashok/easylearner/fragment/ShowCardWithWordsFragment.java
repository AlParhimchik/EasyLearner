package com.example.sashok.easylearner.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.sashok.easylearner.R;
import com.example.sashok.easylearner.adapter.CardWithWordsAdapter;
import com.example.sashok.easylearner.data.DataProvider;
import com.example.sashok.easylearner.data.StudyNowWordDataProvider;
import com.example.sashok.easylearner.model.Folder;
import com.example.sashok.easylearner.model.Word;
import com.example.sashok.easylearner.realm.RealmController;
import com.example.swipecardlibrary.model.WordInterface;
import com.example.swipecardlibrary.view.Card;

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
    private String folderURL;
    private Toolbar toolbar;
    public Card swipeCardView;
    public static final String TAG = "TAG";
    public static final String BUNDLE_FOLDER_ID = "folder_id";
    public static final String BUNDLE_FOlDER_URL = "folder_url";
    private TextView textError;
    private LinearLayout mainLayout;
    private ProgressBar mProgressBar;
    RealmController realmController;
    private Thread loadThread;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realmController = RealmController.with(getActivity());
        al = new ArrayList<>();
    }

    private void setDataList() {
        al.clear();
        mProgressBar.setVisibility(View.VISIBLE);
        swipeCardView.setVisibility(View.INVISIBLE);
        if (getArguments() != null) {
            folderID = getArguments().getInt(BUNDLE_FOLDER_ID);
            folderURL = getArguments().getString(BUNDLE_FOlDER_URL);
            if (folderURL != null) {
                getWordsFromURL();
            } else if (folderID == -1) {// if clicked words with no folder
                List<Word> words = realmController.getWordsWithoutFolder();
                for (Word word : words) {
                    al.add(word);
                }
                onDataRetrived();
            } else if (folderID == -2) { //if clecked favourite words

            } else {
                Folder folder = realmController.getFolderById(folderID);
                for (Word word : folder.getWords()) {
                    al.add(word);
                }
                onDataRetrived();
            }
        } else {
            List<Word> words = realmController.getWords();
            for (Word word : words) {
                al.add(word);
            }
            onDataRetrived();
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

    public static ShowCardWithWordsFragment newInstance(String folder_url) {

        Bundle args = new Bundle();
        args.putString(BUNDLE_FOlDER_URL, folder_url);
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
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        swipeCardView.setSwipeListener(new Card.SwipeListener() {
            @Override
            public void onRecognizeWord(WordInterface word) {
                Log.d(TAG, "onRecognizeWord: ");
            }

            @Override
            public void onForgetWord(WordInterface word) {
                Log.d(TAG, "onForgetWord: ");
            }
        });
        setDataList();
        return view;
    }


    @Override
    public void onDataSetChanged() {
        setDataList();
        swipeCardView.onDataSetChanged();
        //arrayAdapter.notifyDataSetChanged();
        setVisibility();
    }

    private void setVisibility() {
        if (al.size() == 0) {
            if (folderURL != null)
                textError.setText(R.string.Error_no_Internet_Connection);
            else
                textError.setText(R.string.Error_no_Words_In_Folder);
            textError.setVisibility(View.VISIBLE);
            swipeCardView.setVisibility(View.INVISIBLE);
        } else {
            textError.setVisibility(View.INVISIBLE);
            swipeCardView.setVisibility(View.VISIBLE);
        }
    }


    public void onDataRetrived() {
        mProgressBar.setVisibility(View.GONE);
        arrayAdapter = new CardWithWordsAdapter(getContext(), al);
        swipeCardView.setAdapter(arrayAdapter);
        setVisibility();
    }

    public void getWordsFromURL() {
        loadThread = new Thread(new Runnable() {
            @Override
            public void run() {
                final DataProvider provider = new StudyNowWordDataProvider(folderURL);
                List<Word> words = provider.getData();
                if (words != null)
                    for (Word word : words) {
                        al.add(word);
                    }
                    if (loadThread.isInterrupted()) return;
                else
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onDataRetrived();
                    }
                });
            }
        });
        loadThread.start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (loadThread != null)
            if (loadThread.isAlive()) {
                loadThread.interrupt();
            }
    }

}
