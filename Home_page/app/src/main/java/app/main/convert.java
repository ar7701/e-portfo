package app.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class convert extends AppCompatActivity {
    Button back;

    String symbol;
    String price;
    String name;
    boolean state = false;
    TextView curr1;
    TextView curr2;
    TextView rate;
    Button conversion;
    EditText amount;
    Button swap;

    TextView revult;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert);
        back = findViewById(R.id.backbtn);
        swap = findViewById(R.id.swap);

        curr1 = findViewById(R.id.currency1);
        curr2 = findViewById(R.id.currency2);

        rate = findViewById(R.id.curr_rate);

        conversion = findViewById(R.id.actual_convert);
        amount = findViewById(R.id.amount_cur);
        revult = findViewById(R.id.rezultat_conv);




        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            symbol = extras.getString("symbol");
            name = extras.getString("name");
            price = extras.getString("price");
        }
        String rated = "1 " + symbol + " = " + price + "USD";
        rate.setText(rated);

        String s = symbol+" -> USD";
        swap.setText(s);
        curr1.setText(name +":");


        swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!state){
                    String s = "USD -> " + symbol;
                    swap.setText(s);
                    String x = (String) curr2.getText();
                    curr2.setText(curr1.getText());
                    curr1.setText(x);
                    state= !state;
                }
                else{
                    String s = symbol+" -> USD";
                    swap.setText(s);
                    String x = (String) curr2.getText();
                    curr2.setText(curr1.getText());
                    curr1.setText(x);
                    state= !state;
                }


            }
        });


        conversion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!state){
                    double CURp = Double.parseDouble(price);
                    String amt = amount.getText().toString();
                    double amounted = Double.parseDouble(amt);

                    double calculation =CURp * amounted;
                    String output= String.valueOf(calculation);

                    revult.setText(output);
                }
                else{
                    double CURp = Double.parseDouble(price);
                    String amt = amount.getText().toString();
                    double amounted = Double.parseDouble(amt);

                    double calculation =amounted/CURp ;

                    if (calculation < 1){
                        @SuppressLint("DefaultLocale") String calc = String.format("%.6f", calculation);
                        revult.setText(calc);
                    }
                    else{
                        String output= String.valueOf(calculation);
                        revult.setText(output);
                    }

                }

            }
        });


        back.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finish();
                                    }
                                }
        );




    }
}