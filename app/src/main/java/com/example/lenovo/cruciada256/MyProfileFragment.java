package com.example.lenovo.cruciada256;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class MyProfileFragment extends Fragment {

    private RecyclerView mBookList,mReadingList,mFollowersList,mFollowingList;

    private TextView finishedBooksText,readingBooksText,followersListText,followingListText,username, reads, followers, following;
    private ImageView profilePic;

    private DatabaseReference mDatabase,mDatabaseUsers,mUsers;
    private FirebaseAuth mAuth;

    private String userID;

    GlobalClass globalClass;

    private FloatingActionButton floatingActionButton;

    private Query mQueryFinishedRead,mQueryReading;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);

        mBookList = (RecyclerView) view.findViewById(R.id.profileBooks);
        mReadingList = (RecyclerView) view.findViewById(R.id.profileReading);
        mFollowersList = (RecyclerView) view.findViewById(R.id.Followers);
        mFollowingList = (RecyclerView) view.findViewById(R.id.Following);

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fabMyProfile);
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("books");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        mUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mQueryFinishedRead = mDatabase.orderByChild(userID).equalTo("read");
        mQueryReading = mDatabase.orderByChild(userID).equalTo("reading");

        profilePic = (ImageView) view.findViewById(R.id.imageView);
        username = (TextView) view.findViewById(R.id.profile_name);
        reads = (TextView) view.findViewById(R.id.profile_read_books);
        followers = (TextView) view.findViewById(R.id.profile_friends_number);
        following = (TextView) view.findViewById(R.id.profile_friends_number2);
        followersListText = (TextView) view.findViewById(R.id.FollowersText);
        followingListText = (TextView) view.findViewById(R.id.FollowingText);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager layoutManager4 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        mBookList.setLayoutManager(layoutManager1);
        mReadingList.setLayoutManager(layoutManager2);
        mFollowersList.setLayoutManager(layoutManager3);
        mFollowingList.setLayoutManager(layoutManager4);

        finishedBooksText = (TextView)view.findViewById(R.id.finishedBooksText);
        readingBooksText = (TextView)view.findViewById(R.id.readingBooksText);

        globalClass = (GlobalClass) getContext().getApplicationContext();

        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Picasso.with(getContext()).load(dataSnapshot. child("image").getValue().toString()).into(profilePic);
                username.setText(dataSnapshot.child("Name").getValue().toString());
                //reads.setText(dataSnapshot.child("reads").getValue().toString()+" cărți citite");
                followers.setText(dataSnapshot.child("followers").getValue().toString()+ " urmăritori");
                following.setText((dataSnapshot.child("following").getValue().toString())+" persoane urmărite");

                if(dataSnapshot.child("followers").getValue().toString().compareTo("0")==0)
                    followersListText.setText("Urmăritorii tăi vor apărea aici");

                if(dataSnapshot.child("following").getValue().toString().compareTo("0")==0)
                    followingListText.setText("Persoanele pe care le urmărești vor apărea aici");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mQueryFinishedRead.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()==0) {

                    finishedBooksText.setText("Cărțile pe care le-ai citit vor apărea aici");

                }

                reads.setText(dataSnapshot.getChildrenCount()+" cărți citite");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mQueryReading.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()==0) {

                    readingBooksText.setText("Cărțile pe care le-ai început vor apărea aici");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment newFragment = new CautaLumeFragment();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.fragment_container,newFragment);
                transaction.addToBackStack(null);

                transaction.commit();

            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Carte,CardCarteViewHolder> FBRA = new FirebaseRecyclerAdapter<Carte, CardCarteViewHolder>(
                Carte.class,
                R.layout.card_book,
                CardCarteViewHolder.class,
                mQueryFinishedRead

        ) {
            @Override
            protected void populateViewHolder(CardCarteViewHolder viewHolder, Carte model, int position) {

                final String post_key = getRef(position).getKey();

                viewHolder.setNume(model.getNume());
                viewHolder.setAutor(model.getAutor());
                viewHolder.setImage(getContext(), model.getImage());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        globalClass.setCurrentBookID(post_key);

                        Fragment newFragment = new CarteFragment();

                        FragmentTransaction transaction = getFragmentManager().beginTransaction();

                        transaction.replace(R.id.fragment_container,newFragment);
                        transaction.addToBackStack(null);

                        transaction.commit();

                    }
                });

            }
        };

        mBookList.setAdapter(FBRA);

        FirebaseRecyclerAdapter<Carte,CardCarteViewHolder> FBRA2 = new FirebaseRecyclerAdapter<Carte, CardCarteViewHolder>(
                Carte.class,
                R.layout.card_book,
                CardCarteViewHolder.class,
                mQueryReading
        ) {
            @Override
            protected void populateViewHolder(CardCarteViewHolder viewHolder, Carte model, int position) {

                final String post_key = getRef(position).getKey();

                viewHolder.setNume(model.getNume());
                viewHolder.setAutor(model.getAutor());
                viewHolder.setImage(getContext(), model.getImage());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        globalClass.setCurrentBookID(post_key);

                        Fragment newFragment = new CarteFragment();

                        FragmentTransaction transaction = getFragmentManager().beginTransaction();

                        transaction.replace(R.id.fragment_container,newFragment);
                        transaction.addToBackStack(null);

                        transaction.commit();

                    }
                });

            }
        };

        mReadingList.setAdapter(FBRA2);

        FirebaseRecyclerAdapter<ProfilPagProfil,CardCarteViewHolder> FBRA3 = new FirebaseRecyclerAdapter<ProfilPagProfil, CardCarteViewHolder>(
                ProfilPagProfil.class,
                R.layout.card_book,
                CardCarteViewHolder.class,
                mUsers
        ) {
            @Override
            protected void populateViewHolder(final CardCarteViewHolder viewHolder, ProfilPagProfil model, int position) {

                final String post_key = getRef(position).getKey();

                final ProfilPagProfil model2=model;

                mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.child("Followers").hasChild(post_key))
                        {

                            viewHolder.setReads(model2.getReads());
                            viewHolder.setImage(getContext(),model2.getImage());
                            viewHolder.setNume(model2.getName());

                        }
                        else{
                            viewHolder.yeetView();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

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

        mFollowersList.setAdapter(FBRA3);

        FirebaseRecyclerAdapter<ProfilPagProfil,CardCarteViewHolder> FBRA4 = new FirebaseRecyclerAdapter<ProfilPagProfil, CardCarteViewHolder>(
                ProfilPagProfil.class,
                R.layout.card_book,
                CardCarteViewHolder.class,
                mUsers
        ) {
            @Override
            protected void populateViewHolder(final CardCarteViewHolder viewHolder, ProfilPagProfil model, int position) {

                final String post_key = getRef(position).getKey();

                final ProfilPagProfil model2=model;

                mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.child("Following").hasChild(post_key))
                        {

                            viewHolder.setReads(model2.getReads());
                            viewHolder.setImage(getContext(),model2.getImage());
                            viewHolder.setNume(model2.getName());

                        }
                        else{
                            viewHolder.yeetView();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

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

        mFollowingList.setAdapter(FBRA4);

    }

    public static class CardCarteViewHolder extends RecyclerView.ViewHolder
    {

        View mView;

        CardView CARD;

        public CardCarteViewHolder(@NonNull View itemView)
        {
            super(itemView);

            mView = itemView;
            CARD = (CardView) mView.findViewById(R.id.cardBookFull);

        }

        public void yeetView()
        {

            CARD.setVisibility(View.GONE);
            CARD.requestLayout();
            CARD.getLayoutParams().height=0;
            CARD.getLayoutParams().width=0;

            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) CARD.getLayoutParams();
            layoutParams.setMargins(0,0,0,0);
            CARD.requestLayout();

            //CARD.setPadding(0,0,0,0);

            //CARD.setLayoutParams(new CardView.LayoutParams(0,0));

            //ImageView imageView = mView.findViewById(R.id.cardBookImage);

            //imageView.requestLayout();
            //imageView.getLayoutParams().height = 0;
            //imageView.getLayoutParams().width = 0;

        }

        public void setNume(String nume)
        {
            TextView carte_nume = (TextView)mView.findViewById(R.id.cardBookNume);
            carte_nume.setText(nume);
        }

        public void setAutor(String autor)
        {
            TextView carte_autor = (TextView)mView.findViewById(R.id.cardBookAutor);
            carte_autor.setText(autor);
        }

        public void setReads(String reads)
        {
            TextView carte_reads = (TextView)mView.findViewById(R.id.cardBookAutor);
            carte_reads.setText(reads+" cărți");
        }

        public void setImage(Context ctx, String image)
        {
            ImageView carte_img = (ImageView) mView.findViewById(R.id.cardBookImage );
            Picasso.with(ctx).load(image).into(carte_img);
        }

    }

}
