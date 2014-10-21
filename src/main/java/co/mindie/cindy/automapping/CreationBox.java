/////////////////////////////////////////////////
// Project : WSFramework
// Package : co.mindie.wsframework.automapping
// Scope.java
//
// Author : Simon CORSIN <simoncorsin@gmail.com>
// File created on Jun 11, 2014 at 2:08:37 PM
////////

package co.mindie.cindy.automapping;

public enum CreationBox {

	/**
	 * If not found while following the {@link SearchScope} rule,
	 * the dependency will be created in the current {@link co.mindie.cindy.component.ComponentBox}
	 */
	CURRENT_BOX,

	/**
	 * If not found while following the {@link SearchScope} rule,
	 * the dependency will be created in the parent of the current {@link co.mindie.cindy.component.ComponentBox}.
	 */
	PARENT_BOX,

	/**
	 * If not found while following the {@link SearchScope} rule,
	 * the dependency will NOT be created.
	 */
	NO_CREATION
}
