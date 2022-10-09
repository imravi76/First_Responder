package com.firstresponder.app.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firstresponder.app.Adapters.RuleAdapter;
import com.firstresponder.app.AddRuleActivity;
import com.firstresponder.app.DBHandler;
import com.firstresponder.app.Models.Rule;
import com.firstresponder.app.R;
import com.firstresponder.app.SwipeToDeleteCallback;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;

public class MessageFragment extends Fragment {

    private ExtendedFloatingActionButton mAddRule;

    private ArrayList<Rule> ruleModalArrayList;
    private DBHandler dbHandler;
    private RuleAdapter ruleAdapter;
    private RecyclerView ruleRV;

    public MessageFragment() {
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
        View v = inflater.inflate(R.layout.fragment_message, container, false);

        mAddRule = v.findViewById(R.id.addRule);

        mAddRule.setOnClickListener(view -> {
            Intent addNewRule = new Intent(getActivity(), AddRuleActivity.class);
            addNewRule.putExtra("ruleMethod", "new");
            startActivity(addNewRule);
        });

        ruleModalArrayList = new ArrayList<>();
        dbHandler = new DBHandler(getActivity());

        ruleModalArrayList = dbHandler.readRules();

        ruleAdapter = new RuleAdapter(ruleModalArrayList, getActivity());
        ruleRV = v.findViewById(R.id.ruleRecycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        ruleRV.setLayoutManager(linearLayoutManager);

        ruleRV.setAdapter(ruleAdapter);
        enableSwipeToDeleteAndUndo();
        return v;
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                //final String item = ruleAdapter.getData().get(position);
                ruleAdapter.removeItem(position);
                dbHandler.deleteRule((Integer) viewHolder.itemView.getTag());
                Toast.makeText(getContext(), "Position: "+viewHolder.itemView.getTag(), Toast.LENGTH_SHORT).show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(ruleRV);
    }
}
