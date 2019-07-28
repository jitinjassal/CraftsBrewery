package com.example.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BeerAdapter extends RecyclerView.Adapter<BeerAdapter.ViewHolder> implements Filterable {


    private Context context;
    private List<Beers> beers;
    private List<Beers> beerListFiltered;
    private BeersAdapterListener listener;


    public BeerAdapter(Context context, List<Beers> beersList, BeersAdapterListener listener) {
        this.context = context;
        this.beers = beersList;
        this.listener = listener;
        this.beerListFiltered = beersList;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_beer_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(beers.get(position));

        Beers bu = beerListFiltered.get(position);

        holder.pName.setText(bu.getAbv()+" "+bu.getIbu());
        holder.pJobProfile.setText(bu.getId());
        holder.tvBName.setText(bu.getName());
        holder.tvBStyle.setText(bu.getStyle());
        holder.tvBOunces.setText(bu.getOunces());

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return beerListFiltered.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView pName;
        public TextView pJobProfile;
        public TextView tvBName;
        public TextView tvBStyle;
        public TextView tvBOunces;



        public ViewHolder(View itemView) {
            super(itemView);

            pName =  itemView.findViewById(R.id.pNametxt);
            pJobProfile =  itemView.findViewById(R.id.pJobProfiletxt);
            tvBName =  itemView.findViewById(R.id.tvName);
            tvBStyle =  itemView.findViewById(R.id.tvStyle);
            tvBOunces =  itemView.findViewById(R.id.tvOunces);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Beers bbu = (Beers) view.getTag();
                    listener.onBeerSelected(beerListFiltered.get(getAdapterPosition()));


                    Toast.makeText(view.getContext(), bbu.getAbv()+" "+bbu.getIbu()+" ID: "+ bbu.getId() + "  " +bbu.getName() +  "   " + bbu.getStyle() +  "    " +bbu.getOunces(), Toast.LENGTH_SHORT).show();

                }
            });

        }
    }



    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
//                    beerListFiltered = beers;
                } else {
                    List<Beers> filteredList = new ArrayList<>();
                    for (Beers row : beers) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match

                        Log.d("BeerAdapter", "getStyle : " + row.getStyle());

                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getName().contains(charSequence)) {
                            filteredList.add(row);
                        }

                        if (row.getStyle().toLowerCase().contains(charString.toLowerCase()) || row.getStyle().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    beerListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = beerListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                beerListFiltered = (ArrayList<Beers>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface BeersAdapterListener {
        void onBeerSelected(Beers beers);
    }

}

