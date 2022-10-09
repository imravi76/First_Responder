package com.firstresponder.app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firstresponder.app.AddRuleActivity;
import com.firstresponder.app.DBHandler;
import com.firstresponder.app.Models.Rule;
import com.firstresponder.app.R;

import java.util.ArrayList;

public class RuleAdapter extends RecyclerView.Adapter<RuleAdapter.ViewHolder>{

    private ArrayList<Rule> ruleModalArrayList;
    private Context context;

    private DBHandler dbHandler;

    public RuleAdapter(ArrayList<Rule> ruleModalArrayList, Context context) {
        this.ruleModalArrayList = ruleModalArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RuleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_rule_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RuleAdapter.ViewHolder holder, int position) {

        Rule modal = ruleModalArrayList.get(position);
        dbHandler = new DBHandler(context.getApplicationContext());

        holder.mRule.setText("Rule #" + Integer.toString(modal.getId()));
        //holder.mStatus.setText("Rule Status: "+modal.getStatus());
        holder.mReply.setText("Reply Message: "+modal.getReply());
        holder.mSwitch.setText(modal.getStatus());

        if (modal.getStatus().equals("1")){
            holder.mSwitch.setChecked(true);
            holder.mStatus.setText("Rule Status: On");
        } else{
            holder.mSwitch.setChecked(false);
            holder.mStatus.setText("Rule Status: Off");
        }

        holder.itemView.setTag(modal.getId());
        holder.mSwitch.setOnCheckedChangeListener((compoundButton, b) -> {

            if (b){
                dbHandler.updateRule(modal.getId(), "1");
                holder.mStatus.setText("Rule Status: On");
                holder.mSwitch.setText("1");
            }else {
                dbHandler.updateRule(modal.getId(), "0");
                holder.mStatus.setText("Rule Status: Off");
                holder.mSwitch.setText("0");
            }

        });

        holder.itemView.setOnClickListener(view -> {
            Intent updateRule = new Intent(context, AddRuleActivity.class);
            updateRule.putExtra("ruleMethod", "update");
            updateRule.putExtra("ruleId", String.valueOf(modal.getId()));
            updateRule.putExtra("ruleStatus", modal.getStatus());
            updateRule.putExtra("ruleMsg", modal.getMsg());
            updateRule.putExtra("ruleReply", modal.getReply());
            updateRule.putExtra("ruleCount", modal.getMsgCount());
            updateRule.putExtra("ruleContact", modal.getSpecificContact());
            updateRule.putExtra("ruleIgnored", modal.getIgnoredContact());
            context.startActivity(updateRule);
        });

    }

    @Override
    public int getItemCount() {
        return ruleModalArrayList.size();
    }

    public void removeItem(int position) {
        ruleModalArrayList.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mRule, mStatus, mReply;
        private Switch mSwitch;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // initializing our text views
            mRule = itemView.findViewById(R.id.ruleText);
            mStatus = itemView.findViewById(R.id.statusText);
            mReply = itemView.findViewById(R.id.replyText);
            mSwitch = itemView.findViewById(R.id.statusSwitch);
        }

    }
}
