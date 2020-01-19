package com.acxca.ava.presentation.consts;

public enum Lang {
    EN(1,"English"),
    JP(2,"Japanese"),
    KR(3,"Korean"),
    FR(4,"French");

    private int id;
    private String name;

    Lang(int id,String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }
    public String getName() {return name;}

    public static Lang fromId(int id){
        switch (id) {
            case 1:
                return EN;
            case 2:
                return JP;
            case 3:
                return KR;
            case 4:
                return FR;
            default:
                return EN;
        }
    }

}