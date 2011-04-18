package org.ssh.pm.utils.simple;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class XMLUtils {

	public static void saveToFile(final Document document, final File targetFile) throws IOException {
		XMLWriter writer = null;
		try {
			writer = new XMLWriter(new FileOutputStream(targetFile), OutputFormat.createPrettyPrint());
			writer.write(document);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (final IOException e) {
				}
			}
		}
	}
}
