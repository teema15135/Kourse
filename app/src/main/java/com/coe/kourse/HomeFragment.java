package com.coe.kourse;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.coe.kourse.data.AlarmReminderContract;
import com.dpro.widgets.OnWeekdaysChangeListener;
import com.dpro.widgets.WeekdaysPicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.Calendar.FRIDAY;
import static java.util.Calendar.SATURDAY;
import static java.util.Calendar.SUNDAY;
import static java.util.Calendar.THURSDAY;
import static java.util.Calendar.TUESDAY;


public class HomeFragment extends Fragment {

    String TAG = "HomeFragment";
    final String[] MONTH_NAME = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    ValueEventListener currentListener;
    private static final int VEHICLE_LOADER = 0;

    boolean userFrameUnbonded = true;
    String myresult = "";
    String accountUID;
    int maxFragment = 0;
    int currentUserIndex = 0;

    List<User> userList;
    List<Fragment> userFragmentList;
    ArrayList<ArrayList<Course>> allUserCourseList;

    DatabaseReference accountRef, usersRef;
    FirebaseDatabase database;
    FirebaseAuth accountAuth;

    View view;
    TextView usernameHomeTextView;
    Animation textAnimation;
    FrameLayout fragment_sweep_container;
    FloatingActionButton fab;
    ProgressBar homeLoadingProgressBar;
    LinearLayout bulletPageIndicator;

    String sCourse, sColor, sStampAmount;
    LinearLayout linearSwipe;
    int widthLayout, heightLayout,
            marginBottomLayout, paddingLayout, paddingLeftLayout,
            widthHeightStampbtn, marginStamp;
    EditText nameCourse, totalCourse, payAmount, payDate, startDate;
    RadioGroup typeRadioGroup;
    RadioButton nonFixTimeTypeRadioButton, fixTimeTypeRadioButton;
    int stampAmount;

    private boolean isSelected = false;

    boolean startDatePicked = false;
    boolean datePicked = false;
    boolean courseTimePicked = false;
    Calendar calendar;
    Date payDay;
    String sStartDate;

    Fragment fragment;

    private final LinkedHashMap<Integer, Boolean> mp = new LinkedHashMap<>();
    private WeekdaysPicker widget;
    private List<Integer> selected_days;
    private TextView tv_selected_days;

    int year, month, dayOfMonth, hour, minute;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        maxFragment = 0;
        userFrameUnbonded = true;
        userFragmentList = new ArrayList<>();
        allUserCourseList = new ArrayList<>();

        fragment = MainActivity.notificationFragment;


        initializeFirebaseComponent();
        initializeViews();
        addViewListeners();

        updateUserList();

