package com.example.alessio.tesi;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.example.alessio.tesi.Database.AppDB;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity {

    PieChart subjectsPiechart;
    PieChart typesPiechart;
    TextView mostFrequentLocation;
    TextView totalTime;
    TextView advice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_activity);

        //preparo db
        AppDB db = new AppDB(this);

        //istanzio le textview e ne setto il testo tramite query
        mostFrequentLocation = (TextView)findViewById(R.id.mostFrequentLocationLabel);
        String place = db.getMostFrequentLocation();
        if(place != null)
            mostFrequentLocation.setText(place);

        totalTime = (TextView)findViewById(R.id.totalTimeLabel);
        int time = db.getTotalTime();
        totalTime.setText(String.valueOf(time)+" minuti");

        advice = (TextView)findViewById(R.id.advice);
        float[] infoForAdvice = db.getInfoForAdvice();              //informazioni recuperate dal db per il consiglio
        //chiamo un metodo che generi a runtime la stringa del consiglio sulla base delle informazioni recuperate
        //dal db
        advice.setText(chooseAdvice(infoForAdvice));

        //istanzio i grafici a torta e disabilito la legenda
        subjectsPiechart = (PieChart)findViewById(R.id.subjectsPieChart);
        typesPiechart = (PieChart)findViewById(R.id.typesPieChart);
        Legend legend = subjectsPiechart.getLegend();
        legend.setEnabled(false);
        legend = typesPiechart.getLegend();
        legend.setEnabled(false);

        //eseguo query per ottenere i dati dal db da inserire el grafico a torta
        ArrayList<PieEntry> subjEntries = db.getSubjectsPieChartData();
        ArrayList<PieEntry> typesEntries = db.getTypesPieChartData();

        if(subjEntries.size() != 0 && typesEntries.size() != 0) {
            //una volta ottenuto l'ArrayList<PieEntry> con i dati, questi sono i passaggi necessari per poterli inserire
            //nel grafico (da documentazione libreria)
            Description desc = new Description();
            desc.setText("");
            subjectsPiechart.setDescription(desc);
            typesPiechart.setDescription(desc);

            int[] colors =  {android.R.color.holo_red_light,android.R.color.holo_green_dark,android.R.color.holo_orange_light,
                    android.R.color.holo_blue_bright,android.R.color.holo_blue_dark,android.R.color.holo_purple};

            PieDataSet subjSet = new PieDataSet(subjEntries, "Distribuzione corsi");
            subjSet.setColors(colors,this);
            PieData subjData = new PieData(subjSet);
            subjectsPiechart.setData(subjData);
            subjectsPiechart.invalidate();

            PieDataSet typeSet = new PieDataSet(typesEntries, "Distribuzione tipi di sessione");
            typeSet.setColors(colors,this);
            PieData typesData = new PieData(typeSet);
            typesPiechart.setData(typesData);
            typesPiechart.invalidate();
        }
        else
            Toast.makeText(this,"Nessun dato disponibile",Toast.LENGTH_SHORT).show();

        // SBLOCCO TROFEO 14
        if(db.getTrophies()[13].getUnlocked() == 0){
            db.unlockTrophy(14);
            Toast t = Toast.makeText(getApplicationContext(), "CONGRATULATIONS: TROPHY 14 UNLOCKED.",Toast.LENGTH_LONG);
            t.show();
        }
    }

    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Fragment fragment;
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;

        switch(id){
            case R.id.menu_trophies:
                fragment = new TrophiesFragment();
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.resultsActivity, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.menu_settings:
                fragment = new SettingsFragment();
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.resultsActivity, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.menu_results:
                Intent intent = new Intent(this,ResultsActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_calendar:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private String chooseAdvice(float[] infoForAdvice) {
        String advice = new String("");
        //considero la distanza temporale media tra le sessioni
        if(infoForAdvice[2] <= 2)                                   //distanza media di 2 giorni o meno
            advice = "Stai studiando con costanza. ";
        else
            advice = "Dovresti studiare con maggiore frequenza. ";

        //considero la media dei minuti di studio al giorno
        if(infoForAdvice[1] <= 60)
            advice += "Sarebbe meglio studiare per più tempo al giorno. ";
        else if(infoForAdvice[1] >= 240)
            advice += "Rischi di affaticarti e stressarti se studi per troppo tempo al giorno. ";
        else
            advice += "Studi il corretto numero di ore al giorno: continua così. ";

        //considero la media del numero di corsi diversi studiati negli ultimi 10 giorni
        if(infoForAdvice[0] <= 2)
            advice += "Negli ultimi dieci giorni hai studiato poche materie diverse al giorno.";
        else if(infoForAdvice[0] >= 5)
            advice += "Dovresti concentrarti su meno materie alla volta";
        else
            advice += "Negli ultimi dieci giorni hai studiato il corretto numero di materie diverse al giorno.";

        return advice;
    }
}
