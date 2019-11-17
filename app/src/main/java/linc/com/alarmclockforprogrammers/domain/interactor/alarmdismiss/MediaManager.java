package linc.com.alarmclockforprogrammers.domain.interactor.alarmdismiss;

import java.io.IOException;

public interface MediaManager {

    void startPlayer(String path) throws IOException;
    void stopPlayer();

}
