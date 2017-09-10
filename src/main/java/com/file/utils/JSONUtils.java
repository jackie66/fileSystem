package com.file.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * json的工具类
 *
 * @author wenteng
 */
public class JSONUtils {
    public static ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 支持将Date数据按照特定的格式转换为String
        JSONUtils.objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 操作成功的json接口，
     * status 采用标准的 httpStatus 状态码
     *
     * @return
     */
    public static String operateSuccess(Object value) {
        return operator("200", "操作成功", value);
    }

    /**
     * 操作失败的json接口
     * status 采用标准的 httpStatus 状态码
     *
     * @return
     */
    public static String operateError(String message) {
        return operator("500", message, null);
    }

    private static String operator(String status, String message, Object value) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("status", status);
        jsonMap.put("message", message);
        jsonMap.put("data", value);
        return toJson(jsonMap);
    }

    /**
     * 将对象转化为json的静态方法
     *
     * @param value json数据的value，并没有提供json的key
     * @return json数据
     */
    private static String toJson(Object value) {
        try {
            return JSONUtils.objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            e.printStackTrace();
            return "\"status\" : \"500\", \"message\" : \"json解析异常\"";
		}
	}
}
