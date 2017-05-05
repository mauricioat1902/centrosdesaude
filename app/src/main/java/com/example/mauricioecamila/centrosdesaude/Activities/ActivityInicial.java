package com.example.mauricioecamila.centrosdesaude.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mauricioecamila.centrosdesaude.Conexao;
import com.example.mauricioecamila.centrosdesaude.R;
import com.example.mauricioecamila.centrosdesaude.Usuario;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class ActivityInicial extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener{

    private LoginButton buttonFacebook;
    private List<String> facebookPermitions;
    private CallbackManager callbackManager;
    GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "SignInActivity";
    public Usuario usuario;
    private Button btnEntrarEmail;

    private String url = "";
    private String parametros = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Verificar se o usuário já logou antes, se já tem os dados gravados não irá para a página de login
        if(VerificaUsuarioSalvo()){
            //Se estiver salvo, verifica se é usuário Google, Facebook ou email

            SharedPreferences preferences = getSharedPreferences("prefUsuario", Context.MODE_PRIVATE);
            int tipoUsuario = preferences.getInt("tipoUsuario", 0);
            System.out.println("--preferences: " + preferences.getString("nomeUsuario", "sem nome"));
            System.out.println("--preferences: " + preferences.getString("sobrenomeUsuario", "sem nome"));
            System.out.println("--preferences: " + preferences.getString("emailUsuario", "sem nome"));
            if(tipoUsuario == 2){
                Intent abrePrincipal = new Intent(ActivityInicial.this, ActivityPrincipal.class);
                startActivity(abrePrincipal);
            }else if(tipoUsuario == 3){
                //Usuário Google
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();

                // Build a GoogleApiClient with access to the Google Sign-In API and the
                // options specified by gso.
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .build();

                signIn();
            }else if(tipoUsuario == 4 && isLoggedInFacebook()){
                //Facebook
                finish();
                Intent abrePrincipal = new Intent(ActivityInicial.this, ActivityPrincipal.class);
                startActivity(abrePrincipal);
            }
        }
        //Limpa os dados da sharedpreference se houver algo salvo
        //SharedPreferences.Editor prefsEditor = getSharedPreferences("prefUsuario", Context.MODE_PRIVATE).edit();
        //prefsEditor.clear();
        //prefsEditor.commit();

        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_inicial);

        //Entrar com Email
        btnEntrarEmail = (Button)findViewById(R.id.btnEntrarEmail);
        btnEntrarEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent abreLogin = new Intent(ActivityInicial.this, ActivityLogin.class);
                startActivity(abreLogin);
            }
        });
        //Fim entrar com Email

        callbackManager = CallbackManager.Factory.create();
        facebookPermitions = Arrays.asList("email", "public_profile", "user_friends", "user_about_me");
        buttonFacebook = (LoginButton) findViewById(R.id.login_button_facebook);
        buttonFacebook.setReadPermissions(facebookPermitions);

        //Entrar com Facebook
        buttonFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject user, GraphResponse graphResponse) {
                        //SUCESSO AO LOGAR COM FACEBOOK
                        //Toast.makeText(getApplicationContext(), "SUCESSO: " + jsonObject.toString(), Toast.LENGTH_LONG).show();
                        //Verificar se há um usuário com esse email, se houver irá fazer o login, se não irá cadastrar no banco
                        //new ActivityInicial().cadastrarLogarFacebook();
                        String nome = user.optString("name").split(" ")[0];
                        String sobrenome = user.optString("name").split(" ")[1];
                        Usuario usuario = new Usuario();
                        usuario.setNome(nome);
                        usuario.setSobreNome(sobrenome);
                        usuario.setTipoUsuario(4);
                        if(user.optString("email").trim() == "")
                            usuario.setEmail("Login com Facebook");
                        else
                            usuario.setEmail(user.optString("email"));
                        ArmazenarDadosLogin(usuario);
                        finish();
                        Intent abrePrincipal = new Intent(ActivityInicial.this, ActivityPrincipal.class);
                        startActivity(abrePrincipal);

                    }
                });
                request.executeAsync();
                //Toast.makeText(getApplicationContext(), "SUCESSO!", Toast.LENGTH_LONG).show();
                /*final AccessToken accessToken = loginResult.getAccessToken();
                GraphRequestAsyncTask request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject user, GraphResponse graphResponse) {
                        System.out.println("---email:" + user.optString("email"));
                        System.out.println("---name:" +user.optString("name"));
                        System.out.println("---id:" +user.optString("id"));
                        System.out.println("---user_about_me:" +user.optString("user_about_me"));
                        System.out.println("---negadas: " + AccessToken.getCurrentAccessToken().getPermissions());
                    }
                }).executeAsync();*/
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "CANCEL!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(getApplicationContext(), "ERROR! -- " + e.toString(), Toast.LENGTH_LONG).show();
            }
        });
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        // Set the dimensions of the sign-in button.
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        TextView textView = (TextView) signInButton.getChildAt(0);
        textView.setText("Login com Google");
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    //Google e Faceboook
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        //Se requestCode for igual ao RC_SIGN_IN é do Google, se não é do Facebook
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    //Google
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [START signOut]
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END signOut]
    // [START revokeAccess]
    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END revokeAccess]

    //Google
    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            this.cadastrarLogarGoogle(result);

        } else {
            // Signed out, show unauthenticated UI.
            Toast.makeText(getApplicationContext(), "ERRO: " + result, Toast.LENGTH_LONG).show();
            updateUI(false);
        }
    }
    // [END handleSignInResult]
    private void updateUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);

        } else {
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
        }
    }

    //Google
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(getApplicationContext(), "onConnectionFailed: " + connectionResult, Toast.LENGTH_LONG).show();
    }

    //Google
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            /*case R.id.sign_out_button:
                signOut();
                break;
            case R.id.disconnect_button:
                revokeAccess();
                break;*/
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
            if(resultado.contains("email_cadastrado")){
                //dialog.dismiss();
                //Toast.makeText(getApplicationContext(),"Este email já está cadastrado", Toast.LENGTH_SHORT).show();
                ArmazenarDadosLogin(usuario);
                Intent abreLogin = new Intent(ActivityInicial.this, ActivityLogin.class);
                startActivity(abreLogin);
            }
            else if(resultado.contains("OK")){
                //dialog.dismiss();
                Toast.makeText(getApplicationContext(),"Cadastro concluído com sucesso", Toast.LENGTH_SHORT).show();
                ArmazenarDadosLogin(usuario);
                Intent abreLogin = new Intent(ActivityInicial.this, ActivityLogin.class);
                startActivity(abreLogin);
            }
            else{
                //dialog.dismiss();
                Toast.makeText(getApplicationContext(),"Ocorreu um erro ao realizar o login: " + resultado, Toast.LENGTH_SHORT).show();
            }
        }
    }//SolicitaDados

    //Se sair da tela vai fechar
    /*@Override
    protected void onPause() {
        super.onPause();
        finish();
    }*/

    public void ArmazenarDadosLogin(Usuario usuario){
        SharedPreferences settings = getSharedPreferences("prefUsuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("nomeUsuario", usuario.getNome());
        editor.putString("sobrenomeUsuario", usuario.getSobreNome());
        editor.putString("emailUsuario", usuario.getEmail());
        editor.putString("sexoUsuario", usuario.getSexo());
        editor.putInt("tipoUsuario", usuario.getTipoUsuario());
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

    private void cadastrarLogarGoogle(GoogleSignInResult result){
        // Signed in successfully, show authenticated UI.
        GoogleSignInAccount acct = result.getSignInAccount();
        //updateUI(true);
        //CADASTRO DO USUÁRIO
        //Verifica o estado da rede e conexão
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        //Se o estado da rede for diferente de nulo e a rede estiver conectada, irá executar
        if (networkInfo != null && networkInfo.isConnected()) {
            String nome =  acct.getGivenName();
            String sobreNome =  acct.getFamilyName();
            String email =  acct.getEmail();
            usuario = new Usuario(nome,sobreNome,email);
            usuario.setTipoUsuario(3);
            //Criar a URL
            url = "http://192.168.0.31:8090/cadastrarGoogle.php";
            //url = "http://localhost:8090/login/logar.php";
            parametros = "?nome=" + nome + "&sobreNome=" + sobreNome +"&email=" +email;
            new SolicitaDados().execute(url);

        } else {
            Toast.makeText(getApplicationContext(), "Nenhuma conexão foi detectada", Toast.LENGTH_LONG).show();
        }//FIM CADASTRO DO USUÁRIO
    }

    private void cadastrarLogarFacebook(){
        /*ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        //Se o estado da rede for diferente de nulo e a rede estiver conectada, irá executar
        if (networkInfo != null && networkInfo.isConnected()) {
            String nome =  acct.getGivenName();
            String sobreNome =  acct.getFamilyName();
            String email =  acct.getEmail();
            usuario = new Usuario(nome,sobreNome,email);
            usuario.setTipoUsuario(3);
            //Criar a URL
            url = "http://192.168.0.31:8090/cadastrarGoogle.php";
            //url = "http://localhost:8090/login/logar.php";
            parametros = "?nome=" + nome + "&sobreNome=" + sobreNome +"&email=" +email;
            new SolicitaDados().execute(url);

        } else {
            Toast.makeText(getApplicationContext(), "Nenhuma conexão foi detectada", Toast.LENGTH_LONG).show();
        }//FIM CADASTRO DO USUÁRIO*/
    }

    public boolean isLoggedInFacebook() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }
}
