package org.ssh.pm.utils.simple;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class NumberUtils {
	private static Map<String, DecimalFormat> decimalFormats;

	public static String formatDouble(final Number number) {
		return format("#.##", number);
	}

	public static String format(final String pattern, final Number number) {
		if (number == null) {
			return "0";
		}
		if (!StringUtils.hasText(pattern)) {
			return number.toString();
		}
		if (decimalFormats == null) {
			decimalFormats = new ConcurrentHashMap<String, DecimalFormat>();
		}
		DecimalFormat formatter = decimalFormats.get(pattern);
		if (formatter == null) {
			decimalFormats.put(pattern, formatter = new DecimalFormat(pattern));
		}
		return formatter.format(number);
	}
}
