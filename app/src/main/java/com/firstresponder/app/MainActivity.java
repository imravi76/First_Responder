package com.firstresponder.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.firstresponder.app.Adapters.RuleAdapter;
import com.firstresponder.app.Models.Rule;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //private SettingsFragment settingsFragment;

    private ExtendedFloatingActionButton mAddRule;

    private ArrayList<Rule> ruleModalArrayList;
    private DBHandler dbHandler;
    private RuleAdapter ruleAdapter;
    private RecyclerView ruleRV;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAddRule = findViewById(R.id.addRule);

        mAddRule.setOnClickListener(view -> {
            Intent addNewRule = new Intent(MainActivity.this, AddRuleActivity.class);
            addNewRule.putExtra("ruleMethod", "new");
            startActivity(addNewRule);
        });

        ruleModalArrayList = new ArrayList<>();
        dbHandler = new DBHandler(MainActivity.this);

        ruleModalArrayList = dbHandler.readRules();

        ruleAdapter = new RuleAdapter(ruleModalArrayList, MainActivity.this);
        ruleRV = findViewById(R.id.ruleRecycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);
        ruleRV.setLayoutManager(linearLayoutManager);

        ruleRV.setAdapter(ruleAdapter);
        enableSwipeToDeleteAndUndo();

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

    }

    private boolean isNotificationServiceRunning() {
        ContentResolver contentResolver = getContentResolver();
        String enabledNotificationListeners =
                Settings.Secure.getString(contentResolver, "enabled_notification_listeners");
        String packageName = getPackageName();
        return enabledNotificationListeners != null && enabledNotificationListeners.contains(packageName);
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(MainActivity.this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                //final String item = ruleAdapter.getData().get(position);
                ruleAdapter.removeItem(position);
                dbHandler.deleteRule((Integer) viewHolder.itemView.getTag());
                Toast.makeText(MainActivity.this, "Position: "+viewHolder.itemView.getTag(), Toast.LENGTH_SHORT).show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(ruleRV);
    }

}
