package com.hahazql.tools.random;/**
 * Created by zql on 15/6/24.
 */

import com.hahazql.tools.random.name.china.RandChinaName;

import java.util.List;
import java.util.Random;

/**
 * Created by zql on 15/6/24.
 *
 * @className RandHelper
 * @classUse 此类提供一系列产生随机数的方法，以满足不同用例需要
 */
public class RandHelper {

    private static final byte[] bytes = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'q', 'w', 'e', 'r', 't',
            'y', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n', 'm',
            'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Z', 'X',
            'C', 'V', 'B', 'N', 'M'};
    private static final long multiplier = 0x5DEECE66DL;
    private static final long addend = 0xBL;
    private static final long mask = (1L << 48) - 1;
    private static final long integerMask = (1L << 33) - 1;
    private static final long seedUniquifier = 8682522807148012L;


    //随机数对象
    private static Random random;
    //用于产生随机数的种子
    private static long seed;

    // 静态初始化区域
    static {
        //产生随机数种子
        long s = seedUniquifier + System.nanoTime();
        s = (s ^ multiplier) & mask;
        seed = s;
        random = new Random(seed);
    }


    private RandHelper() {
    }

    /***********************************************************
     * 产生基本的随机数
     ***********************************************************/

    /**
     * 获取此类实例的伪随机种子生成器
     */
    public static void setSeed(long s) {
        seed = s;
        random = new Random(seed);
    }

    /**
     * 获取此类实例提供的伪随机种子生成器
     */
    public static long getSeed() {
        return seed;
    }

    /**
     * 返回一个随机的范围在[0,1)之间的double类型的数
     */
    private static double uniform() {
        return random.nextDouble();
    }


    /**
     * 返回一个随机的范围在[0,N)之间的int类型的数
     */
    private static int uniform(int N) {
        return random.nextInt(N);
    }


    /**
     * 返回一个范围在 [0, 1)的实数
     */
    @SuppressWarnings("unused")
    private static double random() {
        return uniform();
    }

    /**
     * 返回一个范围在 [a, b)的int类型值
     */
    private static int uniform(int a, int b) {
        return a + uniform(b - a);
    }

    /**
     * 返回一个范围在 [a, b)的实数
     */
    private static double uniform(double a, double b) {
        return a + uniform() * (b - a);
    }

    /**
     * 返回一个随机boolean值,该p表示此布尔值为真的概率
     *
     * @param p 0~1 之间的double值,表示产生boolean真值的可能性
     */
    private static boolean bernoulli(double p) {
        return uniform() < p;
    }

    /**
     * 返回一个随机boolean值,此布尔值为真的概率为0.5
     */
    private static boolean bernoulli() {
        return bernoulli(0.5);
    }


    /***********************************************************
     * 产生满足特定概率分布的实数
     ***********************************************************/


    /**
     * 返回一个满足标准正态分布的实数
     */
    public static double gaussian() {
        double r, x, y;
        do {
            x = uniform(-1.0, 1.0);
            y = uniform(-1.0, 1.0);
            r = x * x + y * y;
        } while (r >= 1 || r == 0);
        return x * Math.sqrt(-2 * Math.log(r) / r);

    }

    /**
     * 返回一个满足平均值为mean,标准差为stddev的正态分布的实数
     *
     * @param mean   正态分布的平均值
     * @param stddev 正太分布的标准差
     */
    public static double gaussian(double mean, double stddev) {
        return mean + stddev * gaussian();
    }

    /**
     * 返回一个满足几何分布的整型值 平均值为1/p
     */
    public static int geometric(double p) {
        // Knuth
        return (int) Math.ceil(Math.log(uniform()) / Math.log(1.0 - p));
    }

    /**
     * 根据指定的参数返回一个满足泊松分布的实数
     */
    public static int poisson(double lambda) {
        // 使用 Knuth 的算法
        // 参见 http://en.wikipedia.org/wiki/Poisson_distribution
        int k = 0;
        double p = 1.0;
        double L = Math.exp(-lambda);
        do {
            k++;
            p *= uniform();
        } while (p >= L);
        return k - 1;
    }

    /**
     * 根据指定的参数按返回一个满足帕雷托分布的实数
     */
    public static double pareto(double alpha) {
        return Math.pow(1 - uniform(), -1.0 / alpha) - 1.0;
    }

    /**
     * 返回一个满足柯西分布的实数
     */
    public static double cauchy() {
        return Math.tan(Math.PI * (uniform() - 0.5));
    }

    /**
     * 返回一个满足离散分布的int类型的数
     *
     * @param a 算法产生随机数过程中需要使用此数组的数据，a[i]代表i出现的概率
     *          前提条件 a[i] 非负切和接近 1.0
     */
    public static int discrete(double[] a) {
        double EPSILON = 1E-14;
        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            if (a[i] < 0.0) throw new IllegalArgumentException("数组元素 " + i + " 为负数: " + a[i]);
            sum = sum + a[i];
        }
        if (sum > 1.0 + EPSILON || sum < 1.0 - EPSILON)
            throw new IllegalArgumentException("数组各个元素之和为: " + sum);

        while (true) {
            double r = uniform();
            sum = 0.0;
            for (int i = 0; i < a.length; i++) {
                sum = sum + a[i];
                if (sum > r) return i;
            }
        }
    }

    /**
     * 返回一个满足指数分布的实数，该指数分布比率为lambda
     */
    public static double exp(double lambda) {
        return -Math.log(1 - uniform()) / lambda;
    }

    /***********************************************************
     * 数组操作
     ***********************************************************/


    /**
     * 随机打乱List
     *
     * @param a 待打乱的List
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void shuffle(List a) {
        int N = a.size();
        for (int i = 0; i < N; i++) {
            int r = i + uniform(N - i);
            Object temp = a.get(i);
            a.set(i, a.get(r));
            a.set(r, temp);
        }
    }

    /**
     * 随机打乱指定的Object型数组
     *
     * @param a 待打乱的Object型数组
     */
    public static void shuffle(Object[] a) {
        int N = a.length;
        for (int i = 0; i < N; i++) {
            int r = i + uniform(N - i);
            Object temp = a[i];
            a[i] = a[r];
            a[r] = temp;
        }
    }

    /**
     * 随机打乱指定的double型数组
     *
     * @param a 待打乱的double型数组
     */
    public static void shuffle(double[] a) {
        int N = a.length;
        for (int i = 0; i < N; i++) {
            int r = i + uniform(N - i);
            double temp = a[i];
            a[i] = a[r];
            a[r] = temp;
        }
    }

    /**
     * 随机打乱指定的int型数组
     *
     * @param a 待打乱的int型数组
     */
    public static void shuffle(int[] a) {
        int N = a.length;
        for (int i = 0; i < N; i++) {
            int r = i + uniform(N - i);
            int temp = a[i];
            a[i] = a[r];
            a[r] = temp;
        }
    }


    /**
     * 随机打乱指定Object类型数组中指定范围的数据
     *
     * @param a  指定的数组
     * @param lo 起始位置
     * @param hi 结束位置
     */
    public static void shuffle(Object[] a, int lo, int hi) {
        if (lo < 0 || lo > hi || hi >= a.length) {
            throw new IndexOutOfBoundsException("不合法的边界");
        }
        for (int i = lo; i <= hi; i++) {
            int r = i + uniform(hi - i + 1);
            Object temp = a[i];
            a[i] = a[r];
            a[r] = temp;
        }
    }

    /**
     * 随机打乱指定double类型数组中指定范围的数据
     *
     * @param a  指定的数组
     * @param lo 起始位置
     * @param hi 结束位置
     */
    public static void shuffle(double[] a, int lo, int hi) {
        if (lo < 0 || lo > hi || hi >= a.length) {
            throw new IndexOutOfBoundsException("不合法的边界");
        }
        for (int i = lo; i <= hi; i++) {
            int r = i + uniform(hi - i + 1);
            double temp = a[i];
            a[i] = a[r];
            a[r] = temp;
        }
    }

    /**
     * 随机打乱指定int类型数组中指定范围的数据
     *
     * @param a  指定的数组
     * @param lo 起始位置
     * @param hi 结束位置
     */
    public static void shuffle(int[] a, int lo, int hi) {
        if (lo < 0 || lo > hi || hi >= a.length) {
            throw new IndexOutOfBoundsException("不合法的边界");
        }
        for (int i = lo; i <= hi; i++) {
            int r = i + uniform(hi - i + 1);
            int temp = a[i];
            a[i] = a[r];
            a[r] = temp;
        }
    }

    /**
     * 从Object[]的概率数组中抽取指定数量的索引
     *
     * @param rate
     * @param num
     * @return
     */
    private static int getIndex(Object[] rate, int num) {
        int tt = 0;
        for (int n = 0; n < rate.length; n++) {
            int starnum = 0;
            int endnum = 0;
            for (int i = 0; i <= n; i++) {
                if (i != 0)
                    starnum += ((Integer) rate[(i - 1)]).intValue();
                endnum += ((Integer) rate[i]).intValue();
            }
            if ((starnum < num) && (num <= endnum)) {
                tt = n;
                break;
            }
        }
        return tt;
    }


    /**
     * 从概率数组中按概率抽取索引
     *
     * @param rate     概率数组
     * @param limitnum 指定数量
     * @return
     */
    public static int getRate(List<Integer> rate, int limitnum) {
        int tt = 0;
        int num = uniform(1, limitnum);
        tt = getIndex(rate.toArray(), num);
        return tt;
    }

    /**
     * 从几率数组中按几率抽取
     *
     * @param rate 几率数组
     * @return
     * @note 若rate数组总和为1000则是千分率，若为100则是百分率
     */
    public static int getRate(List<Integer> rate) {
        int limitnum = 0;
        for (int n = 0; n < rate.size(); n++)
            limitnum += ((Integer) rate.get(n)).intValue();
        return getRate(rate, limitnum);
    }

    /***********************************************************
     * 常用操作
     ***********************************************************/


    public static int randInt() {
        return random.nextInt();
    }

    /**
     * 获得一个0~max之间的整数
     *
     * @param max
     * @return
     */
    public static int randInt(int max) {
        return uniform(max);
    }

    /**
     * 获得一个min~max之间的整数
     *
     * @param min
     * @param max
     * @return
     */
    public static int randInt(int min, int max) {
        return uniform(min, max);
    }

    /**
     * 获取一个min~max之间的带小数数
     *
     * @param min
     * @param max
     * @return
     */
    public static double randDouble(double min, double max) {
        return uniform(min, max);
    }

    /**
     * 获得一个0~1之间的随机数
     *
     * @return
     */
    public static double randDouble() {
        return uniform();
    }

    /**
     * 根据几率随机一个bool值
     *
     * @param rate 随机到true的几率
     * @return
     */
    public static Boolean randBool(Double rate) {
        return bernoulli(rate);
    }

    /**
     * 随机一个bool值
     *
     * @return
     */
    public static Boolean randBool() {
        return bernoulli();
    }

    public static long randLong() {
        return random.nextLong();
    }


    public static final byte[] randomBytes(int size) {
        byte[] bb = bytes;
        byte[] ab = new byte[size];
        for (int i = 0; i < size; i++) {
            ab[i] = randomByte(bb);
        }
        return ab;
    }

    private static byte randomByte(byte[] b) {
        int ran = (int) ((next() & integerMask) >>> 16);
        return b[ran % b.length];
    }

    private static long next() {
        long oldSeed = seed;
        long nextSeed = 0L;
        do {
            nextSeed = (oldSeed * multiplier + addend) & mask;
        } while (oldSeed == nextSeed);
        seed = nextSeed;
        return nextSeed;
    }


    /***********************************************************
     * 随机姓名
     ***********************************************************/

    public static String randNameChina() {
        return RandChinaName.random();
    }
}
