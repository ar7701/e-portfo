package app.main;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;

public class topCrypto extends AppCompatActivity {

    //String[] testCoins = {"bitcoin", "litecoin", "tether","bitcoin", "ethereum", "litecoin", "tether","bitcoin", "ethereum", "litecoin", "tether"};

    public static ArrayList<String[]> kovanci100;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_crypto);
        recyclerView = findViewById(R.id.recyView);

        getRequest();
    }

    private void getRequest(){
        String link = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_" +
                "desc&per_page=100&page=1&sparkline=false&price_change_percentage=24h%2C7d";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, link,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        kovanci100 = new ArrayList<>();
                        response = response.substring(2,response.length()-1);
                        String[] posamezni = response.split("\\},\\{");
                        for (int i=0; i<100;i++){
                            String[] pos = new String[7];
                            String d ="";
                            if (i<99){
                                d = "{" + posamezni[i]+ "}";
                            }
                            else {
                                d = "{" + posamezni[i];
                            }
                            Gson gson = new Gson();
                            Tcoin newCoin = gson.fromJson(d, Tcoin.class);
                            pos[0] = newCoin.market_cap_rank;
                            pos[1] = newCoin.name;
                            pos[2] = newCoin.symbol;
                            pos[3] = newCoin.current_price;
                            pos[4] = newCoin.price_change_percentage_24h_in_currency;
                            pos[5] = newCoin.market_cap;
                            pos[6] = newCoin.image;

                            kovanci100.add(pos);
                        }
                        setAdapter(kovanci100);
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


    private void setAdapter(ArrayList<String[]> kovan) {
        //System.out.println(kovan.size());
        recyclerAdapter adapter = new recyclerAdapter(kovan);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private static class Tcoin {

        public String symbol;
        public String name;
        public String current_price;
        public String market_cap;
        public String price_change_percentage_24h_in_currency;
        public String market_cap_rank;
        public String image;


    }

}