/**
 *  @AssignmetName - HW05
 *  @FileName Group16_HW05.zip
 * @Author Atul Jha & Shivani Varma
 */

package com.example.forumapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainerView, new com.example.forumapp.LoginFragment(), "login")
                .commit();


       /* mauth = FirebaseAuth.getInstance();
        mauth.signInWithEmailAndPassword("a@a.com", "123456")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                        }else{

                        }
                    }
                });

        mauth.createUserWithEmailAndPassword("putuli@j.com", "123456")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){


                        }
                    }
                });
        getData();*/
    }

    private void getData() {
        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();

        fireStore.collection("contacts").get()
                .addOnCompleteListener(this, new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        for(QueryDocumentSnapshot doc: task.getResult()){
                            doc.getData();
                        }
                    }
                });


    }

  void  getStorage(){
      FirebaseStorage storage = FirebaseStorage.getInstance();
      StorageReference storageRef = storage.getReference();
    }
}