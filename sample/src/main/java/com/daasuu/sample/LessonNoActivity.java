package com.daasuu.sample;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daasuu.sample.widget.CommonUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

public class LessonNoActivity extends AppCompatActivity {
    int lessonNo = 1;
    private ListFetchTask mListFetchTask;
    public static String LESSON_LIST_REFRESH = "com.lesson.list.refresh";
    RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    private LessonListItemAdataper adapter;
    ArrayList<JSONObject> list = new ArrayList<>();
    TextView lessonNameTextView;
    ProgressBar progressBar1;
    String lessonName;
    EditText lessonNoEditText;
    EditText changeSpeedEditText;
    TextView lessonTextView;
    JSONObject appStringObject;
    boolean isFromSheet = false;
    private Menu menu;
    SharedPreferences prefs;


    private BroadcastReceiver event = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setList();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_no);
        readFile();
        lessonTextView = findViewById(R.id.lessonno);
        lessonTextView.setText("Lesson No : "+lessonNo);
        lessonNoEditText = findViewById(R.id.lessonNoEditText);
        changeSpeedEditText = findViewById(R.id.changeSpeedEditText);
        lessonNameTextView = findViewById(R.id.lessonname);
        prefs = this.getSharedPreferences(
                "com.daasuu.sample", Context.MODE_PRIVATE);

        if (prefs.getBoolean("googleSheet", false) == false){
            isFromSheet = false;
        } else {
            isFromSheet = true;

        }
        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               lessonNo=Integer.parseInt(lessonNoEditText.getText().toString().trim());
                getList();
            }
        });
        findViewById(R.id.submitSpeed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (changeSpeedEditText.getText().toString().trim() != ""){
                    prefs.edit().putInt("speedControl", Integer.parseInt(changeSpeedEditText.getText().toString().trim())).commit();
                    Toast.makeText(LessonNoActivity.this, "Speed Updated Successfully",Toast.LENGTH_SHORT).show();
                    findViewById(R.id.speedLayout).setVisibility(View.GONE);
                }

            }
        });
        mRecyclerView = findViewById(R.id.my_recycler_view);
        if (mRecyclerView != null) {
            mRecyclerView.setHasFixedSize(true);
        }
        //using staggered grid pattern in recyclerview
        mLayoutManager = new GridLayoutManager(getApplicationContext(),1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(event, new IntentFilter(LESSON_LIST_REFRESH));

        getList();

        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(LessonNoActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == 1) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(LessonNoActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        2);
            }
        } else if (requestCode == 2) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(LessonNoActivity.this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        3);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void readFile() {
        BufferedReader reader = null;
        StringBuilder buf = new StringBuilder();
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("appstring.json"), "UTF-8"));

            String mLine;
            while ((mLine = reader.readLine()) != null) {
                buf.append(mLine);
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        try {
            appStringObject = new JSONObject(buf.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_process_order, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (prefs.getBoolean("googleSheet", false) == false){
            menu.findItem(R.id.google_sheet).setTitle("Switch to sheet");
        } else {
            menu.findItem(R.id.google_sheet).setTitle("Switch to Database");
        }
        return super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                findViewById(R.id.lessonLayout).setVisibility(View.VISIBLE);
                return true;
            case R.id.changeSpeed:
                findViewById(R.id.speedLayout).setVisibility(View.VISIBLE);
                return true;
            case R.id.google_sheet:
                if (item.getTitle().toString().trim().equalsIgnoreCase("Switch to sheet")) {
                    isFromSheet = true;
                    prefs.edit().putBoolean("googleSheet", true).commit();
                    item.setTitle("Switch to Database");
                } else {
                    isFromSheet = false;
                    prefs.edit().putBoolean("googleSheet", false).commit();
                    item.setTitle("Switch to sheet");
                }
                getList();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

        private void setList(){
            findViewById(R.id.lessonLayout).setVisibility(View.GONE);
            lessonTextView.setText("Lesson No : "+lessonNo);
            lessonNameTextView.setText(lessonName);
        if (adapter == null){
            adapter = new LessonListItemAdataper(list,R.layout.lessonlistitem,LessonNoActivity.this,lessonNo);
            mRecyclerView.setAdapter(adapter);
        }else{
            adapter.refreshAdapter(list);
        }
    }

    private void getList(){
        if(mListFetchTask != null){
            mListFetchTask.cancel(true);
        }
        mListFetchTask = new ListFetchTask();
        mListFetchTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    class ListFetchTask extends AsyncTask<Void, Void , Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                String url = "https://helloenglish.com/getAudioStrings.action?language=Hindi&lessonNumber="+(300+lessonNo)+"&lessonNumberVar=0";
                if (isFromSheet) {
                    url = "http://mail.culturealley.com/english-app/utility/getLessonVideoData.php?lessonNumber="+lessonNo;
                }
                String response = ServerInterface.doSync(url);
                try {
                    JSONObject responseObject= null;
                    JSONArray dataArray= null;
                    if(isFromSheet) {
                        dataArray= new JSONArray(response);
                    } else {
                        responseObject= new JSONObject(response);
                        lessonName = responseObject.optString("lessonName");
                        dataArray=responseObject.getJSONArray("stringArray");
                    }

                    list = new ArrayList<>();
                    for (int i = 0; i<dataArray.length();i++){
                        JSONObject obj = dataArray.getJSONObject(i);
                        if(obj.has("file_name")){
                            obj.put("filename", obj.getString("file_name").replaceAll(".mp3", "").replaceAll(".mp4","")+ ".mp4");
                        } else {
                            obj.put("filename", obj.getString("filename").replaceAll(".mp3", "").replaceAll(".mp4","")+ ".mp4");
                        }
                        String filename = obj.getString("filename");
                        String filePath = BaseCameraActivity.getVideoFilePath(lessonNo,filename);
                        File file = new File(filePath);
                        if (file.exists()){
                            obj.put("fileExist", true);
                        } else {
                            obj.put("fileExist", false);
                        }
                        if (isFromSheet && obj.has("data_english")) {
                            obj.put("data", CommonUtility.replaceVariable(obj.getString("data_english"),appStringObject));
                        } else {
                            obj.put("data", CommonUtility.replaceVariable(obj.getString("data"),appStringObject));
                        }
                        list.add(obj);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(LessonNoActivity.LESSON_LIST_REFRESH));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode,resultCode,data);
        Log.d("ActivityResult", "hi i am called requestCode: "+requestCode + " resultCode: "+resultCode);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
                for (int i = 0; i < list.size(); i++) {
                    JSONObject obj = list.get(i);
                    String filename = null;
                    try {
                        filename = obj.getString("filename");
                        String filePath = BaseCameraActivity.getVideoFilePath(lessonNo, filename);
                        File file = new File(filePath);
                        if (file.exists()) {
                            obj.put("fileExist", true);
                            list.set(i, obj);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                setList();
        }
    }
    class LessonListItemAdataper extends RecyclerView.Adapter<LessonListItemAdataper.LessonViewHolder> {

        private ArrayList<JSONObject> items;
        private int lesssonNo;
        private int rowLayout;
        private Context context;
        private int currentPosition = 0;

        protected class LessonViewHolder extends RecyclerView.ViewHolder {
            TextView cardname;
            TextView filename;
            CardView cardView;
            ImageView previewButton;


            public LessonViewHolder(View v) {
                super(v);
                cardname = v.findViewById(R.id.text);
                cardView = v.findViewById(R.id.card_view);
                filename = v.findViewById(R.id.text2);
                previewButton = v.findViewById(R.id.previewButton);
            }
        }

        public LessonListItemAdataper(ArrayList<JSONObject> list, int rowLayout, Context context, int lesssonNo) {
            this.items = list;
            this.rowLayout = rowLayout;
            this.context = context;
            this.lesssonNo = lesssonNo;
        }


        @Override
        public LessonListItemAdataper.LessonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;
            view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
            return new LessonListItemAdataper.LessonViewHolder(view);

        }


        @Override
        public void onBindViewHolder(final LessonListItemAdataper.LessonViewHolder holder, int position) {
            try {
                holder.cardname.setText(Html.fromHtml(items.get(position).getString("data")));
                holder.filename.setText(items.get(position).getString("filename"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (items.get(position).getBoolean("fileExist") == true) {
                    holder.previewButton.setVisibility(View.VISIBLE);
                } else {
                    holder.previewButton.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            holder.previewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String filename = null;
                    try {
                        filename = items.get(position).getString("filename");
                        String filePath = BaseCameraActivity.getVideoFilePath(lessonNo,filename);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(filePath));
                        intent.setDataAndType(Uri.parse(filePath), "video/mp4");
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            CardView RLContainer = holder.cardView;

            View.OnClickListener mClickListener;

            final int pos = position;
            mClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentPosition = pos;
                    Intent intent = new Intent(context, PortrateActivity.class);
                    intent.putExtra("listObject", items.toString());
                    intent.putExtra("lessonNo", lesssonNo);
                    intent.putExtra("pos", pos);
                    startActivityForResult(intent, 1);
                }
            };
            RLContainer.setOnClickListener(mClickListener);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public  int getCurrentPosition() {
            return currentPosition;
        }

        public void refreshAdapter(ArrayList<JSONObject> items) {
            this.items = items;
            notifyDataSetChanged();
        }



        public ArrayList<JSONObject> getList() {
            return items;
        }


    }

}




