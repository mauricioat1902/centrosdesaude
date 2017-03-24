package com.example.mauricioecamila.centrosdesaude.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.mauricioecamila.centrosdesaude.Estabelecimento;
import com.example.mauricioecamila.centrosdesaude.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Mauricio e Camila on 25/01/2017.
 */

public class EstabelecimentoAdapter extends ArrayAdapter<Estabelecimento> {

    private final Context context;
    private final ArrayList<Estabelecimento> elementos;
    public EstabelecimentoAdapter(Context context, ArrayList<Estabelecimento> elementos) {
        super(context, R.layout.linha, elementos);
        this.context = context;
        this.elementos = elementos;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.linha, parent, false);
        TextView nomeEstabelecimento = (TextView) rowView.findViewById(R.id.nomeEstabelecimento);
        TextView endereco = (TextView) rowView.findViewById(R.id.enderecoEstabelecimento);
        TextView tvDistancia = (TextView)rowView.findViewById(R.id.tvDistancia);
        Button btnMap = (Button)rowView.findViewById(R.id.btnMap);
        RatingBar rbAvGeral = (RatingBar)rowView.findViewById(R.id.rbAvGeral);
        rbAvGeral.setRating(Float.parseFloat(elementos.get(position).getMdGeral().toString()));

        nomeEstabelecimento.setText(elementos.get(position).getNome());
        endereco.setText(elementos.get(position).getlogradouro() + ", " + elementos.get(position).getNumero());
        LinearLayout llLinhaEst = (LinearLayout) rowView.findViewById(R.id.llLinhaEst);

        tvDistancia.setText(new DecimalFormat("0.0").format(elementos.get(position).getDistancia()).toString() + " Km");


        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.example.mauricioecamila.centrosdesaude.Activities.ActivityMapa");
                //Passando dados para a próxima Activity
                Bundle parametros = new Bundle();
                String resposta = elementos.get(position).getLatitude();
                parametros.putString("latitude", resposta);
                resposta = elementos.get(position).getLongitude();
                parametros.putString("longitude", resposta);

                intent.putExtras(parametros);
                context.startActivity(intent);
            }
        });

        llLinhaEst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.example.mauricioecamila.centrosdesaude.Activities.ActivityEstabelecimento");
                Bundle parametros = new Bundle();
                parametros.putLong("id",elementos.get(position).getId());
                parametros.putString("nome",elementos.get(position).getNome());
                parametros.putString("tipoEstabelecimento", elementos.get(position).getTipoEstabelecimento());
                parametros.putString("vinculoSus", elementos.get(position).getVinculoSus());
                parametros.putString("temAtendimentoUrgencia", elementos.get(position).getTemAtendimentoUrgencia());
                parametros.putString("temAtendimentoAmbulatorial", elementos.get(position).getTemAtendimentoAmbulatorial());
                parametros.putString("temCentroCirurgico", elementos.get(position).getTemCentroCirurgico());
                parametros.putString("temObstetra", elementos.get(position).getTemObstetra());
                parametros.putString("temNeoNatal", elementos.get(position).getTemNeoNatal());
                parametros.putString("temDialise", elementos.get(position).getTemDialise());
                parametros.putString("logradouro", elementos.get(position).getLogradouro());
                parametros.putString("numero", elementos.get(position).getNumero());
                parametros.putString("bairro", elementos.get(position).getBairro());
                parametros.putString("cidade", elementos.get(position).getCidade());
                parametros.putString("cep", elementos.get(position).getNuCep());
                parametros.putString("estado", elementos.get(position).getEstado());
                parametros.putString("telefone", elementos.get(position).getTelefone());
                parametros.putString("turnoAtendimento", elementos.get(position).getTurnoAtendimento());
                parametros.putString("latitude", elementos.get(position).getLatitude());
                parametros.putString("longitude", elementos.get(position).getLongitude());
                parametros.putDouble("distancia", elementos.get(position).getDistancia());
                parametros.putDouble("mediaGeral", elementos.get(position).getMdGeral());
                parametros.putDouble("mediaAtendimento", elementos.get(position).getMdAtendimento());
                parametros.putDouble("mediaEstrutura", elementos.get(position).getMdEstrutura());
                parametros.putDouble("mediaEquipamentos", elementos.get(position).getMdEquipamentos());
                parametros.putDouble("mediaLocalizacao", elementos.get(position).getMdLocalizacao());
                parametros.putDouble("mediaTempoAtendimento", elementos.get(position).getMdTempoAtendimento());


                intent.putExtras(parametros);
                context.startActivity(intent);
            }
        });

        return rowView;
    }
}
