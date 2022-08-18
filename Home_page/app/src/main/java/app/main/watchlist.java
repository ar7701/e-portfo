package app.main;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import java.util.ArrayList;
import java.util.Arrays;

public class watchlist extends AppCompatActivity {
    private FirebaseAuth mAuth= FirebaseAuth.getInstance();

    private RecyclerView recView;
    private String[] lista;
    private String Tag ="watchlist";
    private String uid;
    String rezultat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchlist);
        mAuth = FirebaseAuth.getInstance();
        recView = findViewById(R.id.recyView);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        assert currentUser != null;
        uid = currentUser.getUid();


        DatabaseReference database = FirebaseDatabase.getInstance(
                "“https://eportfo-5a2ad-default-rtdb.europe-west1.firebasedatabase.app").getReference("Watchlist");
        Query getWatch = database.child(uid);
        getWatch.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Watch newWatch = dataSnapshot.getValue(Watch.class);
                if(newWatch != null){
                    rezultat = newWatch.watchlist;
                    ArrayList<String> lista = new ArrayList<>() ;
                    if (!rezultat.isEmpty()){
                        String[] dd = rezultat.split(";");
                        lista = new ArrayList<>(Arrays.asList(dd));
                    }
                    setAdapter(lista);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(watchlist.this, "Failed to get information for your watchilist at this moment,\n please try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setAdapter(ArrayList<String> kovan) {
        recyclerwatchlist adapter = new recyclerwatchlist(kovan,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recView.setLayoutManager(layoutManager);
        recView.setItemAnimator(new DefaultItemAnimator());
        recView.setAdapter(adapter);
    }

}