/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.query.sqm.tree.expression.function;

import org.hibernate.query.sqm.NodeBuilder;
import org.hibernate.query.sqm.consume.spi.SemanticQueryWalker;
import org.hibernate.query.sqm.tree.AbstractSqmNode;
import org.hibernate.query.sqm.tree.SqmTypedNode;
import org.hibernate.query.sqm.tree.SqmVisitableNode;
import org.hibernate.query.sqm.tree.expression.AbstractSqmExpression;
import org.hibernate.sql.ast.produce.metamodel.spi.ExpressableType;

/**
 * @author Gavin King
 */
public class SqmStar extends AbstractSqmExpression<Object> {

	public SqmStar(NodeBuilder builder) {
		super( null, builder );
	}

	@Override
	public <X> X accept(SemanticQueryWalker<X> walker) {
		return walker.visitStar(this);
	}
}
