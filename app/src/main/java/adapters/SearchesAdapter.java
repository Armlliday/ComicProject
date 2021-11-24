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

import java.util.ArrayList;

public class SearchesAdapter extends RecyclerView.Adapter<SearchesAdapter.ViewHolderSearches>{

    ArrayList<String> searches;
    private OnSearchListener mOnSearchListener;


    public SearchesAdapter(ArrayList<String> searches, OnSearchListener onSearchListener) {
        this.searches = searches;
        this.mOnSearchListener = onSearchListener;
    }
    @NonNull
    @Override
    public ViewHolderSearches onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent_searches, null,false);
        return new ViewHolderSearches(view, mOnSearchListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderSearches holder, int position) {
        holder.searchText.setText(searches.get(position));
    }

    @Override
    public int getItemCount() {
        return searches.size();
    }

    public class ViewHolderSearches extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView searchText;
        OnSearchListener onSearchListener;

        public ViewHolderSearches(@NonNull View itemView, OnSearchListener onSearchListener) {
            super(itemView);
            searchText = itemView.findViewById(R.id.idSearch);
            this.onSearchListener = onSearchListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onSearchListener.onSearchClick(getAdapterPosition());
        }
    }

    public interface OnSearchListener{
        void onSearchClick(int position);
    }


}
