package com.projet.easybusiness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.projet.easybusiness.fragment.AllAdsFragment;
import com.projet.easybusiness.fragment.FavoritAdsFragment;
import com.projet.easybusiness.fragment.MyAdsFragment;
import com.projet.easybusiness.fragment.ProfilFragment;
import com.projet.easybusiness.helper_request.HelperClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SeeAllAd extends AppCompatActivity {
    protected Map<String,Annonce> annonces=new HashMap<>();
    protected ArrayList<Annonce> listAnnonce;
    private ViewPager viewPage;
    private TabLayout tablelayout;
    public FragmentAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all_ad);
        SharedPreferences preferences = getSharedPreferences("PREF",MODE_PRIVATE);
        // si la personne n'est pas identifiée
        if(preferences.getString("email","inconnu").equals("inconnu")){
            Intent intent = new Intent(this,UserInformation.class);
            startActivity(intent);
        }else{
            Toolbar toolbarItem = findViewById(R.id.tool_br);
            toolbarItem.setTitle("");
            setSupportActionBar(toolbarItem);
            this.viewPage = (ViewPager) findViewById(R.id.viewpager);
            tablelayout = (TabLayout) findViewById(R.id.tablay);

            this.pagerAdapter = new FragmentAdapter(getSupportFragmentManager());

            ((FragmentAdapter) pagerAdapter).addFragment(new AllAdsFragment(),"Tout");
            ((FragmentAdapter) pagerAdapter).addFragment(new FavoritAdsFragment(),"Favorie");
            //((FragmentAdapter) pagerAdapter).addFragment(new MyAdsFragment() ,"Mes annonces");

            this.viewPage.setAdapter((FragmentAdapter)pagerAdapter);
            this.tablelayout.setupWithViewPager(viewPage);
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
            intent = new Intent(this,DepotAnnonce.class); //ajout annonce
            startActivity(intent);
        }else{
            //envoyer à la liste des annonces
            intent = new Intent(this,SeeAllAd.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
    /********************************/
    public void itemClick(View v){
        String titreClick;
        TextView clicked = (TextView)v.findViewById(R.id.idAnnonce);
        titreClick=clicked.getText().toString();

        Intent next= new Intent(this,SeeAd.class);
        Log.i("titret",titreClick);
        next.putExtra("idAnnonce", annonces.get(titreClick));
        startActivity(next);
    }


    public void fillMap(){
        for(int i=0; i<this.listAnnonce.size() ;i++){
            Annonce ad= this.listAnnonce.get(i);
            ad.setImage(HelperClass.changeToPng(ad.getImages()));
            annonces.put(ad.getId(),ad);
            Log.i("roooo",ad.getId());
        }
    }
}