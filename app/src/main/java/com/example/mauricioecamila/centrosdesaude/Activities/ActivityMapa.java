package com.example.mauricioecamila.centrosdesaude.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.mauricioecamila.centrosdesaude.FragmentoMapaProvider;
import com.example.mauricioecamila.centrosdesaude.R;

public class ActivityMapa extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FragmentManager fragmentManager;
    private String paramLatitude;
    private String paramLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Bundle params = intent.getExtras();

        if(params!=null) {
            paramLatitude = params.getString("latitude");
            paramLongitude = params.getString("longitude");
        }
        ShowFragment(new FragmentoMapaProvider(), "FragmentoMapaProvider");

    }

    private void ShowFragment(Fragment fragment, String name)
    {
        //Código para chamar o mapa
        //begin
        fragmentManager = getSupportFragmentManager();
        //Pssando os parâmetros
        Bundle parametros = new Bundle();
        parametros.putString("paramLatitude", paramLatitude);
        parametros.putString("paramLongitude", paramLongitude);
        fragment.setArguments(parametros);

        //Inicia uma transição para podeer iniciar, substituir ou modificar o fragmento
        FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.container_mapa, fragment, name);
        //Confirmar a transação
        transaction.commit();
        //End
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
