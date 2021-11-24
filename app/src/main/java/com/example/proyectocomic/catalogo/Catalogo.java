package com.example.proyectocomic.catalogo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.util.Log;

import com.example.proyectocomic.MainActivity;
import com.example.proyectocomic.comics.Autor;
import com.example.proyectocomic.comics.Categoria;
import com.example.proyectocomic.comics.Comic;
import com.example.proyectocomic.comics.Editorial;
import com.example.proyectocomic.comics.Lenguaje;
import com.example.proyectocomic.comics.Tomo;
import com.example.proyectocomic.comics.Usuario;
import com.example.proyectocomic.structures.DynamicArray;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.EmptyStackException;
import java.util.Scanner;

import org.apache.lucene.document.Document;

public class Catalogo { 
      
	private DynamicArray<Editorial> editoriales;
	private DynamicArray<Tomo> tomos;
	private DynamicArray<Autor> autores;
	private DynamicArray<Categoria> categorias;
	private DynamicArray<Usuario> usuarios;
    private DynamicArray<Lenguaje> lenguajes;

    private SQLiteDatabase db;
    private boolean existeIndex;

        
        //Constructores
        public Catalogo(DynamicArray<Editorial> editoriales, DynamicArray<Tomo> tomos, DynamicArray<Autor> autores, DynamicArray<Categoria> categorias, DynamicArray<Usuario> usuarios) {
            this.editoriales = editoriales;
            this.tomos = tomos;
            this.autores = autores;
            this.categorias = categorias;
            this.usuarios = usuarios;
            this.existeIndex = false;
        }
        public Catalogo()
        {
            this(null,null,null,null,null);
        }

    public void index(DBHelper helper) {
        this.db = helper.getWritableDatabase();

        for (int i = 0; i < this.getTomos().getSize(); ++i) {
            Tomo tomo = this.getTomos().get(i);
            Comic comic = tomo.getCabeza();

            for (int j = 0; j < tomo.getTamano(); ++j) {
                String nombre_c = comic.getNombre();
                String dibujante_c = comic.getDibujante().getNombre();
                String escritor_c = comic.getEscritor().getNombre();
                String tomo_n = tomo.getNombre();
                String categorias_c = tomo.getCategorias().toString();

                String sql = "INSERT INTO tabla (nombre, dibujante, escritor, tomo, categorias) VALUES (?,?,?,?,?)";
                SQLiteStatement statement = db.compileStatement(sql);

                statement.bindString(1, nombre_c); // 1-based: matches first '?' in sql string
                statement.bindString(2, dibujante_c);  // matches second '?' in sql string
                statement.bindString(3, escritor_c);
                statement.bindString(4, tomo_n);
                statement.bindString(5, categorias_c);
                long rowId = statement.executeInsert();

                comic = comic.getSecuela();
            }
            this.existeIndex = true;
        }
    }
	/*public Comic[] buscarPorAgno(int agno){
		return null;
		
	}*/
    private Comic buscarComic(String string, Tomo buscarTomo) {
        Comic comic = buscarTomo.getCabeza();
        for(int i = 0; i < buscarTomo.getTamano(); ++i)
        {
            if(string.equalsIgnoreCase(comic.getNombre())) return comic;
            comic = comic.getSecuela();
        }
        return null;
    }

