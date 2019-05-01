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
    private String _mimeType ="*/*";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isStoragePermissionGranted()){
            run();
        }
    }

    public FileChooserActivity addMimeType(String mimeType){
        this._mimeType =mimeType;
        return this;
    }

    public void run(){
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(this._mimeType);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_REQUEST_CODE);
        }catch (android.content.ActivityNotFoundException e){
            e.printStackTrace();
            setResult(RESULT_CANCELED);
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
                    setResult(RESULT_CANCELED,data);
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