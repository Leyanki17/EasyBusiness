package com.projet.easybusiness.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projet.easybusiness.Annonce;
import com.projet.easybusiness.AnnonceDb;
import com.projet.easybusiness.R;
import com.projet.easybusiness.helper_request.ApiListAnnonceAdapter;
import com.projet.easybusiness.helper_request.ResponseAnnonces;
import com.projet.easybusiness.recycler_view_helper.AnnonceAdapter;
import com.projet.easybusiness.recycler_view_helper.RecyclerViewCursorAdapter;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class FavoritAdsFragment extends Fragment {
    private AnnonceDb database;
    private RecyclerViewCursorAdapter cAdapter;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_favorit_ads, container, false);
        database= new AnnonceDb(getContext());

        RecyclerView recyclerView= v.findViewById(R.id.AdList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cAdapter=  new RecyclerViewCursorAdapter(getContext(),database.listeAnnoncesSauvegardees());

        recyclerView.setAdapter(cAdapter);
        return v;
    }

}
