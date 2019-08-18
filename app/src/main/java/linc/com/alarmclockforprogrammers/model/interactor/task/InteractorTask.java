package linc.com.alarmclockforprogrammers.model.interactor.task;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import linc.com.alarmclockforprogrammers.entity.Question;
import linc.com.alarmclockforprogrammers.model.data.preferences.PreferencesAlarm;
import linc.com.alarmclockforprogrammers.model.repository.task.RepositoryTask;

public class InteractorTask {

    private RepositoryTask repository;
    private PreferencesAlarm preferences;

    public InteractorTask(RepositoryTask repository, PreferencesAlarm preferences) {
        this.repository = repository;
        this.preferences = preferences;
    }

    public Observable<List<Question>> getQuestions(String programmingLanguage, int difficult) {
        return repository.getQuestions(programmingLanguage, difficult)
                .map(questions -> {
                    List<Question> randomQuestions = new ArrayList<>();
                    Set<Integer> randomIds = new LinkedHashSet<>();
                    Random r = new Random();
                    while (randomIds.size() < getNumberOfQuestions(difficult)*2) {
                        Integer next = r.nextInt(questions.size());
                        randomIds.add(next);
                    }
                    for(int i : randomIds) {
                        randomQuestions.add(questions.get(i));
                    }
                    return randomQuestions;
                })
                .flatMap(Observable::fromIterable)
                .map(question -> {
                    question.setAnswersList(fromJson(question.getJsonAnswers()));
                    return question;
                })
                .toList()
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void setQuestionCompleted(Question question) {
        Log.d("INTERACTOR_LAYER", "setQuestionCompleted: "  + question.isCompleted() + " " + question.getId());
        question.setCompleted(true);
        this.repository.setQuestionCompleted(question)
            .subscribe();
    }

    public int getBalance() {
        return preferences.getBalance();
    }

    public void reduceBalance(int price) {
        int newBalance = preferences.getBalance() - price;
        preferences.saveBalance(newBalance);
    }

    public void increaseBalance(int award) {
        int newBalance = preferences.getBalance() + award;
        preferences.saveBalance(newBalance);
    }

    private int getNumberOfQuestions(int difficult) {
        return ((difficult < 1) ? 3 : (difficult > 1) ? 1 : 2);
    }

    private List<String> fromJson(String answersJson) {
        return new Gson().fromJson(answersJson, ArrayList.class);
    }

}
