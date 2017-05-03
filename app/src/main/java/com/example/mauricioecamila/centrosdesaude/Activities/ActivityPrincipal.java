package com.example.mauricioecamila.centrosdesaude.Activities;

import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mauricioecamila.centrosdesaude.R;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import java.util.ArrayList;

public class ActivityPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fragmentManager;
    private AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton botaoProcurarEstabelecimento = (ImageButton) findViewById(R.id.botaoProcurarEstabelecimento);
        ImageButton botaoRanking = (ImageButton) findViewById(R.id.botaoRanking);
        //Aqui está chamando o menu lateral
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.layout_principal);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


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

                ArrayList<String> itens1 = new ArrayList<String>();
                itens1.add("Buscar por Proximidade");
                itens1.add("Buscar por Especialidade");
                itens1.add("Buscar por Nome da Unidade");
                final CharSequence[] itens = {"Buscar por Proximidade","Buscar por Especialidade","Buscar por Nome da Unidade"};
                ArrayAdapter adapter = new ArrayAdapter(ActivityPrincipal.this, R.layout.dialog_itens_buscas, itens);
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityPrincipal.this);
                builder.setTitle("Escolha a busca desejada:");
                builder.setItems(itens, new DialogInterface.OnClickListener() {
                    Intent it;
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                it = new Intent(ActivityPrincipal.this, ActivityBuscaProximidade.class);
                                startActivity(it);
                                break;
                            case 1:
                                it = new Intent(ActivityPrincipal.this, ActivityBuscaEspecialidade.class);
                                startActivity(it);
                                break;
                            case 2:
                                it = new Intent(ActivityPrincipal.this, ActivityBuscaNome.class);
                                startActivity(it);
                                break;
                        }
                    }
                });
                alertDialog = builder.create();
                alertDialog.show();
            }
        });

        botaoRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ActivityPrincipal.this, ActivityRanking.class);
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
        System.out.println("--preferences2: " + preferences.getString("nomeUsuario", "sem nome"));
        System.out.println("--preferences2: " + preferences.getString("sobrenomeUsuario", "sem nome"));
        System.out.println("--preferences2: " + preferences.getString("emailUsuario", "sem nome"));
        if(preferences.getString("nomeUsuario", "Não encontrado") == "Não encontrado")
            navNome.setText(preferences.getString("nomeUsuario", "Não encontrado"));
        else
            navNome.setText(preferences.getString("nomeUsuario", "Não encontrado") + " " + preferences.getString("sobrenomeUsuario", "Não encontrado"));
        navEmail.setText(preferences.getString("emailUsuario", "Não encontrado"));
        String sexoUser = preferences.getString("sexoUsuario", "Não encontrado");

        ImageView imgUser = (ImageView)findViewById(R.id.imgIconUser);
        //Verifica o sexo do usuário para setar a imagem de icone
        if(sexoUser.trim() == "F"){
            Drawable drawableIconUser = getResources().getDrawable(R.drawable.icon_user_fem);
            imgUser.setImageDrawable(drawableIconUser);
        }
        else{
            Drawable drawableIconUser = getResources().getDrawable(R.drawable.icon_user_masc);
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
                Intent startActivityBuscaLocalizacao = new Intent(this, ActivityBuscaProximidade.class);
                startActivity(startActivityBuscaLocalizacao);
                break;
            case R.id.nav_buscaEspec:
                Intent startActivityBuscaEspec = new Intent(this, ActivityBuscaEspecialidade.class);
                startActivity(startActivityBuscaEspec);
                break;
            case R.id.nav_buscaNome:
                Intent startActivityBuscaNome = new Intent(this, ActivityBuscaNome.class);
                startActivity(startActivityBuscaNome);
                break;
            case R.id.nav_Ranking:
                Intent startActitivy = new Intent(this, ActivityRanking.class);
                startActivity(startActitivy);
                break;
            case R.id.nav_sair:
                SharedPreferences.Editor prefsEditor = getSharedPreferences("prefUsuario", Context.MODE_PRIVATE).edit();
                prefsEditor.clear();
                prefsEditor.commit();
                if(isLoggedInFacebook())
                    LoginManager.getInstance().logOut();
                this.finish();
                Intent startActivity = new Intent(this, ActivityInicial.class);
                startActivity(startActivity);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.layout_principal);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean isLoggedInFacebook() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }


}
