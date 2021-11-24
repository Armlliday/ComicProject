package navigation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import adapters.Adapter;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectocomic.MainActivity;
import com.example.proyectocomic.R;
import com.example.proyectocomic.catalogo.Catalogo;
import com.example.proyectocomic.catalogo.DBHelper;
import com.example.proyectocomic.comics.Autor;
import com.example.proyectocomic.comics.Categoria;
import com.example.proyectocomic.comics.Comic;
import com.example.proyectocomic.comics.Tomo;
import com.example.proyectocomic.structures.BinarySearchTree;
import com.example.proyectocomic.structures.DynamicArray;
import com.example.proyectocomic.structures.Stack;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;


public class BottonNavigation extends AppCompatActivity implements Adapter.OnComicListener {

    DynamicArray<Comic> ListaComic;
    RecyclerView recyclercomic;



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        //Searchbar
        final EditText searchbar = (EditText) findViewById(R.id.searchbar);



        /*DynamicArray<Tomo> tomos = new DynamicArray<Tomo>();
        Categoria cat = new Categoria("La buena");
        DynamicArray<Categoria> cats = new DynamicArray<Categoria>();
        cats.pushBack(cat);
        Comic comicsito = new Comic("Watchmen#70000",1987, new Autor("Alan_Moore",32,
                new DynamicArray<Categoria>()),new Autor("Dave_Gibbons",32,new DynamicArray<Categoria>()));
        Tomo tomo = new Tomo("Watchmen",comicsito.getEscritor(),comicsito.getDibujante(),
               comicsito.getAgno_publicacion(),true,cats);
        tomo.agregarAtras(comicsito);
        tomos.pushBack(tomo);
        //MainActivity.catalogo.setTomos(tomos);



        MainActivity.catalogo.tomoAgregarAtras(comicsito,MainActivity.catalogo.getTomos().get(0));
        Log.d("Mensaje" , ""+MainActivity.catalogo.getTomos().get(0).getTamano());
*/
        DBHelper db = new DBHelper(getApplicationContext());
        MainActivity.catalogo.index(db);
        DynamicArray<Comic> bus = MainActivity.catalogo.buscarPorAutor("Alan_Moore");
        Log.d("Mensaje" , ""+bus.getSize());

        /**/


        //Recycler comics
        ListaComic = new DynamicArray<Comic>();
        recyclercomic = findViewById(R.id.RecyclerId);
        recyclercomic.setLayoutManager(new LinearLayoutManager(this));
        //llenar lista
        llenarPersonajes();
        Adapter adapter = new Adapter(ListaComic,this);
        recyclercomic.setAdapter(adapter);

        final Adapter.OnComicListener deMomento = this;


