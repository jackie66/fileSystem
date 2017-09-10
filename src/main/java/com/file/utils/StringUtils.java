package com.file.utils;

import java.util.UUID;

/**
 * @author wenteng
 * @date 2017年9月6日 下午8:01:33
 */

public class StringUtils {

	private static String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
			"o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8",
			"9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
			"U", "V", "W", "X", "Y", "Z" };
	///

	/**
	 * 生成UUID值
	 *
	 * @return 唯一的uuid字符串
	 */
	public static String generateUUID() {
		String id = UUID.randomUUID().toString();
		id = id.substring(0, 8) + id.substring(9, 13) + id.substring(14, 18) + id.substring(19, 23) + id.substring(24);
		return id;
	}

	/**
	 * 判断str是否是空的 如果str是null，"" 都算是空的，返回true，否则返回false
	 *
	 * @param str
	 *            判断是否为空的字符
	 * @return true 是空字符，false 非空字符
	 */
	public static boolean isEmpty(String str) {
		if (str == null || str.equals("") || str.equals("null")) {
			return true;
		}
		return false;
	}

	/**
	 * 至少一个字符是空的
	 *
	 * @param strs
	 *            判断的字符集
	 * @return true 至少有一个为空， false 所有都是非空的
	 */
	public static boolean isEmptyOr(String... strs) {
		if (strs == null || strs.length == 0) {
			return false;
		}
		for (String str : strs) {
			if (isEmpty(str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 至少有一个字符是非空的
	 *
	 * @param strs
	 *            字符集
	 * @return true 至少一个是非空的， false 都是空的
	 */
	public static boolean isNotEmptyOr(String... strs) {
		if (strs == null || strs.length == 0) {
			return false;
		}
		for (String str : strs) {
			if (!isEmpty(str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 所有字符都是非空的
	 *
	 * @param strs
	 *            判断的字符集
	 * @return true 所有都是空，false 至少有一个不为空
	 */
	public static boolean isEmptyAnd(String... strs) {
		if (strs == null)
			return false;
		for (String str : strs) {
			if (isEmpty(str))
				return false;
		}
		return true;
	}

	/**
	 * 将第一个字符转成大写的
	 *
	 * @param str
	 *            需转成的字符
	 * @return 头字母大写的字符
	 */
	public static String toUpperFirstCase(String str) {
		if (StringUtils.isEmpty(str))
			return null;
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	/**
	 * 将第一个字符转成小写的
	 *
	 * @param str
	 *            需转化的字符
	 * @return 头字母小写的字符
	 */
	public static String toLowerFirstCase(String str) {
		if (StringUtils.isEmpty(str))
			return null;
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}

	public static String getGetMethod(String fieldName) {
		return "get" + toUpperFirstCase(fieldName);
	}

	public static String getSetMethod(String fieldName) {
		return "set" + toUpperFirstCase(fieldName);
	}

	public static String getFieldName(String methodName) {
		if (!methodName.startsWith("get") && !methodName.startsWith("set")) {
			return null;
		}
		return toLowerFirstCase(methodName.substring(3));
	}

	public static String generate8Uuid() {
		StringBuilder shortBuffer = new StringBuilder();
		String uuid = UUID.randomUUID().toString().replace("-", "");
		for (int i = 0; i < 8; i++) {
			String str = uuid.substring(i * 4, i * 4 + 4);
			int x = Integer.parseInt(str, 16);
			shortBuffer.append(chars[x % 0x3E]);
		}
		return shortBuffer.toString();
	}
}
