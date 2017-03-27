package com.example.mauricioecamila.centrosdesaude.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.mauricioecamila.centrosdesaude.Framentos.FragmentoRankingCategoria;
import com.example.mauricioecamila.centrosdesaude.Framentos.FragmentoRankingGeral;

public class FragmentoPagerAdapter extends FragmentPagerAdapter {

    private String[] tabTitulos;

    public FragmentoPagerAdapter(FragmentManager fm, String[] tabTitulos) {
        super(fm);
        this.tabTitulos = tabTitulos;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new FragmentoRankingGeral();
            case 1:
                return new FragmentoRankingCategoria();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return this.tabTitulos.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return this.tabTitulos[position];
    }

}
