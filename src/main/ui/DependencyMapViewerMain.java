package main.ui;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Set;
import main.api.DependencyScanner;
import data.DependencyElem;
import data.DependencyMap;

/**
 * A simple class to show dependencies for clases inside a folder or zip file
 * 
 * @author Simon Emmanuel Gutierrez Brida
 * @version 0.1u
 */
public class DependencyMapViewerMain {

    public static void main(final String[] args) throws IOException {
    	DependencyScanner dependencyScanner = new DependencyScanner(Paths.get(args[0]));
        showDependencies(dependencyScanner.getDependencyMap(), dependencyScanner.getScannedPackages());
    }

    
    public static void showDependencies(DependencyMap dependencies, Set<String> packagesInSourceFolder) {
    	System.out.println("DIRECT DEPENDENCIES");
    	for (String className : dependencies.getClasses()) {
    		if (!packagesInSourceFolder.contains(dependencies.getClassAsDependencyElem(className).getPackage())) {
    			continue;
    		}
    		System.out.println("Direct dependencies for class " + className);
    		for (DependencyElem dependency : dependencies.getDependenciesAsDependencyElems(className)) {
    			if (!packagesInSourceFolder.contains(dependency.getPackage())) {
    				continue;
    			}
    			System.out.println("\t" + dependency.getFullyQualifiedName());
    		}
    		System.out.println();
    	}
    }
}
