package com.example.mario.mataputinsdeudas;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HistorialActivity extends AppCompatActivity {
    boolean programador = false;
    private static final String TAG ="" ;
    private RecyclerView rv;
    private List<deuda> listDeudas,list;
    FirebaseDatabase database ;
    static DatabaseReference myRef;
    @Nullable
    private FirebaseUser user;
    SharedPreferences preferences;
    private FirebaseAuth mAuth;
    final String Anna = "anna@gmail.com";
    final String Laurita = "laurita@gmail.com";
    final String Lauron = "lauron@gmail.com";
    final String Mario = "mario@gmail.com";
    final String Blanca = "blanca@gmail.com";
    String seleccionat;
    Spinner spiner;
    Button historial;

    ImageView fons;
    int color = Color.BLACK;
    String nom;
    String [] usuarios;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        rv = findViewById(R.id.recyclerView);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        programador = preferences.getBoolean("Programador", false);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);
        rv.setItemViewCacheSize(20);
        rv.setDrawingCacheEnabled(true);
        rv.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if(user.getEmail().equals(Anna)){nom="Anna"; usuarios = new String[]{"Todos","Laurita","Lauron","Mario","Blanca"};


        }
        if(user.getEmail().equals(Laurita)){nom="Laurita"; usuarios = new String[]{"Todos","Anna","Lauron","Mario","Blanca"};
           }
        if(user.getEmail().equals(Lauron)) {
            nom = "Lauron";
            usuarios = new String[]{"Todos","Laurita", "Anna", "Mario", "Blanca"};
        }
        if(user.getEmail().equals(Mario)){nom="Mario"; usuarios = new String[]{"Todos","Laurita","Lauron","Anna","Blanca"};

        }
        if(user.getEmail().equals(Blanca)){nom="Blanca"; usuarios = new String[]{"Todos","Laurita","Lauron","Anna","Mario"};

        }
        database = FirebaseDatabase.getInstance();
        String prog = "";
        historial = findViewById(R.id.btEliminarHistorial);
        if(programador)
        {
            prog="programador/";
            historial.setText("Modo Programador");
        }
        myRef = database.getReference(prog+"usuarios/"+ nom +"/historial");

        historial.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                myRef.setValue("");
                finish();
                return true;
            }
        });
        /*String Pfondo = preferences.getString("fondo","");
        if(Pfondo!=""){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(Pfondo, options);
            fons.setImageBitmap(bitmap);}*/

        inicialitzarAdaptador();
        spiner = findViewById(R.id.spinner);
        ArrayAdapter adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,usuarios);
        spiner.setAdapter(adaptador);
        spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                seleccionat=usuarios[position];

                inicialitzarAdaptador();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }
    @Override
    public void onBackPressed() {
        finish();
    }
    private void inicialitzarAdaptador()

    {
        try{


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {

                    boolean todos = false;
                    listDeudas = new ArrayList<>();
                    if (seleccionat.equals("Todos"))
                        todos = true;
                    for (DataSnapshot fechas : dataSnapshot.getChildren()) {
                        String usuario = fechas.child("usuario").getValue(String.class);
                        if (todos | seleccionat.equals(usuario)) {
                            String id = fechas.getKey();
                            String fecha = fechas.child("fecha").getValue(String.class);
                            String titol = fechas.child("descripcion").getValue(String.class);
                            Double valor = fechas.child("valor").getValue(Double.class);
                            String imatge = "";
                            if (usuario.equals("Anna")) {
                                imatge = "anna.jpg";
                            }
                            if (usuario.equals("Blanca")) {
                                imatge = "blanca.jpg";
                            }
                            if (usuario.equals("Laurita")) {
                                imatge = "laurita.jpg";
                            }
                            if (usuario.equals("Lauron")) {
                                imatge ="lauron.jpg";
                            }
                            if (usuario.equals("Mario")) {
                                imatge = "mario.jpg";
                            }
                           // listDeudas.add(new deuda(id, imatge, titol, valor.toString() + " €", usuario, fecha));
                            listDeudas.add(new deuda(id, imatge, titol,  NumberFormat.getCurrencyInstance(new Locale("es", "ES"))
                                    .format(valor), usuario, fecha));

                        }
                    }



                if (listDeudas.size() == 0) {
                    listDeudas.add(new deuda("", "", "Historial Vacio", "", "", ""));
                }
                RVAdaptador adaptador = new RVAdaptador(listDeudas,  Color.BLACK);
                    rv.setHasFixedSize(true);
                    rv.setNestedScrollingEnabled(false);
                    rv.setItemViewCacheSize(20);
                    rv.setDrawingCacheEnabled(true);
                    rv.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
                rv.setAdapter(adaptador);
            }catch (Exception e)
                {


                }
        }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });}
        catch(Exception e)
        {

        }
    }
public static void eliminar(@NonNull String id)
{
    myRef.child(id).removeValue();
}


}
