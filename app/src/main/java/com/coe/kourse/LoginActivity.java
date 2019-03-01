package com.coe.kourse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button googleLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        googleLoginBtn = (Button)findViewById(R.id.btn_google_login);

        googleLoginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == googleLoginBtn) {
            gotoApp();
        }
    }

    public void gotoApp() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
