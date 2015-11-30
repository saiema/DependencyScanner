package utils;

import static java.nio.file.FileVisitResult.CONTINUE;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A file visitor that will recursively visit any directory and will collect all
 * files that match a specific pattern
 * 
 * @author Drona
 * @see {@link http://wilddiary.com/list-files-matching-a-naming-pattern-java/}
 * @version 1.0
 */
public class FileVisitor extends SimpleFileVisitor<Path> {

	private final PathMatcher matcher;
	private List<Path> matchedPaths = new ArrayList<Path>();

	/**
	 * Constructor
	 * 
	 * @param pattern	:	the pattern to use
	 */
	FileVisitor(String pattern) {
		this.matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
	}
	
	/**
	 * @return the number of paths that matched the pattern used in the constructor
	 */
	public int getTotalMatches() {
		return matchedPaths.size();
	}

	/**
	 * @return all paths that matches with the pattern used in the constructor
	 */
	public Collection<Path> getMatchedPaths() {
		return matchedPaths;
	}

	// Invoke the pattern matching
	// method on each file.
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
		match(file);
		return CONTINUE;
	}

	// Invoke the pattern matching
	// method on each directory.
	@Override
	public FileVisitResult preVisitDirectory(Path dir,
			BasicFileAttributes attrs) {
		match(dir);
		return CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) {
		System.err.println(exc);
		return CONTINUE;
	}

	/**
	 * Adds a path to the matched paths if the path matches the pattern used in the constructor
	 * 
	 * @param file	:	the file to check
	 */
	private void match(Path file) {
		Path name = file.toAbsolutePath();
		if (name != null && matcher.matches(name)) {
			matchedPaths.add(name);
		}
	}

}
