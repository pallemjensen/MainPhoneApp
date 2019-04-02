package com.example.mainphoneapp.Model;

import java.util.List;

public interface IDataAccess {

    long insert(BEFriend p);

    void deleteAll();

    void deleteById(long id);

    void update(BEFriend p);

    List<BEFriend> getAll();

    BEFriend getById(long id);
}
