package com.appbroker.core;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class FileChooserActivity extends AppCompatActivity {
    private final int FILE_SELECT_REQUEST_CODE=0;
    private final int STORAGE_PERMISSION_REQUEST_CODE=999;

    public final static String TAG_MIME_TYPE ="mime_type";
    public final static String TAG_CHOOSER_TITLE ="chooser_title";
    public final static String TAG_USE_DEFAULT_FILE_EXPLORER ="use_default_file_explorer";

    private String _mimeType;
    private String _chooserTitle;
    private boolean _useDefaultFileExplorer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActionBar()!=null){
            getActionBar().hide();
        }else if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        assignIntent(getIntent());

        if (isStoragePermissionGranted()){
            run();
        }
    }

    private void assignIntent(final Intent intent) {
        String s;
        if ((s=intent.getStringExtra(TAG_MIME_TYPE))!=null){
            this._mimeType=s;
        }
        if ((s=intent.getStringExtra(TAG_CHOOSER_TITLE))!=null){
            this._chooserTitle=s;
        }
        this._useDefaultFileExplorer=intent.getBooleanExtra(TAG_USE_DEFAULT_FILE_EXPLORER,false);

    }

    private void run(){
        if (_useDefaultFileExplorer){
            Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType(this._mimeType);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            try {
                startActivityForResult(Intent.createChooser(intent, this._chooserTitle), FILE_SELECT_REQUEST_CODE);
            }catch (android.content.ActivityNotFoundException e){
                e.printStackTrace();
                setResult(RESULT_CANCELED);
            }
        }else {
            setContentView(R.layout.activity_file_chooser);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case FILE_SELECT_REQUEST_CODE:
                if (resultCode==RESULT_OK){
                    if (data!=null){
                        setResult(RESULT_OK,data);
                    }else {
                        setResult(RESULT_CANCELED);
                    }

                }else {
                    setResult(RESULT_CANCELED);
                }
                finish();
                break;
        }
    }
    private boolean isStoragePermissionGranted(){
        if(Build.VERSION.SDK_INT>=23){
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                return true;
            }else{
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST_CODE);
                return false;
            }
        }else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case STORAGE_PERMISSION_REQUEST_CODE:
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    run();
                }
                break;
        }
    }
}
