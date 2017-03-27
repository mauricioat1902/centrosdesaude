package com.example.mauricioecamila.centrosdesaude.Framentos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mauricioecamila.centrosdesaude.R;


public class FragmentoRankingGeral extends Fragment{
    private RecyclerView rvRankGeral;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmento_rank_geral,container,false);
    }
}
