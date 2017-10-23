package com.example.sashok.easylearner.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sashok.easylearner.R;
import com.example.sashok.easylearner.activity.VocabluraryActivity;
import com.example.sashok.easylearner.data.FolderDataProvider;
import com.example.sashok.easylearner.data.RealmDBFolderDataProvider;
import com.example.sashok.easylearner.listener.ActionModeListener;
import com.example.sashok.easylearner.model.Folder;
import com.example.sashok.easylearner.model.RealmString;
import com.example.sashok.easylearner.model.Word;
import com.example.sashok.easylearner.realm.RealmController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import io.realm.RealmList;

/**
 * Created by sashok on 20.10.17.
 */

public class VocabluraryAdapter extends RecyclerView.Adapter implements VocabluraryActivity.ToolbarItemListener {
    private List<Word> filteredWords;
    private List<Word> mWords;
    private ArrayList<Word> selectedItems;
    private boolean editWords = false;
    private VocabluraryActivity.SortType mSortType = VocabluraryActivity.SortType.SORT_BY_FOLDER;
    private boolean orderBy = true;
    private OnItemClickListener itemClickListener;
    private ArrayList<Folder> separate_folders;
    private List<Folder> folders;
    private Activity context;
    private ActionModeListener mActionModeListener;
    private RealmController mRealmController;
    private Folder untitledWords;

    public VocabluraryAdapter(Activity context, List<Word> words) {
        mActionModeListener = ((VocabluraryActivity) context);
        selectedItems = new ArrayList();
        this.filteredWords = words;
        mWords = new ArrayList<>();
        mWords.addAll(filteredWords);
        this.context = context;
        separate_folders = new ArrayList<>();
        FolderDataProvider provider = new RealmDBFolderDataProvider(context);
        folders = provider.getData();
        mRealmController = RealmController.with(context);
        untitledWords = new Folder();
        untitledWords.setName(context.getResources().getString(R.string.default_folder));
        sortWords();
        notifyDataSetChanged();
    }

    @Override
    public void onDeleteItemClicked() {
        Folder folder_delete;
        for (Word word :
                selectedItems) {
            if (filteredWords.contains(word)) {
                if (mSortType == VocabluraryActivity.SortType.SORT_BY_FOLDER) {
                    folder_delete = getFolderById(word.getFolderID());
                    mRealmController.deleteWordFromFolder(folder_delete, word);
                    mRealmController.deleteWord(word);
//                    if (folder_delete.getWords().size()==0) {
//                        mRealmController.deleteFolder(folder_delete);
//                        folders.remove(folder_delete);
//                    }
                } else {
                    getFolderByWord(word).getWords().remove(word);
                }

                filteredWords.remove(word);
            }

        }
        selectedItems.clear();

    }

    @Override
    public void onEditItemClicked() {

        selectedItems.clear();
        editWords = !editWords;

        notifyDataSetChanged();

    }

    @Override
    public void onSortByItemClicked(VocabluraryActivity.SortType type, Boolean orderBy) {
        this.orderBy = orderBy;
        this.mSortType = type;
        sortWords();
        notifyDataSetChanged();
    }

    @Override
    public void onCancelItemClicked() {
        editWords = false;
        notifyDataSetChanged();
    }

