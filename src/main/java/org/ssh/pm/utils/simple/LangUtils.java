package org.ssh.pm.utils.simple;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class LangUtils {

	public static boolean objectEquals(final Object newVal, final Object oldVal) {
		if (newVal == oldVal) {
			return true;
		} else if ((newVal == null) || (oldVal == null)) {
			return false;
		} else {
			if (newVal.getClass().isArray() && oldVal.getClass().isArray()) {
				final int nLength = Array.getLength(newVal);
				final int oLength = Array.getLength(oldVal);
				if (nLength != oLength) {
					return false;
				}
				for (int i = 0; i < nLength; i++) {
					if (!objectEquals(Array.get(newVal, i), Array.get(oldVal, i))) {
						return false;
					}
				}
				return true;
			} else {
				return newVal.equals(oldVal);
			}
		}
	}

	public static Object[] removeDuplicatesAndNulls(final Object[] array) {
		if (array == null) {
			return null;
		}
		final LinkedHashSet<Object> ht = new LinkedHashSet<Object>();
		for (final Object element : array) {
			if (element == null) {
				continue;
			}
			ht.add(element);
		}

		final Object[] ret = (Object[]) Array.newInstance(array.getClass().getComponentType(),
				ht.size());
		int j = 0;

		final Iterator<?> it = ht.iterator();
		while (it.hasNext()) {
			ret[j++] = it.next();
		}
		return ret;
	}
}
