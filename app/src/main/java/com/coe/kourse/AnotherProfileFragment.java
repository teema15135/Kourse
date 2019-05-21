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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AnotherProfileFragment extends Fragment {

    String TAG = "AnotherProfile";

    View view;

    CircleImageView profileImage;
    Button dialogButton;
    TextView user;
    String name;
    LinearLayout userListLinearLayout;

    String accountEmail, accountUID, accountName, accountURL;

    ValueEventListener currentListener;

    ArrayList<User> userList;

    DatabaseReference accountRef, usersRef;
    FirebaseDatabase database;
    FirebaseAuth accountAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_another_profile, container, false);

        initializeFirebaseComponent();
        initializeViews();
        updateUserList();

        updateProfileInfo();

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        name = namePerson.getText().toString();
                        User newUser = new User(name);
                        String userID = newUser.getID();
                        DatabaseReference userRef = usersRef.child(userID);
                        Map<String, Object> userMap = newUser.toMap();

                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put(userID, userMap);
                        usersRef.updateChildren(childUpdates);

                        dialog.dismiss();

                        updateUserList();
                    }
                });

                dialog.show();
            }
        });

        return view;
    }

    private void initializeViews() {
        profileImage = view.findViewById(R.id.a_profile_img);
        dialogButton = view.findViewById(R.id.a_profile_btn_person);
        userListLinearLayout = view.findViewById(R.id.userListLinearLayout);
        user = view.findViewById(R.id.a_profile_gmail);
    }

    private void initializeFirebaseComponent() {
        database = FirebaseDatabase.getInstance();
        accountAuth = FirebaseAuth.getInstance();
        accountUID = accountAuth.getCurrentUser().getUid();
        accountEmail = accountAuth.getCurrentUser().getEmail();
        accountName = accountAuth.getCurrentUser().getDisplayName();
        accountURL = accountAuth.getCurrentUser().getPhotoUrl().toString();

        userList = new ArrayList<User>();

        accountRef = database.getReference();
        usersRef = accountRef.child("accounts").child(accountUID).child("users");
    }

    private void updateProfileInfo() {
        user.setText(accountName);
        Picasso.with(view.getContext()).load(accountURL).into(profileImage);
    }

    private void updateUserList() {

        userListLinearLayout.removeAllViewsInLayout();
        if (currentListener != null) usersRef.removeEventListener(currentListener);

        currentListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot unique : dataSnapshot.getChildren()) {
                    User user = unique.getValue(User.class);
                    userList.add(user);

                    LinearLayout.LayoutParams lprams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);

                    // Create Button
                    Button btn = new Button(getActivity());
                    btn.setId(Integer.parseInt(user.getID().substring(4)));
                    btn.setText(user.getName());
                    btn.setLayoutParams(lprams);
                    btn.setAllCaps(false);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    userListLinearLayout.addView(btn);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };

        usersRef.addValueEventListener(currentListener);
    }
}
