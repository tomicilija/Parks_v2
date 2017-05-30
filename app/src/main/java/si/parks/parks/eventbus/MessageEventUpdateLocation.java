package si.parks.parks.eventbus;

import android.location.Location;



public class MessageEventUpdateLocation {
    Location m;

    public MessageEventUpdateLocation(Location m) {
        this.m = m;
    }

    public Location getM() {
        return m;
    }
}
