package app.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class Add_portfolio extends AppCompatActivity {

    private FirebaseAuth mAuth= FirebaseAuth.getInstance();
    TextView symbol,crypto_name,curr_portf,curr_value;
    EditText amount;
    RadioButton buy,sell;
    double TOTAL_AMOUNT;
    Button enter;
    String coin;
    String uid;
    Button back;
    ImageView icon;

    private DatabaseReference mDatabase;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_portfolio);

        symbol=findViewById(R.id.Symbol);
        crypto_name=findViewById(R.id.crypto_id);
        curr_portf=findViewById(R.id.curr_portf);
        curr_value =findViewById(R.id.curr_amount);
        amount=findViewById(R.id.entr_amount);
        enter = findViewById(R.id.transaction);
        icon = findViewById(R.id.icon_p);
        back = findViewById(R.id.back);

        buy = findViewById(R.id.buy_radio);
        sell = findViewById(R.id.sell_radio);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        assert currentUser != null;
        uid = currentUser.getUid();

        Intent intent = getIntent();
        String x = intent.getStringExtra("stringi");
        String[] d =x.split(";");
        TOTAL_AMOUNT = Double.parseDouble(d[5]);
        coin = d[4];
        symbol.setText(d[0].toUpperCase());
        crypto_name.setText(d[1]);
        curr_value.setText( Double.toString(TOTAL_AMOUNT));
        Picasso.get().load(d[3]).into(icon);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String d = String.valueOf(amount.getText());
                if(d.isEmpty()){
                    amount.setError("No input detected!!");
                    amount.requestFocus();
                    return;
                }
                if (d.contains(",")){
                    amount.setError("Use . with a decimal number!");
                    amount.requestFocus();
                    return;
                }
                if(!d.matches("^[0-9]*.?[0-9]+$")){
                    amount.setError("Not a number!");
                    amount.requestFocus();
                    return;
                }
                double val = Double.parseDouble(d);

                if(val > TOTAL_AMOUNT && sell.isChecked()){
                    amount.setError("Amount higher than the value in your portfolio!!");
                    amount.requestFocus();
                    return;
                }


                if (buy.isChecked()){
                    DatabaseReference dadabase = FirebaseDatabase.getInstance(
                            "“https://eportfo-5a2ad-default-rtdb.europe-west1.firebasedatabase.app").getReference("Portfolio");
                    Query getWatch = dadabase.child(uid);
                    getWatch.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Portfo newPort = dataSnapshot.getValue(Portfo.class);
                            if(newPort != null){

                                HashMap<String,Double> c = newPort.portfolio;

                                if(!c.containsKey(coin)){
                                    c.put(coin,val);
                                }
                                else{
                                    c.computeIfPresent(coin,(k,v) -> v+val);
                                }
                                mDatabase.child("Portfolio")
                                        .child(uid)
                                        .child("portfolio")
                                        .setValue(c);
                                finish();

                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(Add_portfolio.this, "Failed to get information for your portfolio at this moment,\n please try again later", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    DatabaseReference dadabase = FirebaseDatabase.getInstance(
                            "“https://eportfo-5a2ad-default-rtdb.europe-west1.firebasedatabase.app").getReference("Portfolio");
                    Query getWatch = dadabase.child(uid);
                    getWatch.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Portfo newPort = dataSnapshot.getValue(Portfo.class);
                            if(newPort != null){

                                HashMap<String,Double> c = newPort.portfolio;


                                double d = c.get(coin) - val;

                                if (d ==0){
                                    c.remove(coin);
                                }
                                else {
                                    c.computeIfPresent(coin, (k, v) -> v - val);
                                }

                                mDatabase.child("Portfolio")
                                        .child(uid)
                                        .child("portfolio")
                                        .setValue(c);
                                finish();

                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(Add_portfolio.this, "Failed to get information for your portfolio at this moment,\n please try again later", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            finish();
            }
        });

    }

}