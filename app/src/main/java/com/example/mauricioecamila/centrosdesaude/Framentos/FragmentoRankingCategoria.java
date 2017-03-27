package com.example.mauricioecamila.centrosdesaude.Framentos;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mauricioecamila.centrosdesaude.Adapters.EstabelecimentoAdapterRV;
import com.example.mauricioecamila.centrosdesaude.Conexao;
import com.example.mauricioecamila.centrosdesaude.Estabelecimento;
import com.example.mauricioecamila.centrosdesaude.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class FragmentoRankingCategoria extends Fragment{

    private RecyclerView rvRankCategoria;
    private String url="";
    private String parametros="";
    private ArrayList<Estabelecimento> estabelecimentos;
    private ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dialog = new ProgressDialog(getActivity());
        dialog.setCancelable(true);
        dialog.setMessage("Carregando");
        dialog.show();
        rvRankCategoria = (RecyclerView)getActivity().findViewById(R.id.rvRankCategoria);
        ConnectivityManager connMgr = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()) {
            //Criar a URL
            url = "http://centrosdesaude.com.br/rankingGeral.php";
            //url = "http://localhost:8090/login/logar.php";
            parametros = "";
            new FragmentoRankingCategoria.SolicitaDados().execute(url);
        }else{
            Toast.makeText(getActivity(), "Nenhuma conexão foi detectada", Toast.LENGTH_LONG).show();
        }
        return inflater.inflate(R.layout.fragmento_rank_categoria,container,false);
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
                Toast.makeText(getActivity(),"Erro no retorno", Toast.LENGTH_LONG).show();
            }
            else {
                if (!resultado.isEmpty()) {

                    estabelecimentos = new ArrayList<Estabelecimento>();
                    try {
                        JSONObject jsonObject = new JSONObject(resultado);
                        JSONArray jsonArray = jsonObject.getJSONArray("RankingGeral");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            long id = Long.parseLong(jsonArray.getJSONObject(i).getString("idUnidade"));
                            String nome = jsonArray.getJSONObject(i).getString("nmFantasia");
                            String tipoEstabelecimento = jsonArray.getJSONObject(i).getString("nmTipoEstabelecimento");
                            String vinculoSus = jsonArray.getJSONObject(i).getString("vinculoSus");
                            String temAtendimentoUrgencia = jsonArray.getJSONObject(i).getString("temAtendimentoUrgencia");
                            String temAtendimentoAmbulatorial = jsonArray.getJSONObject(i).getString("temAtendimentoAmbulatorial");
                            String temCentroCirurgico = jsonArray.getJSONObject(i).getString("temCentroCirurgico");
                            String temObstetra = jsonArray.getJSONObject(i).getString("temObstetra");
                            String temNeoNatal = jsonArray.getJSONObject(i).getString("temNeoNatal");
                            String temDialise = jsonArray.getJSONObject(i).getString("temDialise");
                            String logradouro = jsonArray.getJSONObject(i).getString("logradouro");
                            String numero = jsonArray.getJSONObject(i).getString("numero").toString();
                            String bairro = jsonArray.getJSONObject(i).getString("bairro");
                            String cidade = jsonArray.getJSONObject(i).getString("cidade");
                            String nuCep = jsonArray.getJSONObject(i).getString("nuCep");
                            String estado = jsonArray.getJSONObject(i).getString("estado_siglaEstado");
                            String nuTelefone = jsonArray.getJSONObject(i).getString("nuTelefone");
                            String turnoAtendimento = jsonArray.getJSONObject(i).getString("nmTurnoAtendimentocol");
                            String latitude = jsonArray.getJSONObject(i).getString("lat");
                            String longitude = jsonArray.getJSONObject(i).getString("long");
                            Double mediaGeral = jsonArray.getJSONObject(i).getDouble("mediaGeral");
                            Double mediaAtendimento = jsonArray.getJSONObject(i).getDouble("mediaAtendimento");
                            Double mediaEstrutura = jsonArray.getJSONObject(i).getDouble("mediaEstrutura");
                            Double mediaEquipamentos = jsonArray.getJSONObject(i).getDouble("mediaEquipamentos");
                            Double mediaLocalizacao = jsonArray.getJSONObject(i).getDouble("mediaLocalizacao");
                            Double mediaTempoAtendimento = jsonArray.getJSONObject(i).getDouble("mediaTempoAtendimento");

                            Estabelecimento e = new Estabelecimento(id, nome, tipoEstabelecimento, vinculoSus, temAtendimentoUrgencia, temAtendimentoAmbulatorial,
                                    temCentroCirurgico, temObstetra, temNeoNatal, temDialise, logradouro, numero, bairro, cidade, nuCep, estado, nuTelefone,
                                    turnoAtendimento, latitude, longitude);
                            e.setMdGeral(mediaGeral);
                            e.setMdAtendimento(mediaAtendimento);
                            e.setMdEstrutura(mediaEstrutura);
                            e.setMdEquipamentos(mediaEquipamentos);
                            e.setMdLocalizacao(mediaLocalizacao);
                            e.setMdTempoAtendimento(mediaTempoAtendimento);

                            estabelecimentos.add(e);
                        }

                        EstabelecimentoAdapterRV adapter = new EstabelecimentoAdapterRV(getActivity(),estabelecimentos);
                        rvRankCategoria.setAdapter(adapter);
                        dialog.dismiss();
                    } catch (Exception e) {
                        dialog.dismiss();
                        System.out.print(e.toString());
                    }
                } else {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "Nenhum registro foi encontrado", Toast.LENGTH_LONG).show();
                }
            }
        }
    }//SolicitaDados
}
