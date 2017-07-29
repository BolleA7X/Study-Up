package com.example.alessio.tesi;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.alessio.tesi.Database.AppDB;
import com.example.alessio.tesi.Database.Trophy;

import java.util.zip.Inflater;

import static java.util.zip.Inflater.*;

/**
 * Created by Fabio on 23/06/2017.
 */

public class TrophiesFragment extends Fragment {

    private ImageView trophyImage;
    private int[] trophyIds;
    private Trophy[] trophyData;

    @Override
    public void onCreate(Bundle savedInstanceState){
        trophyIds = new int[] {R.id.t1,R.id.t2,R.id.t3,R.id.t4,R.id.t5,R.id.t6,R.id.t7,R.id.t8,R.id.t9,R.id.t10,
                               R.id.t11,R.id.t12,R.id.t13,R.id.t14,R.id.t15,R.id.t16,R.id.t17,R.id.t18,R.id.t19,R.id.t20};

        AppDB db = new AppDB(getActivity());
        trophyData = db.getTrophies();

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.trophies_fragment, container, false);

        for(int i=0;i<20;i++) {
            trophyImage = (ImageView)view.findViewById(trophyIds[i]);
            if(trophyData[i].getUnlocked() == 0)
                trophyImage.setImageResource(R.drawable.trophy_unlocked);
            else {
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
}
