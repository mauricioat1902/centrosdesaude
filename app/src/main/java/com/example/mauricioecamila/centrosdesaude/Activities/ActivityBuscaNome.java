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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mauricioecamila.centrosdesaude.Conexao;
import com.example.mauricioecamila.centrosdesaude.Estabelecimento;
import com.example.mauricioecamila.centrosdesaude.Adapters.EstabelecimentoAdapter;
import com.example.mauricioecamila.centrosdesaude.GPSTracker;
import com.example.mauricioecamila.centrosdesaude.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityBuscaNome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private Button botaoBusca;
    private EditText editText;
    private ListView listView;
    private ArrayList<Estabelecimento> estabelecimentos;
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
                            parametros = "?nomeEstabelecimento=" + nomeEstabelecimento + "&estado=" + estado + "&lat=" + latitude + "&lng=" + longitude;
                            new SolicitaDados().execute(url);
                        } else {
                            //Criar a URL
                            url = "http://centrosdesaude.com.br/app/buscaNome.php";
                            //url = "http://localhost:8090/login/logar.php";
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
                startActivity = new Intent(this, ActivityLogin.class);
                startActivity(startActivity);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_busca_nome);
        drawer.closeDrawer(GravityCompat.START);
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

            if(resultado.contains("Erro Conexao:")){
                dialog.dismiss();
                Toast.makeText(getApplicationContext(),"Erro no retorno da busca: " + resultado, Toast.LENGTH_LONG).show();
            }
            else {
                if (!resultado.isEmpty()) {
                    listView = (ListView) findViewById(R.id.listViewBuscaNome);
                    estabelecimentos = new ArrayList<Estabelecimento>();
                    try {
                        JSONObject jsonObject = new JSONObject(resultado);
                        JSONArray jsonArray = jsonObject.getJSONArray("estabelecimentos");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            long id = Long.parseLong(jsonArray.getJSONObject(i).getString("idUnidade"));
                            String nome = jsonArray.getJSONObject(i).getString("nmFantasia");
                            String tipoEstabelecimento = jsonArray.getJSONObject(i).getString("nmTipoEstabelecimento");
                            String vinculoSus = jsonArray.getJSONObject(i).getString("vinculoSus");
                            String temAtendimentoUrgencia = jsonArray.getJSONObject(i).getString("temAtendimentoUrgencia");
                            String temAtendimentoAmbulatorial = jsonArray.getJSONObject(i).getString("temAtendimentoAmbulatorial");
                            String temCentroCirurgico = jsonArray.getJSONObject(i).getString("temCentroCirurgico");
                            String temObstetra = jsonArray.getJSONObject(i).getString("temObstetra");
                            String temNeoNatal = jsonArray.getJSONObject(i).getString("temNeoNatal");
                            String temDialise = jsonArray.getJSONObject(i).getString("temDialise");
                            String logradouro = jsonArray.getJSONObject(i).getString("logradouro");
                            String numero = jsonArray.getJSONObject(i).getString("numero").toString();
                            String bairro = jsonArray.getJSONObject(i).getString("bairro");
                            String cidade = jsonArray.getJSONObject(i).getString("cidade");
                            String nuCep = jsonArray.getJSONObject(i).getString("nuCep");
                            String estado = jsonArray.getJSONObject(i).getString("estado_siglaEstado");
                            String nuTelefone = jsonArray.getJSONObject(i).getString("nuTelefone");
                            String turnoAtendimento = jsonArray.getJSONObject(i).getString("nmTurnoAtendimentocol");
                            String latitude = jsonArray.getJSONObject(i).getString("lat");
                            String longitude = jsonArray.getJSONObject(i).getString("long");
                            Double distancia = jsonArray.getJSONObject(i).getDouble("distancia");
                            Double mediaGeral = jsonArray.getJSONObject(i).getDouble("mediaGeral");
                            Double mediaAtendimento = jsonArray.getJSONObject(i).getDouble("mediaAtendimento");
                            Double mediaEstrutura = jsonArray.getJSONObject(i).getDouble("mediaEstrutura");
                            Double mediaEquipamentos = jsonArray.getJSONObject(i).getDouble("mediaEquipamentos");
                            Double mediaLocalizacao = jsonArray.getJSONObject(i).getDouble("mediaLocalizacao");
                            Double mediaTempoAtendimento = jsonArray.getJSONObject(i).getDouble("mediaTempoAtendimento");

                            Estabelecimento e = new Estabelecimento(id, nome, tipoEstabelecimento, vinculoSus, temAtendimentoUrgencia, temAtendimentoAmbulatorial,
                                    temCentroCirurgico, temObstetra, temNeoNatal, temDialise, logradouro, numero, bairro, cidade, nuCep, estado, nuTelefone,
                                    turnoAtendimento, latitude, longitude);
                            e.setDistancia(distancia);
                            e.setMdGeral(mediaGeral);
                            e.setMdAtendimento(mediaAtendimento);
                            e.setMdEstrutura(mediaEstrutura);
                            e.setMdEquipamentos(mediaEquipamentos);
                            e.setMdLocalizacao(mediaLocalizacao);
                            e.setMdTempoAtendimento(mediaTempoAtendimento);

                            estabelecimentos.add(e);
                        }

                        ArrayAdapter adaptador = new EstabelecimentoAdapter(ActivityBuscaNome.this, estabelecimentos);
                        listView.setAdapter(adaptador);
                        dialog.dismiss();
                    } catch (Exception e) {
                        dialog.dismiss();
                        System.out.print(e.toString());
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
}