package navigation;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectocomic.MainActivity;
import com.example.proyectocomic.R;
import com.example.proyectocomic.comics.Comic;
import com.example.proyectocomic.structures.BinaryHeap;
import com.example.proyectocomic.structures.PriorityComic;
import com.example.proyectocomic.structures.Queue;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class AddComicActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_comic_activity);
        ImageButton back = (ImageButton) findViewById(R.id.backEditA);
        final EditText nombre = (EditText) findViewById(R.id.nombreEA);
        final EditText tomo = (EditText) findViewById(R.id.tomoEA);
        final EditText anho = (EditText) findViewById(R.id.anoEA);
        final EditText descripcion = (EditText) findViewById(R.id.descEA);
        final EditText nombreautor = (EditText) findViewById(R.id.autEA);
        final EditText nombredibujante = (EditText) findViewById(R.id.dibEA);
        Button enviar = (Button) findViewById(R.id.sendA);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comic adding = new Comic(nombre.getText().toString(),
                        Integer.parseInt(anho.getText().toString()),null,null,descripcion.getText().toString(),0);
                PriorityComic prAdding = new PriorityComic(adding,tomo.getText().toString(),nombreautor.getText().toString(),
                        nombredibujante.getText().toString(),getIntent().getExtras().getString("comicName"));
                MainActivity.addingComics.insert(prAdding);



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

                finish();
            }

        });
    }
}

