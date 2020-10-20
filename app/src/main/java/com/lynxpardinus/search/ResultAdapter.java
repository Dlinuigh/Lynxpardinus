package com.lynxpardinus.search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lynxpardinus.R;

import java.util.ArrayList;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {
    private final ArrayList<String> arrayList;
    private final ArrayList<String> strings;
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    public ResultAdapter(Context mcontext, ArrayList<String> results, ArrayList<String> list){
        arrayList=results;
        strings = list;
        context=mcontext;
    }
    static class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView textView;
        TextView expand2;
        TextView content;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView)itemView;
            textView=itemView.findViewById(R.id.entry);
            expand2 = itemView.findViewById(R.id.expand2);
            content = itemView.findViewById(R.id.content);
            SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
            int precent=preferences.getInt("r-value",50);
            float radius= (float) (precent*0.8);
            cardView.setRadius(radius);
            float fontSize=(float)(preferences.getInt("fontSize",12));
            textView.setTextSize(fontSize);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.entry, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //holder.textView.setText(arrayList.get(position));
        holder.textView.setText(arrayList.get(position));
        holder.expand2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.content.getVisibility() == View.GONE){
                    holder.content.setVisibility(View.VISIBLE);
                    holder.textView.setVisibility(View.GONE);
                    holder.content.setText(strings.get(position));
                }else{
                    holder.textView.setVisibility(View.VISIBLE);
                    holder.content.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
