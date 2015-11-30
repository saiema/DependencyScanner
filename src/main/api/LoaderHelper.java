package main.api;

/**
 * This class is used to help a class reloader to know which classes (and in which order) to load when loading a specific class
 * <p>
 * For example if class A uses B and B uses A. The dependency map should be
 * <p>
 * <ul>
 * <li>Direct dependencies for class A : [B]</li>
 * <li>Direct dependencies for class B : [A]</li>
 * </ul>
 * <p>
 * To reload class A the classes that use A should be reloaded first.
 * In this case B, 
 * 
 * A -> B
 * B -> A
 * B* -> A
 * A* -> B*
 * <p>
 * 
 * @author Simon Emmanuel Gutierrez Brida
 * @version 0.1u
 */
public class LoaderHelper {

}
