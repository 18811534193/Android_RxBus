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
