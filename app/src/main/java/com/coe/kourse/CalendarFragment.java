package com.coe.kourse;

import android.app.DatePickerDialog;
import android.app.usage.UsageEvents;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.chrono.ThaiBuddhistDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CalendarFragment extends Fragment {

    CompactCalendarView compactCalendarView;
    ListView list;
    ArrayAdapter adapter;
    String dateString, timeString, monthString;
    Long dateInMillis, currentDate, dateInMilliseconds;
    TextView month;
    Integer rColor;
    Long rDate;
    String rdata;

    FirebaseDatabase database;
    DatabaseReference rootRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        compactCalendarView = view.findViewById(R.id.compactcalendar_view);
        compactCalendarView.setFirstDayOfWeek(Calendar.SUNDAY);
        list = view.findViewById(R.id.listView);
        month = view.findViewById(R.id.monthtv);

        initializeFirebaseInstance();

        getInput();

        //setDefaultMonth
        currentDate = compactCalendarView.getFirstDayOfCurrentMonth().getTime();
        millisToDate(currentDate);
        month.setText(monthString + "");

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                ArrayList<String> listItems = new ArrayList<String>();
                adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,listItems);
                List<Event> events = compactCalendarView.getEvents(dateClicked);
                for(int i=0; i< events.size();i++) {
                    Event e = (Event)events.get(i);

                    //convert timeInmillis to date
                    dateInMillis = e.getTimeInMillis();
                    millisToDate(dateInMillis);

                    listItems.add(timeString + "\n" + e.getData().toString());
                }
                list.setAdapter(adapter);
                list.setBackgroundColor(getResources().getColor(R.color.calendar_grey));
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                currentDate = compactCalendarView.getFirstDayOfCurrentMonth().getTime();
                millisToDate(currentDate);
                month.setText(monthString + "");
            }

        });



        return view;
    }

    private void initializeFirebaseInstance() {
        database = FirebaseDatabase.getInstance();
        rootRef = database.getReference();
    }

    private  void getInput(){
        //input Example
        rColor = Color.YELLOW;
        dateString = "24-May-2019 00:00";
        dateToMillis(dateString);
        rDate = dateInMilliseconds;
        rdata = "test";
        createEvent();
    }

    private void millisToDate (Long dateInMillis){
        SimpleDateFormat formatter = new SimpleDateFormat("MMM, yyyy");
        //SimpleDateFormat monthFormatter = new SimpleDateFormat("MM",Locale.forLanguageTag("th"));
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH : mm");
        /*Date now = new Date();
        timeString = timeFormatter.format(now);*/

        //set TimeZone
        monthString = formatter.format(currentDate);
        timeString = timeFormatter.format(dateInMillis);
        return;
    }

    private void dateToMillis (String date) {
        //dateString = "23-05-2019 00:00:00";
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        try {
            Date mDate = dateFormatter.parse(dateString);
            dateInMilliseconds = mDate.getTime();
            return;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void createEvent() {
        Event ev = new Event(rColor, rDate, rdata);
        compactCalendarView.addEvent(ev);
    }

    private void createEvent(Integer rColor, long rDate, String rData) {
        Event ev = new Event(rColor, rDate, rData);
        compactCalendarView.addEvent(ev);
    }
}
