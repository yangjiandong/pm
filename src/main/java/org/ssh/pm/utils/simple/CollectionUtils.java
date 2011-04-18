package org.ssh.pm.utils.simple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class CollectionUtils {

	public static Collection<?> list(final Enumeration<?> e) {
		final ArrayList<Object> l = new ArrayList<Object>();
		if (e != null) {
			while (e.hasMoreElements()) {
				final Object object = e.nextElement();
				if (object != null) {
					l.add(object);
				}
			}
		}
		return l;
	}

	public static Enumeration<?> enumeration(final Collection<?> c) {
		return Collections.enumeration(c);
	}
}
