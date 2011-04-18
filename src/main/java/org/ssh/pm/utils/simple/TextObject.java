package org.ssh.pm.utils.simple;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */

public class TextObject extends ANamedObject {
	private String text;

	public TextObject(final String name) {
		super(name);
	}

	public TextObject(final String name, final String text) {
		super(name);
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(final String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return getText();
	}

	private static final long serialVersionUID = 8250066556301501862L;
}