    public OnItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vocablurary_word_item, parent, false);
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        final WordViewHolder wordViewHolder = (WordViewHolder) viewHolder;
        final Word word = filteredWords.get(position);
        wordViewHolder.eng_word.setText(word.getEnWord());
        wordViewHolder.rus_word.setText(word.getTranslation());
        if (editWords) {
            wordViewHolder.mCheckBox.getLayoutParams().width = RecyclerView.LayoutParams.WRAP_CONTENT;
            if (selectedItems.contains(word)) wordViewHolder.mCheckBox.setChecked(true);
            else wordViewHolder.mCheckBox.setChecked(false);
        } else wordViewHolder.mCheckBox.getLayoutParams().width = 0;
        Folder cur_folder = null;

        if (mSortType == VocabluraryActivity.SortType.SORT_BY_DATE) {
            cur_folder = getFolderByWord(word);
        }
        if (mSortType == VocabluraryActivity.SortType.SORT_BY_FOLDER) {
            cur_folder = getFolderById(word.getFolderID());
        }
        if (cur_folder.getWords().indexOf(word) == 0) {
            wordViewHolder.separator_layout.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
            wordViewHolder.sep_text.setText(cur_folder.getName());
        } else {
            wordViewHolder.separator_layout.getLayoutParams().height = 0;
            ((LinearLayout.LayoutParams) (wordViewHolder.separator_layout.getLayoutParams())).weight = 0;
        }

        final Folder final_cur_folder = cur_folder;
        ((WordViewHolder) viewHolder).separator_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editWords) {
                    Boolean isChecked = false;
                    for (Word word : final_cur_folder.getWords()
                            ) {
                        if (selectedItems.contains(word)) {

                            isChecked = true;
                            break;
                        }

                    }
                    if (!isChecked)
                        selectedItems.addAll(final_cur_folder.getWords());
                    else selectedItems.removeAll(final_cur_folder.getWords());
                    mActionModeListener.onItemSelectedCountChanges(selectedItems.size());
                    notifyDataSetChanged();
                }

            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editWords) {
                    Log.d("TAG", "onClick: ");
                    wordViewHolder.mCheckBox.setChecked(!wordViewHolder.mCheckBox.isChecked());
                    if (wordViewHolder.mCheckBox.isChecked()) selectedItems.add(word);
                    else selectedItems.remove(word);
                    mActionModeListener.onItemSelectedCountChanges(selectedItems.size());

                }
                if (itemClickListener != null) {
                    itemClickListener.onItemClicked(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredWords == null ? 0 : filteredWords.size();
    }

    public static class WordViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout separator_layout;
        private TextView sep_text;
        private TextView eng_word;
        private TextView rus_word;
        private CheckBox mCheckBox;
        private LinearLayout main;

        public WordViewHolder(View itemView) {
            super(itemView);
            eng_word = (TextView) itemView.findViewById(R.id.eng_word);
            rus_word = (TextView) itemView.findViewById(R.id.rus_word);
            sep_text = (TextView) itemView.findViewById(R.id.order_name);
            separator_layout = (LinearLayout) itemView.findViewById(R.id.sep_layout);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.check_box);
            main = (LinearLayout) itemView.findViewById(R.id.main_word_layout);
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(int pos);
    }

    public void sortWords() {
        separate_folders.clear();
        untitledWords.getWords().clear();
        Folder word_folder;

        if (mSortType == VocabluraryActivity.SortType.SORT_BY_FOLDER) {
            for (Word word :
                    filteredWords) {
                word_folder = getFolderById(word.getFolderID());
                if (!word_folder.getWords().contains(word))
                    word_folder.getWords().add(word);
                if (!separate_folders.contains(word_folder)) {
                    separate_folders.add(word_folder);
                }

            }

        }

        if (mSortType == VocabluraryActivity.SortType.SORT_BY_DATE) {
//            Folder day_folder = new Folder();
//            day_folder.name = "today";
//            day_folder.words.add(mWords.get(0));
//            day_folder.words.add(mWords.get(1));
//            day_folder.words.add(mWords.get(2));
//            separate_folders.add(day_folder);
//            Folder last_week = new Folder();
//            last_week.name = "last week";
//            last_week.words.add(mWords.get(3));
//            last_week.words.add(mWords.get(4));
//            last_week.words.add(mWords.get(5));
//            separate_folders.add(last_week);
        }
        if (!orderBy) {
            Collections.sort(separate_folders, new Comparator<Folder>() {
                @Override
                public int compare(Folder o1, Folder o2) {
                    return (o1.getName().compareTo(o2.getName()));
                }
            });
        } else {
            Collections.sort(separate_folders, new Comparator<Folder>() {
                @Override
                public int compare(Folder o1, Folder o2) {
                    return -(o1.getName().compareTo(o2.getName()));
                }
            });
        }
        filteredWords.clear();
        for (Folder folder : separate_folders
                ) {
            filteredWords.addAll(folder.getWords());
        }

    }

    public Folder getFolderByWord(Word word) {
        for (Folder folder :
                separate_folders) {
            if (folder.getWords().indexOf(word) != -1) return folder;
        }
        return null;
    }

    public Folder getFolderById(int id) {
        Folder folder = mRealmController.getFolderById(id);
        if (folder == null) folder = untitledWords;
        return folder;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        filteredWords.clear();
        if (charText.length() == 0) {
            filteredWords.addAll(mWords);
        } else {
            for (Word word : mWords) {
                if (word.getEnWord().contains(charText) ||
                        getFolderById(word.getFolderID()).getName().contains(charText) ||
                        getFolderByWord(word).getName().contains(charText)) {
                    filteredWords.add(word);
                    break;
                }

                for (RealmString string : (RealmList<RealmString>) word.getTranslations()) {
                    if (string.string_name.contains(charText)) {
                        filteredWords.add(word);
                        break;
                    }

                }
            }

        }
        notifyDataSetChanged();
    }

}