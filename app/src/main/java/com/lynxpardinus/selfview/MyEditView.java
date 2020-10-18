package com.lynxpardinus.selfview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyEditView extends androidx.appcompat.widget.AppCompatEditText {
    private String leadText =null;
    public MyEditView(@NonNull Context context) {
        super(context);
    }

    public MyEditView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyEditView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        @SuppressLint("DrawAllocation")
        Paint paint = new Paint();
        paint.setTextSize(56);//自定义字大小
        paint.setColor(Color.GRAY);//自定义字颜色
        canvas.drawText("  "+getLeadText(),2,getHeight()/2+20,paint);
        int paddingLeft = (int) paint.measureText(getLeadText());
        //设置距离光标距离左侧的距离
        setPadding(paddingLeft*2, getPaddingTop(), getPaddingRight(), getPaddingBottom());
        super.onDraw(canvas);
    }
    public void setLeadText(String s){
        leadText = s;
    }
    private String getLeadText() {
        return leadText;
    }
}
