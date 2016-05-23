package com.cfish.notepad.util;

import android.os.AsyncTask;

import com.cfish.notepad.database.NoteDB;

/**
 * Created by GKX100217 on 2016/5/20.
 */
public class DeleteAsyncTask extends AsyncTask {

    private NoteDB noteDB;

    public DeleteAsyncTask(NoteDB noteDB) {
        this.noteDB = noteDB;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        noteDB.deleteById((Integer)params[0]);
        return null;
    }
}

