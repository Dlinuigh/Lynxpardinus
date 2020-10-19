package com.lynxpardinus.lp;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lynxpardinus.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LpActivity extends AppCompatActivity {

    private boolean isLearn;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lp);
        isLearn = getIntent().getBooleanExtra("isLearn",true);
        String Lfilename = "learn.json";
        String Pfilename = "practice.json";
        if(isLearn){
           init_env(Lfilename);
        }else{
            init_env(Pfilename);
        }
    }
    private String loadConfig(String filename) {
        InputStream is = null;
        ByteArrayOutputStream bos = null;
        try {
            is = getAssets().open(filename);
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
    private void init_env(String filename){
        String json=loadConfig(filename);
        Gson gson=new Gson();
        ArrayList<lpclass> arrayList=gson.fromJson(json,new TypeToken<List<lpclass>>(){}.getType());
        assert arrayList != null;
        RecyclerView recyclerView = findViewById(R.id.arrayList);
        LpAdapter lpAdapter = new LpAdapter(this, arrayList, isLearn);
        recyclerView.setAdapter(lpAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        //recyclerView.addItemDecoration(new DividerItemDecoration(requireActivity(),DividerItemDecoration.VERTICAL));
    }

}