package com.wying.pesservice01.rxjava;

import org.junit.Test;
import rx.Notification;
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;

/**
 * description:rxjava demo
 * date: 2022/1/11
 * author: gaom
 * version: 1.0
 */
public class RxjavaDemo {
    public static  void main(String[] args){
        new RxjavaDemo().demo();
    }

    /**
     * 模拟Hyxtrix的 run方法
     * @return
     * @throws Exception
     */
    protected String run() throws Exception {
        System.err.println(" run...并通过Observable.just包装成Observable");
        return "run success!";
    }
    private Observable<String> applyHystrixSemantics() {
        final Action1<String> markEmits = new Action1<String>() {
            @Override
            public void call(String r) {
                System.out.println("③ Observable doOnNext call, target: markEmits");
            }
        };
        final Action1<Notification<? super String>> setRequestContext = new Action1<Notification<? super String>>() {
            @Override
            public void call(Notification<? super String> rNotification) {
                System.out.println("③ Observable doOnEach call ,target:setRequestContext");
            }
        };
        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                System.out.println("③ Observable is call ");
                return Observable.defer(new Func0<Observable<String>>() {
                    @Override
                    public Observable<String> call() {
                        System.out.println("④ Observable is call ");
                        return Observable.defer(new Func0<Observable<String>>() {
                            @Override
                            public Observable<String> call() {
                                try {
                                    System.out.println("⑤ Observable is call ");
                                    return  Observable.just(run());
                                } catch (Throwable ex) {
                                    return Observable.error(ex);
                                }
                            }
                        }).doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                System.out.println("⑤ Observable doOnSubscribe is call 在 ⑤被订阅之前执行，一般用于修改、添加或者删除事件源的数据流");
                            }
                        });
                    }
                });
            }
        }).doOnTerminate(new Action0() {
            @Override
            public void call() {
                System.out.println("③ Observable doOnTerminate  is call ");
            }
        }).doOnNext(markEmits)
                .doOnEach(setRequestContext);
    }

    public void demo(){
        final Func0<Observable<String>> applyHystrixSemantics = new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                System.out.println("② Observable is call");
                return applyHystrixSemantics();
            }
        };
        final Action0 terminateCommandCleanup = new Action0() {

            @Override
            public void call() {
                System.out.println("② Observable doOnTerminate is call target:terminateCommandCleanup");
            }
        };
        final Action0 unsubscribeCommandCleanup = new Action0() {
            @Override
            public void call() {
                System.out.println("② Observable doOnUnsubscribe is call target:unsubscribeCommandCleanup");
            }
        };
        final Action0 fireOnCompletedHook = new Action0() {
            @Override
            public void call() {
                System.out.println("② Observable doOnCompleted  is call target:fireOnCompletedHook");
            }
        };
        System.out.println("demo execute");
        Observable defer= Observable.defer(new Func0<Observable<String>>() {

            @Override
            public Observable<String> call() {
                System.out.println("① Observable call");
                Observable<String> hystrixObservable =Observable.defer(applyHystrixSemantics);
                return hystrixObservable.doOnTerminate(terminateCommandCleanup)     // perform cleanup once (either on normal terminal state (this line), or unsubscribe (next line))
                        .doOnUnsubscribe(unsubscribeCommandCleanup) // perform cleanup once
                        .doOnCompleted(fireOnCompletedHook);
            }
        }).doOnCompleted(new Action0() {
            @Override
            public void call() {
                System.out.println("① Observable doOnCompleted is  call ");
            }
        }).doOnUnsubscribe(new Action0() {
            @Override
            public void call() {
                System.out.println("① Observable doOnUnsubscribe is call");
            }
        });
        //defer.toBlocking();
        //不通过toFuture 不会执行
        defer.toBlocking().toFuture();

    }
}
