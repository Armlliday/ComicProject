package navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectocomic.MainActivity;
import com.example.proyectocomic.R;
import com.example.proyectocomic.comics.Comic;
import com.example.proyectocomic.structures.DynamicArray;

import java.util.ArrayList;

import adapters.Adapter;

public class FavouritesActivity extends AppCompatActivity implements Adapter.OnComicListener {

    DynamicArray<Comic> favComics;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourites_activity);
        ImageButton back = (ImageButton) findViewById(R.id.backFav);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.RecyclerFav);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        favComics = MainActivity.favorites.inOrder();
        Adapter adapter = new Adapter(favComics,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onComicClick(int position) {
        Intent intent = new Intent(this, ComicActivity.class);
        intent.putExtra("comic", favComics.get(position));
        intent.putExtra("autor", favComics.get(position).getEscritor());
        intent.putExtra("dibujante", favComics.get(position).getDibujante());


        startActivity(intent);
    }
}