    public DynamicArray<Comic> buscarPorAutor(String nombre){

        DynamicArray<Comic> res = new DynamicArray();

        String[] nombreApellido = nombre.split(" ");

        if(nombre.length() < 3) {
            System.out.println("El nombre del autor debe contener almenos 3 caracteres");
            return res;
        }

        else if(nombreApellido.length > 2)
        {
            System.out.println("Solo se debe incluir el nombre y el apellido del autor a buscar");
            return res;
        }


        if(existeIndex) {
            DynamicArray<String[]> resultado = null;

            if (nombreApellido.length == 2) {
                String[] sqlSelect = {"nombre", "dibujante", "escritor", "tomo", "categorias"};
                String name = nombreApellido[0];
                String lastname = nombreApellido[1];
                SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
                qb.setTables("tabla");
                Cursor cursor = qb.query(this.db, sqlSelect, "dibujante LIKE '%" + name +"%' OR escritor LIKE '%" + name + "%' OR dibujante LIKE '%" + lastname +"%'  OR escritor LIKE '%" + lastname + "%'" , null, null, null, "nombre");

                if (cursor.moveToFirst()) {
                    Comic comic = null;
                    do {
                        String nombreComic = cursor.getString(cursor.getColumnIndex("nombre"));
                        String nombreTomo = cursor.getString(cursor.getColumnIndex("tomo"));

                        comic = this.buscarComic(nombreComic, this.buscarTomo(nombreTomo));
                        res.pushBackCheck(comic);

                    } while (cursor.moveToNext());
                }
            }
            else {
                String name = nombreApellido[0];
                SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
                qb.setTables("tabla");
                String[] sqlSelect = {"nombre", "dibujante", "escritor", "tomo", "categorias"};
                Cursor cursor = qb.query(this.db, sqlSelect, "dibujante LIKE '%" + name +"%' OR escritor LIKE '%" + name + "%'", null, null, null, "nombre");

                if (cursor.moveToFirst()) {
                    Comic comic = null;
                    do {
                        String nombreComic = cursor.getString(cursor.getColumnIndex("nombre"));
                        String nombreTomo = cursor.getString(cursor.getColumnIndex("tomo"));

                        comic = this.buscarComic(nombreComic, this.buscarTomo(nombreTomo));
                        if(comic==null||comic.getNombre()==null||comic.getEscritor()==null||comic.getDibujante()==null||comic.getEscritor().getNombre()==null) continue;
                        res.pushBackCheck(comic);

                    } while (cursor.moveToNext());
                }
            }
        }
        return res;
    }



    public DynamicArray<Comic> buscarPorNombre(String nombre,int max){

        DynamicArray<Comic> res = new DynamicArray();

        if(existeIndex) {
            SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
            String[] sqlSelect = {"nombre", "dibujante", "escritor", "tomo", "categorias"};
            qb.setTables("tabla");
            Cursor cursor = qb.query(this.db, sqlSelect, "nombre LIKE ?", new String[]{"%" + nombre + "%"}, null, null, "nombre");


            if (cursor.moveToFirst()) {
                Comic comic = null;
                do {
                    String nombreComic = cursor.getString(cursor.getColumnIndex("nombre"));
                    String nombreTomo = cursor.getString(cursor.getColumnIndex("tomo"));

                    comic = this.buscarComic(nombreComic, this.buscarTomo(nombreTomo));
                    if(comic==null||comic.getNombre()==null||comic.getEscritor()==null||comic.getDibujante()==null||comic.getEscritor().getNombre()==null) continue;
                    res.pushBackCheck(comic);

                } while (cursor.moveToNext()&&res.getSize()!=max);
            }
        }
        return res;

    }
    public DynamicArray<Comic> buscarPorNombre(String nombre){

        DynamicArray<Comic> res = new DynamicArray();

        if(existeIndex) {
            SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
            String[] sqlSelect = {"nombre", "dibujante", "escritor", "tomo", "categorias"};
            qb.setTables("tabla");
            Cursor cursor = qb.query(this.db, sqlSelect, "nombre LIKE ?", new String[]{"%" + nombre + "%"}, null, null, "nombre");


            if (cursor.moveToFirst()) {
                Comic comic = null;
                do {
                    String nombreComic = cursor.getString(cursor.getColumnIndex("nombre"));
                    String nombreTomo = cursor.getString(cursor.getColumnIndex("tomo"));

                    comic = this.buscarComic(nombreComic, this.buscarTomo(nombreTomo));
                    if(comic==null||comic.getNombre()==null||comic.getEscritor()==null||comic.getDibujante()==null||comic.getEscritor().getNombre()==null) continue;
                    res.pushBackCheck(comic);

                } while (cursor.moveToNext());
            }
        }
        return res;

    }

