/////////////////////////////////////////////////
// Project : WSFramework
// Package : co.mindie.wsframework.dao.impl.builtin
// SimpleHibernateDAO.java
//
// Author : Simon CORSIN <simoncorsin@gmail.com>
// File created on Jul 29, 2014 at 9:20:04 PM
////////

package co.mindie.cindy.dao.impl.builtin;

import java.io.Serializable;

import co.mindie.cindy.automapping.SearchScope;
import co.mindie.cindy.automapping.Wired;
import co.mindie.cindy.dao.impl.HibernateDAO;
import co.mindie.cindy.database.handle.HibernateDatabaseHandle;

public abstract class SimpleHibernateDAO<ElementType, PrimaryKey extends Serializable> extends HibernateDAO<ElementType, PrimaryKey> {

	////////////////////////
	// VARIABLES
	////////////////

	@Wired(searchScope = SearchScope.LOCAL) private HibernateDatabaseHandle databaseHandle;

	////////////////////////
	// CONSTRUCTORS
	////////////////

	public SimpleHibernateDAO(Class<ElementType> managedClass) {
		super(managedClass);
	}

	////////////////////////
	// METHODS
	////////////////

	@Override
	public void init() {
		super.init();

		this.setDatabaseHandle(this.databaseHandle);
	}

	////////////////////////
	// GETTERS/SETTERS
	////////////////

}