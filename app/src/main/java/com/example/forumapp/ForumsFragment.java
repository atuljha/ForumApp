package com.example.forumapp;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForumsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForumsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ForumsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ForumsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ForumsFragment newInstance(String param1, String param2) {
        ForumsFragment fragment = new ForumsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forums, container, false);
    }

    List<Forum> forums = new ArrayList<>();
    ForumAdapter forumAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        getActivity().setTitle("Forums");
        view.findViewById(R.id.createPost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, new NewForumFragment(), "createForum")
                        .addToBackStack(null)
                        .commit();
            }
        });


      view.findViewById(R.id.Logout).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              FirebaseAuth.getInstance().signOut();
              getActivity().getSupportFragmentManager().beginTransaction()
                      .replace(R.id.fragmentContainerView, new LoginFragment(), "login")
                      .commit();
          }
      });
        updateForums();

        RecyclerView recyclerView = view.findViewById(R.id.postsRecycleView);
         forumAdapter = new ForumAdapter(forums);
        recyclerView.setLayoutManager( new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.HORIZONTAL));
        recyclerView.setAdapter(forumAdapter);
    }

    private void updateForums() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("forums").addSnapshotListener(new EventListener<QuerySnapshot>() {


            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                forums.clear();
                for(QueryDocumentSnapshot documentSnapshot: value){
                    Forum listForum=   (Forum) documentSnapshot.toObject(Forum.class);
                    listForum.setId(documentSnapshot.getId());
                  if(listForum != null)
                  forums.add(listForum);
                }
                forumAdapter.notifyDataSetChanged();
            }
        });
    }


    public class ForumAdapter extends RecyclerView.Adapter<ViewHolder>{

       List<Forum> forums = new ArrayList<>();

        public ForumAdapter(List<Forum> forums) {
            this.forums = forums;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

         View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.posts, parent, false);
         ViewHolder viewHolder = new ViewHolder(view);

            return viewHolder;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

           Forum forum = forums.get(position);

           holder.itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   getActivity().getSupportFragmentManager().beginTransaction()
                           .replace(R.id.fragmentContainerView, ForumDetails.newInstance(forum), "forum")
                           .addToBackStack(null)
                           .commit();
               }
           });

            holder.formTitle.setText(forums.get(position).getTitle());
            holder.formDesc.setText(forums.get(position).getDescription());
            holder.createdBy.setText(forums.get(position).getName());
            holder.createdAt.setText(forums.get(position).getCreatedAt());
            holder.likes.setText(forum.getLikes().size() + "Likes |");

            if(Objects.nonNull(forums.get(position).getName()) &&  forums.get(position).getName().equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())){
                holder.imageView.setVisibility(View.VISIBLE);
            }else{
                holder.imageView.setVisibility(View.INVISIBLE);
            }
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
                    CollectionReference forumRef = fireStore.collection("forums");
                    forumRef.document(forum.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                forumAdapter.notifyDataSetChanged();
                            }
                        }
                    });

                }
            });

            if(forum.getLikes().contains(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())){
                holder.likeDislike.setImageResource(R.drawable.like_favorite);
            }else {
                holder.likeDislike.setImageResource(R.drawable.like_not_favorite);
            }

            holder.likeDislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
                    DocumentReference forumRef = fireStore.collection("forums").document(forum.getId());
                    if(!forum.getLikes().contains(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())) {
                        forumRef.update("likes", FieldValue.arrayUnion(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()));
                    }else{
                        forumRef.update("likes", FieldValue.arrayRemove(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()));
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return forums.size();
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView formTitle;
        TextView formDesc;
        TextView createdBy;
        TextView createdAt;
        ImageView imageView;
        ImageView likeDislike;
        TextView likes;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            formTitle = itemView.findViewById(R.id.forumTitle);
            formDesc = itemView.findViewById(R.id.forumDesc);
            createdAt = itemView.findViewById(R.id.createdAt);
            createdBy = itemView.findViewById(R.id.createdBy);
            imageView = itemView.findViewById(R.id.imageView);
            likeDislike = itemView.findViewById(R.id.likeImage);
            likes = itemView.findViewById(R.id.Likes);

        }
    }

}