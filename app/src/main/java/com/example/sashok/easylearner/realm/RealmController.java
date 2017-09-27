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
            instance = new RealmController(fragment.getActivity().getApplication());

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
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Number maxValue = realm.where(Word.class).max("ID");
                int pk = (maxValue != null) ? maxValue.intValue() + 1 : 0;
                word.setID(pk);
                realm.copyToRealmOrUpdate(word);
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


    public List<Folder> getFolders(){
        final ArrayList<Folder> folders=new ArrayList<>();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Folder> result=realm.where(Folder.class).findAll();
                for (Folder folder:result){
                    folders.add(folder);
                }
            }
        });
        return  folders;

    }

    public void addFolder(final  Folder folder){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Number maxValue = realm.where(Folder.class).max("ID");
                int pk = (maxValue != null) ? maxValue.intValue() + 1 : 1;
                folder.setID(pk);
                List<Word>word_in_folder=new ArrayList<Word>();
                for (Word word:folder.getWords()){
                    word.setFolderID(folder.getID());
                    realm.copyToRealmOrUpdate(word);
                }
                realm.copyToRealmOrUpdate(folder);
            }
        });
    }

    public void upDateFolder(final Folder folder){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(folder);
            }
        });
    }

    public ArrayList<Word> getWordsWithoutFolder(){

        final ArrayList<Word> words=new ArrayList<>();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Word> result=realm.where(Word.class).equalTo("folderID",-1).findAll();
                for (Word word:result){
                    words.add(word);
                }
            }
        });
        return  words;

    }


    public Folder getFolderById(final int ID){
        final Folder[] folder = new Folder[1];
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Folder result=realm.where(Folder.class).equalTo("ID",ID).findFirst();
                folder[0] =result;
            }
        });
        return folder[0];
    }

    //xz ili tak
    public void addWordsToFolder(final RealmList<Word> words, final Folder folder){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                folder.setWords(words);
            }
        });
    }
    public void addWordToFolder(final Word word,final  Folder folder){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                folder.setWord(word);
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

    public Word getWordById(final int id) {
        final Word[] word = new Word[1];
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Word result=realm.where(Word.class).equalTo("ID",id).findFirst();
                word[0] =result;
            }
        });
        return word[0];
    }
}
