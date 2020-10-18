package com.lynxpardinus.lp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lynxpardinus.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.google.android.material.internal.ContextUtils.getActivity;

public class LpAdapter extends RecyclerView.Adapter<LpAdapter.ViewHolder> {
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private static ArrayList<lpclass> lpclassArrayList;
    private static boolean isLearn;
    public LpAdapter(Context context, ArrayList<lpclass> arrayList, boolean islearn){
        LpAdapter.context = context;
        lpclassArrayList=arrayList;
        isLearn = islearn;
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView textView;
        public ViewHolder(View view){
            super(view);
            cardView = (CardView)view;
            textView=view.findViewById(R.id.item);
            SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
            int precent=preferences.getInt("r-value",50);
            float radius= (float) (precent*0.8);
            cardView.setRadius(radius);

            float fontSize=(float)(preferences.getInt("fontSize",12));
            textView.setTextSize(fontSize);
            textView.setSingleLine();
            cardView.setOnClickListener(v -> {
                lpclass lpclass = lpclassArrayList.get(getLayoutPosition());
                if(isLearn){
                    Intent intent = new Intent(context, StatementActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("title", lpclass.getTitle());
                    bundle.putString("content", lpclass.getContent());
                    intent.putExtra("bundle", bundle);
                    context.startActivity(intent);
                }else{
                    int key = lpclass.getId();
                    String Cfilename = "choice_practice.json";
                    String Pfilename = "program.json";
                    String json = loadConfig(Cfilename);
                    Gson gson = new Gson();
                    ArrayList<choice> CarrayList = gson.fromJson(json, new TypeToken<List<choice>>() {
                    }.getType());
                    json = loadConfig(Pfilename);
                    ArrayList<program> ParrayList = gson.fromJson(json, new TypeToken<List<program>>(){}.getType());
                    if(isExist(CarrayList,key)){
                        choice temp = null;
                        for(choice i : CarrayList){
                            if(i.getId() == key){
                                temp = i;
                                break;
                            }
                        }
                        Intent intent = new Intent(context, ChoiceActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList("choices", temp.getChoices());
                        bundle.putStringArrayList("answer", temp.getAnswer());
                        bundle.putString("title", lpclass.getTitle());
                        bundle.putString("content", lpclass.getContent());
                        intent.putExtra("bundle", bundle);
                        context.startActivity(intent);
                    }else{
                        program temp = null;
                        for(program i :ParrayList){
                            if(i.getId() == key){
                                temp = i;
                                break;
                            }
                        }
                        Intent intent=new Intent(context, ProgramActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putStringArrayList("values",temp.getArgument_value());
                        bundle.putStringArray("names",temp.getArgument_name());
                        bundle.putString("title",lpclass.getTitle());
                        bundle.putString("content",lpclass.getContent());
                        bundle.putString("answer",temp.getAnswer());
                        bundle.putInt("id",temp.getId());
                        intent.putExtra("bundle",bundle);
                        context.startActivity(intent);
                    }
                }
            });

        }
        static boolean isExist(ArrayList<choice> lpclasses, int key){
            for(choice i : lpclasses){
                if(i.getId() == key)
                    return true;
            }
            return false;
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.lpitemview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        lpclass temp = lpclassArrayList.get(position);
        holder.textView.setText(temp.getTitle());
    }

    @Override
    public int getItemCount() {
        return lpclassArrayList.size();
    }


    @SuppressLint("RestrictedApi")
    private static String loadConfig(String filename) {
        InputStream is = null;
        ByteArrayOutputStream bos = null;
        try {
            is = getActivity(context).getAssets().open(filename);
            bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int len;
            while ((len = is.read(b)) != -1) {
                bos.write(b, 0, len);
            }
            return bos.toString("utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null)
                    bos.close();
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
