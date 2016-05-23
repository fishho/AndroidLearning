package com.cfish.notepad;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReadActivity extends AppCompatActivity {

    private static final String TAG =ReadActivity.class.getSimpleName();
    private TextView mComplete;
    private ImageButton mBackBtn;
    private EditText mContent;
    private LinearLayout mScreen;

    private int noteId;
    private String noteTime;
    private String noteContent;
    private String originData;

    private NoteDB mNoteDB;
    private static Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_note);


        Intent intent = getIntent();
        note = (Note) intent.getSerializableExtra("note_item");
        noteId = intent.getIntExtra("note_id",0);
        Log.d(TAG,"note id = "+ noteId);

        initView();
        new LoadAsyncTask().execute();
        initEvent();

    }

    private void initView() {
        mComplete = (TextView) findViewById(R.id.complete_btn);
        mBackBtn = (ImageButton) findViewById(R.id.back_btn);
        mContent = (EditText) findViewById(R.id.note_content);
        mScreen = (LinearLayout) findViewById(R.id.screen_view);

        mNoteDB = NoteDB.getInstance(this);
    }

    private void initEvent() {
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDataOrNot();
            }
        });

        mComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContent.getText().toString().trim().equals("”")) {
                    Log.d(TAG,"check null");
                    new DeleteAsyncTask(mNoteDB).execute(noteId);
                    finish();
                } else if (mContent.getText().toString().equals(originData)) {
                    finish();
                } else {
                    Log.d(TAG,"check not null");
                    new UpdateAsyncTask().execute();
                    Toast.makeText(ReadActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    class LoadAsyncTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            note = mNoteDB.loadById(noteId);
            return note;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Note note = (Note)o;
            originData = note.getContent();
            mContent.setText(note.getContent());
            mContent.setSelection(mContent.getText().toString().length());
        }
    }

    class UpdateAsyncTask extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
            Date date = new Date(System.currentTimeMillis());
            noteTime = format.format(date);
            noteContent = mContent.getText().toString();
            note.setTime(noteTime);
            note.setContent(noteContent);
        }

        @Override
        protected Object doInBackground(Object[] params) {
            mNoteDB.updateById(noteTime, noteContent, noteId);
            return null;
        }
    }

    private void updateDataOrNot() {
        if (!mContent.getText().toString().equals(originData)) {
            new AlertDialog.Builder(ReadActivity.this)
                    .setTitle("提示")
                    .setMessage("还有未保存的内容，保存吗")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new UpdateAsyncTask().execute();
                            finish();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .show();
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        updateDataOrNot();
    }
}
