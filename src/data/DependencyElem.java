package data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class represents a class defined by its name, package and dependencies.
 * 
 * @author Simon Emmanuel Gutierrez Brida
 * @version 0.2u
 */
public class DependencyElem implements Comparable<DependencyElem>{
	/**
	 * The name of the class with no package
	 */
	private String simpleClassName;
	/**
	 * The name of the class including the package
	 */
	private String fullyQualifiedClassName;
	/**
	 * The package this class belongs to
	 */
	private String pkg;
	/**
	 * Direct dependencies of this class divided by packages
	 */
	private Map<String, Set<DependencyElem>> dependencies;
	/**
	 * Recursive dependencies of this class divided by packages
	 */
	private Map<String, Set<DependencyElem>> flattenedDependencies;
	/**
	 * The las set of dependencies added
	 */
	private Set<DependencyElem> latestDependenciesAdded;
	
	/**
	 * Constructor
	 * 
	 * @param fullyQualifiedName	:	the fully qualified name of a class
	 */
	public DependencyElem(String fullyQualifiedName) {
		this.fullyQualifiedClassName = fullyQualifiedName;
		int lastDotIdx = fullyQualifiedName.lastIndexOf('.');
		if (lastDotIdx > 0) {
			this.pkg = fullyQualifiedName.substring(0, lastDotIdx);
			this.simpleClassName = fullyQualifiedName.substring(lastDotIdx+1, fullyQualifiedName.length());
		} else {
			this.pkg = "";
			this.simpleClassName = fullyQualifiedName;
		}
		this.dependencies = new HashMap<String, Set<DependencyElem>>();
		this.flattenedDependencies = new HashMap<String, Set<DependencyElem>>();
		this.latestDependenciesAdded = new TreeSet<DependencyElem>();
	}
	
	/**
	 * Constructor
	 * 
	 * @param fullyQualifiedName	:	the fully qualified name of a class
	 * @param dependencies			:	the dependencies of this class
	 */
	public DependencyElem(String fullyQualifiedName, Map<String, Set<DependencyElem>> dependencies) {
		this(fullyQualifiedName);
		this.dependencies.putAll(dependencies);
		for (Set<DependencyElem> deps : dependencies.values()) {
			this.latestDependenciesAdded.addAll(deps);
		}
	}
	
	/**
	 * Adds a new dependency for this class
	 * 
	 * @param elem	:	the dependency to add
	 */
	public void addDependency(DependencyElem elem) {
		Set<DependencyElem> dependenciesForPackage;
		if (this.equals(elem)) {
			return;
		}
		if (this.dependencies.containsKey(elem.getPackage())) {
			dependenciesForPackage = this.dependencies.get(elem.getPackage());
		} else {
			dependenciesForPackage = new TreeSet<DependencyElem>();
			this.dependencies.put(elem.getPackage(), dependenciesForPackage);
		}
		if (!dependenciesForPackage.contains(elem)) {
			dependenciesForPackage.add(elem);
			this.latestDependenciesAdded.add(elem);
		}
	}
	
	/**
	 * @return direct dependencies of this class divided by packages
	 */
	public Map<String, Set<DependencyElem>> getDirectDependencies() {
		return this.dependencies;
	}
	
	/**
	 * @return recursive dependencies of this class divided by packages
	 */
	public Map<String, Set<DependencyElem>> getAllDependencies() {
		flattenDependencies();
		return this.flattenedDependencies;
	}
	
	/**
	 * @return the package this class belongs to
	 */
	public String getPackage() {
		return this.pkg;
	}
	
	/**
	 * @return the name of the class with no package
	 */
	public String getSimpleName() {
		return this.simpleClassName;
	}
	
	/**
	 * @return the name of the class including the package
	 */
	public String getFullyQualifiedName() {
		return this.fullyQualifiedClassName;
	}
	
	@Override
	public int compareTo(DependencyElem other) {
		int result = this.fullyQualifiedClassName.compareTo(other.fullyQualifiedClassName);
		return result;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		if (!(other instanceof DependencyElem)) return false;
		if (this.pkg.compareTo(((DependencyElem)other).pkg) != 0) return false;
		if (this.simpleClassName.compareTo(((DependencyElem)other).simpleClassName) != 0) return false;
		if (this.fullyQualifiedClassName.compareTo(((DependencyElem)other).fullyQualifiedClassName) != 0) return false;
		return true;
	}
	
	@Override
	public String toString() {
		return this.fullyQualifiedClassName;
	}
	
	private void flattenDependencies() {
		if (!this.latestDependenciesAdded.isEmpty()) {
			for (DependencyElem dep : this.latestDependenciesAdded) {
				if (addToFlattenDependencies(dep)) {
					this.flattenedDependencies.putAll(dep.getAllDependencies());
				}
			}
			this.latestDependenciesAdded.clear();
		}
	}
	
	private boolean addToFlattenDependencies(DependencyElem elem) {
		Set<DependencyElem> dependenciesPerPackage;
		String pkg = elem.getPackage();
		if (this.flattenedDependencies.containsKey(pkg)) {
			dependenciesPerPackage = this.flattenedDependencies.get(pkg);
		} else {
			dependenciesPerPackage = new TreeSet<DependencyElem>();
			this.flattenedDependencies.put(pkg, dependenciesPerPackage);
		}
		return dependenciesPerPackage.add(elem);
	}
	
}
