package com.cfish.notepad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by GKX100217 on 2016/5/20.
 */
public class MyAdapter extends BaseAdapter {

    private List<Note> noteList;
    private LayoutInflater mInflater;
    private Context mContext;
    private int index;

    public MyAdapter(Context context,List noteList) {
        this.mInflater = LayoutInflater.from(context);
        this.noteList = noteList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return noteList.size();
    }

    @Override
    public Object getItem(int position) {
        return noteList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_item,null);
            viewHolder.mTime = (TextView) convertView.findViewById(R.id.show_time);
            viewHolder.mContent = (TextView) convertView.findViewById(R.id.show_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mTime.setText(noteList.get(position).getTime());
        viewHolder.mContent.setText(noteList.get(position).getContent());

        return convertView;
    }

    class ViewHolder {
        public TextView mTime;
        public TextView mContent;
    }
}
