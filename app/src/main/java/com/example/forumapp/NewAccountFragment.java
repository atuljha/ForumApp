package com.example.forumapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewAccountFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewAccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewAccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewAccountFragment newInstance(String param1, String param2) {
        NewAccountFragment fragment = new NewAccountFragment();
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
        return inflater.inflate(R.layout.fragment_new_account, container, false);
    }

    TextView email;
    TextView password;
    TextView name;
    FirebaseAuth mauth;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        getActivity().setTitle("Create New Account");
        mauth = FirebaseAuth.getInstance();
        email = view.findViewById(R.id.emailId);
        password = view.findViewById(R.id.passwordId);
        name = view.findViewById(R.id.namePerson);

        view.findViewById(R.id.createAccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, new LoginFragment(), "login")
                        .commit();
            }
        });

        view.findViewById(R.id.SubmitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().isEmpty() || email.getText().toString().isEmpty() || password.getText().toString().isEmpty()){

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("Please enter valid details");
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
                    mauth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){

                                        FirebaseUser user = mauth.getCurrentUser();
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(name.getText().toString())
                                                .build();
                                        user.updateProfile(profileUpdates);
                                       /* FirebaseFirestore fireStore = FirebaseFirestore.getInstance();

                                        Map<String, Object> data = new HashMap<>();
                                        data.put("name", name.getText().toString());
                                        data.put("forums", new ArrayList<Forum>());

                                       fireStore.collection("forumdata").document(email.getText().toString()).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(@NonNull Task<Void> task) {*/
                                               getActivity().getSupportFragmentManager().beginTransaction()
                                                       .replace(R.id.fragmentContainerView, new ForumsFragment(),"forums")
                                                       .commit();
                                          /* }
                                       });*/

                                       //Toast.makeText(getActivity()," User Success", Toast.LENGTH_LONG).show();
                                    }else{
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                builder.setMessage(task.getException().getMessage());
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
                                    }
                                }
                            });
                }
            }
        });

    }
}