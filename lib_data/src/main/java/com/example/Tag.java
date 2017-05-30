package com.example;


public class Tag {
    private String ime;
    private boolean checked;

    public Tag(Tag t) {
        ime =t.getIme();
        checked = t.isChecked();
    }

    public String getHtmlFromat() {
        if (checked) {
            return "<b>"+ime+"<b>";
        } else {
            return ime;
        }
    }
    public String idTag() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public Tag(String ime) {
        this.ime = ime;
        checked = false;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "ime='" + ime + '\'' +
                '}';
    }

    public String getIme() {
        return ime;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
