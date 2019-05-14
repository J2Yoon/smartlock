package com.example.test1;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.test1.MyBroadCast.MyReceiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 또리또리 on 2019-02-24.
 */

public class makeweekLock extends Activity {

    private TimePicker start, end;
    private String result = "";
    private CheckBox cbm, cbtu, cbw, cbth, cbf, cbsa, cbsu;
    private Button confimrbtn;
    private weekDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new weekDBHelper(getApplicationContext(), "WEEKALARM.db", null, 1);
        setviews();
    }

    public void setviews() {
        setContentView(R.layout.makeweeklock);
        confimrbtn = (Button) findViewById(R.id.confirmbtn);
        confimrbtn.setOnClickListener(confirmListener);
        start = (TimePicker) findViewById(R.id.start_t);
        end = (TimePicker) findViewById(R.id.end_t);
        setweeknumbers();
    }

    public void setweeknumbers() {
        cbm = (CheckBox) findViewById(R.id.cb_m);
        cbtu = (CheckBox) findViewById(R.id.cb_tu);
        cbw = (CheckBox) findViewById(R.id.cb_w);
        cbth = (CheckBox) findViewById(R.id.cb_th);
        cbf = (CheckBox) findViewById(R.id.cb_f);
        cbsa = (CheckBox) findViewById(R.id.cb_sa);
        cbsu = (CheckBox) findViewById(R.id.cb_su);
    }



    Button.OnClickListener confirmListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                int s_h = start.getHour();
                int s_m = start.getMinute();
                int e_h = end.getHour();
                int e_m = end.getMinute();
                String w_m = getcheckweekres();
                Log.d("윅",w_m);
                Log.d("시작", "start : " + s_h + ":" + s_m);
                Log.d("끝", "end : " + e_h + ":" + e_m);

                SimpleDateFormat dateFormat = new SimpleDateFormat("hhmmssss");
                int alarm_id = Integer.valueOf(dateFormat.format(new Date(System.currentTimeMillis())));

                if(!dbHelper.isExist(0,w_m,s_h + ":" + s_m)&&!dbHelper.isExist(1,w_m,e_h + ":" + e_m)) {
                    addAlarm(s_h, s_m, w_m, "start", alarm_id);
                    addAlarm(e_h, e_m, w_m, "end", alarm_id + 1);

                    Toast.makeText(getApplicationContext(), "정기잠금이 생성되었습니다.", Toast.LENGTH_LONG).show();
                    finishActivity(123);
                    setResult(RESULT_OK,getIntent());
                    Log.e("여기다2","111");
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "존재하는 잠금입니다.", Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    public String getcheckweekres() {
        String result = "";
        if (cbm.isChecked()) result += "2/";
        if (cbtu.isChecked()) result += "3/";
        if (cbw.isChecked()) result += "4/";
        if (cbth.isChecked()) result += "5/";
        if (cbf.isChecked()) result += "6/";
        if (cbsa.isChecked()) result += "7/";
        if (cbsu.isChecked()) result += "1";

        return result;
    }

    private void addAlarm(int h, int m, String w, String action, int alarm_id) {
        weekLockInfo item = new weekLockInfo();
        item.setAlarm_id(alarm_id);
        item.setWeeknum(w);
        item.setAlarm_op((action.equals("start") ? 0 : 1));
        item.setAlarm_time(h + ":" + m);
        dbHelper.insert(item.getAlarm_id(), item.getAlarm_op(), item.getWeeknum(), item.getAlarm_time());
    }
}