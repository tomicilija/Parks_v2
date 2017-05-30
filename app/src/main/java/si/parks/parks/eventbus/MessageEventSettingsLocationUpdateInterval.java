package si.parks.parks.eventbus;


public class MessageEventSettingsLocationUpdateInterval {
    int interval;

    public int getInterval() {
        return interval;
    }

    public MessageEventSettingsLocationUpdateInterval(int interval) {

        this.interval = interval;
    }
}
