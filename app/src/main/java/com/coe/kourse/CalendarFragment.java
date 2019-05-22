package com.coe.kourse;

import android.app.DatePickerDialog;
import android.app.usage.UsageEvents;
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
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarFragment extends Fragment {

    CompactCalendarView compactCalendarView;
    TextView txt;
    ListView list;
    ArrayAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        compactCalendarView = view.findViewById(R.id.compactcalendar_view);
        compactCalendarView.setFirstDayOfWeek(Calendar.SUNDAY);
        list = view.findViewById(R.id.listView);

        createEvent();

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                ArrayList<String> listItems = new ArrayList<String>();
                adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,listItems);
                List<Event> events = compactCalendarView.getEvents(dateClicked);
                for(int i=0; i< events.size();i++) {
                    Event e = (Event)events.get(i);
                    listItems.add(e.getData().toString());
                }
                list.setAdapter(adapter);
                //list.setBackgroundColor(Color.YELLOW);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {

            }

        });

        return view;
    }

    private void createEvent() {
        Event ev1 = new Event(Color.WHITE, 1558544400000L, "Quitar Class");
        compactCalendarView.addEvent(ev1);
        Event ev2 = new Event(Color.YELLOW, 1558544400000L, "Bimmin is happy");
        compactCalendarView.addEvent(ev2);
        Event ev3 = new Event(Color.BLACK, 1558544400000L, "yeah");
        compactCalendarView.addEvent(ev3);
        Event ev4 = new Event(Color.BLACK, 1558630800000L, "Can i kick it ? yeah!");
        compactCalendarView.addEvent(ev4);

    }
}
