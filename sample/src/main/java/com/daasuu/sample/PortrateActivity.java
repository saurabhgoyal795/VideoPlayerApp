package com.daasuu.sample;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.opengl.GLException;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daasuu.camerarecorder.CameraRecordListener;
import com.daasuu.camerarecorder.CameraRecorder;
import com.daasuu.camerarecorder.CameraRecorderBuilder;
import com.daasuu.camerarecorder.LensFacing;
import com.daasuu.sample.widget.Filters;
import com.daasuu.sample.widget.SampleGLView;
import com.daasuu.sample.widget.ScrollTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.opengles.GL10;
import javax.xml.transform.Result;

public class PortrateActivity extends AppCompatActivity {
    JSONObject jsonObj = new JSONObject();
    JSONArray jsonArray = new JSONArray();
    int lessonNo = 1;
    String fileName = "";
    String data = "";
    private SampleGLView sampleGLView;
    protected CameraRecorder cameraRecorder;
    protected String filepath;
    protected TextView recordBtn;
    ScrollTextView scrollTextView;
    TextView textSizeText;
    TextView speedControlText;
    ImageView playButton;
    SeekBar seekbar;
    SeekBar speedControlSeekBar;
    TextView normalTextView;
    ListView list;
    protected LensFacing lensFacing = LensFacing.FRONT;
    protected int cameraWidth = 1280;
    protected int cameraHeight = 720;
    protected int videoWidth = 720;
    protected int videoHeight = 720;
    int position = 0;
    private AlertDialog filterDialog;
    private boolean toggleClick = false;
    private float density_global = 0;
    int step = 1;
    int max = 60;
    int min = 20;
    int textSize = 32;
    int speedControlSize = 10;
    int max2 = 30;
    int min2 = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portrate);
        density_global = getResources().getDisplayMetrics().density;
        getSupportActionBar().hide();
        takeKeyEvents(true);

        lessonNo = getIntent().getIntExtra("lessonNo", 1);
        position = getIntent().getIntExtra("pos", 0);

        try {
            jsonArray = new JSONArray(getIntent().getStringExtra("listObject"));
            jsonObj = jsonArray.getJSONObject(position);
            fileName = jsonObj.getString("filename");
            data = jsonObj.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        takeKeyEvents(true);

        recordBtn = findViewById(R.id.btn_record);
        seekbar = findViewById(R.id.textSizeSeekBar);
        textSizeText= findViewById(R.id.textSizeText);
        speedControlSeekBar = findViewById(R.id.speedSeekBar);
        speedControlText = findViewById(R.id.speedControlText);
        playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.smoothScrollToPositionFromTop(1,0, speedControlSize * 1000);
                playButton.setVisibility(View.GONE);
            }
        });
        findViewById(R.id.btn_flash).setOnClickListener(v -> {
            if (cameraRecorder != null && cameraRecorder.isFlashSupport()) {
                cameraRecorder.switchFlashMode();
                cameraRecorder.changeAutoFocus();
            }
        });

        findViewById(R.id.btn_switch_camera).setOnClickListener(v -> {
            releaseCamera();
            if (lensFacing == LensFacing.BACK) {
                lensFacing = LensFacing.FRONT;
            } else {
                lensFacing = LensFacing.BACK;
            }
            toggleClick = true;
        });
        list = findViewById(R.id.list);

        findViewById(R.id.btn_filter).setOnClickListener(v -> {
            list.smoothScrollToPositionFromTop(1,0, speedControlSize * 1000);

            if (filterDialog == null) {

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Choose a filter");
                builder.setOnDismissListener(dialog -> {
                    filterDialog = null;
                });

                final Filters[] filters = Filters.values();
                CharSequence[] charList = new CharSequence[filters.length];
                for (int i = 0, n = filters.length; i < n; i++) {
                    charList[i] = filters[i].name();
                }
                builder.setItems(charList, (dialog, item) -> {
                    changeFilter(filters[item]);
                });
                filterDialog = builder.show();
            } else {
                filterDialog.dismiss();
            }

        });
