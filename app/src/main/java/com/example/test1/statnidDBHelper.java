package com.example.test1;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by 또리또리 on 2019-03-24.
 */

public class statnidDBHelper  extends SQLiteOpenHelper {


    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public statnidDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE STATIONID (_id INTEGER PRIMARY KEY AUTOINCREMENT, statnid TEXT, statnname TEXT);");
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(String sid, String sname) {

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO STATIONID VALUES(null,'" + sid + "','" + sname+"')");
        db.close();
    }

    public void alldelete() {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("delete from STATIONID");
        db.close();
    }

    public String getResName(String sid) {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String result = "";
        Log.e("후잇",sid);
        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT statnname FROM STATIONID WHERE statnid='"+sid+"';", null);
        while (cursor.moveToNext()) {
            result = cursor.getString(0);
        }
        return result;
    }
}
