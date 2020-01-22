/*package com.projet.easybusiness.helper_request.;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.projet.easybusiness.helper_request.ApiListAnnonceAdapter;
import com.projet.easybusiness.recycler_view_helper.AnnonceAdapter;
import com.projet.easybusiness.recycler_view_helper.FakeData;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Brouillon extends AppCompatActivity {
    private static String annonces;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all_ad);

        // on recupere la recycle view
        RecyclerView recyclerView= findViewById(R.id.recycleView);

        // on inserer une linear view afin d'afficher les éléments sur une ligne
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // on fait un adaptateur qui va  le lien entre les objets et les vues
        //makeHttpRequest("https://ensweb.users.info.unicaen.fr/android-api/mock-api/liste.json");
        try {
            makeHttpRequest("https://ensweb.users.info.unicaen.fr/android-api/mock-api/liste.json");

            AnnonceAdapter adapter= new AnnonceAdapter(createListItem(annonces));
            recyclerView.setAdapter(adapter);
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
                Log.i("YKJ","echec de la requete");
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
        Moshi moshi= new Moshi.Builder().add(new ApiAnnonceAdapter()).build();
        JsonAdapter<String> adapter = moshi.adapter(String.class);
        Log.i("YKJ","je suis dans l'adapteur 222");
        try{
            annonces= adapter.fromJson(body);
            Log.i("YKJ", "Annonce"+annonces);//recyclerView.setAdapter(adapter);
            AnnonceAdapter finalAdapter= new AnnonceAdapter(createListItem(annonces));
            RecyclerView recyclerView= findViewById(R.id.recycleView);
            recyclerView.setAdapter(finalAdapter);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    private  ArrayList<Annonce> createListItem(String data) throws JSONException {
        ArrayList<Annonce> listeAnnonces=new ArrayList<>();
        Log.i("data",""+data);
        JSONArray listeJson= new JSONArray(data);
        Log.i("liste",""+listeJson);
        for (int i=0; i<listeJson.length();i++) {
            JSONObject ad = listeJson.getJSONObject(i);
            listeAnnonces.add(new Annonce(ad.getString("id"),
                    ad.getString("titre"),
                    ad.getString("description"),
                    (float) ad.getDouble("prix"),
                    ad.getString("pseudo"),
                    ad.getString("emailContact"),
                    ad.getString("telContact"),
                    ad.getString("ville"),
                    ad.getString("cp"),
                    (String[]) ad.get("image"),
                    ad.getLong("date")
            ));
            Log.i("test",i+"");
        }
        return listeAnnonces;
    }
}*/