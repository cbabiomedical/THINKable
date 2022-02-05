package com.example.thinkableproject.pianotiles;

import android.os.AsyncTask;
import android.os.SystemClock;

public class Task extends AsyncTask<Integer,Void,String> {

    public interface OnPostExecuteDone{
        void onDone();
    }
    public interface OnPreExecuteCalledListener{
        void onpreExecutecalled();
    }
    private OnPostExecuteDone o;
    private OnPreExecuteCalledListener opcl;
    void Task()
    {

    }
    void setOnPreExecuteCalledListener(OnPreExecuteCalledListener onPreExecuteCalledListener)
    {
        opcl=onPreExecuteCalledListener;
    }
    void setOnPostExecuteDone(OnPostExecuteDone op)
    {
        o=op;
    }

    @Override
    protected void onPreExecute() {
        opcl.onpreExecutecalled();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Integer... params) {
        long startTime= SystemClock.uptimeMillis();
        while(SystemClock.uptimeMillis()-startTime<5000)
        {

        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        o.onDone();
    }
}
