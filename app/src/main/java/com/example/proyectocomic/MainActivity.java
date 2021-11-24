package com.example.proyectocomic;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectocomic.catalogo.Catalogo;
import com.example.proyectocomic.catalogo.DBHelper;
import com.example.proyectocomic.comics.Comic;
import com.example.proyectocomic.comics.Hashmap;
import com.example.proyectocomic.comics.Tomo;
import com.example.proyectocomic.structures.BinaryHeap;
import com.example.proyectocomic.structures.BinarySearchTree;
import com.example.proyectocomic.structures.DynamicArray;
import com.example.proyectocomic.structures.Node;
import com.example.proyectocomic.structures.PriorityComic;
import com.example.proyectocomic.structures.Queue;
import com.example.proyectocomic.structures.SinglyLinkedList;
import com.example.proyectocomic.structures.Stack;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.HashMap;
import java.util.Scanner;

import registro.registrar;
import navigation.BottonNavigation;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //defining view objects
    private EditText TextEmail;
    private EditText TextPassword;
    private TextView btregistrar;
    private Button  btnLogin;
    private ProgressDialog progressDialog;
    public static BinarySearchTree<Comic> favorites;
    public static Stack<String> recentSearches;
    public static Queue<String[]> updatables;
    public static BinaryHeap<PriorityComic> addingComics;

    //Declaramos un objeto firebaseAuth
    static public FirebaseAuth firebaseAuth;
    static public FirebaseStorage mStorageRef;


    public static File localFileA;
    public static File localFileC;
    public static File localFileE;
    public static File localFileL;
    public static File localFileT;
    public static Catalogo catalogo;
    public static File tempA;
    public static File tempC;
    public static File tempE;
    public static File tempL;
    public static File tempT;
    public static File fileStack;
    public static File tempFileStack;
    public static boolean Super;
    private  DatabaseReference mDatabase;
    public static File fileFavs;
    public static File tempFileFavs;
    public static File fileUpdatables;
    public static File tempFileUpdatables;
    public static File fileAddables;
    public static File tempFileAddables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //inicializamos el objeto firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        //inicializamos el objeto StorageReference
        mStorageRef = FirebaseStorage.getInstance();

        //favoritos
        favorites = new BinarySearchTree<Comic>();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        //queue actualizaciones
        updatables = new Queue<String[]>();
        fileUpdatables = getBaseContext().getFileStreamPath("updatables.txt");

       mStorageRef.getReferenceFromUrl("gs://rproyectocomic.appspot.com/").child("updatables.txt")
                .getFile(fileUpdatables).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.e("firebases",";local tem file created  created " +fileUpdatables.toString());
                //  updateDb(timestamp,localFile.toString(),position);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("firebases",";local tem file not created  created " +exception.toString());
            }
        });
        try {
            Scanner reader = new Scanner(new FileInputStream(fileUpdatables));
            reader.next();
            int counter = Integer.parseInt(reader.next());
            reader.nextLine();
            while(counter!=0){
                String[] update = new String[6];
                update[0] = reader.next().replaceAll("_"," ");
                update[1] = reader.next().replaceAll("_"," ");
                update[2] = reader.next();
                update[3] = reader.next().replaceAll("_"," ");
                update[4] = reader.next().replaceAll("_"," ");
                update[5] = reader.next().replaceAll("_"," ");
                updatables.enqueue(update);
                counter--;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        tempFileUpdatables = getBaseContext().getFileStreamPath("tempupdatables.txt");

        //cómics para agregar
        addingComics = new BinaryHeap<PriorityComic>();

        fileAddables = getBaseContext().getFileStreamPath("addables.txt");

        mStorageRef.getReferenceFromUrl("gs://rproyectocomic.appspot.com/").child("addables.txt")
                .getFile(fileAddables).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.e("firebases",";local tem file created  created " +fileAddables.toString());
                //  updateDb(timestamp,localFile.toString(),position);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("firebases",";local tem file not created  created " +exception.toString());
            }
        });
        try {
            Scanner reader = new Scanner(new FileInputStream(fileAddables));
            reader.next();
            int counter = Integer.parseInt(reader.next());
            reader.nextLine();
            while(counter!=0){
                Comic cAdding = new Comic(reader.next().replaceAll("_"," "),
                        Integer.parseInt(reader.next()),null,null,reader.next().replaceAll("_"," "),0);
                PriorityComic adding = new PriorityComic(cAdding,reader.next().replaceAll("_"," "),
                        reader.next().replaceAll("_"," "), reader.next().replaceAll("_"," "),
                        reader.next().replaceAll("_"," "));

                addingComics.insert(adding);
                counter--;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        tempFileAddables = getBaseContext().getFileStreamPath("tempaddables.txt");

        //Referenciamos los views
        TextEmail = (EditText) findViewById(R.id.editTextEmail);
        TextPassword = (EditText) findViewById(R.id.editTextPassword);
        btregistrar = findViewById(R.id.botonRegistre);
        btnLogin = (Button) findViewById(R.id.botonLogin);

        progressDialog = new ProgressDialog(this);

        //attaching listener to button
        btregistrar.setOnClickListener(this);
        btnLogin.setOnClickListener(this);


        //Creación del catálogo


        tempA = this.getFileStreamPath("tempautor.txt");
        tempC = this.getFileStreamPath("tempcategoria.txt");
        tempE = this.getFileStreamPath("tempeditorial.txt");
        tempL = this.getFileStreamPath("templenguaje.txt");
        tempT = this.getFileStreamPath("temptomo.txt");



        File rootPathA = this.getFileStreamPath("autor.txt");
        if(!rootPathA.exists()) {
            rootPathA.mkdirs();
        }
            localFileA = this.getFileStreamPath("autor.txt");

        mStorageRef.getReferenceFromUrl("gs://rproyectocomic.appspot.com/").child("autor.txt")
                .getFile(localFileA).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.e("firebases",";local tem file created  created " +localFileA.toString());
                //  updateDb(timestamp,localFile.toString(),position);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("firebases",";local tem file not created  created " +exception.toString());
            }
        });

        File rootPathC = this.getFileStreamPath("categoria.txt");
        if(!rootPathC.exists()) {
            rootPathC.mkdirs();
        }
       localFileC = this.getFileStreamPath("categoria.txt");

        mStorageRef.getReferenceFromUrl("gs://rproyectocomic.appspot.com/").child("categoria.txt")
                .getFile(localFileC).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.e("firebases",";local tem file created  created " +localFileC.toString());
                //  updateDb(timestamp,localFile.toString(),position);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("firebases",";local tem file not created  created " +exception.toString());
            }
        });

        File rootPathE = this.getFileStreamPath("editorial.txt");
        if(!rootPathE.exists()) {
            rootPathE.mkdirs();
        }
         localFileE = this.getFileStreamPath("editorial.txt");

        mStorageRef.getReferenceFromUrl("gs://rproyectocomic.appspot.com/").child("editorial.txt")
                .getFile(localFileE).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.e("firebases",";local tem file created  created " +localFileE.toString());
                //  updateDb(timestamp,localFile.toString(),position);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("firebases",";local tem file not created  created " +exception.toString());
            }
        });

        File rootPathL = this.getFileStreamPath("lenguaje.txt");
        if(!rootPathL.exists()) {
            rootPathL.mkdirs();
        }
         localFileL = this.getFileStreamPath("lenguaje.txt");

        mStorageRef.getReferenceFromUrl("gs://rproyectocomic.appspot.com/").child("lenguaje.txt")
                .getFile(localFileL).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.e("firebases",";local tem file created  created " +localFileL.toString());
                //  updateDb(timestamp,localFile.toString(),position);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("firebases",";local tem file not created  created " +exception.toString());
            }
        });

        File rootPathT = this.getFileStreamPath("tomo.txt");
        if(!rootPathT.exists()) {
            rootPathT.mkdirs();
        }
       localFileT = this.getFileStreamPath("tomo.txt");

        mStorageRef.getReferenceFromUrl("gs://rproyectocomic.appspot.com/").child("tomo.txt")
                .getFile(localFileT).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.e("firebases",";local tem file created  created " +localFileT.toString());
                //  updateDb(timestamp,localFile.toString(),position);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("firebases",";local tem file not created  created " +exception.toString());
            }
        });




        Scanner scannerCat = null;
        Scanner scannerAut = null;
        Scanner scannerTom = null;
        Scanner scannerLen = null;
        Scanner scannerEdi = null;
