package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class commute extends Activity implements View.OnClickListener {
    Button back;
    private TextView comment;
    private TextView hide_down;
    private EditText comment_content;
    private Button comment_send;

    private LinearLayout rl_enroll;
    private RelativeLayout rl_comment;

    private ListView comment_list;
    private commuteadaptercomment adapterComment;
    private List<commutecomment> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commute);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {

        // 初始化评论列表
        comment_list = (ListView) findViewById(R.id.comment_list);
        // 初始化数据
        data = new ArrayList<>();
        // 初始化适配器
        adapterComment = new commuteadaptercomment(getApplicationContext(), data);
        // 为评论列表设置适配器
        comment_list.setAdapter(adapterComment);

        comment = (TextView) findViewById(R.id.comment);
        hide_down = (TextView) findViewById(R.id.hide_down);
        comment_content = (EditText) findViewById(R.id.comment_content);
        comment_send = (Button) findViewById(R.id.comment_send);
        rl_enroll = (LinearLayout) findViewById(R.id.rl_enroll);
        rl_comment = (RelativeLayout) findViewById(R.id.rl_comment);

        setListener();
    }

    /**
     * 设置监听
     */
    public void setListener(){
        comment.setOnClickListener(this);

        hide_down.setOnClickListener(this);
        comment_send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.comment){
            // 弹出输入法
            InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            // 显示评论框
            rl_enroll.setVisibility(View.GONE);
            rl_comment.setVisibility(View.VISIBLE);
        }
        if(v.getId()==R.id.hide_down) {
            // 隐藏评论框
            rl_enroll.setVisibility(View.VISIBLE);
            rl_comment.setVisibility(View.GONE);
            // 隐藏输入法，然后暂存当前输入框的内容，方便下次使用
            InputMethodManager im = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(comment_content.getWindowToken(), 0);
        }
        if(v.getId()==R.id.comment_send){
            sendComment();
        }
    }

    /**
     * 发送评论
     */
    public void sendComment(){
        if(comment_content.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "留言不能为空！", Toast.LENGTH_SHORT).show();
        }else{
            // 生成评论数据
            commutecomment comment = new commutecomment();
            comment.setName("留言者"+(data.size()+1)+"：");
            comment.setContent(comment_content.getText().toString());
            adapterComment.addComment(comment);
            // 发送完，清空输入框
            comment_content.setText("");

            Toast.makeText(getApplicationContext(), "留言成功！", Toast.LENGTH_SHORT).show();
        }
    }
}