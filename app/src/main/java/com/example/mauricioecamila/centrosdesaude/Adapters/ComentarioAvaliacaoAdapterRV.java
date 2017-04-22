package com.example.mauricioecamila.centrosdesaude.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.mauricioecamila.centrosdesaude.Avaliacao;
import com.example.mauricioecamila.centrosdesaude.R;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ComentarioAvaliacaoAdapterRV extends RecyclerView.Adapter<ComentarioAvaliacaoAdapterRV.MyViewHolder> {

    private LayoutInflater layoutInflater;
    private final Context context;
    private ArrayList<Avaliacao> avaliacoes;

    public ComentarioAvaliacaoAdapterRV(Context context, ArrayList<Avaliacao> avaliacoes) {
        this.avaliacoes = avaliacoes;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = layoutInflater.inflate(R.layout.linha_comentario, parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, final int position) {

        myViewHolder.tvNomeUsuario.setText(avaliacoes.get(position).getNomeUsuario());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        myViewHolder.tvData.setText(sdf.format(avaliacoes.get(position).getDataComentario()));

        myViewHolder.rbGeral.setRating(Float.parseFloat(avaliacoes.get(position).mediaAvaliacoes().toString()));

        String comentario2= "nao deu certo";
        //String comentario = new String(avaliacoes.get(position).getComentario(), Charset.forName("UTF-8"));
        try {
            comentario2 = new String((avaliacoes.get(position).getComentario().getBytes()) ,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        myViewHolder.tvComentario.setText(comentario2);

        myViewHolder.rbEstruturaCom.setRating(Float.parseFloat(avaliacoes.get(position).getAvEstrutura().toString()));
        myViewHolder.rbAtendimentoCom.setRating(Float.parseFloat(avaliacoes.get(position).getAvAtendimento().toString()));
        myViewHolder.rbEquipamentosCom.setRating(Float.parseFloat(avaliacoes.get(position).getAvEquipamentos().toString()));
        myViewHolder.rbLocalizaçãoCom.setRating(Float.parseFloat(avaliacoes.get(position).getAvLocalizacao().toString()));
        myViewHolder.rbTempoAtendimento.setRating(Float.parseFloat(avaliacoes.get(position).getAvTempoAtendimento().toString()));



    }

    @Override
    public int getItemCount() {
        return avaliacoes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvNomeUsuario;
        TextView tvData;
        RatingBar rbGeral;
        TextView tvComentario;
        RatingBar rbEstruturaCom;
        RatingBar rbAtendimentoCom;
        RatingBar rbEquipamentosCom;
        RatingBar rbLocalizaçãoCom;
        RatingBar rbTempoAtendimento;

        public MyViewHolder(View itemView){
            super(itemView);

            tvNomeUsuario = (TextView) itemView.findViewById(R.id.tvNomeUsuario);
            tvData = (TextView) itemView.findViewById(R.id.tvData);
            rbGeral = (RatingBar) itemView.findViewById(R.id.rbAvGeral);
            tvComentario = (TextView)itemView.findViewById(R.id.tvComentario);
            rbEstruturaCom = (RatingBar) itemView.findViewById(R.id.rbEstruturaCom);
            rbAtendimentoCom = (RatingBar) itemView.findViewById(R.id.rbAtendimentoCom);
            rbEquipamentosCom = (RatingBar) itemView.findViewById(R.id.rbEquipamentosCom);
            rbLocalizaçãoCom = (RatingBar) itemView.findViewById(R.id.rbLocalizaçãoCom);
            rbTempoAtendimento = (RatingBar) itemView.findViewById(R.id.rbTempoAtendimento);

        }
    }

}