    public DynamicArray<Comic> buscarPorCategoria(String categoria){
        DynamicArray<Comic> res = new DynamicArray();

        if(categoria.length() < 3) {
            System.out.println("La categoria a buscar debe contener almenos 3 caracteres");
            return res;
        }

        if(existeIndex)
        {

            SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
            String[] sqlSelect = {"nombre", "dibujante", "escritor", "tomo", "categorias"};
            qb.setTables("tabla");
            Cursor cursor = qb.query(this.db, sqlSelect, "categorias LIKE ?", new String[]{"%" + categoria + "%"}, null, null, "nombre");

            if (cursor.moveToFirst()) {
                Comic comic = null;
                do {
                    String nombreComic = cursor.getString(cursor.getColumnIndex("nombre"));
                    String nombreTomo = cursor.getString(cursor.getColumnIndex("tomo"));

                    comic = this.buscarComic(nombreComic, this.buscarTomo(nombreTomo));
                    res.pushBackCheck(comic);

                } while (cursor.moveToNext());
            }
        }

        return res;

    }
    //PERMITIR BUSCAR POR CATEGORIAS, SIMILAR AL DE AUTOR Y NOMBRE.
	
	public void actualizarComic(Comic comic, int tipoOperacion, String nuevaInformacion){
		
	}
        
        //Persistencia


