package com.appbroker.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class FileChooser {
    private int _requestCode;
    private String _mimeType="*/*";
    private String _chooserTitle ="Select file";
    private boolean _useDefaultFileExplorer=false;
    private Activity _activity;

    /*
    * Context artibute must belong an Activity
    * */
    public FileChooser(Context context,int requestCode) {
        this._activity=(Activity) context;
        this._requestCode=requestCode;
    }
    public FileChooser(Activity activity,int requestCode) {
        this._activity=activity;
        this._requestCode=requestCode;
    }

    public FileChooser withMimeType(String mimeType){
        this._mimeType=mimeType;
        return this;
    }
    public FileChooser withTitle(String title){
        this._chooserTitle=title;
        return this;
    }
    public FileChooser defaultFileChooser(boolean isEnabled){
        this._useDefaultFileExplorer=isEnabled;
        return this;
    }
    public void start(){
        Intent intent=new Intent(this._activity, FileChooserActivity.class);
        intent.putExtra(FileChooserActivity.TAG_MIME_TYPE,this._mimeType);
        intent.putExtra(FileChooserActivity.TAG_CHOOSER_TITLE,this._chooserTitle);
        intent.putExtra(FileChooserActivity.TAG_USE_DEFAULT_FILE_EXPLORER,_useDefaultFileExplorer);
        _activity.startActivityForResult(intent,_requestCode);
    }
}
