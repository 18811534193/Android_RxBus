# Android_RxBus
Rxbus简单使用，适用于初学者

第一个窗体
 <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Rxjava2.0版本的RxBus"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
    <Button
        android:id="@+id/btn_sendself"
        android:text="给自己窗体发送消息"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
    <Button
        android:id="@+id/btn_nextpage"
        android:text="去第二个窗体"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
    <TextView
        android:id="@+id/txt_message"
        android:layout_gravity="center"
        android:layout_marginTop="50dp"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="消息：：："
        />
        
        activity
        package com.rock.han.rxbusstudy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    private CompositeDisposable compositeDisposable;

    private Button btn_sendself;
    private Button btn_nextpage;
    private TextView txt_message;

    private RxBus rxBus = RxBus.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_sendself=(Button)findViewById(R.id.btn_sendself);
        btn_nextpage=(Button)findViewById(R.id.btn_nextpage);
        txt_message=(TextView)findViewById(R.id.txt_message);

        initRxBus();

        btn_sendself.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.getInstance().post(new RxActivityBean("我是使用RxBus向Activity发送的数据"));
            }
        });

        btn_nextpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }

    private void initRxBus()
    {
        compositeDisposable = new CompositeDisposable();

        rxBus.tObservable(RxActivityBean.class)
                .subscribe(new Observer<RxActivityBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Toast.makeText(MainActivity.this,"onSubscribe",Toast.LENGTH_SHORT).show();
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull RxActivityBean rxActivityBean) {
                        Toast.makeText(MainActivity.this,"onNext",Toast.LENGTH_SHORT).show();
                        txt_message.setText(rxActivityBean.getMessage());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(MainActivity.this,"onError",Toast.LENGTH_SHORT).show();
                        txt_message.setText(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(MainActivity.this,"onComplete",Toast.LENGTH_SHORT).show();
                    }
                });

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != compositeDisposable && !compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
        }
    }
}
        
        第二个窗体
        <Button
        android:id="@+id/btn_sendtomain"
        android:text="向第一个窗体发送信息"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
        
        activity
        package com.rock.han.rxbusstudy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Administrator on 2019/9/25.
 */

public class SecondActivity extends AppCompatActivity {

    private Button btn_sendtomain;

   // private CompositeDisposable compositeDisposable;
    private RxBus rxBus = RxBus.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        btn_sendtomain = (Button) findViewById(R.id.btn_sendtomain);
        btn_sendtomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //rxBus.post("second", new String("第二个页面的数据"));
                RxBus.getInstance().post(new RxActivityBean("我是使用RxBus从第二个界面向Activity发送的数据"));
            }
        });

    }




}

Rxbus类
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
