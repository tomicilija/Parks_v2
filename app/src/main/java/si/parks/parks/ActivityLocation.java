package si.parks.parks;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.DataAll;
import com.example.Lokacija;
import com.example.LokacijaTag;
import com.frosquivel.magicalcamera.Functionallities.PermissionGranted;
import com.frosquivel.magicalcamera.MagicalCamera;
import com.google.android.flexbox.FlexboxLayout;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import si.parks.parks.eventbus.MessageEventUpdateLocation;
import me.gujun.android.taggroup.TagGroup;

public class ActivityLocation extends AppCompatActivity {

    MyApp app;
    ImageView ivSlika;
    EditText edName;
    TextView tvLatLag;
    TextView tvDatum;
    Button save;
    Lokacija l;
    String ID;
    TagGroup tagGroup;
    PermissionGranted permissionGranted;
    MagicalCamera magicalCamera;
    FlexboxLayout flexBoxLayout;
    ArrayList<TagTextView> tags;

    boolean stateNew;
    public static String NEW_LOCATION_ID="NEW_LOCATION";

    private int RESIZE_PHOTO_PIXELS_PERCENTAGE = 20;


    Location mLocation;

    public Location getLocation() {
        return mLocation;
    }

    MapView mMapView;
    DisplayMetrics dm;
    ArrayList<OverlayItem> items;
    private ItemizedOverlayWithFocus<OverlayItem> mMyLocationOverlay;

    @Subscribe
    public void onMessageEvent(MessageEventUpdateLocation event) {
        Log.i("ActivityLocation","MessageEventUpdateLocation ");
        mLocation = event.getM();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location); //se ustvarijo vizualni objekti
        app = (MyApp) getApplication();
        tags = new ArrayList<>();
        ivSlika =(ImageView) findViewById(R.id.imageViewMain);
        tvLatLag = (TextView) findViewById(R.id.textViewLanLat);
        edName = (EditText) findViewById(R.id.editTextName);
        tvDatum = (TextView) findViewById(R.id.textViewDatum);
        flexBoxLayout = (FlexboxLayout) findViewById(R.id.flexBoxLayout);

        mMapView = (MapView) findViewById(R.id.map);
        mMapView.setTileSource(TileSourceFactory.MAPNIK);
        mMapView.setBuiltInZoomControls(false);
        mMapView.setMultiTouchControls(false);

