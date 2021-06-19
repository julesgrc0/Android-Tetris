package com.julesg10.tetris;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    MainSurface mainSurface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getSupportActionBar().hide();
        this.mainSurface = new MainSurface(this);
        this.setContentView(this.mainSurface);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.mainSurface = new MainSurface(this);
        this.setContentView(this.mainSurface);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mainSurface.saveSettings();
    }
}