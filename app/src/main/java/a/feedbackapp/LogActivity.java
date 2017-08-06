package a.feedbackapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LogActivity extends AppCompatActivity {

    private EditText unF, pwF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        unF = (EditText) findViewById(R.id.in_un);
        pwF = (EditText) findViewById(R.id.in_pw);
    }

    public void login(View v) {
        final String un = unF.getText().toString(), pw = pwF.getText().toString();
        if (un.equals("") && pw.equals("password")) { //Default login method
            Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_SHORT).show();
            unF.getText().clear();
            pwF.getText().clear();
            Intent in = new Intent(getApplicationContext(), LarActivity.class);
            startActivity(in);
        } else {
            Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }

}
