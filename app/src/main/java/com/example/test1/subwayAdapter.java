package com.example.test1;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

import static android.content.Context.NOTIFICATION_SERVICE;


public class subwayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private NotificationManager notificationManager;
    private static final int NOTIFICATION_ID = 1;
    private Context context;
    private ArrayList<subway_Info> subwayInfoArrayList;
    private int positions;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView trainName;
        TextView trainNum1;
        ToggleButton likebtn;

        MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            trainName = view.findViewById(R.id.train_name);
            trainNum1 = view.findViewById(R.id.train_num);
            likebtn = view.findViewById(R.id.likeButton);
        }

        @Override
        public void onClick(View view) {
            positions=getAdapterPosition();
            String tname=subwayInfoArrayList.get(positions).getTrainName();
            String tnum=subwayInfoArrayList.get(positions).getTrainNum();
            String txtnum=subwayInfoArrayList.get(positions).getSubwaynum();
            Intent intent=new Intent(context, detailsubway.class);
            intent.putExtra("address",tname+";"+tnum+";"+txtnum);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

    }

/*
    public static class WrapContentLinearLayoutManager extends LinearLayoutManager {
        public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }
        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.e("probe", "meet a IOOBE in RecyclerView");
            }
        }
    }*/

    public subwayAdapter(Context context, ArrayList<subway_Info> subwayInfoArrayList) {
        this.context = context;
        this.subwayInfoArrayList = subwayInfoArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_subway, parent, false);
        return new MyViewHolder(v);
    }



    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        final MyViewHolder myViewHolder = (MyViewHolder) holder;
         subway_Info item = subwayInfoArrayList.get(position);

         Log.e("하이루",subwayInfoArrayList.get(position).getTrainName());

             myViewHolder.trainName.setText(subwayInfoArrayList.get(position).getTrainName());
             myViewHolder.trainNum1.setText(subwayInfoArrayList.get(position).getTrainNum());



        myViewHolder.likebtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (myViewHolder.likebtn.isChecked()) {
                    //myViewHolder.likebtn.setBackground();
                    Toast.makeText(context,"알람을 추가하셨습니다",Toast.LENGTH_LONG).show();
                //   generateNotification(item);
                    //블루투스 위치
                }else{
                    Toast.makeText(context,"알람을 제거하셨습니다",Toast.LENGTH_LONG).show();
                    //블루투스 위치
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return subwayInfoArrayList.size();
    }


    private void generateNotification(subway_Info subwayInfo) {

        notificationManager = (NotificationManager)
                context.getSystemService(NOTIFICATION_SERVICE);

        Notification notify = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                notify = new Notification.Builder(context)
                        .setTicker("아주 중요한 메시지")
                        .setContentTitle(subwayInfo.getTrainName()+"역")
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