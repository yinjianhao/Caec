package com.me.caec.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 流转化
 * <p>
 * Created by yin on 2016/9/2.
 */
public class StreamUtils {

    public static String stream2String(InputStream is) throws IOException {
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
