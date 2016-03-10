package com.muyangren.dubbox.bean; /**
 * Created by zql on 15/11/24.
 */

import javax.ws.rs.Path;

/**
 * Created by zql on 15/11/24.
 * @className TestResult
 * @classUse
 *
 *
 */
public class TestResult
{
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "TestResult{" +
                "msg='" + msg + '\'' +
                '}';
    }
}
