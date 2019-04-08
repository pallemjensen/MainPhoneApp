package com.example.mainphoneapp.DB;

import com.example.mainphoneapp.Model.BEFriend;

import java.util.List;

public interface IDataAccess {

    long insert(BEFriend f);

    void deleteById(long id);

    void update(BEFriend f);

    List<BEFriend> getAll();

    BEFriend getById(long id);
}
