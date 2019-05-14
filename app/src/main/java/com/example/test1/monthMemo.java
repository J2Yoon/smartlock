package com.example.test1;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by 또리또리 on 2019-03-23.
 */

public class monthMemo extends Activity {
    private final int SIG_MEMO=112;
    private CalendarView calendar;
    private FloatingActionButton pbtn;
    private String dayresult;
    private Fragment fr;
    private String sday;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comple_lock);

        calendar = findViewById(R.id.calendarView);
        pbtn = findViewById(R.id.floatingplus);
        pbtn.setOnClickListener(pluslistener);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                // TODO Auto-generated method stub
                sday=year+"-"+(month+1)+"-"+dayOfMonth;
                todolistFragment();
                // todolistFragment();
            }
        });

    }

    FloatingActionButton.OnClickListener pluslistener = new FloatingActionButton.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), getMemo.class);
            startActivityForResult(intent,SIG_MEMO);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case SIG_MEMO:
                if(resultCode==RESULT_OK)
                {
                    todolistFragment();
                }
                break;
        }
    }


    public void todolistFragment() {
        Fragment fr=new todoFragment();
        Bundle bundle=new Bundle();
        bundle.putString("day",sday);
        Log.e("산타",sday);
        fr.setArguments(bundle);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.todofrag, fr);
        fragmentTransaction.commit();
    }

}
