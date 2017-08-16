package com.example.alessio.tesi;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    PieChart piechart;
    TextView mostFrequentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_activity);

        //preparo db
        AppDB db = new AppDB(this);

        //istanzio la textview per il posto più frequentato e setto il testo tramite query
        mostFrequentLocation = (TextView)findViewById(R.id.mostFrequentLocationLabel);
        mostFrequentLocation.setText(db.getMostFrequentLocation());

        //istanzio il grafico a torta e disabilito la legenda
        piechart = (PieChart)findViewById(R.id.subjectsPieChart);
        Legend legend = piechart.getLegend();
        legend.setEnabled(false);

        //eseguo query per ottenere i dati dal db da inserire el grafico a torta
        ArrayList<PieEntry> entries = db.getPieChartData();     //contiene i dati del grafico

        if(entries.size() != 0) {
            //una volta ottenuto l'ArrayList<PieEntry> con i dati, questi sono i passaggi necessari per poterli inserire
            //nel grafico (da documentazione libreria)
            Description desc = new Description();
            desc.setText("");
            piechart.setDescription(desc);
            PieDataSet set = new PieDataSet(entries, "Distribuzione corsi");
            int[] colors =  {android.R.color.holo_red_light,android.R.color.holo_green_dark,android.R.color.holo_orange_light,
                    android.R.color.holo_blue_bright,android.R.color.holo_blue_dark,android.R.color.holo_purple};
            set.setColors(colors,this);
            PieData data = new PieData(set);
            piechart.setData(data);
            piechart.invalidate();
        }
        else
            Toast.makeText(this,"Nessun dato disponibile",Toast.LENGTH_SHORT).show();
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

        switch(id){
            case R.id.menu_trophies:
                Fragment trophiesFragment = new TrophiesFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.resultsActivity, trophiesFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.menu_settings:
                break;
            case R.id.menu_results:
                break;
            case R.id.menu_calendar:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
