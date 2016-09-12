package com.me.caec.globle;

import com.alibaba.fastjson.JSON;

import org.xutils.common.util.ParameterizedTypeUtil;
import org.xutils.http.app.ResponseParser;
import org.xutils.http.request.UriRequest;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.List;

/**
 * xutils用于解析http请求返回结果
 * string to javabean
 * 用法: 在对应bean的class上添加注解  @HttpResponse(parser = JsonResponseParser.class)
 *
 * @auther yjh
 * @date 2016/9/12
 */

public class JsonResponseParser implements ResponseParser {
    @Override
    public void checkResponse(UriRequest request) throws Throwable {

    }

    @Override
    public Object parse(Type resultType, Class<?> resultClass, String result) throws Throwable {
        if (resultClass == List.class) {
            return JSON.parseArray(result, (Class<?>) ParameterizedTypeUtil.getParameterizedType(resultType, List.class, 0));
        } else {
            return JSON.parseObject(result, resultClass);
        }
    }
}
