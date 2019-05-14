package com.example.test1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class MainActivity extends Activity {
    Button b_click;
    Button w_click;
    Button s_click;
    Button c_click;
    Button l_click;
    Button bl_click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b_click = (Button) findViewById(R.id.bustime);
        w_click = (Button) findViewById(R.id.weeklock);
        s_click = (Button) findViewById(R.id.stt);
        c_click = findViewById(R.id.complete);
        l_click = findViewById(R.id.sublist);
        bl_click=findViewById(R.id.blue);


        b_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, subwayList.class);
                startActivity(intent);
            }
        });

        w_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                 weekDBHelper dbHelper=new weekDBHelper(getApplicationContext(), "WEEKALARM.db", null, 1);
//                 dbHelper.alldelete();
//                dbHelper.close();
                Intent intent = new Intent(MainActivity.this, weekLock.class);
                startActivity(intent);
            }
        });

        s_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, sttTest.class);
                startActivity(intent);
            }
        });


        c_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*memoDBHelper helper = new memoDBHelper(getApplicationContext(), "Memo.db", null, 1);
                helper.alldelete();*/
                Intent intent = new Intent(MainActivity.this, monthMemo.class);
                startActivity(intent);
            }
        });

        l_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statnidDBHelper dbHelper = new statnidDBHelper(getApplicationContext(), "STATIONID.db", null, 1);
                dbHelper.alldelete();
                String test = null;
                try {
                    test = readTxt();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String[] result = test.split("\n");

                for (int i = 0; i < result.length; i++) {
                    String[] res = result[i].split(",");
                    dbHelper.insert(res[0], res[1]);
                }
                Log.e("호에엥",dbHelper.getResName("1002000213"));
            }
        });
    }

    private String readTxt() throws UnsupportedEncodingException {
        String data = null;
        InputStream inputStream = getResources().openRawResource(R.raw.statinfo);
        InputStreamReader txtReader=new InputStreamReader(inputStream,"euc-kr");
        BufferedReader txtbr = new BufferedReader(txtReader);

        String line=null;
        try {
            while ((line = txtbr.readLine()) != null) {
                if(!line.equals(""))
                    data+=line+"\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            inputStream.close();
            txtReader.close();
            txtbr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}