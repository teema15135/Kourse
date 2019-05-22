package com.coe.kourse;

import android.app.Dialog;
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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeUserFragment extends Fragment {

    String TAG = "HomeUserFragment";

    String userName;
    ArrayList<Course> courses;

    User user;

    View scrollView, contentView;
    TextView nameTextView;

    int selectCoursePosition;

    int widthLayout, heightLayout,
            marginBottomLayout, paddingLayout, paddingLeftLayout,
            widthHeightStampbtn, marginStamp;

    public void setUser(@NonNull User user) {
        try {
            while (user == null) ;
            Log.d(TAG, "Done wait for null");
            this.userName = user.getName();
            this.courses = user.getCourses();
            this.user = user;
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
        int numCourse = this.courses.size();
        for (int i = 0; i < numCourse; i++) {
            if (courses.get(i).getName().equals("N/A")) continue;
            createCourseElements(contentView, courses.get(i).getName(), courses.get(i).getColor(), courses.get(i).getAttend(), courses.get(i).getTotal(), courses.get(i), i);
        }
        if (this.courses.size() == 1) {
            LinearLayout linearLayout = new LinearLayout(getActivity());
            linearLayout.setPadding(0, heightLayout, 0, 0);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            TextView textView = new TextView(getActivity());
            textView.setGravity(Gravity.CENTER);
            textView.setText("This user have no course added");

            linearLayout.addView(textView);

            contentView.addView(linearLayout);
        }
        scrollView.addView(contentView);
    }

    private void createCourseElements(LinearLayout main, String sCourse, String sColor, int stampAttend, int stampAmount, Course course, int index) {

        LinearLayout.LayoutParams mainPrams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lprams = new LinearLayout.LayoutParams(widthLayout, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams sublprams = new LinearLayout.LayoutParams(widthLayout, heightLayout);
        LinearLayout.LayoutParams stamplprams = new LinearLayout.LayoutParams(widthLayout, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams stampBtnlprams = new LinearLayout.LayoutParams(widthHeightStampbtn, widthHeightStampbtn);

        LinearLayout margin = new LinearLayout(getActivity());
        margin.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, paddingLayout));

        lprams.setMargins(0, marginBottomLayout, 0, 0);
        stampBtnlprams.setMargins(0, 0, marginStamp, marginStamp);
        mainPrams.setMargins(0, marginBottomLayout, 0, 0);

        //Main Layout
        main.setOrientation(LinearLayout.VERTICAL);
        main.setPadding(0, paddingLayout, 0, paddingLayout);
        main.setGravity(Gravity.CENTER_HORIZONTAL);
        main.setLayoutParams(mainPrams);

        //minimize layout (pop-up layout)
        RelativeLayout row = new RelativeLayout(getActivity());
        row.setClipToOutline(true);
        row.setPadding(paddingLeftLayout, paddingLayout, paddingLeftLayout, paddingLayout);
        row.setLayoutParams(sublprams);
        row.setBackground(getDrawableWithRadius(sColor));

        row.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popup = new PopupMenu(getContext(), v);
                MenuInflater inflater = popup.getMenuInflater();
                selectCoursePosition = index;
                inflater.inflate(R.menu.menu_username_manage, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.renameUsername:
                                renameCourse();
                                return true;
                            case R.id.deleteUsername:
                                deleteCourse();
                                return true;
                            default: return false;
                        }
                    }
                });
                popup.show();
                return true;
            }
        });

        TextView showCourse = new TextView(getActivity());
        showCourse.setText(sCourse);
        showCourse.setTextColor(Color.WHITE);
        showCourse.setGravity(Gravity.LEFT);
        row.addView(showCourse);

        TextView showCourseNum = new TextView(getActivity());
        showCourseNum.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        showCourseNum.setText(Integer.toString(stampAttend));
        showCourseNum.setTextColor(Color.WHITE);
        showCourseNum.setGravity(Gravity.RIGHT);
        row.addView(showCourseNum);

        int rowOfStamp = stampAmount / 5;
        if (stampAmount % 5 != 0) rowOfStamp++;

        String lightColor = getLighterColor(sColor);
        int nonSelectStamp = R.drawable.stamp_unselect;
        int selectStamp = getSelectStampResource(sColor);

        LinearLayout allStampLayout = new LinearLayout(getActivity());
        allStampLayout.setOrientation(LinearLayout.VERTICAL);
        allStampLayout.setPadding(0, paddingLayout, paddingLayout, paddingLayout);
        allStampLayout.setLayoutParams(stamplprams);
        allStampLayout.setBackground(getDrawableWithRadiusStamp(lightColor));

        for (int i = 0; i < rowOfStamp; i++) {

            //stamp layout
            LinearLayout stampLayout = new LinearLayout(getActivity());
            stampLayout.setOrientation(LinearLayout.HORIZONTAL);
            stampLayout.setPadding(paddingLeftLayout, paddingLayout, paddingLayout, paddingLayout);
            stampLayout.setLayoutParams(stamplprams);
            stampLayout.setBackground(getDrawableWithRadiusStamp(lightColor));

            for (int j = 1; j <= 5; j++) {
                int numStamp = i * 5 + j;
                LinearLayout stampBtnLayout = new LinearLayout(getActivity());
                Button stampButton = new Button(getActivity());
                if (numStamp > stampAttend) {
                    stampButton.setBackgroundResource(nonSelectStamp);
                    stampButton.setText(Integer.toString(numStamp));
                    stampButton.setTextSize(10);
                    stampButton.setTextColor(0xFF3B3B3B);
                } else {
                    stampButton.setBackgroundResource(selectStamp);
                }
                stampBtnLayout.setLayoutParams(stampBtnlprams);
                stampBtnLayout.addView(stampButton);
                stampLayout.addView(stampBtnLayout);
                if (numStamp == stampAttend + 1)
                    stampButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            /* Increase the attend */

                            course.attend();
                            Log.d(TAG, "Attend added");

                            Map<String, Object> userMap = user.toMap();

                            Map<String, Object> childUpdates = new HashMap<>();
                            childUpdates.put(user.getID(), userMap);

                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            FirebaseUser user = auth.getCurrentUser();
                            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference()
                                    .child("accounts")
                                    .child(user.getUid())
                                    .child("users");
                            usersRef.updateChildren(childUpdates);

                            Toast.makeText(getActivity(), course.getName() + " attend !", Toast.LENGTH_SHORT).show();
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

    private String getLighterColor(String color) {
        switch (color) {
            case "#40a2b7":
                return "#a4d5e0";
            case "#b3d53f":
                return "#d8e8a0";
            case "#ffab2d":
                return "#fdffcc";
            case "#ff5855":
                return "#ffb0ae";
            default:
                return "#b9b0a2";
        }
    }

    private int getSelectStampResource(String color) {
        switch (color) {
            case "#40a2b7":
                return R.drawable.stamp_select_blue;
            case "#b3d53f":
                return R.drawable.stamp_select_green;
            case "#ffab2d":
                return R.drawable.stamp_select_orange;
            case "#ff5855":
                return R.drawable.stamp_select_red;
            default:
                return R.drawable.stamp_select_orange;
        }
    }

    private void renameCourse() {
        Log.d(TAG, "Course rename");
        Course selectCourse = courses.get(selectCoursePosition);

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_profile_person);

        final EditText namePerson = (EditText) dialog.findViewById(R.id.profile_dialog_name);
        Button buttonCancel = (Button) dialog.findViewById(R.id.profile_btn_cancel);
        Button buttonOK = (Button) dialog.findViewById(R.id.profile_btn_ok);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Push update to Firebase Database
                String name = namePerson.getText().toString();
                String userID = user.getID();
                courses.get(selectCoursePosition).name = name;

                String accountUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                        .child("accounts")
                        .child(accountUID)
                        .child("users")
                        .child(userID);

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("courses", courses);
                userRef.updateChildren(childUpdates);

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void deleteCourse() {
        Log.d(TAG, "Course delete");
        Dialog confirmDialog = new Dialog(getContext());
        confirmDialog.setContentView(R.layout.dialog_confirm);

        Button okButton = (Button) confirmDialog.findViewById(R.id.okConfirm);
        Button cancelButton = (Button) confirmDialog.findViewById(R.id.cancelConfirm);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = user.getID();
                courses.remove(selectCoursePosition);

                String accountUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                        .child("accounts")
                        .child(accountUID)
                        .child("users")
                        .child(userID);

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("courses", courses);
                userRef.updateChildren(childUpdates);

                confirmDialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
            }
        });

        confirmDialog.show();
    }
}
