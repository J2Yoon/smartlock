package com.example.test1;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ThemedSpinnerAdapter;
import android.widget.Toast;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

/**
 * Created by 또리또리 on 2019-02-24.
 */

public class weekLock extends Activity{

    private final int SIG_MAKE=123;
    private FloatingActionButton plusbtn;
    private  RecyclerView RecyclerView1;
    private ArrayList<weekLockInfo> weekLockInfoArrayList;
    private weekLockAdapter myAdapter2;
    private weekDBHelper dbHelper;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weeklock);
        dbHelper = new weekDBHelper(getApplicationContext(), "WEEKALARM.db", null, 1);

        setviews();
        setrecycleviews();

        getweeklist();
        RecyclerView1.setAdapter(new weekLockAdapter(weekLockInfoArrayList));
    }

    Button.OnClickListener plusOnClickListener= new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(weekLock.this, makeweekLock.class);
            startActivityForResult(intent,SIG_MAKE);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        Log.e("여기다1",String.valueOf(requestCode));
        switch (requestCode)
        {
            case SIG_MAKE:
                if(resultCode==RESULT_OK)
                {
                    weekLockInfoArrayList.clear();
                    getweeklist();
                    RecyclerView1.getAdapter().notifyDataSetChanged();
                }
                break;
        }
    }


    public void setviews(){
        plusbtn = (FloatingActionButton) findViewById(R.id.plusbtn);
        plusbtn.setOnClickListener(plusOnClickListener);
    }

    public void setrecycleviews(){
        RecyclerView1 = findViewById(R.id.recycler_weeklock);
        weekLockInfoArrayList = new ArrayList<weekLockInfo>();
        RecyclerView1.setHasFixedSize(true);
        RecyclerView1.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
    }

    public void getweeklist() {
        String result = dbHelper.getResult();
        if (result != "") {
            String[] weekinfos = result.split(";");
            for (int i = 0; i < weekinfos.length; i++) {
                String weekinfoelem[] = weekinfos[i].split("&");
                Log.e("여기지롱", weekinfoelem[3]);
                int id = Integer.parseInt(weekinfoelem[0]);
                int op= Integer.parseInt(weekinfoelem[1]);
                weekLockInfoArrayList.add(new weekLockInfo(id,op, weekinfoelem[2], weekinfoelem[3]));
            }
        } else {
            myAdapter2 = new weekLockAdapter(weekLockInfoArrayList);
        }
    }
}



