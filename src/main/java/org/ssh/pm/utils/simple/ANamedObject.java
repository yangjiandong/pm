package org.ssh.pm.utils.simple;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public abstract class ANamedObject extends ADescriptionObject {
	private String name;

	protected ANamedObject(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public boolean equals(final Object obj) {
		if (name != null && obj instanceof ANamedObject) {
			return name.equals(((ANamedObject) obj).getName());
		} else {
			return super.equals(obj);
		}
	}

	@Override
	public int hashCode() {
		return name != null ? name.hashCode() : super.hashCode();
	}
}
