package com.coe.kourse;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileFragment extends Fragment {

    Button dialogButton;
    TextView user;
    String name;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        dialogButton = view.findViewById(R.id.profile_btn_person);
        user = view.findViewById(R.id.user1);

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
                        user.setText(""+ name);
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        return view;
    }
}
