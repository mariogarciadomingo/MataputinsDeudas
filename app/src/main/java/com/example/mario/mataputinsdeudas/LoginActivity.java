package com.example.mario.mataputinsdeudas;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "1";
    final String Anna = "anna@gmail.com";
    final String Laurita = "laurita@gmail.com";
    final String Lauron = "lauron@gmail.com";
    final String Mario = "mario@gmail.com";
    final String Blanca = "blanca@gmail.com";
    final String clave = "1234567";
    boolean programador = false;
    SharedPreferences preferences;
    Button bMario, bAnna, bLaurita, blauron, bBlanca;
    ProgressBar bar;
    private Switch ModoProgramador;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        ModoProgramador = findViewById(R.id.ModoProgramador);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        inicializarSharedPreferences();
        ModoProgramador.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("Programador", isChecked);
                editor.commit();
            }
        });
        bAnna = findViewById(R.id.btAnne);
        bMario = findViewById(R.id.btMario);
        bLaurita = findViewById(R.id.btLaurita);
        blauron = findViewById(R.id.btLauron);
        bBlanca = findViewById(R.id.btBlanca);
        bar = findViewById(R.id.progressBar2);
        bAnna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reiniciarValors();
                Login(Anna, clave);
                reiniciarValors();
            }
        });
        bMario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reiniciarValors();
                Login(Mario, clave);
                reiniciarValors();

            }
        });

        bLaurita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reiniciarValors();
                Login(Laurita, clave);
                reiniciarValors();
            }
        });
        blauron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reiniciarValors();
                Login(Lauron, clave);
                reiniciarValors();
            }
        });
        bBlanca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reiniciarValors();
                Login(Blanca, clave);
                reiniciarValors();
            }
        });
    }

    private void inicializarSharedPreferences() {

        programador = preferences.getBoolean("Programador", false);
        ModoProgramador.setChecked(programador);

    }

    private void reiniciarValors() {

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("fondo", "");
        editor.putString("total4", 0 + "");
        editor.putString("total3", 0 + "");
        editor.putString("total2", 0 + "");
        editor.putString("total1", 0 + "");
        editor.putString("total4_Ant", 0 + "");
        editor.putString("total3_Ant", 0 + "");
        editor.putString("total2_Ant", 0 + "");
        editor.putString("total1_Ant", 0 + "");
        editor.putInt("ColorTexto", Color.BLACK);
        editor.putInt("ColorMateriales", Color.WHITE);
        editor.commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void Login(@NonNull String email, @NonNull String password) {
        bar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Autentificando",
                                    Toast.LENGTH_SHORT).show();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
                            startActivity(intent);


                            updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(@Nullable FirebaseUser currentUser) {
        if (currentUser != null) {
            Toast.makeText(this, ("creado el usuario " + currentUser.getEmail() + "ui: " + currentUser.getUid()), Toast.LENGTH_LONG);
            Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
            startActivity(intent);
            finish();
        }
    }

}
