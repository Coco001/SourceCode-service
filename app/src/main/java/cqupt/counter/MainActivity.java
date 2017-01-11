package cqupt.counter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,IConterCallback{

    private TextView mTv;
    private Button mStop;
    private Button mStart;
    private IConterService mService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }

    private void init(){
        mTv = (TextView) findViewById(R.id.count_tv);
        mStart = (Button) findViewById(R.id.start);
        mStop = (Button) findViewById(R.id.stop);
        mStart.setOnClickListener(this);
        mStop.setOnClickListener(this);
        mStart.setEnabled(true);
        mStop.setEnabled(false);
        //使用服务的类名创建一个intent
        Intent intent = new Intent(MainActivity.this, CounterService.class);
        //绑定服务，当服务绑定成功，mServiceConnection的成员函数onServiceConnected()就会被调用，
        //通过该函数，当前组件就可以访问服务组件的接口了。
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                mService.startCounter(0, this);
                mStart.setEnabled(false);
                mStop.setEnabled(true);
                break;
            case R.id.stop:
                mService.stopCounter();
                mStart.setEnabled(true);
                mStop.setEnabled(false);
                break;
        }
    }

    @Override
    public void count(int val) {
        mTv.setText(String.valueOf(val));
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = ((CounterService.CounterBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    };
}
