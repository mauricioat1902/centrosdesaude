package com.example.mauricioecamila.centrosdesaude.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mauricioecamila.centrosdesaude.Adapters.FragmentoPagerAdapter;
import com.example.mauricioecamila.centrosdesaude.R;

public class ActivityRanking extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private TabLayout tabRanking;
    private ViewPager viewPagerRank;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarActRanking);
        setSupportActionBar(toolbar);

        //Aqui está chamando o menu lateral
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_ranking);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_viewRanking);
        navigationView.setNavigationItemSelectedListener(this);

        viewPagerRank = (ViewPager)findViewById(R.id.view_pager_ranking);
        tabRanking = (TabLayout)findViewById(R.id.tabRanking);
        viewPagerRank.setAdapter(new FragmentoPagerAdapter(getSupportFragmentManager(),getResources().getStringArray(R.array.tab_titulos)));
        tabRanking.setupWithViewPager(viewPagerRank);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_ranking);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
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
                startActivity = new Intent(this, ActivityInicial.class);
                startActivity(startActivity);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_ranking);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
