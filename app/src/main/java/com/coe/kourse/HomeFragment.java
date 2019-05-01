package com.coe.kourse;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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


    FloatingActionButton fab;

    String sCourse, sColor, sStampAmount ;
    LinearLayout layoutList;
    int widthLayout, heightLayout,
            marginBottomLayout, paddingLayout, paddingLeftLayout,
            widthHeightStampbtn, marginStamp;
    EditText nameCourse, totalCourse;
    int stampAmount;

    private boolean isSelected = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

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

        layoutList = view.findViewById(R.id.layout_list);


        widthLayout = (int) getResources().getDimension(R.dimen.l_course_width);
        heightLayout = (int) getResources().getDimension(R.dimen.l_course_height);
        marginBottomLayout = (int) getResources().getDimension(R.dimen.margin_course_bottom);
        paddingLayout = (int) getResources().getDimensionPixelOffset(R.dimen.padding_course_layout);
        paddingLeftLayout = (int) getResources().getDimensionPixelOffset(R.dimen.paddingl_course_layout);
        widthHeightStampbtn = (int) getResources().getDimension(R.dimen.stamp_width_height);
        marginStamp = (int) getResources().getDimension(R.dimen.stamp_margin);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_add_course);

                nameCourse = (EditText) dialog.findViewById(R.id.add_name_edittext);
                totalCourse = (EditText) dialog.findViewById(R.id.add_total_edittext);
                Button buttonCancel = (Button) dialog.findViewById(R.id.home_btn_cancel);
                Button buttonOK = (Button) dialog.findViewById(R.id.home_btn_ok);

                Button buttonBlue = (Button) dialog.findViewById(R.id.add_blue_button);
                Button buttonGreen = (Button) dialog.findViewById(R.id.add_green_button);
                Button buttonRed = (Button) dialog.findViewById(R.id.add_red_button);
                Button buttonYellow = (Button) dialog.findViewById(R.id.add_yellow_button);


                // just set Drawable of color buttons
                GradientDrawable blueSelected = new GradientDrawable();
                blueSelected.setShape(GradientDrawable.OVAL);
                blueSelected.setColor(Color.parseColor("#40a2b7"));
                blueSelected.setStroke(10, Color.parseColor("#D2D1D2"));

                GradientDrawable blueUnselected = new GradientDrawable();
                blueUnselected.setShape(GradientDrawable.OVAL);
                blueUnselected.setColor(Color.parseColor("#40a2b7"));

                GradientDrawable greenSelected = new GradientDrawable();
                greenSelected.setShape(GradientDrawable.OVAL);
                greenSelected.setColor(Color.parseColor("#b3d53f"));
                greenSelected.setStroke(10, Color.parseColor("#D2D1D2"));

                GradientDrawable greenUnselected = new GradientDrawable();
                greenUnselected.setShape(GradientDrawable.OVAL);
                greenUnselected.setColor(Color.parseColor("#b3d53f"));

                GradientDrawable redSelected = new GradientDrawable();
                redSelected.setShape(GradientDrawable.OVAL);
                redSelected.setColor(Color.parseColor("#ff5855"));
                redSelected.setStroke(10, Color.parseColor("#D2D1D2"));

                GradientDrawable redUnselected = new GradientDrawable();
                redUnselected.setShape(GradientDrawable.OVAL);
                redUnselected.setColor(Color.parseColor("#ff5855"));

                GradientDrawable yellowSelected = new GradientDrawable();
                yellowSelected.setShape(GradientDrawable.OVAL);
                yellowSelected.setColor(Color.parseColor("#ffab2d"));
                yellowSelected.setStroke(10, Color.parseColor("#D2D1D2"));

                GradientDrawable yellowUnselected = new GradientDrawable();
                yellowUnselected.setShape(GradientDrawable.OVAL);
                yellowUnselected.setColor(Color.parseColor("#ffab2d"));


                //selected color and then set color string
                //enable and unable stroke when click color button

                buttonBlue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sColor = "#40a2b7";
                        buttonBlue.setBackground(blueSelected);
                        buttonGreen.setBackground(greenUnselected);
                        buttonRed.setBackground(redUnselected);
                        buttonYellow.setBackground(yellowUnselected);
                    }
                });

                buttonGreen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sColor = "#b3d53f";
                        buttonBlue.setBackground(blueUnselected);
                        buttonGreen.setBackground(greenSelected);
                        buttonRed.setBackground(redUnselected);
                        buttonYellow.setBackground(yellowUnselected);
                    }
                });

                buttonRed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sColor = "#ff5855";
                        buttonBlue.setBackground(blueUnselected);
                        buttonGreen.setBackground(greenUnselected);
                        buttonRed.setBackground(redSelected);
                        buttonYellow.setBackground(yellowUnselected);
                    }
                });

                buttonYellow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sColor = "#ffab2d";
                        buttonBlue.setBackground(blueUnselected);
                        buttonGreen.setBackground(greenUnselected);
                        buttonRed.setBackground(redUnselected);
                        buttonYellow.setBackground(yellowSelected);
                    }
                });



                // when click ok or cancel button

                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                buttonOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        createCourseElements(sColor);

                    }
                });

                dialog.show();

            }


        return view;
    }

    private void leftUser() {
        if (currentUserIndex == 0) return;
        changeUser(currentUserIndex - 1, 1);
        currentUserIndex -= 1;
    }

    private void rightUser() {
        if (currentUserIndex == maxFragment - 1) return;
        changeUser(currentUserIndex + 1, 0);
        currentUserIndex += 1;
    }


    private Drawable getDrawableWithRadius(String sColor) {

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadii(new float[]{10, 10, 10, 10, 10, 10, 10, 10 });
        gradientDrawable.setColor(Color.parseColor(sColor));
        return gradientDrawable;

    }

    private Drawable getDrawableWithRadiusStamp(String sColor) {

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadii(new float[]{0, 0, 0, 0, 0, 0, 0, 0 });
        gradientDrawable.setColor(Color.parseColor(sColor));
        return gradientDrawable;

    }




    private void createCourseElements(String sColor) {

        sCourse  = nameCourse.getText().toString();
        sStampAmount = totalCourse.getText().toString();
        stampAmount = Integer.parseInt(sStampAmount);
        LinearLayout.LayoutParams lprams = new LinearLayout.LayoutParams(widthLayout, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams sublprams = new LinearLayout.LayoutParams(widthLayout, heightLayout);
        LinearLayout.LayoutParams stamplprams = new LinearLayout.LayoutParams(widthLayout, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams stampBtnlprams = new LinearLayout.LayoutParams(widthHeightStampbtn, widthHeightStampbtn);

        lprams.setMargins(0,marginBottomLayout,0,0);
        stampBtnlprams.setMargins(0,0,marginStamp,marginStamp);

        for(int i=0;i<1;i++){
            //Main Layout
            LinearLayout main = new LinearLayout(getActivity());
            main.setOrientation(LinearLayout.VERTICAL);
            main.setLayoutParams(lprams);

            //minimize layout
            LinearLayout row = new LinearLayout(getActivity());
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setClipToOutline(true);
            row.setPadding(paddingLeftLayout, paddingLayout, paddingLayout, paddingLayout );
            row.setLayoutParams(sublprams);
            row.setBackground(getDrawableWithRadius(sColor));

            TextView showCourse = new TextView(getActivity());
            showCourse.setText(sCourse);
            showCourse.setTextColor(Color.WHITE);
            row.addView(showCourse);

            //stamp layout
            LinearLayout stampLayout = new LinearLayout(getActivity());
            stampLayout.setOrientation(LinearLayout.HORIZONTAL);
            stampLayout.setPadding(paddingLeftLayout, paddingLayout, paddingLayout, paddingLayout );
            stampLayout.setLayoutParams(stamplprams);
            stampLayout.setBackground(getDrawableWithRadiusStamp("#b9b0a2"));

            for(int j=1 ; j <= stampAmount ; j++) {
                String numstamp = String.valueOf(j);
                LinearLayout stampBtnLayout = new LinearLayout(getActivity());
                Button stampButton = new Button(getActivity());
                stampButton.setBackgroundResource(R.drawable.stamp_unselect_white);
                stampButton.setText(numstamp);
                stampButton.setTextSize(10);
                stampButton.setTextColor(Color.WHITE);
                stampBtnLayout.setLayoutParams(stampBtnlprams);
                stampBtnLayout.addView(stampButton);
                stampLayout.addView(stampBtnLayout);
            }

            main.addView(row);
            main.addView(stampLayout);
            layoutList.addView(main);

            row.setVisibility(View.VISIBLE);
            stampLayout.setVisibility(View.GONE);

            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isSelected) {
                        isSelected = false;
                        stampLayout.setVisibility(View.GONE);
                    }
                    else {
                        isSelected = true;
                        stampLayout.setVisibility(View.VISIBLE);
                    }
                }
            });

        }

    }




}
