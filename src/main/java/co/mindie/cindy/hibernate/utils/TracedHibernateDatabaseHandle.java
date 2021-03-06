/////////////////////////////////////////////////
// Project : WSFramework
// Package : co.mindie.wsframework.database
// TracedHibernateDatabaseHandle.java
//
// Author : Simon CORSIN <simoncorsin@gmail.com>
// File created on Jul 7, 2014 at 10:30:54 AM
////////

package co.mindie.cindy.hibernate.utils;

import co.mindie.cindy.hibernate.database.handle.HibernateDatabaseHandle;
import org.joda.time.DateTime;

public class TracedHibernateDatabaseHandle {

	////////////////////////
	// VARIABLES
	////////////////

	private HibernateDatabaseHandle handle;
	private DateTime createdDate;
	private StackTraceElement[] stackTrace;

	////////////////////////
	// CONSTRUCTORS
	////////////////

	public TracedHibernateDatabaseHandle(HibernateDatabaseHandle handle, StackTraceElement[] stackTrace) {
		this.handle = handle;
		this.createdDate = DateTime.now();
		this.stackTrace = stackTrace;
	}

	////////////////////////
	// METHODS
	////////////////

	////////////////////////
	// GETTERS/SETTERS
	////////////////

	public HibernateDatabaseHandle getHandle() {
		return this.handle;
	}

	public DateTime getCreatedDate() {
		return this.createdDate;
	}

	public StackTraceElement[] getStackTrace() {
		return this.stackTrace;
	}
}
