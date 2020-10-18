package com.lynxpardinus.lp;

import java.io.Serializable;
import java.util.ArrayList;

public class choice implements Serializable {
    private int id;
    private ArrayList<String> choices;
    private ArrayList<String> answer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<String> getChoices() {
        return choices;
    }

    public void setChoices(ArrayList<String> choices) {
        this.choices = choices;
    }

    public ArrayList<String> getAnswer() {
        return answer;
    }

    public void setAnswer(ArrayList<String> answer) {
        this.answer = answer;
    }
}
