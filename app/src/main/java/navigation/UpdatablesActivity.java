package navigation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectocomic.MainActivity;
import com.example.proyectocomic.R;
import com.example.proyectocomic.comics.Comic;
import com.example.proyectocomic.structures.Queue;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class UpdatablesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updatables_activity);
        ImageButton back = (ImageButton) findViewById(R.id.UbackEdit);
        final TextView nombre = (TextView) findViewById(R.id.UnombreE);
        final TextView tomo = (TextView) findViewById(R.id.UtomoE);
        final TextView anho = (TextView) findViewById(R.id.UanoE);
        final TextView descripcion = (TextView) findViewById(R.id.UdescE);
        final TextView nombreautor = (TextView) findViewById(R.id.UautE);
        final TextView nombredibujante = (TextView) findViewById(R.id.UdibE);
        Button aceptar = (Button) findViewById(R.id.Uaccept);
        Button rechazar = (Button) findViewById(R.id.Udecline);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        //non-editables
        final TextView nnombre = (TextView) findViewById(R.id.Unombre);
        final TextView ntomo = (TextView) findViewById(R.id.Utomo);
        final TextView nanho = (TextView) findViewById(R.id.Uano);
        final TextView ndescripcion = (TextView) findViewById(R.id.Udesc);
        final TextView nnombreautor = (TextView) findViewById(R.id.Uaut);
        final TextView nnombredibujante = (TextView) findViewById(R.id.Udib);

        if(MainActivity.updatables.empty()){
            nombre.setVisibility(View.INVISIBLE);
            tomo.setVisibility(View.INVISIBLE);
            anho.setVisibility(View.INVISIBLE);
            descripcion.setVisibility(View.INVISIBLE);
            nombreautor.setVisibility(View.INVISIBLE);
            nombredibujante.setVisibility(View.INVISIBLE);
            aceptar.setVisibility(View.INVISIBLE);
            rechazar.setVisibility(View.INVISIBLE);
            nnombre.setVisibility(View.INVISIBLE);
            ntomo.setVisibility(View.INVISIBLE);
            nanho.setVisibility(View.INVISIBLE);
            //ndescripcion.setVisibility(View.INVISIBLE);
            ndescripcion.setText("No hay ninguna solicitud para actualizar cómics");
            nnombreautor.setVisibility(View.INVISIBLE);
            nnombredibujante.setVisibility(View.INVISIBLE);
        } else{
            String[] updating = MainActivity.updatables.top();
            nombre.setText(updating[0]);
            tomo.setText(updating[1]);
            anho.setText(updating[2]);
            descripcion.setText(updating[3]);
            nombreautor.setText(updating[4]);
            nombredibujante.setText(updating[5]);
            aceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Acá agregar cuando ya funcione el buscador
                    String[] update = MainActivity.updatables.pop();
                    Comic up = MainActivity.catalogo.buscarPorNombre(update[0],1).get(0);
                    up.setAgno_publicacion(Integer.parseInt(update[2]));
                    up.setDescripcion(update[3]);
                    up.getEscritor( ).setNombre(update[4]);
                    up.getDibujante().setNombre(update[5]);
                    MainActivity.catalogo.escribirTomos();




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

                    startActivity(new Intent(getBaseContext(),UpdatablesActivity.class));
                    finish();
                }
            });
            rechazar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.updatables.pop();
                    startActivity(new Intent(getBaseContext(),UpdatablesActivity.class));

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
                    finish();
                }
            });
        }

    }
}
