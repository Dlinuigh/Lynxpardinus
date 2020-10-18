package com.lynxpardinus.lp;

import java.io.Serializable;
import java.util.ArrayList;

public class program implements Serializable {
    private int id;
    private String[] argument_name;
    private ArrayList<String> argument_value;
    private String answer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String[] getArgument_name() {
        return argument_name;
    }

    public void setArgument_name(String[]  argument_name) {
        this.argument_name = argument_name;
    }

    public ArrayList<String> getArgument_value() {
        return argument_value;
    }

    public void setArgument_value(ArrayList<String> argument_value) {
        this.argument_value = argument_value;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
