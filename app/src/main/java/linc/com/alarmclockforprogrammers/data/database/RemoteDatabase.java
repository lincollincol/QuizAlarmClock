package linc.com.alarmclockforprogrammers.data.database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Repo;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.schedulers.Schedulers;

public class RemoteDatabase {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public RemoteDatabase() {
        this.firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public Single<DataSnapshot> getDataSnapshot(final String reference) {
        return Single.create((SingleOnSubscribe<DataSnapshot>) emitter-> {
            this.databaseReference = this.firebaseDatabase.getReference(reference);
            this.databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   emitter.onSuccess(dataSnapshot);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    emitter.onError(databaseError.toException());
                }
            });
        })      .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.io());
    }


}
