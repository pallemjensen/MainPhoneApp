package com.example.mainphoneapp.Model;

import java.util.List;

public interface IDataAccess {

    long insert(BEFriend f);

    void deleteAll();

    void deleteById(long id);

    void update(BEFriend f);

    List<BEFriend> getAll();

    BEFriend getById(long id);
}
