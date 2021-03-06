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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mauricioecamila.centrosdesaude.Adapters.EspecialidadeAdapter;
import com.example.mauricioecamila.centrosdesaude.Adapters.UnidadeAdapterRV;
import com.example.mauricioecamila.centrosdesaude.Conexao;
import com.example.mauricioecamila.centrosdesaude.Conexao3;
import com.example.mauricioecamila.centrosdesaude.Especialidade;
import com.example.mauricioecamila.centrosdesaude.GPSTracker;
import com.example.mauricioecamila.centrosdesaude.R;
import com.example.mauricioecamila.centrosdesaude.Unidade;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityBuscaEspecialidade extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private String url = "";
    private String parametros = "";
    private double latitude = 0;
    private double longitude = 0;
    private ProgressDialog dialog = null;
    private Button btnBuscarEspec;
    private ArrayList<Unidade> unidades;
    private RecyclerView rvBuscaEspecialidade;
    private GPSTracker gps;
    private Spinner spinEstado, spinCidade;
    private AutoCompleteTextView actvEspecialidade;
    private String[] estados = new String[]{"AC","AL","AP","AM","BA","CE","DF","ES","GO","MA","MT",
            "MS","MG","PA","PB","PR","PE","PI","RJ","RN","RS","RO","RR","SC","SP","SE","TO"};
    private ArrayList<Especialidade> especialidades;
    private ArrayList<String> municipios;

    private Especialidade especSelecionadaAutoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca_especialidade);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarBuscaEspecialidade);
        setSupportActionBar(toolbar);

        //Aqui está chamando o menu lateral
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_busca_especialidade);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_viewBuscaEspecialidade);
        navigationView.setNavigationItemSelectedListener(this);

        /*LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        Boolean estaOn = manager.isProviderEnabled( LocationManager.GPS_PROVIDER);

        gps = new GPSTracker(ActivityBuscaEspecialidade.this);
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
        }*/

        actvEspecialidade = (AutoCompleteTextView) findViewById(R.id.actvEspecialidade);

        rvBuscaEspecialidade = (RecyclerView)findViewById(R.id.rvBuscaEspecialidade);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        rvBuscaEspecialidade.setLayoutManager(layoutManager);

        spinEstado = (Spinner) findViewById(R.id.spinEstado);
        spinCidade = (Spinner) findViewById(R.id.spinCidade);
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            dialog = new ProgressDialog(ActivityBuscaEspecialidade.this);
            dialog.setCancelable(true);
            dialog.setMessage("");
            dialog.show();
            url = "https://www.centrosdesaude.com.br/app/especialidades.php";
            new ActivityBuscaEspecialidade.CarregaEspecialidades().execute(url);
        }else{
            Toast.makeText(getApplicationContext(), "Nenhuma conexão foi detectada", Toast.LENGTH_LONG).show();
        }



        ArrayAdapter<String> adpEstados = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,estados);
        adpEstados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinEstado.setAdapter(adpEstados);
        spinEstado.setSelection(24,false);
        //Preenchendo o spinner do municipio
        spinEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String estado = parent.getItemAtPosition(position).toString();
                /*Conexao2 con = new Conexao2();
                String resultJson = con.postDados("https://gist.githubusercontent.com/ografael/2037135/raw/5d31e7baaddd0d599b64c3ec04827fc244333447/estados_cidades.json");
                System.out.println("----AQUI: " + resultJson);
                */
                //String url = "https://gist.githubusercontent.com/ografael/2037135/raw/5d31e7baaddd0d599b64c3ec04827fc244333447/estados_cidades.json";
                //new ActivityBuscaEspecialidade.SolicitaDados().execute(url);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        actvEspecialidade.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                especSelecionadaAutoCompleteTextView = (Especialidade) parent.getItemAtPosition(position);
            }
        });

        btnBuscarEspec = (Button)findViewById(R.id.btnBuscarEspec);
        btnBuscarEspec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Busca da especialidade

                dialog = new ProgressDialog(ActivityBuscaEspecialidade.this);
                dialog.setCancelable(true);
                dialog.setMessage("Buscando");
                dialog.show();
                long especialidade_id = especSelecionadaAutoCompleteTextView.getId();
                String estado =  spinEstado.getSelectedItem().toString();
                url = "https://www.centrosdesaude.com.br/app/buscaEspecialidade.php";
                parametros="?especialidade_id=" + especialidade_id + "&estado="+estado;
                new ActivityBuscaEspecialidade.SolicitaDados().execute(url);

            }
        });

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_busca_especialidade);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_busca_especialidade);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    public class CarregaEspecialidades extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            return Conexao3.postDados(urls[0],parametros);
            //} catch (IOException e) {
            //    return "Unable to download the requested page.";
            //}
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String resultado) {
            if(resultado.contains("Erro Conexao:")){
                if(dialog != null)
                    dialog.dismiss();
                System.out.println("Erro Conexao: " + resultado);
                Toast.makeText(getApplicationContext(),"Erro no retorno da busca" + resultado, Toast.LENGTH_LONG).show();
            }
            else if(resultado.contains("especialidades")) {
                if (!resultado.isEmpty()) {
                    especialidades = new ArrayList<Especialidade>();
                    try {
                        JSONObject jsonObject = new JSONObject(resultado);
                        JSONArray jsonArray = jsonObject.getJSONArray("especialidades");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            int id = Integer.parseInt(jsonArray.getJSONObject(i).getString("id"));
                            String nome = jsonArray.getJSONObject(i).getString("nome");
                            Especialidade esp = new Especialidade(id,nome);
                            especialidades.add(esp);
                        }
                        ArrayAdapter adapter = new EspecialidadeAdapter(ActivityBuscaEspecialidade.this,R.layout.support_simple_spinner_dropdown_item,especialidades);
                        actvEspecialidade.setAdapter(adapter);
                        dialog.dismiss();
                    } catch (Exception e) {
                        System.out.print(e.toString());
                        dialog.dismiss();
                    }
                } else {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Nenhum registro foi encontrado", Toast.LENGTH_LONG).show();
                }
            }else{
                //Executa para o spinner município
                try {
                    JSONObject jsonObject = new JSONObject(resultado);
                }catch (JSONException e){
                    Toast.makeText(getApplicationContext(), "ERRO: " + e.toString(), Toast.LENGTH_LONG).show();
                }

            }
        }
    }//

    public class SolicitaDados extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            return Conexao3.postDados(urls[0],parametros);
            //} catch (IOException e) {
            //    return "Unable to download the requested page.";
            //}
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String resultado) {

            if(resultado.contains("Erro Conexao:")){
                dialog.dismiss();
                Toast.makeText(ActivityBuscaEspecialidade.this,"Erro no retorno: " + resultado, Toast.LENGTH_LONG).show();
            }
            else {
                if (!resultado.isEmpty()) {

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
                            Double mediaGeral = jsonArray.getJSONObject(i).getDouble("mediaGeral");
                            Double mediaAtendimento = jsonArray.getJSONObject(i).getDouble("mediaAtendimento");
                            Double mediaEstrutura = jsonArray.getJSONObject(i).getDouble("mediaEstrutura");
                            Double mediaEquipamentos = jsonArray.getJSONObject(i).getDouble("mediaEquipamentos");
                            Double mediaLocalizacao = jsonArray.getJSONObject(i).getDouble("mediaLocalizacao");
                            Double mediaTempoAtendimento = jsonArray.getJSONObject(i).getDouble("mediaTempoAtendimento");

                            Unidade un = new Unidade(id, nome, logradouro, numero, bairro, municipio, cep, estado, latitude, longitude,
                                    mediaAtendimento, mediaEstrutura, mediaEquipamentos, mediaLocalizacao, mediaTempoAtendimento, mediaGeral, tipoUnidade);
                            unidades.add(un);

                        }

                        UnidadeAdapterRV adapterRV = new UnidadeAdapterRV(ActivityBuscaEspecialidade.this, unidades);
                        rvBuscaEspecialidade.setAdapter(adapterRV);
                        dialog.dismiss();
                    } catch (Exception e) {
                        Toast.makeText(ActivityBuscaEspecialidade.this, "Erro: " + e.toString(), Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                } else {
                    dialog.dismiss();
                    Toast.makeText(ActivityBuscaEspecialidade.this, "Nenhum registro foi encontrado", Toast.LENGTH_LONG).show();
                }
            }
        }
    }//SolicitaDados

    public class CarregaMunicipios extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return Conexao.postDados(urls[0],parametros);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String resultado) {
            if(resultado.contains("Erro Conexao:")){
                if(dialog != null)
                    dialog.dismiss();
                System.out.println("Erro Conexao: " + resultado);
                Toast.makeText(getApplicationContext(),"Erro no retorno " + resultado, Toast.LENGTH_LONG).show();
            }
            else if(resultado.contains("sigla")) {
                if (!resultado.isEmpty()) {
                    municipios = new ArrayList<String>();
                    try {
                        JSONObject jsonObject = new JSONObject(resultado);
                        JSONArray jsonArray = jsonObject.getJSONArray("estados");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String estado = jsonArray.getJSONObject(i).getString("nome");
                            if(estado == spinEstado.getSelectedItem().toString()){
                                System.out.println("CIDADES: " + jsonArray.getJSONObject(i).getString("cidades"));
                            }
                            int id = Integer.parseInt(jsonArray.getJSONObject(i).getString("id"));
                            String nome = jsonArray.getJSONObject(i).getString("nome");
                            Especialidade esp = new Especialidade(id,nome);
                            especialidades.add(esp);
                        }
                        dialog.dismiss();
                    } catch (Exception e) {
                        System.out.print(e.toString());
                        dialog.dismiss();
                    }
                } else {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Nenhum registro foi encontrado", Toast.LENGTH_LONG).show();
                }
            }else{
                //Executa para o spinner município
                try {
                    JSONObject jsonObject = new JSONObject(resultado);
                }catch (JSONException e){
                    Toast.makeText(getApplicationContext(), "ERRO: " + e.toString(), Toast.LENGTH_LONG).show();
                }

            }
        }
    }//Carrega Municipios

    public boolean isLoggedInFacebook() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

}
