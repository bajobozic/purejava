package Foo;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * Created by bajob on 2/14/2017.
 */

public class Foo {

    static <T> Observable.Transformer<T, T> current(final String name) {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable.doOnSubscribe(() -> logThread(" " + name + " onSubscribe"))
                        .doOnNext(t -> logThread(" " + name + " onNext"));
            }
        };
    }

    enum DayOfWeaks {
        SUNDAY,
        MONDAY
    }

    private static Observable<String> loadRecordsForDay(DayOfWeaks day) {
        switch (day) {
            case SUNDAY:
                return Observable
                        .interval(90, TimeUnit.MILLISECONDS)
                        .take(5)
                        .map(aLong -> "Sun " + aLong);
            case MONDAY:
                return Observable.interval(65, TimeUnit.MILLISECONDS)
                        .take(5)
                        .map(aLong -> "Mon " + aLong);
        }
        return null;
    }

    private static void logThread(final Object object) {
        System.out.println(Thread.currentThread().getName() + ":" + object);
    }

    public static void main(String[] arg) throws InterruptedException {
        String hi = "Hello";
        String name = "Savo";
        String greattins = " Greattings";
        final List<String> stringList = Arrays.asList(hi, name, greattins);
        final List<String> integerList = Arrays.asList("5", "4", "10");
        final List<Integer> intList = Arrays.asList(5, 4, 11, 2, 8);

        /*final Subscription subscription = Observable.from(intList)
                .groupBy(integer -> integer%2==0)
                .compose(current("from+scan"))
                .subscribe(integer -> logThread(integer)
                        , Throwable::printStackTrace
                        , () -> logThread("onCompleated"));*/
        /*final Subscription subscription = Observable.from(intList)
                .reduce((integer, integer2) -> integer+integer2)
                .compose(current("from+scan"))
                .subscribe(integer -> logThread(integer)
                        , Throwable::printStackTrace
                        , () -> logThread("onCompleated"));*/
/*
final Subscription subscription = Observable.from(intList).scan((integer, integer2) -> integer + integer2)
                .compose(current("from+scan"))
                .subscribe(integer -> logThread(integer)
                        , Throwable::printStackTrace
                        , () -> logThread("onCompleated"));
*/

        /*final Subscription subscription = Observable.zip(Observable.from(integerList).subscribeOn(Schedulers.computation()).compose(current("seconde "))
                , Observable.from(stringList).compose(current("first "))
                ,(s, s2) -> s + s2)
                .compose(current("zip"))
                .subscribe(s -> logThread(s),
                        Throwable::printStackTrace,
                        () -> logThread("onCompleated"));*/
/*
        final Subscription subscription = Observable
                .merge(Observable.interval(50, TimeUnit.MILLISECONDS)
                                .take(stringList.size())
                                .map(aLong -> stringList.get(aLong.intValue()))
                        ,
                        Observable.interval(40, TimeUnit.MILLISECONDS)
                                .take(integerList.size())
                                .map(aLong -> integerList.get(aLong.intValue())))
                .subscribe(s -> logThread(s));*/
                /*.merge(Observable.from(stringList).subscribeOn(Schedulers.computation())
                        , Observable.from(integerList).subscribeOn(Schedulers.computation()))
                .subscribe(s -> logThread(s));*/



        /*final Subscription subscription = Observable.just(DayOfWeaks.SUNDAY, DayOfWeaks.MONDAY)
                .flatMap(Foo::loadRecordsForDay)
                .subscribe(s -> logThread(s)
                        , Throwable::printStackTrace
                        , () -> logThread("onCompleated"));*/
     /*   final Subscription subscription = Observable.just("Lorem", "ipsum", "dolor", "sit", "amet", "consectetur", "adipiscing", "elit")
                .delay(word -> Observable.timer(word.length(), TimeUnit.SECONDS))
                .subscribe(s -> logThread(s)
                        , Throwable::printStackTrace
                        , () -> logThread("onCompleated"));*/

        /*final Subscription subscription = Observable.interval(500, 1000, TimeUnit.MILLISECONDS, Schedulers.immediate())
                .take(4)
                .flatMap(aLong -> Observable.just(aLong)
                        , throwable -> Observable.error(throwable)
                        , () -> Observable.from(stringList))
                .subscribe(s -> {logThread(s);}
                           ,Throwable::printStackTrace,
                            () -> {logThread("onCompleated");});*/
        /*System.out.println("Test");
        Subscription subscription = Observable.from(stringList)
                .compose(current("just"))
                .flatMap(s -> Observable.just(s.length()).subscribeOn(Schedulers.computation()).compose(current("Inner just")))
                .compose(current("flatMap"))
                .map(integer -> integer + 2)
                .compose(current("Map"))
                .subscribe(integer -> {
                    logThread("Subscriber " + integer);
                }, Throwable::printStackTrace, () -> {
                    System.out.println("Subscriber onCompleated");
                });*/
        /*System.out.println("Test1");
        Subscription subscription1 = Observable.merge(Observable.just(hi).delay(500, TimeUnit.MILLISECONDS)
                        .doOnNext(s -> {
                            System.out.println(Thread.currentThread().getName() + s + " merge0");
                        })
                , Observable.just(name).delay(500, TimeUnit.MILLISECONDS).doOnNext(s -> {
                    System.out.println(Thread.currentThread().getName() + s + " merge1");
                }))
//                        .observeOn(Schedulers.newThread())
                .subscribe(s -> {
                            logThread(s + " subscriber");
                        }, Throwable::printStackTrace,
                        () -> {
                            System.out.println("onCompleated");
                        });*/
        System.out.println("Test2");
        Subscription subscription = Observable.concat(Observable
                .empty()
                .flatMap(o -> Observable.error(new UnsupportedOperationException("SOME ERROR")).onErrorResumeNext(throwable -> Observable.just(3)))
                ,Observable.range(0, 2))
                .first()
                .subscribe(integer -> {
                    logThread(integer);
                }, Throwable::printStackTrace, () -> {
                    logThread(" onCompleated");
                });
        Thread.sleep(5500);
        if (!subscription.isUnsubscribed()) {
            System.out.println("not unsubscriebed");
            subscription.unsubscribe();


        }
                /*System.out.println("Test3");
        /*System.out.println("Test3");

        /*System.out.println("Test3");
        Subscription subscription3 = Observable.zip(Observable.range(0, 3).subscribeOn(Schedulers.computation()).doOnSubscribe(() -> {
            logThread("subscribing range1");
        }).doOnNext(integer -> {logThread(integer);}), Observable.range(4, 4).subscribeOn(Schedulers.computation()).doOnSubscribe(() -> {
            logThread("subscribing range2");
        }).doOnNext(integer -> {logThread(integer);}), (t1, t2) -> t1 + t2)
                .subscribe(integer -> {
                    logThread(integer);
                }, Throwable::printStackTrace, () -> {
                    logThread("onCompleated");
                });
        System.out.println("End Test3");*/
//        subscription.unsubscribe();

  /*      if (!subscription1.isUnsubscribed()) {
            System.out.println("not unsubscriebed");
            subscription1.unsubscribe();
        }*/
        /*if (!subscription3.isUnsubscribed()) {
            System.out.println("not unsubscriebed");
            subscription3.unsubscribe();
        }*/

    }
}
