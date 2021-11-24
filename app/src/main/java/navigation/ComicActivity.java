package navigation;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectocomic.MainActivity;
import com.example.proyectocomic.R;
import com.example.proyectocomic.comics.*;
import com.example.proyectocomic.structures.DynamicArray;
import com.example.proyectocomic.structures.Queue;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import adapters.Adapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;


public class ComicActivity extends AppCompatActivity implements Adapter.OnComicListener{

    DynamicArray<Comic> ListaComic = new DynamicArray<Comic>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comic_activity);
        TextView tomo = (TextView) findViewById(R.id.com_tomo);
        TextView titulo = (TextView) findViewById(R.id.com_title);
        TextView editorial = (TextView) findViewById(R.id.com_publisher);
        TextView autor = (TextView) findViewById(R.id.com_author);
        TextView dibujante = (TextView) findViewById(R.id.com_author2);
        TextView categoria = (TextView) findViewById(R.id.com_category);
        TextView lenguaje = (TextView) findViewById(R.id.com_language);
        TextView anho = (TextView) findViewById(R.id.com_year);
        TextView descripcion = (TextView) findViewById(R.id.com_description);
        Button secuela = (Button) findViewById(R.id.button_secuela);
        Button precuela = (Button) findViewById(R.id.button_precuela);
        ImageView imagen = (ImageView) findViewById(R.id.imageT);
        RecyclerView recycler = (RecyclerView) findViewById(R.id.recyclerAut);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        final ImageButton fav = (ImageButton) findViewById(R.id.FavButton);
        final ImageButton edit = (ImageButton) findViewById(R.id.editButton);
        final ImageButton add = (ImageButton) findViewById(R.id.addButton);

        if(getIntent().hasExtra("comic")) {
            final Comic comic = getIntent().getParcelableExtra("comic");
            comic.setEscritor((Autor) getIntent().getParcelableExtra("autor"));
            comic.setDibujante((Autor) getIntent().getParcelableExtra("dibujante"));
            tomo.setText(comic.nombre_Tom);
            titulo.setText(comic.getNombre().replaceAll("_"," "));
            editorial.setText("DC Comics");
            autor.setText(comic.getEscritor().getNombre().replaceAll("_"," "));
            dibujante.setText(comic.getDibujante().getNombre().replaceAll("_"," "));
            categoria.setText(comic.categorias);
            lenguaje.setText("Inglés");
            anho.setText(Integer.toString(comic.getAgno_publicacion()));
            descripcion.setText(comic.getDescripcion());
            imagen.setImageResource(comic.getFoto());
            DynamicArray<Comic> mismoAutor = MainActivity.catalogo.buscarPorAutor(comic.getEscritor().getNombre().replaceAll(" ","_"));
            ListaComic = mismoAutor;
            Log.d("Maracuya",ListaComic.getSize()+"");
            Adapter adapter = new Adapter(mismoAutor,this);
            recycler.setAdapter(adapter);
            if(comic.getSecuela()==null){
                secuela.setVisibility(View.INVISIBLE);
            } else{
                secuela.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent intent2 = new Intent(getBaseContext(),ComicActivity.class);
                        comic.getSecuela().setEscritor(comic.getEscritor());
                        comic.getSecuela().setDibujante(comic.getDibujante());
                        intent2.putExtra("comic", comic.getSecuela());
                        intent2.putExtra("autor", comic.getSecuela().getEscritor());
                        intent2.putExtra("dibujante", comic.getSecuela().getDibujante());
                        startActivity(intent2);
                        finish();
                    }
                });
            }
            if(comic.getPrecuela()==null){
                precuela.setVisibility(View.INVISIBLE);
            } else{
                precuela.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent intent3 = new Intent(getBaseContext(),ComicActivity.class);
                        comic.getPrecuela().setEscritor(comic.getEscritor());
                        comic.getPrecuela().setDibujante(comic.getDibujante());
                        intent3.putExtra("comic", comic.getPrecuela());
                        intent3.putExtra("autor", comic.getPrecuela().getEscritor());
                        intent3.putExtra("dibujante", comic.getPrecuela().getDibujante());
                        startActivity(intent3);
                        finish();
                    }
                });
            }
            fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.favorites.root = MainActivity.favorites.Insert(comic,MainActivity.favorites.root);
                    Log.d("mensaje", Integer.toString(MainActivity.favorites.height(MainActivity.favorites.root)));
                    fav.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#830003")));

                    File file_actualizada = MainActivity.tempFileFavs;
                    File file = MainActivity.fileFavs;
                    PrintStream flujo_salida = null;
                    try {
                        flujo_salida = new PrintStream(file_actualizada);
                    } catch (FileNotFoundException ex) {
                        System.out.println("Error en la actualizacion de tomos");
                        Log.d("Durazno","a, "+ex.getMessage());
                    }

                    int counter = MainActivity.favorites.numberNodes(MainActivity.favorites.root);
                    flujo_salida.println("Favoritos "+ counter);
                    DynamicArray<Comic> comicu =MainActivity.favorites.inOrder();
                    while(counter!=0){
                        Comic c = comicu.get(counter-1);
                        flujo_salida.println(c.getNombre().replaceAll(" ","_"));
                        counter--;
                    }
                    System.gc();
                    flujo_salida.flush();
                    System.gc();
                    flujo_salida.close();
                    System.gc();
                    file.delete();
                    file_actualizada.renameTo(file);

                    Uri fileFavorites = Uri.fromFile(file);
                    StorageReference riversRef = MainActivity.mStorageRef.getReferenceFromUrl("gs://rproyectocomic.appspot.com/")
                            .child("Favoritos").child("f_"+MainActivity.firebaseAuth.getUid()+".txt");
                    UploadTask uploadTask = riversRef.putFile(fileFavorites);
                    uploadTask = riversRef.putFile(fileFavorites);

                }
            });
            if(!MainActivity.favorites.isEmpty()){
                if(comic.equals(MainActivity.favorites.Find(comic,MainActivity.favorites.root).key)) {
                    fav.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#830003")));

                }
            }
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent editIntent = new Intent(getBaseContext(), EditComicActivity.class);
                    editIntent.putExtra("editComic",(Comic) comic);
                    editIntent.putExtra("autor", (String) comic.getEscritor().getNombre());
                    editIntent.putExtra("dibujante",(String) comic.getDibujante().getNombre());

                    startActivity(editIntent);
                }
            });
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent addIntent = new Intent(getBaseContext(), AddComicActivity.class);
                    addIntent.putExtra("comicName",comic.getNombre());
                    startActivity(addIntent);
                }
            });
        }
        ImageButton back = (ImageButton) findViewById(R.id.imageButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onComicClick(int position) {
        Intent intent = new Intent(this,ComicActivity.class);
        intent.putExtra("comic", ListaComic.get(position));
        intent.putExtra("autor", ListaComic.get(position).getEscritor());
        intent.putExtra("dibujante", ListaComic.get(position).getDibujante());

        startActivity(intent);
    }
}

