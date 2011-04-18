package org.ssh.pm.utils.simple;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class LinkedCaseInsensitiveMap<V> extends
		org.springframework.util.LinkedCaseInsensitiveMap<V> {
	public LinkedCaseInsensitiveMap() {
	}

	public LinkedCaseInsensitiveMap(final int initialCapacity) {
		super(initialCapacity);
	}

	private static final long serialVersionUID = -7467743735579931453L;
}
