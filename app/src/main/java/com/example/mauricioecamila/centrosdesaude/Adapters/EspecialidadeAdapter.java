package com.example.mauricioecamila.centrosdesaude.Adapters;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import com.example.mauricioecamila.centrosdesaude.Especialidade;

import java.util.ArrayList;
import java.util.List;

public class EspecialidadeAdapter extends ArrayAdapter<Especialidade> {

    Context context;
    int resource;
    List<Especialidade> items, tempItems, suggestions;

    public EspecialidadeAdapter(Context context, int resource, List<Especialidade> items) {
        super(context, resource, items);
        this.context = context;
        this.resource = resource;
        this.items = items;
        tempItems = new ArrayList<Especialidade>(items); // this makes the difference.
        suggestions = new ArrayList<Especialidade>();
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((String) resultValue);
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Especialidade especialidade : tempItems) {
                    if (especialidade.getNome().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(especialidade);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<Especialidade> filterList = (ArrayList<Especialidade>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Especialidade especialidade : filterList) {
                    add(especialidade);
                    notifyDataSetChanged();
                }
            }
        }
    };
}