package com.example.test1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.widget.Toast;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by 또리또리 on 2019-02-15.
 */

public class detailsubway extends Activity {
   private TextView subname;
    private TextView updown;
    private TextView destiny;
    private TextView traintype;
    private TextView predicttime;
    private TextView firstmsg;
    private TextView secondmsg;
    private TextView subwaynum;
    private TextView statnfid;
    private TextView statntid;
    private statnidDBHelper dbHelper;
    private String address2[];
    private String address1="";
    private String queryUrl="http://swopenapi.seoul.go.kr/api/subway/656843434561706636356d6f707650/xml/realtimeStationArrival/1/5/";
    private ArrayList<subwayDetailInfo> subwayDetailInfos;
    private NotificationManager notificationManager;
    private static final int NOTIFICATION_ID = 1;
    private Thread subApiThread;
    private Handler detailHandler= new Handler(new Handler.Callback(){
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what)
            {
                case 101:
                    setInfos();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subway_detail);

        setViews();
        dbHelper = new statnidDBHelper(getApplicationContext(), "STATIONID.db", null, 1);
    }

    private void setViews()
    {
        Intent intent=getIntent();
        address1= intent.getStringExtra("address");
        address2=address1.split(";");

        Log.e("하이",address2[0]);
        subname = (TextView) findViewById(R.id.sub_name);
        updown = (TextView) findViewById(R.id.updown);
        destiny = (TextView) findViewById(R.id.destiny);
        traintype = (TextView) findViewById(R.id.train_type);
        predicttime = (TextView) findViewById(R.id.predict_time);
        firstmsg = (TextView) findViewById(R.id.first_msg);
        secondmsg = (TextView) findViewById(R.id.second_msg);
        subwaynum=(TextView)findViewById(R.id.sub_num);
        statnfid=findViewById(R.id.fid);
        statntid=findViewById(R.id.tid);

        subwayDetailInfos=new ArrayList<subwayDetailInfo>();
        queryUrl=queryUrl+address2[0];
    }

