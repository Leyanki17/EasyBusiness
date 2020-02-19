package com.projet.easybusiness.recycler_view_helper;

import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.projet.easybusiness.Annonce;
import com.projet.easybusiness.AnnonceContract;
import com.projet.easybusiness.R;
import com.projet.easybusiness.helper_request.HelperClass;
import com.squareup.picasso.Picasso;

public class AnnonceViewHolder extends RecyclerView.ViewHolder {

    private TextView titre;
    private TextView date;
    private TextView id;
    private TextView description;
    private ImageView img;
    public AnnonceViewHolder(@NonNull View itemView) {
        super(itemView);
       titre = (TextView)itemView.findViewById(R.id.titre);
       date= (TextView)itemView.findViewById(R.id.date);
       description=(TextView)itemView.findViewById(R.id.description);
       id= (TextView)itemView.findViewById(R.id.idAnnonce);
       img = itemView.findViewById(R.id.imgAd);

        // initialisation des items de la vue
    }

    public void bind(Annonce ad) {
        titre.setText(ad.getTitre());
        date.setText(HelperClass.formatDate(ad.getDate()));
        description.setText(ad.getDescription());
        id.setText(ad.getId());
        Log.e("try",""+ad.getImages()[0]);
        Picasso.get().load(ad.getImages()[0]).fit().error(R.drawable.laptop_hp).into(img);
    }

    public void bind(Cursor cursor){
        String ctitre= cursor.getString(cursor.getColumnIndex(AnnonceContract.FeedEntry.COLUMN_NAME_TITRE));
        Long cdate = cursor.getLong(cursor.getColumnIndex(AnnonceContract.FeedEntry.COLUMN_NAME_DATE));
        String cdescription = cursor.getString(cursor.getColumnIndex(AnnonceContract.FeedEntry.COLUMN_NAME_DESCRIPTION));
        String cimage = cursor.getString(cursor.getColumnIndex(AnnonceContract.FeedEntry.COLUMN_NAME_IMAGES));
        String cid= cursor.getString(cursor.getColumnIndex(AnnonceContract.FeedEntry.COLUMN_NAME_IMAGES));
        titre.setText(ctitre);
        date.setText(HelperClass.formatDate(cdate));
        description.setText(cdescription);
        id.setText(cid);
        Picasso.get().load(cimage).fit().error(R.drawable.laptop_hp).into(img);
    }
}
