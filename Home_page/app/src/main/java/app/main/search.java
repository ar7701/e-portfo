package app.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TimeZone;

public class search extends AppCompatActivity {
    private FirebaseAuth mAuth= FirebaseAuth.getInstance();
    String profile;
    Button search_price;
    EditText search_value;
    TextView curr_price_val;
    TextView h24chng;
    TextView d7chng;
    TextView mkt_cap;
    TextView symb;
    RelativeLayout rel;

    TextView mkt_rank;
    TextView volume;

    Button top_coins;
    TextView not_found;

    Button convert_btn;
    private String uid;

    ImageView portf;
    ImageButton watch;
    ImageButton remove_watch;
    ImageButton add_portf;
    ImageButton rem_portf;
    ImageView symb_icon;
    private DatabaseReference mDatabase;
    private Context context;
    HashMap<String,Double> portfolio= new HashMap<>();

    ArrayList<String> watchlist_list = new ArrayList<>();

    private Boolean added;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        this.context=context;

        Intent intent = getIntent();
        String x = intent.getStringExtra("watchlist");
        String names = intent.getStringExtra("names");
        String name_values= intent.getStringExtra("name_value");

        if(!names.isEmpty())
        {
            String[] named = names.split(";");
            String[] values = name_values.split(";");


            for(int a=0; a<named.length;a++){
                portfolio.put(named[a],Double.parseDouble(values[a]));
            }
        }

