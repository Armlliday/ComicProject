package navigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.proyectocomic.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DashBoard extends AppCompatActivity {

private TextView mnombreP;
private TextView mnombrePA;
private TextView mtelefonoP;
private TextView memailP;
private TextView mprofesionP;
private DatabaseReference mDatabase;
    FirebaseAuth auth     = FirebaseAuth.getInstance();
    FirebaseUser user     = auth.getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        CardView backp = findViewById(R.id.button_home);

        //Inicializar variables
        mnombreP = findViewById(R.id.nombreP);
        mtelefonoP = findViewById(R.id.telefonoP);
        memailP = findViewById(R.id.emailP);
        mnombrePA = findViewById(R.id.nombrePA);
        mprofesionP = findViewById(R.id.profesionP);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //cojemos users
        mDatabase.child("Users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    String nombre  = dataSnapshot.child("Name").getValue().toString();
                    mnombreP.setText(nombre);

                    String nombre2  = dataSnapshot.child("Name").getValue().toString();
                    mnombrePA.setText(nombre2);

                   String telefono = dataSnapshot.child("Telefono").getValue().toString();
                   mtelefonoP.setText(telefono);

                    String profesion  = dataSnapshot.child("Profesion").getValue().toString();
                    mprofesionP.setText(profesion);

                    String email  = dataSnapshot.child("Email").getValue().toString();
                    memailP.setText(email);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Inicializa el navigationview
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Define la home
        bottomNavigationView.setSelectedItemId(R.id.dashboard);

        //defina back to home
        backp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),BottonNavigation.class));
            }
        });


        //Agrega su selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.dashboard:

                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), BottonNavigation.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.about:
                        startActivity(new Intent(getApplicationContext(), About.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
}
