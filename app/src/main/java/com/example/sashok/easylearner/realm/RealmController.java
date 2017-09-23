package com.example.sashok.easylearner.realm;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;

import com.example.sashok.easylearner.model.Folder;
import com.example.sashok.easylearner.model.Word;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by sashok on 23.9.17.
 * all bd logic here
 */
//Singletone
public class RealmController  {

    private static RealmController instance;
    private final Realm realm;

    private   RealmController(Application application){
        realm=Realm.getDefaultInstance();
    }

    public static RealmController with(Activity activity){
        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static  RealmController with(Application application){

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController with(Fragment fragment){
        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;

    }
    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }
    public void refresh() {

        realm.refresh();
    }

    public void addWord(final Word word){
        Boolean result=false;
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(word);
            }
        }, new Realm.Transaction.OnSuccess(){
            @Override
            public void onSuccess() {

            }
        },new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {

            }
        });
    }
    public  void deleteWord(final Word word){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                final RealmResults<Word> results= realm.where(Word.class).contains("id",String.valueOf(word.getID())).findAll();
                results.deleteFirstFromRealm();
            }
        });
    }

    public ArrayList<Word> getWordsinFolder(final Folder folder){
        final ArrayList<Word> words=new ArrayList<>();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Folder result=realm.where(Folder.class).contains("name",folder.getName()).findFirst();
                for (Word word:result.getWords()){
                    words.add(word);
                }
            }
        });
        return  words;
    }
    public ArrayList<Word> getWords(){
        final ArrayList<Word> link=new ArrayList<>();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Word>  words=realm.where(Word.class).findAll();
                for (Word word:words){
                    link.add(word);
                }
            }
        });
        return  link;
    }

    public void addFolder(final  Folder folder){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(folder);
            }
        });
    }

    //xz ili tak
    public void addWordsToFolder(final List<Word> words, final Folder folder){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Folder link=realm.copyToRealmOrUpdate(folder);
                RealmList<Word> links=(RealmList<Word>)realm.copyToRealmOrUpdate(words);
                link.setWords(links);
            }
        });
    }

    public void deleteFolder(final  Folder folder){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Folder.class).contains("id",String.valueOf(folder.getID())).findFirst().deleteFromRealm();
            }
        });
    }
}
