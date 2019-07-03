package com.daasuu.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SquareActivity extends BaseCameraActivity {
    String fileName = "";


    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, SquareActivity.class);
        activity.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square);
        onCreateActivity(1,fileName,"");
    }
}
