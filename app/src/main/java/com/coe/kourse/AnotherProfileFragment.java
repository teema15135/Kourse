package com.coe.kourse;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
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

public class AnotherProfileFragment extends Fragment implements PopupMenu.OnMenuItemClickListener, AdapterView.OnItemClickListener {

    String TAG = "AnotherProfile";

    int selectUsernamePosition = -1;

    View view;

    CircleImageView profileImage;
    Button dialogButton;
    ImageView logoutImage;
    TextView user, logoutTextView;
    String name;
    ListView listView;

    String accountEmail, accountUID, accountName, accountURL;

    ValueEventListener currentListener;

    ArrayList<User> userList;
    ArrayList<String> nameList;

    DatabaseReference accountRef, usersRef;
    FirebaseDatabase database;
    FirebaseAuth accountAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_another_profile, container, false);

        initializeFirebaseComponent();
        initializeViews();
        addViewListeners();
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
        listView.setOnItemClickListener(this);

        return view;
    }

    private void initializeViews() {
        profileImage = view.findViewById(R.id.a_profile_img);
        logoutImage = view.findViewById(R.id.logoutImageButton);
        logoutTextView = view.findViewById(R.id.logoutTextView);
        dialogButton = view.findViewById(R.id.a_profile_btn_person);
        user = view.findViewById(R.id.a_profile_gmail);
        listView = view.findViewById(R.id.userListView);
    }

    private void initializeFirebaseComponent() {
        database = FirebaseDatabase.getInstance();
        accountAuth = FirebaseAuth.getInstance();
        accountUID = accountAuth.getCurrentUser().getUid();
        accountEmail = accountAuth.getCurrentUser().getEmail();
        accountName = accountAuth.getCurrentUser().getDisplayName();
        accountURL = accountAuth.getCurrentUser().getPhotoUrl().toString();

        userList = new ArrayList<User>();
        nameList = new ArrayList<String>();

        accountRef = database.getReference();
        usersRef = accountRef.child("accounts").child(accountUID).child("users");
    }

    private void addViewListeners() {
        View.OnClickListener logoutListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog confirmDialog = new Dialog(getContext());
                confirmDialog.setContentView(R.layout.dialog_confirm);

                Button okButton = (Button) confirmDialog.findViewById(R.id.okConfirm);
                Button cancelButton = (Button) confirmDialog.findViewById(R.id.cancelConfirm);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        accountAuth.signOut();
                        confirmDialog.dismiss();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
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
        };
        logoutTextView.setOnClickListener(logoutListener);
        logoutImage.setOnClickListener(logoutListener);
    }

    private void updateProfileInfo() {
        user.setText(accountName);
        Picasso.with(view.getContext()).load(accountURL).into(profileImage);
    }

    private void updateUserList() {
        if (currentListener != null) usersRef.removeEventListener(currentListener);

        currentListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList = new ArrayList<>();
                nameList = new ArrayList<>();
                for (DataSnapshot unique : dataSnapshot.getChildren()) {
                    User user = unique.getValue(User.class);
                    userList.add(user);
                    nameList.add(user.getName());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, nameList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };

        usersRef.addValueEventListener(currentListener);

    }

    private void renameUser() {
        User selectUser = userList.get(selectUsernamePosition);

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
                String userID = selectUser.getID();
                DatabaseReference userRef = usersRef.child(userID);

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("name", name);
                userRef.updateChildren(childUpdates);

                dialog.dismiss();

                updateUserList();
            }
        });

        dialog.show();
    }

    private void deleteUser() {
        User selectUser = userList.get(selectUsernamePosition);
        Dialog confirmDialog = new Dialog(getContext());
        confirmDialog.setContentView(R.layout.dialog_confirm);

        Button okButton = (Button) confirmDialog.findViewById(R.id.okConfirm);
        Button cancelButton = (Button) confirmDialog.findViewById(R.id.cancelConfirm);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put(selectUser.getID(), null);
                usersRef.updateChildren(childUpdates);
                confirmDialog.dismiss();
                updateUserList();
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

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.renameUsername:
                renameUser();
                return true;
            case R.id.deleteUsername:
                deleteUser();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, Integer.toString(position));
        PopupMenu popup = new PopupMenu(getContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_username_manage, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        selectUsernamePosition = position;
        popup.show();
    }
}
