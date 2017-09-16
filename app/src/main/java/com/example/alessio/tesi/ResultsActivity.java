package com.example.alessio.tesi;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.alessio.tesi.Database.AppDB;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity {

    PieChart subjectsPiechart;
    PieChart typesPiechart;
    TextView mostFrequentLocation,mostFrequentLocationLabel;
    TextView totalTime;
    TextView advice;
    EditText searchUser;
    Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_activity);

        //preparo db
        AppDB db = new AppDB(this);

        //istanzio la edittext e il button per cercare i risultati di un utente
        searchUser = (EditText)findViewById(R.id.search_field);
        searchButton = (Button)findViewById(R.id.search_user_button);

        //istanzio le textview e ne setto il testo tramite query
        mostFrequentLocation = (TextView)findViewById(R.id.mostFrequentLocationTextView);
        mostFrequentLocationLabel = (TextView)findViewById(R.id.mostFrequentLocationLabel);
        String place = db.getMostFrequentLocation();
        if(place != null)
            mostFrequentLocationLabel.setText(place);

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

            PieDataSet subjSet = new PieDataSet(subjEntries, this.getResources().getString(R.string.courseDistribution));
            subjSet.setColors(colors,this);
            PieData subjData = new PieData(subjSet);
            subjectsPiechart.setData(subjData);
            subjectsPiechart.invalidate();

            PieDataSet typeSet = new PieDataSet(typesEntries, this.getResources().getString(R.string.typeDistribution));
            typeSet.setColors(colors,this);
            PieData typesData = new PieData(typeSet);
            typesPiechart.setData(typesData);
            typesPiechart.invalidate();
        }
        else
            Toast.makeText(this,this.getResources().getString(R.string.noData),Toast.LENGTH_SHORT).show();

        // RICERCA DI UN UTENTE
        //richiesta al server e risposta
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareRequest("pieData.php");
            }
        });

        // SBLOCCO TROFEO 14
        if(db.getTrophies()[13].getUnlocked() == 0){
            db.unlockTrophy(14);
            Toast t = Toast.makeText(getApplicationContext(), getApplication().getResources().getString(R.string.unlockTrophy)+"14",Toast.LENGTH_LONG);
            t.show();
        }
    }

    //Menu
    //IN QUESTA ACTIVITY EVITO CHE IL MENU SIA UTILIZZABILE
    //by Alessio: ho pensato che fosse una buona idea per evitare che l'utente potesse aprire activity su activity troppe volte
    //TODO se si decide di fare così dovremmo considerare l'idea di farlo anche nei fragment dei trofei e delle opzioni

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
        }

        return super.onOptionsItemSelected(item);
    }

    private String chooseAdvice(float[] infoForAdvice) {
        String advice = new String("");
        //considero la distanza temporale media tra le sessioni
        if(infoForAdvice[2] <= 2)                                   //distanza media di 2 giorni o meno
            advice = this.getResources().getString(R.string.advice_2_0);
        else
            advice = this.getResources().getString(R.string.advice_2_1);

        //considero la media dei minuti di studio al giorno
        if(infoForAdvice[1] <= 60)
            advice += this.getResources().getString(R.string.advice_1_0);
        else if(infoForAdvice[1] >= 240)
            advice += this.getResources().getString(R.string.advice_1_1);
        else
            advice += this.getResources().getString(R.string.advice_1_2);

        //considero la media del numero di corsi diversi studiati negli ultimi 10 giorni
        if(infoForAdvice[0] <= 2)
            advice += this.getResources().getString(R.string.advice_0_0);
        else if(infoForAdvice[0] >= 5)
            advice += this.getResources().getString(R.string.advice_0_1);
        else
            advice += this.getResources().getString(R.string.advice_0_2);

        return advice;
    }

    private boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    //metodo unico per preparare la richiesta e il json con i dati in output
    private void prepareRequest(String filename) {
        String username = searchUser.getText().toString();
        //INVIO DATI AL SERVER
        //invio username e password al server che mi risponde dicendomi se c'è stato un errore o è andata bene
        //vedo se sono connesso
        if(isConnected()) {
            try {
                //1) costruisco il json
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("user",username);
                String json = jsonObject.toString();
                //2) preparo la richiesta e la invio
                GetData reg = new GetData(new RequestHandler(json, filename));
                reg.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
            Toast.makeText(this,this.getResources().getString(R.string.noConnection),Toast.LENGTH_LONG).show();
    }

    //classe per eseguire in background la richiesta per ottenere i dati di un utente
    private class GetData extends AsyncTask<Void,Void,Void> {
        private RequestHandler rh;
        private JSONObject response;

        GetData(RequestHandler rh) {
            this.rh = rh;
        }

        @Override
        protected Void doInBackground(Void... params) {
            response = rh.makeRequest();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            String message;
            try {
                if (response != null)
                    message = response.getString("message");
                else
                    message = "error";
                //3) interpretazione della risposta
                //se risposta positiva
                if(message.equals("ok")) {
                    int totalDuration = response.getInt("total_time");
                    ArrayList<PieEntry> subj = new ArrayList<>();
                    ArrayList<PieEntry> types = new ArrayList<>();
                    totalTime.setText(String.valueOf(totalDuration));
                    JSONArray courses = response.getJSONArray("courses");
                    JSONObject typePerc = response.getJSONObject("percents");
                    //prelevo i dati sui corsi dal json e calcolo le percentuali, quindi preparo le strutture dati per inserirli
                    for(int i=0;i<courses.length();i++) {
                        JSONObject jo = courses.getJSONObject(i);
                        String cName = jo.getString("course");
                        double cTime = jo.getDouble("time");
                        PieEntry pe = new PieEntry((float)cTime*100/(float)totalDuration,cName);
                        subj.add(pe);
                    }
                    //prelevo le percentuali dal json e le inserisco nelle strutture dati
                    PieEntry th = new PieEntry((float)typePerc.getDouble("th"),"theory");
                    PieEntry ex = new PieEntry((float)typePerc.getDouble("ex"),"exercise");
                    PieEntry pr = new PieEntry((float)typePerc.getDouble("pr"),"project");
                    types.add(th);types.add(ex);types.add(pr);
                    //setto i dati e le proprietà grafiche dei grafici
                    //colori e descrizione
                    int[] colors =  {android.R.color.holo_red_light,android.R.color.holo_green_dark,android.R.color.holo_orange_light,
                            android.R.color.holo_blue_bright,android.R.color.holo_blue_dark,android.R.color.holo_purple};
                    Description desc = new Description();
                    desc.setText("");
                    //grafico dei corsi
                    PieDataSet subjSet = new PieDataSet(subj, ResultsActivity.this.getResources().getString(R.string.courseDistribution));
                    subjSet.setColors(colors,ResultsActivity.this);
                    PieData subjData = new PieData(subjSet);
                    subjectsPiechart.setData(subjData);
                    subjectsPiechart.setDescription(desc);
                    subjectsPiechart.invalidate();
                    //grafico dei tipi
                    PieDataSet typeSet = new PieDataSet(types, ResultsActivity.this.getResources().getString(R.string.typeDistribution));
                    typeSet.setColors(colors,ResultsActivity.this);
                    PieData typeData = new PieData(typeSet);
                    typesPiechart.setData(typeData);
                    typesPiechart.setDescription(desc);
                    typesPiechart.invalidate();
                    //tolgo i consigli e il luogo più frequentato
                    advice.setVisibility(View.INVISIBLE);
                    mostFrequentLocation.setVisibility(View.INVISIBLE);
                    mostFrequentLocationLabel.setVisibility(View.INVISIBLE);
                }
                else if(message.equals("wrong"))
                    Toast.makeText(ResultsActivity.this, ResultsActivity.this.getResources().getString(R.string.unknownUser), Toast.LENGTH_SHORT).show();
                else if(message.equals("error"))
                    Toast.makeText(ResultsActivity.this, ResultsActivity.this.getResources().getString(R.string.unknownErr), Toast.LENGTH_LONG).show();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
