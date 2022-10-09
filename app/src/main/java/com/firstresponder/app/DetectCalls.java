package com.firstresponder.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.telephony.TelephonyManager;

public class DetectCalls extends BroadcastReceiver {

    private Window window;

    @Override
    public void onReceive(Context context, Intent intent) {
        try{
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

            if(Settings.canDrawOverlays(context)) {
                if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                    context.startForegroundService(new Intent(context, ForegroundService.class));
                }
                if ((state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))){
                    //window.close();
                }
                if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                    context.stopService(new Intent(context, ForegroundService.class));
                    //window.close();
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
