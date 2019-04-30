package com.coe.kourse;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements View.OnClickListener {

    String TAG = "HomeFragment";
    ValueEventListener currentListener;

    boolean userFrameUnbonded = true;
    String myresult = "";
    String accountUID;
    int maxFragment = 0;
    int currentUserIndex = 0;

    List<User> userList;
    List<Fragment> userFragmentList;

    DatabaseReference accountRef, usersRef;
    FirebaseDatabase database;
    FirebaseAuth accountAuth;

    View view;
    FrameLayout fragment_sweep_container;
    LinearLayout homeSelectUserLayout;
    FloatingActionButton fab;
    Button userLeftButton, userRightButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        maxFragment = 0;
        userFrameUnbonded = true;
        userFragmentList = new ArrayList<>();

        initializeFirebaseComponent();
        initializeViews();
        addViewListeners();

        updateUserList();

        return view;
    }

    private void addViewListeners() {
        fab.setOnClickListener(this);
        userLeftButton.setOnClickListener(this);
        userRightButton.setOnClickListener(this);
        fragment_sweep_container.setOnTouchListener(new OnSwipeTouchListener(this.getContext()) {
            public void onSwipeTop() {
                Log.d(TAG, "TOP Swipe");
            }
            public void onSwipeRight() {
                leftUser();
            }
            public void onSwipeLeft() {
                rightUser();
            }
            public void onSwipeBottom() {
                Log.d(TAG, "BOTTOM Swipe");
            }

        });
    }

    private void initializeViews() {
        fragment_sweep_container = view.findViewById(R.id.fragment_sweep_container);
        homeSelectUserLayout = view.findViewById(R.id.homeSelectUserLayout);
        fab = view.findViewById(R.id.fab);
        userLeftButton = view.findViewById(R.id.userLeftButton);
        userRightButton = view.findViewById(R.id.userRightButton);
    }

    private void initializeFirebaseComponent() {
        database = FirebaseDatabase.getInstance();
        accountAuth = FirebaseAuth.getInstance();
        accountUID = accountAuth.getCurrentUser().getUid();

        userList = new ArrayList<>();

        accountRef = database.getReference();
        usersRef = accountRef.child("accounts").child(accountUID).child("users");
    }

    private void updateUserList() {
        // userListLinearLayout.removeAllViewsInLayout();
        if (currentListener != null) usersRef.removeEventListener(currentListener);

        currentListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, dataSnapshot.getValue().toString());
                for (DataSnapshot unique : dataSnapshot.getChildren()) {

                    User user = unique.getValue(User.class);
                    userList.add(user);

                    String userName = user.getName();

                    // Set Fragment Content
                    HomeUserFragment userFragment = new HomeUserFragment();
                    userFragment.setUserName(userName);

                    userFragmentList.add(userFragment);

                    if (userFrameUnbonded) {
                        try {
                            changeUser(currentUserIndex);
                        } catch (Exception e) {
                        }
                    }
                    maxFragment++;
                    Log.d(TAG, "MaxFragment is " + maxFragment);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };

        usersRef.addValueEventListener(currentListener);
    }

    private void leftUser() {
        if (currentUserIndex == 0) return;
        changeUser(currentUserIndex - 1);
        currentUserIndex -= 1;
    }

    private void rightUser() {
        if (currentUserIndex == maxFragment - 1) return;
        changeUser(currentUserIndex + 1);
        currentUserIndex += 1;
    }

    private void changeUser(int index) {
        Log.d(TAG, "Change User to " + index);
        Fragment selectedFragment = userFragmentList.get(index);
        userFrameUnbonded = false;
        getChildFragmentManager().beginTransaction().replace(R.id.fragment_sweep_container, selectedFragment).commit();
    }

    @Override
    public void onClick(View v) {
        if (v == fab) {
            Intent intent = new Intent(getActivity(), AddKourseActivity.class);
            startActivity(intent);
        } else if (v == userLeftButton) {
            leftUser();
        } else if (v == userRightButton) {
            rightUser();
        }
    }
}
