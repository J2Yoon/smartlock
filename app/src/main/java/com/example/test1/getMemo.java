package com.example.test1;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by 또리또리 on 2019-03-29.
 */

public class getMemo extends Activity implements RecognitionListener {
    private final int SIG_Date=100;
    private final int PERMISSION = 1;
    private TextView title;
    private EditText gettitle;
    private TextView contents;
    private EditText getcontents;
    private TextView selectday;
    private Button mstorebtn;
    private Button speechbtn;
    private String day;
    private Button getdate;
    private Intent intent;
    private memoDBHelper helper;
    private SpeechRecognizer speech;
    Date date;
    Fragment dfragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getmemo);

        title=findViewById(R.id.todaytitle);
        gettitle=findViewById(R.id.gettitle);
        contents=findViewById(R.id.todaycontent);
        getcontents=findViewById(R.id.getcontent);
        mstorebtn=findViewById(R.id.storebtn);
        getdate=findViewById(R.id.getdate);
        selectday=findViewById(R.id.selectday);
        speechbtn=findViewById(R.id.speechbtn);

        helper = new memoDBHelper(getApplicationContext(), "Memo.db", null, 1);
        setTts();


        mstorebtn.setOnClickListener(memostorelistener);
        getdate.setOnClickListener(datelistener);

        speechbtn.setOnClickListener(v ->{
            intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getPackageName());
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR");
            speech.startListening(intent);
        });
    }

    Button.OnClickListener memostorelistener= new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
/*            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                date = transFormat.parse(day);
            } catch (ParseException e) {
                e.printStackTrace();
            }*/
            /*파베 저장*/
            day=selectday.getText().toString();
            helper.insert(day,gettitle.getText().toString(),getcontents.getText().toString());
            Toast.makeText(getApplicationContext(),gettitle.getText()+" "+getcontents.getText()+" "+day, Toast.LENGTH_SHORT).show();
            finishActivity(112);
            setResult(RESULT_OK,getIntent());
            finish();
        }
    };

    Button.OnClickListener datelistener= new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getMemo.this, dateActivity.class);
            startActivityForResult(intent,SIG_Date);
        }
    };

    private void setTts(){
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case SIG_Date:
                if(resultCode==RESULT_OK)
                {
                    day= data.getStringExtra("day");
                    selectday.setText(day);
                }
                break;
        }
    }

    @Override
    public void onReadyForSpeech(Bundle bundle) {
        Toast.makeText(getApplicationContext(),"음성인식을 시작합니다.",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float v) {

    }

    @Override
    public void onBufferReceived(byte[] bytes) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int error) {
        String message;

        switch (error) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "오디오 에러";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "클라이언트 에러";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "퍼미션 없음";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "네트워크 에러";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "네트웍 타임아웃";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "찾을 수 없음";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RECOGNIZER가 바쁨";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "서버가 이상함";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "말하는 시간초과";
                break;
            default:
                message = "알 수 없는 오류임";
                break;
        }

        Toast.makeText(getApplicationContext(), "에러가 발생하였습니다. : " + message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResults(Bundle results) {
        // 말을 하면 ArrayList에 단어를 넣고 textView에 단어를 이어줍니다.
        ArrayList<String> matches =
                results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (matches.get(0).contains("제목"))
            for (int i = 0; i < matches.size(); i++) {
                gettitle.setText(matches.get(i).replace("제목",""));
            }
        else if (matches.get(0).contains("날짜"))
            for (int i = 0; i < matches.size(); i++) {
                String speechday=matches.get(i).replace("년", "-").replace("월","-").replace("일","").replace(" ","");
                speechday=speechday.replace("날짜","");
                selectday.setText(speechday);
            }
        else if (matches.get(0).contains("내용")) {
            for (int i = 0; i < matches.size(); i++) {
                getcontents.setText(matches.get(i).replace("내용",""));
            }
        }
    }

    @Override
    public void onPartialResults(Bundle bundle) {

    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }
}