package com.example.forumapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewForumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewForumFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewForumFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewForumFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewForumFragment newInstance(String param1, String param2) {
        NewForumFragment fragment = new NewForumFragment();
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
        return inflater.inflate(R.layout.fragment_new_forum, container, false);
    }

    TextView formTitle;
    TextView formDesc;
    FirebaseAuth mauth;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        getActivity().setTitle("New Forum");

        formTitle = view.findViewById(R.id.forumTile);
        formDesc = view.findViewById(R.id.forumDescription);
        mauth = FirebaseAuth.getInstance();

        view.findViewById(R.id.forumSubmit).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if(formDesc.getText().toString().isEmpty() || formTitle.getText().toString().isEmpty()){

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage(" Forum title/description is missing! ");
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
                        }
                    });
                }else{
                    FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
                    CollectionReference forumRef = fireStore.collection("forums");
                    DateFormat DFormat
                            = DateFormat.getDateTimeInstance(
                            DateFormat.LONG, DateFormat.LONG,
                            Locale.getDefault());

                    Forum forum = new Forum(mauth.getCurrentUser().getDisplayName(), DFormat.format(Calendar.getInstance().getTime()), formTitle.getText().toString(),
                            formDesc.getText().toString(), new ArrayList(), new ArrayList());
                   // forumRef.update("forums", FieldValue.arrayUnion(forum));
                    forumRef.add(forum);

                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });

        view.findViewById(R.id.cancelPost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

    }
}