        return view;
    }


    private void initializeFirebaseComponent() {
        database = FirebaseDatabase.getInstance();
        accountAuth = FirebaseAuth.getInstance();
        accountUID = accountAuth.getCurrentUser().getUid();

        userList = new ArrayList<>();

        accountRef = database.getReference();
        usersRef = accountRef.child("accounts").child(accountUID).child("users");
    }

    private void initializeViews() {
        linearSwipe = view.findViewById(R.id.linearSwipe);
        usernameHomeTextView = view.findViewById(R.id.usernameHomeTextView);
        fab = view.findViewById(R.id.fab);
        homeLoadingProgressBar = view.findViewById(R.id.homeLoadingProgressBar);
        bulletPageIndicator = view.findViewById(R.id.bulletPageIndicator);

        textAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.enter_from_left);

        widthLayout = (int) getResources().getDimension(R.dimen.l_course_width);
        heightLayout = (int) getResources().getDimension(R.dimen.l_course_height);
        marginBottomLayout = (int) getResources().getDimension(R.dimen.margin_course_bottom);
        paddingLayout = (int) getResources().getDimensionPixelOffset(R.dimen.padding_course_layout);
        paddingLeftLayout = (int) getResources().getDimensionPixelOffset(R.dimen.paddingl_course_layout);
        widthHeightStampbtn = (int) getResources().getDimension(R.dimen.stamp_width_height);
        marginStamp = (int) getResources().getDimension(R.dimen.stamp_margin);
    }

    private void addViewListeners() {
        view.setOnTouchListener(new OnSwipeTouchListener(this.getContext()) {
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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_add_course);

                sColor = null;

                nonFixTimeTypeRadioButton = (RadioButton) dialog.findViewById(R.id.nonFixTimeTypeRadioButton);
                fixTimeTypeRadioButton = (RadioButton) dialog.findViewById(R.id.fixTimeTypeRadioButton);
                nameCourse = (EditText) dialog.findViewById(R.id.add_name_edittext);
                totalCourse = (EditText) dialog.findViewById(R.id.add_total_edittext);
                payAmount = (EditText) dialog.findViewById(R.id.add_pay_edittext);
                payDate = (EditText) dialog.findViewById(R.id.add_due_edittext);
                startDate = (EditText) dialog.findViewById(R.id.add_start_edt);
                Button buttonCancel = (Button) dialog.findViewById(R.id.home_btn_cancel);
                Button buttonOK = (Button) dialog.findViewById(R.id.home_btn_ok);

                Button buttonBlue = (Button) dialog.findViewById(R.id.add_blue_button);
                Button buttonGreen = (Button) dialog.findViewById(R.id.add_green_button);
                Button buttonRed = (Button) dialog.findViewById(R.id.add_red_button);
                Button buttonYellow = (Button) dialog.findViewById(R.id.add_yellow_button);

                ImageButton selectTime = (ImageButton) dialog.findViewById(R.id.btn_time);
                TextView txtshowTime = (TextView) dialog.findViewById(R.id.txt_time_picker);


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

                calendar = Calendar.getInstance();

                datePicked = false;
                payDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                datePicked = true;
                                payDay = new Date();
                                payDay.setYear(year);
                                payDay.setMonth(month);
                                payDay.setDate(dayOfMonth);
                                payDay.setHours(1);
                                payDate.setText(payDay.getDate() + "/" + MONTH_NAME[payDay.getMonth()] + "/" + payDay.getYear());
                            }
                        };
                        DatePickerDialog dpd = new DatePickerDialog(getContext(), listener,
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DATE));
                        dpd.getDatePicker().setMinDate(System.currentTimeMillis());
                        dpd.show();
                    }
                });

                startDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                startDatePicked = true;
                                payDay = new Date();
                                payDay.setYear(year);
                                payDay.setMonth(month);
                                payDay.setDate(dayOfMonth);
                                payDay.setHours(1);
                                startDate.setText(payDay.getDate() + "/" + MONTH_NAME[payDay.getMonth()] + "/" + payDay.getYear());
                                sStartDate = payDay.getDate() + "-" + (payDay.getMonth() + 1) + "-" + payDay.getYear();
                            }
                        };
                        DatePickerDialog dpd = new DatePickerDialog(getContext(), listener,
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DATE));
                        dpd.getDatePicker().setMinDate(System.currentTimeMillis());
                        dpd.show();
                    }
                });

                selectTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Calendar mcurrentTime = Calendar.getInstance();
                        hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                        minute = mcurrentTime.get(Calendar.MINUTE);
                        TimePickerDialog mTimePicker;
                        mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                courseTimePicked = true;
                                txtshowTime.setText(String.format("%02d",selectedHour) + ":" + String.format("%02d",selectedMinute));
                            }
                        }, hour, minute, true);//Yes 24 hour time
                        mTimePicker.setTitle("Select Time");
                        mTimePicker.show();

                    }
                });


                selected_days = Arrays.asList(Calendar.SATURDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.SUNDAY);
                mp.put(SUNDAY, true);
                mp.put(SATURDAY, true);
                mp.put(THURSDAY, false);
                mp.put(FRIDAY, true);
                mp.put(TUESDAY, true);
                mp.put(SATURDAY, false); //For duplicated values, the first one is counting, but the last one is updating the selected value

                ArrayList<Integer> days = new ArrayList<>();

                WeekdaysPicker widget = (WeekdaysPicker) dialog.findViewById(R.id.weekdays);
                widget.setOnWeekdaysChangeListener(new OnWeekdaysChangeListener() {
                    @Override
                    public void onChange(View view, int clickedDayOfWeek, List<Integer> selectedDays) {
//                tv_selected_days.setText("Selected days: " + Arrays.toString(selectedDays.toArray()));
                        days.removeAll(days);
                        days.addAll(selectedDays);
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

                        int timeType = 0;
                        if (fixTimeTypeRadioButton.isChecked()) timeType = 1;

                        sCourse = nameCourse.getText().toString();
                        sColor = (sColor == null ? "#b3d53f" : sColor);
                        sStampAmount = totalCourse.getText().toString(); // May cause Number Exception
                        stampAmount = Integer.parseInt(sStampAmount);

                        String sPayAmount = payAmount.getText().toString();
                        String sPayDate = payDate.getText().toString();
                        String sTime = txtshowTime.getText().toString();

                        Course course = new Course(sCourse, sColor, stampAmount, timeType, "N/A", "N/A", "N/A");

                        if (courseTimePicked && startDatePicked && timeType == 1) {
                            /*
                             * if start date and time picked and type is fix time
                             * change course.start, course.time and course.date
                             */
                            String sDay = "";

                            for (int k = 0; k < days.size(); k++) {
                                sDay += Integer.toString(days.get(k) - 1);
                            }

                            Log.d(TAG, sDay);

                            course.start = sStartDate;
                            course.time = sTime;
                            course.date = sDay;

                        }

                        User currentUser = userList.get(currentUserIndex);
                        currentUser.addCourse(course);
                        Map<String, Object> userMap = currentUser.toMap();

                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put(currentUser.getID(), userMap);
                        usersRef.updateChildren(childUpdates);

                        Log.d("TAG", "payamount : " + sPayAmount + " payDate : " + sPayDate);
                        if (!(sPayAmount.isEmpty() || sPayDate.isEmpty())) {
                            Log.d("TAG", "Go into if");
                            /**
                             * send data to another fragment
                             */
                            Bundle bundle = new Bundle();
                            bundle.putString("date", payDate.getText().toString()); // Key, value
                            bundle.putString("time", "08:00");
                            bundle.putString("title", sCourse + " : payment amount " + sPayDate);

                            fragment.setArguments(bundle);
                            MainActivity.bottomNavMain.setSelectedItemId(R.id.nav_notification);

                            MainActivity.isHome = false;
                        }


                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    private void updateBullet() {
        bulletPageIndicator.removeAllViews();
        int numBullet = userFragmentList.size();
        for (int i = 0; i < numBullet; i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setPadding(8, 0, 8, 0);
            imageView.setImageResource((i == currentUserIndex ? R.drawable.bullet_selected : R.drawable.bullet_unselected));
            bulletPageIndicator.addView(imageView);
        }
    }

    private void updateBannerName() {
        usernameHomeTextView.setText(userList.get(currentUserIndex).getName());
    }

    private void leftUser() {
        if (currentUserIndex == 0) return;
        changeUser(currentUserIndex - 1, 1);
        currentUserIndex -= 1;
        updateBannerName();
        updateBullet();
    }

    private void rightUser() {
        if (currentUserIndex == maxFragment - 1) return;
        changeUser(currentUserIndex + 1, 0);
        currentUserIndex += 1;
        updateBannerName();
        updateBullet();
    }

    private void fetchUser() {
        Log.d(TAG, "Refreshing user");
        Fragment fragment = userFragmentList.get(currentUserIndex);
        userFrameUnbonded = false;
        getChildFragmentManager().beginTransaction().replace(R.id.fragment_sweep_container, fragment).commit();
        updateBannerName();
        updateBullet();
    }

    private void changeUser(int index) {
        Log.d(TAG, "Change User to " + index);
        Fragment selectedFragment = userFragmentList.get(index);
        userFrameUnbonded = false;
        getChildFragmentManager().beginTransaction().replace(R.id.fragment_sweep_container, selectedFragment).commit();
    }

    private void changeUser(int index, int direction) {
        Log.d(TAG, "Change User to " + index);
        Fragment selectedFragment = userFragmentList.get(index);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (direction == 1)
            transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        else
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.fragment_sweep_container, selectedFragment);
        transaction.commit();
    }

    private void updateUserList() {
        // userListLinearLayout.removeAllViewsInLayout();
        if (currentListener != null) usersRef.removeEventListener(currentListener);

        currentListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userFragmentList = new ArrayList<>();
                homeLoadingProgressBar.setVisibility(View.INVISIBLE);
                for (DataSnapshot unique : dataSnapshot.getChildren()) {

                    User user = unique.getValue(User.class);
                    userList.add(user);

                    Log.d(TAG, user.toString());

                    // Set Fragment Content
                    HomeUserFragment userFragment = new HomeUserFragment();
                    userFragment.setUser(user);

                    userFragmentList.add(userFragment);
                    Log.d(TAG, "fragment array added");

                    maxFragment++;
                    Log.d(TAG, "MaxFragment is " + maxFragment);
                }

                if (userFrameUnbonded) {
                    try {
                        changeUser(currentUserIndex);
                        updateBannerName();
                        updateBullet();
                        homeLoadingProgressBar.setVisibility(View.INVISIBLE);
                    } catch (Exception e) {
                    }
                } else if (MainActivity.isHome) {
                    fetchUser();
                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };

        usersRef.addValueEventListener(currentListener);
    }

}
