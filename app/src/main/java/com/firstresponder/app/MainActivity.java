package com.firstresponder.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.view.MenuItem;

import com.firstresponder.app.Fragments.CallFragment;
import com.firstresponder.app.Fragments.MessageFragment;
import com.firstresponder.app.Fragments.SettingsFragment;
import com.firstresponder.app.Models.Contact;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MessageFragment messageFragment;
    private CallFragment callFragment;
    private SettingsFragment settingsFragment;

    //public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    ArrayList<String> arrayList;
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView mainbottomNav = findViewById(R.id.mainBottomNav);
        //mainbottomNav.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.transparent));

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

        arrayList = new ArrayList<>();
        dbHandler = new DBHandler(this);

        boolean isNotificationServiceRunning = isNotificationServiceRunning();

        if(!isNotificationServiceRunning){
            startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
        }

        isNotificationServiceRunning();
        //checkContactPermissions();

        final PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        //Toast.makeText(this, "Package List"+packages, Toast.LENGTH_LONG).show();

        /*for (ApplicationInfo packageInfo : packages) {
            Log.d(TAG, "Installed package :" + packageInfo.packageName);
            dbHandler.addNewPackage(packageInfo.packageName);
            }*/

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

    /*private void checkContactPermissions()
    {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS},
                        PERMISSIONS_REQUEST_READ_CONTACTS);

        } else {
            //Toast.makeText(this, "Permission Granted...", Toast.LENGTH_LONG).show();
            readContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        readContacts();
    }

    @SuppressLint("Range")
    private void readContacts() {

        ContentResolver contentResolver=getContentResolver();
        Cursor cursor=contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        if (cursor.moveToFirst()){

            String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            String contactNew = contactName.replaceAll("[-+^()0-9 ]*", "");
            List<Contact> contacts = dbHandler.search(contactNew);
            if (contacts == null) {
                dbHandler.addNewContact(contactNew, contactNumber);
            }

            //arrayList.add(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
        }

        while(cursor.moveToNext()){
            String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            String contactNew = contactName.replaceAll("[-+^()0-9 ]*", "");
            List<Contact> contacts = dbHandler.search(contactNew);
            if (contacts == null) {
                dbHandler.addNewContact(contactNew, contactNumber);
            }
            //arrayList.add(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));

        }

        //Toast.makeText(this, "Final Contact Lis: "+arrayList, Toast.LENGTH_LONG).show();
    }*/

}
