package data;

public class ReloadCommand {
	
	private DependencyElem elemToReload;
	private DependencyMap dependencyMap;
	private boolean justReload;
	
	public ReloadCommand(String fullyQualifiedClassName, DependencyMap dependencyMap) throws IllegalArgumentException {
		DependencyElem elemToReload = dependencyMap.getClassAsDependencyElem(fullyQualifiedClassName);
		if (elemToReload == null) {
			throw new IllegalArgumentException("Class " + fullyQualifiedClassName + " is not found in the dependency map");
		}
		this.elemToReload = elemToReload;
		this.dependencyMap = dependencyMap;
		this.justReload = false;
	}

	public ReloadCommand(DependencyElem elemToReload, DependencyMap dependencyMap) {
		this(elemToReload, dependencyMap, false);
	}
	
	private ReloadCommand(DependencyElem elemToReload, DependencyMap dependencyMap, boolean justReload) {
		this.elemToReload = elemToReload;
		this.dependencyMap = dependencyMap;
		this.justReload = justReload;
	}
	
	
	
}
