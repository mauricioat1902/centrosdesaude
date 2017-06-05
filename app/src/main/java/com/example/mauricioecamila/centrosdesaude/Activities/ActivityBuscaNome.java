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
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mauricioecamila.centrosdesaude.Adapters.UnidadeAdapterRV;
import com.example.mauricioecamila.centrosdesaude.Conexao;
import com.example.mauricioecamila.centrosdesaude.Estabelecimento;
import com.example.mauricioecamila.centrosdesaude.GPSTracker;
import com.example.mauricioecamila.centrosdesaude.R;
import com.example.mauricioecamila.centrosdesaude.Unidade;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.Normalizer;
import java.util.ArrayList;

public class ActivityBuscaNome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private Button botaoBusca;
    private EditText editText;
    private RecyclerView rvBuscaNome;
    private ArrayList<Unidade> unidades;
    private Estabelecimento estabelecimento;
    private String[] estados = new String[]{"AC","AL","AP","AM","BA","CE","DF","ES","GO","MA","MT",
            "MS","MG","PA","PB","PR","PE","PI","RJ","RN","RS","RO","RR","SC","SP","SE","TO"};
    private Spinner spnEstados;
    private double latitude = 0;
    private double longitude = 0;
    private GPSTracker gps;
    private FragmentManager fragmentManager;
    private LocationManager manager;
    private String url = "";
    private String parametros = "";
    private AlertDialog alert = null;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca_nome);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Aqui está chamando o menu lateral
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_busca_nome);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_viewBuscaNome);
        navigationView.setNavigationItemSelectedListener(this);

        //Preenchendo o spinner com os estados
        ArrayAdapter<String> adpEstados = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,estados);
        adpEstados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnEstados = (Spinner)findViewById(R.id.spinnerEstado);
        spnEstados.setAdapter(adpEstados);
        spnEstados.setSelection(24,false);

        botaoBusca = (Button) findViewById(R.id.botaoBusca);
        editText = (EditText) findViewById(R.id.editText1);

        rvBuscaNome = (RecyclerView) findViewById(R.id.rvBuscaNome);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvBuscaNome.setLayoutManager(layoutManager);

        manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        Boolean estaOn = manager.isProviderEnabled( LocationManager.GPS_PROVIDER);
        gps = new GPSTracker(ActivityBuscaNome.this);
        if(manager.isProviderEnabled( LocationManager.GPS_PROVIDER)){
            //latitude = gps.getLatitude();
            //longitude = gps.getLongitude();
        }else{
            // não pôde pegar a localização
            // GPS ou a Rede não está habilitada
            // Pergunta ao usuário para habilitar GPS/Rede em configurações
            AlertaGPS();
        }
        if(!estaOn ){
            Toast.makeText(getApplicationContext(), "O GPS está desativado.", Toast.LENGTH_SHORT).show();
        }

        botaoBusca.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String nomeEstabelecimento = editText.getText().toString();
                if(nomeEstabelecimento.isEmpty()){
                    Toast.makeText(getApplicationContext(), "O campo de pesquisa está vazio", Toast.LENGTH_LONG).show();
                }
                else {
                    dialog = new ProgressDialog(ActivityBuscaNome.this);
                    dialog.setCancelable(true);
                    dialog.setMessage("Buscando...");
                    dialog.show();
                    nomeEstabelecimento = removerAcentos(nomeEstabelecimento);
                    //Relizando a busca
                    ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                    //Se o estado da rede for diferente de nulo e a rede estiver conectada, irá executar
                    if (networkInfo != null && networkInfo.isConnected()) {
                        String estado = spnEstados.getSelectedItem().toString();
                        //Verifica se há algo no email e senha

                        if (gps.canGetLocation()) {
                            latitude = gps.getLatitude();
                            longitude = gps.getLongitude();
                            url = "http://centrosdesaude.com.br/app/buscaNomeGPS.php";
                            //url = "http://192.168.0.31:8090/busca/buscaNomeGPS.php";
                            parametros = "?nomeEstabelecimento=" + nomeEstabelecimento + "&estado=" + estado + "&lat=" + latitude + "&lng=" + longitude;
                            new SolicitaDados().execute(url);
                        } else {
                            //Criar a URL
                            //url = "http://centrosdesaude.com.br/app/buscaNome.php";
                            url="http://192.168.0.31:8090/busca/buscaNome.php";
                            parametros = "?nomeEstabelecimento=" + nomeEstabelecimento + "&estado=" + estado;
                            new SolicitaDados().execute(url);
                        }


                    } else {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Nenhuma conexão foi detectada", Toast.LENGTH_LONG).show();
                    }
                    //Fim da busca
                }
            }//OnClick
        });//setOnclickListener
    }//onCreate

    @Override
    public void onClick(View v) {

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_busca_nome);
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
                if(isLoggedInFacebook())
                    LoginManager.getInstance().logOut();
                this.finish();
                Intent startActivityInicial = new Intent(this, ActivityInicial.class);
                startActivity(startActivityInicial);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_busca_nome);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class SolicitaDados extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return Conexao.postDados(urls[0],parametros);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String resultado) {

            if(resultado.contains("Erro Conexao:")){
                dialog.dismiss();
                Toast.makeText(getApplicationContext(),"Erro no retorno da busca: " + resultado, Toast.LENGTH_LONG).show();
            }
            else {
                if (!resultado.isEmpty()) {
                    unidades = new ArrayList<Unidade>();
                    try {
                        JSONObject jsonObject = new JSONObject(resultado);
                        JSONArray jsonArray = jsonObject.getJSONArray("unidades");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            System.out.println("--INICIO--");
                            long id = Long.parseLong(jsonArray.getJSONObject(i).getString("id"));
                            System.out.println("--ID-- " + id);
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

                            System.out.println("--VAI CRIAR UNIDADE--");
                            Unidade un = new Unidade(id, nome, logradouro, numero, bairro, municipio, cep, estado, latitude, longitude,
                                    distancia, mediaAtendimento, mediaEstrutura, mediaEquipamentos, mediaLocalizacao, mediaTempoAtendimento, mediaGeral, tipoUnidade);

                            unidades.add(un);
                            System.out.println("--FIM--");
                        }
                        System.out.println("--UnidadeAdapterRV--");
                        UnidadeAdapterRV adapterRV = new UnidadeAdapterRV(ActivityBuscaNome.this,unidades);
                        rvBuscaNome.setAdapter(adapterRV);
                        dialog.dismiss();
                    } catch (Exception e) {
                        dialog.dismiss();
                        System.out.print("ERRO: " + e.toString());
                    }
                } else {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Nenhum registro foi encontrado", Toast.LENGTH_LONG).show();
                }
            }
        }
    }//SolicitaDados

    private void AlertaGPS(){
        LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
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

    public static String removerAcentos(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    public boolean isLoggedInFacebook() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

}