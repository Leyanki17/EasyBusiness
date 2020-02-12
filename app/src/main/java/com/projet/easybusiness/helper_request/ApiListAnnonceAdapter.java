package com.projet.easybusiness.helper_request;

import android.util.Log;

import com.projet.easybusiness.Annonce;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.ToJson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;

public class ApiListAnnonceAdapter {

    /*private ArrayList<Annonce> annonces;
    public  ApiListAnnonceAdapter(){
        annonces=new ArrayList<>();
    }*/
    @ToJson
    public String toJson(ResponseAnnonces response) {
        return "";
    }

    @FromJson
     public ResponseAnnonces fromJson(JsonReader reader, JsonAdapter<Annonce> delegate) throws Exception{
        ResponseAnnonces result= null;

        // commencons le  parsing de l'elt;

        reader.beginObject();
        boolean success=false;

        while (reader.hasNext()){
            String name = reader.nextName();

            if(name.equals("success")){
                success= reader.nextBoolean();

                if (!success) {
                    // @todo : récupérer le message d'erreur et le donner à l'exception
                    // @todo : créer une exception spécifique pour la distinguer des IOException
                    Log.i("YKJ", "je ne suis pas aller dans le success");
                    throw new IOException("API a répondu FALSE");
                }
            }else if(name.equals("response")){

                Log.i("YKJ", "je suis dans l'adaptater");
                Log.i("YKJD", "");

              //  result=  ""+ reader.readJsonValue().toString();
                ArrayList<Annonce> p = new ArrayList<Annonce>();
                reader.beginArray();

                while(reader.hasNext()){
                    p.add(delegate.fromJson(reader));
                }
                reader.endArray();
                reader.endObject();
                Log.i("YKJ", "Le parsing c'est bien passé");
                return new ResponseAnnonces(p,success);
            }else{
                // dans notre cas on ne devrait pas avoir d'autres clés que success et response dans le Json
                throw new IOException("Response contient des données non conformes");
            }

        }
        reader.endObject();
        return result;
    }
}
