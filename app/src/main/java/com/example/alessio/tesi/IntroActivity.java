package com.example.alessio.tesi;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2Fragment;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Note here that we DO NOT use setContentView();

        // Slide custom
        /*addSlide(firstFragment);
        addSlide(secondFragment);
        addSlide(thirdFragment);
        addSlide(fourthFragment);*/

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
        addSlide(AppIntro2Fragment.newInstance(getString(R.string.title_intro_1), getString(R.string.description_intro_1), R.drawable.icon_yellow, Color.parseColor("#3F51B5")));
        addSlide(AppIntro2Fragment.newInstance(getString(R.string.title_intro_2), getString(R.string.description_intro_2), R.drawable.pom_custom_icon, Color.parseColor("#3F51B5")));
        addSlide(AppIntro2Fragment.newInstance(getString(R.string.title_intro_3), getString(R.string.description_intro_3), R.drawable.graphic_icon, Color.parseColor("#3F51B5")));
        // OPTIONAL METHODS
        // Override bar/separator color.
        setBarColor(Color.parseColor("#3F51B5"));
        setSeparatorColor(Color.parseColor("#2196F3"));

        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);

        setFadeAnimation();
        //askForPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 3);

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permission in Manifest.
        setVibrate(true);
        setVibrateIntensity(30);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        launchMain();
    }
    //Quando finisci tutte le slide
    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        launchMain();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
    private void launchMain(){
        Intent i = new Intent();
        i.putExtra("Opening","firstOpening");
        setResult(Activity.RESULT_OK,i);
        finish();
    }

}