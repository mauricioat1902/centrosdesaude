package com.example.mauricioecamila.centrosdesaude.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.mauricioecamila.centrosdesaude.Estabelecimento;
import com.example.mauricioecamila.centrosdesaude.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class EstabelecimentoAdapterRV extends RecyclerView.Adapter<EstabelecimentoAdapterRV.MyViewHolder> {

    private ArrayList<Estabelecimento> estabelecimentos;
    private LayoutInflater layoutInflater;
    public final Context context;

    public EstabelecimentoAdapterRV(Context context, ArrayList<Estabelecimento> estabelecimentos) {
        this.estabelecimentos = estabelecimentos;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = layoutInflater.inflate(R.layout.linha, parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, final int position) {

        myViewHolder.rbAvGeral.setRating(Float.parseFloat(estabelecimentos.get(position).getMdGeral().toString()));
        myViewHolder.nomeEstabelecimento.setText(estabelecimentos.get(position).getNome());
        myViewHolder.endereco.setText(estabelecimentos.get(position).getlogradouro() + ", " + estabelecimentos.get(position).getNumero());

        myViewHolder.tvDistancia.setText(new DecimalFormat("0.0").format(estabelecimentos.get(position).getDistancia()).toString() + " Km");
        myViewHolder.llLinhaEst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.example.mauricioecamila.centrosdesaude.Activities.ActivityEstabelecimento");
                Bundle parametros = new Bundle();
                parametros.putLong("id",estabelecimentos.get(position).getId());
                parametros.putString("nome",estabelecimentos.get(position).getNome());
                parametros.putString("tipoEstabelecimento", estabelecimentos.get(position).getTipoEstabelecimento());
                parametros.putString("vinculoSus", estabelecimentos.get(position).getVinculoSus());
                parametros.putString("temAtendimentoUrgencia", estabelecimentos.get(position).getTemAtendimentoUrgencia());
                parametros.putString("temAtendimentoAmbulatorial", estabelecimentos.get(position).getTemAtendimentoAmbulatorial());
                parametros.putString("temCentroCirurgico", estabelecimentos.get(position).getTemCentroCirurgico());
                parametros.putString("temObstetra", estabelecimentos.get(position).getTemObstetra());
                parametros.putString("temNeoNatal", estabelecimentos.get(position).getTemNeoNatal());
                parametros.putString("temDialise", estabelecimentos.get(position).getTemDialise());
                parametros.putString("logradouro", estabelecimentos.get(position).getLogradouro());
                parametros.putString("numero", estabelecimentos.get(position).getNumero());
                parametros.putString("bairro", estabelecimentos.get(position).getBairro());
                parametros.putString("cidade", estabelecimentos.get(position).getCidade());
                parametros.putString("cep", estabelecimentos.get(position).getNuCep());
                parametros.putString("estado", estabelecimentos.get(position).getEstado());
                parametros.putString("telefone", estabelecimentos.get(position).getTelefone());
                parametros.putString("turnoAtendimento", estabelecimentos.get(position).getTurnoAtendimento());
                parametros.putString("latitude", estabelecimentos.get(position).getLatitude());
                parametros.putString("longitude", estabelecimentos.get(position).getLongitude());
                parametros.putDouble("distancia", estabelecimentos.get(position).getDistancia());
                parametros.putDouble("mediaGeral", estabelecimentos.get(position).getMdGeral());
                parametros.putDouble("mediaAtendimento", estabelecimentos.get(position).getMdAtendimento());
                parametros.putDouble("mediaEstrutura", estabelecimentos.get(position).getMdEstrutura());
                parametros.putDouble("mediaEquipamentos", estabelecimentos.get(position).getMdEquipamentos());
                parametros.putDouble("mediaLocalizacao", estabelecimentos.get(position).getMdLocalizacao());
                parametros.putDouble("mediaTempoAtendimento", estabelecimentos.get(position).getMdTempoAtendimento());


                intent.putExtras(parametros);
                context.startActivity(intent);
            }
        });

        myViewHolder.btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.example.mauricioecamila.centrosdesaude.Activities.ActivityMapa");
                //Passando dados para a próxima Activity
                Bundle parametros = new Bundle();
                String resposta = estabelecimentos.get(position).getLatitude();
                parametros.putString("latitude", resposta);
                resposta = estabelecimentos.get(position).getLongitude();
                parametros.putString("longitude", resposta);

                intent.putExtras(parametros);
                context.startActivity(intent);
            }
        });

        try{
            YoYo.with(Techniques.FlipInX)
                    .duration(700)
                    .repeat(1)
                    .playOn(myViewHolder.itemView);
        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return estabelecimentos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nomeEstabelecimento;
        TextView endereco;
        TextView tvDistancia;
        Button btnMap;
        RatingBar rbAvGeral;
        LinearLayout llLinhaEst;
        public MyViewHolder(View itemView){
            super(itemView);

            nomeEstabelecimento = (TextView) itemView.findViewById(R.id.nomeEstabelecimento);
            endereco = (TextView) itemView.findViewById(R.id.enderecoEstabelecimento);
            tvDistancia = (TextView) itemView.findViewById(R.id.tvDistancia);
            btnMap = (Button) itemView.findViewById(R.id.btnMap);
            rbAvGeral = (RatingBar) itemView.findViewById(R.id.rbAvGeral);
            llLinhaEst = (LinearLayout) itemView.findViewById(R.id.llLinhaEst);
        }
    }

}
