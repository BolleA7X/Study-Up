package com.example.alessio.tesi;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.example.alessio.tesi.Database.AppDB;
import com.example.alessio.tesi.Database.Trophy;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2Fragment;


public class IntroActivity extends AppIntro {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Se voglio usare slide custom in dei fragments
        //addSlide(firstFragment);

        // Se invece uso le slide di default
        addSlide(AppIntro2Fragment.newInstance(getString(R.string.title_intro_1), getString(R.string.description_intro_1), R.drawable.icon_yellow, Color.parseColor("#3F51B5")));
        addSlide(AppIntro2Fragment.newInstance(getString(R.string.title_intro_2), getString(R.string.description_intro_2), R.drawable.pom_custom_icon, Color.parseColor("#3F51B5")));
        addSlide(AppIntro2Fragment.newInstance(getString(R.string.title_intro_3), getString(R.string.description_intro_3), R.drawable.graphic_icon, Color.parseColor("#3F51B5")));

        // METODI ADDIZIONALI
        // Override bar/separator color.
        setBarColor(Color.parseColor("#3F51B5"));
        setSeparatorColor(Color.parseColor("#2196F3"));

        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);

        setFadeAnimation();
        askForPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WAKE_LOCK }, 3);

        // Turn vibration on and set intensity.
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
        // SBLOCCO TROFEO 1
        // Eseguo solo la prima volta che apro l'app
        AppDB db = new AppDB(this);
        Trophy[] trophies = db.getTrophies();
        if(trophies[0].getUnlocked() == 0){
            db.unlockTrophy(1);
            Toast t = Toast.makeText(this, this.getResources().getString(R.string.unlockTrophy)+ " " + getResources().getString(R.string.trophy_title_01),Toast.LENGTH_LONG);
            t.show();
        }
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