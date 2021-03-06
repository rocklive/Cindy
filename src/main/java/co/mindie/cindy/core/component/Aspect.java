package co.mindie.cindy.core.component;

/**
 * Defines the aspects of the component. This gives an insight of what
 * a component can do and where it can be used.
 */
public enum Aspect {

	/**
	 * The component is thread safe and supports concurrent operations.
	 */
	THREAD_SAFE,

	/**
	 * The component requires to be in the most upper ComponentBox (the application's box)
	 */
	SINGLETON

}
