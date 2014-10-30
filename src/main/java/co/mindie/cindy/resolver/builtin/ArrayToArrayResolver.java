package co.mindie.cindy.resolver.builtin;

import co.mindie.cindy.exception.CindyException;
import co.mindie.cindy.resolver.IDynamicResolver;
import co.mindie.cindy.resolver.IResolver;

import java.lang.reflect.Array;

public class ArrayToArrayResolver<Input, Output> implements IDynamicResolver<Input[], Output[]> {

	////////////////////////
	// VARIABLES
	////////////////

	private IResolver<Input, Output> singleResolver;
	final private Class<Output> outputClass;

	////////////////////////
	// CONSTRUCTORS
	////////////////

	public ArrayToArrayResolver() {
		this((Class)Object.class, null);
	}

	public ArrayToArrayResolver(Class<Output> outputClass, IResolver<Input, Output> singleResolver) {
		this.singleResolver = singleResolver;
		this.outputClass = outputClass;
	}

	////////////////////////
	// METHODS
	////////////////

	@Override
	public Output[] resolve(Input[] inputArray, Class<?> expectedOutputType, int options) {
		if (this.singleResolver == null) {
			throw new CindyException("ArrayToArrayResolver needs to has a sub resolver");
		}

		if (inputArray == null) {
			return null;
		}

		Output[] outputArray = (Output[])Array.newInstance(this.outputClass, inputArray.length);

		for (int j = 0; j < inputArray.length; j++) {
			Output obj = this.singleResolver.resolve(inputArray[j], expectedOutputType, options);

			outputArray[j] = obj;
		}

		return outputArray;
	}

	@Override
	public void appendSubResolver(IResolver resolver) {
		this.singleResolver = resolver;
	}

	////////////////////////
	// GETTERS/SETTERS
	////////////////

}