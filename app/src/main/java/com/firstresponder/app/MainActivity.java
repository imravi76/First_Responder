package com.firstresponder.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import com.firstresponder.app.Fragments.CallFragment;
import com.firstresponder.app.Fragments.MessageFragment;
import com.firstresponder.app.Fragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MessageFragment messageFragment;
    private CallFragment callFragment;
    private SettingsFragment settingsFragment;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView mainbottomNav = findViewById(R.id.mainBottomNav);

        messageFragment = new MessageFragment();
        callFragment = new CallFragment();
        settingsFragment = new SettingsFragment();

        initializeFragment();

        mainbottomNav.setOnNavigationItemSelectedListener(item -> {

            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.main_container);

            switch (item.getItemId()){
                case R.id.bottom_message:
                    replaceFragment(messageFragment, currentFragment);
                    return true;

                case R.id.bottom_call:
                    replaceFragment(callFragment, currentFragment);
                    return true;

                case R.id.bottom_setting:
                    replaceFragment(settingsFragment, currentFragment);
                    return true;

                default:
                    return false;
            }

        });

        boolean isNotificationServiceRunning = isNotificationServiceRunning();

        if(!isNotificationServiceRunning){

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setMessage("To read notifications, need to enable Notification Access from setting, do you want us to take you there?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    finish();
                    System.exit(0);
                }
            });

            builder.setCancelable(false);

            builder.create().show();

        }

        isNotificationServiceRunning();

        checkOverlayPermission();
        //startService();

    }

    private void initializeFragment() {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.add(R.id.main_container, messageFragment);
        fragmentTransaction.add(R.id.main_container, callFragment);
        fragmentTransaction.add(R.id.main_container, settingsFragment);

        fragmentTransaction.hide(callFragment);
        fragmentTransaction.hide(settingsFragment);

        fragmentTransaction.commit();
    }

    private void replaceFragment(Fragment fragment, Fragment currentFragment){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if(fragment == messageFragment){

            fragmentTransaction.hide(callFragment);
            fragmentTransaction.hide(settingsFragment);

        }

        if(fragment == callFragment){

            fragmentTransaction.hide(messageFragment);
            fragmentTransaction.hide(settingsFragment);

        }

        if (fragment == settingsFragment){
            fragmentTransaction.hide(messageFragment);
            fragmentTransaction.hide(callFragment);
        }

        assert fragment != null;
        fragmentTransaction.show(fragment);

        fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.commit();

    }

    private boolean isNotificationServiceRunning() {
        ContentResolver contentResolver = getContentResolver();
        String enabledNotificationListeners =
                Settings.Secure.getString(contentResolver, "enabled_notification_listeners");
        String packageName = getPackageName();
        return enabledNotificationListeners != null && enabledNotificationListeners.contains(packageName);
    }

    public void startService(){

        if(Settings.canDrawOverlays(this)) {
            startForegroundService(new Intent(this, ForegroundService.class));
        }
    }

    public void checkOverlayPermission(){

        if (!Settings.canDrawOverlays(this)) {
            Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            startActivity(myIntent);
        }
    }

}
