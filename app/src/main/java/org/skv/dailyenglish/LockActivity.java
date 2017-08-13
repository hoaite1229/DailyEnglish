package org.skv.dailyenglish;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class LockActivity extends AppCompatActivity {

    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);

        editText = (EditText)findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("yhseo", "text size is "+editText.length());
                if (editText.length() == 4) {
                    if (editText.getText().toString().equals("1234")) {
                        Log.i("yhseo", "password is right : " + editText.getText().toString());
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        AlarmReceiver.stopRinging();
                        startActivity(intent);
                    } else {
                        Log.i("yhseo", "password is wrong : " + editText.getText().toString());
                        Toast.makeText(getApplicationContext(), "Password is wrong. Please Retry.", Toast.LENGTH_SHORT).show();
                        editText.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
