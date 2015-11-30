package data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * This class is a container for all classes and dependencies found while scanning a folder
 * 
 * @author Simon Emmanuel Gutierrez Brida
 * @version 0.1u
 */
public class DependencyMap {

	/**
	 * A mapping of classes full names and {@code DependencyElem}
	 */
	private Map<String, DependencyElem> elements;
	/**
	 * All classes found
	 */
	private Set<String> classes;
	
	/**
	 * Constructor
	 */
	public DependencyMap() {
		this.elements = new HashMap<String, DependencyElem>();
		this.classes = new HashSet<String>();
	}
	
	/**
	 * Searches for a {@code DependencyElem} associated with a class name
	 * 
	 * @param fullyQualifiedClassName	:	a class fully qualified name
	 * @return	an {@code DependencyElem} object {@code e} such that {@code e.getFullyQualifiedName().compareTo(fullyQualifiedName) == 0}, {@code null} otherwise
	 */
	public DependencyElem getClassAsDependencyElem(String fullyQualifiedClassName) {
		return this.elements.get(fullyQualifiedClassName);
	}
	
	/**
	 * Add a new {@code DependencyElem} to the container
	 * 
	 * @param elem	:	the element to add
	 */
	public void addNewDependencyElem(DependencyElem elem) {
		this.elements.put(elem.getFullyQualifiedName(), elem);
		this.classes.add(elem.getFullyQualifiedName());
	}
	
	/**
	 * Add a dependency to another element.
	 * 
	 * If any of the elements is not in the container then they will be added
	 * 
	 * @param elem			:	the element to which the dependency will be added
	 * @param dependency	:	the dependency to add
	 */
	public void addNewDependency(DependencyElem elem, DependencyElem dependency) {
		if (elem.equals(dependency)) return;
		elem.addDependency(dependency);
		if (!this.elements.containsKey(elem.getFullyQualifiedName())) addNewDependencyElem(elem);
		if (!this.elements.containsKey(dependency.getFullyQualifiedName())) addNewDependencyElem(dependency);
	}
	
	/**
	 * Variant of {@link DependencyMap#addNewDependency(DependencyElem, DependencyElem)} that will take
	 * a fully qualified class name instead of a {@code DependencyElem} for the first argument
	 * 
	 * @param elemFullyQualifiedClassName	:	fully qualified class name of the element to which the dependency will be added
	 * @param dependency					:	the dependency to add
	 */
	public void addNewDependency(String elemFullyQualifiedClassName, DependencyElem dependency) {
		DependencyElem elem;
		if (this.elements.containsKey(elemFullyQualifiedClassName)) {
			elem = this.elements.get(elemFullyQualifiedClassName);
		} else {
			elem = new DependencyElem(elemFullyQualifiedClassName);
		}
		addNewDependency(elem, dependency);
	}
	
	/**
	 * Variant of {@link DependencyMap#addNewDependency(DependencyElem, DependencyElem)} that will take
	 * a fully qualified class name instead of a {@code DependencyElem} for the second argument
	 * 
	 * @param elemFullyQualifiedClassName			:	the element to which the dependency will be added
	 * @param dependencyFullyQualifiedClassName		:	fully qualified class name of the dependency to add
	 */
	public void addNewDependency(DependencyElem elem, String dependencyFullyQualifiedClassName) {
		DependencyElem dependency;
		if (this.elements.containsKey(dependencyFullyQualifiedClassName)) {
			dependency = this.elements.get(dependencyFullyQualifiedClassName);
		} else {
			dependency = new DependencyElem(dependencyFullyQualifiedClassName);
		}
		addNewDependency(elem, dependency);
	}
	
