******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) metas GmbH All Rights Reserved.                              *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *                                                                            *
 * @author Tobias Schoenebrg, metas GmbH                                      *
 *  			                                                              *
 *****************************************************************************/
package org.adempiere.util.trxConstraints.api;

import java.sql.Savepoint;

import org.adempiere.util.ISingletonService;
import org.compiere.util.Trx;

/**
 * Service contains methods to be called from {@link Trx} on certain events to check against {@link ITrxConstraints}.
 * Implementers of this service are responsible for the enforcement of the transaction constraints
 * 
 * @see org.compiere.util.DB#getConstraints()
 * @see ITrxConstraints
 */
public interface IOpenTrxBL extends ISingletonService
{
	void onCommit(Trx trx);

	void onClose(Trx trx);

	void onNewTrx(Trx trx);

	void onRollback(Trx trx);

	void onSetSavepoint(Trx trx, Savepoint savepoint);

	void onReleaseSavepoint(Trx trx, Savepoint savepoint);
}
