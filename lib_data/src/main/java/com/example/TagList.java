package com.example;

import java.util.ArrayList;

public class TagList {
    private ArrayList<Tag> list;

    public TagList() {
        list = new ArrayList<>();
        list.add(new Tag("Igle"));
        list.add(new Tag("Plastenke"));
        list.add(new Tag("Kosovni"));
        list.add(new Tag("Nujno"));
        list.add(new Tag("Gozd"));

    }

    @Override
    public String toString() {
        return "TagList{" +
                "list=" + list +
                '}';
    }

    public Tag getPrvi() {
        return list.get(0);
    }
}
