package com.example.test1.MyBroadCast;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyReceiver extends BroadcastReceiver {
    // 파이어베이스 연동
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Calendar cday= Calendar.getInstance();
        Log.e("요일", Integer.toString(cday.get(cday.DAY_OF_WEEK)));
        if (Integer.parseInt(intent.getStringExtra("weekend")) == cday.get(cday.DAY_OF_WEEK)) {
            if (intent.getExtras().getInt("action")==0) {
                Log.e("시작점", "파베시작");
                //파이어베이스 디비에서 신호 값을 1로 변경
            } else if(intent.getExtras().getInt("action")==1) {
                Log.e("종료점", "파베종료");
                //파베 디비 신호값 0 변경
            }
        }
    }
}
