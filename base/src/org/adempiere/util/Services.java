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
package org.adempiere.util;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.compiere.util.CLogger;

/**
 * This registry allows it to separate API interfaces from their implementation
 * 
 * @author Tobias Schoeneberg, metas GmbH
 */ 
public class Services
{
	private final static CLogger logger = CLogger.getCLogger(Services.class);

	private static Map<Class<? extends ISingletonService>, Object> services = new HashMap<Class<? extends ISingletonService>, Object>();

	private static boolean isAutodetectServices = false;

	public static void setAutodetectServices(boolean enable)
	{
		isAutodetectServices = enable;
	}

	public static boolean isAutodetectServices()
	{
		return isAutodetectServices;
	}

	@SuppressWarnings("unchecked")
	public static <T extends ISingletonService> T get(final Class<T> clazz)
	{
		T service = (T)services.get(clazz);
		if (service != null)
			return service;

		//
		// Auto-detect service class:
		if (isAutodetectServices)
		{
			String serviceClassname = clazz.getPackage().getName() + ".impl.";
			if (clazz.getSimpleName().startsWith("I"))
				serviceClassname += clazz.getSimpleName().substring(1);
			else
				serviceClassname += clazz.getSimpleName();
			try
			{
				service = (T)Thread.currentThread().getContextClassLoader().loadClass(serviceClassname).newInstance();
				registerService(clazz, service);
				logger.info("Autodetected service for " + clazz + ": " + service.getClass());
			}
			catch (Exception e)
			{
				logger.log(Level.WARNING, "Cannot load autodetected class "+serviceClassname, e);
			}
		}

		if (service == null)
		{
			logger.saveError("No service is registered for " + clazz, "");
		}
		return service;
	}

	public static boolean isRegistered(final Class<?> clazz)
	{
		return services.get(clazz) != null;
	}

	public static <T extends ISingletonService> void registerService(final Class<T> clazz, final T service)
	{
		logger.info("Registering service " + service + " (class " + service.getClass().getName() + ") for " + clazz);

		if (!clazz.isInterface())
		{
			throw new IllegalArgumentException("Parameter 'clazz' must be an interface class. clazz is" + clazz.getName());
		}
		if (!clazz.isAssignableFrom(service.getClass()))
		{
			throw new IllegalArgumentException("Service " + service + " must implement interface " + clazz);
		}

		services.put(clazz, service);
	}

	/**
	 * Clears the service registry. Intended use between unit tests.
	 */
	public static void clear()
	{
		services.clear();
	}
}
