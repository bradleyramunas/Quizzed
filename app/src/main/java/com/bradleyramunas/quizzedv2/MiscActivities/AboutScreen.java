package com.bradleyramunas.quizzedv2.MiscActivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bradleyramunas.quizzedv2.R;

public class AboutScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_screen);
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}
