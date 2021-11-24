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
import com.example.proyectocomic.comics.*;
import com.example.proyectocomic.structures.Queue;
import com.example.proyectocomic.structures.Stack;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class EditComicActivity extends AppCompatActivity {
    String[] update;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_comic_activity);
        ImageButton back = (ImageButton) findViewById(R.id.backEdit);
        final EditText nombre = (EditText) findViewById(R.id.nombreE);
        final EditText tomo = (EditText) findViewById(R.id.tomoE);
        final EditText anho = (EditText) findViewById(R.id.anoE);
        final EditText descripcion = (EditText) findViewById(R.id.descE);
        final EditText nombreautor = (EditText) findViewById(R.id.autE);
        final EditText nombredibujante = (EditText) findViewById(R.id.dibE);
        Button enviar = (Button) findViewById(R.id.send);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if(getIntent().hasExtra("editComic")) {
            final Comic comic = (Comic) getIntent().getParcelableExtra("editComic");
            nombre.setText(comic.getNombre());
            anho.setText(Integer.toString(comic.getAgno_publicacion()));
            descripcion.setText(comic.getDescripcion());
            if(getIntent().hasExtra("autor")){
                nombreautor.setText(getIntent().getExtras().getString("autor"));
            }
            if(getIntent().hasExtra("dibujante")){
                nombredibujante.setText(getIntent().getExtras().getString("dibujante"));
            }
            update = new String[6];

            enviar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    update[0] = nombre.getText().toString();
                    update[1] = tomo.getText().toString();
                    update[2] = anho.getText().toString();
                    update[3] = descripcion.getText().toString();
                    update[4] = nombreautor.getText().toString();
                    update[5] = nombredibujante.getText().toString();
                    MainActivity.updatables.enqueue(update);


                    File file_actualizada = MainActivity.tempFileUpdatables;
                    File file = MainActivity.fileUpdatables;
                    PrintStream flujo_salida = null;
                    try {
                        flujo_salida = new PrintStream(file_actualizada);
                    } catch (FileNotFoundException ex) {
                        System.out.println("Error en la actualizacion de tomos");
                        Log.d("Durazno","a, "+ex.getMessage());
                    }


                    Queue<String[]> tempQueue = null;
                    try {
                        tempQueue = (Queue<String[]>) MainActivity.updatables.clone();
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                    flujo_salida.println("Modificables "+tempQueue.size);
                    int counter = tempQueue.size;

                    while(counter!=0){
                        String[] comicu = tempQueue.pop();
                        flujo_salida.print(comicu[0].replaceAll(" ","_")+" ");
                        flujo_salida.print(comicu[1].replaceAll(" ","_")+" ");
                        flujo_salida.print(comicu[2]+" ");
                        flujo_salida.print(comicu[3].replaceAll(" ","_")+" ");
                        flujo_salida.print(comicu[4].replaceAll(" ","_")+" ");
                        flujo_salida.println(comicu[5].replaceAll(" ","_"));
                        counter--;
                    }
                    System.gc();
                    flujo_salida.flush();
                    System.gc();
                    flujo_salida.close();
                    System.gc();
                    file.delete();
                    file_actualizada.renameTo(file);

                    Uri fileUpdatables = Uri.fromFile(file);
                    StorageReference riversRef = MainActivity.mStorageRef.getReferenceFromUrl("gs://rproyectocomic.appspot.com/")
                            .child("updatables.txt");
                    UploadTask uploadTask = riversRef.putFile(fileUpdatables);
                    uploadTask = riversRef.putFile(fileUpdatables);



                    Log.d("Mensajito", "actualmente la fila tiene "+MainActivity.updatables.size+" elementos");
                    finish();
                }
            });
        }
    }
}
