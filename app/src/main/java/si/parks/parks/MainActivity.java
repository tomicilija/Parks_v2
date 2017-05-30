package si.parks.parks;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.DataAll;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import si.parks.parks.eventbus.MessageEventUpdateLocation;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private MyApp app;
    private FloatingActionButton fab; //TODO https://github.com/Clans/FloatingActionButton
    private RecyclerView mRecyclerView;
    private ActivityMainAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    //https://github.com/2dxgujun/AndroidTagGroup
    //https://github.com/Cutta/TagView
    private Location mLocation;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                startActivity(new Intent(this,ActivityMySettings.class));
                return true;

            case R.id.action_sort:
                //app.sortUpdate();
                app.sortChangeAndUpdate();
                mAdapter.notifyDataSetChanged();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEventUpdateLocation event) {
        Log.i("ActivityZacetna","MessageEventUpdateLocation ");
        mLocation = event.getM();
        mAdapter.setLastLocation(mLocation);
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (MyApp) getApplication();
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.myrecycleview);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        app = (MyApp) getApplication();
        mAdapter = new ActivityMainAdapter(app.getAll(), this);
        mRecyclerView.setAdapter(mAdapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setDeleteOnSwipe(mRecyclerView);

        mLocation=null;
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundColor(getResources().getColor(R.color.colorGray));
        //fab.set
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //         .setAction("Action", null).show();
                if (mLocation==null) {
                    //                   Snackbar.make(view, getResources().getText(R.string.add_new_location_no_location), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    Snackbar.make(view, getResources().getText(R.string.add_new_location_no_location), Snackbar.LENGTH_LONG).show();
                }else {
                    Intent i = new Intent(getBaseContext(), ActivityLocation.class);
                    i.putExtra(DataAll.LOKACIJA_ID, ActivityLocation.NEW_LOCATION_ID);
                    startActivity(i);
                }
            }
        });

    }

    private void getPermissions() {
        MultiplePermissionsListener my  = new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (!report.areAllPermissionsGranted()) {
                    new android.app.AlertDialog.Builder(MainActivity.this)
                            .setTitle(getString(R.string.permission_must_title))
                            .setMessage(getString(R.string.permission_must))
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    MainActivity.this.finish(); //end app!
                                }
                            })
                            .setIcon(R.drawable.trash_icon)
                            .show();
                }}
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        };


        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.CAMERA
                ).withListener(my).check();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getPermissions();

    }

    //TODO Show V7
    //http://stackoverflow.com/questions/27293960/swipe-to-dismiss-for-recyclerview
    public void setDeleteOnSwipe(final RecyclerView mRecyclerView) {


        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                app.removeLocationByPosition(viewHolder.getAdapterPosition());
                                app.save();
                                mRecyclerView.getAdapter().notifyDataSetChanged();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                mRecyclerView.getAdapter().notifyDataSetChanged();
                                break;
                        }
                        // mRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Delete location");
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener)
                ;
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                });
                builder.show();

            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }


    @Override
    protected void onResume() {
        super.onResume();
        //mLocation = app.getLastLocation();
        if (mLocation==null) {

            fab.setBackgroundColor(Color.BLUE);
        }
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);
        //  checkPermissions();
        app.sortUpdate();
        mAdapter.notifyDataSetChanged();

        startService(new Intent(app, GPSTracker.class));//start service

        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorGray)));



    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
        // stopService(new Intent(ActivityZacetna.this, GPSTracker.class));
    }


    public Location getLocation() {
        return mLocation;
    }
}
