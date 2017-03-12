package com.example.mauricioecamila.centrosdesaude;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mauricio e Camila on 06/03/2017.
 */

public class AvaliacaoAdapter extends ArrayAdapter<Avaliacao> {

    private final Context context;
    private final ArrayList<Avaliacao> elementosAvalicoes;

    public AvaliacaoAdapter(Context context,  ArrayList<Avaliacao> elementosAvalicoes) {
        super(context, R.layout.linha_comentario, elementosAvalicoes);
        this.context = context;
        this.elementosAvalicoes = elementosAvalicoes;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.linha, parent, false);

        TextView tvNomeUsuario = (TextView)rowView.findViewById(R.id.tvNomeUsuario);
        tvNomeUsuario.setText(elementosAvalicoes.get(position).getNomeUsuario());

        TextView tvComentario = (TextView)rowView.findViewById(R.id.tvComentario);
        tvComentario.setText(elementosAvalicoes.get(position).getComentario());

        RatingBar rbEstruturaCom = (RatingBar)rowView.findViewById(R.id.rbEstruturaCom);
        RatingBar rbAtendimentoCom = (RatingBar)rowView.findViewById(R.id.rbAtendimentoCom);
        RatingBar rbEquipamentosCom = (RatingBar)rowView.findViewById(R.id.rbEquipamentosCom);
        RatingBar rbLocalizaçãoCom = (RatingBar)rowView.findViewById(R.id.rbLocalizaçãoCom);
        RatingBar rbTempoAtendimento = (RatingBar)rowView.findViewById(R.id.rbTempoAtendimento);

        TextView tvData = (TextView)rowView.findViewById(R.id.tvData);
        tvData.setText(elementosAvalicoes.get(position).getDataComentario().toString());






        return rowView;
    }
}
