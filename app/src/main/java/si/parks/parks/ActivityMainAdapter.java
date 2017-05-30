package si.parks.parks;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.DataAll;
import com.example.Lokacija;
import com.squareup.picasso.Picasso;

import java.io.File;


class ActivityMainAdapter extends RecyclerView.Adapter<ActivityMainAdapter.ViewHolder> {
    DataAll all;
    MainActivity ac;
    Location last;
    Lokacija l;
    public static int UPDATE_DISTANCE_IF_DIFF_IN_M=10;


    public void setLastLocation(Location l) {
        if (last==null) {
            last = l;
            notifyDataSetChanged();
        }
        else {
            if (Util.distance(last.getLatitude(),last.getLongitude(),l.getLatitude(),l.getLongitude())>UPDATE_DISTANCE_IF_DIFF_IN_M){
                last = l;
                notifyDataSetChanged();
            }
        }
    }

    public ActivityMainAdapter(DataAll all, MainActivity ac) {
        super();
        this.all = all;
        this.ac = ac;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtHeader;

        public TextView txtDis;
        public TextView txtTime;
        public ImageView iv;

        public ViewHolder(View v) {
            super(v);
            txtHeader = (TextView) v.findViewById(R.id.firstLine);

            txtDis = (TextView) v.findViewById(R.id.textViewDistance);
            txtTime = (TextView) v.findViewById(R.id.textViewTime);
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
        Intent i = new Intent(ac.getBaseContext(), ActivityLocation.class);
        i.putExtra(DataAll.LOKACIJA_ID,  lokacijaID);
        ac.startActivity(i);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Lokacija trenutni = all.getLocation(position);
        if (trenutni.hasImage()) {
            //"http://i.imgur.com/DvpvklR.png"
            File f = new File(trenutni.getFileName()); //
            Picasso.with(ac.getApplicationContext())
                    .load(f) //URL
                    .placeholder(R.drawable.ic_cloud_download_black_124dp)
                    .error(R.drawable.ic_error_black_124dp)
                    // To fit image into imageView
                    .fit().centerCrop()
                    // To prevent fade animation
                    .noFade()
                    .into(holder.iv);

            //   Picasso.with(ac).load(trenutni.getFileName()).into(holder.iv);
            // holder.iv.setImageDrawable(this.ac.getDrawable(R.drawable.ic_airline_seat_recline_extra_black_24dp));
        }
        else {
            if (trenutni.getLatitude()==46.3154)
                holder.iv.setImageDrawable(this.ac.getDrawable(R.drawable.triglav));
            else if(trenutni.getLatitude()==45.7657)
                holder.iv.setImageDrawable(this.ac.getDrawable(R.drawable.cerkinsko));
            else if(trenutni.getLatitude()==45.4942)
                holder.iv.setImageDrawable(this.ac.getDrawable(R.drawable.soline));
            else
            holder.iv.setImageDrawable(this.ac.getDrawable(R.drawable.bled2));
        }
        //holder.txtHeader.setText(Html.fromHtml(DataAll.getHtmlFormatedLocationTagList(all.getTagList(trenutni.getId()))));
        /*
        if (position%2==1) {
            holder.txtHeader.setTextColor(Color.BLUE);
        } else {
            holder.txtHeader.setTextColor(Color.RED);

        }
        */
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityMainAdapter.startDView(trenutni.getId(),ac);
            }
        });
   /*     holder.txtHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityMainAdapter.startDView(trenutni.getId(),ac);
            }
        });

        holder.txtFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityMainAdapter.startDView(trenutni.getId(),ac);
            }
        });
*/
        if (last==null) holder.txtDis.setText("N/A");
        else  holder.txtDis.setText(Util.getDistanceInString(ac.getLocation().getLatitude(), ac.getLocation().getLongitude(),trenutni.getLatitude(),trenutni.getLongitude()));
        //holder.txtFooter.setText("Footer: " + trenutni.getLatitude()+","+trenutni.getLongitude());
        //holder.txtTime.setText(Util.getTimeDiff(System.currentTimeMillis(), trenutni.getDate()));


        holder.txtHeader.setText(trenutni.getName() + "\n" + trenutni.getLatitude() + ", " + trenutni.getLongitude());
    }


    @Override
    public int getItemCount() {
        return all.getLocationSize();
    }
}
