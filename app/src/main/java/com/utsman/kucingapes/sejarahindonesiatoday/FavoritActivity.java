package com.utsman.kucingapes.sejarahindonesiatoday;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class FavoritActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    FirebaseUser user;

    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<Getter, FavoritActivity.ItemViewHolder> recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorit);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid());
        mDatabase.keepSynced(true);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Query query = mDatabase.orderByKey();

        FirebaseRecyclerOptions optionsItem = new FirebaseRecyclerOptions.Builder<Getter>()
                .setQuery(query, Getter.class).build();

        recyclerAdapter = new FirebaseRecyclerAdapter<Getter, ItemViewHolder>(optionsItem) {
            @Override
            protected void onBindViewHolder(@NonNull final ItemViewHolder holder, int position, @NonNull final Getter model) {
                holder.setTitle(model.getTitle());
                holder.setDate(model.getDate());
                holder.setImg(getBaseContext(), model.getImg());

                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String img = model.getImg();
                        final String title = model.getTitle();
                        final String date = model.getDate();
                        final String body = model.getBody();
                        Intent intent = new Intent(getApplicationContext(), FavResult.class);
                        intent.putExtra("img", img);
                        intent.putExtra("title", title);
                        intent.putExtra("date", date);
                        intent.putExtra("body", body);
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_fav, parent, false);
                return new FavoritActivity.ItemViewHolder(view);
            }
        };

        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        recyclerAdapter.stopListening();
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        View view;

        public ItemViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

        public void setTitle(String title){
            TextView tvJudul = view.findViewById(R.id.re_judul);
            tvJudul.setText(title);
        }

        public void setDate (String date){
            TextView tvDate = view.findViewById(R.id.re_date);
            tvDate.setText(date);
        }

      /*  public void setImageView (String imageView, Context context){
            ImageView img = view.findViewById(R.id.re_img);
            Glide.with(context)
                    .load(imageView)
                    .into(img);
        }*/
        public void setImg(Context ctx, String img){
            ImageView imgView = view.findViewById(R.id.re_img);
            Glide.with(ctx).load(img).into(imgView);
        }
    }
}