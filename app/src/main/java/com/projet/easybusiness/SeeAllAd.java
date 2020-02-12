package com.projet.easybusiness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.projet.easybusiness.helper_request.ApiListAnnonceAdapter;
import com.projet.easybusiness.helper_request.HelperClass;
import com.projet.easybusiness.helper_request.ResponseAnnonces;
import com.projet.easybusiness.recycler_view_helper.AnnonceAdapter;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class SeeAllAd extends AppCompatActivity {
     private Map<String,Annonce> annonces=new HashMap<>();
    private ArrayList<Annonce> listAnnonce;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all_ad);

        // on recupere la recycle view


        // on fait un adaptateur qui va  le lien entre les objets et les vues
        try {
            makeHttpRequest("https://ensweb.users.info.unicaen.fr/android-api/mock-api/liste.json");

        } catch (Exception e) {
            e.printStackTrace();
        }


        // chargement des élément de la vue avec un adapteur

    }

    public void makeHttpRequest(String url){
        OkHttpClient client = new OkHttpClient();
        Request req=  new Request.Builder().url(url).build();
        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try(ResponseBody responseBody=  response.body()){

                    if(!response.isSuccessful()){
                        throw  new IOException("La requete n'a pas aboutit");
                    }

                    Log.i("YKJ","la reponse m'a été remise");
                    final String body = responseBody.string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            parseAds(body);
                        }
                    });
                }
            }
        });
    }

    public void parseAds(String body){
        // declaration de l'instance de moshi qui va gerer le parsing
            Moshi moshi= new Moshi.Builder().add(new ApiListAnnonceAdapter()).build();
            JsonAdapter<ResponseAnnonces> adapter = moshi.adapter(ResponseAnnonces.class);
        Log.i("YKJ","je suis dans l'adapteur 222");
            try{
                 ResponseAnnonces response = adapter.fromJson(body);
                 listAnnonce=response.getAnnonces();
                 AnnonceAdapter adapterListAnnonce= new AnnonceAdapter(listAnnonce);
                 fillMap();
                RecyclerView recyclerView= findViewById(R.id.recycleView);

                // on inserer une linear view afin d'afficher les éléments sur une ligne
                recyclerView.setLayoutManager(new LinearLayoutManager(this));

                recyclerView.setAdapter(adapterListAnnonce);

            }catch (Exception e) {
                e.printStackTrace();
            }

    }

    public void itemClick(View v){
        String titreClick;
        TextView clicked = (TextView)v.findViewById(R.id.idAnnonce);
        titreClick=clicked.getText().toString();

        Intent next= new Intent(this,SeeAd.class);
        next.putExtra("idAnnonce", annonces.get(titreClick));

        startActivity(next);
    }


    public void fillMap(){
        for(int i=0; i<this.listAnnonce.size() ;i++){
            Annonce ad= this.listAnnonce.get(i);
            ad.setImage(HelperClass.changeToPng(ad.getImages()));
            annonces.put(ad.getId(),ad);
        }
    }
}