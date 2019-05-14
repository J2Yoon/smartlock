package com.example.test1;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Date;

/**
 * Created by 또리또리 on 2019-04-02.
 */

public class memoDBHelper extends SQLiteOpenHelper {


    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public memoDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Memo (_id INTEGER PRIMARY KEY AUTOINCREMENT, day TEXT, title TEXT, contents TEXT);");
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(String day, String title, String contents) {

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO Memo VALUES(null,'" + day + "','" + title +"','"+contents+"');");
        db.close();
    }

    public void delete(String sday, String stitle, String scontetnt) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM Memo WHERE day='"+sday+"' And title='"+stitle+ "' AND contents='"+scontetnt+"';");
        Log.e("아아아",sday+stitle+scontetnt);
        db.close();
    }

    public void alldelete() {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("delete from Memo");
        db.close();
    }

    public String getResult(String sday) {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT day, title, contents FROM Memo Where day="+"'"+sday+"'", null);

        while (cursor.moveToNext()) {
            result += cursor.getString(0)
                    +"&"+
                    cursor.getString(1)
                    + "&"
                    + cursor.getString(2)
                    +";";
            Log.e("에에에",result);
        }
        return result;
    }
}
