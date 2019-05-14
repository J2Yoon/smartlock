package com.example.test1;

import android.*;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by 또리또리 on 2019-03-19.
 */

public class subwayList extends Activity implements RecognitionListener{

    private String queryUrl="http://swopenapi.seoul.go.kr/api/subway/566d67674f61706637307059577459/xml/nearBy/0/20/197529.91541/450688.46452";
    private AssetManager assetManager;
    private ArrayList<subway_Info> subwayInfos;
    private RecyclerView recyclerView1;
    private RecyclerView.LayoutManager mLayoutManager;
    private Thread apiThread;
    private Button btnspeech;
    private TextView txt;
    private SpeechRecognizer speech;
    private String tname;
    private String txtnum;
    private final int RESULT_SPEECH = 1000;
    private Intent intent;
    final int PERMISSION = 1;
    private subNumCheck scheck;
    private NotificationManager notificationManager;
    private static final int NOTIFICATION_ID = 1;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case 101:
                    //정상
                    Log.e("안녕",subwayInfos.get(0).getTrainName());
                    recyclerView1.setAdapter(new subwayAdapter(getApplicationContext(), subwayInfos));
                    break;
                case 102:
                    //오류
                    Toast.makeText(getApplicationContext(), (String)message.obj, Toast.LENGTH_SHORT).show();
                    break;
            }

            return true;
        }
    });
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subwaylist);

        btnspeech=findViewById(R.id.btn_speech);
        recyclerView1 = findViewById(R.id.recycler_subway);
         txt=findViewById(R.id.textView);

        btnspeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getPackageName());
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR");
                speech.startListening(intent);
            }
        });

        setTts();
        init();
        getsubwayApi();
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerView1.setAdapter(new subwayAdapter(getApplicationContext(), subwayInfos));

    }

    @Override
    public void onStop() {
        if(apiThread != null){
            if(apiThread.isAlive()){
                apiThread.interrupt();
            }
            apiThread = null;
        }

        super.onStop();
    }

    private void getsubwayApi(){
        subwayInfos.clear();
        apiThread = initgetAPiThread();
        apiThread.start();
    }

    private void init(){
        subwayInfos = new ArrayList<>();

        recyclerView1.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView1.setLayoutManager(mLayoutManager);
    }

    private void setTts(){
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);
    }

    private Thread initgetAPiThread(){
        return new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = mHandler.obtainMessage();
                InputStream ins=null;
                try {
                    ins = new URL(queryUrl).openStream();      //二쇱냼 ?좎븣??諛쏆븘 ?ㅽ뵂?ㅽ듃由?

                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser xpars = factory.newPullParser();

                    xpars.setInput(new InputStreamReader(ins, "UTF-8"));    //?명뭼由щ뜑濡??뚯떛???댁슜 ?ｊ린

                    int eventType = xpars.getEventType();
                    subway_Info subwayInfo = null;
                    String text="";
                    while (eventType != XmlPullParser.END_DOCUMENT) {   //媛?몄삩 ?댁슜???앹씠 ?꾨땺?뚭퉴吏 ?섑뻾
                        String tag = xpars.getName();

                        switch (eventType) {
                            case XmlPullParser.START_TAG:
                                tag = xpars.getName();//?쒓렇 ?대쫫 ?살뼱?ㅺ린
                                if (tag.equals("row")) {// ?쒕툕由ъ뒪???쒓렇媛 ???쒕퉬?ㅻ쭏???쒖옉 ?뚮젮二쇰뒗 嫄?
                                    subwayInfo= new subway_Info();
                                }
                                break;

                            case XmlPullParser.TEXT:     //?띿뒪諛쏆쑝硫?媛곴컖???댁슜??留욊쾶 ?⑥닔 ?ㅽ뻾?쒖폒 ?ｌ뼱以?
                                text = xpars.getText();
                                break;

                            case XmlPullParser.END_TAG:       //</servList>?대윴 ?앹쑝 ?쒓렇 留뚮굹硫??앹씠誘濡??ㅽ뻾
                                if (tag.equals("row")) {       //?쒕툕由ъ뒪?몃씪???쒓렇瑜?留뚮굹硫??앹꽦??諛곗뿴 ?먯냼 媛앹껜 ?섎굹 ?뷀빐二쇨린
                                    subwayInfos.add(subwayInfo);
                                } else if (tag.equals("statnNm")) {
                                    subwayInfo.setTrainName(text);
                                } else if (tag.equals("subwayNm")) {
                                    subwayInfo.setTrainNum(text);
                                } else if(tag.equals("statnId")){
                                    subwayInfo.setTrainId(text);
                                } else if(tag.equals("subwayId")) {
                                    subwayInfo.setSubwaynum(text);
                                }
                                break;
                            default:
                                break;
                        }
                        eventType = xpars.next();
                    }
                    ins.close();
                    msg.what = 101;
                } catch (XmlPullParserException | IOException e) {
                    e.printStackTrace();
                    msg.what = 102;
                    msg.obj="API 오류";
                }
                mHandler.sendMessage(msg);
            }

        });
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
    public void onError(int i) {
        String message;

        switch (i) {
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
        scheck=new subNumCheck();
        boolean isalarm=false;
        ArrayList<String> matches =
                results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

       txt.setText(matches.get(0));

        for(int j=0;j<subwayInfos.size();j++){
            if(matches.get(0).contains(subwayInfos.get(j).getTrainName()))
            {
                tname=subwayInfos.get(j).getTrainName();
            }
        }



      for(int j=0;j<subwayInfos.size();j++){
            if(matches.get(0).contains(subwayInfos.get(j).getTrainNum()))
            {
                txtnum=subwayInfos.get(j).getTrainNum();
            }
        }

            if(matches.get(0).contains("알람"))
            {
                isalarm=true;
            }
        if(isalarm){
            Toast.makeText(getApplicationContext(),"알람을 추가하셨습니다",Toast.LENGTH_LONG).show();
              generateNotification(tname);
        }
        else {
            String tnum=scheck.revsubcheck(txtnum);
            Intent subintent = new Intent(getApplicationContext(), detailsubway.class);
            subintent.putExtra("address", tname + ";" + txtnum + ";" + tnum);
            Log.e("야호!", tname + tnum + txtnum);
            subintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(subintent);
        }
}

    @Override
    public void onPartialResults(Bundle bundle) {

    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }

    private void generateNotification(String tname) {

        notificationManager = (NotificationManager)
                getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

        Notification notify = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                notify = new Notification.Builder(getApplicationContext())
                        .setTicker("아주 중요한 메시지")
                        .setContentTitle(tname+"역")
                        .setContentText("첫 번째 열차는 5분 후, 두 번째 열차는 10분 후 도착입니다")
                        .setSmallIcon(android.R.drawable.stat_notify_more)
                        .setWhen(System.currentTimeMillis())
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .build();
            }
        }

        notificationManager.notify(NOTIFICATION_ID, notify);
    }
}