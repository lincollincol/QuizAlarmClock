package linc.com.alarmclockforprogrammers.model.interactor.wake;

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
import linc.com.alarmclockforprogrammers.model.repository.wake.RepositoryWake;

public class InteractorWake {

    private RepositoryWake repository;

    public InteractorWake(RepositoryWake repository) {
        this.repository = repository;
    }

    public Observable<List<Question>> getQuestions(String programmingLanguage, int difficult) {
        int numberOfQuestions = ((difficult < 1) ? 3 : (difficult > 1) ? 1 : 2);
        return repository.getQuestions(programmingLanguage, difficult)
                .map(questions -> {
                    List<Question> randomQuestions = new ArrayList<>();
                    Set<Integer> randomIds = new LinkedHashSet<>();
                    Random r = new Random();
                    while (randomIds.size() < numberOfQuestions) {
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



    private List<String> fromJson(String answersJson) {
        return new Gson().fromJson(answersJson, ArrayList.class);
    }

}
