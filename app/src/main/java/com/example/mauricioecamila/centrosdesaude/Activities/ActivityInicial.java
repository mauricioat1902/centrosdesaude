package com.example.mauricioecamila.centrosdesaude.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.mauricioecamila.centrosdesaude.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class ActivityInicial extends AppCompatActivity {

    private LoginButton buttonFacebook;
    private List<String> facebookPermitions;
    private CallbackManager callbackManager;

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


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
