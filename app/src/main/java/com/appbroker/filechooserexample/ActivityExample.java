package com.appbroker.filechooserexample;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.appbroker.core.FileChooserActivity;

public class ActivityExample extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        Intent intent=new Intent(this, FileChooserActivity.class);
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case 0:
                if (data!=null){
                    Log.d("REQUEST 0",data.getDataString());
                    Toast.makeText(ActivityExample.this,data.getDataString(),Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
