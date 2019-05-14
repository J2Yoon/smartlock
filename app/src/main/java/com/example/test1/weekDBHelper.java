package com.example.test1;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by 또리또리 on 2019-02-24.
 */


public class weekDBHelper extends SQLiteOpenHelper {

    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public weekDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE WEEKALARM (_id INTEGER PRIMARY KEY AUTOINCREMENT, alarm_id int, alarm_op int, weeknum TEXT, weektime TEXT);");
        Log.e("새테이블","새테이블");
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(int alarmid, int alarmop, String weeknum, String weektime) {

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO WEEKALARM VALUES(null,'" + alarmid + "','" + alarmop + "','" + weeknum + "','"+weektime+"')");
        db.close();
    }

    public void update(int id, int op, String num, String time) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        db.execSQL("UPDATE WEEKALARM SET alarm_op='" + op + "', weeknum='"+num+ "', weektime='"+time+"' WHERE alarm_id='"+id+"';");
        db.close();
    }

    public void delete(int alarm_id) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        Log.e("딜아이디",Integer.toString(alarm_id));
        db.execSQL("DELETE FROM WEEKALARM WHERE alarm_id='" +alarm_id+"';");
        //db.delete("WEEKALARM","alarm_id=", new String[]{Integer.toString(alarm_id)});
        db.close();
    }

    public void alldelete() {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("delete from WEEKALARM");
        db.close();
    }

    public  void droptable(){
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DROP TABLE WEEKALARM");
        db.close();
    }

    public boolean isExist(int o, String wn, String wt) {
        SQLiteDatabase db = getReadableDatabase();
        boolean exist = false;
        Cursor cursor = db.rawQuery("SELECT * FROM WEEKALARM WHERE alarm_op='"+o+"' AND weeknum='"+wn+"' AND weektime='"+wt+"';", null);
        if(cursor.getCount() > 0){
            exist=true;
        }
        cursor.close();
        db.close();
        return  exist;
    }


    public String getResult() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM WEEKALARM", null);
        while (cursor.moveToNext()) {
            result += cursor.getString(1)
                    + "&"
                    + cursor.getString(2)
                    + "&"
                    + cursor.getString(3)
                    + "&"
                    + cursor.getString(4)
                    +";";
        }
        Log.e("리저트", result);
        return result;
    }
}


