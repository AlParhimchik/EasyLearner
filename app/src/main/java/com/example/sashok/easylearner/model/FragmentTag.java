package com.example.sashok.easylearner.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sashok on 26.9.17.
 */

public enum FragmentTag implements Parcelable {

    TAG_LIST_FOLDER("TAG_LIST_FOLDER"),
    TAG_SHOW_CARD("TAG_SHOW_CARD"),
    TAG_ADD_FOLDER("TAG_ADD_FOLDER"),
    TAG_SEARCH_INTERNER("TAG_SEARCH_INTERNER"),
    TAG_DEFAULT_FRAGMENT("TAG_DEFAULT_FRAGMENT");

    private FragmentTag(String name) {
        fragmentName = name;
    }

    private String fragmentName;

    public FragmentTag fromString(String tag) {
        return FragmentTag.valueOf(tag);
    }

    public String getFragmentName() {
        return this.fragmentName;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FragmentTag> CREATOR = new Creator<FragmentTag>() {
        @Override
        public FragmentTag createFromParcel(Parcel in) {
            return FragmentTag.values()[in.readInt()];
        }

        @Override
        public FragmentTag[] newArray(int size) {
            return new FragmentTag[size];
        }
    };
}
