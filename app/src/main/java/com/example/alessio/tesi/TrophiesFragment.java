package com.example.alessio.tesi;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.alessio.tesi.Database.AppDB;
import com.example.alessio.tesi.Database.Trophy;

public class TrophiesFragment extends Fragment {

    private ImageView trophyImage;
    private int[] trophyIds;
    private Trophy[] trophyData;

    @Override
    public void onCreate(Bundle savedInstanceState){
        //questo array tiene gli id di tutte le ImageView dei trofei. Serve per poter iterare.
        trophyIds = new int[] {R.id.t1,R.id.t2,R.id.t3,R.id.t4,R.id.t5,R.id.t6,R.id.t7,R.id.t8,R.id.t9,R.id.t10,
                               R.id.t11,R.id.t12,R.id.t13,R.id.t14,R.id.t15,R.id.t16,R.id.t17,R.id.t18,R.id.t19,R.id.t20};

        //ottengo i dati sui trofei
        AppDB db = new AppDB(getActivity());
        trophyData = db.getTrophies();

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.trophies_fragment, container, false);

        //itero sui 20 trofei
        for(int i=0;i<20;i++) {
            //per ognuno accedo alla relativa ImageView
            trophyImage = (ImageView)view.findViewById(trophyIds[i]);
            //qua scelgo l'icona che deve avere
            //se è bloccato setto alla ImageView l'icona del trofeo bloccato
            if(trophyData[i].getUnlocked() == 0)
                trophyImage.setImageResource(R.drawable.trophy_locked);
            else {
                //se è sbloccato
                //setto l'icona del colore giusto in base al trofeo
                if(trophyData[i].getColor().equals(Trophy.BRONZE))
                    trophyImage.setImageResource(R.drawable.trophy_bronze);
                else if(trophyData[i].getColor().equals(Trophy.SILVER))
                    trophyImage.setImageResource(R.drawable.trophy_silver);
                else if(trophyData[i].getColor().equals(Trophy.GOLD))
                    trophyImage.setImageResource(R.drawable.trophy_gold);
                else if(trophyData[i].getColor().equals(Trophy.PLATINUM))
                    trophyImage.setImageResource(R.drawable.trophy_platinum);
            }
        }

        return view;
    }

    //se sono in questo fragment disabilito il relativo item del menu
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_trophies).setEnabled(false);
    }
}
