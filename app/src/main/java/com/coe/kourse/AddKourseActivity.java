package com.coe.kourse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddKourseActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 10;
    EditText courseName, courseTotal, coursePay, courseDue, courseTime;
    Button blueButton, greenButton, yellowButton, redButton;
    String sName, sTotal, sPay, sDue, sTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kourse);

        courseName = (EditText) findViewById (R.id.add_name_edittext);
        courseTotal = (EditText) findViewById (R.id.add_total_edittext);
        coursePay = (EditText) findViewById (R.id.add_pay_edittext);
        courseDue = (EditText) findViewById (R.id.add_due_edittext);
        courseTime = (EditText) findViewById (R.id.add_time_edittext);
        blueButton = (Button) findViewById(R.id.add_blue_button);
        greenButton = (Button) findViewById(R.id.add_green_button);
        yellowButton = (Button) findViewById(R.id.add_yellow_button);
        redButton = (Button) findViewById(R.id.add_red_button);
    }

    public void addButtonClicked(View view) {

        sName = courseName.getText().toString();
        sTotal = courseTotal.getText().toString();
        sPay = coursePay.getText().toString();
        sDue = courseDue.getText().toString();
        sTime = courseTime.getText().toString();

//        Bundle bundle = new Bundle();
//        bundle.putString("sName", sName);
//        HomeFragment fragobj = new HomeFragment();
//        fragobj.setArguments(bundle);


//        Intent i = new Intent(this, HomeFragment.class);
//        i.putExtra("sName", sName);
//        i.putExtra("sTotal", sTotal);
//        i.putExtra("sPay", sPay);
//        i.putExtra("sDue", sDue);
//        i.putExtra("sTime", sTime);
////        startActivityForResult(i,REQUEST_CODE);
//        startActivity(i);
    }
}
