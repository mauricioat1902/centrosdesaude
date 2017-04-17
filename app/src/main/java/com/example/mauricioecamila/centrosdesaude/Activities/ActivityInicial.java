package com.example.mauricioecamila.centrosdesaude.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mauricioecamila.centrosdesaude.Conexao;
import com.example.mauricioecamila.centrosdesaude.R;
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

    private String url = "";
    private String parametros = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_inicial);

        callbackManager = CallbackManager.Factory.create();
        facebookPermitions = Arrays.asList("email", "public_profile", "user_friends");
        buttonFacebook = (LoginButton)findViewById(R.id.login_button_facebook);
        buttonFacebook.setReadPermissions(facebookPermitions);

        buttonFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                        Toast.makeText(getApplicationContext(), "SUCESSO: " + jsonObject.toString(), Toast.LENGTH_LONG).show();
                    }
                });
                request.executeAsync();
                Toast.makeText(getApplicationContext(), "SUCESSO!", Toast.LENGTH_LONG).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //callbackManager.onActivityResult(requestCode, resultCode, data);
        System.out.println("ESTÁ EM onActivityResult");
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            updateUI(true);
            //CADASTRO DO USUÁRIO
            //Verifica o estado da rede e conexão
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            //Se o estado da rede for diferente de nulo e a rede estiver conectada, irá executar
            if (networkInfo != null && networkInfo.isConnected()) {
                String nome =  acct.getGivenName();
                String sobreNome =  acct.getFamilyName();
                String email =  acct.getEmail();
                    //Criar a URL
                    url = "http://centrosdesaude.com.br/app/cadastrarGoogle.php";
                    //url = "http://localhost:8090/login/logar.php";
                    parametros = "?nome=" + nome + "&sobreNome=" + sobreNome +"&email=" +email;
                    new SolicitaDados().execute(url);

            } else {
                Toast.makeText(getApplicationContext(), "Nenhuma conexão foi detectada", Toast.LENGTH_LONG).show();
            }//FIM CADASTRO DO USUÁRIO

        } else {
            // Signed out, show unauthenticated UI.
            System.out.println("Erro handleSignInResult: " + result);
            Toast.makeText(getApplicationContext(), "DEU RUIM: " + result, Toast.LENGTH_LONG).show();
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(getApplicationContext(), "onConnectionFailed: " + connectionResult, Toast.LENGTH_LONG).show();
    }

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

            if(resultado.contains("ERRO")){
                //dialog.dismiss();
                Toast.makeText(getApplicationContext(),"Este email já está cadastrado", Toast.LENGTH_SHORT).show();
            }
            else if(resultado.contains("OK")){
                //dialog.dismiss();
                Toast.makeText(getApplicationContext(),"Cadastro concluído com sucesso", Toast.LENGTH_SHORT).show();
                Intent abreLogin = new Intent(ActivityInicial.this, ActivityLogin.class);
                startActivity(abreLogin);
            }
            else{
                //dialog.dismiss();
                Toast.makeText(getApplicationContext(),"Ocorreu um erro ao cadastrar", Toast.LENGTH_SHORT).show();
            }
        }
    }//SolicitaDados

}
