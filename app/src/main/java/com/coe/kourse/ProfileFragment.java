package com.coe.kourse;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.data.remote.EmailSignInHandler;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment implements View.OnClickListener{

    TextView curName;
    Button logOutbtn;
    ImageView profileImg;
    GoogleSignInAccount googleSignInAccount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        profileImg = view.findViewById(R.id.profile_img);
        curName = (TextView)view.findViewById(R.id.curName);
        logOutbtn = (Button)view.findViewById(R.id.logOutbtn);

        googleSignInAccount = GoogleSignIn.getLastSignedInAccount(view.getContext());
        if(googleSignInAccount != null) {
            curName.setText(googleSignInAccount.getDisplayName());
            Picasso.with(view.getContext()).load(googleSignInAccount.getPhotoUrl().toString()).into(profileImg);
        }

        logOutbtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);

    }
}
