/////////////////////////////////////////////////
// Project : WSFramework
// Package : co.mindie.wsframework.resolver
// ModelConverterOutput.java
//
// Author : Simon CORSIN <simoncorsin@gmail.com>
// File created on Jun 20, 2014 at 5:24:53 PM
////////

package co.mindie.cindy.webservice.resolver;

import co.mindie.cindy.core.component.initializer.ComponentInitializer;
import co.mindie.cindy.core.component.box.ComponentBox;

public class ResolverBuilder implements IResolverBuilder {

	////////////////////////
	// VARIABLES
	////////////////

	final private Class<?> converterClass;
	final private Class<?> inputClass;
	final private Class<?> outputClass;
	final private int priority;

	////////////////////////
	// CONSTRUCTORS
	////////////////

	public ResolverBuilder(Class<?> converterClass, Class<?> inputClass, Class<?> outputClass, int priority) {
		this.converterClass = converterClass;
		this.inputClass = inputClass;
		this.outputClass = outputClass;
		this.priority = priority;
	}

	////////////////////////
	// METHODS
	////////////////

	@Override
	public String toString() {
		return this.inputClass + " -> " + this.outputClass;
	}

	@Override
	public IResolver findOrCreateResolver(ComponentInitializer initializer, ComponentBox enclosingBox) {
		IResolver resolver = (IResolver) enclosingBox.findComponent(this.converterClass);

		if (resolver == null) {
			resolver = (IResolver) initializer.createComponent(this.converterClass, enclosingBox).getInstance();
		}

		return resolver;
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

	public boolean isDynamic() {
		return IDynamicResolver.class.isAssignableFrom(this.converterClass);
	}

	public int getPriority() {
		return priority;
	}
}
