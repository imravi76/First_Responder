package com.firstresponder.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;

public class AddRuleActivity extends AppCompatActivity {

    private DBHandler dbHandler;

    private RadioGroup mRecMsgGroup;
    private RadioButton mSelectedMsg, mWelcomeMsg, mMatchMsg, mAllMsg;
    private EditText mRecMsg;

    private TextView mOptionalText;

    private ChipGroup mReplyNumberGroup;
    private Chip mSingleChip, mAllChip, mRandomChip;
    private Button mAddReply, mSubtractReply;
    private EditText mReplyMsg;

    private EditText mSpecificContacts;
    private EditText mIgnoredContacts;
    private ExtendedFloatingActionButton mAddRuleFab;

    private CardView mReplyCard, mSpecificCard, mIgnoreCard;

    private LinearLayout mLinearLayout;
    private int i = 0;

    private EditText editText;
    private final ArrayList<EditText> textHolder = new ArrayList<>();

    String recvMsg, msgCount;
    StringBuilder replyMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rule);

        String type = getIntent().getExtras().getString("ruleMethod");
        //Toast.makeText(this, type, Toast.LENGTH_SHORT).show();

        dbHandler = new DBHandler(this);

        mRecMsgGroup = findViewById(R.id.receivedMsgGroup);
        mWelcomeMsg = findViewById(R.id.welcomeRadio);
        mMatchMsg = findViewById(R.id.matchRadio);
        mAllMsg = findViewById(R.id.allRadio);
        int selectedId = mRecMsgGroup.getCheckedRadioButtonId();
        mSelectedMsg = findViewById(selectedId);
        mRecMsg = findViewById(R.id.receivedMsg);

        mOptionalText = findViewById(R.id.optionalText);

        mReplyNumberGroup = findViewById(R.id.replyNumberGroup);
        mSingleChip = findViewById(R.id.singleChip);
        mAllChip = findViewById(R.id.allChip);
        mRandomChip = findViewById(R.id.randomChip);
        mAddReply = findViewById(R.id.addReplyBtn);
        mSubtractReply = findViewById(R.id.subtractReplyBtn);
        mReplyMsg = findViewById(R.id.replyMsg);

        mSpecificContacts = findViewById(R.id.specificContacts);
        mIgnoredContacts = findViewById(R.id.ignoredContacts);
        mAddRuleFab = findViewById(R.id.addRuleFab);

        mReplyCard = findViewById(R.id.replyCard);
        mSpecificCard = findViewById(R.id.specificCard);
        mIgnoreCard = findViewById(R.id.ignoredCard);

        mLinearLayout = findViewById(R.id.linearLayout);

        if (type.equals("update")){
            StringBuilder test = new StringBuilder(getIntent().getExtras().getString("ruleId"));
            test.append(getIntent().getExtras().getString("ruleStatus"));
            test.append(getIntent().getExtras().getString("ruleMsg"));
            test.append(getIntent().getExtras().getString("ruleReply"));
            test.append(getIntent().getExtras().getString("ruleCount"));
            test.append(getIntent().getExtras().getString("ruleContact"));
            test.append(getIntent().getExtras().getString("ruleIgnored"));
            //Toast.makeText(this, test, Toast.LENGTH_LONG).show();

            mReplyCard.setVisibility(View.VISIBLE);
            mSpecificCard.setVisibility(View.VISIBLE);
            mIgnoreCard.setVisibility(View.VISIBLE);
            mAddRuleFab.setVisibility(View.VISIBLE);

            if (getIntent().getExtras().getString("ruleMsg").equals("*welcome*")){
                recvMsg = "*welcome*";
                mWelcomeMsg.setChecked(true);
            } else if (getIntent().getExtras().getString("ruleMsg").equals("*")){
                recvMsg = "*";
                mAllMsg.setChecked(true);
            } else{
                mMatchMsg.setChecked(true);
                recvMsg = getIntent().getExtras().getString("ruleMsg");
                mRecMsg.setVisibility(View.VISIBLE);
                mRecMsg.setText(recvMsg);
            }

            if (getIntent().getExtras().getString("ruleCount").equals("all")){
                mAllChip.setChecked(true);
                mAllChip.setEnabled(true);
                mRandomChip.setEnabled(true);
                mSingleChip.setEnabled(false);
            }else if (getIntent().getExtras().getString("ruleCount").equals("random")){
                mRandomChip.setChecked(true);
                mRandomChip.setEnabled(true);
                mAllChip.setEnabled(true);
                mSingleChip.setEnabled(false);
            }else{
                mSingleChip.setChecked(true);
            }

            long regex = getIntent().getExtras().getString("ruleReply").chars().filter(num -> num == ';').count();
            //Toast.makeText(this, ""+regex, Toast.LENGTH_LONG).show();

            String[] result = getIntent().getExtras().getString("ruleReply").split(";");
            //Toast.makeText(this, ""+ result[0], Toast.LENGTH_LONG).show();
            mReplyMsg.setText(result[0]);
            for (int x=1; x<result.length; x++) {
                //System.out.println(result[x]);
                editText = new EditText(AddRuleActivity.this);
                editText.setId(x);
                editText.setHint("Should be sent");
                editText.setText(result[x]);
                editText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                //editText.setPadding(8, 8, 8, 8);

                textHolder.add(editText);

                mLinearLayout.addView(editText);
                i++;
                mSubtractReply.setEnabled(true);
                mSingleChip.setEnabled(false);
                mAllChip.setChecked(true);
                mAllChip.setEnabled(true);
                mRandomChip.setEnabled(true);
            }

            mSpecificContacts.setText(getIntent().getExtras().getString("ruleContact"));
            mIgnoredContacts.setText(getIntent().getExtras().getString("ruleIgnored"));
        }

        mMatchMsg.setOnClickListener(view -> {
            mRecMsg.setVisibility(View.VISIBLE);
            mReplyCard.setVisibility(View.VISIBLE);
            mOptionalText.setVisibility(View.VISIBLE);
            mSpecificCard.setVisibility(View.VISIBLE);
            mIgnoreCard.setVisibility(View.VISIBLE);
            mAddRuleFab.setVisibility(View.VISIBLE);
        });

        mWelcomeMsg.setOnClickListener(view -> {
            mReplyCard.setVisibility(View.VISIBLE);
            mOptionalText.setVisibility(View.VISIBLE);
            mSpecificCard.setVisibility(View.VISIBLE);
            mIgnoreCard.setVisibility(View.VISIBLE);
            mAddRuleFab.setVisibility(View.VISIBLE);
            mRecMsg.setVisibility(View.GONE);
        });

        mAllMsg.setOnClickListener(view -> {
            mReplyCard.setVisibility(View.VISIBLE);
            mOptionalText.setVisibility(View.VISIBLE);
            mSpecificCard.setVisibility(View.VISIBLE);
            mIgnoreCard.setVisibility(View.VISIBLE);
            mAddRuleFab.setVisibility(View.VISIBLE);
            mRecMsg.setVisibility(View.GONE);
        });

        if (i==0){
            mSubtractReply.setEnabled(false);
            mAllChip.setEnabled(false);
            mRandomChip.setEnabled(false);
        }

        mAddReply.setOnClickListener(view -> {

            editText = new EditText(AddRuleActivity.this);
            editText.setId(i);
            editText.setHint("Should be sent");
            editText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            //editText.setPadding(8, 8, 8, 8);

            textHolder.add(editText);
            //Toast.makeText(this, ""+i, Toast.LENGTH_SHORT).show();

            mLinearLayout.addView(editText);
            i++;
            //Toast.makeText(this, ""+i, Toast.LENGTH_SHORT).show();
            mSubtractReply.setEnabled(true);
            mSingleChip.setEnabled(false);
            mAllChip.setChecked(true);
            mAllChip.setEnabled(true);
            mRandomChip.setEnabled(true);

        });

        mSubtractReply.setOnClickListener(view -> {

            mLinearLayout.removeViewAt(i);
            //Toast.makeText(this, "Removed view from "+i, Toast.LENGTH_SHORT).show();
            textHolder.remove(i-1);
            //Toast.makeText(this, textHolder.toString(), Toast.LENGTH_SHORT).show();
            i--;
            //Toast.makeText(this, ""+i, Toast.LENGTH_SHORT).show();

            if (i==0){
                mSubtractReply.setEnabled(false);
                mSingleChip.setChecked(true);
                mSingleChip.setEnabled(true);
                mAllChip.setEnabled(false);
                mRandomChip.setEnabled(false);
            }
        });

        mAddRuleFab.setOnClickListener(view -> {

            if (TextUtils.isEmpty(mReplyMsg.getText().toString())){
                Toast.makeText(this, "You have to fill all replies", Toast.LENGTH_SHORT).show();
                //replyMsg = new StringBuilder("");
            }else{
                replyMsg = new StringBuilder(mReplyMsg.getText().toString());
                for (EditText editText : textHolder) {
                    replyMsg.append(";").append(editText.getText().toString());
                }

            }

            if (mMatchMsg.isChecked()){

                if (TextUtils.isEmpty(mRecMsg.getText().toString())) {
                    Toast.makeText(this, "You have to fill a received message ", Toast.LENGTH_SHORT).show();
                    //recvMsg = "";
                }else {
                    recvMsg = mRecMsg.getText().toString();
                    //addRule();
                }
            }else if (mWelcomeMsg.isChecked()){
                recvMsg = "*welcome*";
            } else if(mAllMsg.isChecked()){
                recvMsg = "*";
            }

            if (mSingleChip.isChecked()){
                msgCount = "single";
            } else if (mAllChip.isChecked()){
                msgCount = "all";
            } else if (mRandomChip.isChecked()){
                msgCount = "random";
            }

            //contacts = mSpecificContacts.getText().toString()

           if (recvMsg.isEmpty() || mReplyMsg.getText().toString().isEmpty()){
               Toast.makeText(this, "You have to fill all fields", Toast.LENGTH_SHORT).show();
           } else {

               if (replyMsg.charAt(replyMsg.length() - 1) == ';'){
                   Toast.makeText(this, "Delete empty reply", Toast.LENGTH_SHORT).show();
               }else {

                   if (type.equals("new")) {
                       addRule();
                   }else if (type.equals("update")){
                       updateRule();
                   }
               }
           }

        });
    }

    private void updateRule() {

        dbHandler.updateFullRule(Integer.valueOf(getIntent().getExtras().getString("ruleId")), getIntent().getExtras().getString("ruleStatus"), recvMsg, String.valueOf(replyMsg), msgCount, mSpecificContacts.getText().toString(), mIgnoredContacts.getText().toString());
        Intent addNewRule = new Intent(AddRuleActivity.this, MainActivity.class);
        addNewRule.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(addNewRule);
        finish();
    }

    private void addRule() {
        dbHandler.addNewRule("1", recvMsg, String.valueOf(replyMsg), msgCount, mSpecificContacts.getText().toString(), mIgnoredContacts.getText().toString());

        Intent addNewRule = new Intent(AddRuleActivity.this, MainActivity.class);
        addNewRule.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(addNewRule);
        finish();
    }
}
