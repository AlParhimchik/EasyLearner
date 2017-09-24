package com.example.sashok.easylearner.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.sashok.easylearner.R;
import com.example.sashok.easylearner.model.Folder;
import com.example.sashok.easylearner.model.Word;
import com.example.sashok.easylearner.realm.RealmController;

import java.util.List;

/**
 * Created by sashok on 24.9.17.
 */

public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.WordHolder> {
    private List<Word> words;
    private Activity activity;
    private SparseBooleanArray posToAdd;

    public class WordHolder extends RecyclerView.ViewHolder {
        private TextView en_name;
        private CheckBox add;

        public WordHolder(View itemView) {
            super(itemView);
            en_name = (TextView) itemView.findViewById(R.id.word_en_name);
            add = (CheckBox) itemView.findViewById(R.id.add_to_folder_checkbox);
        }

    }

    public WordsAdapter(Activity activity, List<Word> words) {
        this.words = words;
        this.activity = activity;
        posToAdd = new SparseBooleanArray(words.size());
    }

    @Override
    public WordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.words_to_add_item, parent, false);
        WordHolder viewHolder = new WordHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final WordHolder holder, final int position) {
        Word word = words.get(position);
        holder.en_name.setText(word.getEnWord());
        holder.add.setChecked(false);
        holder.add.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                posToAdd.put(position, isChecked);

            }
        });
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    public void onSaveBtnClicked(Folder folder) {
        for (int i = 0; i < posToAdd.size(); i++) {
            if (posToAdd.get(i)) folder.setWord(words.get(i));
        }
        RealmController realmController = RealmController.with(activity);
        realmController.addFolder(folder);


    }
}
