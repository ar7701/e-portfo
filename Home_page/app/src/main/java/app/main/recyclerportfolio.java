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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class recyclerportfolio extends RecyclerView.Adapter<recyclerportfolio.MyViewHolder> {
    private Context context;
    //private ArrayList<String[]> coinList;
    private HashMap<String,Double> coinList;
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<Double> name_value = new ArrayList<>();
    private String lmaoTest;



    public recyclerportfolio(HashMap<String,Double> d, Context context){
        this.coinList=d;
        this.context=context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView rank;
        private TextView name;
        private TextView price;
        private TextView h24day;
        private TextView syb;
        private ImageView icon;
        private TextView cur_value;
        private TextView amt_cur;
        private ConstraintLayout tst;
        public MyViewHolder(final View view){
            super(view);
            rank=view.findViewById(R.id.rankd);
            syb = view.findViewById(R.id.symbod);
            name=view.findViewById(R.id.coin_named);
            price=view.findViewById(R.id.price_curd);
            h24day=view.findViewById(R.id.day_chngd);
            icon =view.findViewById(R.id.icon);
            cur_value= view.findViewById(R.id.valued);
            amt_cur = view.findViewById(R.id.amt_cry);
            tst= view.findViewById(R.id.tst);

            for (String x:coinList.keySet()
                 ) {
                names.add(x);
            }
            for (Double d:coinList.values()
            ) {
                name_value.add(d);
            }

        }
    }

    @NonNull
    @Override
    public recyclerportfolio.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.portfolio_items,parent,false);
        return new MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(@NonNull recyclerportfolio.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String coin = names.get(position);
        double value = name_value.get(position);


        if(value!=0) {
            String link = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&ids=" + coin + "&order=market_cap_desc&per_page=1&page=1&sparkline=false&price_change_percentage=1h%2C24h%2C7d";
            RequestQueue queue = Volley.newRequestQueue(context);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, link,
                    new Response.Listener<String>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onResponse(String response) {

                            response = response.substring(1, response.length() - 1);
                            //System.out.println(response);

                            Gson gson = new Gson();
                            Tcoin newCoin = gson.fromJson(response, Tcoin.class);
                            holder.rank.setText(newCoin.market_cap_rank);

                            if (newCoin.name.length() > 18) {
                                String l = newCoin.name.substring(0, 16);
                                holder.name.setText(l);
                            } else {
                                holder.name.setText(newCoin.name);
                            }

                            Picasso.get().load(newCoin.image).into(holder.icon);

                            double cena = Double.parseDouble(newCoin.current_price);
                            double izracun = cena * value;


                            String iz = String.valueOf(izracun);
                            if(iz.matches("^[0-9]*.[0-9]+$")){
                                DecimalFormat df = new DecimalFormat("#.#");
                                iz = df.format(izracun);
                            }

                            holder.cur_value.setText("$" + iz);
                            holder.amt_cur.setText(value + " " + newCoin.symbol.toUpperCase());


                            String c = Double.toString(Math.abs(cena));

                            int integerPlaces = c.indexOf('.');
                            int decimalPlaces = c.length() - integerPlaces - 1;
                            if (decimalPlaces == 9) {
                                @SuppressLint("DefaultLocale") String p = String.format("%.6f", cena);
                                holder.price.setText("$" + p);
                            } else if (decimalPlaces == 8) {
                                @SuppressLint("DefaultLocale") String p = String.format("%.5f", cena);
                                holder.price.setText("$" + p);
                            } else if (decimalPlaces == 7) {
                                @SuppressLint("DefaultLocale") String p = String.format("%.5f", cena);
                                holder.price.setText("$" + p);
                            } else if (decimalPlaces == 6) {
                                @SuppressLint("DefaultLocale") String p = String.format("%.4f", cena);
                                holder.price.setText("$" + p);
                            } else if (decimalPlaces == 5) {
                                @SuppressLint("DefaultLocale") String p = String.format("%.3f", cena);
                                holder.price.setText("$" + p);
                            } else {
                                holder.price.setText("$" + newCoin.current_price.trim());
                            }

                            holder.syb.setText(newCoin.symbol.toUpperCase());

                            double h24 = Double.parseDouble(newCoin.price_change_percentage_24h_in_currency);

                            if (h24 < 0) {
                                @SuppressLint("DefaultLocale") String h24h = String.format("%.2f", h24);
                                String d = h24h + "%";
                                //h24chng.setText("24h price change: " + );
                                holder.h24day.setText(h24h + "%");
                                holder.h24day.setTextColor(Color.parseColor("#f70713"));
                            } else {
                                @SuppressLint("DefaultLocale") String h24h = String.format("%.2f", h24);
                                String d = " " + h24h + "%";
                                holder.h24day.setText(d);
                                holder.h24day.setTextColor(Color.parseColor("#2b911d"));
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
        else{
            holder.rank.setVisibility(View.INVISIBLE);
            holder.syb.setVisibility(View.INVISIBLE);
            holder.name.setVisibility(View.INVISIBLE);
            holder.price.setVisibility(View.INVISIBLE);
            holder.h24day.setVisibility(View.INVISIBLE);
            holder.icon .setVisibility(View.INVISIBLE);
            holder.cur_value.setVisibility(View.INVISIBLE);
            holder.amt_cur .setVisibility(View.INVISIBLE);
            holder.tst .setVisibility(View.INVISIBLE);

        }
    }


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
        public String market_cap_rank;
        public String price_change_percentage_24h_in_currency;


    }
}