        if (!x.isEmpty()){
            String[] dd = x.split(";");
            System.out.println(Arrays.toString(dd));
            for (String d: dd
            ) {
                watchlist_list.add(d);
            }
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();
        not_found=findViewById(R.id.not_found);
        search_price = findViewById(R.id.search_price);
        search_value = findViewById(R.id.search_value);
        mkt_cap = findViewById(R.id.mkt_cap); //TUKAJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ
        h24chng = findViewById(R.id.h24chng);
        d7chng = findViewById(R.id.d7chng);
        curr_price_val = findViewById(R.id.curr_price_val);
        top_coins = findViewById(R.id.top_coins);
        symb = findViewById(R.id.symbols);

        watch = findViewById(R.id.add_watch);
        mkt_rank = findViewById(R.id.mkt_rank);
        volume = findViewById(R.id.volume);
        symb_icon = findViewById(R.id.symb_icon);
        remove_watch= findViewById(R.id.remove_wat);
        add_portf=findViewById(R.id.add_portf);


      //  Picasso.get().load("https://pngset.com/images/red-heart-icon-dark-border-red-heart-icon-cushion-transparent-png-1586153.png").into(watch);
      //  Picasso.get().load("https://pngset.com/images/gray-heart-5-icon-free-gray-heart-icons-heart-balloon-pillow-cushion-transparent-png-2501533.png").into(remove_watch);

        convert_btn = findViewById(R.id.convert_btn);

        top_coins = findViewById(R.id.top_coins);

        rel = findViewById(R.id.relativeLayout);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        assert currentUser != null;
        uid = currentUser.getUid();

        search_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String coin = search_value.getText().toString().toLowerCase();
                if(coin.isEmpty()){
                    rel.setVisibility(View.INVISIBLE);
                    search_value.setError("No input!");
                    search_value.requestFocus();
                    return;

                }

                if (watchlist_list.contains(coin)){
                    watch.setVisibility(View.INVISIBLE);
                    remove_watch.setVisibility(View.VISIBLE);
                }
                else {
                    remove_watch.setVisibility(View.INVISIBLE);
                    watch.setVisibility(View.VISIBLE);

                }

                if (portfolio.containsKey(coin)){
                    //already_has.setText(portfolio.get(coin));!!!!!!!!!!!!!
                    //DODAJ TEXTVIEW ZA HOW MUCH HE HAS
                }
                if (coin.length()!=0) {
                        getRequest();
                }

                getGraph();
            }
        });

        watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String coin = search_value.getText().toString().toLowerCase();
                watchlist_list.add(coin);
                String rez="";
                if (watchlist_list.size()==1){
                    rez+=watchlist_list.get(0).toString();
                   // rez+=";";
                }
                else{
                    for (String d: watchlist_list) {
                        rez+=d+";";
                    }
                    rez =rez.substring(0,rez.length()-1);
                }
                mDatabase.child("Watchlist")
                        .child(uid)
                        .child("watchlist")
                        .setValue(rez);
                watch.setVisibility(View.INVISIBLE);
                remove_watch.setVisibility(View.VISIBLE);
            }
        });
        remove_watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String coin = search_value.getText().toString().toLowerCase();
                watchlist_list.remove(coin);
                String rez = "";
                for (String d: watchlist_list
                ) {
                    rez+=d+";";

                }
                rez =rez.substring(0,rez.length()-1);

                mDatabase.child("Watchlist")
                        .child(uid)
                        .child("watchlist")
                        .setValue(rez);
                remove_watch.setVisibility(View.INVISIBLE);
                watch.setVisibility(View.VISIBLE);
            }
        });

        add_portf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_to_portfolio();
            }
        });

        convert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                konvert();
            }
        });

        top_coins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTopCoins();
            }
        });
    }
    private void konvert(){

        if(nameConv.isEmpty()){
            nameConv="bitcoin";
            symbolConv="BTC";
            priceConv="42784";
        }

        Intent intent = new Intent(this, convert.class);
        intent.putExtra("name", nameConv);
        intent.putExtra("symbol", symbolConv);
        intent.putExtra("price", priceConv);
        startActivity(intent);
    }
    private String nameConv="";
    private String symbolConv ="";
    private String priceConv ="";

    private void getRequest(){

        String coin = search_value.getText().toString().toLowerCase();
        String link = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&ids=" + coin +  "&order=market_cap_desc&per_page=1&page=1&sparkline=false&price_change_percentage=1h%2C24h%2C7d";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, link,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        if (response.length() >5) {
                            not_found.setVisibility(View.INVISIBLE);
                            add_portf.setVisibility(View.VISIBLE);


                            //remove extra symbols in json
                            response = response.substring(1, response.length() - 1);

                            //gson to json to Object
                            Gson gson = new Gson();
                            UCoin newCoin = gson.fromJson(response, UCoin.class);

                            rel.setVisibility(View.VISIBLE);

                            symbolConv = newCoin.symbol;;
                            nameConv = newCoin.name;;
                            priceConv = newCoin.price();;
                            Picasso.get().load(newCoin.image).into(symb_icon);

                            symb.setVisibility(View.VISIBLE);
                            symb.setText(newCoin.symbol.toUpperCase());
                            curr_price_val.setVisibility(View.VISIBLE);
                            curr_price_val.setText("Current price: $" + newCoin.price());

                            h24chng.setVisibility(View.VISIBLE);
                            double h24 = Double.parseDouble(newCoin.price24());
                            @SuppressLint("DefaultLocale") String h24h = String.format("%.2f", h24);
                            h24chng.setText("24h price change: " + h24h + "%");
                            if(h24>0){
                                h24chng.setTextColor(Color.parseColor("#2b911d"));
                            }
                            else{
                                h24chng.setTextColor(Color.parseColor("#f70713"));
                            }
                            d7chng.setVisibility(View.VISIBLE);
                            double d7 = Double.parseDouble(newCoin.price7d());
                            @SuppressLint("DefaultLocale") String d7d = String.format("%.2f", d7);
                            d7chng.setText("7d price change: " + d7d + "%");
                            if(d7>0){
                                d7chng.setTextColor(Color.parseColor("#2b911d"));
                            }
                            else{
                                d7chng.setTextColor(Color.parseColor("#f70713"));
                            }

                            double mkt = Double.parseDouble(newCoin.market_cap);
                            long volumed = Long.parseLong(newCoin.total_volume);

                            mkt_rank.setVisibility(View.VISIBLE);
                            mkt_rank.setText("Market cap rank: " + newCoin.market_cap_rank +".");

                            mkt_cap.setVisibility(View.VISIBLE);
                            //market cap double shortdown
                            if (mkt > 1000000000)
                            {
                                mkt/= 1000000000;
                                @SuppressLint("DefaultLocale") String market_c = String.format("%.2f", mkt);
                                mkt_cap.setText("Market cap: $" + market_c +"B");
                            }
                            else if (mkt > 1000000)
                            {
                                mkt/= 1000000;
                                @SuppressLint("DefaultLocale") String market_c = String.format("%.2f", mkt);
                                mkt_cap.setText("Market cap: $" + market_c +"M");
                            }
                            else {
                                mkt/= 1000;
                                @SuppressLint("DefaultLocale") String market_c = String.format("%.2f", mkt);
                                mkt_cap.setText("Market cap: $" + market_c +"T");
                            }

                            volume.setVisibility(View.VISIBLE);
                            //volume shortdown
                            if (volumed > 1000000000)
                            {
                                Double x= (double) (volumed / 1000000000);
                                @SuppressLint("DefaultLocale") String barket = String.format("%.2f", x);

                                volume.setText("Volume: $" + barket +"B");
                            }
                            else if (volumed > 1000000)
                            {
                                Double x= (double) (volumed / 1000000);
                                @SuppressLint("DefaultLocale") String barket = String.format("%.2f", x);
                                volume.setText("Volume: $" + barket +"M");
                            }
                            else {
                                Double x= (double) (volumed / 1000);
                                @SuppressLint("DefaultLocale") String barket = String.format("%.2f", x);
                                volume.setText("Volume: $" + barket +"t");
                            }
                        }
                        else{
                            //not_found.setVisibility(View.VISIBLE);
                            watch.setVisibility(View.INVISIBLE);
                            remove_watch.setVisibility(View.INVISIBLE);
                            add_portf.setVisibility(View.INVISIBLE);
                            rel.setVisibility(View.INVISIBLE);
                            search_value.setError("Are you sure this is your coin?");
                            search_value.requestFocus();
                            return;
                        }

                        convert_btn.setVisibility(View.VISIBLE);
                    }
                }, new Response.ErrorListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onErrorResponse(VolleyError error) {

                search_value.setError("Are you sure this is your coin?");
                search_value.requestFocus();
                return;
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void getGraph(){

        //URL https://api.coingecko.com/api/v3/coins/bitcoin/market_chart?vs_currency=usd&days=7&interval=4h
        String coin = search_value.getText().toString().toLowerCase();

        String link = "https://api.coingecko.com/api/v3/coins/" + coin + "/market_chart?vs_currency=usd&days=7&interval=daily";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, link,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {

                        Gson gson = new Gson();
                        Graf newGraph = gson.fromJson(response, Graf.class);

                        GraphView graph = (GraphView) findViewById(R.id.graph);
                        graph.setVisibility(View.VISIBLE);

                        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

                        for(int i=0; i< newGraph.prices.size();i++){

                            String[] podatek = newGraph.prices.get(i);

                            long unix = Long.parseLong(podatek[0]); //v milisekundah
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat jdf = new SimpleDateFormat("dd");
                            jdf.setTimeZone(TimeZone.getTimeZone("CEST"));
                            String java_date = jdf.format(unix);
                            int d = Integer.parseInt(java_date);

                            double cena = Double.parseDouble(podatek[1]);
                            series.appendData(new DataPoint(d,cena), true, 8);
                        }
                        graph.addSeries(series);
                        series.setTitle("7d " + coin + " price chart");
                        //series.setBackgroundColor(Color.); Nastavi barvo :)
                        //series.setColor();
                        series.setDrawDataPoints(true);
                        series.setThickness(8);
                        series.setDataPointsRadius(3);
                        graph.getGridLabelRenderer().setGridColor(Color.WHITE);
                        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
                        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);

                    }
                }, new Response.ErrorListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onErrorResponse(VolleyError error) {
                curr_price_val.setText("That didn't work!");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    private void add_to_portfolio(){

        String coin = search_value.getText().toString().toLowerCase();
        String link = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&ids=" + coin +  "&order=market_cap_desc&per_page=1&page=1&sparkline=false&price_change_percentage=1h%2C24h%2C7d";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, link,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        //System.out.println(response);
                        if (response.length() >5) {
                            not_found.setVisibility(View.INVISIBLE);
                            add_portf.setVisibility(View.VISIBLE);

                            //remove extra symbols in json
                            response = response.substring(1, response.length() - 1);

                            //gson to json to Object
                            Gson gson = new Gson();
                            UCoin newCoin = gson.fromJson(response, UCoin.class);


                            if (!portfolio.containsKey(coin)){
                                portfolio.put(coin,0.0);
                            }

                            String x = newCoin.symbol +";" + newCoin.name +";" + newCoin.current_price +";" + newCoin.image +";" +
                            coin+";"+ portfolio.get(coin);
                            System.out.println(x);
                            Intent intent = new Intent(search.this, Add_portfolio.class);
                            intent.putExtra("stringi" , x);
                            startActivity(intent);
                        }
                    }
                }, new Response.ErrorListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onErrorResponse(VolleyError error) {
                curr_price_val.setText("That didn't work!");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    public static class UCoin {
        public String symbol;
        public String name;
        public String current_price;
        public String market_cap;
        public String price_change_percentage_24h_in_currency;
        public String price_change_percentage_7d_in_currency;
        public String total_volume;
        public String market_cap_rank;
        public String image;

        public String price (){
            return current_price;
        }
        public String price24 (){
            return price_change_percentage_24h_in_currency;
        }
        public String price7d (){
            return price_change_percentage_7d_in_currency;
        }

    }

    private class Graf {
        public ArrayList<String[]> prices;
    }

    public void openTopCoins(){
        Intent intent = new Intent(this, topCrypto.class);
        intent.putExtra("profil", profile);
        startActivity(intent);
    }
}