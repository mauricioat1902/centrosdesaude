package com.example.mauricioecamila.centrosdesaude.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.mauricioecamila.centrosdesaude.Avaliacao;
import com.example.mauricioecamila.centrosdesaude.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

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
        View rowView = inflater.inflate(R.layout.linha_comentario, parent, false);

        TextView tvNomeUsuario = (TextView)rowView.findViewById(R.id.tvNomeUsuario);
        tvNomeUsuario.setText(elementosAvalicoes.get(position).getNomeUsuario());
        System.out.println("--PASSOU: " + elementosAvalicoes.get(position).getId());
        System.out.println("--PASSOU2: " + elementosAvalicoes.get(position).getNomeUsuario());
        TextView tvData = (TextView)rowView.findViewById(R.id.tvData);
        /*Dateformat.SHORT // 03/04/10
        Dateformat.MEDIUM // 03/04/2010
        Dateformat.LONG //3 de Abril de 2010
        Dateformat.FULL //Sábado, 3 de Abril de 2010*/
        /*DateFormat usa um tipo como modelo, o SimpleDateFormat eu monto o modo que quero*/
        //DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        tvData.setText(sdf.format(elementosAvalicoes.get(position).getDataComentario()));

        RatingBar rbGeral = (RatingBar)rowView.findViewById(R.id.rbAvGeral);
        rbGeral.setRating(Float.parseFloat(elementosAvalicoes.get(position).mediaAvaliacoes().toString()));

        TextView tvComentario = (TextView)rowView.findViewById(R.id.tvComentario);
        tvComentario.setText(elementosAvalicoes.get(position).getComentario());

        RatingBar rbEstruturaCom = (RatingBar)rowView.findViewById(R.id.rbEstruturaCom);
        rbEstruturaCom.setRating(Float.parseFloat(elementosAvalicoes.get(position).getAvEstrutura().toString()));

        RatingBar rbAtendimentoCom = (RatingBar)rowView.findViewById(R.id.rbAtendimentoCom);
        rbAtendimentoCom.setRating(Float.parseFloat(elementosAvalicoes.get(position).getAvAtendimento().toString()));

        RatingBar rbEquipamentosCom = (RatingBar)rowView.findViewById(R.id.rbEquipamentosCom);
        rbEquipamentosCom.setRating(Float.parseFloat(elementosAvalicoes.get(position).getAvEquipamentos().toString()));

        RatingBar rbLocalizaçãoCom = (RatingBar)rowView.findViewById(R.id.rbLocalizaçãoCom);
        rbLocalizaçãoCom.setRating(Float.parseFloat(elementosAvalicoes.get(position).getAvLocalizacao().toString()));

        RatingBar rbTempoAtendimento = (RatingBar)rowView.findViewById(R.id.rbTempoAtendimento);
        rbTempoAtendimento.setRating(Float.parseFloat(elementosAvalicoes.get(position).getAvTempoAtendimento().toString()));

        return rowView;
    }
}
