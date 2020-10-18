package com.lynxpardinus.search;

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
    private static Context context;
    public ResultAdapter(Context mcontext, ArrayList<String> results){
        arrayList=results;
        context=mcontext;
    }
    static class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView)itemView;
            textView=itemView.findViewById(R.id.entry);
            SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
            int precent=preferences.getInt("r-value",50);
            float radius= (float) (precent*0.8);
            cardView.setRadius(radius);

            float fontSize=(float)(preferences.getInt("fontSize",12));
            textView.setTextSize(fontSize);
            textView.setSingleLine();

        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.entry, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
