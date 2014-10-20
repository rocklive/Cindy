/////////////////////////////////////////////////
// Project : WSFramework
// Package : co.mindie.wsframework.resolver
// ModelConverterOutput.java
//
// Author : Simon CORSIN <simoncorsin@gmail.com>
// File created on Jun 20, 2014 at 5:24:53 PM
////////

package co.mindie.cindy.resolver;

import co.mindie.cindy.CindyApp;
import co.mindie.cindy.component.ComponentBox;

public class ResolverOutput implements IResolverOutput {

	////////////////////////
	// VARIABLES
	////////////////

	final private CindyApp application;
	final private Class<?> converterClass;
	final private Class<?> inputClass;
	final private Class<?> outputClass;

	////////////////////////
	// CONSTRUCTORS
	////////////////

	public ResolverOutput(CindyApp application, Class<?> converterClass, Class<?> inputClass, Class<?> outputClass) {
		this.application = application;
		this.converterClass = converterClass;
		this.inputClass = inputClass;
		this.outputClass = outputClass;
	}

	////////////////////////
	// METHODS
	////////////////

	@Override
	@SuppressWarnings("unchecked")
	public Object createResolversAndResolve(ComponentBox cc, Object inputObject, int options) {
		@SuppressWarnings("rawtypes")
		IResolver resolver = (IResolver)this.application.findOrCreateComponent(cc, this.converterClass);
		return resolver.resolve(inputObject, this.outputClass, options);
	}

	@Override
	public String toString() {
		return this.inputClass + " -> " + this.outputClass;
	}

	////////////////////////
	// GETTERS/SETTERS
	////////////////

	public Class<?> getConverterClass() {
		return this.converterClass;
	}

	public Class<?> getOutputClass() {
		return this.outputClass;
	}

	public Class<?> getInputClass() {
		return this.inputClass;
	}
}
