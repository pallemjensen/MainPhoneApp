package com.example.mainphoneapp.DB;

import android.content.Context;

import com.example.mainphoneapp.Model.BEFriend;

import java.util.List;

public class FirestoreImpl implements IDataAccess{

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
        return null;
    }

    @Override
    public BEFriend getById(long id) {
        return null;
    }

    @Override
    public String getFirestoreDocumentId() {
        return null;
    }
}
