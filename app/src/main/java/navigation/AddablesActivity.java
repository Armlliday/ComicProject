package navigation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectocomic.MainActivity;
import com.example.proyectocomic.R;
import com.example.proyectocomic.comics.Autor;
import com.example.proyectocomic.comics.Comic;
import com.example.proyectocomic.comics.Tomo;
import com.example.proyectocomic.structures.BinaryHeap;
import com.example.proyectocomic.structures.DynamicArray;
import com.example.proyectocomic.structures.PriorityComic;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class AddablesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addables_activity);
        ImageButton back = (ImageButton) findViewById(R.id.backA);
        TextView tomo = (TextView) findViewById(R.id.tomoAC);
        TextView escritor = (TextView) findViewById(R.id.writerAC);
        TextView dibujante = (TextView) findViewById(R.id.drawerAC);
        TextView titulo = (TextView) findViewById(R.id.titleAC);
        TextView anho = (TextView) findViewById(R.id.yearAC);
        TextView descripcion = (TextView) findViewById(R.id.descAC);
        TextView ntomo = (TextView) findViewById(R.id.tomoA);
        TextView nescritor = (TextView) findViewById(R.id.writerA);
        TextView ndibujante = (TextView) findViewById(R.id.drawerA);
        TextView ntitulo = (TextView) findViewById(R.id.titleA);
        TextView nanho = (TextView) findViewById(R.id.yearA);
        TextView ndescripcion = (TextView) findViewById(R.id.descA);
        Button aceptar = (Button) findViewById(R.id.acceptA);
        Button rechazar = (Button) findViewById(R.id.declineA);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if(MainActivity.addingComics.isEmpty()){
            tomo.setVisibility(View.INVISIBLE);
            anho.setVisibility(View.INVISIBLE);
            titulo.setVisibility(View.INVISIBLE);
            descripcion.setVisibility(View.INVISIBLE);
            aceptar.setVisibility(View.INVISIBLE);
            rechazar.setVisibility(View.INVISIBLE);
            escritor.setVisibility(View.INVISIBLE);
            dibujante.setVisibility(View.INVISIBLE);
            ntomo.setVisibility(View.INVISIBLE);
            nanho.setVisibility(View.INVISIBLE);
            ntitulo.setVisibility(View.INVISIBLE);
            //ndescripcion.setVisibility(View.INVISIBLE);
            ndescripcion.setText("No hay ninguna solicitud para agregar cómics");
            nescritor.setVisibility(View.INVISIBLE);
            ndibujante.setVisibility(View.INVISIBLE);
        } else{
            PriorityComic temp = null;
            try {
                temp = MainActivity.addingComics.getMax();
            } catch (Exception e) {
                e.printStackTrace();
            }
            tomo.setText(temp.tomo);
            escritor.setText(temp.escritor);
            dibujante.setText(temp.dibujante);
            titulo.setText(temp.comic.getNombre());
            anho.setText(Integer.toString(temp.comic.getAgno_publicacion()));
            descripcion.setText(temp.comic.getDescripcion());
            final PriorityComic finalTemp = temp;
            aceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Acá agregar cuando ya funcione el buscador
                    try {
                        MainActivity.addingComics.extractMax();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Tomo tomo1 = null;
                    for(int j = 0; j<MainActivity.catalogo.getTomos().getSize(); j++){
                        if (MainActivity.catalogo.getTomos().get(j).getNombre().equals(finalTemp.tomo)){
                            tomo1 = MainActivity.catalogo.getTomos().get(j);
                            break;
                        }
                    }
                    if(tomo1!=null){
                        Comic siendoAgregado = finalTemp.comic;
                        siendoAgregado.setEscritor(new Autor(finalTemp.escritor,30,null));
                        siendoAgregado.setDibujante(new Autor(finalTemp.dibujante,30,null));
                        DynamicArray<Comic> precuela = MainActivity.catalogo.buscarPorNombre(finalTemp.precuela,1);
                        MainActivity.catalogo.tomoAgregarDespues(siendoAgregado,precuela.get(0),tomo1);
                    }






                    File file_actualizada = MainActivity.tempFileAddables;
                    File file = MainActivity.fileAddables;
                    PrintStream flujo_salida = null;
                    try {
                        flujo_salida = new PrintStream(file_actualizada);
                    } catch (FileNotFoundException ex) {
                        System.out.println("Error en la actualizacion de tomos");
                        Log.d("Durazno","a, "+ex.getMessage());
                    }


                    BinaryHeap<PriorityComic> tempQueue = null;
                    try {
                        tempQueue = (BinaryHeap<PriorityComic>) MainActivity.addingComics.clone();
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                    flujo_salida.println("Agregables "+tempQueue.size);
                    int counter = tempQueue.size;

                    while(counter!=0){
                        PriorityComic c = null;
                        try {
                            c = tempQueue.extractMax();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        flujo_salida.print(c.comic.getNombre().replaceAll(" ","_")+" ");
                        flujo_salida.print(c.comic.getAgno_publicacion()+" ");
                        flujo_salida.print(c.comic.getDescripcion().replaceAll(" ","_")+" ");
                        flujo_salida.print(c.tomo.replaceAll(" ","_")+" ");
                        flujo_salida.print(c.escritor.replaceAll(" ","_")+" ");
                        flujo_salida.print(c.dibujante.replaceAll(" ","_")+" ");
                        flujo_salida.println(c.precuela.replaceAll(" ","_"));

                        counter--;
                    }
                    System.gc();
                    flujo_salida.flush();
                    System.gc();
                    flujo_salida.close();
                    System.gc();
                    file.delete();
                    file_actualizada.renameTo(file);

                    Uri fileAddables = Uri.fromFile(file);
                    StorageReference riversRef = MainActivity.mStorageRef.getReferenceFromUrl("gs://rproyectocomic.appspot.com/")
                            .child("addables.txt");
                    UploadTask uploadTask = riversRef.putFile(fileAddables);
                    uploadTask = riversRef.putFile(fileAddables);


                    startActivity(new Intent(getBaseContext(),AddablesActivity.class));
                    finish();
                }
            });
            rechazar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        MainActivity.addingComics.extractMax();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    File file_actualizada = MainActivity.tempFileAddables;
                    File file = MainActivity.fileAddables;
                    PrintStream flujo_salida = null;
                    try {
                        flujo_salida = new PrintStream(file_actualizada);
                    } catch (FileNotFoundException ex) {
                        System.out.println("Error en la actualizacion de tomos");
                        Log.d("Durazno","a, "+ex.getMessage());
                    }


                    BinaryHeap<PriorityComic> tempQueue = null;
                    try {
                        tempQueue = (BinaryHeap<PriorityComic>) MainActivity.addingComics.clone();
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                    flujo_salida.println("Agregables "+tempQueue.size);
                    int counter = tempQueue.size;

                    while(counter!=0){
                        PriorityComic c = null;
                        try {
                            c = tempQueue.extractMax();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        flujo_salida.print(c.comic.getNombre().replaceAll(" ","_")+" ");
                        flujo_salida.print(c.comic.getAgno_publicacion()+" ");
                        flujo_salida.print(c.comic.getDescripcion().replaceAll(" ","_")+" ");
                        flujo_salida.print(c.tomo.replaceAll(" ","_")+" ");
                        flujo_salida.print(c.escritor.replaceAll(" ","_")+" ");
                        flujo_salida.print(c.dibujante.replaceAll(" ","_")+" ");
                        flujo_salida.println(c.precuela.replaceAll(" ","_"));

                        counter--;
                    }
                    System.gc();
                    flujo_salida.flush();
                    System.gc();
                    flujo_salida.close();
                    System.gc();
                    file.delete();
                    file_actualizada.renameTo(file);

                    Uri fileAddables = Uri.fromFile(file);
                    StorageReference riversRef = MainActivity.mStorageRef.getReferenceFromUrl("gs://rproyectocomic.appspot.com/")
                            .child("addables.txt");
                    UploadTask uploadTask = riversRef.putFile(fileAddables);
                    uploadTask = riversRef.putFile(fileAddables);

                    startActivity(new Intent(getBaseContext(),AddablesActivity.class));
                    finish();
                }
            });
        }

    }
}
