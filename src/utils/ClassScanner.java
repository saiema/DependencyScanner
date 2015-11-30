package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * This class scans a Path (folder or zip file) in search for java classes (.class files)
 * and for each one it will return an {@code InputStream object}
 * 
 * @author Simon Emmanuel Gutierrez Brida
 * @version 0.1u
 */
public class ClassScanner {
	
	private List<InputStream> istreams;
	private Path pathToScan;
	
	/**
	 * Constructor
	 * 
	 * @param path	:	path to scan
	 */
	public ClassScanner(String path) {
		this(Paths.get(path));
	}
	
	/**
	 * Constructor
	 * 
	 * @param path	:	path to scan
	 */
	public ClassScanner(Path path) {
		this.pathToScan = path;
		this.istreams = new LinkedList<InputStream>();
	}
	
	/**
	 * Scans a path (folder or zip file) and creates an {@code InputStream} for each .class file
	 * 
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public void scan() throws IllegalStateException, IOException {
		this.istreams.clear();
		File folderOrZipToScan = this.pathToScan.toFile();
		if (!folderOrZipToScan.exists()) {
			throw new IllegalStateException("ClassScanner was constructed with a path to a file or folder that doesn't exist");
		} else if (folderOrZipToScan.isFile() && !folderOrZipToScan.getName().endsWith(".zip")) {
			throw new IllegalStateException("ClassScanner was constructed to a path that refers to a file that is not a zip");
		}
		if (folderOrZipToScan.isFile()) {
			ZipFileScanner zipScanner = new ZipFileScanner(this.pathToScan, "\\.class$");
			zipScanner.scan();
			this.istreams = zipScanner.getInputStreams();
		} else {
			FileVisitor fileVisitor = new FileVisitor("**.class");
	 
			Files.walkFileTree(this.pathToScan, fileVisitor);
	 
			for (Path matchedPath : fileVisitor.getMatchedPaths()) {
				InputStream is = new FileInputStream(matchedPath.toFile());
				this.istreams.add(is);
			}
			
		}
	}
	
	/**
	 * @return all the {@code InputStream} created with method {@code scan()}
	 */
	public List<InputStream> getInputStreams() {
		return this.istreams;
	}
	
}
