package com.projet.easybusiness;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;

import android.database.Cursor;

import android.content.SharedPreferences;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.projet.easybusiness.helper_request.HelperClass;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class SeeAd extends AppCompatActivity {
    Annonce ad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_ad);
       // makeHttpRequest("https://ensweb.users.info.unicaen.fr/android-api/mock-api/liste.json");

        Intent intent= getIntent();

        this.ad= intent.getParcelableExtra("idAnnonce");
        if(ad!=null){
            Log.i("annonceId",ad.getId());
            rempliAnnonce(this.ad);
            Log.i("annonceId",ad.getId());
        }else{
            //makeHttpRequest("https://ensweb.users.info.unicaen.fr/android-api/mock-api/completeAdWithImages.json");
            makeHttpRequest("https://ensweb.users.info.unicaen.fr/android-api/?apikey=21912873&method=details&id=5e47df98b4f45");
        }
    }

    public void okhttp(View View){
        makeHttpRequest("https://ensweb.users.info.unicaen.fr/android-api/mock-api/completeAdWithImages.json");
    }

    public void okhttp404(View view){
        makeHttpRequest("https://ensweb.users.info.unicaen.fr/android-api/mock-api/erreur.json");

    }

    public void parseAd(String body){
        Moshi moshi= new Moshi.Builder().add(new ApiAnnonceAdapter()).build();
        JsonAdapter<Annonce> jsonAdapter = moshi.adapter(Annonce.class);
        try{
            this.ad= jsonAdapter.fromJson(body);
            rempliAnnonce(ad);
        }catch(IOException e){
            Log.i("YKJ",e.getMessage());
        }

    }

    private void makeHttpRequest(String url){
        OkHttpClient client = new OkHttpClient();
        Request req= new Request.Builder()
                .url(url)
                .build();
        client.newCall(req).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // on recupere le corps de la reponce
                try(ResponseBody responseBody =response.body()){
                    // si la requete n'a pas reussi
                    if(!response.isSuccessful()){
                        // afficher un message d'erreur
                        throw new IOException("Unexpected HTTP Code "+response);
                    }
                    final String adBody= responseBody.string();
                    runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    parseAd(adBody);
                                }
                            }
                    );
                }
            }
        });
    }
    /*
     * Permet de remplir une annonce
     * @param Ad qui correspond à une annonce
     */
    public void rempliAnnonce(Annonce ad){
        TextView titre = ((TextView) findViewById(R.id.titre));
        TextView prix = (TextView) findViewById(R.id.prix);
        TextView date = (TextView) findViewById(R.id.date);
        TextView proprietaire =  (TextView) findViewById(R.id.proprietaire);
        TextView email = (TextView) findViewById(R.id.email);
        TextView telephone= (TextView) findViewById(R.id.tel);
        TextView adresse = (TextView) findViewById(R.id.adresse);
        TextView description= (TextView) findViewById(R.id.description);

        TextView slideNumber= (TextView) findViewById(R.id.slideNumber);

        Log.i ("YKJ", "l'image de "+ ad.getPseudo() +" est " +ad.getImages()[0]);
        ViewPager slider= findViewById(R.id.slide);
        SliderAdapter sliderAdapter=new SliderAdapter(this,ad.getImages(),slideNumber);
        slider.setAdapter(sliderAdapter);

        titre.setText(ad.getTitre());
        prix.setText(" "+ad.getPrix()+" $");
        proprietaire.setText(ad.getPseudo());
        email.setText(ad.getEmailContact());
        telephone.setText(ad.getTelContact());
        adresse.setText(ad.getAdresse());
        description.setText(ad.getDescription());
        date.setText(" "+ HelperClass.formatDate(ad.getDate()));
        Log.i("YKJ", "logo fin");
    }

    public void callPers(View v){
        TextView telephone= (TextView) findViewById(R.id.tel);
        startActivity(new Intent(Intent.ACTION_DIAL,Uri.parse("tel: "+telephone.getText().toString())));
    }

    public void sendEmail(View v){
        TextView mail= (TextView) findViewById(R.id.email);
        SharedPreferences preferences=  getSharedPreferences("PREF",MODE_PRIVATE);
        Log.i("YKJ",preferences.getString("pseudo","inconnu"));
        Log.i("YKJ",preferences.getString("email", "inconnu"));
        Log.i("YKJ", preferences.getString("tel","pas de numero"));
        startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("mailto: "+ mail.getText().toString())));
    }

    public void saveAd(View v){
        AnnonceDb annonceDb = new AnnonceDb(this);

        Log.i("xxxx", "on va enregistrer");
        try {
           long res =  annonceDb.ajouter(this.ad);
           Log.i("xxxx","Dans le try : " + res);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("xxxx","Erreur d'insertion : ");
        }

        Log.i("xxxx","Après insertion : ");

    }

    //requete de suppression
    OkHttpClient clientSuppression = new OkHttpClient();

    public void suppressionId(String urlSuppression)  {

            Request requestSuppression = new Request.Builder()
                    .url(urlSuppression)
                    .build();
            clientSuppression.newCall(requestSuppression).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try (ResponseBody responseBody = response.body()) {
                        Log.i("try","on est dans le try");
                        if (!response.isSuccessful())
                            throw new IOException("Unexpected code " + response);
                        System.out.println(responseBody.string());
                    }catch(IOException e)
                    {
                       Log.i("catch","on est dans le catch");
                    }
                }
            });
    }

    public void supprimer(View view) {
        SharedPreferences preferences=  getSharedPreferences("PREF",MODE_PRIVATE);


        if(view.getId()==R.id.supprimer && preferences.getString("email","inconnu").equalsIgnoreCase(ad.getEmailContact())) {
            suppressionId("https://ensweb.users.info.unicaen.fr/android-api/?apikey=21912873&method=delete&id=" + ad.getId());
            Snackbar.make(findViewById(R.id.date),"votre annonce a été supprimer avec succes", Snackbar.LENGTH_LONG).show();
        }else
        {
            Snackbar.make(findViewById(R.id.date),"vous ne disposez pas les droit de supprimer cette annonce", Snackbar.LENGTH_LONG).show();
        }
    }
    public void modifierAnnonce(View v)
    {
        if(v.getId()==R.id.modifier)
        {
            Intent next= new Intent(this,ModifAnnonce.class);
            next.putExtra("idAnnonce", ad);
            startActivity(next);
        }
    }
  /* public void seeAdsSeved(View v){

        AnnonceDb annonceDb = new AnnonceDb(v.getContext());
       // Cursor resultat = annonceDb.listeAnnoncesSauvegardees();
        //Log.i("ttt ", resultat.toString());
    }*/
}
