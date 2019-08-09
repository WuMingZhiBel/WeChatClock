package com.l024.wx;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //广播
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(Intent.ACTION_TIME_TICK)) {
                //查看service状态
                boolean isRun = isServiceRunning(WxService.class);
                //没运行则启动
                if(!isRun){

                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button startBtn = this.findViewById(R.id.start_service);
        startBtn.setOnClickListener(this);

        //守护进程
        IntentFilter filter=new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(receiver,filter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start_service:{
                break;
            }
        }
    }

    //检测该服务是否在运行
    public boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(Integer.MAX_VALUE);
        if(serviceList == null|| serviceList.size() == 0)
            return false;
        for(ActivityManager.RunningServiceInfo info : serviceList) {
            if(info.service.getClassName().equals(serviceClass.getName()))
                return true;
        }
        return false;
    }

    private void  xx(){
        Intent intent = new Intent(getBaseContext(), RequestAlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(getBaseContext(),
                100, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
        // 设置calendar到今天14点
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 10);
        calendar.set(Calendar.MILLISECOND, 0);
        // 设置触发时间，并每隔一天进行提醒
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, sender);
    }
}
