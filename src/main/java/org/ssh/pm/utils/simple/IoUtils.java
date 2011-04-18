package org.ssh.pm.utils.simple;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class IoUtils {
	static final int BUFFER = 8 * 1024;

	public static String getStringFromInputStream(final InputStream inputStream) throws IOException {
		return getStringFromReader(new InputStreamReader(inputStream));
	}

	public static String getStringFromInputStream(final InputStream inputStream,
			final String charsetName) throws IOException {
		return getStringFromReader(new InputStreamReader(inputStream, charsetName));
	}

	public static String getStringFromReader(final Reader reader) throws IOException {
		BufferedReader br = null;
		try {
			br = new BufferedReader(reader);
			final StringWriter sw = new StringWriter();
			final PrintWriter writer = new PrintWriter(sw);
			String s;
			while ((s = br.readLine()) != null) {
				writer.println(s);
			}
			writer.flush();
			return sw.toString();
		} finally {
			if (br != null) {
				br.close();
			}
		}
	}

	public static String[] getStringsFromReader(final Reader reader) throws IOException {
		BufferedReader br = null;
		try {
			br = new BufferedReader(reader);
			final List<String> l = new ArrayList<String>();
			String s;
			while ((s = br.readLine()) != null) {
				l.add(s);
			}
			return l.toArray(new String[l.size()]);
		} finally {
			if (br != null) {
				br.close();
			}
		}
	}

	public static void unzip(final InputStream in, final String target) throws IOException {
		unzip(in, target, true);
	}

	public static void unzip(final InputStream in, final String target, final boolean rewrite)
			throws IOException {
		unzip(in, target, rewrite, new IUnZipHandle() {
			@Override
			public void doFile(final ZipInputStream is, final File destFile) throws IOException {
				final BufferedOutputStream oStream = new BufferedOutputStream(new FileOutputStream(
						destFile));
				try {
					copyStream(is, oStream);
				} finally {
					if (oStream != null) {
						oStream.close();
					}
				}
			}
		});
	}

	public static void unzip(final InputStream in, String target, final boolean rewrite,
			final IUnZipHandle unzipHandle) throws IOException {
		if (target.charAt(target.length() - 1) != File.separatorChar) {
			target += File.separatorChar;
		}
		ZipInputStream is;
		if (in instanceof ZipInputStream) {
			is = (ZipInputStream) in;
		} else {
			is = new ZipInputStream(new BufferedInputStream(in));
		}
		try {
			ZipEntry entry;
			while ((entry = is.getNextEntry()) != null) {
				final String entryName = entry.getName();
				final int index = entryName.lastIndexOf("/");
				if (index > 0) {
					createDirectoryRecursively(new File(target + entryName.substring(0, index)));
				}
				if (entry.isDirectory()) {
					continue;
				}

				final File destFile = new File(target + entryName);
				if (rewrite || !destFile.exists()) {
					unzipHandle.doFile(is, destFile);
				}
			}
		} finally {
			if (is != null) {
				in.close();
			}
		}
	}

	public static interface IUnZipHandle {

		void doFile(ZipInputStream is, File destFile) throws IOException;
	}

	public static boolean createDirectoryRecursively(File directory) {
		if (directory == null) {
			return false;
		} else if (directory.exists()) {
			return !directory.isFile();
		} else if (!directory.isAbsolute()) {
			directory = new File(directory.getAbsolutePath());
		}
		final String parent = directory.getParent();
		if ((parent == null) || !createDirectoryRecursively(new File(parent))) {
			return false;
		}
		directory.mkdir();
		return directory.exists();
	}

	public static File createFile(final File file) throws IOException {
		if (!file.exists()) {
			createDirectoryRecursively(file.getParentFile());
			file.createNewFile();
		}
		return file;
	}

	public static void copyFile(final File from, final File to) throws IOException {
		final InputStream inputStream = new BufferedInputStream(new FileInputStream(from));
		copyFile(inputStream, to);
	}

	public static void copyFile(final InputStream inputStream, final File to) throws IOException {
		createFile(to);
		OutputStream outputStream = null;
		try {
			copyStream(inputStream, outputStream = new BufferedOutputStream(new FileOutputStream(to)));
		} finally {
			inputStream.close();
			if (outputStream != null) {
				outputStream.close();
			}
		}
	}

	public static String toFileSize(final long size) {
		if (size < 0) {
			return "";
		} else if (size > 1024 * 1024) {
			final double d = (double) size / (double) (1024 * 1024);
			return ConvertUtils.toString(NumberUtils.formatDouble(d)) + " MB";
		} else if (size > 1024) {
			final double d = (double) size / (double) 1024;
			return ConvertUtils.toString(NumberUtils.formatDouble(d)) + " KB";
		} else {
			return ConvertUtils.toString(size) + " B";
		}
	}

	public static long sizeOfDirectory(final File directory) {
		return FileUtils.sizeOfDirectory(directory);
	}

	public static int copyStream(final InputStream inputStream, final OutputStream outputStream)
			throws IOException {
		int result = 0;
		final byte[] buf = new byte[BUFFER];
		for (;;) {
			final int numRead = inputStream.read(buf);
			if (numRead == -1) {
				break;
			}
			outputStream.write(buf, 0, numRead);
			result += numRead;
		}
		outputStream.flush();
		return result;
	}

	public static byte[] getMacAddressBytes() throws IOException {
		final NetworkInterface ni = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
		return ni.getHardwareAddress();
	}
}