    public void leerDatos(Scanner scannerCat,Scanner scannerAut,Scanner scannerTom,Scanner scannerLen,Scanner scannerEdi)
    {
        Scanner flujo_entrada = null;
        int iteraciones = 0;
        DynamicArray array = null;
        //Lectura categoria

        try {
            flujo_entrada = scannerCat;
            String primera_linea = flujo_entrada.nextLine();
            Scanner temp_scanner = new Scanner(primera_linea);
            temp_scanner.next();
            iteraciones = temp_scanner.nextInt();
        } catch (Exception ex){System.out.println("Error en la lectura de categorias");}

        array = new DynamicArray<Categoria>();

        for(;iteraciones > 0;--iteraciones)
        {
            Categoria categoria = new Categoria(flujo_entrada.nextLine());
            array.pushBackCheck(categoria);
        }
        this.setCategorias(array);

        //Lectura autor
        try {
            flujo_entrada = scannerAut;
            String primera_linea = flujo_entrada.nextLine();
            Scanner temp_scanner = new Scanner(primera_linea);
            temp_scanner.next();
            iteraciones = temp_scanner.nextInt();
        } catch (Exception ex){System.out.println("Error en la lectura de autor");}
        array = new DynamicArray<Autor>();

        for(;iteraciones > 0;--iteraciones){

            String nombre = flujo_entrada.next();
            int edad = flujo_entrada.nextInt();
            Autor autor = new Autor(nombre,edad);
            boolean existe_autor = false;
            if(array.pushBackCheck(autor)) //Si ya existe un autor, entonces basta con buscar ese autor y comparar categorias
            {
                autor = buscarAutor(nombre, edad);
                existe_autor = true;
            }
            DynamicArray<Categoria> categorias = new DynamicArray<Categoria>();

            int numero_categorias = flujo_entrada.nextInt();
            for(int i = 0; i < numero_categorias; ++i)
            {
                Categoria categoria = new Categoria(flujo_entrada.next());
                if(existe_autor){
                    autor.getCategorias().pushBackCheck(categoria);
                }
                else if(i == 0) {
                    autor.setCategorias(categorias);
                    autor.getCategorias().pushBackCheck(categoria);
                }
                else autor.getCategorias().pushBackCheck(categoria);


            }
        }
        this.setAutores(array);

        //Lectura tomo
        array = new DynamicArray<Tomo>();

        try {
            flujo_entrada = scannerTom;
            String primera_linea = flujo_entrada.nextLine();
            Log.d("Pero",primera_linea);
            Scanner temp_scanner = new Scanner(primera_linea);
            temp_scanner.next();
            iteraciones = temp_scanner.nextInt();
        } catch (Exception ex){System.out.println("Error en la lectura de tomos");
            Log.d("Pera","Error");}

        for(;iteraciones > 0;--iteraciones){
            String nombre = flujo_entrada.next();
            int agno_publicacion = flujo_entrada.nextInt();
            String s_escritor = flujo_entrada.next();
            String s_dibujante = flujo_entrada.next();
            boolean finalizado = flujo_entrada.nextBoolean();
            boolean tieneDescripcion = flujo_entrada.nextBoolean();
            String descripcion = null;

            if(tieneDescripcion)
            {
                flujo_entrada.nextLine();
                descripcion = flujo_entrada.nextLine();

            }

            int n_categorias = flujo_entrada.nextInt();
            DynamicArray<Categoria> tomo_categorias = new DynamicArray<Categoria>();
            for(int i = 0; i < n_categorias; ++i){
                Categoria categoria = new Categoria(flujo_entrada.next());
                tomo_categorias.pushBackCheck(categoria);
            }

            Autor escritor = null;

            Autor dibujante = null;

            for(int i = 0; i < this.getAutores().getSize(); ++i){
                String t_autor = this.getAutores().get(i).getNombre();
                if(t_autor.equalsIgnoreCase(s_escritor)){
                    s_escritor = null;

                    escritor = this.getAutores().get(i);
                }

                if(t_autor.equalsIgnoreCase(s_dibujante)){
                    s_dibujante = null;

                    dibujante = this.getAutores().get(i);
                }

                if(s_escritor == null && s_dibujante == null) break;

            }
            if(s_escritor != null){
                escritor = new Autor(s_escritor);
            }

            if(s_dibujante != null){
                dibujante = new Autor(s_dibujante);
                this.getAutores().pushBack(dibujante);
            }

            //Lectura comic dentro de tomo
            int n_comics = flujo_entrada.nextInt();
            Tomo tomo = new Tomo(nombre, escritor, dibujante, agno_publicacion, finalizado, tomo_categorias);
            tomo.setDescripcion(descripcion);

            for(int i = 0; i < n_comics; ++i){
                String nombre_c = flujo_entrada.next();
                int agno_publicacion_c = flujo_entrada.nextInt();
                String s_escritor_c = flujo_entrada.next();
                String s_dibujante_c = flujo_entrada.next();
                boolean tieneDescripcionComic = flujo_entrada.nextBoolean();
                String descripcion_c = null;

                if(tieneDescripcionComic)
                {
                    flujo_entrada.nextLine();
                    descripcion_c = flujo_entrada.nextLine();

                }

                Autor escritor_c = null;

                Autor dibujante_c = null;

                for(int j = 0; j < this.getAutores().getSize(); ++j){
                    String t_autor_c = this.getAutores().get(j).getNombre();
                    if(t_autor_c.equalsIgnoreCase(s_escritor_c)){
                        s_escritor_c = null;

                        escritor_c = this.getAutores().get(j);
                    }

                    if(t_autor_c.equalsIgnoreCase(s_dibujante_c)){
                        s_dibujante_c = null;

                        dibujante_c = this.getAutores().get(j);
                    }

                    if(s_escritor_c == null && s_dibujante_c == null) break;

                }
                if(s_escritor_c != null){
                    escritor_c = new Autor(s_escritor_c);
                }

                if(s_dibujante_c != null){
                    dibujante_c = new Autor(s_dibujante_c);
                }

                Comic comic = new Comic(nombre_c, agno_publicacion_c, escritor_c, dibujante_c, descripcion_c,0);
                tomo.agregarAtras(comic);

            }
            array.pushBack(tomo);
        }
        this.setTomos(array);


        //Lectura editorial, los tomos ya deben haber sido creados antes de aÃ±adirlos a un editorial
        array = new DynamicArray<Editorial>();
        try {
            flujo_entrada = scannerEdi;
            String primera_linea = flujo_entrada.nextLine();
            Scanner temp_scanner = new Scanner(primera_linea);
            temp_scanner.next();
            iteraciones = temp_scanner.nextInt();
        } catch (Exception ex){System.out.println("Error en la lectura de editoriales");}

        for(;iteraciones > 0;--iteraciones){
            String nombre = flujo_entrada.next();

            DynamicArray<Tomo> tomos = new DynamicArray<Tomo>();

            Editorial editorial = new Editorial(nombre,tomos);

            int n_tomos = flujo_entrada.nextInt();

            for(int i = 0; i < n_tomos; ++i){
                String nombre_t = flujo_entrada.next();

                int agno_publicacion_t = flujo_entrada.nextInt();

                Tomo tomo = null;

                for(int j = 0; j < this.getTomos().getSize(); ++j){
                    Tomo temp = this.getTomos().get(j);
                    if(temp.getNombre().equalsIgnoreCase(nombre_t) && temp.getAgno_publicacion() == agno_publicacion_t){
                        tomo = temp;
                        break;
                    }
                }

                tomos.pushBack(tomo);
                tomo.setEditorial(editorial);
                array.pushBack(editorial);
                editorial.setTomos(tomos);
            }

        }
        this.setEditoriales(array);

        //Lectura lenguaje, crear tomos antes de agnadirlos a Lenguaje
        array = new DynamicArray<Lenguaje>();
        try {
            flujo_entrada = scannerLen;
            String primera_linea = flujo_entrada.nextLine();
            Scanner temp_scanner = new Scanner(primera_linea);
            temp_scanner.next();
            iteraciones = temp_scanner.nextInt();
        } catch (Exception ex){System.out.println("Error en la lectura de lenguajes");}

        for(;iteraciones > 0;--iteraciones){
            String nombre = flujo_entrada.next();

            DynamicArray<Tomo> tomos = new DynamicArray<Tomo>();

            Lenguaje lenguaje = new Lenguaje(nombre,tomos); ///LENGUAJE

            int n_tomos = flujo_entrada.nextInt();

            for(int i = 0; i < n_tomos; ++i){
                String nombre_t = flujo_entrada.next();

                int agno_publicacion_t = flujo_entrada.nextInt();

                Tomo tomo = null;

                for(int j = 0; j < this.getTomos().getSize(); ++j){
                    Tomo temp = this.getTomos().get(j);
                    if(temp.getNombre().equalsIgnoreCase(nombre_t) && temp.getAgno_publicacion() == agno_publicacion_t){
                        tomo = temp;
                        break;
                    }
                }

                tomos.pushBack(tomo);
                tomo.setLenguaje(lenguaje);
                Comic comic = tomo.getCabeza();
                for(int k = 0; k < tomo.getTamano(); ++k){
                    comic.setLenguaje(lenguaje);
                    comic = comic.getSecuela();
                }
                array.pushBack(lenguaje);
                lenguaje.setTomos(tomos);
            }

        }
        this.setLenguajes(lenguajes);
        System.out.println("Lectura completada");
    }




