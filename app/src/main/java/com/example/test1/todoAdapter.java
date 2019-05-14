package com.example.test1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

/**
 * Created by 또리또리 on 2019-03-31.
 */

public class todoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<toDo> toDOArrayList;
    private toDo item;
    private int positions;
    private memoDBHelper helper;
    private Context context;

    todoAdapter(ArrayList<toDo> toDoArrayList){
        this.toDOArrayList = toDoArrayList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dotitle;
        TextView docontents;
        Button deletebtn;

        MyViewHolder(View view) {
            super(view);
            context = view.getContext();
            dotitle = view.findViewById(R.id.titles);
            docontents = view.findViewById(R.id.contents);
            deletebtn = view.findViewById(R.id.delete_btn);

            deletebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    positions=getAdapterPosition();
                    helper.delete(toDOArrayList.get(positions).getDay(),toDOArrayList.get(positions).getTitle(),toDOArrayList.get(positions).getContents());
                    Toast.makeText(context,"할일을 삭제합니다.",Toast.LENGTH_LONG).show();
                    toDOArrayList.remove(positions);
                    notifyDataSetChanged();
                }
            });
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_todo, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        helper = new memoDBHelper(context, "Memo.db", null, 1);
        todoAdapter.MyViewHolder myViewHolder = (todoAdapter.MyViewHolder) holder;

        item = toDOArrayList.get(position);

        myViewHolder.dotitle.setText(item.getTitle());
        myViewHolder.docontents.setText(item.getContents());
    }

    @Override
    public int getItemCount() {
        return toDOArrayList.size();
    }
}
