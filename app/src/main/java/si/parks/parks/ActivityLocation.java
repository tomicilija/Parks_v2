package si.parks.parks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.DataAll;
import com.example.Lokacija;
import com.frosquivel.magicalcamera.Functionallities.PermissionGranted;
import com.frosquivel.magicalcamera.MagicalCamera;

import java.util.Date;

public class ActivityLocation extends AppCompatActivity {

    MyApp app;
    ImageView ivSlika;
    EditText edName;
    EditText edX;
    EditText edY;
    TextView tvDatum;
    Button save;
    Lokacija l;
    String ID;
    PermissionGranted permissionGranted;
    MagicalCamera magicalCamera;
    boolean stateNew;
    public static String NEW_LOCATION_ID="NEW_LOCATION";

    private int RESIZE_PHOTO_PIXELS_PERCENTAGE = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location); //se ustvarijo vizualni objekti
        app = (MyApp) getApplication();
        ivSlika =(ImageView) findViewById(R.id.imageViewMain);
        edName = (EditText) findViewById(R.id.editTextName);
        stateNew = false;
       /* save = (Button) findViewById(R.id.buttonSave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Klik Save");
            }
        }); */
        //update(app.getTestLocation());
        permissionGranted = new PermissionGranted(this);

        if (android.os.Build.VERSION.SDK_INT >= 24) {
            permissionGranted.checkAllMagicalCameraPermission();
        }else{
            permissionGranted.checkCameraPermission();
            permissionGranted.checkReadExternalPermission();
            permissionGranted.checkWriteExternalPermission();
            permissionGranted.checkLocationPermission();
        }
        //permission for take photo, it is false if the user check deny
/*        permissionGranted.checkCameraPermission();

        //for search and write photoss in device internal memory
        //normal or SD memory
        permissionGranted.checkReadExternalPermission();
        permissionGranted.checkWriteExternalPermission();

        //permission for location for use the `photo information device.
        permissionGranted.checkLocationPermission();
*/



        ID ="";
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if (magicalCamera ==null)    magicalCamera =  new MagicalCamera(this,permissionGranted);
        //CALL THIS METHOD EVER IN THIS OVERRIDE FOR ACTIVATE PERMISSIONS
        magicalCamera.permissionGrant(requestCode, permissions, grantResults);
    }

    void setLokacija(String ID) {
        l = app.getLocationByID(ID);
        update(l);
    }


    public void onSave(View v) {
        System.out.println("Klik Save OnClick method");
    }

    public void onPicture(View v){

        if (magicalCamera ==null) magicalCamera =  new MagicalCamera(this,permissionGranted);

        System.out.println("Klik Save magicalCamera1 method");
        magicalCamera.takePhoto();
        System.out.println("Klik Save magicalCamera2 method");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //CALL THIS METHOD EVER
        magicalCamera.resultPhoto(requestCode, resultCode, data);

        //this is for rotate picture in this method
        //magicalCamera.resultPhoto(requestCode, resultCode, data, MagicalCamera.ORIENTATION_ROTATE_180);

        //with this form you obtain the bitmap (in this example set this bitmap in image view)


        ivSlika = (ImageView)findViewById(R.id.imageView);


        ivSlika.setImageBitmap(magicalCamera.getPhoto());

        //if you need save your bitmap in device use this method and return the path if you need this
        //You need to send, the bitmap picture, the photo name, the directory name, the picture type, and autoincrement photo name if           //you need this send true, else you have the posibility or realize your standard name for your pictures.
        //String path = magicalCamera.savePhotoInMemoryDevice(magicalCamera.getPhoto(),"myPhotoName",  true);
        String path = magicalCamera.savePhotoInMemoryDevice(magicalCamera.getPhoto(),"myPhotoName", MagicalCamera.JPEG, true);


        if(path != null){
            if (l!=null)
                l = new Lokacija("mLocation ",  l.getLatitude(), l.getLongitude(),app.getAll().getUserMe().getIdUser(),path,System.currentTimeMillis());
            //new location
            update(l);
            System.out.println("Path l:"+l);
            Toast.makeText(this, "The photo is save in device, please check this path: " + path, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Sorry your photo dont write in devide, please contact with fabian7593@gmail and say this error", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    public void addNewLocation() {
        if (magicalCamera ==null) magicalCamera =  new MagicalCamera(this,permissionGranted);
        System.out.println("Klik Save magicalCamera1 method");
        magicalCamera.takePhoto();
        System.out.println("Klik Save magicalCamera2 method");

    }
    @Override
    protected void onResume() {
        super.onResume();
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
        //permissionGranted.checkAllMagicalCameraPermission();
       // l = app.getTestLocation();
       // update(l);
    }

    public void update(Lokacija l) {
        //ivSlika
        /*edX.setText(""+l.getX());
        edY.setText(""+l.getY());*/
        edName.setText(l.getName());
        tvDatum.setText(DataAll.dt.format(new Date(l.getDate())));

    }

    public void save() {
        System.out.println("Prej:"+l);
        l.setName(edName.getText().toString());
        System.out.println("Po:"+l);
        app.save();
    }

    public void onClickSaveMe(View v) {
        if (stateNew) app.getAll().addLocation(l);
        //save();
        finish();
    }
}
