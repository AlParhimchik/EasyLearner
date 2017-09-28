package com.example.sashok.easylearner.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sashok on 26.9.17.
 */

public enum FragmentTags  implements Parcelable{
    TAG_LIST_FOLDER,
   TAG_SHOW_CARD,
    TAG_ADD_FOLDER ,
    TAG_SEARCH_INTERNER;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FragmentTags> CREATOR = new Creator<FragmentTags>() {
        @Override
        public FragmentTags createFromParcel(Parcel in) {
            return FragmentTags.values()[in.readInt()];
        }

        @Override
        public FragmentTags[] newArray(int size) {
            return new FragmentTags[size];
        }
    };
}
