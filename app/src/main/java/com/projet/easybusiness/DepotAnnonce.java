package com.projet.easybusiness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DepotAnnonce extends AppCompatActivity {
    private EditText titre;
    private EditText prix;
    private EditText ville;
    private EditText cp;
    private EditText description;
    private Annonce annonce;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depot_annonce);
    }
    public void onClick(View view) throws IOException {
        if(view.getId()==R.id.valider) {

            try {
                run();
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

    //okHtttp pour l'envoi de post
    private final OkHttpClient client = new OkHttpClient();

    public void run() throws Exception {
        titre = (EditText) findViewById(R.id.champsTitre);
        prix = (EditText) findViewById(R.id.champsPrix);
        ville = (EditText) findViewById(R.id.champsVille);
        cp = (EditText) findViewById(R.id.champsCp);
        description = (EditText) findViewById(R.id.champsDescription);
        RequestBody formBody = new FormBody.Builder()
                .add("apikey", "21912873")
                .add("method","save")
                .add("titre", titre.getText().toString())
                .add("description",description.getText().toString())
                .add("prix", prix.getText().toString())
                .add("pseudo","pseudo")
                .add("emailContact","email")
                .add("telContact","772345676")
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
                        runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        parseAd(adBody);
                                    }
                                }
                        );
                        Log.i("ad",ad.getId());
                        idImage = ad.getId();

                    }
                } catch (Exception e)
                {
                    System.out.println(e.getMessage());
                }
            }
        });
    }
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

            //storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            //Log.i("image",storageDir.getAbsolutePath());
        }
    }

    /*public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }*/

    /*private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    private final OkHttpClient clientImage = new OkHttpClient();

    public void sendImage() throws Exception {
        // Use the imgur image upload API as documented at https://api.imgur.com/endpoints/image
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("apikey", "21912873")
                .addFormDataPart("method", "addImage")
                .addFormDataPart("id", idImage)
                .addFormDataPart("photo", "nom.png",
                        RequestBody.create(MEDIA_TYPE_PNG, new File(storageDir.getAbsolutePath())))
                .build();

        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + idImage)
                .url(" https://ensweb.users.info.unicaen.fr/android-api/")
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            System.out.println(response.body().string());
        }
    }*/
}


