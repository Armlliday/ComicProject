package adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectocomic.R;
import com.example.proyectocomic.comics.Comic;
import com.example.proyectocomic.structures.DynamicArray;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolderComic> {

    DynamicArray<Comic> ListaComic;
    private OnComicListener mOnComicListener;

    public Adapter(DynamicArray<Comic> listaComic, OnComicListener onComicListener) {
        this.ListaComic = listaComic;
        this.mOnComicListener = onComicListener;
    }

    @NonNull
    @Override
    public ViewHolderComic onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_comics, null,false);
        return new ViewHolderComic(view, mOnComicListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderComic holder, int position) {
        holder.etiNombre.setText(ListaComic.get(position).getNombre().replaceAll("_"," "));
        holder.etiAutor.setText(ListaComic.get(position).getEscritor().getNombre().replaceAll("_"," "));
        holder.etiDibujante.setText(ListaComic.get(position).getDibujante().getNombre().replaceAll("_"," "));
        holder.foto.setImageResource(ListaComic.get(position).getFoto());

    }

    @Override
    public int getItemCount() {
        return ListaComic.getSize();
    }

    public class ViewHolderComic extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView etiNombre, etiDibujante, etiAutor;
        ImageView foto;
        OnComicListener onComicListener;

        public ViewHolderComic(@NonNull View itemView, OnComicListener onComicListener) {
            super(itemView);
            etiNombre = itemView.findViewById(R.id.idNombre);
            etiAutor = itemView.findViewById(R.id.idAutor);
            etiDibujante = itemView.findViewById(R.id.idInfo);
            foto = itemView.findViewById(R.id.idImagen);
            this.onComicListener = onComicListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onComicListener.onComicClick(getAdapterPosition());
        }
    }

    public interface OnComicListener{
        void onComicClick(int position);
    }

    public void updateList (DynamicArray<Comic> items) {
        if (items != null && items.getSize() > 0) {
            ListaComic.clear();
            ListaComic.addAll(items);
            notifyDataSetChanged();
        }
    }


}
