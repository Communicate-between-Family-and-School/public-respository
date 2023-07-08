package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class commuteadaptercomment extends BaseAdapter {
    Context context;
    List<commutecomment> data;

    public commuteadaptercomment(Context c, List<commutecomment> data){
        this.context = c;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_commute_item_comment, null);
            holder.comment_name = (TextView) convertView.findViewById(R.id.comment_name);
            holder.comment_content = (TextView) convertView.findViewById(R.id.comment_content);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        // 适配数据
        holder.comment_name.setText(data.get(i).getName());
        holder.comment_content.setText(data.get(i).getContent());

        return convertView;
    }

    /**
     * 添加一条留言,刷新列表
     * @param comment
     */
    public void addComment(commutecomment comment){
        data.add(comment);
        notifyDataSetChanged();
    }

    public static class ViewHolder{
        TextView comment_name;
        TextView comment_content;
    }
}



