package com.projet.easybusiness;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;

import com.projet.easybusiness.helper_request.HelperClass;


public class AnnonceDb {

    private AnnonceDbOpener annonceDbOpener;

    public AnnonceDb(Context context)
    {
        annonceDbOpener = new AnnonceDbOpener(context);
    }

    public long ajouter(Annonce annonce)
    {
        SQLiteDatabase db = annonceDbOpener.getWritableDatabase();
        ContentValues values = new ContentValues();
        //int price = Integer.parseInt(String.valueOf(AnnoceContract.FeedEntry.COLUMN_NAME_PRIX));
        values.put( AnnoceContract.FeedEntry.COLUMN_NAME_ID , annonce.getId());
        values.put( AnnoceContract.FeedEntry.COLUMN_NAME_TITRE , annonce.getTitre());
        values.put( AnnoceContract.FeedEntry.COLUMN_NAME_DESCRIPTION , annonce.getDescription());
       values.put( AnnoceContract.FeedEntry.COLUMN_NAME_PRIX,  annonce.getPrix());
        Log.i("nnnn", "Prix : "+(annonce.getPrix()));
        values.put( AnnoceContract.FeedEntry.COLUMN_NAME_PSEUDO , annonce.getPseudo());
        values.put( AnnoceContract.FeedEntry.COLUMN_NAME_EMAIL , annonce.getEmailContact());
        values.put( AnnoceContract.FeedEntry.COLUMN_NAME_TELEPHONE , annonce.getTelContact());
        values.put( AnnoceContract.FeedEntry.COLUMN_NAME_VILLE , annonce.getVille());
        values.put( AnnoceContract.FeedEntry.COLUMN_NAME_CODE_POSTAL , annonce.getCp());
        //values.put(AnnoceContract.FeedEntry.COLUMN_NAME_IMAGES,);
      values.put(AnnoceContract.FeedEntry.COLUMN_NAME_DATE,   annonce.getDate());
        Log.i("nnnn", "Date : "+  HelperClass.formatDate(annonce.getDate())/*HelperClass.stringToDate(HelperClass.formatDate(annonce.getDate()))*/ + " "+ HelperClass.stringToDate(HelperClass.formatDate(annonce.getDate())).getClass().getName());
        long res = db.insert(AnnoceContract.FeedEntry.TABLE_NAME, null, values);


        return res;
    }

    public Cursor listeAnnoncesSauvegardees(){
        String[] projection = {
                AnnoceContract.FeedEntry.COLUMN_NAME_ID,
                AnnoceContract.FeedEntry.COLUMN_NAME_TITRE,
                AnnoceContract.FeedEntry.COLUMN_NAME_DESCRIPTION,
                //String.valueOf(AnnoceContract.FeedEntry.COLUMN_NAME_PRIX),
                AnnoceContract.FeedEntry.COLUMN_NAME_PSEUDO,
                AnnoceContract.FeedEntry.COLUMN_NAME_EMAIL,
                AnnoceContract.FeedEntry.COLUMN_NAME_TELEPHONE,
                AnnoceContract.FeedEntry.COLUMN_NAME_VILLE,
                AnnoceContract.FeedEntry.COLUMN_NAME_CODE_POSTAL
                //String.valueOf(AnnoceContract.FeedEntry.COLUMN_NAME_IMAGES),
               // AnnoceContract.FeedEntry.COLUMN_NAME_DATE
        };

        Log.i("tttt", "après projection");

        Log.i("tttt", "debut ouverture de la requete");
        //annonceDbOpener.close();
        SQLiteDatabase db = annonceDbOpener.getReadableDatabase();
        //SQLiteOpenHelper helper = new AnnonceDbOpener(v.getContext());
       // SQLiteDatabase db = helper.getReadableDatabase();
        Log.i("tttt", "après ouverture de la requete");

        Cursor cursor =   db.query(AnnoceContract.FeedEntry.TABLE_NAME, projection,null,null,null,null,null);
        Log.i("tttt", "après l'execution de la requete");

       while(cursor != null){
           cursor.moveToNext();
       }
        return cursor;
    }

}
