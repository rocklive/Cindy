/////////////////////////////////////////////////
// Project : WSFramework
// Package : co.mindie.wsframework.resolver.impl
// StringToIntConverter.java
//
// Author : Simon CORSIN <simoncorsin@gmail.com>
// File created on Jul 24, 2014 at 3:12:27 PM
////////

package co.mindie.cindy.resolver.builtin;

import me.corsin.javatools.misc.NullArgumentException;
import co.mindie.cindy.automapping.Resolver;
import co.mindie.cindy.resolver.IResolver;

@Resolver(managedInputClasses = { String.class }, managedOutputClasses = { int.class, Integer.class })
public class StringToIntResolver implements IResolver<String, Integer> {

	////////////////////////
	// VARIABLES
	////////////////

	////////////////////////
	// CONSTRUCTORS
	////////////////

	public StringToIntResolver() {

	}

	////////////////////////
	// METHODS
	////////////////

	@Override
	public Integer resolve(String input, Class<?> expectedOutputType, int options) {
		if (expectedOutputType == null) {
			expectedOutputType = Integer.class;
		}

		if (input == null) {
			if (expectedOutputType == Integer.class) {
				return null;
			} else {
				throw new NullArgumentException("input");
			}
		}

		return Integer.valueOf(input);
	}

	////////////////////////
	// GETTERS/SETTERS
	////////////////
}