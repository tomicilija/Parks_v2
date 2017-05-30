package si.parks.parks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.example.DataAll;
import com.example.Lokacija;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;

//https://github.com/osmdroid/osmdroid
public class ActivityLocationFullMap extends AppCompatActivity {
    MapView mMapView;
    MyApp app;
    Lokacija l;
    String ID;
    ArrayList<OverlayItem> items;
    private ItemizedOverlayWithFocus<OverlayItem> mMyLocationOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context ctx = getApplicationContext();
        //important! set your user agent to prevent getting banned from the osm servers
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_full_map);
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.setTileSource(TileSourceFactory.MAPNIK);
        mMapView.setBuiltInZoomControls(true);
        mMapView.setMultiTouchControls(true);

        items = new ArrayList<OverlayItem>();

        mMyLocationOverlay = new ItemizedOverlayWithFocus<OverlayItem>(items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        IMapController mapController = mMapView.getController();
                        mapController.setCenter(item.getPoint());
                        mapController.zoomTo(mMapView.getMaxZoomLevel());
                        return true;
                    }

                    @Override
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        Intent i = new Intent(getBaseContext(), ActivityLocationFullMap.class);
                        i.putExtra(DataAll.LOKACIJA_ID, l.getId());
                        startActivity(i);
                        return false;
                    }
                }, this);
        mMyLocationOverlay.setFocusItemsOnTap(true);

        mMapView.getOverlays().add(mMyLocationOverlay);
        app = (MyApp) getApplication();

    }
    void setLokacija(String ID) {
        l = app.getLocationByID(ID);
        IMapController mapController = mMapView.getController();
        mapController.setZoom(18);
        GeoPoint startPoint = new GeoPoint(l.getLatitude(), l.getLongitude());

        mMyLocationOverlay.removeAllItems();
        mMyLocationOverlay.addItem(new OverlayItem(l.getName(),l.getLatitude()+";"+l.getLongitude(),startPoint));
        mapController.setCenter(startPoint);

        //update(l);
    }
    public void onResume(){
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        Bundle extras = getIntent().getExtras();
        if( (extras !=null))
        {
            ID = extras.getString(DataAll.LOKACIJA_ID);
            setLokacija(extras.getString(DataAll.LOKACIJA_ID));

        } else {
            System.out.println("Nič ni v extras!");
        }

    }
}
