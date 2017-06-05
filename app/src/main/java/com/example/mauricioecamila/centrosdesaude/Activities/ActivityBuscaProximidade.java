package com.example.mauricioecamila.centrosdesaude.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mauricioecamila.centrosdesaude.Adapters.UnidadeAdapterRV;
import com.example.mauricioecamila.centrosdesaude.Conexao;
import com.example.mauricioecamila.centrosdesaude.GPSTracker;
import com.example.mauricioecamila.centrosdesaude.R;
import com.example.mauricioecamila.centrosdesaude.Unidade;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityBuscaProximidade extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private Button botaoBuscaLocalizacao;
    private int km = 5;
    private RecyclerView rvBuscaProximidade;
    private ArrayList<Unidade> unidades;
    private Button btnMap;
    private EditText etRaioBusca;
    private ProgressDialog dialog;

    private String url = "";
    private String parametros = "";
    private double latitude = 0;
    private double longitude = 0;
    GPSTracker gps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca_proximidade);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarBuscaLocalizacao);
        setSupportActionBar(toolbar);

        //Aqui está chamando o menu lateral
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_busca_proximidade);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_viewBuscaLocalizacao);
        navigationView.setNavigationItemSelectedListener(this);

        rvBuscaProximidade = (RecyclerView) findViewById(R.id.rvBuscaProximidade);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvBuscaProximidade.setLayoutManager(layoutManager);

        LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        Boolean estaOn = manager.isProviderEnabled( LocationManager.GPS_PROVIDER);
        botaoBuscaLocalizacao = (Button) findViewById(R.id.botaoBuscaLocalizacao);

        gps = new GPSTracker(ActivityBuscaProximidade.this);
        if(gps.canGetLocation()){
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        }else{
            // não pôde pegar a localização
            // GPS ou a Rede não está habilitada
            // Pergunta ao usuário para habilitar GPS/Rede em configurações
            AlertaGPS();
        }
        if(!estaOn ){
            Toast.makeText(getApplicationContext(), "O GPS está desativado.", Toast.LENGTH_SHORT).show();
        }

        etRaioBusca = (EditText) findViewById(R.id.etRaioBusca);


        botaoBuscaLocalizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!gps.canGetLocation()){
                    Toast.makeText(getApplicationContext(), "GPS desativado. Ative-o para que possa realizar a busca.", Toast.LENGTH_SHORT).show();
                }
                else {
                    dialog = new ProgressDialog(ActivityBuscaProximidade.this);
                    dialog.setCancelable(true);
                    dialog.setMessage("Buscando...");
                    dialog.show();
                    if (etRaioBusca.getText().toString().trim().length() > 0) {
                        km = Integer.parseInt(etRaioBusca.getText().toString());
                    } else {
                        km = 5;
                    }

                    //Relizando a busca
                    ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                    //Se o estado da rede for diferente de nulo e a rede estiver conectada, irá executar
                    if (networkInfo != null && networkInfo.isConnected()) {
                        //Criar a URL
                        url = "http://centrosdesaude.com.br/app/buscaLocalizacao.php";
                        //url = "http://localhost:8090/login/logar.php";
                        parametros = "?lat=" + latitude + "&lng=" + longitude + "&km=" + km;
                        new ActivityBuscaProximidade.SolicitaDados().execute(url);

                    } else {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Nenhuma conexão foi detectada", Toast.LENGTH_LONG).show();
                    }
                    //Fim da busca
                }
            }
        });


    }

    private void AlertaGPS(){
        LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        AlertDialog alert = null;
        final Boolean estaOn = manager.isProviderEnabled( LocationManager.GPS_PROVIDER);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("O GPS está desativado. Deseja ativar?")
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") int which) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") int which) {
                        dialog.cancel();
                        if(!estaOn){
                            Toast.makeText(getApplicationContext(), "O GPS está desativado.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
                if(isLoggedInFacebook())
                    LoginManager.getInstance().logOut();
                this.finish();
                Intent startActivityInicial = new Intent(this, ActivityInicial.class);
                startActivity(startActivityInicial);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_busca_proximidade);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean isLoggedInFacebook() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_busca_proximidade);
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


    public class SolicitaDados extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            return Conexao.postDados(urls[0],parametros);
            //} catch (IOException e) {
            //    return "Unable to download the requested page.";
            //}
        }


        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String resultado) {

            if(!resultado.isEmpty()){
                unidades = new ArrayList<Unidade>();
                try {
                    JSONObject jsonObject = new JSONObject(resultado);
                    JSONArray jsonArray = jsonObject.getJSONArray("unidades");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        long id = Long.parseLong(jsonArray.getJSONObject(i).getString("id"));
                        String nome = jsonArray.getJSONObject(i).getString("nomeFantasia");
                        //String vinculoSus = jsonArray.getJSONObject(i).getString("vinculoSus");
                        String logradouro = jsonArray.getJSONObject(i).getString("logradouro");
                        String numero;
                        if(!jsonArray.getJSONObject(i).getString("numero").toString().isEmpty())
                            numero = jsonArray.getJSONObject(i).getString("numero").toString();
                        else
                            numero = "0";
                        String bairro = jsonArray.getJSONObject(i).getString("bairro");
                        String municipio = jsonArray.getJSONObject(i).getString("municipio");
                        long cep = Long.parseLong(jsonArray.getJSONObject(i).getString("cep"));
                        String estado = jsonArray.getJSONObject(i).getString("estado_sigla");
                        String latitude = jsonArray.getJSONObject(i).getString("latitude");
                        String longitude = jsonArray.getJSONObject(i).getString("longitude");
                        String tipoUnidade = jsonArray.getJSONObject(i).getString("tipoUnidade");
                        Double distancia = jsonArray.getJSONObject(i).getDouble("distancia");
                        Double mediaGeral = jsonArray.getJSONObject(i).getDouble("mediaGeral");
                        Double mediaAtendimento = jsonArray.getJSONObject(i).getDouble("mediaAtendimento");
                        Double mediaEstrutura = jsonArray.getJSONObject(i).getDouble("mediaEstrutura");
                        Double mediaEquipamentos = jsonArray.getJSONObject(i).getDouble("mediaEquipamentos");
                        Double mediaLocalizacao = jsonArray.getJSONObject(i).getDouble("mediaLocalizacao");
                        Double mediaTempoAtendimento = jsonArray.getJSONObject(i).getDouble("mediaTempoAtendimento");

                        Unidade un = new Unidade(id, nome, logradouro, numero, bairro, municipio, cep, estado, latitude, longitude,
                                distancia, mediaAtendimento, mediaEstrutura, mediaEquipamentos, mediaLocalizacao, mediaTempoAtendimento, mediaGeral, tipoUnidade);

                        unidades.add(un);
                    }
                    UnidadeAdapterRV adapterRV = new UnidadeAdapterRV(ActivityBuscaProximidade.this,unidades);
                    rvBuscaProximidade.setAdapter(adapterRV);
                    dialog.dismiss();
                }catch (Exception e){
                    dialog.dismiss();
                    System.out.print("ERRO: " + e.toString());
                }
            }
            else{
                dialog.dismiss();
                Toast.makeText(getApplicationContext(),"Nenhum registro foi encontrado", Toast.LENGTH_LONG).show();
            }
        }
    }//SolicitaDados
}
