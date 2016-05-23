package com.cfish.notepad;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView mTitle;
    private TextView mNoteNum;
    private ImageButton mWrite;
    private ListView mNoteListView;
    private ImageButton mAbout;

    private List<Note> mNoteList = new ArrayList();
    private NoteDB mNoteDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initView();
        new NewAsyncTask().execute();
        initEvent();
    }


    class NewAsyncTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            mNoteList = mNoteDB.loadNotes();
            return mNoteList;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            MyAdapter myAdapter = new MyAdapter(MainActivity.this,(List)o);
            mNoteListView.setAdapter(myAdapter);

            int temp = mNoteList.size();
            mNoteNum.setText("共"+temp+"条笔记");
        }
    }

    public void initView() {
        mTitle = (TextView) findViewById(R.id.app_title);
        mNoteNum = (TextView) findViewById(R.id.note_num);
        mWrite = (ImageButton) findViewById(R.id.write_btn);
        mNoteListView = (ListView) findViewById(R.id.listview);
        mAbout = (ImageButton) findViewById(R.id.about_btn);

        mNoteDB = NoteDB.getInstance(this);
    }
    private void initEvent() {
        //添加新的笔记
        mWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivity(intent);
            }
        });
        //查看或修改一条已有笔记
        mNoteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Note note = (Note) parent.getItemAtPosition(position);
                Intent intent  = new Intent(MainActivity.this, ReadActivity.class);
                intent.putExtra("note_id", note.getId());
            }
        });
        //长按删除
        mNoteListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Note note = (Note) parent.getItemAtPosition(position);

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("提示")
                        .setMessage("确定删除码？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new DeleteAsyncTask(mNoteDB).execute(note.getId());
                                new NewAsyncTask().execute();
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
                return true;
            }
        });

        mAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        new NewAsyncTask().execute();
    }
}
