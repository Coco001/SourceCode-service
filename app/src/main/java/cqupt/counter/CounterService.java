package cqupt.counter;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * 计数器的服务
 */

public class CounterService extends Service implements IConterService {

    private boolean stop = false;
    private IConterCallback mCallback = null;
    private IBinder mBinder = new CounterBinder();

    public class CounterBinder extends Binder {
        public CounterService getService() {
            return CounterService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void startCounter(final int initVal, IConterCallback callback) {
        mCallback = callback;
        AsyncTask<Integer, Integer, Integer> task = new AsyncTask<Integer, Integer, Integer>() {
            @Override
            protected Integer doInBackground(Integer... params) {
                Integer initCounter = params[0];
                stop = false;
                while (!stop) {
                    publishProgress(initCounter);
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    initCounter++;
                }
                return initCounter;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {//执行publishProgress(initCounter)方法刷新界面
                super.onProgressUpdate(values);
                int val = values[0];
                mCallback.count(val);
            }

            @Override
            protected void onPostExecute(Integer integer) {//任务停止时调用
                super.onPostExecute(integer);
                mCallback.count(initVal);
            }
        };
        task.execute(initVal);
    }

    @Override
    public void stopCounter() {
        stop = true;
    }
}
