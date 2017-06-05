package com.example.mauricioecamila.centrosdesaude.Framentos;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mauricioecamila.centrosdesaude.Adapters.UnidadeAdapterRV;
import com.example.mauricioecamila.centrosdesaude.Conexao;
import com.example.mauricioecamila.centrosdesaude.R;
import com.example.mauricioecamila.centrosdesaude.Unidade;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class FragmentoRankingGeral extends Fragment{
    private RecyclerView rvRankGeral;
    private String url="";
    private String parametros="";
    private ArrayList<Unidade> unidades;
    private ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragmento_rank_geral, container, false);



            rvRankGeral = (RecyclerView) view.findViewById(R.id.rvRankGeral);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rvRankGeral.setLayoutManager(layoutManager);


            return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dialog = new ProgressDialog(getActivity());
        dialog.setCancelable(true);
        dialog.setMessage("Carregando");
        dialog.show();

        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //Criar a URL
            url = "http://centrosdesaude.com.br/app/rankingGeral.php";
            //url = "http://192.168.0.31:8090/rankingGeral.php";
            parametros = "";
            new FragmentoRankingGeral.SolicitaDados().execute(url);
        } else {
            dialog.dismiss();
            Toast.makeText(getActivity(), "Nenhuma conexão foi detectada", Toast.LENGTH_LONG).show();
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

            if(resultado.contains("Erro Conexao:")){
                dialog.dismiss();
                Toast.makeText(getActivity(),"Erro no retorno: " + resultado, Toast.LENGTH_LONG).show();
            }
            else {
                if (!resultado.isEmpty()) {
                    if(resultado.contains("Não houve retorno")){
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "Nenhum registro foi encontrado", Toast.LENGTH_LONG).show();
                    }else {
                        unidades = new ArrayList<Unidade>();
                        try {
                            JSONObject jsonObject = new JSONObject(resultado);
                            JSONArray jsonArray = jsonObject.getJSONArray("RankingGeral");
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
                                Double distancia = jsonArray.getJSONObject(i).getDouble("distancia");
                                Double mediaGeral = jsonArray.getJSONObject(i).getDouble("mediaGeral");
                                Double mediaAtendimento = jsonArray.getJSONObject(i).getDouble("mediaAtendimento");
                                Double mediaEstrutura = jsonArray.getJSONObject(i).getDouble("mediaEstrutura");
                                Double mediaEquipamentos = jsonArray.getJSONObject(i).getDouble("mediaEquipamentos");
                                Double mediaLocalizacao = jsonArray.getJSONObject(i).getDouble("mediaLocalizacao");
                                Double mediaTempoAtendimento = jsonArray.getJSONObject(i).getDouble("mediaTempoAtendimento");

                                Unidade un = new Unidade(id, nome, logradouro, numero, bairro, municipio, cep, estado, latitude, longitude,
                                        distancia, mediaAtendimento, mediaEstrutura, mediaEquipamentos, mediaLocalizacao, mediaTempoAtendimento, mediaGeral, tipoUnidade);
                                un.setPosicaoRank(i + 1);
                                unidades.add(un);
                            }
                            UnidadeAdapterRV adapterRV = new UnidadeAdapterRV(getActivity(), unidades);
                            rvRankGeral.setAdapter(adapterRV);
                            dialog.dismiss();
                        } catch (Exception e) {
                            dialog.dismiss();
                            System.out.print("ERRO: " + e.toString());
                        }
                    }
                } else {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "Nenhum registro foi encontrado", Toast.LENGTH_LONG).show();
                }
            }
        }
    }//SolicitaDados
}
