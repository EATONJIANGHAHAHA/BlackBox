package com.mad.blackbox.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mad.blackbox.R;

public class FeedbackActivity extends AppCompatActivity {

    private EditText mExperienceEdt;
    private EditText mSuggestionEdt;
    private EditText mNameEdt;
    private Button mSubmitBtn;
    private Button mCancelBtn;
    private String mIsCanceled = "false";

    /**
     * this method manage the events that user drawn from the activity feedback user interface.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        mExperienceEdt = (EditText) findViewById(R.id.experienceTxt);
        mSuggestionEdt = (EditText) findViewById(R.id.suggestionsTxt);
        mNameEdt = (EditText) findViewById(R.id.nameTxt);
        mSubmitBtn = (Button) findViewById(R.id.submitBtn);
        mCancelBtn = (Button) findViewById(R.id.cancelBtn);


        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mNameEdt.getText().toString();
                Intent result = getIntent();
                result.putExtra("name", name);
                result.putExtra("isCanceled", mIsCanceled);
                if(mExperienceEdt.getText().toString() != null
                        && mSuggestionEdt.getText().toString() != null
                        && name != null){
                    setResult(RESULT_OK,result);
                    finish();
                }
                else Toast.makeText(FeedbackActivity.this, "Please fill in all fields and then submit", Toast.LENGTH_LONG);
            }
        });
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = getIntent();
                mIsCanceled = "true";
                result.putExtra("isCanceled",mIsCanceled);
                setResult(RESULT_OK,result);
                finish();
            }
        });
    }
}
