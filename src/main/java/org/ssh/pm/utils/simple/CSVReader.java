package org.ssh.pm.utils.simple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class CSVReader {
	private final BufferedReader br;

	private boolean hasNext = true;

	private final char separator;

	private final char quotechar;

	private final int skipLines;

	private boolean linesSkiped;

	public static final char DEFAULT_SEPARATOR = ',';

	public static final char DEFAULT_QUOTE_CHARACTER = '"';

	public static final int DEFAULT_SKIP_LINES = 0;

	public CSVReader(final Reader reader) {
		this(reader, DEFAULT_SEPARATOR);
	}

	public CSVReader(final Reader reader, final char separator) {
		this(reader, separator, DEFAULT_QUOTE_CHARACTER);
	}

	public CSVReader(final Reader reader, final char separator, final char quotechar) {
		this(reader, separator, quotechar, DEFAULT_SKIP_LINES);
	}

	public CSVReader(final Reader reader, final char separator, final char quotechar, final int line) {
		this.br = new BufferedReader(reader);
		this.separator = separator;
		this.quotechar = quotechar;
		this.skipLines = line;
	}

	@SuppressWarnings("rawtypes")
	public List readAll() throws IOException {
		final List allElements = new ArrayList();
		while (hasNext) {
			final String[] nextLineAsTokens = readNext();
			if (nextLineAsTokens != null)
				allElements.add(nextLineAsTokens);
		}
		return allElements;
	}

	public String[] readNext() throws IOException {
		final String nextLine = getNextLine();
		return hasNext ? parseLine(nextLine) : null;
	}

	private String getNextLine() throws IOException {
		if (!this.linesSkiped) {
			for (int i = 0; i < skipLines; i++) {
				br.readLine();
			}
			this.linesSkiped = true;
		}
		final String nextLine = br.readLine();
		if (nextLine == null) {
			hasNext = false;
		}
		return hasNext ? nextLine : null;
	}

	@SuppressWarnings("rawtypes")
	private String[] parseLine(String nextLine) throws IOException {
		if (nextLine == null) {
			return null;
		}

		final List tokensOnThisLine = new ArrayList();
		StringBuilder sb = new StringBuilder();
		boolean inQuotes = false;
		do {
			if (inQuotes) {
				sb.append("\n");
				nextLine = getNextLine();
				if (nextLine == null)
					break;
			}
			for (int i = 0; i < nextLine.length(); i++) {
				final char c = nextLine.charAt(i);
				if (c == quotechar) {
					if (inQuotes && nextLine.length() > (i + 1) && nextLine.charAt(i + 1) == quotechar) {
						sb.append(nextLine.charAt(i + 1));
						i++;
					} else {
						inQuotes = !inQuotes;
						if (i > 2 && nextLine.charAt(i - 1) != this.separator
								&& nextLine.length() > (i + 1) && nextLine.charAt(i + 1) != this.separator) {
							sb.append(c);
						}
					}
				} else if (c == separator && !inQuotes) {
					tokensOnThisLine.add(sb.toString());
					sb = new StringBuilder(); // start work on next token
				} else {
					sb.append(c);
				}
			}
		} while (inQuotes);
		tokensOnThisLine.add(sb.toString());
		return (String[]) tokensOnThisLine.toArray(new String[0]);
	}

	public void close() throws IOException {
		br.close();
	}
}
