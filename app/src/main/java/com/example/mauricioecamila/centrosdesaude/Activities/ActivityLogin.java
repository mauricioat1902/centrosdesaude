package com.example.mauricioecamila.centrosdesaude.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mauricioecamila.centrosdesaude.Conexao;
import com.example.mauricioecamila.centrosdesaude.R;
import com.example.mauricioecamila.centrosdesaude.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ActivityLogin extends AppCompatActivity {

    private EditText editEmail, editSenha;
    private Button botaoEntrar;
    private TextView txtViewCriarConta;
    private Usuario usuario;


    private String url = "";
    private String parametros = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Verificar se o usuário já logou antes, se já tem os dados gravados não irá para a página de login
        if(VerificaUsuarioSalvo()){
            Intent abrePrincipal = new Intent(ActivityLogin.this, ActivityPrincipal.class);
            startActivity(abrePrincipal);
        }
        else {

            setContentView(R.layout.activity_login);
            editEmail = (EditText) findViewById(R.id.editEmail);
            editSenha = (EditText) findViewById(R.id.editSenha);
            botaoEntrar = (Button) findViewById(R.id.botaoEntrar);
            txtViewCriarConta = (TextView) findViewById(R.id.txtViewCriarConta);

            txtViewCriarConta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txtViewCriarConta.setTextColor(Color.WHITE);
                    Intent abreCadastro = new Intent(ActivityLogin.this, ActivityCadastro.class);
                    startActivity(abreCadastro);
                }
            });

            botaoEntrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //enviarDados(v);
                    //Verifica o estado da rede e conexão
                    ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                    //Se o estado da rede for diferente de nulo e a rede estiver conectada, irá executar
                    if (networkInfo != null && networkInfo.isConnected()) {
                        String email = editEmail.getText().toString();
                        String senha = editSenha.getText().toString();

                        //Verifica se há algo no email e senha
                        if (email.isEmpty() || senha.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Nenhum campo pode estar vazio", Toast.LENGTH_LONG).show();
                        } else {
                            //Criar a URL
                            url = "http://centrosdesaude.com.br/logar.php";
                            //url = "http://localhost:8090/login/logar.php";
                            parametros = "?email=" + email + "&senha=" + senha;
                            new SolicitaDados().execute(url);
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Nenhuma conexão foi detectada", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
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

            ProgressDialog dialog = new ProgressDialog(ActivityLogin.this);
            dialog.setCancelable(true);
            dialog.setMessage("Carregando");
            dialog.show();
            //MyDialog md = new MyDialog(ActivityLogin.this, "");

            if(resultado.contains("Erro:")){
                Toast.makeText(getApplicationContext(), resultado, Toast.LENGTH_LONG).show();
            }
            else{
                if(!resultado.contains("login_erro")){
                    try {
                        JSONObject jsonObject = new JSONObject(resultado);
                        JSONArray jsonArray = jsonObject.getJSONArray("usuario");
                        int idUsuario = Integer.parseInt(jsonArray.getJSONObject(0).getString("id"));
                        String nomeUsuario = jsonArray.getJSONObject(0).getString("nome");
                        String sobrenomeUsuario = jsonArray.getJSONObject(0).getString("sobrenome");
                        String email = jsonArray.getJSONObject(0).getString("email");
                        String sexo = jsonArray.getJSONObject(0).getString("sexo");
                        usuario = new Usuario(idUsuario,nomeUsuario,sobrenomeUsuario,email,sexo);
                        ArmazenarDadosLogin(usuario);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Intent abrePrincipal = new Intent(ActivityLogin.this, ActivityPrincipal.class);
                    startActivity(abrePrincipal);
                }
                else{
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Usuário ou senha estão incorretos", Toast.LENGTH_LONG).show();

                }
            }
        }
    }//SolicitaDados

    //Se sair da tela vai fechar
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
    public void ArmazenarDadosLogin(Usuario usuario){
        SharedPreferences settings = getSharedPreferences("prefUsuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("idUsuario", usuario.getId());
        editor.putString("nomeUsuario", usuario.getNome());
        editor.putString("sobrenomeUsuario", usuario.getSobreNome());
        editor.putString("emailUsuario", usuario.getEmail());
        editor.putString("sexoUsuario", usuario.getSexo());
        editor.commit();
    }

    public boolean VerificaUsuarioSalvo(){
        //Pega os dados do usuário armezados na SharedPreferences
        SharedPreferences preferences = getSharedPreferences("prefUsuario", Context.MODE_PRIVATE);
        preferences.getString("nomeUsuario", "Não encontrado");
        if(preferences.getString("nomeUsuario", "Não encontrado") != "Não encontrado")
            return true;
        else
            return false;
    }
}
