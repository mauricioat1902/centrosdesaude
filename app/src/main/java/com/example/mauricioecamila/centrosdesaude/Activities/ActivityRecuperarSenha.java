package com.example.mauricioecamila.centrosdesaude.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mauricioecamila.centrosdesaude.R;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ActivityRecuperarSenha extends AppCompatActivity {
    private EditText etRecEmail;
    private Button btnRecEnviar;
    Session session = null;
    ProgressDialog pdialog = null;
    Context context = null;
    String destinatario, titulo, mensagemTexto;
    private String url, parametros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);

        etRecEmail = (EditText) findViewById(R.id.etRecEmail);
        btnRecEnviar = (Button) findViewById(R.id.btnRecEnviar);
        context = this;

        titulo = "Recuperação de Senha Centros de Saúde";
        mensagemTexto = "Nova senha provisória: CentrosSaude3562";

        btnRecEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdialog = ProgressDialog.show(context, "", "Enviando E-mail...", true);
                //Alterando a senha
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if(etRecEmail.getText().toString().trim().equals("")){
                    Toast.makeText(getApplicationContext(), "Campo do E-mail vazio", Toast.LENGTH_LONG).show();
                }
                else {
                    destinatario = etRecEmail.getText().toString().trim();
                    //String emailUsuario = destinatario;
                    //String emailUsuario = "mauricioat1902@gmail.com";

                    //Enviando o Email
                    Properties props = new Properties();
                    //usando email do Google
                    /*
                    props.put("mail.smtp.host", "smtp.gmail.com");
                    props.put("mail.smtp.socketFactory.port", "465");
                    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.port", "465");*/
                    props.put("mail.smtp.host", "smtp.sparkpostmail.com");
                    props.put("mail.smtp.socketFactory.port", "587");
                    //props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.port", "587");

                    props.put("mail.smtp.starttls.enable", "true");


                    session = Session.getDefaultInstance(props, new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication("SMTP_Injection", "05f2f41332bd71ab936c2646455021e282b49415");
                            //return new PasswordAuthentication("centrossaude@gmail.com", "centrosdesaude2017");
                        }
                    });
                    //session = Session.getDefaultInstance(props);
                    RetreiveFeedTask task = new RetreiveFeedTask();
                    task.execute();
                }
            }
        });
    }

        class RetreiveFeedTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                try {
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress("contato@centrosdesaude.com.br"));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("mauricioat1902@gmail.com"));
                    message.setSubject(titulo);
                    message.setContent(mensagemTexto, "text/html; charset=utf-8");
                    Transport.send(message);
                } catch (MessagingException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                pdialog.dismiss();
                Toast.makeText(getApplicationContext(), "E-mail encaminhado com sucesso", Toast.LENGTH_LONG).show();
            }
        }
}