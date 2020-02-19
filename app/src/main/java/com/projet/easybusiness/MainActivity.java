package com.projet.easybusiness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void next(View view) {
        Intent next= new Intent(this,SeeAd.class);
        startActivity(next);
    }

    public void seeAllAd(View v){
        Intent view =  new Intent(this,SeeAllAd.class);
        startActivity(view);
    }

    public void fillProfile(View v){
        Intent view =  new Intent(this,UserInformation.class);
        startActivity(view);
    }

    public void listeAdSaved(View v) {
        /*AnnonceDb annonceDb = new AnnonceDb(v.getContext());
        Cursor resultat = annonceDb.listeAnnoncesSauvegardees();
        Log.i("ttt ", "Liste des annonces sauvegardées: " + resultat.toString());
        */
        Intent view = new Intent(this,ListeDesAnnoncesSauvegargees.class);
        startActivity(view);
    }

    public void depotAnnonce(View v){
        Intent view =  new Intent(this,DepotAnnonce.class);
        startActivity(view);

    }

    public void seeMyAds(View v) {
        Intent view =  new Intent(this,SeeAllMyAds.class);
        startActivity(view);
    }
}
