package com.example;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by crepinsek on 24/02/17.
 */

public class DataAll {
    public static final String LOKACIJA_ID = "lokacija_idXX";
    public static SimpleDateFormat dt = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    //http://stackoverflow.com/questions/4772425/change-date-format-in-a-java-string
    private TagList tags;
    private User userMe;
    private ArrayList<Lokacija> lokacijaList;
    private ArrayList<LokacijaTag> lokacijaTagList;

    public Lokacija getLocationByID(String ID) {
        for (Lokacija l: lokacijaList) { //TODO this solution is relatively slow! If possible don't use it!
            // if (l.getId() == ID) return l; //NAPAKA primerja reference
            if (l.getId().equals(ID)) return l;
        }
        return null;
    }

    public DataAll() {
        userMe = new User("neznani.nedolocen@nekje.ne","NiDefiniran");
        tags = new TagList();
        lokacijaList = new ArrayList<>();
        lokacijaTagList = new ArrayList<>();
    }

    public Lokacija addLocation(String name, double x, double y, String im) {
        Lokacija tmp = new Lokacija(name, x,y, userMe.getIdUser(),im,System.currentTimeMillis());
        lokacijaList.add(tmp);
        return tmp;
    }
    public void addLocationTag(Lokacija l, Tag t) {
        lokacijaTagList.add(new LokacijaTag(l.id, t.idTag(),System.currentTimeMillis(),userMe.getIdUser()));
    }

    @Override
    public String toString() {
        return "DataAll{" +
                "\ntags=" + tags +
                ", \nuserMe=" + userMe +
                ", \nlokacijaList=" + lokacijaList +
                ", \nlokacijaTagList=" + lokacijaTagList +
                '}';
    }
    //   public Lokacija(String name, long x, long y, String idUser, String fileName, long date) {
    public static DataAll scenarijA() {
        DataAll da = new DataAll();
        Date danes = new Date();
        da.userMe = new User("ilija.tomic@student.um.si","IlijaTOMIC");
        Lokacija tmp;
        tmp = da.addLocation("Triglevski narodni park", 22.1212,11.21, "");
        da.addLocationTag(tmp,da.tags.getPrvi());
        tmp = da.addLocation("Postojnska jama", 2212212,113121, "slika.png");
        da.addLocationTag(tmp,da.tags.getPrvi());
        tmp = da.addLocation("Cerkniško jezero", 3212212,23123121, "");
        da.addLocationTag(tmp,da.tags.getPrvi());

       /* for (int i=0; i<130; i++){
            tmp = da.addLocation("Igrišče "+i, 321*i,231*i, "");
            da.addLocationTag(tmp,da.tags.getPrvi());
        }*/

        return da;
    }

    public Lokacija getLocation(int i) {
        return lokacijaList.get(i);
    }

    public List<Lokacija> getLokacijaAll() {
        return lokacijaList;
    }

    public Lokacija getNewLocation(double d1, double d2) {
        return addLocation("N/A", d1, d2, "");
    }

    public int getLocationSize() {
        return lokacijaList.size();
    }

    public void addLocation(Lokacija l) {
        lokacijaList.add(l);

    }

    public User getUserMe() {
        return userMe;
    }
}
