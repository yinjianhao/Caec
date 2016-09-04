package com.me.caec.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 流转化
 * <p>
 * Created by yin on 2016/9/2.
 */
public class StreamUtils {

    /**
     * 输入流转字符串(数据太长时解析不完? why?)
     *
     * @param is 输入流
     * @return 转换后的字符串
     * @throws IOException
     */
    public static String stream2String(InputStream is) throws IOException {
        byte[] b = new byte[1024];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int len;

        if ((len = is.read(b)) != -1) {
            byteArrayOutputStream.write(b, 0, len);
        }

        is.close();
        byteArrayOutputStream.close();

        return byteArrayOutputStream.toString();
    }

    public static String stream2String2(InputStream is) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        if ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        is.close();
        return stringBuilder.toString();
    }
}
