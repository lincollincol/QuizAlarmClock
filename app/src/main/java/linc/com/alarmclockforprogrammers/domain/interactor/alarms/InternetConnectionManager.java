package linc.com.alarmclockforprogrammers.domain.interactor.alarms;

public interface InternetConnectionManager {
    boolean isConnected();
    boolean lastState();
}
