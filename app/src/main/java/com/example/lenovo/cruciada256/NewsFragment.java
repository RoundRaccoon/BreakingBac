package com.example.lenovo.cruciada256;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.NumberFormat;


public class NewsFragment extends Fragment {

    RecyclerView mEventList;
    String userID;
    LinearLayout farastiri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news, container, false);

        mEventList = (RecyclerView) view.findViewById(R.id.eventList);
        mEventList.setHasFixedSize(true);
        mEventList.setLayoutManager(new LinearLayoutManager(getActivity()));

        farastiri = (LinearLayout) view.findViewById(R.id.farastiri);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<News,NewsViewHolder> FBRA = new FirebaseRecyclerAdapter<News, NewsViewHolder>(
                News.class,
                R.layout.news_row,
                NewsViewHolder.class,
                FirebaseDatabase.getInstance().getReference().child("News")
        ) {
            @Override
            protected void populateViewHolder(final NewsViewHolder viewHolder, News model, int position) {

                final String post_key = getRef(position).getKey();
                final News model_use = model;

                if(post_key.compareTo("cnt")==0)
                    viewHolder.yeetView();
                else
                {

                    FirebaseDatabase.getInstance().getReference().child("Users").child(userID).child("Following").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(!dataSnapshot.hasChild(model_use.getUserID()))
                            {
                                viewHolder.yeetView();
                            }
                            else
                            {

                                viewHolder.init();
                                viewHolder.setStuff(model_use.getUserID(),model_use.getBookID(),model_use.getDate(),model_use.getTip(),getContext());

                                farastiri.setVisibility(View.GONE);


                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }
        };

        mEventList.setAdapter(FBRA);

    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder{

        View mView;

        CardView cardView;

        public NewsViewHolder(@NonNull View itemView)
        {
            super(itemView);

            mView = itemView;

            cardView = (CardView) mView.findViewById(R.id.cardNewsFull);

        }

        public void yeetView()
        {
            cardView.setVisibility(View.GONE);
            //cardView.setLayoutParams(new CardView.LayoutParams(0,0));

            cardView.requestLayout();
            cardView.getLayoutParams().height=0;
            cardView.getLayoutParams().width=0;

        }

        public void init()
        {

            mView.setVisibility(View.VISIBLE);

        }

        public void setStuff(String user, String book, String date, String tip, final Context ctx)
        {
            final ImageView profilePic = (ImageView) mView.findViewById(R.id.newsProfilePic);
            final TextView userName = (TextView) mView.findViewById(R.id.newsUserName);
            final TextView postDate = (TextView) mView.findViewById(R.id.newsDate);
            final TextView mesaj = (TextView) mView.findViewById(R.id.newsMessage);
            final ImageView copertaCarte = (ImageView) mView.findViewById(R.id.newsPoza);
            final TextView numeCarte = (TextView) mView.findViewById(R.id.newsNumeCarte);
            final TextView autorCarte = (TextView) mView.findViewById(R.id.newsAutorCarte);
            final TextView ratingCarte = (TextView) mView.findViewById(R.id.newsRating);

            final ImageView carteStea1 = (ImageView) mView.findViewById(R.id.newsStea1);
            final ImageView carteStea2 = (ImageView) mView.findViewById(R.id.newsStea2);
            final ImageView carteStea3 = (ImageView) mView.findViewById(R.id.newsStea3);
            final ImageView carteStea4 = (ImageView) mView.findViewById(R.id.newsStea4);
            final ImageView carteStea5 = (ImageView) mView.findViewById(R.id.newsStea5);

            postDate.setText(date);

            if(tip.compareTo("start")==0)
            {
                mesaj.setText("A început să citească o carte nouă.");
            }
            else
            {
                mesaj.setText("A terminat de citit o carte.");
            }

            FirebaseDatabase.getInstance().getReference().child("Users").child(user).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Picasso.with(ctx).load(dataSnapshot.child("image").getValue().toString()).into(profilePic);
                    userName.setText(dataSnapshot.child("Name").getValue().toString());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            FirebaseDatabase.getInstance().getReference().child("books").child(book).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Picasso.with(ctx).load(dataSnapshot.child("image").getValue().toString()).into(copertaCarte);
                    numeCarte.setText(dataSnapshot.child("nume").getValue().toString());
                    autorCarte.setText(dataSnapshot.child("autor").getValue().toString());

                    String totalRecenziiString,totalSteleString;
                    int totalRecenzii,totalStele;

                    if(dataSnapshot.hasChild("totalRec"))
                        totalRecenziiString = dataSnapshot.child("totalRec").getValue().toString();
                    else
                        totalRecenziiString = "0";

                    if(dataSnapshot.hasChild("totalStele"))
                        totalSteleString = dataSnapshot.child("totalStele").getValue().toString();
                    else
                        totalSteleString = "0";

                    totalRecenzii = StringToInt(totalRecenziiString);
                    totalStele = StringToInt(totalSteleString);

                    if(totalRecenzii==0)
                    {

                        Picasso.with(ctx).load(R.drawable.steagoala).into(carteStea1);
                        Picasso.with(ctx).load(R.drawable.steagoala).into(carteStea2);
                        Picasso.with(ctx).load(R.drawable.steagoala).into(carteStea3);
                        Picasso.with(ctx).load(R.drawable.steagoala).into(carteStea4);
                        Picasso.with(ctx).load(R.drawable.steagoala).into(carteStea5);

                        ratingCarte.setText("(?)");

                    }
                    else
                    {

                        float rating = (float)totalStele/totalRecenzii;

                        NumberFormat format = NumberFormat.getNumberInstance();

                        format.setMinimumFractionDigits(1);
                        format.setMaximumFractionDigits(1);

                        String out = format.format(rating);

                        StringBuilder out2 = new StringBuilder(out);
                        out2.setCharAt(1,'.');

                        ratingCarte.setText("("+out2+")");

                        if(rating>4.7)
                        {
                            Picasso.with(ctx).load(R.drawable.steaplina).into(carteStea1);
                            Picasso.with(ctx).load(R.drawable.steaplina).into(carteStea2);
                            Picasso.with(ctx).load(R.drawable.steaplina).into(carteStea3);
                            Picasso.with(ctx).load(R.drawable.steaplina).into(carteStea4);
                            Picasso.with(ctx).load(R.drawable.steaplina).into(carteStea5);
                        }
                        else if(rating>4.2)
                        {
                            Picasso.with(ctx).load(R.drawable.steaplina).into(carteStea1);
                            Picasso.with(ctx).load(R.drawable.steaplina).into(carteStea2);
                            Picasso.with(ctx).load(R.drawable.steaplina).into(carteStea3);
                            Picasso.with(ctx).load(R.drawable.steaplina).into(carteStea4);
                            Picasso.with(ctx).load(R.drawable.steajuma).into(carteStea5);
                        }
                        else if(rating>3.7)
                        {
                            Picasso.with(ctx).load(R.drawable.steaplina).into(carteStea1);
                            Picasso.with(ctx).load(R.drawable.steaplina).into(carteStea2);
                            Picasso.with(ctx).load(R.drawable.steaplina).into(carteStea3);
                            Picasso.with(ctx).load(R.drawable.steaplina).into(carteStea4);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(carteStea5);
                        }
                        else if(rating>3.2)
                        {
                            Picasso.with(ctx).load(R.drawable.steaplina).into(carteStea1);
                            Picasso.with(ctx).load(R.drawable.steaplina).into(carteStea2);
                            Picasso.with(ctx).load(R.drawable.steaplina).into(carteStea3);
                            Picasso.with(ctx).load(R.drawable.steajuma).into(carteStea4);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(carteStea5);
                        }
                        else if(rating>2.7)
                        {
                            Picasso.with(ctx).load(R.drawable.steaplina).into(carteStea1);
                            Picasso.with(ctx).load(R.drawable.steaplina).into(carteStea2);
                            Picasso.with(ctx).load(R.drawable.steaplina).into(carteStea3);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(carteStea4);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(carteStea5);
                        }
                        else if(rating>2.2)
                        {
                            Picasso.with(ctx).load(R.drawable.steaplina).into(carteStea1);
                            Picasso.with(ctx).load(R.drawable.steaplina).into(carteStea2);
                            Picasso.with(ctx).load(R.drawable.steajuma).into(carteStea3);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(carteStea4);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(carteStea5);
                        }
                        else if(rating>1.7)
                        {
                            Picasso.with(ctx).load(R.drawable.steaplina).into(carteStea1);
                            Picasso.with(ctx).load(R.drawable.steaplina).into(carteStea2);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(carteStea3);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(carteStea4);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(carteStea5);
                        }
                        else if(rating>1.2)
                        {
                            Picasso.with(ctx).load(R.drawable.steaplina).into(carteStea1);
                            Picasso.with(ctx).load(R.drawable.steajuma).into(carteStea2);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(carteStea3);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(carteStea4);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(carteStea5);
                        }
                        else if(rating>0.7)
                        {
                            Picasso.with(ctx).load(R.drawable.steaplina).into(carteStea1);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(carteStea2);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(carteStea3);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(carteStea4);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(carteStea5);
                        }
                        else
                        {
                            Picasso.with(ctx).load(R.drawable.steagoala).into(carteStea1);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(carteStea2);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(carteStea3);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(carteStea4);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(carteStea5);
                        }

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

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
            }while(n!=0);

            for(int i=k;i>=0;--i)
                s = s + v[i];

            return s;

        }


    }

}
