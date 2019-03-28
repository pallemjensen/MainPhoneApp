package com.example.mainphoneapp.Model;

import java.util.ArrayList;

public class Friends {

    ArrayList<BEFriend> m_friends;

    public Friends()
    {
        m_friends = new ArrayList<BEFriend>();
        m_friends.add(new BEFriend("Palle", "31240918",34.511104, 11.410175, "pmj74@hotmail.com", "http://www.tv2.dk", "pallePicture", "06-01-1974", "Haandvaerkervej 38 6710 Esbjerg" ));
        m_friends.add(new BEFriend("Sven", "234567", 55.470290, 8.477170, "sven@mail.com", "http://www.dr.dk", "svenPicture", "01-01-1973", "Rahbeks Alle 16 6700 Esbjerg" ));
        m_friends.add(new BEFriend("Morten", "126256", 55.464348, 8.585723, "morten@mail.com", "http://www.golf.com", "mortenPicture", "15-10-1996", "Kirkegade 76 6700 Esbjerg"));
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