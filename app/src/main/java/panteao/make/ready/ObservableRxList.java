package panteao.make.ready;

import panteao.make.ready.utils.cropImage.helpers.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ListIterator;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class ObservableRxList<T> {

    protected final ArrayList<T> list;
    protected final PublishSubject<T> subject;

    public ObservableRxList() {
        Logger.e("ObservableRxList", "Created");
        list = new ArrayList<T>();
        subject = PublishSubject.create();
    }

    public void add(T value) {
        list.add(value);
        subject.onNext(value);
    }
    public void addAll(Collection<T> collection){
        list.addAll(collection);
    }

    public void update(T value) {
        for (ListIterator<T> it = list.listIterator(); it.hasNext(); ) {
            if (value.equals(it.next())) {
                it.set(value);
                subject.onNext(value);
                return;
            }
        }
    }

    public void remove(T value) {
        list.remove(value);
        subject.onNext(value);
    }

    public Observable<T> getObservable() {
        return subject;
    }

    public Observable<T> getCurrentList() {
        return Observable.fromIterable(list);
    }

    public void clear() {
        list.clear();
    }

    public int size() {
        return list.size();
    }
}