package org.ssh.pm.utils.simple;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Properties;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class StringUtils {
	private static final String defaultDelimiter = ";";

	public static String[] tokenizeToStringArray(final String str) {
		return tokenizeToStringArray(str, defaultDelimiter);
	}

	public static String[] tokenizeToStringArray(final String str, final String delimiters,
			final boolean ignoreEmptyTokens) {
		return org.springframework.util.StringUtils.tokenizeToStringArray(str, delimiters, true,
				ignoreEmptyTokens);
	}

	public static String[] tokenizeToStringArray(final String str, final String delimiters) {
		return org.springframework.util.StringUtils.tokenizeToStringArray(str, delimiters);
	}

	public static String join(final Collection<?> c, final String delim) {
		return org.springframework.util.StringUtils.collectionToDelimitedString(c, delim);
	}

	public static String join(final Collection<?> c) {
		return join(c, defaultDelimiter);
	}

	public static String join(final Object[] arr, final String delim) {
		return org.springframework.util.StringUtils.arrayToDelimitedString(arr, delim);
	}

	public static String join(final Object[] arr) {
		return join(arr, defaultDelimiter);
	}

	public static String replace(final String inString, final String oldPattern,
			final String newPattern) {
		return org.springframework.util.StringUtils.replace(inString, oldPattern, newPattern);
	}

	public static boolean hasText(final String string) {
		return org.springframework.util.StringUtils.hasText(string);
	}

	public static boolean hasTextObject(final Object object) {
		if (object == null) {
			return false;
		}
		if (object instanceof String) {
			return hasText((String) object);
		}
		return true;
	}

	public static String text(final String... strings) {
		if (strings != null) {
			for (final String string : strings) {
				if (hasText(string)) {
					return string;
				}
			}
		}
		return IConstants.BLANK;
	}

	public static String blank(final Object object) {
		return object == null ? IConstants.BLANK : ConvertUtils.toString(object);
	}

	public static String encodeHex(final byte[] b) {
		return new String(Hex.encodeHex(b));
	}

	public static byte[] decodeHex(final String s) {
		try {
			return Hex.decodeHex(s.toCharArray());
		} catch (final DecoderException e) {
			return IConstants.NULL_BYTE_ARRAY;
		}
	}

	public static String decodeHexString(final String s) {
		return new String(decodeHex(s));
	}

	public static String hash(final Object object) {
		final int hash = object.hashCode();
		return hash > 0 ? String.valueOf(hash) : "0" + Math.abs(hash);
	}

	public static String getFilename(final String path) {
		return org.springframework.util.StringUtils.getFilename(path);
	}

	public static String getFilenameExtension(final String path) {
		return org.springframework.util.StringUtils.getFilenameExtension(path);
	}

	public static String stripFilenameExtension(final String path) {
		return org.springframework.util.StringUtils.stripFilenameExtension(path);
	}

	private static PropertiesPersister persister = new DefaultPropertiesPersister();

	public static String propertiesToString(final Properties properties) {
		return propertiesToString(properties, null);
	}

	public static String propertiesToString(final Properties properties, final String header) {
		if (properties != null) {
			try {
				final StringWriter writer = new StringWriter();
				persister.store(properties, writer, header);
				return writer.toString();
			} catch (final IOException e) {
			}
		}
		return null;
	}

	public static Properties stringToProperties(final String str) {
		Properties props = null;
		if (hasText(str)) {
			try {
				persister.load(props = new Properties(), new StringReader(str));
			} catch (final IOException e) {
			}
		}
		return props;
	}

	public static String getLimitString(final String str, int len, final String symbol,
			final String encoding) throws UnsupportedEncodingException {
		if ((str == null) || "".equals(str)) {
			return str;
		}
		int counterOfDoubleByte;
		byte b[];
		counterOfDoubleByte = 0;
		b = str.getBytes(encoding);
		if (len <= 0) {
			len = 400;
		}
		if (b.length <= len) {
			return str;
		}
		for (int i = 0; i < len; i++) {
			if (b[i] < 0) {
				counterOfDoubleByte++;
			}
		}

		if (counterOfDoubleByte % 2 == 0) {
			return new String(b, 0, len, encoding) + symbol;
		} else {
			return new String(b, 0, len - 1, encoding) + symbol;
		}
	}

	public static String trimAllWhitespace(final String str) {
		return org.springframework.util.StringUtils.trimAllWhitespace(str);
	}

	public static String trimLeadingCharacter(final String str, final char leadingCharacter) {
		return org.springframework.util.StringUtils.trimLeadingCharacter(str, leadingCharacter);
	}
}
