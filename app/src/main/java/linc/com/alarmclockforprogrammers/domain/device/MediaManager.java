package linc.com.alarmclockforprogrammers.domain.device;

import java.io.IOException;

public interface MediaManager {

    void startPlayer(String path) throws IOException;
    void stopPlayer();

}
