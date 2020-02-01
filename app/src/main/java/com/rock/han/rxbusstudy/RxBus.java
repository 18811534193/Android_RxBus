package com.rock.han.rxbusstudy;


import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by Administrator on 2019/9/25.
 */

public class RxBus
{

    private final Subject<Object> bus;
    private static RxBus rxBus;

    private RxBus() {
        bus = PublishSubject.create().toSerialized();
    }

    public static RxBus getInstance() {
        if (null == rxBus) {
            synchronized (RxBus.class) {
                if (null == rxBus) {
                    rxBus = new RxBus();
                }
            }

        }
        return rxBus;
    }

    public void post(Object o) {
        bus.onNext(o);
    }

   /* *//**
     * 主线程中执行
     * @param tag
     * @param rxBusResult
     *//*
    public void toObserverableOnMainThread(final String tag, final RxBusResult rxBusResult) {

        bus.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                if (tags.containsKey(tag)) {
                    rxBusResult.onRxBusResult(o);
                }
            }
        });
    }*/

    public boolean hasObservable() {
        return bus.hasObservers();
    }

    /**
     * 转换为特定类型的Obserbale
     */
    public <T> Observable<T> tObservable(Class<T> type) {
        return bus.ofType(type);
    }

}
