package si.parks.parks;

import java.text.SimpleDateFormat;


public class Util {
    public static SimpleDateFormat dt = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    public static SimpleDateFormat dateF = new SimpleDateFormat("dd.MM.yyyy");
    public static final float DEG2RAD = (float) (Math.PI / 180.0);
    public static final int RADIUS_EARTH_METERS = 6378137;

    public static final float RAD2DEG = (float) (180.0 / Math.PI);

    public static final float PI = (float) Math.PI;
    public static final float PI_2 = PI / 2.0f;
    public static final float PI_4 = PI / 4.0f;

    public static int distance(double lat1, double long1, double lat2, double long2) {
        final double a1 = DEG2RAD * lat1;
        final double a2 = DEG2RAD * long1;
        final double b1 = DEG2RAD * lat2;
        final double b2 = DEG2RAD * long2;
        final double cosa1 = Math.cos(a1);
        final double cosb1 = Math.cos(b1);
        final double t1 = cosa1 * Math.cos(a2) * cosb1 * Math.cos(b2);
        final double t2 = cosa1 * Math.sin(a2) * cosb1 * Math.sin(b2);
        final double t3 = Math.sin(a1) * Math.sin(b1);
        final double tt = Math.acos(t1 + t2 + t3);
        return (int) (RADIUS_EARTH_METERS * tt);
    }

    public static final String getDistanceInString(int meters) {
        if (meters<1000) return meters+" m";
        return ((int) (meters/1000))+" km";
    }

    public static final String getTimeDiff(long from, long t){
        int d = (Math.round((from-t))/1000/3600);
        if (d<48) return d+" h";
        return Math.round(d/24)+" d";
    }

    public static final String getDistanceInString(double lat1, double long1, double lat2, double long2) {
        return getDistanceInString(distance(lat1,long1,lat2,long2));
    }


}
