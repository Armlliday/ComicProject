package navigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.proyectocomic.MainActivity;
import com.example.proyectocomic.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //Inicializa el navigationview
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Define la home
        bottomNavigationView.setSelectedItemId(R.id.about);

        //Agregar Tomos
        ImageButton addTomos = (ImageButton) findViewById(R.id.addTomoButton);
        addTomos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(),TomoActivity.class));
            }
        });

        //Búsquedas recientes
        final LinearLayout recents = (LinearLayout) findViewById(R.id.button_searches);
        recents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(),RecentSearchesActivity.class));
            }
        });

        //Actualizables
        final CardView updatable =(CardView) findViewById(R.id.button_updatables);
        updatable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(),UpdatablesActivity.class));
            }
        });

        //Agregables
        final LinearLayout addable = (LinearLayout) findViewById(R.id.button_add);
        addable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(),AddablesActivity.class));
            }
        });

        //Favoritos
        final LinearLayout favs = (LinearLayout) findViewById(R.id.button_fav);
        favs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), FavouritesActivity.class));
            }
        });

        if(!MainActivity.Super){
            updatable.setVisibility(View.INVISIBLE);
            addable.setVisibility(View.INVISIBLE);
            addTomos.setVisibility(View.INVISIBLE);
        }

        //Agrega su selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext(), DashBoard.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), BottonNavigation.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.about:
                        return true;
                }
                return false;
            }
        });

    }
}
