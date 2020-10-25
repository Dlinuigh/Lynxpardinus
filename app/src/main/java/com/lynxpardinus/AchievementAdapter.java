package com.lynxpardinus;

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

import java.util.ArrayList;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.ViewHolder> {
    private final ArrayList<String> arrayList;
    private final ArrayList<String> describes;
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    public AchievementAdapter(Context mcontext, ArrayList<String> stringArrayList, ArrayList<String> strings){
        arrayList = stringArrayList;
        describes = strings;
        context = mcontext;
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView textView;
        TextView expand;
        TextView describe;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            textView = itemView.findViewById(R.id.achievement);
            expand = itemView.findViewById(R.id.expand);
            describe = itemView.findViewById(R.id.describe);
            SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
            int precent=preferences.getInt("r-value",50);
            float radius= (float) (precent*0.8);
            cardView.setRadius(radius);
            float fontSize=(float)(preferences.getInt("fontSize",12));
            textView.setTextSize(fontSize);
            textView.setSingleLine();
            describe.setTextSize(fontSize);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.achievement, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //holder.textView.setText(arrayList.get(position));
        holder.textView.setText(arrayList.get(position));
        holder.expand.setOnClickListener(v -> {
            if(holder.describe.getVisibility() == View.GONE){
                holder.describe.setVisibility(View.VISIBLE);
                holder.textView.setVisibility(View.GONE);
                holder.describe.setText(describes.get(position));
            }else{
                holder.textView.setVisibility(View.VISIBLE);
                holder.describe.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
