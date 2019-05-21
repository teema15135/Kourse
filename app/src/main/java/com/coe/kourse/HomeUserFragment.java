package com.coe.kourse;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.SnackbarContentLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.LineNumberReader;
import java.util.ArrayList;

public class HomeUserFragment extends Fragment {

    String TAG = "HomeUserFragment";

    String userName;
    ArrayList<Course> courses;

    View scrollView, contentView;
    TextView nameTextView;

    int widthLayout, heightLayout,
            marginBottomLayout, paddingLayout, paddingLeftLayout,
            widthHeightStampbtn, marginStamp;

    public void setUser(@NonNull User user) {
        try {
            while(user == null);
            Log.d(TAG, "Done wait for null");
            this.userName = user.getName();
            this.courses = user.getCourses();
        } catch (NullPointerException e) {
            Log.d(TAG, "NullPointer for " + user.toString());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initialStaticValue();
        createViews();
        setViewAttributes();
        addContents();
        return this.scrollView;
    }

    private void initialStaticValue() {
        widthLayout = (int) getResources().getDimension(R.dimen.l_course_width);
        heightLayout = (int) getResources().getDimension(R.dimen.l_course_height);
        marginBottomLayout = (int) getResources().getDimension(R.dimen.margin_course_bottom);
        paddingLayout = (int) getResources().getDimensionPixelOffset(R.dimen.padding_course_layout);
        paddingLeftLayout = (int) getResources().getDimensionPixelOffset(R.dimen.paddingl_course_layout);
        widthHeightStampbtn = (int) getResources().getDimension(R.dimen.stamp_width_height);
        marginStamp = (int) getResources().getDimension(R.dimen.stamp_margin);
    }

    private void createViews() {
        scrollView = new ScrollView(getActivity());
        contentView = new LinearLayout(getActivity());
        nameTextView = new TextView(getActivity());
    }

    private void setViewAttributes() {
        nameTextView.setText(userName);
        nameTextView.setGravity(Gravity.CENTER);
    }

    private void addContents() {
        LinearLayout contentView = (LinearLayout) this.contentView;
        ScrollView scrollView = (ScrollView) this.scrollView;
        scrollView.removeAllViews();
        for( Course course : this.courses ) {
            if ( course.getName().equals("N/A") ) continue;
            createCourseElements(contentView, course.getName(), course.getColor(), course.getAttend(), course.getTotal());
        }
        scrollView.addView(contentView);
    }

    private void createCourseElements(LinearLayout main, String sCourse, String sColor, int stampAttend, int stampAmount) {

        LinearLayout.LayoutParams mainPrams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lprams = new LinearLayout.LayoutParams(widthLayout, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams sublprams = new LinearLayout.LayoutParams(widthLayout, heightLayout);
        LinearLayout.LayoutParams stamplprams = new LinearLayout.LayoutParams(widthLayout, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams stampBtnlprams = new LinearLayout.LayoutParams(widthHeightStampbtn, widthHeightStampbtn);

        LinearLayout margin = new LinearLayout(getActivity());
        margin.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, marginStamp));

        lprams.setMargins(0, marginBottomLayout, 0, 0);
        stampBtnlprams.setMargins(0, 0, marginStamp, marginStamp);
        mainPrams.setMargins(0, marginBottomLayout, 0, 0);

        //Main Layout
        main.setOrientation(LinearLayout.VERTICAL);
        main.setPadding(0, paddingLayout, 0, paddingLayout);
        main.setGravity(Gravity.CENTER_HORIZONTAL);
        main.setLayoutParams(mainPrams);

        //minimize layout (pop-up layout)
        LinearLayout row = new LinearLayout(getActivity());
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setClipToOutline(true);
        row.setPadding(paddingLeftLayout, paddingLayout, paddingLayout, paddingLayout);
        row.setLayoutParams(sublprams);
        row.setBackground(getDrawableWithRadius(sColor));

        TextView showCourse = new TextView(getActivity());
        showCourse.setText(sCourse);
        showCourse.setTextColor(Color.WHITE);
        row.addView(showCourse);

        int rowOfStamp = stampAmount / 5;
        if (stampAmount % 5 != 0) rowOfStamp++;

        LinearLayout allStampLayout = new LinearLayout(getActivity());
        allStampLayout.setOrientation(LinearLayout.VERTICAL);
        allStampLayout.setPadding(0, paddingLayout, paddingLayout, paddingLayout);
        allStampLayout.setLayoutParams(stamplprams);
        allStampLayout.setBackground(getDrawableWithRadiusStamp("#b9b0a2"));

        for (int i = 0; i < rowOfStamp; i++) {

            //stamp layout
            LinearLayout stampLayout = new LinearLayout(getActivity());
            stampLayout.setOrientation(LinearLayout.HORIZONTAL);
            stampLayout.setPadding(paddingLeftLayout, paddingLayout, paddingLayout, paddingLayout);
            stampLayout.setLayoutParams(stamplprams);
            stampLayout.setBackground(getDrawableWithRadiusStamp("#b9b0a2"));

            for (int j = 1; j <= 5; j++) {
                int numStamp = i * 5 + j;
                LinearLayout stampBtnLayout = new LinearLayout(getActivity());
                Button stampButton = new Button(getActivity());
                if (numStamp > stampAttend) {
                    stampButton.setBackgroundResource(R.drawable.stamp_unselect_white);
                } else {
                    // Must change to attended stamp
                    stampButton.setBackgroundResource(R.drawable.stamp_unselect_white);
                }
                stampButton.setText(Integer.toString(numStamp));
                stampButton.setTextSize(10);
                stampButton.setTextColor(Color.WHITE);
                stampBtnLayout.setLayoutParams(stampBtnlprams);
                stampBtnLayout.addView(stampButton);
                stampLayout.addView(stampBtnLayout);
                if (numStamp == stampAttend + 1)
                    stampButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            /* Increase the attend */
                        }
                    });
                if (numStamp == stampAmount)
                    break;
            }
            allStampLayout.addView(stampLayout);
        }

        main.addView(row);
        main.addView(allStampLayout);
        main.addView(margin);

        row.setVisibility(View.VISIBLE);
        allStampLayout.setVisibility(View.GONE);

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allStampLayout.getVisibility() == View.VISIBLE) {
                    allStampLayout.setVisibility(View.GONE);
                } else {
                    allStampLayout.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private Drawable getDrawableWithRadius(String sColor) {

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadii(new float[]{10, 10, 10, 10, 10, 10, 10, 10});
        gradientDrawable.setColor(Color.parseColor(sColor));
        return gradientDrawable;

    }

    private Drawable getDrawableWithRadiusStamp(String sColor) {

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadii(new float[]{0, 0, 0, 0, 0, 0, 0, 0});
        gradientDrawable.setColor(Color.parseColor(sColor));
        return gradientDrawable;

    }
}
