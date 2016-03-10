package com.hahazql.tools.guid;/**
 * Created by zql on 16/1/20.
 * <p/>
 * Created by zql on 16/1/20.
 *
 * @className UUIDGen
 * @classUse Created by zql on 16/1/20.
 * @className UUIDGen
 * @classUse
 */

/**
 * Created by zql on 16/1/20.
 * @className UUIDGen
 * @classUse
 *
 *
 */


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;


public final class UUIDGen {
    private static long lastTime = -9223372036854775808L;
    private static String macAddress = null;
    private static long clockSeqAndNode = -9223372036854775808L;

    private UUIDGen() {
    }

    public static long getClockSeqAndNode() {
        return clockSeqAndNode;
    }

    public static long newTime() {
        return createTime(System.currentTimeMillis());
    }

    public static synchronized long createTime(long currentTimeMillis) {
        long timeMillis = currentTimeMillis * 10000L + 122192928000000000L;
        if (timeMillis > lastTime) {
            lastTime = timeMillis;
        } else {
            timeMillis = ++lastTime;
        }

        long time = timeMillis << 32;
        time |= (timeMillis & 281470681743360L) >> 16;
        time |= 4096L | timeMillis >> 48 & 4095L;
        return time;
    }

    public static String getMACAddress() {
        return macAddress;
    }

    static String getFirstLineOfCommand(String... commands) throws IOException {
        Process p = null;
        BufferedReader reader = null;

        String var3;
        try {
            p = Runtime.getRuntime().exec(commands);
            reader = new BufferedReader(new InputStreamReader(p.getInputStream()), 128);
            var3 = reader.readLine();
        } finally {
            if (p != null) {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException var16) {
                        ;
                    }
                }

                try {
                    p.getErrorStream().close();
                } catch (IOException var15) {
                    ;
                }

                try {
                    p.getOutputStream().close();
                } catch (IOException var14) {
                    ;
                }

                p.destroy();
            }

        }

        return var3;
    }

    static {
        try {
            Class.forName("java.net.InterfaceAddress");
            macAddress = Class.forName("com.hahazql.util.guid.UUIDGen$HardwareAddressLookup").newInstance().toString();
        } catch (ExceptionInInitializerError var32) {
            ;
        } catch (ClassNotFoundException var33) {
            ;
        } catch (LinkageError var34) {
            ;
        } catch (IllegalAccessException var35) {
            ;
        } catch (InstantiationException var36) {
            ;
        } catch (SecurityException var37) {
            ;
        }

        if (macAddress == null) {
            Process ex = null;
            BufferedReader in = null;

            try {
                String ex1 = System.getProperty("os.name", "");
                String l;
                if (ex1.startsWith("Windows")) {
                    ex = Runtime.getRuntime().exec(new String[]{"ipconfig", "/all"}, (String[]) null);
                } else if (!ex1.startsWith("Solaris") && !ex1.startsWith("SunOS")) {
                    if ((new File("/usr/sbin/lanscan")).exists()) {
                        ex = Runtime.getRuntime().exec(new String[]{"/usr/sbin/lanscan"}, (String[]) null);
                    } else if ((new File("/sbin/ifconfig")).exists()) {
                        ex = Runtime.getRuntime().exec(new String[]{"/sbin/ifconfig", "-a"}, (String[]) null);
                    }
                } else {
                    l = getFirstLineOfCommand(new String[]{"uname", "-n"});
                    if (l != null) {
                        ex = Runtime.getRuntime().exec(new String[]{"/usr/sbin/arp", l}, (String[]) null);
                    }
                }

                if (ex != null) {
                    in = new BufferedReader(new InputStreamReader(ex.getInputStream()), 128);
                    l = null;

                    while ((l = in.readLine()) != null) {
                        macAddress = MACAddressParser.parse(l);
                        if (macAddress != null && Hex.parseShort(macAddress) != 255) {
                            break;
                        }
                    }
                }
            } catch (SecurityException var38) {
                ;
            } catch (IOException var39) {
                ;
            } finally {
                if (ex != null) {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException var30) {
                            ;
                        }
                    }

                    try {
                        ex.getErrorStream().close();
                    } catch (IOException var29) {
                        ;
                    }

                    try {
                        ex.getOutputStream().close();
                    } catch (IOException var28) {
                        ;
                    }

                    ex.destroy();
                }

            }
        }

        if (macAddress != null) {
            clockSeqAndNode |= Hex.parseLong(macAddress);
        } else {
            try {
                byte[] ex2 = InetAddress.getLocalHost().getAddress();
                clockSeqAndNode |= (long) (ex2[0] << 24) & 4278190080L;
                clockSeqAndNode |= (long) (ex2[1] << 16 & 16711680);
                clockSeqAndNode |= (long) (ex2[2] << 8 & '\uff00');
                clockSeqAndNode |= (long) (ex2[3] & 255);
            } catch (UnknownHostException var31) {
                clockSeqAndNode |= (long) (Math.random() * 2.147483647E9D);
            }
        }

        clockSeqAndNode |= (long) (Math.random() * 16383.0D) << 48;
    }

    static class HardwareAddressLookup {
        HardwareAddressLookup() {
        }

        public String toString() {
            String out = null;

            try {
                Enumeration ex = NetworkInterface.getNetworkInterfaces();
                if (ex != null) {
                    while (ex.hasMoreElements()) {
                        NetworkInterface iface = (NetworkInterface) ex.nextElement();
                        byte[] hardware = iface.getHardwareAddress();
                        if (hardware != null && hardware.length == 6 && hardware[1] != -1) {
                            out = Hex.append(new StringBuilder(36), hardware).toString();
                            break;
                        }
                    }
                }
            } catch (SocketException var5) {
                ;
            }

            return out;
        }
    }

}
