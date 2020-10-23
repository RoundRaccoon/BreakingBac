package com.example.lenovo.cruciada256;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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

public class ReviewListActivity extends AppCompatActivity {

    private RecyclerView listaRecenzii;
    private DatabaseReference mDatabaseRecenzii;

    private Query mQuery;

    private FirebaseAuth mAuth;
    String userID;

    GlobalClass globalClass;

    public boolean mProcessUpVoting,mProcessDownVoting,mProcessSwitchUp,mProcessSwitchDown;

    private Query mQueryData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);

        globalClass = (GlobalClass) getApplicationContext();

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        mDatabaseRecenzii = FirebaseDatabase.getInstance().getReference().child("Reviews").child(globalClass.getCurrentBookID());
        mQuery = mDatabaseRecenzii.orderByChild("Puncte");

        listaRecenzii = (RecyclerView) findViewById(R.id.listaRecenzii);
        listaRecenzii.setHasFixedSize(true);
        listaRecenzii.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Recenzie,RecenziiViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Recenzie, RecenziiViewHolder>(
                Recenzie.class,
                R.layout.recenzie_row,
                RecenziiViewHolder.class,
                mQuery
        ) {
            @Override
            protected void populateViewHolder(RecenziiViewHolder viewHolder, Recenzie model, int position) {

                final String post_key = getRef(position).getKey();

                if(post_key.compareTo(userID)==0)
                    viewHolder.yeetView();

                viewHolder.setImage(getApplicationContext(),model.getImage());
                viewHolder.setNume(model.getNume());
                viewHolder.setDate(model.getDate());
                viewHolder.setStars(getApplicationContext(),model.getNota());
                viewHolder.setParere(model.getParere());
                viewHolder.setScor(model.getPuncte());
                viewHolder.setVotes(post_key,globalClass.getCurrentBookID());

                final int puncte = StringToInt(model.getPuncte());
                final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Reviews").child(globalClass.getCurrentBookID()).child(post_key);


                viewHolder.downvote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mProcessUpVoting = true;

                            mDatabase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if(mProcessUpVoting) {

                                        if (dataSnapshot.child("Voturi").hasChild(userID)) {
                                            if (dataSnapshot.child("Voturi").child(userID).getValue().toString() == "nu") {
                                                mProcessUpVoting = false;
                                                mDatabaseRecenzii.child(post_key).child("Puncte").setValue(IntToString(puncte + 2));
                                                mDatabaseRecenzii.child(post_key).child("Voturi").child(userID).setValue("da");
                                            } else {
                                                mProcessUpVoting = false;
                                                mDatabaseRecenzii.child(post_key).child("Puncte").setValue(IntToString(puncte - 1));
                                                mDatabaseRecenzii.child(post_key).child("Voturi").child(userID).removeValue();
                                            }
                                        } else {
                                            mProcessUpVoting = false;
                                            mDatabaseRecenzii.child(post_key).child("Puncte").setValue(IntToString(puncte + 1));
                                            mDatabaseRecenzii.child(post_key).child("Voturi").child(userID).setValue("da");
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });



                    }
                });

                viewHolder.upvote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mProcessDownVoting = true;

                        mDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(mProcessDownVoting) {

                                    if (dataSnapshot.child("Voturi").hasChild(userID)) {
                                        if (dataSnapshot.child("Voturi").child(userID).getValue().toString() == "nu") {
                                            mProcessDownVoting = false;
                                            mDatabaseRecenzii.child(post_key).child("Puncte").setValue(IntToString(puncte + 1));
                                            mDatabaseRecenzii.child(post_key).child("Voturi").child(userID).removeValue();
                                        } else {
                                            mProcessDownVoting = false;
                                            mDatabaseRecenzii.child(post_key).child("Puncte").setValue(IntToString(puncte - 2));
                                            mDatabaseRecenzii.child(post_key).child("Voturi").child(userID).setValue("nu");
                                        }
                                    } else {
                                        mProcessDownVoting = false;
                                        mDatabaseRecenzii.child(post_key).child("Puncte").setValue(IntToString(puncte - 1));
                                        mDatabaseRecenzii.child(post_key).child("Voturi").child(userID).setValue("nu");
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });



                    }
                });

            }
        };

        listaRecenzii.setAdapter(firebaseRecyclerAdapter);

    }

    public static class RecenziiViewHolder extends RecyclerView.ViewHolder
    {

        View mView;

        private ImageView stea1,stea2,stea3,stea4,stea5,upvote,downvote;
        private CardView CARD;

        private DatabaseReference mDatabase;

        boolean mProcess;

        public RecenziiViewHolder( View itemView)
        {

            super(itemView);
            mView = itemView;

            CARD = (CardView) mView.findViewById(R.id.cardpecareilsterg);
            stea1 = (ImageView) mView.findViewById(R.id.actualRecenzieStea1);
            stea2 = (ImageView) mView.findViewById(R.id.actualRecenzieStea2);
            stea3 = (ImageView) mView.findViewById(R.id.actualRecenzieStea3);
            stea4 = (ImageView) mView.findViewById(R.id.actualRecenzieStea4);
            stea5 = (ImageView) mView.findViewById(R.id.actualRecenzieStea5);

            upvote = (ImageView) mView.findViewById(R.id.recenzieupvote);
            downvote =(ImageView)mView.findViewById(R.id.recenziedownvote);

            mDatabase = FirebaseDatabase.getInstance().getReference().child("Reviews");

            mDatabase.keepSynced(true);

        }

        public void yeetView()
        {
            CARD.setVisibility(View.GONE);
            CARD.setLayoutParams(new CardView.LayoutParams(0,0));
        }

        public void setVotes(final String post_key,final String bookID)
        {
            mProcess = true;
            mDatabase.child(bookID).child(post_key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(mProcess) {

                        mProcess = false;

                        if (dataSnapshot.child("Voturi").hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            if (dataSnapshot.child("Voturi").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue().toString().compareTo("da") == 0) {
                                upvote.setImageResource(R.drawable.upnegru);
                                downvote.setImageResource(R.drawable.downverde);
                            } else {
                                upvote.setImageResource(R.drawable.upverde);
                                downvote.setImageResource(R.drawable.downnegru);

                            }
                        } else {
                            upvote.setImageResource(R.drawable.upnegru);
                            downvote.setImageResource(R.drawable.downnegru);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        public void setStars(Context ctx, String nota)
        {

            if(nota.compareTo("1")==0)
            {
                Picasso.with(ctx).load(R.drawable.steaplina).into(stea1);
                Picasso.with(ctx).load(R.drawable.steagoala).into(stea2);
                Picasso.with(ctx).load(R.drawable.steagoala).into(stea3);
                Picasso.with(ctx).load(R.drawable.steagoala).into(stea4);
                Picasso.with(ctx).load(R.drawable.steagoala).into(stea5);
            }
            else if(nota.compareTo("2")==0)
            {
                Picasso.with(ctx).load(R.drawable.steaplina).into(stea1);
                Picasso.with(ctx).load(R.drawable.steaplina).into(stea2);
                Picasso.with(ctx).load(R.drawable.steagoala).into(stea3);
                Picasso.with(ctx).load(R.drawable.steagoala).into(stea4);
                Picasso.with(ctx).load(R.drawable.steagoala).into(stea5);
            }
            else if(nota.compareTo("3")==0)
            {
                Picasso.with(ctx).load(R.drawable.steaplina).into(stea1);
                Picasso.with(ctx).load(R.drawable.steaplina).into(stea2);
                Picasso.with(ctx).load(R.drawable.steaplina).into(stea3);
                Picasso.with(ctx).load(R.drawable.steagoala).into(stea4);
                Picasso.with(ctx).load(R.drawable.steagoala).into(stea5);
            }
            else if(nota.compareTo("4")==0)
            {
                Picasso.with(ctx).load(R.drawable.steaplina).into(stea1);
                Picasso.with(ctx).load(R.drawable.steaplina).into(stea2);
                Picasso.with(ctx).load(R.drawable.steaplina).into(stea3);
                Picasso.with(ctx).load(R.drawable.steaplina).into(stea4);
                Picasso.with(ctx).load(R.drawable.steagoala).into(stea5);
            }
            else if(nota.compareTo("5")==0)
            {
                Picasso.with(ctx).load(R.drawable.steaplina).into(stea1);
                Picasso.with(ctx).load(R.drawable.steaplina).into(stea2);
                Picasso.with(ctx).load(R.drawable.steaplina).into(stea3);
                Picasso.with(ctx).load(R.drawable.steaplina).into(stea4);
                Picasso.with(ctx).load(R.drawable.steaplina).into(stea5);
            }


        }

        public void setScor(String scor)
        {
            TextView reviewScor = (TextView) mView.findViewById(R.id.actualRecenzieScor);
            reviewScor.setText("Scor recenzie: "+ (500-StringToInt(scor)));
            /**if(scor.charAt(0)=='-')reviewScor.setText("Scor recenzie: "+scor.substring(1));
            else if(scor.charAt(0)=='0')reviewScor.setText("Scor recenzie: "+scor);
            else reviewScor.setText("Scor recenzie: -"+scor);*/
        }

        public void setParere(String parere)
        {
            TextView reviewParere = (TextView)mView.findViewById(R.id.actualRecenzieLunga);
            reviewParere.setText(parere);
        }

        public void setNume(String nume)
        {
            TextView reviewName = (TextView)mView.findViewById(R.id.actualRecenzieUserName);
            reviewName.setText(nume);
        }

        public void setImage(Context ctx, String image)
        {
            ImageView reviewimg = (ImageView) mView.findViewById(R.id.actualRecenzieProfilePic);
            Picasso.with(ctx).load(image).into(reviewimg);
        }

        public void setDate(String date)
        {
            TextView reviewDate = (TextView) mView.findViewById(R.id.actualRecenzieData);
            reviewDate.setText(date);
        }

        public int StringToInt(String s)
        {

            if(s.charAt(0)=='-')
            {

                int x = 0, p = 1;

                for (int i = s.length() - 1; i > 0; --i) {
                    x = x + p * (s.charAt(i) - '0');
                    p *= 10;
                }

                x = x * (-1);
                return x;
            }
            else {
                int x = 0, p = 1;

                for (int i = s.length() - 1; i >= 0; --i) {
                    x = x + p * (s.charAt(i) - '0');
                    p *= 10;
                }
                return x;
            }


        }

        public String IntToString(int n) {

            String s = "";

            if (n >= 0) {
                int k = -1;

                int[] v = new int[5];

                do {
                    k++;
                    v[k] = n % 10;
                    n /= 10;
                } while (n != 0);

                for (int i = k; i >= 0; --i)
                    s = s + v[i];

                return s;

            }

            else
            {
                s = "-";
                n = n * (-1);

                int k = -1;

                int[] v = new int[5];

                do {
                    k++;
                    v[k] = n % 10;
                    n /= 10;
                } while (n != 0);
                for (int i = k; i >= 0; --i)
                    s = s + v[i];

                return s;
            }

        }

    }

    public int StringToInt(String s)
    {

        if(s.charAt(0)=='-')
        {

            int x = 0, p = 1;

            for (int i = s.length() - 1; i > 0; --i) {
                x = x + p * (s.charAt(i) - '0');
                p *= 10;
            }

            x = x * (-1);
            return x;
        }
        else {
            int x = 0, p = 1;

            for (int i = s.length() - 1; i >= 0; --i) {
                x = x + p * (s.charAt(i) - '0');
                p *= 10;
            }
            return x;
        }


    }

    public String IntToString(int n) {

        String s = "";

        if (n >= 0) {
            int k = -1;

            int[] v = new int[5];

            do {
                k++;
                v[k] = n % 10;
                n /= 10;
            } while (n != 0);

            for (int i = k; i >= 0; --i)
                s = s + v[i];

            return s;

        }

        else
        {
            s = "-";
            n = n * (-1);

            int k = -1;

            int[] v = new int[5];

            do {
                k++;
                v[k] = n % 10;
                n /= 10;
            } while (n != 0);
            for (int i = k; i >= 0; --i)
                s = s + v[i];

            return s;
        }

    }

}