        protected Autor buscarAutor(String nombre, int edad)
        {
            Autor res = new Autor(nombre,edad);
            
            for(int i = 0; i < this.getAutores().getSize(); ++i)
            {
                if(res.equals(this.getAutores().get(i)))
                {
                    return this.getAutores().get(i);
                }
            }
            return null;
        }
        
        private Tomo buscarTomo(String tomo){
        	for(int i = 0; i < this.getTomos().getSize(); ++i)
        	{
        		if(tomo.equalsIgnoreCase(this.getTomos().get(i).getNombre())||tomo.equalsIgnoreCase(this.getTomos().get(i).getNombre().replaceAll("_"," "))||tomo.equalsIgnoreCase(this.getTomos().get(i).getNombre().replaceAll(" ","_"))) return this.getTomos().get(i);
        	}
        	return null;
        }
        
        //Metodos para modificar Tomos
        public void tomoAgregarAtras(Comic comic, Tomo tomo){  //Agrega comic al final de tomo.
            tomo.agregarAtras(comic);
            this.escribirTomos();
        }     
        public void tomoAgregarFrente(Comic comic, Tomo tomo){ //Agrega comic al inicio de tomo.
            tomo.agregarFrente(comic);
            this.escribirTomos();
        }       
        public Comic tomoQuitarFrente(Tomo tomo){
            if(tomo.getTamano()==0){
	            throw new EmptyStackException();
	        }
            Comic comic = tomo.quitarFrente();
            this.escribirTomos();
            return comic;
        }             
        public void tomoAgregarAntes(Comic comic_agregar, Comic comic_antes, Tomo tomo){
            tomo.agregarAntes(comic_agregar, comic_antes);
            this.escribirTomos();
        }      
        public void tomoAgregarDespues(Comic comicSiendoAgregado, Comic comic, Tomo tomo){
            tomo.agregarDespues(comicSiendoAgregado, comic);
            this.escribirTomos();
        }
        public void escribirTomos() {
            File file_actualizada = MainActivity.tempT;
            File file = MainActivity.localFileT;
            DynamicArray <Tomo> tomos = this.getTomos();
            PrintStream flujo_salida = null;
            try {
                flujo_salida = new PrintStream(file_actualizada);
            } catch (FileNotFoundException ex) {
                System.out.println("Error en la actualizacion de tomos");
                Log.d("Durazno","a, "+ex.getMessage());
            }
            
            flujo_salida.println("Tomos " + tomos.getSize());
            
            for(int i = 0; i < tomos.getSize(); ++i){
                Tomo tomo = tomos.get(i);
                flujo_salida.println(tomo);
                if(tomo.hayDescripcion()) flujo_salida.println(tomo.getDescripcion());
                flujo_salida.print(tomo.getCategorias().getSize() + " ");
                for(int j = 0; j < tomo.getCategorias().getSize(); ++j){
                    flujo_salida.print(tomo.getCategorias().get(j) + " ");
                }
                flujo_salida.println();
                flujo_salida.println(tomo.getTamano());
                Comic comic = tomo.getCabeza();
                for(int j = 0; j < tomo.getTamano(); ++j){
                    flujo_salida.println(comic);
                    if(comic.hayDescripcion()) flujo_salida.println(comic.getDescripcion());
                    comic = comic.getSecuela();
                }
            }
            System.gc();
            flujo_salida.flush();
            System.gc();
            flujo_salida.close();
            System.gc();
            file.delete();
            file_actualizada.renameTo(file);

            Uri fileTomo = Uri.fromFile(file);
            StorageReference riversRef = MainActivity.mStorageRef.getReferenceFromUrl("gs://rproyectocomic.appspot.com/").child("tomo.txt");
            UploadTask uploadTask = riversRef.putFile(fileTomo);
            uploadTask = riversRef.putFile(fileTomo);
        }
        
