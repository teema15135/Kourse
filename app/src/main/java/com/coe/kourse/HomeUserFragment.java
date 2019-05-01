package com.coe.kourse;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomeUserFragment extends Fragment {

    static int fragmentCounter = 0;

    String userName;

    View contentView;
    TextView nameTextView;

    public void setUserName(String userName) {
        fragmentCounter++;
        this.userName = userName;
//        nameTextView.setText(userName);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        createViews();
        setViewAttributes();
        addContents();
        Log.d("HomeUserFragment", "View Created");
        return this.contentView;

    }

    private void createViews() {
        contentView = new LinearLayout(getActivity());
        nameTextView = new TextView(getActivity());
    }

    private void setViewAttributes() {
        nameTextView.setText(userName);
        nameTextView.setGravity(Gravity.CENTER);
    }

    private void addContents() {
        LinearLayout contentView = (LinearLayout) this.contentView;
        contentView.addView(nameTextView);
    }

}