//        videoWidth = 1080;
//        videoHeight = 1980;
//        cameraWidth = 1280;
//        cameraHeight = 720;
        videoWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        videoHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        cameraWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        cameraHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        recordBtn.setOnClickListener(v -> {

            if (recordBtn.getText().equals(getString(R.string.app_record))) {
                list.smoothScrollToPositionFromTop(1,0, speedControlSize * 1000);
                filepath = getVideoFilePath(lessonNo,fileName);
                cameraRecorder.start(filepath);
                recordBtn.setText("Stop");
            } else {
                cameraRecorder.stop();
                recordBtn.setText(getString(R.string.app_record));
            }

        });


        ArrayList<String> items = new ArrayList<>();
        items.add(data);
        items.add("");

        MyListAdapter adapter=new MyListAdapter(this, items);
        list=(ListView)findViewById(R.id.list);

        list.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_UP:
                        playButton.setVisibility(View.VISIBLE);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                }
                return false;
            }
        });
        new Handler().postDelayed(new Runnable() {
            public void run() {
                list.setAdapter(adapter);
                }
             }, 500);
        seekbar.setMax(max - min);
        seekbar.setProgress(textSize -min);
        speedControlSeekBar.setMax(max2 - min2);
        speedControlSeekBar.setProgress(speedControlSize - min2);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                textSize = min + progress;
                textSizeText.setText("TextSize: "+textSize);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (adapter !=null) {
                    adapter.refreshAdapter();
                }
            }
        });

        speedControlSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                speedControlSize = min2 + progress;
                speedControlText.setText("Speed: "+speedControlSize + "sec");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK);
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseCamera();
    }

    private void startScrollAnimation(int height) {
        ValueAnimator marginTopAnimation = ValueAnimator.ofInt((int) (250 * density_global), (int) (-height + (150 * (density_global))));
        marginTopAnimation.setDuration(10000); // milliseconds
        marginTopAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {

                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) normalTextView.getLayoutParams();
                layoutParams.topMargin = (int)animator.getAnimatedValue();
                normalTextView.setLayoutParams(layoutParams);
            }
        });
        marginTopAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                normalTextView.clearAnimation();


