package com.example.settingsnotification;

public class exemple_item {
    private String sLine1;
    private String sLine2;
    private int intId;

    public exemple_item(String line1, String line2, int Id)
    {
        sLine1 = line1;
        sLine2 = line2;
        intId = Id;
    }

    public void changeText1(String text)
    {
        sLine1 = text;
    }
    public void changeText2(String text)
    {
        sLine2 = text;
    }

    public String getLine1()
    {
        return sLine1;
    }

    public String getLine2()
    {
        return sLine2;
    }

    public int getId() { return intId; }
}
