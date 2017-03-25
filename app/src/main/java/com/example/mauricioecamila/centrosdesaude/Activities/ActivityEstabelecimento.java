package com.example.mauricioecamila.centrosdesaude.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mauricioecamila.centrosdesaude.Avaliacao;
import com.example.mauricioecamila.centrosdesaude.Adapters.AvaliacaoAdapter;
import com.example.mauricioecamila.centrosdesaude.Conexao;
import com.example.mauricioecamila.centrosdesaude.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ActivityEstabelecimento extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private long idEstabelecimento;
    private String nome;
    private String tipoEstabelecimento;
    private String vinculoSus, temAtendimentoUrgencia, temAtendimentoAmbulatorial, temCentroCirurgico, temObstetra, temNeoNatal, temDialise;
    private String logradouro;
    private String numero;
    private String bairro, cidade, nuCep, estado;
    private String telefone;
    private String turnoAtendimento;
    private String latitude, longitude;
    private Double distancia;
    private Button btnEnviarComentario;
    private EditText etComentario;
    private Double mediadAtendimento, mediaEstrutura, mediaEquipamentos, mediaLocalizacao, mediaTempoAtendimento, mediaGeral;
    private RatingBar rbAtendimento, rbEstrutura, rbEquipamentos, rbLocalização, rbTempoAtendimento;
    private ListView lvComentarios;
    private ArrayList<Avaliacao> avaliacaos;


    private TextView tvNomeEstabelecimento, tvEndereco, tvTelefone, tvTpEstabelecimento, tvAtendUrgencia, tvAtendAmbulatorial,
            tvCentroCirurgico, tvObstetra, tvNeoNatal, tvDialise, tvturnoAtendimento;

    private String url = "";
    private String parametros = "";

    private String parametros2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estabelecimento);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarEstabelecimento);
        setSupportActionBar(toolbar);

        //Aqui está chamando o menu lateral
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_estabelecimento);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_viewEstabelecimento);
        navigationView.setNavigationItemSelectedListener(this);
        //Pegando os parametros passado para essa Activity
        Intent intent = getIntent();
        Bundle params = intent.getExtras();

        if(params!=null) {
            idEstabelecimento = params.getLong("id");
            nome = params.getString("nome");
            tipoEstabelecimento = params.getString("tipoEstabelecimento");
            vinculoSus = params.getString("vinculoSus");
            temAtendimentoUrgencia = params.getString("temAtendimentoUrgencia");
            temAtendimentoAmbulatorial = params.getString("temAtendimentoAmbulatorial");
            temCentroCirurgico = params.getString("temCentroCirurgico");
            temObstetra = params.getString("temObstetra");
            temNeoNatal = params.getString("temNeoNatal");
            temDialise = params.getString("temDialise");
            logradouro = params.getString("logradouro");
            numero = params.getString("numero");
            bairro = params.getString("bairro");
            cidade = params.getString("cidade");
            nuCep = params.getString("cep");
            estado = params.getString("estado");
            telefone = params.getString("telefone");
            turnoAtendimento = params.getString("turnoAtendimento");
            latitude = params.getString("latitude");
            longitude = params.getString("longitude");
            distancia = params.getDouble("distancia");
            mediadAtendimento = params.getDouble("mediaAtendimento");
            mediaEstrutura = params.getDouble("mediaEstrutura");
            mediaEquipamentos = params.getDouble("mediaEquipamentos");
            mediaLocalizacao = params.getDouble("mediaLocalizacao");
            mediaTempoAtendimento = params.getDouble("mediaTempoAtendimento");
            mediaGeral = params.getDouble("mediaGeral");
        }
        tvNomeEstabelecimento = (TextView)findViewById(R.id.tvNomeEstabelecimento);
        tvEndereco = (TextView)findViewById(R.id.tvEndereco);
        tvTelefone = (TextView)findViewById(R.id.tvTelefone);
        tvTpEstabelecimento = (TextView)findViewById(R.id.tvTpEstabelecimento);
        tvAtendUrgencia = (TextView)findViewById(R.id.tvAtendUrgencia);
        tvAtendAmbulatorial = (TextView)findViewById(R.id.tvAtendAmbulatorial);
        tvCentroCirurgico = (TextView)findViewById(R.id.tvCentroCirurgico);
        tvObstetra = (TextView)findViewById(R.id.tvObstetra);
        tvNeoNatal = (TextView)findViewById(R.id.tvNeoNatal);
        tvDialise = (TextView)findViewById(R.id.tvDialise);
        tvturnoAtendimento = (TextView)findViewById(R.id.tvturnoAtendimento);
        etComentario = (EditText)findViewById(R.id.etComentario);

        tvNomeEstabelecimento.setText(nome);
        tvEndereco.setText(logradouro + ", " + numero + " " + cidade + " - " + estado);
        tvTelefone.setText("Telefone: " + telefone);
        tvTpEstabelecimento.setText("Tipo do Estabelecimento: " + tipoEstabelecimento);
        tvAtendUrgencia.setText("Atendimento Urgência: " + temAtendimentoUrgencia);
        tvAtendAmbulatorial.setText("Atendimento Ambulatorial: " + temAtendimentoAmbulatorial);
        tvCentroCirurgico.setText("Centro Cirúrgico: " + temCentroCirurgico);
        tvObstetra.setText("Obstetra: " + temObstetra);
        tvNeoNatal.setText("NeoNatal: " + temNeoNatal);
        tvDialise.setText("Diálise: " + temDialise);
        tvturnoAtendimento.setText("Turno de Atendimento: " + turnoAtendimento);

        rbAtendimento = (RatingBar)findViewById(R.id.rbAtendimento);
        rbAtendimento.setRating(Float.parseFloat(mediadAtendimento.toString()));

        rbEstrutura = (RatingBar)findViewById(R.id.rbEstrutura);
        rbEstrutura.setRating(Float.parseFloat(mediaEstrutura.toString()));

        rbEquipamentos = (RatingBar)findViewById(R.id.rbEquipamentos);
        rbEquipamentos.setRating(Float.parseFloat(mediaEquipamentos.toString()));

        rbLocalização = (RatingBar)findViewById(R.id.rbLocalização);
        rbLocalização.setRating(Float.parseFloat(mediaLocalizacao.toString()));

        rbTempoAtendimento = (RatingBar)findViewById(R.id.rbTempoAtendimento);
        rbTempoAtendimento.setRating(Float.parseFloat(mediaTempoAtendimento.toString()));

        btnEnviarComentario = (Button)findViewById(R.id.btnEnviarComentario);
        btnEnviarComentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Relizando a inserção
                ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                SharedPreferences preferences = getSharedPreferences("prefUsuario", Context.MODE_PRIVATE);
                int idUsuario = preferences.getInt("idUsuario", 0);
                String descComentario = etComentario.getText().toString();

                //Se o estado da rede for diferente de nulo e a rede estiver conectada, irá executar
                if(networkInfo != null && networkInfo.isConnected()){
                    //Criar a URL
                    url = "http://centrosdesaude.com.br/app/enviarComentario.php";
                    parametros = "?idUsuario=" + idUsuario + "&idEstabelecimento=" +  idEstabelecimento + "&descComentario=" + descComentario;

                    new ActivityEstabelecimento.SolicitaDados().execute(url);

                }
                else{
                    Toast.makeText(getApplicationContext(), "Nenhuma conexão foi detectada", Toast.LENGTH_LONG).show();
                }
                //Fim da busca
            }
        });
        //Preencher o listview com os comentários da unidade
        //Se o estado da rede for diferente de nulo e a rede estiver conectada, irá executar
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            //Criar a URL
            url = "http://centrosdesaude.com.br/app/retornoComentarios.php";
            parametros2 = "?idUnidade=" + idEstabelecimento;
            new ActivityEstabelecimento.CarregaComentarios().execute(url);
        }
        else{
            Toast.makeText(getApplicationContext(), "Nenhuma conexão foi detectada", Toast.LENGTH_LONG).show();
        }
        //Fim da busca

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

        switch (id)
        {
            case R.id.nav_buscaLoc:
                Intent startActivityBuscaLocalizacao = new Intent(this, ActivityBuscaLocalizacao.class);
                startActivity(startActivityBuscaLocalizacao);
                break;
            case R.id.nav_buscaEspec:
                break;
            case R.id.nav_buscaNome:
                Intent startActivityBuscaNome = new Intent(this, ActivityBuscaNome.class);
                startActivity(startActivityBuscaNome);
                break;
            case R.id.nav_sair:
                SharedPreferences.Editor prefsEditor = getSharedPreferences("prefUsuario", Context.MODE_PRIVATE).edit();
                prefsEditor.clear();
                prefsEditor.commit();
                this.finish();
                Intent startActivityLogin = new Intent(this, ActivityLogin.class);
                startActivity(startActivityLogin);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_busca_localizacao);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class CarregaComentarios extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return Conexao.postDados(urls[0],parametros2);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String resultado) {
            if (!resultado.isEmpty()) {
                if (resultado.contains("Não houve retorno na busca")) {
                    TextView tvSemRetorno = (TextView)findViewById(R.id.tvSemRetorno);
                    tvSemRetorno.setText("Não há comentários desse Estabelecimento");
                } else{
                    lvComentarios = (ListView)findViewById(R.id.lvComentarios);
                    avaliacaos = new ArrayList<Avaliacao>();
                    try{
                        JSONObject jsonObject = new JSONObject(resultado);
                        JSONArray jsonArray = jsonObject.getJSONArray("comentarios");
                        for (int i=0;i<jsonArray.length();i++){
                            int idComentario = jsonArray.getJSONObject(i).getInt("idComentario");;
                            int idUsuario = jsonArray.getJSONObject(i).getInt("idUsuario");
                            String nome  = jsonArray.getJSONObject(i).getString("nome");
                            String sobrenome  = jsonArray.getJSONObject(i).getString("sobrenome");
                            String data  = jsonArray.getJSONObject(i).getString("data");
                            String descComentario  = jsonArray.getJSONObject(i).getString("descComentario");
                            Long estabelecimento_idUnidade  = jsonArray.getJSONObject(i).getLong("estabelecimento_idUnidade");
                            Double avAtendimento = jsonArray.getJSONObject(i).getDouble("avAtendimento");
                            Double avEstrutura = jsonArray.getJSONObject(i).getDouble("avEstrutura");
                            Double avEquipamentos = jsonArray.getJSONObject(i).getDouble("avEquipamentos");
                            Double avLocalizacao = jsonArray.getJSONObject(i).getDouble("avLocalizacao");
                            Double avTempoAtendimento = jsonArray.getJSONObject(i).getDouble("avTempoAtendimento");

                            Avaliacao avaliacao = new Avaliacao(idComentario,estabelecimento_idUnidade,nome + " " + sobrenome,
                                    avAtendimento, avEstrutura, avEquipamentos, avLocalizacao, avTempoAtendimento, descComentario);

                            Date dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(data);
                            avaliacao.setDataComentario(dt);
                            avaliacaos.add(avaliacao);
                        }

                        ArrayAdapter adapter = new AvaliacaoAdapter(ActivityEstabelecimento.this,avaliacaos);
                        lvComentarios.setAdapter(adapter);

                    }catch (Exception e){
                        System.out.println("Erro no JSON: " + e.toString());
                    }
                }
            }
        }
    }//CarregaComentarios

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

            if(resultado.contains("OK")){
                Toast.makeText(getApplicationContext(),"Comentário enviado com sucesso", Toast.LENGTH_SHORT).show();
            }
            else if(resultado.contains("ERRO")){
                Toast.makeText(getApplicationContext(),"Ocorreu um erro ao enviar o comentário", Toast.LENGTH_SHORT).show();
            }
        }
    }//SolicitaDados


}
