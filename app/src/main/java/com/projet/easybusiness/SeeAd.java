package com.projet.easybusiness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_ad);
       // makeHttpRequest("https://ensweb.users.info.unicaen.fr/android-api/mock-api/liste.json");

        Intent intent= getIntent();

        Annonce ad= intent.getParcelableExtra("idAnnonce");
        if(ad!=null){
            rempliAnnonce(ad);
        }else{
            makeHttpRequest("https://ensweb.users.info.unicaen.fr/android-api/mock-api/completeAdWithImages.json");
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
        JsonAdapter<Annonce> jsonAdapter= moshi.adapter(Annonce.class);
        try{
            Annonce ad= jsonAdapter.fromJson(body);
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
        ImageView imageView= findViewById(R.id.image);

        Log.i("YKJE", "erreur");
        Log.i ("YKJ", "l'image de "+ ad.getPseudo() +" est " +ad.getImages()[0]);

        Picasso.get().load(ad.getImages()[0]).error(R.drawable.laptop_hp).into(imageView);
        titre.setText(ad.getTitre());
        prix.setText(" "+ad.getPrix()+" $");
        proprietaire.setText(ad.getPseudo());
        email.setText(ad.getEmailContact());
        telephone.setText(ad.getTelContact());
        adresse.setText(ad.getAdresse());
        description.setText(ad.getDescription());
        date.setText(" "+ HelperClass.formatDate(ad.getDate()));
        Log.i("YKJE", "logo fin");
    }

    public void callPers(View v){
        TextView telephone= (TextView) findViewById(R.id.tel);
        startActivity(new Intent(Intent.ACTION_DIAL,Uri.parse("tel: "+telephone.getText().toString())));
    }

    public void sendEmail(View v){
        TextView mail= (TextView) findViewById(R.id.email);
        startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("mailto: "+ mail.getText().toString())));

    }
}
