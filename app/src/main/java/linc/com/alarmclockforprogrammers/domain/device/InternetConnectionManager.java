package linc.com.alarmclockforprogrammers.domain.device;

public interface InternetConnectionManager {
    boolean isConnected();
    boolean previousConnectionState();
}
