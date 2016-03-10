package test;


import com.hahazql.tools.auto.config.ClazzConfig;
import com.hahazql.tools.auto.config.FieldConfig;

/**
 * Created by zql on 2015/9/29.
 */
@ClazzConfig(value = "TestConfig", configDesc = "用于测试的Config", configSrcName = "用于测试的配置文件")
public class TestConfig {
    @FieldConfig(value = "ID", desc = "test A")
    public int a;
    @FieldConfig(value = "数量", desc = "test B")
    public int b;
    @FieldConfig(value = "描述", desc = "test C")
    public String c;
    @FieldConfig(value = "浮点数", desc = "test D")
    public double d;
    @FieldConfig(value = "长长的数", desc = "test E")
    public long e;

    @FieldConfig(value = "任务配置", desc = "test Array", elementClazz = TestConfig_2.class)
    public TestConfig_2[] list;


    public void initialize() {
        a = 0;
        b = 1;
        c = "aaa";
        d = 0.1;
        e = 5L;
        list = new TestConfig_2[]{new TestConfig_2(), new TestConfig_2()};
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
    }

    public long getE() {
        return e;
    }

    public void setE(long e) {
        this.e = e;
    }

    public TestConfig_2[] getList() {
        return list;
    }

    public void setList(TestConfig_2[] list) {
        this.list = list;
    }

    @Override
    public String toString() {
        String ret = "a = " + getA() + "  b = " + getB() + "  c = " + getC() + "  d = " + getD() + "  e = " + getE()
                + " list = " + getList();
        return ret;
    }
}
