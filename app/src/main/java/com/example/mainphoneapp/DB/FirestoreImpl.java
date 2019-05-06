package com.example.mainphoneapp.DB;

import android.content.Context;

import com.example.mainphoneapp.Model.BEFriend;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class FirestoreImpl implements IDataAccess{

    final List<BEFriend> list = new ArrayList<BEFriend>();
    private FirebaseFirestore fireDb = FirebaseFirestore.getInstance();
    private CollectionReference friendsColRef = fireDb.collection("Friends");
    private DocumentReference friendRef = fireDb.collection("Friends").document();

    public FirestoreImpl(Context context) {
    }

    @Override
    public void insert(BEFriend f) {
    }

    @Override
    public void deleteById(long id) {

    }

    @Override
    public void update(BEFriend f) {

    }

    @Override
    public List<BEFriend> getAll() {
        friendsColRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            BEFriend beFriend = documentSnapshot.toObject(BEFriend.class);
                            String friendId = documentSnapshot.getId();
                            beFriend.setId(friendId);
                            list.add(beFriend);
                        }
                    }
                });
        return list;
    }

    @Override
    public BEFriend getById(long id) {
        return null;
    }

    @Override
    public String getFirestoreDocumentId() {
        return  null;
    }

}
