package com.example.fjfokwiq.customprocess;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private static final int MSG_UPDATA = 0X110;
       private ProcessbarwithH proce;
    private Handler mhandler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            int process=proce.getProgress();
            proce.setProgress(++process);
            if (process >= 100) {
                mhandler.removeMessages(MSG_UPDATA);
            }
            mhandler.sendEmptyMessageDelayed(MSG_UPDATA,100);
            return false;
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        proce = (ProcessbarwithH) findViewById(R.id.pb_test);
        mhandler.sendEmptyMessage(MSG_UPDATA);
                
    }
}
