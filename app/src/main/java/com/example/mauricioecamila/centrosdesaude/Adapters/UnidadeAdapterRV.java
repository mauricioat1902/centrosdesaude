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
import com.example.mauricioecamila.centrosdesaude.R;
import com.example.mauricioecamila.centrosdesaude.Unidade;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class UnidadeAdapterRV extends RecyclerView.Adapter<UnidadeAdapterRV.MyViewHolder> {

    private ArrayList<Unidade> unidades;
    private LayoutInflater layoutInflater;
    public final Context context;

    public UnidadeAdapterRV(Context context, ArrayList<Unidade> unidades) {
        this.unidades = unidades;
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

        myViewHolder.rbAvGeral.setRating(Float.parseFloat(unidades.get(position).getMdGeral().toString()));
        myViewHolder.nomeUnidade.setText(unidades.get(position).getNome());
        myViewHolder.endereco.setText(unidades.get(position).getLogradouro() + ", " + unidades.get(position).getNumero());

        myViewHolder.tvDistancia.setText(new DecimalFormat("0.0").format(unidades.get(position).getDistancia()).toString() + " Km");
        myViewHolder.llLinhaEst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.example.mauricioecamila.centrosdesaude.Activities.ActivityEstabelecimento");
                Bundle parametros = new Bundle();
                parametros.putLong("id",unidades.get(position).getId());
                parametros.putString("nome",unidades.get(position).getNome());
                parametros.putString("tipoUnidade", unidades.get(position).getTipoUnidade());
                parametros.putString("vinculoSus", unidades.get(position).getVinculoSus());

                parametros.putString("logradouro", unidades.get(position).getLogradouro());
                parametros.putInt("numero", unidades.get(position).getNumero());
                parametros.putString("bairro", unidades.get(position).getBairro());
                parametros.putString("cidade", unidades.get(position).getMunicipio());
                parametros.putLong("cep", unidades.get(position).getCep());
                parametros.putString("estado", unidades.get(position).getEstado());

                parametros.putString("latitude", unidades.get(position).getLatitude());
                parametros.putString("longitude", unidades.get(position).getLongitude());
                parametros.putDouble("distancia", unidades.get(position).getDistancia());
                parametros.putDouble("mediaGeral", unidades.get(position).getMdGeral());
                parametros.putDouble("mediaAtendimento", unidades.get(position).getMdAtendimento());
                parametros.putDouble("mediaEstrutura", unidades.get(position).getMdEstrutura());
                parametros.putDouble("mediaEquipamentos", unidades.get(position).getMdEquipamentos());
                parametros.putDouble("mediaLocalizacao", unidades.get(position).getMdLocalizacao());
                parametros.putDouble("mediaTempoAtendimento", unidades.get(position).getMdTempoAtendimento());


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
                String resposta = unidades.get(position).getLatitude();
                parametros.putString("latitude", resposta);
                resposta = unidades.get(position).getLongitude();
                parametros.putString("longitude", resposta);
                resposta = unidades.get(position).getNome();
                parametros.putString("tituloMarcador", resposta);

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
        return unidades.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nomeUnidade;
        TextView endereco;
        TextView tvDistancia;
        Button btnMap;
        RatingBar rbAvGeral;
        LinearLayout llLinhaEst;
        public MyViewHolder(View itemView){
            super(itemView);

            nomeUnidade = (TextView) itemView.findViewById(R.id.nomeUnidade);
            endereco = (TextView) itemView.findViewById(R.id.enderecoUnidade);
            tvDistancia = (TextView) itemView.findViewById(R.id.tvDistancia);
            btnMap = (Button) itemView.findViewById(R.id.btnMap);
            rbAvGeral = (RatingBar) itemView.findViewById(R.id.rbAvGeral);
            llLinhaEst = (LinearLayout) itemView.findViewById(R.id.llLinhaEst);
        }
    }

}
