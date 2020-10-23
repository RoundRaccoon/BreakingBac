package com.example.lenovo.cruciada256;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class SearchActivity extends AppCompatActivity {

    DatabaseReference mDatabase;
    RecyclerView mSearchList;
    EditText   searchText;
    Button searchButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mSearchList = (RecyclerView) findViewById(R.id.searchList);
        mSearchList.setHasFixedSize(true);
        mSearchList.setLayoutManager(new LinearLayoutManager(this));

        searchText = (EditText) findViewById(R.id.searchview);
        searchButton = (Button) findViewById(R.id.search_button);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("books");

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = searchText.getText().toString();

                FirebaseSearch(text);

            }
        });

    }

    private void FirebaseSearch(String text)
    {

        Query mSearchQuery = mDatabase.orderByChild("nume").startAt(text).endAt(text + "\uf8ff");



        FirebaseRecyclerAdapter<Carte,CarteViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Carte, CarteViewHolder>(
                Carte.class,
                R.layout.carte_row,
                CarteViewHolder.class,
                mSearchQuery
        ) {
            @Override
            protected void populateViewHolder(CarteViewHolder viewHolder, Carte model, int position) {

                Toast.makeText(SearchActivity.this,position,Toast.LENGTH_LONG).show();
                viewHolder.setNume(model.getNume());
                viewHolder.setAutor(model.getAutor());
                viewHolder.setImage(getApplicationContext(), model.getImage());

            }
        };

        mSearchList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class CarteViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public CarteViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setNume(String nume)
        {
            TextView carte_nume = (TextView)mView.findViewById(R.id.numecarteview);
            carte_nume.setText(nume);
        }

        public void setAutor(String autor)
        {
            TextView carte_autor = (TextView)mView.findViewById(R.id.autorcarteview);
            carte_autor.setText(autor);
        }

        public void setImage(Context ctx, String image)
        {
            ImageView carte_img = (ImageView) mView.findViewById(R.id.copertacarteview);
            Picasso.with(ctx).load(image).into(carte_img);
        }

    }

}
