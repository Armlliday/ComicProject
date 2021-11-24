package registro;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectocomic.MainActivity;
import com.example.proyectocomic.R;
import com.example.proyectocomic.structures.Stack;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import navigation.BottonNavigation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class registrar extends AppCompatActivity {

    private EditText mEmail ;
    private EditText mPassword;
    private EditText mName;
    private EditText mTelefono;
    private EditText mProfesion;
    private Button mButtonRegister;
    private TextView Backlogin;
    private ProgressDialog progressDialog;

    //Variables de los datos a registrar
    private String email = "";
    private String password = "";
    private String name = "";
    private String telefono = "";
    private String profesion = "";

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mEmail = (EditText) findViewById(R.id.email_edittext);
        mPassword = (EditText) findViewById(R.id.password_edittext);
        mName = (EditText) findViewById(R.id.fullname_edittext);
        mTelefono = (EditText) findViewById(R.id.phone_edittext);
        mProfesion = findViewById(R.id.profesion_edittext);
        mButtonRegister = (Button) findViewById(R.id.botonRegistrar);
        progressDialog = new ProgressDialog(this);
        Backlogin = findViewById(R.id.backlogin);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Backlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(registrar.this, MainActivity.class));
            }
        });

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = mEmail.getText().toString();
                password = mPassword.getText().toString();
                name = mName.getText().toString();
                telefono = mTelefono.getText().toString();
                profesion= mProfesion.getText().toString();


                if (!name.isEmpty() && !password.isEmpty() && !email.isEmpty() && !telefono.isEmpty() && !profesion.isEmpty()){
                    if(password.length()>=6 ){
                        try {
                            registerUser();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    else{ Toast.makeText(registrar.this, "Debe ingresar al menos 6 caracteres",Toast.LENGTH_LONG);
                    }

                }
                else {
                    Toast.makeText(registrar.this, "Debe completar los campos",Toast.LENGTH_LONG);
                }
                progressDialog.setMessage("Realizando registro en linea...");
                progressDialog.show();

            }
        });
    }
    private void registerUser() throws FileNotFoundException, IOException {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Map<String, Object> map = new HashMap<>();
                    map.put("Name", name);
                    map.put("Email", email);
                    map.put("Password", password);
                    map.put("Telefono", telefono);
                    map.put("Profesion", profesion);
                    map.put("Super", "false");

                    //map.put("Busquedas", new Stack<String>());

                    String id = mAuth.getCurrentUser().getUid();
                    mDatabase.child("Users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if(task2.isSuccessful()){
                                startActivity(new Intent(registrar.this,BottonNavigation.class ));
                                finish();
                            }
                            else{
                                Toast.makeText(registrar.this, "No se crearon los datos Correctamente", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }
                else {
                    Toast.makeText(registrar.this, "No se pudo registrar este usuario", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });

    }
}
