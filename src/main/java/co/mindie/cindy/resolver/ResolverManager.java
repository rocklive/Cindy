/////////////////////////////////////////////////
// Project : WSFramework
// Package : co.mindie.wsframework.resolver
// AbstractModelConverterManager.java
//
// Author : Simon CORSIN <simoncorsin@gmail.com>
// File created on Feb 13, 2014 at 1:16:25 PM
////////

package co.mindie.cindy.resolver;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import co.mindie.cindy.CindyApp;
import org.apache.log4j.Logger;

import co.mindie.cindy.resolver.builtin.ArrayToListResolver;
import co.mindie.cindy.resolver.builtin.CollectionResolver;
import co.mindie.cindy.resolver.builtin.RequestParameterToInputStreamResolver;
import co.mindie.cindy.resolver.builtin.RequestParameterToStringResolver;
import co.mindie.cindy.resolver.builtin.StringToBooleanArrayResolver;
import co.mindie.cindy.resolver.builtin.StringToBooleanResolver;
import co.mindie.cindy.resolver.builtin.StringToDateTimeArrayResolver;
import co.mindie.cindy.resolver.builtin.StringToDateTimeResolver;
import co.mindie.cindy.resolver.builtin.StringToDoubleArrayResolver;
import co.mindie.cindy.resolver.builtin.StringToDoubleResolver;
import co.mindie.cindy.resolver.builtin.StringToFloatArrayResolver;
import co.mindie.cindy.resolver.builtin.StringToFloatResolver;
import co.mindie.cindy.resolver.builtin.StringToIntArrayResolver;
import co.mindie.cindy.resolver.builtin.StringToIntResolver;
import co.mindie.cindy.resolver.builtin.StringToLongArrayResolver;
import co.mindie.cindy.resolver.builtin.StringToLongResolver;
import co.mindie.cindy.resolver.builtin.StringToStringArrayResolver;
import co.mindie.cindy.resolver.builtin.StringToStringResolver;

public class ResolverManager {

	////////////////////////
	// VARIABLES
	////////////////

	private static final Logger LOGGER = Logger.getLogger(ResolverManager.class);

	private Map<Class<?>, ResolverEntry> resolverEntriesByInputClass;
	private CindyApp application;

	////////////////////////
	// CONSTRUCTORS
	////////////////

	public ResolverManager(CindyApp application) {
		super();

		this.application = application;
		this.resolverEntriesByInputClass = new HashMap<>();

		this.addBuiltinConverters();
	}

	////////////////////////
	// METHODS
	////////////////

	private void addBuiltinConverters() {
		this.addBuiltin(CollectionResolver.class);
		this.addBuiltin(ArrayToListResolver.class);
		this.addBuiltin(StringToDoubleResolver.class);
		this.addBuiltin(StringToFloatResolver.class);
		this.addBuiltin(StringToIntResolver.class);
		this.addBuiltin(StringToLongResolver.class);
		this.addBuiltin(StringToBooleanResolver.class);
		this.addBuiltin(StringToStringArrayResolver.class);
		this.addBuiltin(StringToDoubleArrayResolver.class);
		this.addBuiltin(StringToFloatArrayResolver.class);
		this.addBuiltin(StringToIntArrayResolver.class);
		this.addBuiltin(StringToLongArrayResolver.class);
		this.addBuiltin(StringToBooleanArrayResolver.class);
		this.addBuiltin(StringToStringResolver.class);
		this.addBuiltin(StringToDateTimeResolver.class);
		this.addBuiltin(StringToDateTimeArrayResolver.class);
		this.addBuiltin(RequestParameterToInputStreamResolver.class);
		this.addBuiltin(RequestParameterToStringResolver.class);
	}

	private void addBuiltin(Class<?> type) {
		this.application.getComponentMetadataManager().loadComponent(type);
		co.mindie.cindy.automapping.Resolver modelConverter = type.getAnnotation(co.mindie.cindy.automapping.Resolver.class);

		if (modelConverter != null) {
			for (Class<?> inputClass : modelConverter.managedInputClasses()) {
				for (Class<?> outputClass : modelConverter.managedOutputClasses()) {
					this.addConverter(type, inputClass, outputClass, modelConverter.isDefaultForInputTypes());
				}
			}
		}
	}

