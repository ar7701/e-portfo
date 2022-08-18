package app.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class HomePage extends AppCompatActivity {
    private FirebaseAuth mAuth= FirebaseAuth.getInstance();
    Button portfolio_b;
    Button search;
    Button crypto;
    Button stocks;
    Button profile2;
    String profile;
    Button logut;
    Button watchlist;
    String rezultat;
    HashMap<String, Double> portf;
    TextView nick;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        assert currentUser != null;
        uid = currentUser.getUid();
        nick=findViewById(R.id.nickname_home);


        if(uid.isEmpty()){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        search = findViewById(R.id.search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearch();
            }
        });

        crypto = findViewById(R.id.cryptod);

        crypto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTopCrypto();
            }
        });


        logut = findViewById(R.id.logout);
        logut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                BackHome();
            }
        });

        watchlist= findViewById(R.id.watch_list);
        watchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              watchl();
            }
        });
        portfolio_b=findViewById(R.id.port_view);

        portfolio_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPortf();
            }
        });


        DatabaseReference database = FirebaseDatabase.getInstance(
                "“https://eportfo-5a2ad-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users");
        Query getWatch = database.child(uid);
        getWatch.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //System.out.println(dataSnapshot);
                User newUser = dataSnapshot.getValue(User.class);
                if(newUser != null){
                    String ni = newUser.email;
                    nick.setText(ni);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(HomePage.this, "Failed to get information for your watchilist at this moment,\n please try again later", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void openSearch(){

        DatabaseReference database = FirebaseDatabase.getInstance(
                "“https://eportfo-5a2ad-default-rtdb.europe-west1.firebasedatabase.app").getReference("Watchlist");
        Query getWatch = database.child(uid);
        getWatch.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //System.out.println(dataSnapshot);
                Watch newWatch = dataSnapshot.getValue(Watch.class);
                if(newWatch != null){
                    rezultat = newWatch.watchlist;
                }

                DatabaseReference dadabase = FirebaseDatabase.getInstance(
                        "“https://eportfo-5a2ad-default-rtdb.europe-west1.firebasedatabase.app").getReference("Portfolio");
                Query getWatch = dadabase.child(uid);
                getWatch.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Portfo newPort = dataSnapshot.getValue(Portfo.class);
                        if(newPort != null){
                            portf = newPort.portfolio;

                            String names ="";
                            String name_value ="";
                            for (String x:portf.keySet()
                            ) {
                                names+=x+";";
                            }
                            names.substring(0,names.length()-1);
                            for (Double d:portf.values()
                            ) {
                                name_value+=d+";";
                            }
                            name_value.substring(0,name_value.length()-1);
                            //System.out.println(names);
                            //System.out.println(name_value);
                            Intent intent = new Intent(HomePage.this, search.class);
                            intent.putExtra("watchlist", rezultat);
                            intent.putExtra("names",names);
                            intent.putExtra("name_value",name_value);
                            startActivity(intent);

                        }
                        else{
                            String names ="";
                            String name_value ="";
                            Intent intent = new Intent(HomePage.this, search.class);
                            intent.putExtra("watchlist", rezultat);
                            intent.putExtra("names",names);
                            intent.putExtra("name_value",name_value);
                            startActivity(intent);

                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(HomePage.this, "Failed to get information for your watchilist at this moment,\n please try again later", Toast.LENGTH_SHORT).show();
                    }
                });

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(HomePage.this, "Failed to get information for your watchilist at this moment,\n please try again later", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void openTopCrypto(){
        Intent intent = new Intent(this, topCrypto.class);
        startActivity(intent);
    }
    public void BackHome(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void watchl(){
        Intent intent = new Intent(this, watchlist.class);
        startActivity(intent);
    }
    public void openPortf(){
        Intent intent = new Intent(this, portfolio.class);
        startActivity(intent);

    }
}