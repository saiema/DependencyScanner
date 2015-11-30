package main.api;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.ClassReader;

import utils.ClassScanner;
import visitor.DependencyVisitor;
import data.DependencyMap;

/**
 * This class encapsulates the file scanning and dependency map creation.
 * 
 * <p>
 * It allows to scan a folder or zip file in search for classes, and to create a dependency map with all classes
 * found.
 * <p>
 * 
 * @author Simon Emmanuel Gutierrez Brida
 * @version 0.1.1u
 */
public class DependencyScanner {
	
	/**
	 * The dependency map associated with this scanner
	 */
	private DependencyMap dependencyMap;
	/**
	 * A set of packages found inside the path to scan
	 */
	private Set<String> scannedPackages;
	
	private final String pathToScan;
	
	/**
	 * Constructor
	 * <p>
	 * This constructor will construct a dependency map that can be obtained through {@link DependencyScanner#getDependencyMap()}
	 * <p>
	 * 
	 * @param pathToScan	:	the path to scan, pointing to a folder or a zip file
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public DependencyScanner(Path pathToScan) throws IllegalStateException, IOException {
		this.pathToScan = pathToScan.toString();
		ClassScanner classScanner = new ClassScanner(pathToScan);
		scan(classScanner);
	}
	
	/**
	 * @return a dependency map related to the path used in the constructor
	 */
	public DependencyMap getDependencyMap() {
		return this.dependencyMap;
	}
	
	/**
	 * @return a set of packages found inside the path to scan
	 */
	public Set<String> getScannedPackages() {
		return this.scannedPackages;
	}
	
	/**
	 * @return the scanned path
	 */
	public String getScannedPath() {
		return this.pathToScan;
	}
	
	/**
	 * Populates the dependency map
	 * 
	 * @param classScanner				:	a {@code ClassScanner} instance to scan for class files
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	private void scan(ClassScanner classScanner) throws IllegalStateException, IOException {
		classScanner.scan();
		List<InputStream> istreams = classScanner.getInputStreams();
		this.dependencyMap = new DependencyMap();
		DependencyVisitor visitor = new DependencyVisitor(this.dependencyMap);
		for (InputStream is : istreams) {
			new ClassReader(is).accept(visitor, 0);
		}
		this.scannedPackages = visitor.getPackagesInSourceFolder();
	}
	
}
