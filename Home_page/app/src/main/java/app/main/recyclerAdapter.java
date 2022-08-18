package app.main;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder> {

    //private ArrayList<String[]> coinList;
    private ArrayList<String[]> coinList;

    public recyclerAdapter(ArrayList<String[]> d){
        this.coinList=d;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView rank;
        private TextView name;
        private TextView price;
        private TextView h24day;
        private TextView syb;
        private TextView marketcap;
        private ImageView icon;
        public MyViewHolder(final View view){
            super(view);
            rank=view.findViewById(R.id.rankd);
            syb = view.findViewById(R.id.symbod);
            name=view.findViewById(R.id.coin_named);
            price=view.findViewById(R.id.price_curd);
            h24day=view.findViewById(R.id.day_chngd);
            marketcap=view.findViewById(R.id.valued);
            icon =view.findViewById(R.id.icon);
        }
    }

    @NonNull
    @Override
    public recyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.cryptotop_items,parent,false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull recyclerAdapter.MyViewHolder holder, int position) {
        holder.rank.setText(coinList.get(position)[0]);

        if (coinList.get(position)[1].length()>18){
            String l = coinList.get(position)[1].substring(0,16);
            holder.name.setText(l);
        }
        else{
            holder.name.setText(coinList.get(position)[1]);
        }

        Picasso.get().load(coinList.get(position)[6]).into(holder.icon);

        double cena = Double.parseDouble(coinList.get(position)[3]);

        String c = Double.toString(Math.abs(cena));

        int integerPlaces = c.indexOf('.');
        int decimalPlaces = c.length() - integerPlaces-1;
        if (decimalPlaces == 9){
            @SuppressLint("DefaultLocale") String p = String.format("%.6f", cena);
            holder.price.setText("$" + p);
        }
        else if (decimalPlaces == 8){
            @SuppressLint("DefaultLocale") String p = String.format("%.5f", cena);
            holder.price.setText("$" + p);
        }
        else if (decimalPlaces == 7){
            @SuppressLint("DefaultLocale") String p = String.format("%.5f", cena);
            holder.price.setText("$" + p);
        }
        else if (decimalPlaces == 6){
            @SuppressLint("DefaultLocale") String p = String.format("%.4f", cena);
            holder.price.setText("$" + p);
        }
        else if (decimalPlaces == 5){
            @SuppressLint("DefaultLocale") String p = String.format("%.3f", cena);
            holder.price.setText("$" + p);
        }
        else {
            holder.price.setText("$" + coinList.get(position)[3].trim());
        }

        holder.syb.setText(coinList.get(position)[2].toUpperCase());

        double h24 = Double.parseDouble(coinList.get(position)[4]);

        if(h24 <0){
            @SuppressLint("DefaultLocale") String h24h = String.format("%.2f", h24);
            String d=h24h + "%";
            //h24chng.setText("24h price change: " + );
            holder.h24day.setText(h24h + "%");
            holder.h24day.setTextColor(Color.parseColor("#f70713"));
        }
        else{
            @SuppressLint("DefaultLocale") String h24h = String.format("%.2f", h24);
            String d=" " +h24h + "%";
            holder.h24day.setText(d);
            holder.h24day.setTextColor(Color.parseColor("#2b911d"));
        }

        double mkt = Double.parseDouble(coinList.get(position)[5]);
        if (mkt > 1000000000)
        {
            mkt/= 1000000000;
            @SuppressLint("DefaultLocale") String market_c = String.format("%.2f", mkt);
            holder.marketcap.setText( "$"+market_c +"B");
        }
        else if (mkt > 1000000)
        {
            mkt/= 1000000;
            @SuppressLint("DefaultLocale") String market_c = String.format("%.2f", mkt);
            holder.marketcap.setText("$"+market_c +"M");
        }
        else {
            mkt/= 1000;
            @SuppressLint("DefaultLocale") String market_c = String.format("%.2f", mkt);
            holder.marketcap.setText("$"+market_c +"t");
        }
    }

    @Override
    public int getItemCount() {

        return coinList.size();
    }
}
