package com.cfish.notepad;

import android.os.AsyncTask;

/**
 * Created by GKX100217 on 2016/5/20.
 */
public class DeleteAsyncTask extends AsyncTask {

    private NoteDB  noteDB;

    public DeleteAsyncTask(NoteDB noteDB) {
        this.noteDB = noteDB;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        noteDB.deleteById((Integer)params[0]);
        return null;
    }
}

