package linc.com.alarmclockforprogrammers.data.repository;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import linc.com.alarmclockforprogrammers.domain.entity.Question;
import linc.com.alarmclockforprogrammers.domain.entity.Alarm;
import linc.com.alarmclockforprogrammers.data.database.alarms.AlarmDao;
import linc.com.alarmclockforprogrammers.data.database.questions.QuestionsDao;
import linc.com.alarmclockforprogrammers.utils.callbacks.VersionUpdateCallback;

public class RepositoryAlarms {

    private AlarmDao alarmDao;
    private QuestionsDao questionsDao;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public RepositoryAlarms(AlarmDao alarmDao, QuestionsDao questionsDao) {
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.alarmDao = alarmDao;
        this.questionsDao = questionsDao;
    }

    /** Alarms*/
    public Observable<List<Alarm>> getAlarms() {
        return Observable.create((ObservableOnSubscribe<List<Alarm>>) emitter -> {
            try{
                emitter.onNext(alarmDao.getAll());
                emitter.onComplete();
            }catch (Exception e){
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable deleteAlarm(Alarm alarm) {
        return Completable.fromAction(
                () -> alarmDao.deleteAlarm(alarm)
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable updateAlarm(Alarm alarm) {
        return Completable.fromAction(
                () -> alarmDao.updateAlarm(alarm)
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /** Questions*/
//todo replace to questions repos
    /** !!==!!==!!==!!*/
    // todo replace to firebase package
    public void updateLocalQuestionsVersion(VersionUpdateCallback callback) {
        this.databaseReference = this.firebaseDatabase.getReference("questions_version");
        this.databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                callback.onRemoteUpdated(((String)dataSnapshot.getValue()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    public void updateLocalQuestions() {
        List<Question> questions = new ArrayList<>();
        this.databaseReference = this.firebaseDatabase.getReference("questions");
        this.databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    questions.add(new Question(
                                    ((Long) (ds.child("id").getValue())).intValue(),
                                    ((Long) (ds.child("difficult").getValue())).intValue(),
                                    ((Long) (ds.child("trueAnswerPosition").getValue())).intValue(),
                                    (String) (ds.child("programmingLanguage").getValue()),
                                    (String) (ds.child("preQuestion").getValue()),
                                    (String) (ds.child("postQuestion").getValue()),
                                    toJson((ArrayList<String>) (ds.child("answers").getValue())),
                                    (String) (ds.child("htmlCodeSnippet").getValue()),
                                    ((Boolean) (ds.child("completed").getValue()))));
                }
                Disposable d = updateQuestions(questions)
                        .subscribe(() -> {}, e -> e.printStackTrace());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {/** Not implemented*/}
        });
    }

    private Completable updateQuestions(List<Question> questions) {
        Log.d("SIZE", "updateQuestions: " + questions.size());
        return Completable.fromAction(() -> {
            for(Question q : questions) {
                try {
                    questionsDao.insert(q);
                }catch (Exception e) {
                    Log.d("ELEMENT EXIST", ""+q.getId() );
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private String toJson(List<String> answers) {
        return new Gson().toJson(answers);
    }

}
