package com.projet.easybusiness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ModifAnnonce extends AppCompatActivity {

   Annonce ad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modif_annonce);

        Intent intent= getIntent();

        this.ad= intent.getParcelableExtra("idAnnonce");
        if(ad!=null){
            remplirAnnonce(ad);
        }else
        {
        }
    }
    public void remplirAnnonce(Annonce annonce)
    {

        TextView titre = (TextView) findViewById(R.id.champsTitre);
        TextView prix = (TextView) findViewById(R.id.champsPrix);
        TextView ville = (TextView) findViewById(R.id.champsVille);
        TextView cp = (TextView) findViewById(R.id.champsCp);
        TextView description = (TextView) findViewById(R.id.champsDescription);
        titre.setText(annonce.getTitre());
        prix.setText(" "+ad.getPrix()+" $");
        ville.setText(annonce.getVille());
        cp.setText(annonce.getCp());
        description.setText(annonce.getDescription());
    }
    public void parseAd(String body){
        Moshi moshi= new Moshi.Builder().add(new ApiAnnonceAdapter()).build();
        JsonAdapter<Annonce> jsonAdapter = moshi.adapter(Annonce.class);
        try{
            this.ad= jsonAdapter.fromJson(body);
            //rempliAnnonce(ad);
        }catch(IOException e){
            Log.i("YKJ",e.getMessage());
        }
    }
    //
    private final OkHttpClient client = new OkHttpClient();
    public void ModifierAnnonce() throws Exception {

        TextView titre = (TextView) findViewById(R.id.champsTitre);
        TextView prix = (TextView) findViewById(R.id.champsPrix);
        TextView ville = (TextView) findViewById(R.id.champsVille);
        TextView cp = (TextView) findViewById(R.id.champsCp);
        TextView description = (EditText) findViewById(R.id.champsDescription);
        Log.i("titre",titre.getText().toString());
        RequestBody formBody = new FormBody.Builder()
                .add("apikey", "21912873")
                .add("method","update")
                .add("id",ad.getId())
                .add("titre", titre.getText().toString())
                .add("description",description.getText().toString())
                .add("prix", prix.getText().toString())
                .add("pseudo", ad.getPseudo())
                .add("emailContact", ad.getEmailContact())
                .add("telContact", ad.getTelContact())
                .add("ville",ville.getText().toString())
                .add("cp",cp.getText().toString())
                .build();
        final Request request = new Request.Builder()
                .url(" https://ensweb.users.info.unicaen.fr/android-api/")
                .post(formBody)
                .build();
        // autre method avec response
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // on recupere le corps de la reponce
                try{
                    response = client.newCall(request).execute();
                    // si la requete n'a pas reussi
                    if(!response.isSuccessful()){
                        // afficher un message d'erreur
                        Log.i("post", "Le POST n'a pas reussi");
                        throw new IOException("Unexpected HTTP Code ");
                    }else
                    {
                        Log.i("post", "Le POST a belle et bien reussi");
                        //Log.i("post", response.body().string());
                        final String adBody= response.body().string();
                    }
                } catch (Exception e)
                {
                    System.out.println(e.getMessage());
                }
            }
        });
    }
    public void onClick(View view)
    {
        SharedPreferences preferences=  getSharedPreferences("PREF",MODE_PRIVATE);

        if(view.getId()==R.id.valider && preferences.getString("email","inconnu").equalsIgnoreCase(ad.getEmailContact())) {
            try {
                Log.i("modif","modif");
                ModifierAnnonce();
                Snackbar.make(findViewById(R.id.champsDescription),"votre annonce a été modifer avec succes", Snackbar.LENGTH_LONG).show();
                Log.i("modif2","modif2");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else
        {
            // snackbar a faire
            Snackbar.make(findViewById(R.id.champsDescription),"vous ne disposez pas le droit de modifier cette annonce", Snackbar.LENGTH_LONG).show();
        }
    }
}
