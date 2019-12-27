package linc.com.alarmclockforprogrammers.data.repository;

import com.google.firebase.database.DataSnapshot;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import linc.com.alarmclockforprogrammers.data.database.RemoteDatabase;
import linc.com.alarmclockforprogrammers.data.database.achievements.AchievementsDao;
import linc.com.alarmclockforprogrammers.data.database.questions.QuestionsDao;
import linc.com.alarmclockforprogrammers.data.mapper.AchievementEntityMapper;
import linc.com.alarmclockforprogrammers.data.mapper.QuestionEntityMapper;
import linc.com.alarmclockforprogrammers.data.preferences.LocalPreferencesManager;

import static linc.com.alarmclockforprogrammers.utils.Consts.ACHIEVEMENTS_LOCAL_VERSION;
import static linc.com.alarmclockforprogrammers.utils.Consts.ACHIEVEMENTS_REMOTE;
import static linc.com.alarmclockforprogrammers.utils.Consts.ACHIEVEMENTS_REMOTE_VERSION;
import static linc.com.alarmclockforprogrammers.utils.Consts.QUESTIONS_LOCAL_VERSION;
import static linc.com.alarmclockforprogrammers.utils.Consts.QUESTIONS_REMOTE;
import static linc.com.alarmclockforprogrammers.utils.Consts.QUESTIONS_REMOTE_VERSION;
import static linc.com.alarmclockforprogrammers.utils.Consts.THEME;

public class RepositoryVersionUpdate {

    private RemoteDatabase firebase;
    private QuestionsDao questionsDao;
    private AchievementsDao achievementsDao;
    private QuestionEntityMapper questionMapper;
    private AchievementEntityMapper achievementMapper;

    private LocalPreferencesManager preferences;

    public RepositoryVersionUpdate(RemoteDatabase firebase,
                                   QuestionsDao questionsDao,
                                   AchievementsDao achievementsDao,
                                   QuestionEntityMapper questionMapper,
                                   AchievementEntityMapper achievementMapper,
                                   LocalPreferencesManager preferences) {
        this.firebase = firebase;
        this.questionsDao = questionsDao;
        this.achievementsDao = achievementsDao;
        this.questionMapper = questionMapper;
        this.achievementMapper = achievementMapper;
        this.preferences = preferences;
    }

    public Completable updateLocalVersions() {
        return Completable.create(emitter -> {
            Disposable d = firebase.getDataSnapshot(ACHIEVEMENTS_REMOTE_VERSION)
                    .zipWith(firebase.getDataSnapshot(QUESTIONS_REMOTE_VERSION), (achieveDS, questionDS) -> {
                        String achievementsRemoveVersion = ((String) achieveDS.getValue());
                        String questionsRemoveVersion = ((String) questionDS.getValue());

                        if (!preferences.getString(ACHIEVEMENTS_LOCAL_VERSION).equals(achievementsRemoveVersion)
                                || achievementsDao.getItemCount() == 0) {
                            Disposable achieveLocal = updateAchievements()
                                    .subscribe(() -> {
                                        preferences.saveString(achievementsRemoveVersion, ACHIEVEMENTS_LOCAL_VERSION);
                                    });
                        }

                        if(!preferences.getString(QUESTIONS_LOCAL_VERSION).equals(questionsRemoveVersion)
                                || questionsDao.getItemCount() == 0) {
                            Disposable questionLocal = updateQuestions()
                                    .subscribe(() -> {
                                        preferences.saveString(questionsRemoveVersion, QUESTIONS_LOCAL_VERSION);
                                    });
                        }

                        emitter.onComplete();
                        return achieveDS;
                    }).subscribe();

        });
    }

    private Completable updateQuestions() {
        return Completable.create(emitter -> {
            Disposable d = firebase.getDataSnapshot(QUESTIONS_REMOTE)
                    .subscribe(dataSnapshot -> {
                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            try {
                                questionsDao.insert(questionMapper.toQuestionEntity(ds));
                            }catch (Exception ignored) {}
                        }
                        emitter.onComplete();
                    }, emitter::onError);
        });
    }

    private Completable updateAchievements() {
        return Completable.create(emitter -> {
            Disposable d = firebase.getDataSnapshot(ACHIEVEMENTS_REMOTE)
                    .subscribe(dataSnapshot -> {
                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            try {
                                achievementsDao.insert(achievementMapper.toAchievementEntity(ds));
                            }catch (Exception ignored) {}
                        }
                        emitter.onComplete();
                    }, emitter::onError);
        });
    }

    public Single<Boolean> getTheme() {
        return Single.fromCallable(() -> preferences.getBoolean(THEME));
    }
}