//                marginTopAnimation.setBackgroundColor(Color.parseColor("#80000000"));
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        marginTopAnimation.start();
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        Log.d("keycode", "keycode"+ keyCode);

        switch (keyCode) {

            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    Log.i("VOL_UP_pressed", String.valueOf(event.getKeyCode()));
                    Toast.makeText(getApplication(), "IOS button clicked", Toast.LENGTH_SHORT).show();
                }
                return true;

            case KeyEvent.KEYCODE_ENTER:
                if(action==KeyEvent.ACTION_DOWN){
                    Log.i("ENTER_pressed", String.valueOf(event.getKeyCode()));
                    Toast.makeText(getApplication(), "ANDROID button clicked", Toast.LENGTH_SHORT).show();
                }
            default:
                return super.dispatchKeyEvent(event);
        }
    }



    private void releaseCamera() {
        if (sampleGLView != null) {
            sampleGLView.onPause();
        }

        if (cameraRecorder != null) {
            cameraRecorder.stop();
            cameraRecorder.release();
            cameraRecorder = null;
        }

        if (sampleGLView != null) {
            ((FrameLayout) findViewById(R.id.wrap_view)).removeView(sampleGLView);
            sampleGLView = null;
        }
    }


    private void setUpCameraView() {
        runOnUiThread(() -> {
            FrameLayout frameLayout = findViewById(R.id.wrap_view);
            frameLayout.removeAllViews();
            sampleGLView = null;
            sampleGLView = new SampleGLView(getApplicationContext());
            sampleGLView.setTouchListener((event, width, height) -> {
                if (cameraRecorder == null) return;
                cameraRecorder.changeManualFocusPoint(event.getX(), event.getY(), width, height);
            });
            frameLayout.addView(sampleGLView);
        });
    }


    private void setUpCamera() {
        setUpCameraView();

        cameraRecorder = new CameraRecorderBuilder(this, sampleGLView)
                //.recordNoFilter(true)
                .cameraRecordListener(new CameraRecordListener() {
                    @Override
                    public void onGetFlashSupport(boolean flashSupport) {
                        runOnUiThread(() -> {
                            findViewById(R.id.btn_flash).setEnabled(flashSupport);
                        });
                    }

                    @Override
                    public void onRecordComplete() {
                        exportMp4ToGallery(getApplicationContext(), filepath);
                        try {
                            jsonArray = new JSONArray(getIntent().getStringExtra("listObject"));
                            jsonObj = jsonArray.getJSONObject(position + 1);
                            fileName = jsonObj.getString("filename");
                            data = jsonObj.getString("data");
//                            Toast.makeText(PortrateActivity.this,"Recoding will start in 5 sec", Toast.LENGTH_LONG).show();
//
//                            new Handler().postDelayed(new Runnable() {
//                                public void run() {
//                                    recordBtn.performClick();
//                                }
//                            }, 5000);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        onBackPressed();

                    }

                    @Override
                    public void onRecordStart() {

                    }

                    @Override
                    public void onError(Exception exception) {
                        Log.e("CameraRecorder", exception.toString());
                    }

                    @Override
                    public void onCameraThreadFinish() {
                        if (toggleClick) {
                            runOnUiThread(() -> {
                                setUpCamera();
                            });
                        }
                        toggleClick = false;
                    }
                })
                .videoSize(videoWidth, videoHeight)
                .cameraSize(cameraWidth, cameraHeight)
                .lensFacing(LensFacing.FRONT)
                .build();


    }

    private void changeFilter(Filters filters) {
        cameraRecorder.setFilter(Filters.getFilterInstance(filters, getApplicationContext()));
    }


    private interface BitmapReadyCallbacks2 {
        void onBitmapReady(Bitmap bitmap);
    }

    private void captureBitmap(final BitmapReadyCallbacks2 bitmapReadyCallbacks) {
        sampleGLView.queueEvent(() -> {
            EGL10 egl = (EGL10) EGLContext.getEGL();
            GL10 gl = (GL10) egl.eglGetCurrentContext().getGL();
            Bitmap snapshotBitmap = createBitmapFromGLSurface(sampleGLView.getMeasuredWidth(), sampleGLView.getMeasuredHeight(), gl);

            runOnUiThread(() -> {
                bitmapReadyCallbacks.onBitmapReady(snapshotBitmap);
            });
        });
    }

    private Bitmap createBitmapFromGLSurface(int w, int h, GL10 gl) {

        int bitmapBuffer[] = new int[w * h];
        int bitmapSource[] = new int[w * h];
        IntBuffer intBuffer = IntBuffer.wrap(bitmapBuffer);
        intBuffer.position(0);

        try {
            gl.glReadPixels(0, 0, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, intBuffer);
            int offset1, offset2, texturePixel, blue, red, pixel;
            for (int i = 0; i < h; i++) {
                offset1 = i * w;
                offset2 = (h - i - 1) * w;
                for (int j = 0; j < w; j++) {
                    texturePixel = bitmapBuffer[offset1 + j];
                    blue = (texturePixel >> 16) & 0xff;
                    red = (texturePixel << 16) & 0x00ff0000;
                    pixel = (texturePixel & 0xff00ff00) | red | blue;
                    bitmapSource[offset2 + j] = pixel;
                }
            }
        } catch (GLException e) {
            Log.e("CreateBitmap", "createBitmapFromGLSurface: " + e.getMessage(), e);
            return null;
        }

        return Bitmap.createBitmap(bitmapSource, w, h, Bitmap.Config.ARGB_8888);
    }

    public void saveAsPngImage(Bitmap bitmap, String filePath) {
        try {
            File file = new File(filePath);
            FileOutputStream outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void exportMp4ToGallery(Context context, String filePath) {
        final ContentValues values = new ContentValues(2);
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
        values.put(MediaStore.Video.Media.DATA, filePath);
        context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                values);
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse("file://" + filePath)));
    }

    public static String getVideoFilePath(int lessonNo, String fileName) {
        String root = Environment.getExternalStorageDirectory().toString();

        File myDir = new File(root + "/HelloEnglishLesson");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        File myDir2 = new File(myDir.getAbsolutePath() + "/Lesson "+lessonNo);
        if (!myDir2.exists()) {
            myDir2.mkdirs();
        }
        return myDir2.getAbsolutePath() + "/" + fileName;

    }

    public class MyListAdapter extends ArrayAdapter<String> {

        private final Activity context;
        private final ArrayList<String> dataarray;

        public MyListAdapter(Activity context, ArrayList<String> dataarray) {
            super(context,R.layout.item,dataarray);
            // TODO Auto-generated constructor stub

            this.context=context;
            this.dataarray=dataarray;

        }

        public View getView(int position,View view,ViewGroup parent) {
            LayoutInflater inflater=context.getLayoutInflater();
            View rowView=inflater.inflate(R.layout.item, null,true);

            TextView titleText = (TextView) rowView.findViewById(R.id.title);

            titleText.setText(Html.fromHtml(dataarray.get(position)));
            titleText.setTextSize(textSize);
            if (position == 0){
//                rowView.setBackgroundColor(getResources().getColor(R.color.colorBlack50));
            }
            if(dataarray.get(position).trim().equalsIgnoreCase("")){
                titleText.setPadding(0,0,0,0);
            }
            return rowView;

        }

        public void refreshAdapter() {
            notifyDataSetChanged();
        }
    }

}
