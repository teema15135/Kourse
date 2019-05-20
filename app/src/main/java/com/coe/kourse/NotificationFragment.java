package com.coe.kourse;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class NotificationFragment extends Fragment {


    ArrayList<CourseEvent> events;

    FloatingActionButton fabReminder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        events = new ArrayList<>();
        addTestEvents();

        CourseEvent event = events.get(0);

        return view;
    }

    private void addTestEvents() {
        CourseEvent c1 = new CourseEvent(/* fill something */);
        events.add(c1);
    }
}
