package com.coe.kourse;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AnotherProfileFragment extends Fragment {

    String TAG = "AnotherProfile";

    Button dialogButton;
    TextView user;
    String name;
    LinearLayout dynamicview;

    String accountEmail, accountUID;

    DatabaseReference accountRef, usersRef;
    FirebaseDatabase database;
    FirebaseAuth accountAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_another_profile, container, false);

        database = FirebaseDatabase.getInstance();
        accountAuth = FirebaseAuth.getInstance();
        accountUID = accountAuth.getCurrentUser().getUid();
        accountEmail = accountAuth.getCurrentUser().getEmail();

        accountRef = database.getReference();
        usersRef = accountRef.child("accounts").child(accountUID).child("users");

        updateUserList();

        dialogButton = view.findViewById(R.id.a_profile_btn_person);
        user = view.findViewById(R.id.a_user1);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                //dialog.setTitle("Add person");
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
                        name = namePerson.getText().toString();
                        User newUser = new User(name);
                        String userID = newUser.getID();
                        DatabaseReference userRef = usersRef.child(userID);
                        Map<String, Object> userMap = newUser.toMap();

                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put(userID, userMap);
                        usersRef.updateChildren(childUpdates);

                        dialog.dismiss();

                        dynamicview = view.findViewById(R.id.a_llayout);
                        LinearLayout.LayoutParams lprams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);

                        for(int i=0;i<1;i++){
                            Button btn = new Button(getActivity());
                            btn.setId(i+1);
                            btn.setText(name);
                            btn.setLayoutParams(lprams);
                            btn.setAllCaps(false);
                            dynamicview.addView(btn);
                        }
                    }
                });

                dialog.show();
            }
        });

        return view;
    }

    private void updateUserList() {
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
}
