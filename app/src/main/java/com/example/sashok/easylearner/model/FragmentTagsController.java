package com.example.sashok.easylearner.model;

/**
 * Created by sashok on 26.9.17.
 */

public class FragmentTagsController {

    public static  String  TAG_LIST_FOLDER="TAG_LIST_FOLDER";

    public static  String  TAG_SHOW_CARD="TAG_SHOW_CARD";
    public static  String  TAG_ADD_FOLDER="TAG_ADD_FOLDER";
    public static  String  TAG_SEARCH_INTERNER="TAG_SEARCH_INTERNER" ;
    public static String toString(FragmentTags tag){
       return tag.name();
    }
}
