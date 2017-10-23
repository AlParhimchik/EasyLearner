package com.example.sashok.easylearner.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sashok.easylearner.R;
import com.example.sashok.easylearner.listener.DataSetChangeListener;
import com.example.sashok.easylearner.model.Folder;
import com.example.sashok.easylearner.model.RealmString;
import com.example.sashok.easylearner.model.Word;
import com.example.sashok.easylearner.realm.RealmController;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by sashok on 27.9.17.
 */
public class AddWordDialogFragment extends AlertDialog implements View.OnClickListener, View.OnFocusChangeListener {
    EditText en_word;
    EditText transcription_word;
    ViewGroup.LayoutParams en_word_params;

    LinearLayout editText_box;
    ViewGroup.LayoutParams editText_box_params;

    ImageView delete_translate_btn;
    ViewGroup.LayoutParams delete_translate_btn_params;

    final Activity activity;
    DataSetChangeListener listener;
    Word word_on_view;
    Folder folder_of_word_on_view;

    View add_word_view;
    RelativeLayout add_translate_btn;

    TextView choose_folder_btn;
    TextView folder_name;

    RealmController realmController;

    boolean isChangesWord = false;

    public AddWordDialogFragment(final Activity activity, final DataSetChangeListener listener, Word word) {
        super(activity);
        this.activity = activity;
        this.listener = listener;
        word_on_view = word;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initialize();
        if (word_on_view != null) initializeWordOnView();
        else {

            word_on_view = new Word();
            word_on_view.setFolderID(-1);
            isChangesWord = false;

        }
        add_translate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTranslationOnView(null);
            }
        });
        choose_folder_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Builder builderSingle = new Builder(activity);
                builderSingle.setTitle(R.string.change_folder);
                List<Folder> folders = RealmController.with(activity).getFolders();
                final ArrayAdapter<Folder> arrayAdapter = new ArrayAdapter<Folder>(activity, android.R.layout.select_dialog_item);
                arrayAdapter.addAll(folders);

                builderSingle.setNegativeButton(R.string.cancel, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (folder_of_word_on_view != null && folder_of_word_on_view != arrayAdapter.getItem(which)) {
                            realmController.deleteWordFromFolder(folder_of_word_on_view, word_on_view);
                            listener.onDataSetChanged();
                        }
                        folder_of_word_on_view = arrayAdapter.getItem(which);
                        folder_name.setText(folder_of_word_on_view.getName());
                        realmController.changeFolderID(word_on_view, arrayAdapter.getItem(which).getID());
                    }
                });
                builderSingle.show();
            }
        });
        add_word_view.findViewById(R.id.add_button).setOnClickListener(this);

    }


    public AddWordDialogFragment(final Activity activity, DataSetChangeListener listener) {
        this(activity, listener, null);
    }

    public void initialize() {
        LayoutInflater inflater =
                (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        add_word_view = inflater.inflate(R.layout.add_word_dialog, null);
        setView(add_word_view);
        en_word = (EditText) add_word_view.findViewById(R.id.english_word);
        transcription_word = (EditText) add_word_view.findViewById(R.id.transcription);

        en_word_params = en_word.getLayoutParams();
        add_translate_btn = (RelativeLayout) add_word_view.findViewById(R.id.add_trans_layout);

        editText_box = (LinearLayout) add_word_view.findViewById(R.id.add_edit_text_layout);
        editText_box_params = add_word_view.findViewById(R.id.layout_delete_edit).getLayoutParams();

        delete_translate_btn = (ImageView) add_word_view.findViewById(R.id.delete_button);
        delete_translate_btn_params = delete_translate_btn.getLayoutParams();

        choose_folder_btn = (TextView) add_word_view.findViewById(R.id.choose_folder_btn);
        folder_name = (TextView) add_word_view.findViewById(R.id.folder_name);

        realmController = RealmController.with(activity);

    }

    public void initializeWordOnView() {
        isChangesWord = true;
        en_word.setText(word_on_view.getEnWord());
        transcription_word.setText(word_on_view.getTranscription());

        setTitle(word_on_view.getEnWord());
        for (RealmString translate : (RealmList<RealmString>)word_on_view.getTranslations()
                ) {
            addTranslationOnView(translate.string_name);
        }
        folder_of_word_on_view = realmController.getFolderById(word_on_view.getFolderID());
        if (folder_of_word_on_view != null) folder_name.setText(folder_of_word_on_view.getName());


    }

    private void addTranslationOnView(String translation_name) {
        final RelativeLayout layout = new RelativeLayout(activity);
        layout.setLayoutParams(editText_box_params);
        //delete btn
        final ImageView new_delete_btn = new ImageView(activity);
        new_delete_btn.setBackgroundResource(R.drawable.cancel_btn);
        new_delete_btn.setLayoutParams(delete_translate_btn_params);

        new_delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(activity, R.anim.slide_right);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        new Handler().post(new Runnable() {
                            public void run() {
                                editText_box.removeView(layout);
                            }
                        });

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                layout.startAnimation(animation);

            }
        });
        //add new edit_text
        EditText new_translate = new EditText(activity);
        new_translate.setHintTextColor(en_word.getCurrentHintTextColor());
        new_translate.setBackgroundResource(R.drawable.edit_text_style);
        new_translate.setHint(R.string.add_translate);
        if (translation_name != null) new_translate.setText(translation_name);
        new_translate.setInputType(InputType.TYPE_CLASS_TEXT);
        ContextCompat.getDrawable(activity, R.drawable.edit_text_style);

        layout.addView(new_translate, en_word_params);
        layout.addView(new_delete_btn, delete_translate_btn_params);
        editText_box.addView(layout, editText_box_params);
    }

    @Override
    public void onClick(View view) {
        if (editText_box == null || editText_box.getChildCount() == 1) {
            Toast.makeText(activity, "Добавьте перевод", Toast.LENGTH_LONG).show();

        } else {
            Realm realm = realmController.getRealm();
            if (isChangesWord) {
                realmController.deleteAllWordTranslation(word_on_view);
            }
            realm.beginTransaction();
            RealmList<RealmString> translations = new RealmList<>();
            for (int i = 0; i < editText_box.getChildCount(); i++) {
                RelativeLayout layout = (RelativeLayout) editText_box.getChildAt(i);
                View text = layout.getChildAt(0);
                if (text instanceof EditText) {
                    EditText t = (EditText) text;
                    if (i == 0) {
                        word_on_view.setEnWord(t.getText().toString());

                    } else if (i == 1) {
                        word_on_view.setTranscription(t.getText().toString());
                    } else {
                        RealmString realmString = new RealmString();
                        realmString.string_name = t.getText().toString();
                        realm.copyToRealm(realmString);
                        translations.add(realmString);

                    }

                }
            }
            word_on_view.setTranslations(translations);
            realm.commitTransaction();
            if (word_on_view.getEnWord() != "") {
                if (isChangesWord) {
                    realmController.updateWord(word_on_view);
                } else realmController.addWord(word_on_view);

                if (folder_of_word_on_view != null) {
                    word_on_view = realmController.getWordById(word_on_view.getID());
                    realmController.addWordToFolder(word_on_view, folder_of_word_on_view);
                }
                listener.onDataSetChanged();
                dismiss();
            } else Toast.makeText(activity, "Добавьте перевод", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (!b) {
            hideKeyboard(view);
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}