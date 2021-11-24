package navigation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectocomic.MainActivity;
import com.example.proyectocomic.R;
import com.example.proyectocomic.comics.Autor;
import com.example.proyectocomic.comics.Categoria;
import com.example.proyectocomic.comics.Tomo;
import com.example.proyectocomic.structures.DynamicArray;

public class TomoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tomo_activity);
        ImageButton back = (ImageButton) findViewById(R.id.backEditT);
        final EditText nombre = (EditText) findViewById(R.id.nombreET);
        final EditText tomo = (EditText) findViewById(R.id.tomoET);
        final EditText anho = (EditText) findViewById(R.id.anoET);
        final EditText descripcion = (EditText) findViewById(R.id.descET);
        final EditText nombreautor = (EditText) findViewById(R.id.autET);
        final EditText nombredibujante = (EditText) findViewById(R.id.dibET);
        Button enviar = (Button) findViewById(R.id.sendT);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DynamicArray<Categoria> cats = new DynamicArray<Categoria>();
                String[] catStrings = tomo.getText().toString().split(",");
                for(int i=0;i<catStrings.length;i++) {
                    cats.pushBack(new Categoria(catStrings[i]));
                }
                Tomo adding = new Tomo(nombre.getText().toString().replaceAll(" ","_"), new Autor(nombreautor.getText().toString().replaceAll(" ","_"),30,null),
                        new Autor(nombredibujante.getText().toString().replaceAll(" ","_"),30,null),
                        Integer.parseInt(anho.getText().toString()),true,cats );
                adding.setDescripcion(descripcion.getText().toString());
                MainActivity.catalogo.getTomos().pushBack(adding);
                MainActivity.catalogo.escribirTomos();
                finish();
            }

        });
    }
}
