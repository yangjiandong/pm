package org.ssh.pm.utils.simple;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.beanutils.converters.DateConverter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class ConvertUtils {
	static {
		final DateConverter dateConverter = new DateConverter();
		dateConverter.setPatterns(new String[] { "yyyy-MM-dd", "yyyy-MM-dd HH:mm" });
		org.apache.commons.beanutils.ConvertUtils.register(dateConverter, Date.class);
	}

	public static Object convert(final Object value, final Class<?> clazz) {
		return org.apache.commons.beanutils.ConvertUtils.convert(value, clazz);
	}

	public static final boolean toBoolean(final Object value, final boolean defaultValue) {
		final Boolean v = toBoolean(value);
		return v == null ? defaultValue : v.booleanValue();
	}

	public static final Boolean toBoolean(final Object obj) {
		if (obj == null) {
			return null;
		} else if (obj instanceof Boolean) {
			return (Boolean) obj;
		} else if (obj instanceof Number) {
			return ((Number) obj).intValue() == 0 ? Boolean.FALSE : Boolean.TRUE;
		} else if (obj instanceof String) {
			final String s = (String) obj;
			if (s.equalsIgnoreCase("true")) {
				return Boolean.TRUE;
			} else if (s.equalsIgnoreCase("false")) {
				return Boolean.FALSE;
			} else {
				try {
					return new Boolean(Integer.parseInt((String) obj) != 0);
				} catch (final Throwable t) {
					return Boolean.FALSE;
				}
			}
		}
		return null;
	}

	public static final Byte toByte(final Object obj) {
		if (obj == null) {
			return null;
		} else if (obj instanceof Number) {
			return new Byte(((Number) obj).byteValue());
		} else if (obj instanceof Boolean) {
			return obj.equals(Boolean.FALSE) ? new Byte((byte) 0) : new Byte((byte) -1);
		} else {
			try {
				return Byte.valueOf(obj.toString());
			} catch (final Throwable t) {
			}
		}
		return null;
	}

	public static final byte toByte(final Object obj, final byte defaultValue) {
		final Byte s = toByte(obj);
		return s == null ? defaultValue : s.byteValue();
	}

	public static final Short toShort(final Object obj) {
		if (obj == null) {
			return null;
		} else if (obj instanceof Number) {
			return new Short(((Number) obj).shortValue());
		} else if (obj instanceof Boolean) {
			return obj.equals(Boolean.FALSE) ? new Short((short) 0) : new Short((short) -1);
		} else {
			try {
				return Short.valueOf(obj.toString());
			} catch (final Throwable t) {
			}
		}
		return null;
	}

	public static final short toShort(final Object obj, final short defaultValue) {
		final Short s = toShort(obj);
		return s == null ? defaultValue : s.shortValue();
	}

	public static final Integer toInt(final Object obj) {
		if (obj == null) {
			return null;
		} else if (obj instanceof Number) {
			return new Integer(((Number) obj).intValue());
		} else if (obj instanceof Boolean) {
			return obj.equals(Boolean.FALSE) ? new Integer(0) : new Integer(-1);
		} else {
			try {
				return Integer.valueOf(obj.toString());
			} catch (final Throwable t) {
			}
		}
		return null;
	}

	public static final int toInt(final Object obj, final int defaultValue) {
		final Integer i = toInt(obj);
		return i == null ? defaultValue : i.intValue();
	}

	public static final Long toLong(final Object obj) {
		if (obj == null) {
			return null;
		} else if (obj instanceof Long) {
			return (Long) obj;
		} else if (obj instanceof Number) {
			return new Long(((Number) obj).longValue());
		} else if (obj instanceof Date) {
			return new Long(((Date) obj).getTime());
		} else if (obj instanceof java.sql.Timestamp) {
			return new Long(((java.sql.Timestamp) obj).getTime());
		} else {
			try {
				return new Long(Long.parseLong(obj.toString()));
			} catch (final Throwable t) {
			}
		}
		return null;
	}

	public static final long toLong(final Object obj, final long defaultValue) {
		final Long l = toLong(obj);
		return l == null ? defaultValue : l.longValue();
	}

	public static final Double toDouble(final Object obj) {
		if (obj == null) {
			return null;
		} else if (obj instanceof Number) {
			return new Double(((Number) obj).doubleValue());
		} else if (obj instanceof Boolean) {
			return obj.equals(Boolean.FALSE) ? new Double(0.0) : new Double(-1.0);
		} else {
			try {
				return Double.valueOf(obj.toString());
			} catch (final Throwable t) {
			}
		}
		return null;
	}

	public static final double toDouble(final Object obj, final double defaultValue) {
		final Double d = toDouble(obj);
		return d == null ? defaultValue : d.doubleValue();
	}

	public static final String toString(final Object obj) {
		if (obj == null) {
			return null;
		} else if (obj instanceof String) {
			return (String) obj;
		} else if (obj instanceof Throwable) {
			final StringWriter writer = new StringWriter();
			((Throwable) obj).printStackTrace(new PrintWriter(writer));
			return writer.toString();
		} else if (obj instanceof char[]) {
			return String.valueOf((char[]) obj);
		} else {
			return String.valueOf(obj);
		}
	}

	public static final String toString(final Object obj, final String defaultValue) {
		final String s = toString(obj);
		return s == null ? defaultValue : s;
	}

	public static String defaultDatePattern = "yyyy-MM-dd HH:mm";

	public static final String toDateString(final Date date, final String pattern) {
		final SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return date == null ? null : sdf.format(date);
	}

	public static final String toDateString(final Date date) {
		return toDateString(date, defaultDatePattern);
	}

	public static final Date toDate(final String dateString, final String pattern) {
		final SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			return sdf.parse(dateString);
		} catch (final Exception e) {
			return null;
		}
	}

	public static <T extends Enum<T>> T toEnum(final Class<T> enumClazz, final Object obj) {
		try {
			final int index = toInt(obj, -1);
			if (index > -1) {
				final T[] arr = enumClazz.getEnumConstants();
				return arr[index];
			} else {
				return Enum.valueOf(enumClazz, toString(obj));
			}
		} catch (final Throwable e) {
			return enumClazz.getEnumConstants()[0];
		}
	}
}