     /*   mMapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), ActivityLocationFullMap.class);
                i.putExtra(DataAll.LOKACIJA_ID, l.getId());
                startActivity(i);
            }
        });
        mMapView.setClickable(true);
        mMapView.setEnabled(true);*/
        items = new ArrayList<OverlayItem>();
        mMapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Intent i = new Intent(getBaseContext(), ActivityLocationFullMap.class);
                i.putExtra(DataAll.LOKACIJA_ID, l.getId());
                startActivity(i);
                return true;
            }
        });

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
        stateNew = false;

        permissionGranted = new PermissionGranted(this); //need for magic camera also checkPermissions
        if (android.os.Build.VERSION.SDK_INT >= 24) {
            permissionGranted.checkAllMagicalCameraPermission();
        }else{
            permissionGranted.checkCameraPermission();
            permissionGranted.checkReadExternalPermission();
            permissionGranted.checkWriteExternalPermission();
            permissionGranted.checkLocationPermission();
        }
        ID ="";
    }



    void setLokacija(String ID) {
        l = app.getLocationByID(ID);
        setTagsViewList( app.getAll().getTagList(ID));
        //LokacijaTag
        update(l);
    }

    void setTagsViewList(ArrayList<LokacijaTag> lt) {
        tags.clear();
        for (LokacijaTag t:lt) { //save them all for update
            TagTextView tv = new TagTextView(this, t,true);
            //  flexBoxLayout.addView(tv);
            tags.add(tv);
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //CALL THIS METHOD EVER
        magicalCamera.resultPhoto(requestCode, resultCode, data);

        //this is for rotate picture in this method
        //magicalCamera.resultPhoto(requestCode, resultCode, data, MagicalCamera.ORIENTATION_ROTATE_180);

        //with this form you obtain the bitmap (in this example set this bitmap in image view)
        ivSlika.setImageBitmap(magicalCamera.getPhoto());

        //if you need save your bitmap in device use this method and return the path if you need this
        //You need to send, the bitmap picture, the photo name, the directory name, the picture type, and autoincrement photo name if           //you need this send true, else you have the posibility or realize your standard name for your pictures.
        //String path = magicalCamera.savePhotoInMemoryDevice(magicalCamera.getPhoto(),"myPhotoName",  true);
        String path = magicalCamera.savePhotoInMemoryDevice(magicalCamera.getPhoto(),"myPhotoName", MagicalCamera.JPEG, true);


        if(path != null){
            if (mLocation!=null)
                l = new Lokacija("mLocation ",  mLocation.getLatitude(), mLocation.getLongitude(),app.getAll().getUserMe().getIdUser(),path,System.currentTimeMillis());
            else
                l = new Lokacija("Poimenuj ", app.getLastLocation().getLatitude(),  app.getLastLocation().getLongitude(),app.getAll().getUserMe().getIdUser(),path,System.currentTimeMillis());
            //new location
            setTagsViewList(app.getAll().getDefultTagLists(app.getDefultTags(),l));
            update(l);
            System.out.println("Path l:"+l);
            Toast.makeText(this, "The photo is save in device, please check this path: " + path, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Sorry your photo dont write in devide, please contact with fabian7593@gmail and say this error", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    public void addNewLocation() {
        if (magicalCamera ==null) magicalCamera =  new MagicalCamera(this,RESIZE_PHOTO_PIXELS_PERCENTAGE,permissionGranted);
        magicalCamera.takePhoto();

    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        startService(new Intent(app, GPSTracker.class));//start service

        Bundle extras = getIntent().getExtras();
        if( (extras !=null) && (!ID.equals(NEW_LOCATION_ID)))
        {
            ID = extras.getString(DataAll.LOKACIJA_ID);
            if (ID.equals(NEW_LOCATION_ID)) {
                stateNew = true;
                addNewLocation();
            }else {
                stateNew = false;
                setLokacija(extras.getString(DataAll.LOKACIJA_ID));
            }
        } else {
            System.out.println("Nič ni v extras!");
        }
    }
    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onPause() {
        stopService(new Intent(app, GPSTracker.class));//start service
        super.onPause();
    }



    public void update(Lokacija l) {
        tvLatLag.setText(l.getLatitude()+" "+l.getLongitude());
        edName.setText(l.getName());
        tvDatum.setText(Util.dt.format(new Date(l.getDate())));
        //update tags
        flexBoxLayout.removeAllViews();
        for (TagTextView t:tags) {
            //flexBoxLayout.addView(t);
        }


        IMapController mapController = mMapView.getController();
        mapController.setZoom(18);
        GeoPoint startPoint = new GeoPoint(l.getLatitude(),l.getLongitude());


        mMyLocationOverlay.removeAllItems();
        mMyLocationOverlay.addItem(new OverlayItem(l.getName(),l.getLatitude()+";"+l.getLongitude(),startPoint));
        mapController.setCenter(startPoint);
        //mMapView.set
        File f = new File(l.getFileName()); //
        if (l.hasImage()) {
            Picasso.with(this)
                    .load(f) //URL
                    .placeholder(R.drawable.ic_cloud_download_black_124dp)
                    .error(R.drawable.ic_error_black_124dp)
                    // To fit image into imageView
                    .fit().centerCrop()
                    // To prevent fade animation
                    .noFade()
                    .into(ivSlika);}
        else {
            if (l.getLatitude()==46.3154)
                ivSlika.setImageDrawable(this.getDrawable(R.drawable.triglav));
            else if (l.getLatitude()==45.7657)
                ivSlika.setImageDrawable(this.getDrawable(R.drawable.cerkinsko));
            else if (l.getLatitude()==45.4942)
                ivSlika.setImageDrawable(this.getDrawable(R.drawable.soline));
            else
            ivSlika.setImageDrawable(this.getDrawable(R.drawable.bled2));
        }
    }

    public void save() {
        System.out.println("Prej:"+l);
        l.setName(edName.getText().toString());
        System.out.println("Po:"+l);
        app.save();
    }

    public void onClickSaveMe(View v) {
        for (TagTextView tv:tags) {
            tv.updateObjectState(); //sets LogationTag
            if (stateNew) {
                app.getAll().addNewLocationTag(tv.getTag());
            }
        }
        if (stateNew) {
            app.getAll().addLocation(l);
            System.out.println("l:"+l);
        }
        save();
        finish();
    }

}

