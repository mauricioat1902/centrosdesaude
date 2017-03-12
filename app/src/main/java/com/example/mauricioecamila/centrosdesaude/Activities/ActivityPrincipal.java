package com.example.mauricioecamila.centrosdesaude.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mauricioecamila.centrosdesaude.R;

public class ActivityPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton botaoProcurarEstabelecimento = (ImageButton) findViewById(R.id.botaoProcurarEstabelecimento);
        ImageButton botaoBuscaLocalizacao = (ImageButton) findViewById(R.id.botaoEspecialidades);
        //Aqui está chamando o menu lateral
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.layout_principal);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Pega os dados do usuário armezados na SharedPreferences
        //SharedPreferences preferences = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        //navNome = (TextView)navigationView.getHeaderView(R.id.nav_header_activity_principal).findViewById(R.id.navEmailUsuario);
        //navNome.setText("TESTE");
        //navNome = (TextView)findViewById(R.id.navNomeUsuario);
        //navEmail = (TextView)findViewById(R.id.navEmailUsuario);
        //navNome.setText(preferences.getString("nomeUsuario", "Não encontrado"));
        //navEmail.setText(preferences.getString("emailUsuario", "Não encontrado"));



        //Código para chamar um fragmento
        //begin
        /*
        fragmentManager = getSupportFragmentManager();

        //Inicia uma transição para poder iniciar, substituir ou modificar o fragmento
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //O container é o ID do content_activity_principal.xml
        transaction.add(R.id.container, new FragmentoMapa(), "MapsFragment");

        //Confirmar a transação
        transaction.commitAllowingStateLoss();*/
        //End

        botaoProcurarEstabelecimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ActivityPrincipal.this, ActivityBuscaNome.class);
                startActivity(it);
            }
        });

        botaoBuscaLocalizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ActivityPrincipal.this, ActivityBuscaLocalizacao.class);
                startActivity(it);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.layout_principal);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    private void ShowFragment(Fragment fragment, String name)
    {
        //Código para chamar o mapa
        //begin
        fragmentManager = getSupportFragmentManager();

        //Inicia uma transição para podeer iniciar, substituir ou modificar o fragmento
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment, name);
        //Confirmar a transação
        transaction.commit();
        //End
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id)
        {
            case R.id.nav_buscaLoc:
                //ShowFragment(new FragmentoMapa(), "FragmentoMapa");
                Intent startActivityBuscaLocalizacao = new Intent(this, ActivityBuscaLocalizacao.class);
                startActivity(startActivityBuscaLocalizacao);
                break;
            case R.id.nav_buscaEspec:
                //ShowFragment(new FragmentoMapaProvider(), "FragmentoMapaProvider");
                break;
            case R.id.nav_buscaNome:
                //ShowFragment(new FragmentoBuscaNome(), "FragmentoBuscaNome");
                Intent startActivityBuscaNome = new Intent(this, ActivityBuscaNome.class);
                startActivity(startActivityBuscaNome);
                break;
            /*case R.id.nav_ExemploProvider:
                ShowFragment(new ExemploProviderFragmentV1(), "ExemploProviderFragmentV1");
                break;*/
            case R.id.nav_sair:
                SharedPreferences.Editor prefsEditor = getSharedPreferences("prefUsuario", Context.MODE_PRIVATE).edit();
                prefsEditor.clear();
                prefsEditor.commit();
                this.finish();
                Intent startActivityLogin = new Intent(this, ActivityLogin.class);
                startActivity(startActivityLogin);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.layout_principal);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
