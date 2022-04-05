package com.example.forumapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForumDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForumDetails extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";


    // TODO: Rename and change types of parameters
    private Forum mParam1;
    private List<Comment> commentsList = new ArrayList<>();


    public ForumDetails() {
        // Required empty public constructor
    }


    public static ForumDetails newInstance(Forum param1) {
        ForumDetails fragment = new ForumDetails();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (Forum) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forum_details, container, false);
    }

    TextView title;
    TextView createdBy;
    TextView totalComments;
    TextView desc;
    EditText commentDesc;
    CommentsAdapter commentsAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        updateForums();

        title = view.findViewById(R.id.CommentForumTitleId);
        createdBy = view.findViewById(R.id.commentForumCreatedId);
        desc = view.findViewById(R.id.commentForumDecId);
        totalComments = view.findViewById(R.id.CommentDisplayId);

        title.setText(mParam1.getTitle());
        createdBy.setText(mParam1.getName());
        totalComments.setText(mParam1.getComments().size() + " Comments");
        desc.setText(mParam1.getDescription());
        commentDesc = view.findViewById(R.id.commentTextId);

        view.findViewById(R.id.CommentPostBtnId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (commentDesc.getText().toString().isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(" Comment is missing! ");
                    builder.setCancelable(true);

                    builder.setNegativeButton(
                            "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alertBox = builder.create();
                    alertBox.show();
                } else {

                    DateFormat DFormat
                            = DateFormat.getDateTimeInstance(
                            DateFormat.LONG, DateFormat.SHORT,
                            Locale.getDefault());
                    FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
                    DocumentReference forumRef = fireStore.collection("forums").document(mParam1.getId());
                    Comment comment = new Comment(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                            commentDesc.getText().toString(), DFormat.format(Calendar.getInstance().getTime()));
                    forumRef.update("comments", FieldValue.arrayUnion(comment));
                    commentDesc.setText("");

                }

            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.CommentsRecyclerViewId);
        commentsAdapter = new CommentsAdapter(commentsList);
        recyclerView.setLayoutManager( new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.HORIZONTAL));
        recyclerView.setAdapter(commentsAdapter);
    }

    private void updateForums() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("forums").document(mParam1.getId()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                commentsList.clear();

                    Forum listForum=   (Forum) value.toObject(Forum.class);

                    if(listForum != null)
                        commentsList.addAll(listForum.getComments());
                totalComments.setText(commentsList.size() + " Comments");

                commentsAdapter.notifyDataSetChanged();
                }

            });
        }


       public class CommentsAdapter extends RecyclerView.Adapter<CommentsViewHolder>{

        List<Comment> comments;

           public CommentsAdapter(List<Comment> comments) {
               this.comments = comments;
           }

           @NonNull
           @Override
           public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

              View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comments_list_item_view,
                      parent,false);
              CommentsViewHolder commentsViewHolder = new CommentsViewHolder(view);
               return commentsViewHolder;
           }

           @RequiresApi(api = Build.VERSION_CODES.N)
           @Override
           public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {

               Comment comment = comments.get(position);
               holder.commentDesc.setText(comment.getComment());
               holder.createdBy.setText(comment.getCreatedBy());
               holder.createdAt.setText(comment.getCreatedAt());


               if(Objects.nonNull(comments.get(position).getCreatedBy()) &&  comments.get(position).getCreatedBy().equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())){
                   holder.delete.setVisibility(View.VISIBLE);
               }else{
                   holder.delete.setVisibility(View.INVISIBLE);
               }

               holder.delete.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
                       DocumentReference forumRef = fireStore.collection("forums").document(mParam1.getId());

                       forumRef.update("comments", FieldValue.arrayRemove(comment));

                   }
               });


           }

           @Override
           public int getItemCount() {
               return comments.size();
           }
       }


      class CommentsViewHolder extends RecyclerView.ViewHolder{

           TextView createdBy;
           TextView createdAt;
           TextView commentDesc;
           ImageView delete;
           public CommentsViewHolder(@NonNull View itemView) {
               super(itemView);
               createdBy = itemView.findViewById(R.id.commentCreatedById);
               createdAt = itemView.findViewById(R.id.CommentDateId);
               commentDesc = itemView.findViewById(R.id.CommentDisId);
               delete = itemView.findViewById(R.id.commentDeleteId);



           }
       }






}