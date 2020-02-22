package com.projet.easybusiness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.google.android.material.snackbar.Snackbar.LENGTH_LONG;

public class DepotAnnonce extends AppCompatActivity {
    private EditText titre;
    private EditText prix;
    private EditText ville;
    private EditText cp;
    private EditText description;
    private File imageAnnonce;
    private Annonce annonce;
    //okHtttp pour l'envoi de post
    private final OkHttpClient client = new OkHttpClient();

    static final int REQUEST_IMAGE_CAPTURE = 1;
    public DepotAnnonce() throws IOException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depot_annonce);
    }
    public void onClick(View view) throws IOException {
        if(view.getId()==R.id.valider) {

            try {
                sendAnnonce();
                Log.i("test","send annonce marche");
            } catch (Exception e) {
                Log.i("test","send annonce ne  marche pas");
                e.printStackTrace();
            }
        }

    }
  Annonce ad;
    String idImage;
    public void parseAd(String body){
        Moshi moshi= new Moshi.Builder().add(new ApiAnnonceAdapter()).build();
        JsonAdapter<Annonce> jsonAdapter = moshi.adapter(Annonce.class);
        try{
             ad= jsonAdapter.fromJson(body);
        }catch(IOException e){
            Log.i("YKJ",e.getMessage());
        }

    }

    public void sendAnnonce() throws Exception {
        SharedPreferences preferences=  getSharedPreferences("PREF",MODE_PRIVATE);
        titre = (EditText) findViewById(R.id.champsTitre);
        prix = (EditText) findViewById(R.id.champsPrix);
        ville = (EditText) findViewById(R.id.champsVille);
        cp = (EditText) findViewById(R.id.champsCp);
        description = (EditText) findViewById(R.id.champsDescription);
        RequestBody formBody = new FormBody.Builder()
                .add("apikey", "21913373")
                .add("method","save")
                .add("titre", titre.getText().toString())
                .add("description",description.getText().toString())
                .add("prix", prix.getText().toString())
                .add("pseudo",preferences.getString("pseudo","inconnu"))
                .add("emailContact",preferences.getString("email","inconnu"))
                .add("telContact",preferences.getString("tel","inconnu"))
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
                        runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        parseAd(adBody);
                                        idImage = ad.getId();
                                        Snackbar make = Snackbar.make(findViewById(R.id.depotAd), "l'annoce à ete bien enregistrée sous l'id " +
                                                idImage, LENGTH_LONG);
                                        make.show();
                                    }
                                }
                        );
                        Log.i("ad",ad.getId());


                    }
                } catch (Exception e)
                {
                    System.out.println(e.getMessage());
                }
            }
        });
    }

    /**
     * Va permettre l'upload de l'image vers une annonce
     * @param v
     */
    public void photo(View v)
    {
        Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (photoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(photoIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    File storageDir;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            ImageView image = (ImageView) findViewById(R.id.imageview);
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            image.setImageBitmap(imageBitmap);
            Log.i("createFile"," Changement de l'image dans le image view");

        }
        Log.i("createFile"," Avant la creation du fichier");
        this.imageAnnonce= createImageFile();
        Log.i("createFile","apres la creation du fichier"+imageAnnonce);
        try {
            sendImage();
            Log.i("dans le sendImage","");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String currentPhotoPath;

    private File createImageFile(){
        // Create an image file name
       try {
           String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format( new Date());
           String imageFileName = "png_" + timeStamp + "_";
           File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
           File image = File.createTempFile(
                   imageFileName,  /* prefix */
                   ".png",         /* suffix */
                   storageDir      /* directory */
           );
           // Save a file: path for use with ACTION_VIEW intents
           currentPhotoPath = image.getAbsolutePath();
           return image;
       }catch (Exception e){

       }

        return null;

    }
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    private final OkHttpClient clientImage = new OkHttpClient();

    public void sendImage() throws Exception {
        // Use the imgur image upload API as documented at https://api.imgur.com/endpoints/image
        Log.i("testID", "id image " + idImage);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("apikey", "21913373")
                .addFormDataPart("method", "addImage")
                .addFormDataPart("id", idImage)
                .addFormDataPart("photo", "nom.png",
                        RequestBody.create(MEDIA_TYPE_PNG,this.imageAnnonce))
                .build();

        final Request requestImg = new Request.Builder()
                .header("Authorization", idImage)
                .url(" https://ensweb.users.info.unicaen.fr/android-api/")
                .post(requestBody)
                .build();

        clientImage.newCall(requestImg).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // on recupere le corps de la reponce
                try {
                    // si la requete n'a pas reussi
                    if (!response.isSuccessful()) {
                    } else {
                        Toast.makeText(getApplicationContext(), "Ajout d'image reussi", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.i("YKJF", "Erreur lors de l'envoie de l'image");
                }
            }
         });
        /*try (Response response = clientImage.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            System.out.println(response.body().string());
        }*/
        /*try {
            Response response = clientImage.newCall(request).execute();
           // if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            //System.out.println(response.body().string());

        }catch (IOException e)
        {
            Log.i("YKJ","dans le send Image");
        }*/
    }
}


