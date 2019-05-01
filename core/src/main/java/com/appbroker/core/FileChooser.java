package com.appbroker.core;

import android.content.Context;

public class FileChooser {
    private Context mContext;
    private String mMimeType;
    public FileChooser() {
    }
    public FileChooser addMimeType(String mimeType){
        mMimeType=mimeType;
        return this;
    }
    public void run(){

    }
}
