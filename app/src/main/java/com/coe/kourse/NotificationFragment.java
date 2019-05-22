package com.coe.kourse;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.coe.kourse.data.AlarmReminderContract;
import com.coe.kourse.data.AlarmReminderDbHelper;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class NotificationFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    ArrayList<CourseEvent> events;
    FloatingActionButton fabReminder;

    private FloatingActionButton mAddReminderButton;
    private Toolbar mToolbar;
    AlarmCursorAdapter mCursorAdapter;
    AlarmReminderDbHelper alarmReminderDbHelper = new AlarmReminderDbHelper(getActivity());
    ListView reminderListView;
    ProgressDialog prgDialog;
    TextView timeReminder, dateReminder;
    private String alarmTitle = "", alarmDate = "", alarmTime = "";
    private static final int VEHICLE_LOADER = 0;

    EditText nameReminder;
    boolean datePicked;
    Calendar calendar;
    Date payDay;
    final String[] MONTH_NAME = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    int year, month, dayOfMonth, hour, minute;

    String receivedTitle="", receivedDate="", receivedTime="";
    ContentValues values;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            receivedTitle = bundle.getString("title"); // Key, default value
            receivedDate = bundle.getString("date");
            receivedTime = bundle.getString("time");

            values.put(AlarmReminderContract.AlarmReminderEntry.KEY_TITLE, receivedTitle);
            values.put(AlarmReminderContract.AlarmReminderEntry.KEY_DATE, receivedDate);
            values.put(AlarmReminderContract.AlarmReminderEntry.KEY_TIME, receivedTime);

            Uri newUri = getActivity().getContentResolver().insert(AlarmReminderContract.AlarmReminderEntry.CONTENT_URI, values);

            restartLoader();
        }





        values = new ContentValues();

        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.reminder_title);


        reminderListView = (ListView) view.findViewById(R.id.list);

        View emptyView = view.findViewById(R.id.empty_view);
        reminderListView.setEmptyView(emptyView);

        mCursorAdapter = new AlarmCursorAdapter(getActivity(), null);
        reminderListView.setAdapter(mCursorAdapter);

        reminderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), AddReminderActivity.class);

                Uri currentVehicleUri = ContentUris.withAppendedId(AlarmReminderContract.AlarmReminderEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                intent.setData(currentVehicleUri);
                startActivity(intent);

            }
        });


        mAddReminderButton = (FloatingActionButton) view.findViewById(R.id.fab);
        mAddReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(v.getContext(), AddReminderActivity.class);
                //startActivity(intent);
                addReminderTitle();
            }
        });

        LoaderManager.getInstance(this).initLoader(VEHICLE_LOADER, null, this);

        return view;
    }



    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                AlarmReminderContract.AlarmReminderEntry._ID,
                AlarmReminderContract.AlarmReminderEntry.KEY_TITLE,
                AlarmReminderContract.AlarmReminderEntry.KEY_DATE,
                AlarmReminderContract.AlarmReminderEntry.KEY_TIME,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE,
                AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE

        };

        return new CursorLoader(getActivity(),   // Parent activity context
                AlarmReminderContract.AlarmReminderEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    public void addReminderTitle() {
        final Dialog dialogReminder = new Dialog(getActivity());
        dialogReminder.setContentView(R.layout.dialog_add_reminder);

        nameReminder = (EditText) dialogReminder.findViewById(R.id.reminder_name_edt);
        timeReminder = (TextView) dialogReminder.findViewById(R.id.reminder_time_txt);
        dateReminder = (TextView) dialogReminder.findViewById(R.id.reminder_date_txt);
        Button buttonCancel = (Button) dialogReminder.findViewById(R.id.reminder_btn_cancel);
        Button buttonOK = (Button) dialogReminder.findViewById(R.id.reminder_btn_ok);

        ImageButton selectDate = (ImageButton) dialogReminder.findViewById(R.id.btn_calendar);
        ImageButton selectTime = (ImageButton) dialogReminder.findViewById(R.id.btn_time);
        TextView txtshowDate = dialogReminder.findViewById(R.id.reminder_date_txt);
        TextView txtshowTime = dialogReminder.findViewById(R.id.reminder_time_txt);


        View.OnClickListener selDateListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                txtshowDate.setText(day + "/" + month + "/" + year);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();
                Log.d("TAGG", "dialog show");
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            }

        };


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
                        txtshowTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


        selectDate.setOnClickListener(selDateListener);
        txtshowDate.setOnClickListener(selDateListener);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogReminder.dismiss();
            }
        });

        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameReminder.getText().toString().isEmpty()){
                    return;
                }

                alarmTitle = nameReminder.getText().toString();
                alarmDate = dateReminder.getText().toString();
                alarmTime = timeReminder.getText().toString();

                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_TITLE, alarmTitle);
                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_DATE, alarmDate);
                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_TIME, alarmTime);

                Uri newUri = getActivity().getContentResolver().insert(AlarmReminderContract.AlarmReminderEntry.CONTENT_URI, values);

                restartLoader();

                dialogReminder.dismiss();
            }
        });

        dialogReminder.show();

    }


    public void restartLoader(){
        LoaderManager.getInstance(this).restartLoader(VEHICLE_LOADER, null, this);
    }
}
