package com.example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataAll {
    public static final String LOKACIJA_ID = "lokacija_idXX";

    //http://stackoverflow.com/questions/4772425/change-date-format-in-a-java-string

    private User userMe;
    private ArrayList<Lokacija> lokacijaList;
    private ArrayList<LokacijaTag> lokacijaTagList;

    public static String getHtmlFormatedLocationTagList(ArrayList<LokacijaTag> l) {
        StringBuffer sb= new StringBuffer();
        for (int i=0; i<l.size(); i++) {
            sb.append(l.get(i).getHtmlFromat());
            if (i<(l.size()-1)) sb.append(", ");
        }
        return sb.toString();
    }

    public Lokacija getLocationByID(String ID) {
        for (Lokacija l: lokacijaList) { //TODO this solution is relatively slow! If possible don't use it!
            // if (l.getId() == ID) return l; //NAPAKA primerja reference
            if (l.getId().equals(ID)) return l;
        }
        return null;
    }

    public DataAll() {
        userMe = new User("neznani.nedolocen@nekje.ne","NiDefiniran");

        lokacijaList = new ArrayList<>();
        lokacijaTagList = new ArrayList<>();
    }

    public Lokacija addLocation(String name, double x, double y, String im) {
        if (im==null) im = Lokacija.NODATA;
        else
        if (im.trim().length()==0) im = Lokacija.NODATA;
        Lokacija tmp = new Lokacija(name, x,y, userMe.getIdUser(),im, System.currentTimeMillis());
        lokacijaList.add(tmp);
        return tmp;
    }
    public void addLocationTag(Lokacija l, Tag t) {
        lokacijaTagList.add(new LokacijaTag(l.id, t, System.currentTimeMillis(),userMe.getIdUser()));
    }

    public User getUserMe() {
        return userMe;
    }

    @Override
    public String toString() {
        return "DataAll{" +

                ", \nuserMe=" + userMe +
                ", \nlokacijaList=" + lokacijaList +
                ", \nlokacijaTagList=" + lokacijaTagList +
                '}';
    }
    //   public Lokacija(String name, long x, long y, String idUser, String fileName, long date) {
    public static DataAll scenarijA() {
        DataAll da = new DataAll();
        Date danes = new Date();
        da.userMe = new User("ilijatomic17@gmail.com","Tomic");
        Lokacija tmp;
        tmp = da.addLocation("Triglavski narodni park", 46.3154, 13.7797, Lokacija.NODATA);
        tmp = da.addLocation("Cerkinsko jezero", 45.7657, 14.3542, Lokacija.NODATA);
            tmp = da.addLocation("Soline", 45.4942, 13.6059, Lokacija.NODATA);


        return da;
    }

    public Lokacija getLocation(int i) {
        return lokacijaList.get(i);
    }

    public List<Lokacija> getLokacijaAll() {
        return lokacijaList;
    }

    public Lokacija getNewLocation(double d1, double d2, String filename) {
        return addLocation(Lokacija.NODATA, d1, d2, filename);
    }

    public int getLocationSize() {
        return lokacijaList.size();
    }

    public void addLocation(Lokacija l) {
        lokacijaList.add(l);

    }
    public void addNewLocationTag(LokacijaTag tag) {
        lokacijaTagList.add(tag);
    }
    public void addNewLocationTags(ArrayList<LokacijaTag> tags) {
        lokacijaTagList.addAll(tags);
    }

    /*
    TODO Speed up this simple implementation!
     */
    public ArrayList<LokacijaTag> getTagList(String locationId) {
        ArrayList<LokacijaTag> tags = new ArrayList<>();
        for (LokacijaTag lt:lokacijaTagList) {
            if (lt.getIdLokacija().equals(locationId)) {
                tags.add(lt);
            }
        }
        return  tags;
    }

    public void removeFromTagList(String locationId) {
        for (int i=lokacijaTagList.size()-1;i>=0; i--) {
            if (lokacijaTagList.get(i).getIdLokacija().equals(locationId))
                lokacijaTagList.remove(i);
        }
    }

    public ArrayList<LokacijaTag> getDefultTagLists(ArrayList<Tag> tags, Lokacija l) {
        ArrayList<LokacijaTag> lt = new ArrayList<>();

        for (Tag t:tags){
            lt.add(new LokacijaTag(l.getId(),t,System.currentTimeMillis(),userMe.getIdUser()));
        }
        return lt;
    }
}
