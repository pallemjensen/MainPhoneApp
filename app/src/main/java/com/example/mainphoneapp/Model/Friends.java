package com.example.mainphoneapp.Model;

import java.util.ArrayList;

public class Friends {

    ArrayList<BEFriend> m_friends;

    public Friends()
    {
        m_friends = new ArrayList<BEFriend>();
        m_friends.add(new BEFriend("Palle", "31240918",55.511104, 8.410175, "pmj74@hotmail.com", "www.tv2.dk", "pallePicture", "06-01-1974" ));
        m_friends.add(new BEFriend("Sven", "234567", 55.470290, 8.477170, "sven@mail.com", "www.dr.dk", "svenPicture", "01-01-1973" ));
        m_friends.add(new BEFriend("Morten", "126256", 55.464348, 8.585723, "morten@mail.com", "www.golf.com", "mortenPicture", "15-10-1996"));
    }

    public ArrayList<BEFriend> getAll()
    { return m_friends; }

    public String[] getNames()
    {
        String[] res = new String[m_friends.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = m_friends.get(i).getName();
        }
        return res;
    }

}