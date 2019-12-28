package linc.com.alarmclockforprogrammers.utils;

public final class Consts {

    /**
     * Packages constants
     */
    public static final String APPLICATION_PACKAGE = "package:linc.com.alarmclockforprogrammers";
    public static final String GMAIL_PACKAGE = "com.google.android.gm";
    public static final String PLAY_MARKET_APP_PACKAGE = "com.android.chrome";
    public static final String PLAY_MARKET_OLD_URI = "market://details?id=";
    public static final String PLAY_MARKET_NEW_URI = "http://play.google.com/store/apps/details?id=";

    /**
     * Notifications, intents and fragment transitions data keys
     */
    public static final String ALARM_ID = "alarm_id";
    public static final String ALARM_JSON = "alarm_json";
    public static final String DIALOG_TAG = "DIALOG";

    public static final String CHANNEL_ID = "alarm_channel";
    public static final String CHANNEL_NAME = "alarm";
    public static final int NOTIFICATION_ID = 2345;
    public static final int NOTIFICATION_PENDING_INTENT_ID = 1010;

    /**
     * Data layer constants
     */
    public static final String WITHOUT_VERSION = "0";
    public static final String THEME = "theme";
    public static final String BALANCE = "balance";

    public static final String QUESTIONS_LOCAL_VERSION = "local_question s_version";
    public static final String QUESTIONS_REMOTE_VERSION = "questions_version";
    public static final String QUESTIONS_REMOTE = "questions";

    public static final String ACHIEVEMENTS_LOCAL_VERSION = "local_achievements_version";
    public static final String ACHIEVEMENTS_REMOTE_VERSION = "achievements_version";
    public static final String ACHIEVEMENTS_REMOTE = "achievements";

    /**
     * Days and time constants
     */
    public static final long ONE_HOUR = 3600000;
    public static final long ONE_MINUTE = 60000;
    public static final long TWO_MINUTES = 120000;
    public static final long ONE_SECOND = 1000;
    public static final int PICKER_HOURS_MAX = 23;
    public static final int PICKER_MINUTES_MAX = 59;
    public static final int PICKER_SECONDS_MAX = 59;
    public static final int PICKERS_MIN = 0;
    public static final int PROGRESS_MIN = 0;
    public static final int WEEK_DAYS = 7;

    /**
     * Design, animations and transitions constants
     */
    // Progress bar
    public static final int ANIMATION_START = 0;
    public static final int ANIMATION_END = 100;
    // Transitions speed
    public static final long FAST_SPEED = 500;
    public static final long NORMAL_SPEED = 800;
    public static final long SLOW_SPEED = 1000;
    // Recycler view insert animations
    public static final int ITEM_INSERT_SPEED_NORMAL = 200;
    public static final int LAST_ITEM_POSITION_DEFAULT = -1;
    public static final float PERCENT_ZERO = 0.0f;
    public static final float PERCENT_FIFTY = 0.5f;
    public static final float PERCENT_HUNDRED = 1.0f;
    // Recycler view rows
    public static final int TWO_ROWS = 2;

    /**
     * Infrastructure types constants
     */
    public static final String TYPE_AUDIO = "audio/mpeg";
    public static final String TYPE_TEXT = "plain/text";


    /**
     * Common constants
     */
    public static final boolean DISABLE = false;
    public static final boolean ENABLE = true;
    public static final int DEFAULT_ALARM_ID = 0;

}


