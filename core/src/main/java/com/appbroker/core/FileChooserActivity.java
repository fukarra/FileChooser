package com.appbroker.core;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appbroker.core.adapter.ChooserListAdapter;
import com.appbroker.core.object.FileObject;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;

public class FileChooserActivity extends AppCompatActivity {
    private final int FILE_SELECT_REQUEST_CODE=0;
    private final int STORAGE_PERMISSION_REQUEST_CODE=999;

    public final static String TAG_MIME_TYPE ="mime_type";
    public final static String TAG_CHOOSER_TITLE ="chooser_title";
    public final static String TAG_USE_DEFAULT_FILE_EXPLORER ="use_default_file_explorer";

    private String _searchMimeType;
    private String _chooserTitle;
    private boolean _useDefaultFileExplorer;

    /*
    * Attributes for integrated file explorer
    */
    private boolean isRoot;
    private String rootPath;
    private String currentPath;
    boolean isRunning=false;

    private ImageView homeButton;
    private ListView listView;
    private TextView titleView;
    private ProgressBar progressBar;
    private LinearLayout emptyLL;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assignIntent(getIntent());

        if (isStoragePermissionGranted()){
            run();
        }
    }

    private void assignIntent(final Intent intent) {
        String s;
        if ((s=intent.getStringExtra(TAG_MIME_TYPE))!=null){
            this._searchMimeType =s;
        }
        if ((s=intent.getStringExtra(TAG_CHOOSER_TITLE))!=null){
            this._chooserTitle=s;
        }
        this._useDefaultFileExplorer=intent.getBooleanExtra(TAG_USE_DEFAULT_FILE_EXPLORER,false);

    }
    private void showLoading(){
        listView.setVisibility(View.GONE);
        emptyLL.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }
    private void showLoaded(boolean isEmpty){
        if (isEmpty){
            progressBar.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            emptyLL.setVisibility(View.VISIBLE);
        }else {
            emptyLL.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);

        }

    }
    private void run(){
        if (_useDefaultFileExplorer){
            Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType(this._searchMimeType);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            try {
                startActivityForResult(Intent.createChooser(intent, this._chooserTitle), FILE_SELECT_REQUEST_CODE);
            }catch (android.content.ActivityNotFoundException e){
                e.printStackTrace();
                setResult(RESULT_CANCELED);
                finish();
            }
        }else {
            setContentView(R.layout.activity_file_chooser);

            Toolbar toolbar=findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            if (getSupportActionBar()!=null){
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setDisplayShowTitleEnabled(false);

            }

            titleView=findViewById(R.id.activity_chooser_path_text);
            listView=findViewById(R.id.activity_chooser_list_view);
            progressBar=findViewById(R.id.activity_chooser_loading_progress);
            emptyLL=findViewById(R.id.activity_chooser_empty);
            rootPath = Environment.getExternalStorageDirectory().getPath();
            goToDir(rootPath);
        }

    }
    private void goToDir(String path){
        currentPath=path;
        FillDirList fillDirList=new FillDirList();
        fillDirList.execute(path);
    }

    private void goUpDir(){
        File file=new File(currentPath);
        goToDir(file.getParentFile().getPath());
    }

    private class FillDirList extends AsyncTask<String,Void,Void>{
        private ChooserListAdapter chooserListAdapter;
        private ArrayList<FileObject> fileObjects;
        boolean isEmpty=false;

        @Override
        protected void onPostExecute(Void aVoid) {
            isRunning=false;
            titleView.setText(currentPath);
            listView.setAdapter(chooserListAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final String path=((FileObject)chooserListAdapter.getItem(position)).get_path();
                    if (((FileObject)chooserListAdapter.getItem(position)).is_isFolder()){
                        goToDir(((FileObject)chooserListAdapter.getItem(position)).get_path());
                    }else {
                        final File file=new File(path);
                        Intent intent=new Intent();
                        setResult(RESULT_OK,intent.setData(Uri.parse(file.getAbsolutePath())));
                        finish();
                    }
                }
            });
            showLoaded(isEmpty);
        }

        @Override
        protected void onPreExecute() {
            showLoading();
        }

        @Override
        protected Void doInBackground(String... strings) {
            if (!isRunning){
                isRunning=true;
                fileObjects=new ArrayList<>();

                work(strings[0]);

            }
            return null;
        }

        private void work(final String path) {
            final File file=new File(path);
            final File[] files=file.listFiles();
            if (path.equals(rootPath)){
                isRoot=true;
            }else {
                isRoot= false;
            }
            if (files.length==0){
                isEmpty=true;
            }
            for (int i=0;i<files.length;i++){
                Log.d("file",files[i].getName());
                fileObjects.add(new FileObject(FileChooserActivity.this,files[i].getPath()));
            }
            chooserListAdapter = new ChooserListAdapter(FileChooserActivity.this,fileObjects);
        }
    }

    @Override
    public void onBackPressed() {
        if (isRoot){
            setResult(RESULT_CANCELED);
            finish();
        }else {
            goUpDir();
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