	public void removeAllConverters() {
		this.resolverEntriesByInputClass.clear();
	}

	public void addConverter(Class<?> modelConverterClass, Class<?> inputClass, Class<?> outputClass, boolean isDefault) {
		LOGGER.trace("Registering model converter with class=" + modelConverterClass + " for input type=" + inputClass + " and output type=" + outputClass);
		ResolverEntry entry = this.resolverEntriesByInputClass.get(inputClass);

		if (entry == null) {
			entry = new ResolverEntry(this.application, inputClass);
			this.resolverEntriesByInputClass.put(inputClass, entry);
		}

		entry.addConverter(modelConverterClass, outputClass, isDefault);
	}

	////////////////////////
	// GETTERS/SETTERS
	////////////////

	public IResolverOutput getDefaultResolverOutputForInput(Object inputObject) {
		if (inputObject == null) {
			return null;
		}

		ResolverEntry entry = this.getResolverEntry(inputObject.getClass());

		return entry != null ? entry.getDefaultConverterOutput() : null;
	}

	private ResolverEntry getResolverEntry(Class<?> inputClass) {
		ResolverEntry entry = null;

		while (entry == null && inputClass != null) {
			entry = this.resolverEntriesByInputClass.get(inputClass);
			if (entry == null) {
				inputClass = inputClass.getSuperclass();
			}
		}

		return entry;
	}

	public IResolverOutput getResolverOutput(Class<?> inputClass, Class<?> outputClass) {
		ResolverEntry firstEntry = this.getResolverEntry(inputClass);

		if (firstEntry == null) {
			return null;
		}

		Deque<ResolverEntry> q = new ArrayDeque<>();
		Set<ResolverEntry> v = new HashSet<>();
		Map<ResolverEntry, ResolverEntry> childToParent = new HashMap<>();

		q.add(firstEntry);
		v.add(firstEntry);
		ResolverEntry outputEntry = null;

		while (!q.isEmpty()) {
			ResolverEntry t = q.poll();

			if (t.getConverterOutput(outputClass) != null) {
				outputEntry = t;
				break;
			} else {
				Class<?>[] outputClasses = t.getOutputClasses();
				for (Class<?> tOutputClass : outputClasses) {
					ResolverEntry tEntry = this.getResolverEntry(tOutputClass);

					if (tEntry != null) {
						if (!v.contains(tEntry)) {
							childToParent.put(tEntry, t);
							v.add(tEntry);
							q.add(tEntry);
						}
					}
				}
			}
		}

		if (outputEntry == null) {
			return null;
		}

		Class<?> currentOutputClass = outputClass;
		List<IResolverOutput> outputs = new ArrayList<>();
		while (outputEntry != null) {
			outputs.add(0, outputEntry.getConverterOutput(currentOutputClass));

			currentOutputClass = outputEntry.getInputClass();
			if (outputEntry != firstEntry) {
				outputEntry = childToParent.get(outputEntry);
			} else {
				outputEntry = null;
			}
		}

		IResolverOutput output = null;

		if (outputs.size() == 1) {
			output = outputs.get(0);
		} else {
			output = new ChainedResolverOutput(outputs);
		}

		return output;
	}

	public Class<?> getDefaultOutputTypeForInputObject(Object object) {
		if (object == null) {
			return null;
		}

		ResolverEntry entry = this.resolverEntriesByInputClass.get(object.getClass());

		return entry != null ? entry.getDefaultConverterOutput().getOutputClass() : null;
	}

	public Class<?>[] getManagedOutputTypesForInputObject(Object object) {
		if (object == null) {
			return null;
		}

		ResolverEntry entry = this.resolverEntriesByInputClass.get(object.getClass());

		return entry != null ? entry.getOutputClasses() : null;
	}
}