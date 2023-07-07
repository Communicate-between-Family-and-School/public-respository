package com.example.myComponent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.myapplication.R;

@SuppressLint("AppCompatCustomView")
public class MyTextView extends TextView {
    private int infoId;

    public int getInfoId(){
        return infoId;
    }

    public void setInfoId(int infoId) {
        this.infoId = infoId;
    }

    public MyTextView(Context context){
        super(context);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyTextView);
        infoId = typedArray.getInt(R.styleable.MyTextView_infoId, -1);
        typedArray.recycle();
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
    }
}
