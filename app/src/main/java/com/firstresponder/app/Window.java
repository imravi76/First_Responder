package com.firstresponder.app;

import static android.content.Context.WINDOW_SERVICE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

public class Window {

    private Context context;
    private View mView;
    private WindowManager.LayoutParams mParams;
    private WindowManager mWindowManager;
    private LayoutInflater layoutInflater;

    @SuppressLint("InflateParams")
    public Window(Context context){
        this.context=context;

        mParams = new WindowManager.LayoutParams(
              WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
              WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
              WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
              PixelFormat.TRANSLUCENT);

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mView = layoutInflater.inflate(R.layout.popup_window, null);

        mView.findViewById(R.id.actionExtendFab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //close();
                mView.findViewById(R.id.actionFab1).setVisibility(View.VISIBLE);
                mView.findViewById(R.id.actionFab2).setVisibility(View.VISIBLE);
                mView.findViewById(R.id.actionFab3).setVisibility(View.VISIBLE);
                //context.stopService(new Intent(context, ForegroundService.class));
            }
        });

        mParams.gravity = Gravity.END;
        mWindowManager = (WindowManager)context.getSystemService(WINDOW_SERVICE);

    }

    public void open() {

        try {

            if(mView.getWindowToken()==null) {
                if(mView.getParent()==null) {
                    mWindowManager.addView(mView, mParams);
                }
            }
        } catch (Exception e) {
            Log.d("Error1",e.toString());
        }

    }

    public void close (){

        try {

            /*((WindowManager)context.getSystemService(WINDOW_SERVICE)).removeView(mView);
            Toast.makeText(context,"Idle State", Toast.LENGTH_SHORT).show();*/

            this.mView.invalidate();

            ((ViewGroup) this.mView.getParent()).removeAllViews();

        } catch (Exception e) {
            Log.d("Error2",e.toString());
            Toast.makeText(context,e.toString(), Toast.LENGTH_LONG).show();
            //close();
        }
    }
}
