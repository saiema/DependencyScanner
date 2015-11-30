package utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * A class to scan files inside a zip file and collect {@code InputStream} for each
 * file that matches a specific pattern.
 * 
 * @author Simon Emmanuel Gutierrez Brida
 * @version 0.1.1u
 */
public class ZipFileScanner {
	
	private final Pattern pattern;
	private List<InputStream> matchedInputStreams = new LinkedList<InputStream>();
	private Path zipFilePath;
	private ZipFile zipFile;
	
	/**
	 * Constructor
	 * 
	 * @param zipFilePath	:	path to the zip file
	 * @param pattern		:	the pattern to use
	 * @throws IOException
	 */
	public ZipFileScanner(Path zipFilePath, String pattern) throws IOException {
		this.pattern = Pattern.compile(pattern);
		this.zipFilePath = zipFilePath;
		if (this.zipFilePath.toFile().exists()) {
			this.zipFile = new ZipFile(this.zipFilePath.toString());
		}
	}
	
	/**
	 * Scans and collects {@code InputStream} for each file in the zip
	 * that matches the pattern used in the constructor
	 * @throws IOException
	 */
	public void scan() throws IOException {
		if (!this.matchedInputStreams.isEmpty()) {
			this.matchedInputStreams.clear();
		}
		if (this.zipFile != null) {
			Enumeration<? extends ZipEntry> en = this.zipFile.entries();
			while (en.hasMoreElements()) {
				ZipEntry e = en.nextElement();
				match(e);
			}
		}
	}
	
	/**
	 * @return the list of {@code InputStream} collected with the method {@code scan()}
	 */
	public List<InputStream> getInputStreams() {
		return this.matchedInputStreams;
	}
	
	
	/**
	 * Adds an {@code InputStream} to the matched input streams if the zip entry matches the pattern used in the constructor
	 * 
	 * @param file	:	the file to check
	 * @throws IOException 
	 */
	private void match(ZipEntry e) throws IOException {
		String name = e.getName();
		if (name != null && this.pattern.matcher(name).find()) {
			matchedInputStreams.add(this.zipFile.getInputStream(e));
		}
	}
	
}