	/**
	 * Variant of {@link DependencyMap#addNewDependency(DependencyElem, DependencyElem)} that will take
	 * a fully qualified class name instead of a {@code DependencyElem} for the first and second arguments
	 * 
	 * @param elemFullyQualifiedClassName			:	fully qualified class name of the element to which the dependency will be added
	 * @param dependencyFullyQualifiedClassName		:	fully qualified class name of the dependency to add
	 */
	public void addNewDependency(String elemFullyQualifiedClassName, String dependencyFullyQualifiedClassName) {
		DependencyElem elem;
		if (this.elements.containsKey(elemFullyQualifiedClassName)) {
			elem = this.elements.get(elemFullyQualifiedClassName);
		} else {
			elem = new DependencyElem(elemFullyQualifiedClassName);
		}
		DependencyElem dependency;
		if (this.elements.containsKey(dependencyFullyQualifiedClassName)) {
			dependency = this.elements.get(dependencyFullyQualifiedClassName);
		} else {
			dependency = new DependencyElem(dependencyFullyQualifiedClassName);
		}
		addNewDependency(elem, dependency);
	}
	
	/**
	 * @return classes contained by this container
	 */
	public Set<String> getClasses() {
		return this.classes;
	}
	
	/**
	 * Given a class full name this method will return all classes dependencies
	 * 
	 * @param fullyQualifiedClassName	:	the class full name for which to search for dependencies
	 * @return all dependencies for the class
	 */
	public Set<String> getDependencies(String fullyQualifiedClassName) {
		if (!this.elements.containsKey(fullyQualifiedClassName)) {
			return new HashSet<String>();
		} else {
			return getDependencies(this.elements.get(fullyQualifiedClassName));
		}
	}
	
	/**
	 * Variant of {@link DependencyMap#getDependencies(String)} that takes a {@code DependencyElem} as
	 * argument
	 * 
	 * @param elem	:	a class defined by a {@code DependencyElem} for which to search for dependencies
	 * @return all dependencies for the class
	 */
	public Set<String> getDependencies(DependencyElem elem) {
		Set<String> dependenciesAsClassNames = new HashSet<String>();
		Map<String, Set<DependencyElem>> dependencies = elem.getDirectDependencies();
		for (Entry<String, Set<DependencyElem>> dependenciesPerPackage : dependencies.entrySet()) {
			for (DependencyElem dependency : dependenciesPerPackage.getValue()) {
				dependenciesAsClassNames.add(dependency.getFullyQualifiedName());
			}
		}
		return dependenciesAsClassNames;
	}
	
	/**
	 * Variant of {@link DependencyMap#getDependencies(DependencyElem)} that returns dependencies as
	 * {@code DependencyElem} objects instead of fully qualified class names.
	 * 
	 * @param elem	:	a class defined by a {@code DependencyElem} for which to search for dependencies
	 * @return all dependencies for the class as {@code DependencyElem} objects
	 */
	public Set<DependencyElem> getDependenciesAsDependencyElems(DependencyElem elem) {
		Set<DependencyElem> dependenciesAsDependencyElements = new HashSet<DependencyElem>();
		Map<String, Set<DependencyElem>> dependencies = elem.getDirectDependencies();
		for (Entry<String, Set<DependencyElem>> dependenciesPerPackage : dependencies.entrySet()) {
			dependenciesAsDependencyElements.addAll(dependenciesPerPackage.getValue());
		}
		return dependenciesAsDependencyElements;
	}
	
	/**
	 * Variant of {@link DependencyMap#getDependencies(String)} that returns dependencies as
	 * {@code DependencyElem} objects instead of fully qualified class names.
	 * 
	 * @param fullyQualifiedClassName	:	the class full name for which to search for dependencies
	 * @return all dependencies for the class as {@code DependencyElem} objects
	 */
	public Set<DependencyElem> getDependenciesAsDependencyElems(String fullyQualifiedClassName) {
		if (!this.elements.containsKey(fullyQualifiedClassName)) {
			return new HashSet<DependencyElem>();
		} else {
			return getDependenciesAsDependencyElems(this.elements.get(fullyQualifiedClassName));
		}
	}
	
}
