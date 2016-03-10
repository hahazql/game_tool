package com.hahazql.tools.format;

import com.hahazql.tools.exception.BaseException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by zql on 2015/10/12.
 */
public class SerializeUtil {
    public static byte[] serialize(Object object) throws BaseException {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            //序列化
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
            throw (new BaseException("序列化失败！", e.getCause()));
        }
    }

    public static Object unserialize(byte[] bytes) throws BaseException {
        ByteArrayInputStream bais = null;
        try {
            //反序列化
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            throw (new BaseException("反序列化失败！", e.getCause()));
        }
    }
}
