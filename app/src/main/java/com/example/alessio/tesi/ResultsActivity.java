package com.example.alessio.tesi;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.example.alessio.tesi.Database.AppDB;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity {

    PieChart piechart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_activity);

        //istanzio il grafico a torta
        piechart = (PieChart)findViewById(R.id.subjectsPieChart);

        //eseguo query per ottenere i dati dal db da inserire el grafico a torta
        AppDB db = new AppDB(this);
        ArrayList<PieEntry> entries = db.getPieChartData();     //contiene i dati del grafico

        if(entries.size() != 0) {
            //una volta ottenuto l'ArrayList<PieEntry> con i dati, questi sono i passaggi necessari per poterli inserire
            //nel grafico (da documentazione libreria)
            PieDataSet set = new PieDataSet(entries, "Distribuzione corsi");
            set.setColors(new int[] {R.color.violet,R.color.blue,R.color.ciano,R.color.gold,R.color.green,R.color.red});
            PieData data = new PieData(set);
            piechart.setData(data);
            piechart.invalidate();
        }
        else
            Toast.makeText(this,"Nessun dato disponibile",Toast.LENGTH_SHORT).show();
    }
}
