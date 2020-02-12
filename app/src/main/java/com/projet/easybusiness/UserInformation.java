package com.projet.easybusiness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import java.util.prefs.Preferences;

public class UserInformation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);
    }

    public void saveProfile(View v){
        TextView pseudo= (TextView) findViewById(R.id.pseudo);
        TextView email = (TextView) findViewById(R.id.email);
        TextView tel= (TextView)findViewById(R.id.tel);

        SharedPreferences preferences=  getSharedPreferences("PREF",MODE_PRIVATE);
        SharedPreferences.Editor managerPreference = preferences.edit();
        managerPreference.putString("pseudo",pseudo.getText().toString());
        managerPreference.putString("email",email.getText().toString());
        managerPreference.putString("tel", tel.getText().toString());
        managerPreference.commit();
        managerPreference.apply();
    }
}
