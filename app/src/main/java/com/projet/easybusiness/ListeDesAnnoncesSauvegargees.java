package com.projet.easybusiness;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Adapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.projet.easybusiness.recycler_view_helper.RecyclerViewCursorAdapter;

public class ListeDesAnnoncesSauvegargees extends AppCompatActivity {
    private AnnonceDb database;
    private   RecyclerViewCursorAdapter cAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_des_annonces_sauvegardees);

        database= new AnnonceDb(this);

        RecyclerView recyclerView= findViewById(R.id.AdList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cAdapter=  new RecyclerViewCursorAdapter(this,database.listeAnnoncesSauvegardees());

        recyclerView.setAdapter(cAdapter);

    }

    /*public void listeDesAnncesSauvegardees(){


    }*/

}
