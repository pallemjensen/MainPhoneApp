package com.example.mainphoneapp.DB;

import android.content.Context;
import android.util.Log;

import com.example.mainphoneapp.Model.BEFriend;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.example.mainphoneapp.MainActivity.TAG;

public class FirestoreImpl implements IDataAccess{

    private FirebaseFirestore fireDb = FirebaseFirestore.getInstance();
    private CollectionReference friendsColRef = fireDb.collection("Friends");
    private DocumentReference friendRef = fireDb.collection("Friends").document();

    public FirestoreImpl(Context context) {
    }

    @Override
    public long insert(BEFriend f) {
        return 0;
    }

    @Override
    public void deleteById(long id) {

    }

    @Override
    public void update(BEFriend f) {

    }

    @Override
    public List<BEFriend> getAll() {
        final List<BEFriend> list = new ArrayList<BEFriend>();
        friendsColRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            BEFriend beFriend = documentSnapshot.toObject(BEFriend.class);
                            list.add(beFriend);
                        }
                    }
                });
        Log.d(TAG, "Sending ----------------------" + list.size() + " friend objects from firestoreGetAll Method" );
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
