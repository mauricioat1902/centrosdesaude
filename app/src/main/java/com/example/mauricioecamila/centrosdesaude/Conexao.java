package com.example.mauricioecamila.centrosdesaude;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Mauricio e Camila on 15/11/2016.
 */

public class Conexao {
    public static String postDados(String urlUsuario, String parametrosUsuario){
        URL url;
        HttpURLConnection connection = null;

        try{
            url = new URL(urlUsuario+parametrosUsuario);
            //Realizar a conexão
            connection = (HttpURLConnection)url.openConnection();

            connection.setRequestMethod("GET");
            //Tipo da informação, nesse caso está como formulário
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Lenght","" + Integer.toString(parametrosUsuario.getBytes().length));
            connection.setRequestProperty("Content-Language","pt-BR");

            //Não armezar os dados no aparelho, no cache
            connection.setUseCaches(false);
            //Habilitar entrada e saida de dados
            connection.setDoInput(true);
            connection.setDoOutput(true);

            /*
            //Pegamos os dados de saída e armezamos na conexão, escrevemos e finalizamos
            //Fez a solicitação
            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes(parametrosUsuario);
            dataOutputStream.flush();
            dataOutputStream.close();*/
            //Substitui o de cima
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            outputStreamWriter.write(parametrosUsuario);
            outputStreamWriter.flush();

            //Obtém os dados retornados da solicitação
            InputStream inputStream = connection.getInputStream();

            //Armazenar em buffer
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
            String linha;
            StringBuffer resposta = new StringBuffer();

            while((linha = bufferedReader.readLine()) != null){
                resposta.append(linha);
                resposta.append('\r');
            }
            bufferedReader.close();

            return resposta.toString();

        }catch(Exception erro){
            return "Erro Conexao: " + erro.toString();
        } finally {
            //Se connection estiver conectado, vai desconectar
            if(connection != null){
                connection.disconnect();
            }
        }
    }//postDados

}