    private void setInfos()
    {
        for(int i=0;i<subwayDetailInfos.size();i++) {
            Log.e("까르륵",subwayDetailInfos.get(i).getTrainNum());
            Log.e("끼룩",address2[2]);
            if (subwayDetailInfos.get(i).getTrainNum().equals(address2[2])) {
                subname.setText(subwayDetailInfos.get(i).getSubname());
                updown.setText(subwayDetailInfos.get(i).getUpdown());
                destiny.setText(subwayDetailInfos.get(i).getDestiny());
                traintype.setText(subwayDetailInfos.get(i).getTraintype());
                subwaynum.setText(address2[1]);
                predicttime.setText(subwayDetailInfos.get(i).getPredicttime());
                firstmsg.setText(subwayDetailInfos.get(i).getFirstmsg());
                secondmsg.setText(subwayDetailInfos.get(i).getSecondmsg());
                statnfid.setText(dbHelper.getResName(subwayDetailInfos.get(i).getStatnFid()));
                statntid.setText(dbHelper.getResName(subwayDetailInfos.get(i).getStatnTid()));
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        getApi();
    }

    @Override
    public void onStop() {
        if(subApiThread != null){
            if(subApiThread.isAlive()){
                subApiThread.interrupt();
            }
            subApiThread = null;
        }

        super.onStop();
    }


    private void getApi(){
        subwayDetailInfos.clear();
        subApiThread = detailThread();
        subApiThread.start();
    }

    private void generateNotification() {

        notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        Context context = detailsubway.this;
        Notification notify = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            notify = new Notification.Builder(context)
                    .setTicker("아주 중요한 메시지")
                    .setContentTitle(address1+"역")
                    .setContentText("첫 번째 열차는 5분 후, 두 번째 열차는 10분 후 도착입니다")
                    .setSmallIcon(android.R.drawable.stat_notify_more)
                    .setWhen(System.currentTimeMillis())
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .build();
        }

        notificationManager.notify(NOTIFICATION_ID, notify);
    }

    /*Switch.OnCheckedChangeListener myalarmClickListener=new Switch.OnCheckedChangeListener(){

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                Toast.makeText(buttonView.getContext(),address1+"알람을 추가하셨습니다",Toast.LENGTH_LONG).show();
                generateNotification();
            }
             else
                 {
                     Toast.makeText(buttonView.getContext(),"알람을 취소하셨습니다.",Toast.LENGTH_LONG).show();
                     notificationManager.cancel(NOTIFICATION_ID);
                 }

        }

    };*/

    Thread detailThread(){
        return new Thread(new Runnable() {
            @Override
            public void run() {
                    Message msg = detailHandler.obtainMessage();
                    InputStream ins=null;
                    try {
                        ins = new URL(queryUrl).openStream();      //二쇱냼 ?좎븣??諛쏆븘 ?ㅽ뵂?ㅽ듃由?

                        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                        XmlPullParser xpars = factory.newPullParser();

                        xpars.setInput(new InputStreamReader(ins, "UTF-8"));    //?명뭼由щ뜑濡??뚯떛???댁슜 ?ｊ린

                        int eventType = xpars.getEventType();
                        subwayDetailInfo subwayDetailInfo = null;
                        String text="";
                        while (eventType != XmlPullParser.END_DOCUMENT) {   //媛?몄삩 ?댁슜???앹씠 ?꾨땺?뚭퉴吏 ?섑뻾
                            String tag = xpars.getName();

                            switch (eventType) {
                                case XmlPullParser.START_TAG:
                                    tag = xpars.getName();//?쒓렇 ?대쫫 ?살뼱?ㅺ린
                                    if (tag.equals("row")) {// ?쒕툕由ъ뒪???쒓렇媛 ???쒕퉬?ㅻ쭏???쒖옉 ?뚮젮二쇰뒗 嫄?
                                        subwayDetailInfo= new subwayDetailInfo();
                                    }
                                    break;

                                case XmlPullParser.TEXT:     //?띿뒪諛쏆쑝硫?媛곴컖???댁슜??留욊쾶 ?⑥닔 ?ㅽ뻾?쒖폒 ?ｌ뼱以?
                                    text = xpars.getText();
                                    break;

                                case XmlPullParser.END_TAG:
                                    if (tag.equals("row")) {       //?쒕툕由ъ뒪?몃씪???쒓렇瑜?留뚮굹硫??앹꽦??諛곗뿴 ?먯냼 媛앹껜 ?섎굹 ?뷀빐二쇨린
                                        subwayDetailInfos.add(subwayDetailInfo);
                                    } else if (tag.equals("subwayId")) {
                                        subwayDetailInfo.setTrainNum(text);
                                    } else if (tag.equals("updnLine")) {
                                        subwayDetailInfo.setUpdown(text);
                                    } else if(tag.equals("trainLineNm")){
                                        subwayDetailInfo.setDestiny(text);
                                    } else if(tag.equals("btrainSttus")){
                                        subwayDetailInfo.setTraintype(text);
                                    } else if(tag.equals("barvlDt")){
                                        subwayDetailInfo.setPredicttime(text);
                                    } else if(tag.equals("arvlMsg3")){
                                        subwayDetailInfo.setSecondmsg(text);
                                    } else if(tag.equals("arvlMsg2")){
                                        subwayDetailInfo.setFirstmsg(text);
                                    } else if(tag.equals("statnNm")){
                                        subwayDetailInfo.setSubname(text);
                                    }else if(tag.equals("statnFid")){
                                        subwayDetailInfo.setStatnFid(text);
                                    }else if(tag.equals("statnTid")){
                                        subwayDetailInfo.setStatnTid(text);
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
                    detailHandler.sendMessage(msg);
                }
            });
        }
    }

