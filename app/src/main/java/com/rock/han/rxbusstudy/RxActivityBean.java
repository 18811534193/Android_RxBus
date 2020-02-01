package com.rock.han.rxbusstudy;

/**
 * Created by Administrator on 2019/9/25.
 */

public class RxActivityBean
{
    public RxActivityBean(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;
}
