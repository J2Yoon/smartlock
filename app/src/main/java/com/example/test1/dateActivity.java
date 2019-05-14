package com.example.test1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

/**
 * Created by 또리또리 on 2019-03-30.
 */

public class dateActivity extends Activity {

    private Button datestorbtn;
    private DatePicker dpicker;
    private String dresult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dateactivity);

        datestorbtn=findViewById(R.id.datebutton);
        dpicker=findViewById(R.id.datePicker);

        dpicker.init(dpicker.getYear(), dpicker.getMonth(), dpicker.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear,
                                              int dayOfMonth) {
                        // TODO Auto-generated method stub

                        dresult=String.format("%d-%d-%d", year,monthOfYear + 1, dayOfMonth);
                    }
                });


        datestorbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dresult.equals("")){
                    Toast.makeText(getApplicationContext(),"선택된 날짜가 없습니다.",Toast.LENGTH_SHORT);
                }
                else {
                    Log.e("호호호잇",dresult);
                    Intent dintent=new Intent();
                    dintent.putExtra("day",dresult);
                    finishActivity(100);
                    setResult(RESULT_OK,dintent);
                    finish();
                }
            }
        });
    }
}