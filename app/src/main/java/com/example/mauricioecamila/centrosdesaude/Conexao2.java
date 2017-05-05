package com.example.mauricioecamila.centrosdesaude;


import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Conexao2 {

    public static String postDados(String urlUsuario, String parametrosUsuario){

        /*URLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(urlUsuario);
            connection = (HttpURLConnection)url.openConnection();
            String redirect = connection.getHeaderField("Location");
            if(redirect !=  null)
                connection = new URL(redirect).openConnection();
            //connection.connect();

            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line ="";

            while((line = reader.readLine()) != null){
                buffer.append(line);
                buffer.append('\r');
            }
            return buffer.toString();

        }catch(Exception erro){
            return "Erro Conexao: " + erro.toString();
        } finally {
            //Se connection estiver conectado, vai desconectar
            if(connection != null){
                //connection.disconnect();
            }
        }*/
        OkHttpClient client = new OkHttpClient();

        Request.Builder builder= new Request.Builder();

        builder.url(urlUsuario);

        //Tipo de dado enviado
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

        //Corpo
        RequestBody body = RequestBody.create(mediaType, parametrosUsuario);

        builder.post(body);

        //Request que obterá a resposta
        Request request = builder.build();
        try{
            Response response = client.newCall(request).execute();
            return response.body().toString();
        }catch (Exception erro){
            return "Erro Conexao:" + erro.toString();
        }
        /*
        OkHttpClient client = new OkHttpClient();
        Request.Builder builder1 = new Request.Builder();
        builder1.url(urlUsuario+parametrosUsuario);
        Request request = builder1.build();
        try{
            Response response = client.newCall(request).execute();
            return response.body().toString();
        }catch (Exception e){
            return "Erro Conexao:" + e.toString();
        }*/

    }

}
