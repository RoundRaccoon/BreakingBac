package com.example.lenovo.cruciada256;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class CautaLumeFragment extends Fragment {

    private RecyclerView recyclerView;
    GlobalClass globalClass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cauta_lume,container,false);

        recyclerView = (RecyclerView) view.findViewById(R.id.listaProfile);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        globalClass = (GlobalClass) getContext().getApplicationContext();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Profile,profileViewHolder> FBRA = new FirebaseRecyclerAdapter<Profile, profileViewHolder>(
                Profile.class,
                R.layout.profile_row,
                profileViewHolder.class,
                FirebaseDatabase.getInstance().getReference().child("Users")
        ) {
            @Override
            protected void populateViewHolder(profileViewHolder viewHolder, Profile model, int position) {

                final String post_key = getRef(position).getKey();

                if(post_key.compareTo(FirebaseAuth.getInstance().getCurrentUser().getUid())==0)
                    viewHolder.yeetView();
                else
                {
                    viewHolder.setImage(getContext(),model.getImage());
                    viewHolder.setNume(model.getName());
                    viewHolder.setNrCarti(model.getReads());
                    viewHolder.setNrUrmaritori(model.getFollowers());
                    viewHolder.setNrUrmaritori(model.getFollowers());

                }

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        globalClass.setCurrentProfileVisit(post_key);

                        Fragment newFragment = new VisitProfileFragment();

                        FragmentTransaction transaction = getFragmentManager().beginTransaction();

                        transaction.replace(R.id.fragment_container,newFragment);
                        transaction.addToBackStack(null);

                        transaction.commit();

                    }
                });

            }
        };

        recyclerView.setAdapter(FBRA);

    }

    public static class profileViewHolder extends RecyclerView.ViewHolder
    {

        View mView;

        CardView cardView;

        public profileViewHolder(@NonNull View itemView)
        {
            super(itemView);

            mView = itemView;

            cardView = (CardView) mView.findViewById(R.id.cardProfilListFull);

        }

        public void yeetView()
        {
            cardView.setVisibility(View.GONE);
            cardView.setLayoutParams(new CardView.LayoutParams(0,0));
        }

        public void setImage(Context ctx,String image)
        {
            ImageView imageView = (ImageView)mView.findViewById(R.id.poza_profil);
            Picasso.with(ctx).load(image).into(imageView);
        }

        public void setNume(String nume)
        {
            TextView textView = (TextView)mView.findViewById(R.id.nume_profil);
            textView.setText(nume);
        }

        public void setNrCarti(String nume)
        {
            TextView textView = (TextView)mView.findViewById(R.id.nr_cart_profil);
            textView.setText(nume+" cărți citite");
        }

        public void setNrUrmaritori(String nume)
        {
            TextView textView = (TextView)mView.findViewById(R.id.nr_urm_profil);
            textView.setText(nume+" urmăritori");
        }

    }

}
