package com.cfish.notepad;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.renderscript.Script;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNoteActivity extends AppCompatActivity {


    private TextView mComplete;
    private ImageButton mBackBtn;
    private EditText mContent;

    private String noteTime;
    private String noteContent;
    private NoteDB mNoteDB;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_note);

        initView();
        initEvent();
    }

    private void initView() {
        mComplete = (TextView) findViewById(R.id.complete_btn);
        mBackBtn = (ImageButton) findViewById(R.id.back_btn);
        mContent = (EditText) findViewById(R.id.note_content);
        mNoteDB = NoteDB.getInstance(this);
    }

    private void initEvent() {
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataOrNot();
            }
        });

        mComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mContent.getText().toString().equals("")) {
                    new AddAsyncTask().execute();
                    finish();
                } else {
                    finish();
                }
            }
        });
    }

    private void saveDataOrNot() {
        if (!mContent.getText().toString().trim().equals("")) {
            new AlertDialog.Builder(AddNoteActivity.this)
                    .setTitle("提示")
                    .setMessage("保存编辑的内容？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new AddAsyncTask().execute();
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

    class AddAsyncTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            mNoteDB.saveNote(note);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
            Date date = new Date(System.currentTimeMillis());
            noteTime = format.format(date);
            noteContent = mContent.getText().toString();
            note = new Note();
            note.setTime(noteTime);
            note.setContent(noteContent);
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Toast.makeText(AddNoteActivity.this,"保存成功", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        saveDataOrNot();
    }
}