/*
        try {
            scannerCat = new Scanner(getAssets().open("categoria.txt"));
            scannerAut = new Scanner(getAssets().open("autor.txt"));
            scannerTom = new Scanner(getAssets().open("tomo.txt"));
            scannerLen = new Scanner(getAssets().open("lenguaje.txt"));
            scannerEdi = new Scanner(getAssets().open("editorial.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
        try {
            InputStream inputA = new FileInputStream(localFileA);
            InputStream inputC = new FileInputStream(localFileC);
            InputStream inputE = new FileInputStream(localFileE);
            InputStream inputL = new FileInputStream(localFileL);
            InputStream inputT = new FileInputStream(localFileT);
            scannerCat = new Scanner(inputC);
            scannerAut = new Scanner(inputA);
            scannerTom = new Scanner(inputT);
            scannerLen = new Scanner(inputL);
            scannerEdi = new Scanner(inputE);

        } catch (IOException e) {
            e.printStackTrace();
        }
        catalogo = new Catalogo();
        catalogo.leerDatos(scannerCat, scannerAut, scannerTom, scannerLen,scannerEdi);
        Log.d("Pera",""+catalogo.getTomos().getSize());
        DBHelper db = new DBHelper(getApplicationContext());
        MainActivity.catalogo.index(db);

        /*DynamicArray<Tomo> res = catalogo.buscarPorAutor("al4n more");
        System.out.println(res.getSize());
        for(int i = 0; i < res.getSize(); ++i){
            Log.d("MyApp",res.get(i).getNombre());
        }
        try {
            AssetManager am = this.getAssets();
            InputStream inputStreamA = am.open("autor.txt");
            InputStream inputStreamC = am.open("categoria.txt");
            InputStream inputStreamE = am.open("editorial.txt");
            InputStream inputStreamL = am.open("lenguaje.txt");
            InputStream inputStreamT = am.open("tomo.txt");

            fileA = this.getFileStreamPath("autor.txt");
            OutputStream outputStreamA = new FileOutputStream(fileA);
            fileC = this.getFileStreamPath("categoria.txt");
            OutputStream outputStreamC = new FileOutputStream(fileC);
            fileE = this.getFileStreamPath("editorial.txt");
            OutputStream outputStreamE = new FileOutputStream(fileE);
            fileL = this.getFileStreamPath("lenguaje.txt");
            OutputStream outputStreamL = new FileOutputStream(fileL);
            fileT = this.getFileStreamPath("tomo.txt");
            OutputStream outputStreamT = new FileOutputStream(fileT);


            ByteBuffer bufferA = ByteBuffer.allocateDirect(1024 * 8);

            ReadableByteChannel ichA = Channels.newChannel(inputStreamA);
            WritableByteChannel ochA = Channels.newChannel(outputStreamA);

            while (ichA.read(bufferA) > -1 || bufferA.position() > 0)
            {
                bufferA.flip();
                ochA.write(bufferA);
                bufferA.compact();
            }
            ichA.close();
            ochA.close();

            ByteBuffer bufferC = ByteBuffer.allocateDirect(1024 * 8);

            ReadableByteChannel ichC = Channels.newChannel(inputStreamC);
            WritableByteChannel ochC = Channels.newChannel(outputStreamC);

            while (ichC.read(bufferC) > -1 || bufferC.position() > 0)
            {
                bufferC.flip();
                ochC.write(bufferC);
                bufferC.compact();
            }
            ichC.close();
            ochC.close();

            ByteBuffer bufferE = ByteBuffer.allocateDirect(1024 * 8);

            ReadableByteChannel ichE = Channels.newChannel(inputStreamE);
            WritableByteChannel ochE = Channels.newChannel(outputStreamE);

            while (ichE.read(bufferE) > -1 || bufferE.position() > 0)
            {
                bufferE.flip();
                ochE.write(bufferE);
                bufferE.compact();
            }
            ichE.close();
            ochE.close();

            ByteBuffer bufferL = ByteBuffer.allocateDirect(1024 * 8);

            ReadableByteChannel ichL = Channels.newChannel(inputStreamL);
            WritableByteChannel ochL = Channels.newChannel(outputStreamL);

            while (ichL.read(bufferL) > -1 || bufferL.position() > 0)
            {
                bufferL.flip();
                ochL.write(bufferL);
                bufferL.compact();
            }
            ichL.close();
            ochL.close();

            ByteBuffer bufferT = ByteBuffer.allocateDirect(1024 * 8);

            ReadableByteChannel ichT = Channels.newChannel(inputStreamT);
            WritableByteChannel ochT = Channels.newChannel(outputStreamT);

            while (ichT.read(bufferT) > -1 || bufferT.position() > 0)
            {
                bufferT.flip();
                ochT.write(bufferT);
                bufferT.compact();
            }
            ichT.close();
            ochT.close();

            FileReader fr = new FileReader(fileT);
            char chr = (char) fr.read(); // read char


            FileWriter fw = new FileWriter(fileT,true);
            //fw.write("word"); // write string
            //fw.append("r");

            //char chr2 = (char) fr.read(); // read char
            //char chr3 = (char) fr.read(); // read char
            fw.close();
            fr.close();
            //Log.d("Peras",chr+" "+chr2+" "+chr3+" "+f.getAbsolutePath());

            Uri fileAU = Uri.fromFile(fileA);
            StorageReference riversRefA = mStorageRef.getReferenceFromUrl("gs://rproyectocomic.appspot.com/").child("autor.txt");
            UploadTask uploadTaskA = riversRefA.putFile(fileAU);
            uploadTaskA = riversRefA.putFile(fileAU);

            Uri fileCA = Uri.fromFile(fileC);
            StorageReference riversRefC = mStorageRef.getReferenceFromUrl("gs://rproyectocomic.appspot.com/").child("catalogo.txt");
            UploadTask uploadTaskC = riversRefC.putFile(fileCA);
            uploadTaskC = riversRefC.putFile(fileCA);

            Uri fileED = Uri.fromFile(fileE);
            StorageReference riversRefE = mStorageRef.getReferenceFromUrl("gs://rproyectocomic.appspot.com/").child("editorial.txt");
            UploadTask uploadTaskE = riversRefE.putFile(fileED);
            uploadTaskE = riversRefE.putFile(fileED);


            Uri fileLE = Uri.fromFile(fileL);
            StorageReference riversRefL = mStorageRef.getReferenceFromUrl("gs://rproyectocomic.appspot.com/").child("lenguaje.txt");
            UploadTask uploadTaskL = riversRefL.putFile(fileLE);
            uploadTaskL = riversRefL.putFile(fileLE);

            Uri fileTO = Uri.fromFile(fileT);
            StorageReference riversRefT = mStorageRef.getReferenceFromUrl("gs://rproyectocomic.appspot.com/").child("tomo.txt");
            UploadTask uploadTaskT = riversRefT.putFile(fileTO);
            uploadTaskT = riversRefT.putFile(fileTO);

// Register observers to listen for when the download is done or if it fails
            uploadTaskA.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                }
            });

            uploadTaskC.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                }
            });

            uploadTaskE.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                }
            });

            uploadTaskL.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                }
            });

            uploadTaskT.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                }
            });


            inputStreamA.close(); // close the file
            inputStreamC.close(); // close the file
            inputStreamE.close(); // close the file
            inputStreamL.close(); // close the file
            inputStreamT.close(); // close the file
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }

    private void registrarUsuario(){

        startActivity(new Intent(MainActivity.this, registrar.class ));

    }


    private void  logUsuario(){
        //Obtenemos el email y la contraseña desde las cajas de texto
        String email = TextEmail.getText().toString().trim();
        String password  = TextPassword.getText().toString().trim();

        //Verificamos que las cajas de texto no esten vacías
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Se debe ingresar un email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Falta ingresar la contraseña",Toast.LENGTH_LONG).show();
            return;
        }


        progressDialog.setMessage("Realizando consulta  en línea...");
        progressDialog.show();

        //loguear usuario
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){

                            Toast.makeText(MainActivity.this,"Bienvenido al mejor catálogo "+ TextEmail.getText(),Toast.LENGTH_LONG).show();
                            //busquedas recientes
                            fileStack = getBaseContext().getFileStreamPath(firebaseAuth.getUid()+".txt");
                            recentSearches = new Stack<String>();
                            mStorageRef.getReferenceFromUrl("gs://rproyectocomic.appspot.com/").child("Busquedas").child(firebaseAuth.getUid()+".txt")
                                    .getFile(fileStack).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Log.e("firebases",";local tem file created  created " +fileStack.toString());
                                    //  updateDb(timestamp,localFile.toString(),position);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Log.e("firebases",";local tem file not created  created " +exception.toString());
                                }
                            });
                            try {
                                Scanner reader = new Scanner(new FileInputStream(fileStack));
                                reader.next();
                                int counter = Integer.parseInt(reader.next());
                                reader.nextLine();
                                while(counter!=0){
                                    recentSearches.push(reader.nextLine());
                                    counter--;
                                }
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                            tempFileStack = getBaseContext().getFileStreamPath("temp"+firebaseAuth.getUid()+".txt");

                            tempFileFavs = getBaseContext().getFileStreamPath("f_temp"+firebaseAuth.getUid()+".txt");


                            fileFavs = getBaseContext().getFileStreamPath("f_"+firebaseAuth.getUid()+".txt");

                            mStorageRef.getReferenceFromUrl("gs://rproyectocomic.appspot.com/").child("Favoritos").child("f_"+firebaseAuth.getUid()+".txt")
                                    .getFile(fileFavs).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Log.e("firebases",";local tem file created  created " +fileFavs.toString());
                                    //  updateDb(timestamp,localFile.toString(),position);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Log.e("firebases",";local tem file not created  created " +exception.toString());
                                }
                            });
                            try {
                                Scanner reader = new Scanner(new FileInputStream(fileFavs));
                                reader.next();
                                int counter = Integer.parseInt(reader.next());
                                reader.nextLine();
                                while(counter!=0){
                                    DynamicArray<Comic> busqueda = catalogo.buscarPorNombre(reader.next(),1);
                                    Comic fav = busqueda.get(0);

                                    favorites.root = favorites.Insert(fav,favorites.root);
                                    counter--;
                                }
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }




                            mDatabase.child("Users").child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.exists()) {
                                        String s = dataSnapshot.child("Super").getValue().toString();

                                        Super = Boolean.parseBoolean(s);

                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            startActivity( new Intent(MainActivity.this, BottonNavigation.class) );
                            finish();
                        }else{
                            if(task.getException()instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(MainActivity.this, "Este usuario ya existe", Toast.LENGTH_SHORT).show();
                            }

                            else {
                                Toast.makeText(MainActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                            }
                        }
                        progressDialog.dismiss();
                    }
                });


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.botonRegistre:
                registrarUsuario();
                break;
            case  R.id.botonLogin:
                logUsuario();
        }
    }


}
