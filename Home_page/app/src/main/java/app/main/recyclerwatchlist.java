package app.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class recyclerwatchlist extends RecyclerView.Adapter<recyclerwatchlist.MyViewHolder> {
    private Context context;
    private ArrayList<String> coinList;
    private String lmaoTest;

    public recyclerwatchlist(ArrayList<String> d, Context context){
        this.coinList=d;
        this.context=context;
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
    public recyclerwatchlist.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.watchlist_items,parent,false);
        return new MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(@NonNull recyclerwatchlist.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String coin= coinList.get(position);
        String link = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&ids=" + coin +  "&order=market_cap_desc&per_page=1&page=1&sparkline=false&price_change_percentage=1h%2C24h%2C7d";
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, link,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {

                        response = response.substring(1,response.length()-1);
                        //System.out.println(response);

                        Gson gson = new Gson();
                        Tcoin newCoin = gson.fromJson(response, Tcoin.class);
                        holder.rank.setText(newCoin.market_cap_rank);

                        if (newCoin.name.length()>18){
                            String l = newCoin.name.substring(0,16);
                            holder.name.setText(l);
                        }
                        else{
                            holder.name.setText(newCoin.name);
                        }

                        Picasso.get().load(newCoin.image).into(holder.icon);

                        double cena = Double.parseDouble(newCoin.current_price);

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
                            holder.price.setText("$" + newCoin.current_price.trim());
                        }

                        holder.syb.setText(newCoin.symbol.toUpperCase());

                        double h24 = Double.parseDouble(newCoin.price_change_percentage_24h_in_currency);

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

                        double mkt = Double.parseDouble(newCoin.market_cap);
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
                }, new Response.ErrorListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Rest in pepes");
            }
        });
        queue.add(stringRequest);

    }
    //Gson gson = new Gson();
    //Tcoin newCoin = gson.fromJson(response, Tcoin.class);


    @Override
    public int getItemCount() {

        return coinList.size();
    }

    private static class Tcoin {

        public String id;
        public String symbol;
        public String name;
        public String image;
        public String current_price;
        public String market_cap;
        public String market_cap_rank;
        public String total_volume;
        public String price_change_percentage_24h_in_currency;


    }
}
