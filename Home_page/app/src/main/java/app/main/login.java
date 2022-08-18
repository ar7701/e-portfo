package app.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {
    private FirebaseAuth mAuth= FirebaseAuth.getInstance();
    private String TAG = "login";
    private EditText email;
    private EditText password;
    private Button lgn_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        lgn_btn = (Button) findViewById(R.id.login_btn);


        lgn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();

            }
        });



    }

    public void login() {

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
        mAuth.signInWithEmailAndPassword(em, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task< AuthResult > task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user.isEmailVerified()){
                                updateUI(user);
                            }
                            else{
                                user.sendEmailVerification();
                                Toast.makeText(login.this, "Check your email to verify your account!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }


                    }
                });


    }

    private void updateUI(FirebaseUser user) {

        Intent intent = new Intent(this, HomePage.class);
        intent.putExtra("user",user);
        startActivity(intent);

    }

}