package app.main;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class portfolio extends AppCompatActivity {
    private FirebaseAuth mAuth= FirebaseAuth.getInstance();
    private RecyclerView recyView;
    private String Tag ="portfolio";
    private String uid;
    private HashMap<String,Double> porti;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);
        mAuth = FirebaseAuth.getInstance();
        recyView = findViewById(R.id.recyView);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        assert currentUser != null;
        uid = currentUser.getUid();


        DatabaseReference database = FirebaseDatabase.getInstance(
                "“https://eportfo-5a2ad-default-rtdb.europe-west1.firebasedatabase.app").getReference("Portfolio");
        Query getWatch = database.child(uid);
        getWatch.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Portfo newPort = dataSnapshot.getValue(Portfo.class);
                if(newPort != null){
                    porti = newPort.portfolio;
                    setAdapter(porti);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(portfolio.this, "Failed to get information for your portfolio at this moment,\n " +
                        "please try again later", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void setAdapter(HashMap<String,Double> kovan) {
        recyclerportfolio adapter = new recyclerportfolio(kovan,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyView.setLayoutManager(layoutManager);
        recyView.setItemAnimator(new DefaultItemAnimator());
        recyView.setAdapter(adapter);
    }
}