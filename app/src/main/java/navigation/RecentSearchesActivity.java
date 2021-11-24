package navigation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectocomic.MainActivity;
import com.example.proyectocomic.R;
import com.example.proyectocomic.structures.Stack;

import java.util.ArrayList;

import adapters.SearchesAdapter;

public class RecentSearchesActivity extends AppCompatActivity implements SearchesAdapter.OnSearchListener {
    RecyclerView recyclerSearches;
    ArrayList<String> tempSearches;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recent_searches_activity);

        ImageButton back = (ImageButton) findViewById(R.id.backSearch);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        tempSearches = new ArrayList<String>();
        Stack<String> tempStack = new Stack<String>();
        try {
            tempStack = (Stack<String>) MainActivity.recentSearches.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        while(!tempStack.empty()){
            tempSearches.add(tempStack.peek());
            //Log.d("Mensaje",tempStack.peek());
            tempStack.pop();
        }

        recyclerSearches = (RecyclerView) findViewById(R.id.RecyclerRecentSearches);
        recyclerSearches.setLayoutManager(new LinearLayoutManager(this));
        SearchesAdapter adapter = new SearchesAdapter(tempSearches,this);
        recyclerSearches.setAdapter(adapter);
    }

    @Override
    public void onSearchClick(int position) {
        Intent intent = new Intent(this,BottonNavigation.class);
        intent.putExtra("search",(String) tempSearches.get(position) );
        startActivity(intent);
    }
}
