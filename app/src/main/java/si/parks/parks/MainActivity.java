package si.parks.parks;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.DataAll;
import com.example.Lokacija;

public class MainActivity extends AppCompatActivity {
    MyApp app;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.myrecycleview);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        app = (MyApp) getApplication();
        mAdapter = new AdapterLokacija(app.getAll(), this);
        mRecyclerView.setAdapter(mAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Lokacija l = app.getTestLocation();
                //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //         .setAction("Action", null).show();
                Intent i = new Intent(getBaseContext(), TakePhoto.class);
                i.putExtra(DataAll.LOKACIJA_ID, ActivityLocation.NEW_LOCATION_ID);
                startActivity(i);
            }
        });
    }
    public void onClickOpen(View view){
        // Snackbar.make(view, "Izbral si xxx:" + mySpin.getSelectedItem().toString(), Snackbar.LENGTH_LONG)
        //         .setAction("Action", null).show();
        Intent i = new Intent(getBaseContext(), ActivityLocation.class);
        //i.putExtra(DataAll.LOKACIJA_ID,  mySpin.getSelectedItem().toString());
        startActivity(i);

    }
    public void onClickOpen2(View view){
        // Snackbar.make(view, "Izbral si xxx:" + mySpin.getSelectedItem().toString(), Snackbar.LENGTH_LONG)
        //         .setAction("Action", null).show();
        Intent i = new Intent(getBaseContext(), ActivityLocation.class);
        //i.putExtra(DataAll.LOKACIJA_ID,  ((Lokacija)mySpin2.getSelectedItem()).getId());
        startActivity(i);

    }
    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

}
