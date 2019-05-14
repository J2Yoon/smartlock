package com.example.test1;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by 또리또리 on 2019-03-30.
 */

public class todoFragment extends Fragment {
    private RecyclerView recyclerView1;
    private ArrayList<toDo> todoArrayList;
    private memoDBHelper helper;
    private Date dday;
    private java.sql.Date date;
    private String sday;
    private String[] todos;

    public todoFragment newInstance() {
        todoFragment fragment = new todoFragment();
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.todofragment, container, false);
        helper = new memoDBHelper(getContext(), "Memo.db", null, 1);
        sday=getArguments().getString("day");
        Log.e("여긴가",sday);
      /*  SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        try {
            dday=format.parse(sday);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        setrecycleviews(view);
        settodolist();

        recyclerView1.setAdapter(new todoAdapter(todoArrayList));

        return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setrecycleviews(View view){
        recyclerView1 = view.findViewById(R.id.recycler_todo);
        todoArrayList = new ArrayList<toDo>();
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView1.getAdapter().notifyDataSetChanged();
    }


    public void settodolist(){
        todos=helper.getResult(sday).split(";");
        if(!todos[0].equals("")) {
            for (int i = 0; i < todos.length; i++) {
                String tandc[] = todos[i].split("&");
                todoArrayList.add(new toDo(tandc[0], tandc[1], tandc[2]));
            }
        }
        else return;
    }

}

