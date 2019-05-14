package com.example.test1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test1.MyBroadCast.MyReceiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.ALARM_SERVICE;
import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by 또리또리 on 2019-02-24.
 */

public class weekLockAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private weekDBHelper dbHelper;
    private ArrayList<weekLockInfo> weekLockInfoArrayList;
    private int positions;
    private weekLockInfo pitem,pitem1,item;

    weekLockAdapter(ArrayList<weekLockInfo> weekLockInfoArrayList){
        this.weekLockInfoArrayList = weekLockInfoArrayList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_weeklock, parent, false);

        return new weekLockAdapter.MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MyViewHolder myViewHolder = (MyViewHolder) holder;
        item = weekLockInfoArrayList.get(position * 2);
        String weekres = setweeknums(item);

        weekLockInfo start = weekLockInfoArrayList.get(position * 2);  // 0 2 4
        Log.e("스타트", start.getAlarm_time());
        weekLockInfo end = weekLockInfoArrayList.get(position * 2 + 1);  // 1 3 5
        Log.e("엔드", end.getAlarm_time());

        ((MyViewHolder) holder).starttime.setText(start.getAlarm_time());
        ((MyViewHolder) holder).endtime.setText(end.getAlarm_time());
        myViewHolder.weekselect.setText(weekres);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setLock(String time, String w, int action, int alarm_id) {
        String[] iw, wt = {};
        iw = w.split("/");
        wt = time.split(":");
        for (int i = 0; i < iw.length; i++) {
            Calendar cal = Calendar.getInstance();
            //cal.set(Calendar.DAY_OF_WEEK, Integer.parseInt(iw[i]));
            Log.e("앞요일",iw[i]);
            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(wt[0]));
            cal.set(Calendar.MINUTE, Integer.parseInt(wt[1]));
            cal.set(Calendar.SECOND, 0);
            Intent intent = new Intent(context, MyReceiver.class);
            intent.putExtra("action",action);
            intent.putExtra("weekend",iw[i]);
            PendingIntent sender = PendingIntent.getBroadcast(
                    context,
                    alarm_id,
                    intent,
                    0
            );
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            Log.e("남음",Long.toString(cal.getTimeInMillis()));
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),sender);
        }
    }

    public String setweeknums(weekLockInfo item)
    {
        String weekres = "";
        String weeknums="";
        weeknums = item.getWeeknum();

        String weeknamme[] = weeknums.split("/");

        for (int j = 0; j < weeknamme.length; j++) {
            if (weeknamme[j].equals("2")) weekres += "월";
            if (weeknamme[j].equals("3")) weekres += "화";
            if (weeknamme[j].equals("4")) weekres += "수";
            if (weeknamme[j].equals("5")) weekres += "목";
            if (weeknamme[j].equals("6")) weekres += "금";
            if (weeknamme[j].equals("7")) weekres += "토";
            if (weeknamme[j].equals("1")) weekres += "일";
            Log.e("호잇",weekres);
        }
        return weekres;
    }

    @Override
    public int getItemCount() {
        return weekLockInfoArrayList.size()/2;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView starttime;
        TextView endtime;
        TextView weekselect;
        Switch oweek;
        CheckBox checkdel;
        Button delbtn;

        MyViewHolder(View view) {
            super(view);

            context = view.getContext();
            dbHelper = new weekDBHelper(context, "WEEKALARM.db", null, 1);
            starttime = view.findViewById(R.id.start_t);
            endtime = view.findViewById(R.id.end_t);
            weekselect = view.findViewById(R.id.week_select);
            delbtn = view.findViewById(R.id.del_btn);
            oweek = view.findViewById(R.id.weekl_swt);

            delbtn.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View view) {
                    positions = getAdapterPosition() * 2;
                    Log.e("순서", Integer.toString(positions) + weekLockInfoArrayList.size());
                    pitem = weekLockInfoArrayList.get(positions);
                    pitem1 = weekLockInfoArrayList.get(positions + 1);
                    cancelAlarm(pitem);
                    cancelAlarm(pitem1);
                    dbHelper.delete(pitem.getAlarm_id());
                    weekLockInfoArrayList.remove(pitem);
                    dbHelper.delete(pitem1.getAlarm_id());
                    weekLockInfoArrayList.remove(pitem1);
                    Log.e("윅아이디", Integer.toString(item.getAlarm_id()));
                    Toast.makeText(context, "정기잠금을 삭제합니다.", Toast.LENGTH_LONG).show();
                    notifyDataSetChanged();
                }
            });

            oweek.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    positions = getAdapterPosition() * 2;
                    pitem = weekLockInfoArrayList.get(positions);
                    pitem1 = weekLockInfoArrayList.get(positions + 1);
                    if (isChecked) {
                        Toast.makeText(context, "정기잠금을 예약하셨습니다", Toast.LENGTH_LONG).show();
                        Log.e("왜지", pitem.getWeeknum());
                        setLock(pitem.getAlarm_time(), pitem.getWeeknum(), 0, pitem.getAlarm_id());
                        setLock(pitem1.getAlarm_time(), pitem1.getWeeknum(), 1, pitem1.getAlarm_id());
                    } else {
                        Toast.makeText(context, "정기잠금 예약을 취소하셨습니다", Toast.LENGTH_LONG).show();
                        cancelAlarm(pitem);
                        cancelAlarm(pitem1);
                    }
                }
            });
        }
        private void cancelAlarm(weekLockInfo item){
            Intent intent = new Intent(context, MyReceiver.class);
            PendingIntent sender = PendingIntent.getBroadcast(
                    context,
                    item.getAlarm_id(),   //Unique!
                    intent,
                    0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            alarmManager.cancel(sender);
        }
    }
}


