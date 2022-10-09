package com.firstresponder.app.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.ajts.androidmads.library.SQLiteToExcel;
import com.firstresponder.app.DBHandler;
import com.firstresponder.app.R;

import java.io.File;


public class SettingsFragment extends Fragment {

    private Button mExportBtn;

    DBHandler dbHandler;
    String directory_path = Environment.getExternalStorageDirectory().getPath() + "/";
    SQLiteToExcel sqliteToExcel;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        mExportBtn = v.findViewById(R.id.dbBtn);

        File file = new File(directory_path);
        if (!file.exists()) {
            Log.v("File Created", String.valueOf(file.mkdirs()));
        }

        mExportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqliteToExcel = new SQLiteToExcel(getContext(), DBHandler.DB_NAME, directory_path);
                sqliteToExcel.exportAllTables("Responder_DB.xls", new SQLiteToExcel.ExportListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onCompleted(String filePath) {
                        Toast.makeText(getContext(), "Exported", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(getContext(), "Error"+e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.v("Files", String.valueOf(file.mkdirs()));
                        Log.v("Files", e.getMessage());
                    }
                });
            }
        });

        return v;
    }
}
