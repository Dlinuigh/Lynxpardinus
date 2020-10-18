package com.lynxpardinus.lp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lynxpardinus.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ProgramActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program);
        context = this;
        Bundle bundle = getIntent().getBundleExtra("bundle");
        if(bundle!=null){
            String title=bundle.getString("title");
            String content=bundle.getString("content");
            String[] array=bundle.getStringArray("names");
            TextView title_view= findViewById(R.id.program_title);
            title_view.setText(title);
            TextView content_view=findViewById(R.id.program_content);
            content_view.setText(content);
            TextView arguments=findViewById(R.id.arguments);
            String argument_names="";
            EditText editText = findViewById(R.id.program_edit_field);
            FloatingActionButton floatingActionButton=findViewById(R.id.program_run);
            /*assert array != null;
            for (String i:array) {
                argument_names = argument_names+" "+i;
            }
            arguments.setText(argument_names);
            int id = bundle.getInt("id");
            Log.e("ProgramFragment","id: "+ id);
            String filename = "id" + id + "program.py";
            String inputText = null;
            try {
                inputText =load(filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(inputText.length()!=0) {
                Log.e("ProgramFragment","inputText is not empty");
                editText.setText(inputText);
                editText.setSelection(inputText.length());
            }
            Log.e("ProgramFragment","editText: "+ editText.getText().toString());
*/
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("ProgramFragment","floating button is clicked");
                    Toast.makeText(context, "先占个坑，日后填。功能嘛，就你想的那样。",Toast.LENGTH_SHORT).show();
                    //save(inputText,filename);
                }
            });
        }
    }
    public void save(String inputText, String fileName){
        FileOutputStream out;
        BufferedWriter writer = null;
        try{
            out=openFileOutput(fileName,Context.MODE_PRIVATE);
            writer=new BufferedWriter(new OutputStreamWriter(out));
            writer.write(inputText);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                if(writer!=null){
                    writer.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    public String load(String fileName) throws IOException {
        FileInputStream in;
        BufferedReader reader=null;
        StringBuilder content=new StringBuilder();
        try{
            in=openFileInput(fileName);
            reader=new BufferedReader(new InputStreamReader(in));
            String line="";
            while((line=reader.readLine())!=null){
                content.append(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(reader!=null){
                try{
                    reader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return  content.toString();
    }
}