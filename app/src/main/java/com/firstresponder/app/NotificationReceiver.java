package com.firstresponder.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class NotificationReceiver extends NotificationListenerService {

    static Notification notification;
    static Bundle bundle;
    static ArrayList<RemoteInput> remoteInputs;
    static boolean isRunning = false;

    private DBHandler dbHandler;

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);

        if(Settings.Secure.getString(getContentResolver(),"enabled_notification_listeners").contains(getPackageName())// Check for Notification Permission
                && sbn != null && !sbn.isOngoing() && sbn.getPackageName().equals("org.telegram.messenger.web")){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        notification = sbn.getNotification();//Latest Notification of WhatsApp
                        if(notification != null){
                            bundle = notification.extras;
                            //logBundle(bundle);
                            remoteInputs = getRemoteInputs(notification);
                            if(remoteInputs != null && remoteInputs.size() > 0){
                                Object isGroupConversation = bundle.get("android.isGroupConversation");
                                String conversationTitle = bundle.getString("android.conversationTitle");

                                if(isGroupConversation != null){
                                    boolean isGroup = (((boolean) isGroupConversation) && (conversationTitle != null));//Group Params
                                    Object title = bundle.get("android.title");//Chat Title
                                    Object text = bundle.get("android.text");//Chat Text

                                    if(title != null && text != null) {

                                        String contact = (String) title;
                                        String contactNew = contact.replaceAll("[-+^()0-9 ]*", "");

                                        //Log.e("ContactNew", "replyToLastNotification error: " + contactNew);

                                        /*List<Contact> contacts = dbHandler.search(contactNew);
                                        if (contacts == null) {
                                            //dbHandler.addNewContact(contactName, contactNumber);
                                            sendMsg("Bleep Bloop!");
                                        }*/

                                        //Group Specific Replies

                                    }
                                }
                            }
                        }
                    } catch (Exception e){
                        notification = null;
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isRunning = true;
        dbHandler = new DBHandler(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }

    private ArrayList<RemoteInput> getRemoteInputs(Notification notification){
        ArrayList<RemoteInput> remoteInputs = new ArrayList<>();
        NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender(notification);
        for(NotificationCompat.Action act : wearableExtender.getActions()) {
            if(act != null && act.getRemoteInputs() != null) {
                remoteInputs.addAll(Arrays.asList(act.getRemoteInputs()));
            }
        }
        return remoteInputs;
    }

    private void sendMsg(String msg){
        RemoteInput[] allremoteInputs = new RemoteInput[remoteInputs.size()];
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Iterator it = remoteInputs.iterator();
        int i=0;
        while (it.hasNext()) {
            allremoteInputs[i] = (RemoteInput) it.next();
            bundle.putCharSequence(allremoteInputs[i].getResultKey(), msg);//This work, apart from Hangouts as probably they need additional parameter (notification_tag?)
            i++;
        }
        RemoteInput.addResultsToIntent(allremoteInputs, localIntent, bundle);
        try {
            Objects.requireNonNull(replyAction(notification)).actionIntent.send(this, 0, localIntent);
        } catch (PendingIntent.CanceledException e) {

        }
    }

    private NotificationCompat.Action replyAction(Notification notification) {
        NotificationCompat.Action action;
        for (NotificationCompat.Action action2 : new NotificationCompat.WearableExtender(notification).getActions()) {
            if (isAllowFreeFormInput(action2)) {
                return action2;
            }
        }
        if (!(notification == null || notification.actions == null)) {
            for (int i = 0; i < NotificationCompat.getActionCount(notification); i++) {
                action = NotificationCompat.getAction(notification, i);
                if (isAllowFreeFormInput(action)) {
                    return action;
                }
            }
        }
        return null;
    }

    private boolean isAllowFreeFormInput(NotificationCompat.Action action) {
        if (action.getRemoteInputs() == null) {
            return false;
        }
        for (RemoteInput allowFreeFormInput : action.getRemoteInputs()) {
            if (allowFreeFormInput.getAllowFreeFormInput()) {
                return true;
            }
        }
        return false;
    }

    private String[] parseTitle(String title){
        String[] title_split = String.valueOf(title).split("[^\\s\\w\\d]");
        StringBuilder builder = new StringBuilder();
        for(int i=0;i< title_split.length;i++){
            if(i>0){
                builder.append(",");
            }
            builder.append(title_split[i]);
        }
        return builder.toString().split(",");
    }
}
