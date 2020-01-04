package com.dell.projectsmart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){

            ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.CAMERA}, 100);
        }
    }

    public void login(View view){

        Intent intent = new Intent(this, BarCodeScannerActivity.class);
        EditText editText = (EditText) findViewById(R.id.email);
        String userName = editText.getText().toString();
        String text = "";
        if(!userName.isEmpty())
        {
            text = String.valueOf(userName.charAt(0)).toUpperCase() + userName.substring(1).toLowerCase();
        }
        intent.putExtra("userName", text);
        startActivity(intent);

    }
}
