package org.ssh.pm.utils.simple;

import java.util.UUID;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class GenId {
	static Object lock = new Object();

	static long COUNTER = 0;

	/**
	 * 在同一个虚拟机下产生一个唯一的ID，其格式为[time] - [counter]
	 */
	public static String genUID() {
		final long time = System.currentTimeMillis();
		long id;
		synchronized (GenId.lock) {
			id = GenId.COUNTER++;
		}
		return Long.toString(time, Character.MAX_RADIX) + "-"
				+ Long.toString(id, Character.MAX_RADIX);
	}

	public static String genUUID() {
		return UUID.randomUUID().toString();
	}
}
