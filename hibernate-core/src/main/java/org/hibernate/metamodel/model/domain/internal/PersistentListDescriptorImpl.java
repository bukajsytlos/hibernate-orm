/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.metamodel.model.domain.internal;

import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.mapping.IndexedCollection;
import org.hibernate.mapping.Property;
import org.hibernate.metamodel.model.creation.spi.RuntimeModelCreationContext;
import org.hibernate.metamodel.model.domain.internal.collection.SqlAstHelper;
import org.hibernate.metamodel.model.domain.spi.AbstractPersistentCollectionDescriptor;
import org.hibernate.metamodel.model.domain.spi.AbstractPluralPersistentAttribute;
import org.hibernate.metamodel.model.domain.spi.CollectionElement;
import org.hibernate.metamodel.model.domain.spi.CollectionIndex;
import org.hibernate.metamodel.model.domain.spi.ManagedTypeDescriptor;
import org.hibernate.property.access.spi.PropertyAccess;
import org.hibernate.query.NavigablePath;
import org.hibernate.sql.results.internal.domain.collection.CollectionInitializerProducer;
import org.hibernate.sql.results.internal.domain.collection.ListInitializerProducer;
import org.hibernate.sql.results.spi.DomainResult;
import org.hibernate.sql.results.spi.DomainResultCreationState;
import org.hibernate.sql.results.spi.FetchParent;

/**
 * Hibernate's standard PersistentCollectionDescriptor implementor
 * for Lists
 *
 * @author Steve Ebersole
 */
public class PersistentListDescriptorImpl<O,E> extends AbstractPersistentCollectionDescriptor<O,List<E>, E> {
	private final boolean hasFormula;

	public PersistentListDescriptorImpl(
			Property bootProperty,
			ManagedTypeDescriptor runtimeContainer,
			RuntimeModelCreationContext context) {
		super( bootProperty, runtimeContainer, context );
		IndexedCollection collection = (IndexedCollection) bootProperty.getValue();
		hasFormula = collection.getIndex().hasFormula();
	}

	@Override
	protected CollectionInitializerProducer createInitializerProducer(
			NavigablePath navigablePath,
			FetchParent fetchParent,
			boolean selected,
			String resultVariable,
			LockMode lockMode,
			DomainResultCreationState creationState) {
		return new ListInitializerProducer(
				this,
				selected,
				// list-index
				SqlAstHelper.generateCollectionIndexDomainResult(
						navigablePath.append( CollectionIndex.NAVIGABLE_NAME ),
						getIndexDescriptor(),
						selected,
						null,
						creationState
				),
				// list-element
				SqlAstHelper.generateCollectionElementDomainResult(
						navigablePath.append( CollectionElement.NAVIGABLE_NAME ),
						getElementDescriptor(),
						selected,
						null,
						creationState
				)
		);
	}

	@Override
	protected AbstractPluralPersistentAttribute createAttribute(
			Property pluralProperty,
			PropertyAccess propertyAccess,
			RuntimeModelCreationContext creationContext) {
		return new ListAttributeImpl<>( this, pluralProperty, propertyAccess, creationContext );
	}

	@Override
	public boolean contains(Object collection, Object childObject) {
		return ( (List) collection ).contains( childObject );
	}

	@Override
	protected void doProcessQueuedOps(
			PersistentCollection collection,
			Object id,
			SharedSessionContractImplementor session) {
		// throw new NotYetImplementedFor6Exception( getClass() );
	}

	@Override
	protected boolean hasIndex() {
		return true;
	}

	@Override
	protected boolean indexContainsFormula(){
		return hasFormula;
	}

	@Override
	public Object indexOf(Object collection, Object element) {
		List list = (List) collection;
		for ( int i = 0; i < list.size(); i++ ) {
			//TODO: proxies!
			if ( list.get( i ) == element ) {
				return i;
			}
		}
		return null;
	}
}