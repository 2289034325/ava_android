package com.acxca.ava.presentation.consts;

public enum Lang {
    EN(1,"English"),
    JP(2,"日本語"),
    KR(3,"한국어"),
    FR(4,"Français");

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

    public Lang next(){
        switch (id) {
            case 1:
                return JP;
            case 2:
                return KR;
            case 3:
                return FR;
            case 4:
                return EN;
            default:
                return EN;
        }
    }



}