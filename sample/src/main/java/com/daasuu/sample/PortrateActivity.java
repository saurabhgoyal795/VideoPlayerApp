package com.daasuu.sample;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import javax.xml.transform.Result;

public class PortrateActivity extends BaseCameraActivity {
    JSONObject jsonObj = new JSONObject();
    int lessonNo = 1;
    String fileName = "";
    String data = "";

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, PortrateActivity.class);
        activity.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portrate);
        lessonNo = getIntent().getIntExtra("lessonNo", 1);
        try {
            jsonObj = new JSONObject(getIntent().getStringExtra("listObject"));
            fileName = jsonObj.getString("filename");
            data = jsonObj.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        onCreateActivity(lessonNo,fileName, data);
        videoWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        videoHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        cameraWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        cameraHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        recordBtn.setOnClickListener(v -> {

            if (recordBtn.getText().equals(getString(R.string.app_record))) {
                filepath = getVideoFilePath(lessonNo,fileName);
                cameraRecorder.start(filepath);
                recordBtn.setText("Stop");
            } else {
                cameraRecorder.stop();
                recordBtn.setText(getString(R.string.app_record));
            }

        });
//        videoWidth = 1080;
//        videoHeight = 1980;
//        cameraWidth = 1280;
//        cameraHeight = 720;
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK);
        super.onBackPressed();
    }

}
