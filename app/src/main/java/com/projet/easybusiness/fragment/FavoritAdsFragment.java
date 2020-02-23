package com.projet.easybusiness.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projet.easybusiness.Annonce;
import com.projet.easybusiness.R;
import com.projet.easybusiness.helper_request.ApiListAnnonceAdapter;
import com.projet.easybusiness.helper_request.ResponseAnnonces;
import com.projet.easybusiness.recycler_view_helper.AnnonceAdapter;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FavoritAdsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FavoritAdsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoritAdsFragment extends AllAdsFragment {
    public void parseAds(String body){
        // declaration de l'instance de moshi qui va gerer le parsing
        Moshi moshi= new Moshi.Builder().add(new ApiListAnnonceAdapter()).build();
        JsonAdapter<ResponseAnnonces> adapter = moshi.adapter(ResponseAnnonces.class);
        Log.i("YKJ","je suis dans l'adapteur 222");
        try{
            ResponseAnnonces response = adapter.fromJson(body);
            listAnnonce=response.getAnnonces();
            filter(listAnnonce);
            AnnonceAdapter adapterListAnnonce= new AnnonceAdapter(listAnnonce);
            fillMap();

        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void filter(ArrayList<Annonce> listeA){
        SharedPreferences preferences = getActivity().getSharedPreferences("PREF", MODE_PRIVATE);
        ArrayList<Annonce> list= new ArrayList<>();
        for (int i=listeA.size()-1 ; i>=0; i--){
            Annonce ad = listeA.get(i);
            if(ad.getPseudo().equals(preferences.getString("pseudo",""))){
                Log.i("YKJ", i+" On est ici");
                if(ad.getEmailContact().equals(preferences.getString("email",""))){
                    list.add(ad);
                    Log.i("YKJ", i+" inserer");
                }
            }
            Log.i("YKJ", i+""+ preferences.getString("pseudo","")+"=="+ad.getPseudo());

        }
        listAnnonce= new ArrayList<>(list);
    }
}
