package com.example;

import java.util.ArrayList;

public class TagList {
    private ArrayList<Tag> list;

    public TagList() {
        list = new ArrayList<>();
        list.add(new Tag("Mokro"));
        list.add(new Tag("Železo"));
        list.add(new Tag("Igle"));
        list.add(new Tag("Plastenka"));
        list.add(new Tag("Kosovni"));
        list.add(new Tag("Nujno"));
        list.add(new Tag("Gozd"));
        list.add(new Tag("Organski odpadki"));
        list.add(new Tag("Divje odlagališče"));
        list.add(new Tag("Slika je lažna"));
        list.add(new Tag("Lokacija je lažna"));

    }

    @Override
    public String toString() {
        return "TagList{" +
                "list=" + list +
                '}';
    }
    public  ArrayList<Tag> getClone() {
        ArrayList<Tag> l = new ArrayList<>();
        for (Tag t:list) {
            l.add(new Tag(t));
        }
        return l;
    }
    public Tag getPrvi() {
        return list.get(0);
    }
}
