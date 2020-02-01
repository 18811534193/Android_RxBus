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