        if(getIntent().hasExtra("search")){
            searchbar.setText((String) getIntent().getExtras().getString("search"));
            searchbar.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            imm.showSoftInput(searchbar, InputMethodManager.SHOW_IMPLICIT);
            DynamicArray<Comic> res = MainActivity.catalogo.buscarPorNombre(searchbar.getText().toString());
            recyclercomic.swapAdapter(new Adapter(res,deMomento),false);
        }
        searchbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_RIGHT = 2;

                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if(motionEvent.getRawX() >= (searchbar.getRight() - searchbar.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        Toast.makeText(getBaseContext(), searchbar.getText(), Toast.LENGTH_SHORT).show();
                        if(!searchbar.getText().equals("")){
                            try {
                                updateSearches(MainActivity.firebaseAuth.getCurrentUser().getUid(),searchbar.getText().toString());
                            } catch (CloneNotSupportedException e) {
                                e.printStackTrace();
                            }
                            DynamicArray<Comic> res = MainActivity.catalogo.buscarPorNombre(searchbar.getText().toString());
                            ListaComic = res;
                            recyclercomic.swapAdapter(new Adapter(res,deMomento),false);
                        }

                        return true;
                    }
                }
                return false;
            }
        });
        searchbar.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    Toast.makeText(getBaseContext(), searchbar.getText(), Toast.LENGTH_SHORT).show();
                    if(!searchbar.getText().equals("")){
                        try {
                            updateSearches(MainActivity.firebaseAuth.getCurrentUser().getUid(),searchbar.getText().toString());
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                        DynamicArray<Comic> res = MainActivity.catalogo.buscarPorNombre(searchbar.getText().toString());
                        ListaComic = res;
                        recyclercomic.swapAdapter(new Adapter(res,deMomento),false);
                    }
                    return true;
                }
                return false;
            }

        });








        //Inicializa el navigationview
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);


        //Define la home
        bottomNavigationView.setSelectedItemId(R.id.home);

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

    private void updateSearches(String uid, String text) throws CloneNotSupportedException {
        MainActivity.recentSearches.push(text);
        File file_actualizada = MainActivity.tempFileStack;
        File file = MainActivity.fileStack;
        PrintStream flujo_salida = null;
        try {
            flujo_salida = new PrintStream(file_actualizada);
        } catch (FileNotFoundException ex) {
            System.out.println("Error en la actualizacion de tomos");
            Log.d("Durazno","a, "+ex.getMessage());
        }


        Stack<String> tempStack = (Stack<String>) MainActivity.recentSearches.clone();
        Stack<String> inverted = new Stack<String>();
        for(int i = 0; i < MainActivity.recentSearches.size; ++i){
            inverted.push(tempStack.pop());
        }
        flujo_salida.println("Busquedas "+MainActivity.recentSearches.size+" "+inverted.size);
        int counter = inverted.size;

        while(counter!=0){
            String search = inverted.pop();
            flujo_salida.println(search);
            counter--;
        }
        System.gc();
        flujo_salida.flush();
        System.gc();
        flujo_salida.close();
        System.gc();
        file.delete();
        file_actualizada.renameTo(file);

        Uri fileTomo = Uri.fromFile(file);
        StorageReference riversRef = MainActivity.mStorageRef.getReferenceFromUrl("gs://rproyectocomic.appspot.com/")
                .child("Busquedas").child(MainActivity.firebaseAuth.getUid()+".txt");
        UploadTask uploadTask = riversRef.putFile(fileTomo);
        uploadTask = riversRef.putFile(fileTomo);
    }

    private String getName(String t)
    {
        String[] res = t.split("_");
        String retorno = "";
        for(int i = 0; i < res.length; ++i)
        {
            retorno += res[i];
            if(i != res.length-1) retorno += " ";
        }
        return retorno;
    }
    private void llenarPersonajes (){
        Catalogo catalogo = MainActivity.catalogo;
        for(int i = 0; i < catalogo.getTomos().getSize(); ++i)
        {
            Tomo tomo = catalogo.getTomos().get(i);
            Comic comic = tomo.getCabeza();
            for(int j = 1; j <= tomo.getTamano(); ++j)
            {
                comic.foto = this.getResources().getIdentifier("" + tomo.getNombre().toLowerCase() +"" + j, "drawable", this.getPackageName());
                comic.categorias = tomo.getCategorias().toString();
                comic.nombre_Tom = getName(tomo.getNombre());
                ListaComic.pushBack(comic);
                comic = comic.getSecuela();
            }
        }
        /*ListaComic.add(new Comic("Watchmen#1",1987, new Autor("Alan Moore",32,
                new DynamicArray<Categoria>()),new Autor("Dave Gibbons",32,new DynamicArray<Categoria>()),
                "Hollis Mason starts his book by recalling his decision to write a book by turning to a writer named Denise, who had written forty-two stories but never published them, for advice. Denise told him to start with the saddest thing he could think of to get sympathy, \"after that, believe me, it's a walk.\" Hollis dedicates his book, Under the Hood to Denise.",
                resId));
        ListaComic.add(new Comic("Watchmen#2",1987, new Autor("Alan Moore",32,
                new DynamicArray<Categoria>()),new Autor("Dave Gibbons",32,new DynamicArray<Categoria>()),
                "Hollis Mason starts his book by recalling his decision to write a book by turning to a writer named Denise, who had written forty-two stories but never published them, for advice. Denise told him to start with the saddest thing he could think of to get sympathy, \"after that, believe me, it's a walk.\" Hollis dedicates his book, Under the Hood to Denise.",
                R.drawable.watchmen2));
        ListaComic.add(new Comic("Watchmen#3",1987, new Autor("Alan Moore",32,
                new DynamicArray<Categoria>()),new Autor("Dave Gibbons",32,new DynamicArray<Categoria>()),
                "Severo cómic a lo bien",R.drawable.watchmen2));
        ListaComic.add(new Comic("Watchmen#4",1987, new Autor("Alan Moore",32,
                new DynamicArray<Categoria>()),new Autor("Dave Gibbons",32,new DynamicArray<Categoria>()),
                "Severo cómic a lo bien",R.drawable.watchmen2+1));
        ListaComic.add(new Comic("Watchmen#5",1987, new Autor("Alan Moore",32,
                new DynamicArray<Categoria>()),new Autor("Dave Gibbons",32,new DynamicArray<Categoria>()),
                "Severo cómic a lo bien",R.drawable.watchmen2+2));
        ListaComic.add(new Comic("Watchmen#6",1987, new Autor("Alan Moore",32,
                new DynamicArray<Categoria>()),new Autor("Dave Gibbons",32,new DynamicArray<Categoria>()),
                "Severo cómic a lo bien",R.drawable.watchmen2+3));
        ListaComic.add(new Comic("Watchmen#7",1987, new Autor("Alan Moore",32,
                new DynamicArray<Categoria>()),new Autor("Dave Gibbons",32,new DynamicArray<Categoria>()),
                "Severo cómic a lo bien",R.drawable.watchmen2+4));
        ListaComic.add(new Comic("Watchmen#8",1987, new Autor("Alan Moore",32,
                new DynamicArray<Categoria>()),new Autor("Dave Gibbons",32,new DynamicArray<Categoria>()),
                "Severo cómic a lo bien",R.drawable.watchmen));*/
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