        private void escribirAutores()
        {
        	File file_actualizada = new File("../data/temp.txt");
            File file = new File("../data/tomo.txt");
            DynamicArray <Autor> tomos = this.getAutores(); // // // // 
           /* PrintStream flujo_salida = null;
            try {
                flujo_salida = new PrintStream(file_actualizada);
            } catch (FileNotFoundException ex) {
                System.out.println("Error en la actualizacion de tomos");
            }
            
            flujo_salida.println("Tomos " + tomos.getSize());
            
            for(int i = 0; i < tomos.getSize(); ++i){
                Tomo tomo = tomos.get(i);
                flujo_salida.println(tomo);               
                flujo_salida.print(tomo.getCategorias().getSize() + " ");
                for(int j = 0; j < tomo.getCategorias().getSize(); ++j){
                    flujo_salida.print(tomo.getCategorias().get(j) + " ");
                }
                flujo_salida.println();
                flujo_salida.println(tomo.getTamano());
                Comic comic = tomo.getCabeza();
                for(int j = 0; j < tomo.getTamano(); ++j){
                    flujo_salida.println(comic);
                    comic = comic.getSecuela();
                }
            }
            System.gc();
            flujo_salida.flush();
            System.gc();
            flujo_salida.close();
            System.gc();
            System.out.println(file.delete());
            file_actualizada.renameTo(file);*/
        }
        
        //Getters y setters
        
        public DynamicArray<Editorial> getEditoriales() {
            return editoriales;
        }
        public void setEditoriales(DynamicArray<Editorial> editoriales) {
            this.editoriales = editoriales;
        }   
        public DynamicArray<Tomo> getTomos() {
            return tomos;
        }      
        public void setTomos(DynamicArray<Tomo> tomos) {
            this.tomos = tomos;
        }  
        public DynamicArray<Autor> getAutores() {
            return autores;
        }       
        public void setAutores(DynamicArray<Autor> autores) {
            this.autores = autores;
        } 
        public DynamicArray<Categoria> getCategorias() {
            return categorias;
        }      
        public void setCategorias(DynamicArray<Categoria> categorias) {
            this.categorias = categorias;
        }  
        public DynamicArray<Usuario> getUsuarios() {
            return usuarios;
        }    
        public void setUsuarios(DynamicArray<Usuario> usuarios) {
            this.usuarios = usuarios;
        }
        public DynamicArray<Lenguaje> getLenguajes() {
            return lenguajes;
        }
        public void setLenguajes(DynamicArray<Lenguaje> lenguajes) {
            this.lenguajes = lenguajes;
        }
        
        
}
