package com.example.lenovo.cruciada256;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;


public class VisitProfileFragment extends Fragment {

    private ImageView visitProfilePic;
    private TextView visitName,visitReads,visitFollowers,visitFollowing;

    private TextView visitFinishedBooksText,visitReadingText;
    private RecyclerView visitFinishedBooks, visitReading;

    GlobalClass globalClass ;

    private DatabaseReference mDatabaseUser,mDatabaseMyUser;
    private Query mQueryFinishedBooks,mQueryReading;

    private String userID,visitUserID;

    private TextView visitFollow;

    private boolean mProcessFollow;

    private String FollowerCount,FollowingCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_visit_profile, container, false);

        visitProfilePic = (ImageView) view.findViewById(R.id.visitProfilePic);

        visitName = (TextView) view.findViewById(R.id.visitProfileName);
        visitReads = (TextView) view.findViewById(R.id.visitProfileReads);
        visitFollowers = (TextView) view.findViewById(R.id.visitProfileFollowers);
        visitFollowing = (TextView) view.findViewById(R.id.visitProfileFollowing);

        visitFollow = (TextView) view.findViewById(R.id.visit_follow);

        visitFinishedBooksText = (TextView) view.findViewById(R.id.visitProfileFinishedBooksText);
        visitFinishedBooks = (RecyclerView) view.findViewById(R.id.visitProfileFinishedBooks);

        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        visitFinishedBooks.setLayoutManager(linearLayout);

        visitReadingText = (TextView) view.findViewById(R.id.visitProfileReadingText);
        visitReading = (RecyclerView) view.findViewById(R.id.visitProfileReading);

        LinearLayoutManager linearLayout2 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        visitReading.setLayoutManager(linearLayout2);

        globalClass = (GlobalClass) getContext().getApplicationContext();

        visitUserID = globalClass.getCurrentProfileVisit();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(visitUserID);
        mDatabaseMyUser = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        mQueryFinishedBooks = FirebaseDatabase.getInstance().getReference().child("books").orderByChild(visitUserID).equalTo("read");
        mQueryReading = FirebaseDatabase.getInstance().getReference().child("books").orderByChild(visitUserID).equalTo("reading");

        mDatabaseMyUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                FollowingCount = dataSnapshot.child("following").getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    FollowerCount = dataSnapshot.child("followers").getValue().toString();

                    Picasso.with(getContext()).load(dataSnapshot.child("image").getValue().toString()).into(visitProfilePic);
                    visitName.setText(dataSnapshot.child("Name").getValue().toString());
                    visitReads.setText(dataSnapshot.child("reads").getValue().toString()+" cărți citite");
                    visitFollowers.setText(dataSnapshot.child("followers").getValue().toString()+ " urmăritori");
                    visitFollowing.setText((dataSnapshot.child("following").getValue().toString())+" persoane urmărite");

                    if(dataSnapshot.child("Followers").hasChild(userID))
                    {
                        visitFollow.setText("Urmărit");
                        visitFollow.setBackground(getResources().getDrawable(R.drawable.btn_style_2));
                        visitFollow.setTextColor(getResources().getColor(R.color.alb));
                    }
                    else
                    {
                        visitFollow.setText("Urmărește");
                        visitFollow.setBackground(getResources().getDrawable(R.drawable.btn_style_1));
                        visitFollow.setTextColor(getResources().getColor(R.color.colorPrimary));
                    }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mQueryFinishedBooks.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getChildrenCount()==0)
                    visitFinishedBooksText.setText("Nu a citit nicio carte până acum");



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mQueryReading.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getChildrenCount()==0)
                    visitReadingText.setText("Nu citește nimic momentan");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        visitFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProcessFollow = true;

                    mDatabaseUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(mProcessFollow == true) {

                                mProcessFollow = false;

                                if(dataSnapshot.child("Followers").hasChild(userID)) {

                                    mDatabaseUser.child("Followers").child(userID).removeValue();
                                    mDatabaseMyUser.child("Following").child(visitUserID).removeValue();

                                    //Toast.makeText(getContext(),"32",Toast.LENGTH_SHORT).show();

                                    mDatabaseMyUser.child("following").setValue(IntToString(StringToInt(FollowingCount)-1));
                                    mDatabaseUser.child("followers").setValue(IntToString(StringToInt(FollowerCount)-1));



                                }
                                else
                                {

                                    mDatabaseUser.child("Followers").child(userID).setValue("RandomValue");
                                    mDatabaseMyUser.child("Following").child(visitUserID).setValue("RandomValue");

                                    mDatabaseMyUser.child("following").setValue(IntToString(StringToInt(FollowingCount)+1));
                                    mDatabaseUser.child("followers").setValue(IntToString(StringToInt(FollowerCount)+1));

                                }

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Carte,VisitCardCarteViewHolder> FBRA = new FirebaseRecyclerAdapter<Carte, VisitCardCarteViewHolder>(
                Carte.class,
                R.layout.card_book,
                VisitCardCarteViewHolder.class,
                mQueryFinishedBooks

        ) {
            @Override
            protected void populateViewHolder(VisitCardCarteViewHolder viewHolder, Carte model, int position) {

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

        visitFinishedBooks.setAdapter(FBRA);

        FirebaseRecyclerAdapter<Carte,VisitCardCarteViewHolder> FBRA2 = new FirebaseRecyclerAdapter<Carte, VisitCardCarteViewHolder>(
                Carte.class,
                R.layout.card_book,
                VisitCardCarteViewHolder.class,
                mQueryReading

        ) {
            @Override
            protected void populateViewHolder(VisitCardCarteViewHolder viewHolder, Carte model, int position) {

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

        visitReading.setAdapter(FBRA2);

    }

    public static class VisitCardCarteViewHolder extends RecyclerView.ViewHolder
    {

        View mView;

        public VisitCardCarteViewHolder(@NonNull View itemView)
        {
            super(itemView);

            mView = itemView;
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

        public void setImage(Context ctx, String image)
        {
            ImageView carte_img = (ImageView) mView.findViewById(R.id.cardBookImage );
            Picasso.with(ctx).load(image).into(carte_img);
        }

    }

    public int StringToInt(String s)
    {

        int x=0,p=1;

        for(int i=s.length()-1;i>=0;--i)
        {
            x = x + p*(s.charAt(i)-'0');
            p*=10;
        }

        return x;

    }

    public String IntToString(int n)
    {

        String s="";

        int k=-1;

        int[] v = new int[5];

        do
        {
            k++;
            v[k]=n%10;
            n/=10;
        }while(n>0);

        for(int i=k;i>=0;--i)
            s = s + v[i];

        return s;

    }


}
