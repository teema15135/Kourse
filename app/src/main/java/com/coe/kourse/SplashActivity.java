package com.coe.kourse;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import pl.droidsonroids.gif.GifImageView;

public class SplashActivity extends AppCompatActivity {

    GifImageView splashGif;

    @Override
    protected void onCreate(Bundle savedInsantanceState) {
        super.onCreate(savedInsantanceState);
        setContentView(R.layout.activity_splash);

        splashGif = (GifImageView)findViewById(R.id.splashGif);

        startFadeInSplash();
    }

    public void startFadeInSplash() {
        Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_animation_splash);
        splashGif.startAnimation(startAnimation);
    }
}