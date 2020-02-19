package com.projet.easybusiness;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;


import android.content.Context;
import android.content.Intent;

import android.database.Cursor;

import android.content.SharedPreferences;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
    Annonce ad=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_ad);
       // makeHttpRequest("https://ensweb.users.info.unicaen.fr/android-api/mock-api/liste.json");

        Intent intent= getIntent();

        Annonce ad= intent.getParcelableExtra("idAnnonce");
        //Intent inten = Intent(this,this.getParent().getLocalClassName().class);

        Toolbar toolbarItem = findViewById(R.id.tool_br);
        toolbarItem.setTitle("return to patrent");
        setSupportActionBar(toolbarItem);


        this.ad= intent.getParcelableExtra("idAnnonce");

        if(ad!=null){
            rempliAnnonce(this.ad);
        }else{
            makeHttpRequest("https://ensweb.users.info.unicaen.fr/android-api/?apikey=21913373&method=details&id=5e44171ab5bbc");
        }
    }

    /***********MENU************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        if(id == R.id.ic_profil){
            intent = new Intent(this,UserInformation.class);
            startActivity(intent);
        }else if(id == R.id.ic_add){
            //intent = new Intent(this,Add_annonce.class); //ajout annonce
            //startActivity(intent);
        }else{
            //envoyer à la liste des annonces
            intent = new Intent(this,SeeAllAd.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
    /********************************/
    public void okhttp(View View){
        //makeHttpRequest("https://ensweb.users.info.unicaen.fr/android-api/mock-api/completeAdWithImages.json");
    }

    public void okhttp404(View view){
        //makeHttpRequest("https://ensweb.users.info.unicaen.fr/android-api/mock-api/erreur.json");

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

      // Log.i ("YKJ", "l'image de "+ ad.getPseudo() +" est " +ad.getImages()[0]);
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
        AnnonceDb annonceDb = new AnnonceDb(this);

        Log.i("xxxx", "on verifie si l'element est dans la base");
        try {
            boolean res =  annonceDb.exist(this.ad.getId());
            if(res){
                Button btn= findViewById(R.id.btSave);
                btn.setText("UNSAVE");
            }
            Log.i("xxxx","Dans le try : " + res);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("xxxx","Erreur de verification: ");
        }

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

        if(!annonceDb.exist(ad.getId())){
            Log.i("xxxx", "on va enregistrer");
            try {
                long res =  annonceDb.ajouter(this.ad);
                Log.i("xxxx","Dans le try : sddf" + res);
                Button btn= findViewById(R.id.btSave);
                btn.setText("UNSAVE");
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("xxxx","Erreur d'insertion : ");
            }
            Log.i("xxxx","Après insertion : ");
        }else{
            unsaveAd();
        }

    }

    public boolean unsaveAd(){
        AnnonceDb annonceDb = new AnnonceDb(this);

        if(annonceDb.deleteAnnonce(ad.getId())){
            Button btn= findViewById(R.id.btSave);
            btn.setText("SAVE");
            return true;
        }else{
            return false;
        }
    }

    public void delAd(View v){

    }

    public void editAd(View v){

    }

  /* public void seeAdsSeved(View v){

        AnnonceDb annonceDb = new AnnonceDb(v.getContext());
       // Cursor resultat = annonceDb.listeAnnoncesSauvegardees();
        //Log.i("ttt ", resultat.toString());
    }*/
}
