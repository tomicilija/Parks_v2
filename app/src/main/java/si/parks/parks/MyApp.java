package si.parks.parks;

/**
 * Created by Mr Ilija on 10. 04. 2017.
 */

import android.app.Application;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.DataAll;
import com.example.Lokacija;
import com.example.Tag;
import com.example.TagList;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import si.parks.parks.eventbus.MessageEventUpdateLocation;


public class MyApp extends Application {

    public static SharedPreferences preferences;
    DataAll all;
    private static final String DATA_MAP = "ParkMap";
    private static final String FILE_NAME = "Parks.json";
    private Location mLastLocation;
    private TagList tags;
    private static final int SORT_BY_DATE=0;
    private static final int SORT_BY_DISTACE=1;
    int sortType = SORT_BY_DATE;
    public ArrayList<Tag> getDefultTags() {
        return tags.getClone();
    }
    @Override
    public void onCreate() {
        super.onCreate();
        tags = new TagList(); //also sets default tags
        EventBus.getDefault().register(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mLastLocation=null;
        if (!load())
            all = DataAll.scenarijA();
    }

    @Subscribe
    public void onMessageEvent(MessageEventUpdateLocation event) {
        Log.i("MyApp","MessageEventUpdateLocation ");
        mLastLocation = event.getM();
    }
    @Override
    public void onTerminate() {
        EventBus.getDefault().unregister(this);
        super.onTerminate();

    }

    public Location getLastLocation() {
        return mLastLocation;
    }

    public void setLastLocation(Location mLastLocation) {
        this.mLastLocation = mLastLocation;
    }
    public boolean hasLocation() {
        if (mLastLocation==null) return false;
        return true;
    }

    public DataAll getAll() {
        return  all;
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

    public Lokacija getNewLocation(double x, double y, String filename) {
        return all.getNewLocation(x,y, filename);
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
        String id = all.getLokacijaAll().get(adapterPosition).getId();
        all.getLokacijaAll().remove(adapterPosition);
        all.removeFromTagList(id); //
    }

    public void sortUpdate() {
        //sortType= (sortType+1) / 2;
        switch (sortType) {
            case SORT_BY_DATE:{
                Collections.sort(all.getLokacijaAll(), new Comparator<Lokacija>() {
                    @Override
                    public int compare(Lokacija l1, Lokacija l2) {
                        if (l1.getDate()==l2.getDate()) return 0;
                        if (l1.getDate()>l2.getDate()) return -1;
                        return 1;
                    }
                });
            }
            break;
            case SORT_BY_DISTACE:{
                if (mLastLocation==null) return;
                Collections.sort(all.getLokacijaAll(), new Comparator<Lokacija>() {
                    @Override
                    public int compare(Lokacija l1, Lokacija l2) {
                        int d1 = Util.distance(mLastLocation.getLatitude(),mLastLocation.getLongitude(),l1.getLatitude(),l1.getLongitude());
                        int d2 = Util.distance(mLastLocation.getLatitude(),mLastLocation.getLongitude(),l2.getLatitude(),l2.getLongitude());
                        if (d1==d2) return 0;
                        if (d1>d2) return 1;
                        return -1;
                    }
                });

            }
            break;
        }

    }
    public void sortChangeAndUpdate() {
        sortType= (sortType+1) % 2;
        sortUpdate();
    }



}

