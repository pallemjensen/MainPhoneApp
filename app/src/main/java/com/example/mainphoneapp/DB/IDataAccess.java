package com.example.mainphoneapp.DB;

import com.example.mainphoneapp.Model.BEFriend;

import java.util.List;

public interface IDataAccess {

    void insert(BEFriend f);

    void deleteAll();

    void deleteById(long id);

    void update(BEFriend f);

    List<BEFriend> getAll();

    BEFriend getById(long id);
}
