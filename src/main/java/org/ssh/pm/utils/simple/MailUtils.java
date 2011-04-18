package org.ssh.pm.utils.simple;

import java.io.UnsupportedEncodingException;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class MailUtils {

	public static InternetAddress[] getInternetAddresses(final String addresses)
			throws UnsupportedEncodingException, AddressException {
		if (addresses == null) {
			return new InternetAddress[0];
		}
		final String[] arr = StringUtils.tokenizeToStringArray(addresses);
		final InternetAddress[] ret = new InternetAddress[arr.length];
		for (int i = 0; i < arr.length; i++) {
			final String text = arr[i].replaceAll("\"", "").replaceAll(" ", "");
			String personal = null;
			String address = null;
			final int j = text.indexOf("<");
			if (j > 0) {
				personal = text.substring(0, j);
				address = text.substring(j + 1, text.indexOf(">"));
			} else {
				address = text;
			}
			ret[i] = new InternetAddress(address, true);
			if (personal != null) {
				ret[i].setPersonal(personal);
			}
		}
		return ret;
	}
}
