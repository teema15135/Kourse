package com.coe.kourse;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNav;

    static boolean isHome = false;

     static Fragment homeFragment,
            calendarFragment,
            notificationFragment,
            anotherProfileFragment,
            selectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeFragment = new HomeFragment();
        calendarFragment = new CalendarFragment();
        notificationFragment = new NotificationFragment();
        anotherProfileFragment = new AnotherProfileFragment();

        bottomNav = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(this);

        String page = getIntent().getStringExtra("Page");
        Log.d("Page", page);

        switch (page) {
            default:
            case "Home" : getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    homeFragment).commit(); isHome = true; break;
            case "Reminder" : getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    notificationFragment).commit(); isHome = false; break;
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        selectedFragment = null;

        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                selectedFragment = homeFragment;
                isHome = true;
                break;
            case R.id.nav_calendar:
                selectedFragment = calendarFragment;
                isHome = false;
                break;
            case R.id.nav_notification:
                selectedFragment = notificationFragment;
                isHome = false;
                break;
            case R.id.nav_another_profile:
                selectedFragment = anotherProfileFragment;
                isHome = false;
                break;
        }

        if(selectedFragment != null) getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                selectedFragment).commit();

        return true;
    }

}
