package com.sdf.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mAudioBtn;
    private Button mPicBtn;
    private Button mVideoBtn;
    private RecyclerView mAudioRecycler;
    private RecyclerView mPicRecycler;
    private RecyclerView mVideoRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAudioBtn = (Button) findViewById(R.id.btn_audio);
        mPicBtn = (Button) findViewById(R.id.btn_pic);
        mVideoBtn = (Button) findViewById(R.id.btn_video);
        mAudioRecycler = (RecyclerView) findViewById(R.id.recycler_audio);
        mPicRecycler = (RecyclerView) findViewById(R.id.recycler_pic);
        mVideoRecycler = (RecyclerView) findViewById(R.id.recycler_video);
    }
}
