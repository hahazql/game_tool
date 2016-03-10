package test;

import com.hahazql.tools.auto.config.ClazzConfig;
import com.hahazql.tools.auto.config.FieldConfig;

/**
 * Created by zql on 2015/9/29.
 */
@ClazzConfig(value = "TestConfig_2", configSrcName = "TestConfig")
public class TestConfig_2 {

    @FieldConfig(value = "任务ID", desc = "one field")
    public int nn;
    @FieldConfig(value = "奖励值", desc = "one test field cc")
    public int cc;

    public void initialize() {

        nn = 1;
        cc = 2;
    }

    public int getNn() {
        return nn;
    }

    public void setNn(int nn) {
        this.nn = nn;
    }

    public int getCc() {
        return cc;
    }

    public void setCc(int cc) {
        this.cc = cc;
    }

    @Override
    public String toString() {
        String ret = "nn = " + getNn() + " cc =  " + getCc();
        return ret;
    }
}
