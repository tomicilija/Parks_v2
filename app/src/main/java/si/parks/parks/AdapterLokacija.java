package si.parks.parks;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.DataAll;
import com.example.Lokacija;

/**
 * Created by crepinsek on 12/03/17.
 */

class AdapterLokacija extends RecyclerView.Adapter<AdapterLokacija.ViewHolder> {
    DataAll all;
    Activity ac;



    public AdapterLokacija(DataAll all, Activity ac) {
        this.all = all;
        this.ac = ac;
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtHeader;
        public TextView txtFooter;
        public ImageView iv;

        public ViewHolder(View v) {
            super(v);
            txtHeader = (TextView) v.findViewById(R.id.firstLine);
            txtFooter = (TextView) v.findViewById(R.id.secondLine);
            iv = (ImageView)v.findViewById(R.id.icon);
        }
    }

 /*   public void add(int position,Oglas item) {
        mDataset.dodaj().add(position, item);
        notifyItemInserted(position);
    }

    public void remove(String item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }
    */

      @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listrow, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    private static void startDView(String lokacijaID, Activity ac) {
      //  System.out.println(name+":"+position);
        Intent i = new Intent(ac.getBaseContext(), AddLocation.class);
        i.putExtra(DataAll.LOKACIJA_ID,  lokacijaID);
        ac.startActivity(i);

    }
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Lokacija trenutni = all.getLocation(position);
        final String name = trenutni.getName();
        holder.txtHeader.setText(name+" "+trenutni.getIdUser());
        //holder.iv.setImageDrawable(this.  ac.getDrawable(R.drawable.ic_airline_seat_recline_extra_black_24dp));

        if (position%2==1) {
            holder.txtHeader.setTextColor(Color.BLUE);
        }
        holder.txtHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdapterLokacija.startDView(trenutni.getId(),ac);
            }
        });
        holder.txtFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdapterLokacija.startDView(trenutni.getId(),ac);
            }
        });

        holder.txtFooter.setText("Lokacija: " + trenutni.getLatitude()+","+trenutni.getLongitude());
    }


    @Override
    public int getItemCount() {
        return all.getLocationSize();
    }
}
