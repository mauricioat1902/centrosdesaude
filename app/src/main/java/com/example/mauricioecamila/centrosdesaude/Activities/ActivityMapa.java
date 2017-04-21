package com.example.mauricioecamila.centrosdesaude.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mauricioecamila.centrosdesaude.Framentos.FragmentoMapaProvider;
import com.example.mauricioecamila.centrosdesaude.R;

public class ActivityMapa extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FragmentManager fragmentManager;
    private String paramLatitude;
    private String paramLongitude;
    private String paramTituloMarcador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_activity_mapa);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Bundle params = intent.getExtras();

        if(params!=null) {
            paramLatitude = params.getString("latitude");
            paramLongitude = params.getString("longitude");
            paramTituloMarcador = params.getString("tituloMarcador");
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
        parametros.putString("paramTituloMarcador", paramTituloMarcador);
        fragment.setArguments(parametros);

        //Inicia uma transição para podeer iniciar, substituir ou modificar o fragmento
        FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.container_mapa, fragment, name);
        //Confirmar a transação
        transaction.commit();
        //End

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMapa);
        setSupportActionBar(toolbar);

        //Aqui está chamando o menu lateral
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_mapa);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_viewMapa);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_principal, menu);
        TextView navNome, navEmail;
        navNome = (TextView)findViewById(R.id.navNomeUsuario);
        navEmail = (TextView)findViewById(R.id.navEmailUsuario);
        //Pega os dados do usuário armezados na SharedPreferences
        SharedPreferences preferences = getSharedPreferences("prefUsuario", Context.MODE_PRIVATE);
        navNome.setText(preferences.getString("nomeUsuario", "Não encontrado"));
        navEmail.setText(preferences.getString("emailUsuario", "Não encontrado"));
        String sexoUser = preferences.getString("sexoUsuario", "Não encontrado");

        ImageView imgUser = (ImageView)findViewById(R.id.imgIconUser);
        //Verifica o sexo do usuário para setar a imagem de icone
        if(sexoUser.contains("M")){
            Drawable drawableIconUser = getResources().getDrawable(R.drawable.icon_user_masc);
            imgUser.setImageDrawable(drawableIconUser);
        }
        else{
            Drawable drawableIconUser = getResources().getDrawable(R.drawable.icon_user_fem);
            imgUser.setImageDrawable(drawableIconUser);
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        Intent startActivity;
        switch (id)
        {
            case R.id.nav_buscaLoc:
                startActivity = new Intent(this, ActivityBuscaProximidade.class);
                startActivity(startActivity);
                break;
            case R.id.nav_buscaEspec:
                startActivity = new Intent(this, ActivityBuscaEspecialidade.class);
                startActivity(startActivity);
                break;
            case R.id.nav_buscaNome:
                startActivity = new Intent(this, ActivityBuscaNome.class);
                startActivity(startActivity);
                break;
            case R.id.nav_Ranking:
                startActivity = new Intent(this, ActivityRanking.class);
                startActivity(startActivity);
                break;
            case R.id.nav_home:
                startActivity = new Intent(this, ActivityPrincipal.class);
                startActivity(startActivity);
                break;
            case R.id.nav_sair:
                SharedPreferences.Editor prefsEditor = getSharedPreferences("prefUsuario", Context.MODE_PRIVATE).edit();
                prefsEditor.clear();
                prefsEditor.commit();
                this.finish();
                startActivity = new Intent(this, ActivityLogin.class);
                startActivity(startActivity);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_busca_proximidade);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
