package org.ssh.pm.utils.simple;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

public class CSVWriter {
	public static final char NO_QUOTE_CHARACTER = '\u0000';

	public static final char NO_ESCAPE_CHARACTER = '\u0000';

	public static final char DEFAULT_SEPARATOR = ',';

	public static final char DEFAULT_QUOTE_CHARACTER = NO_QUOTE_CHARACTER;

	public static final char DEFAULT_ESCAPE_CHARACTER = '"';

	public static final String DEFAULT_LINE_END = "\n";

	private final Writer rawWriter;

	private final PrintWriter pw;

	private final char separator;

	private final char quotechar;

	private final char escapechar;

	private final String lineEnd;

	public CSVWriter(final Writer writer) {
		this(writer, DEFAULT_SEPARATOR);
	}

	public CSVWriter(final Writer writer, final char separator) {
		this(writer, separator, DEFAULT_QUOTE_CHARACTER);
	}

	public CSVWriter(final Writer writer, final char separator, final char quotechar) {
		this(writer, separator, quotechar, DEFAULT_ESCAPE_CHARACTER);
	}

	public CSVWriter(final Writer writer, final char separator, final char quotechar,
			final char escapechar) {
		this(writer, separator, quotechar, escapechar, DEFAULT_LINE_END);
	}

	public CSVWriter(final Writer writer, final char separator, final char quotechar,
			final String lineEnd) {
		this(writer, separator, quotechar, DEFAULT_ESCAPE_CHARACTER, lineEnd);
	}

	public CSVWriter(final Writer writer, final char separator, final char quotechar,
			final char escapechar, final String lineEnd) {
		this.rawWriter = writer;
		this.pw = new PrintWriter(writer);
		this.separator = separator;
		this.quotechar = quotechar;
		this.escapechar = escapechar;
		this.lineEnd = lineEnd;
	}

	public void writeAll(final List<?> allLines) {
		for (final Iterator<?> iter = allLines.iterator(); iter.hasNext();) {
			final String[] nextLine = (String[]) iter.next();
			writeNext(nextLine);
		}
	}

	public void writeNext(final String[] nextLine) {
		if (nextLine == null)
			return;

		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < nextLine.length; i++) {
			if (i != 0) {
				sb.append(separator);
			}
			final String nextElement = nextLine[i];
			if (nextElement == null)
				continue;
			if (quotechar != NO_QUOTE_CHARACTER)
				sb.append(quotechar);
			for (int j = 0; j < nextElement.length(); j++) {
				final char nextChar = nextElement.charAt(j);
				if (escapechar != NO_ESCAPE_CHARACTER && nextChar == quotechar) {
					sb.append(escapechar).append(nextChar);
				} else if (escapechar != NO_ESCAPE_CHARACTER && nextChar == escapechar) {
					sb.append(escapechar).append(nextChar);
				} else {
					sb.append(nextChar);
				}
			}
			if (quotechar != NO_QUOTE_CHARACTER)
				sb.append(quotechar);
		}

		sb.append(lineEnd);
		pw.write(sb.toString());
	}

	public void flush() throws IOException {
		pw.flush();
	}

	public void close() throws IOException {
		pw.flush();
		pw.close();
		rawWriter.close();
	}
}
