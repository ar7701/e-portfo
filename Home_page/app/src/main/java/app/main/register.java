package app.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class register extends AppCompatActivity {
    private Button register;
    private EditText nickname;
    private EditText email;
    private EditText password;
    private FirebaseAuth mAuth= FirebaseAuth.getInstance();
    private static final String TAG = "register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        register= (Button) findViewById(R.id.reg_reg);
        nickname = (EditText) findViewById(R.id.nickname);
        email = (EditText) findViewById(R.id.email_reg);
        password = (EditText) findViewById(R.id.pass_reg);

        mAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nick = nickname.getText().toString().trim();
                String em = email.getText().toString().trim();
                String pass = password.getText().toString().trim();

                if(em.isEmpty()){
                    email.setError("email is required!");
                    email.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(em).matches()){
                    email.setError("not an email!");
                    email.requestFocus();
                    return;
                }
                if(nick.isEmpty()){
                    nickname.setError("nickname is required!");
                    nickname.requestFocus();
                    return;
                }
                if(pass.isEmpty()){
                    password.setError("password is required!");
                    password.requestFocus();
                    return;
                }
                if(pass.length()<8){
                    password.setError("password minimum length is 8 characters!");
                    password.requestFocus();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(em, pass)
                        .addOnCompleteListener(register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    User user = new User(em, nick);
                                    FirebaseDatabase database = FirebaseDatabase.getInstance(
                                            "“https://eportfo-5a2ad-default-rtdb.europe-west1.firebasedatabase.app");
                                    database.getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(register.this,new OnCompleteListener<Void>(){
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(register.this, "User has been Registred successfully", Toast.LENGTH_SHORT).show();
                                                try {
                                                    TimeUnit.SECONDS.sleep(1);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                                FirebaseAuth.getInstance().signOut();
                                                BackHome();
                                                finish();
                                            }
                                            else{
                                                Toast.makeText(register.this, "Failed to register! Try again", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    });
                                    database.getReference("Watchlist")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("watchlist")
                                            .setValue("");

                                    HashMap<String,Double> ff = new HashMap<>();
                                    ff.put("bitcoin",0.0);
                                    database.getReference("Portfolio")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("portfolio")
                                            .setValue(ff);

                                }
                                else{
                                    Toast.makeText(register.this, "Failed GET REKIHJBGHj register! Try again", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                /*mAuth.createUserWithEmailAndPassword(em, pass)
                        .addOnCompleteListener(register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    User user = new User(em, nick);
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    Toast.makeText(register.this,"User has been successfully registred", Toast.LENGTH_LONG).show();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    try {
                                        TimeUnit.SECONDS.sleep(1);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    FirebaseAuth.getInstance().signOut();
                                    BackHome();
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(register.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });*/

            }
        });
    }
    public void BackHome(){
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }
}