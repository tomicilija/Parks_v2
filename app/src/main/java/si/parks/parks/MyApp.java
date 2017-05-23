package si.parks.parks;

/**
 * Created by Mr Ilija on 10. 04. 2017.
 */

import android.app.Application;
import com.example.DataAll;
import com.example.Lokacija;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class MyApp extends Application {
    int x;
    DataAll all;
    private static final String DATA_MAP = "parkM";
    private static final String FILE_NAME = "park.json";

    @Override
    public void onCreate() {
        super.onCreate();
        x= 5;
        all = DataAll.scenarijA();
    }

    public int getX() {
        return x;
    }
    public DataAll getAll() {
        return  all;
    }
    public void setX(int x) {
        this.x = x;
    }
    public Lokacija getTestLocation() {
        return all.getLocation(0);
    }
    public Lokacija getLocationByID(String id) {
        return all.getLocationByID(id);
    }
    public List<Lokacija> getLokacijaAll() {
        return all.getLokacijaAll();
    }
    public Lokacija getNewLocation(double x, double y) {
        return all.getNewLocation(x,y);
    }


    public boolean save() {
        File file = new File(this.getExternalFilesDir(DATA_MAP), ""
                + FILE_NAME);

        return ApplicationJson.save(all,file);
    }

    public boolean load(){
        File file = new File(this.getExternalFilesDir(DATA_MAP), ""
                + FILE_NAME);
        DataAll tmp = ApplicationJson.load(file);
        if (tmp!=null) all = tmp;
        else return false;
        return true;
    }

    public void removeLocationByPosition(int adapterPosition) {
        all.getLokacijaAll().remove(adapterPosition);
    }
}
