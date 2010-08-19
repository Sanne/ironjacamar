/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.jca.common.metadata.ds;

import org.jboss.jca.common.api.metadata.ds.Security;
import org.jboss.jca.common.api.metadata.ds.Statement;
import org.jboss.jca.common.api.metadata.ds.TimeOut;
import org.jboss.jca.common.api.metadata.ds.TransactionIsolation;
import org.jboss.jca.common.api.metadata.ds.Validation;

import java.io.Serializable;

/**
 *
 * A DataSourceAbstractImpl.
 *
 * @author <a href="stefano.maestri@jboss.com">Stefano Maestri</a>
 *
 */
public abstract class DataSourceAbstractImpl implements Serializable
{

   /** The serialVersionUID */
   private static final long serialVersionUID = -5612846950298960159L;

   /**
   * minPoolSize
   */
   protected final Integer minPoolSize;

   /**
   * maxPoolSize
   */
   protected final Integer maxPoolSize;

   /**
   * prefill
   */
   protected final boolean prefill;

   /**
   * transactionIsolation
   */
   protected final TransactionIsolation transactionIsolation;

   /**
   * timeOut
   */
   protected final TimeOut timeOut;

   /**
   * security
   */
   protected final Security security;

   /**
   * statement
   */
   protected final Statement statement;

   /**
   * validation
   */
   protected final Validation validation;

   /**
   * urlDelimiter
   */
   protected final String urlDelimiter;

   /**
   * urlSelectorStrategyClassName
   */
   protected final String urlSelectorStrategyClassName;

   /**
   * useJavaContext
   */
   protected final boolean useJavaContext;

   /**
   * poolName
   */
   protected final String poolName;

   /**
   * enabled
   */
   protected final boolean enabled;

   /**
   * jndiName
   */
   protected final String jndiName;

   /**
    * Create a new DataSourceAbstractImpl.
    *
    * @param minPoolSize minPoolSize
    * @param maxPoolSize maxPoolSize
    * @param prefill prefill
    * @param transactionIsolation transactionIsolation
    * @param timeOut timeOut
    * @param security security
    * @param statement statement
    * @param validation validation
    * @param urlDelimiter urlDelimiter
    * @param urlSelectorStrategyClassName urlSelectorStrategyClassName
    * @param useJavaContext useJavaContext
    * @param poolName poolName
    * @param enabled enabled
    * @param jndiName jndiName
    */
   protected DataSourceAbstractImpl(Integer minPoolSize, Integer maxPoolSize, boolean prefill,
          TransactionIsolation transactionIsolation,
         TimeOut timeOut,
         Security security, Statement statement, Validation validation,
         String urlDelimiter, String urlSelectorStrategyClassName, boolean useJavaContext,
         String poolName, boolean enabled, String jndiName)
   {
      super();
      this.minPoolSize = minPoolSize;
      this.maxPoolSize = maxPoolSize;
      this.prefill = prefill;
      this.transactionIsolation = transactionIsolation;
      this.timeOut = timeOut;
      this.security = security;
      this.statement = statement;
      this.validation = validation;
      this.urlDelimiter = urlDelimiter;
      this.urlSelectorStrategyClassName = urlSelectorStrategyClassName;
      this.useJavaContext = useJavaContext;
      this.poolName = poolName;
      this.enabled = enabled;
      this.jndiName = jndiName;
   }

   /**
    * Get the minPoolSize.
    *
    * @return the minPoolSize.
    */

   public final Integer getMinPoolSize()
   {
      return minPoolSize;
   }

   /**
    * Get the maxPoolSize.
    *
    * @return the maxPoolSize.
    */

   public final Integer getMaxPoolSize()
   {
      return maxPoolSize;
   }

   /**
    * Get the prefill.
    *
    * @return the prefill.
    */

   public final boolean isPrefill()
   {
      return prefill;
   }

   /**
    * Get the transactionIsolation.
    *
    * @return the transactionIsolation.
    */

   public final TransactionIsolation getTransactionIsolation()
   {
      return transactionIsolation;
   }

   /**
    * Get the timeOut
    *
    * @return the timeOut.
    */

   public final TimeOut getTimeOut()
   {
      return timeOut;
   }

   /**
    * Get the security.
    *
    * @return the security.
    */

   public final Security getSecurity()
   {
      return security;
   }

   /**
    * Get the validation.
    *
    * @return the validation.
    */

   public final Validation getValidation()
   {
      return validation;
   }

   /**
    * Get the useJavaContext.
    *
    * @return the useJavaContext.
    */

   public final boolean isUseJavaContext()
   {
      return useJavaContext;
   }

   /**
    * Get the poolName.
    *
    * @return the poolName.
    */

   public final String getPoolName()
   {
      return poolName;
   }

   /**
    * Get the enabled.
    *
    * @return the enabled.
    */

   public final boolean isEnabled()
   {
      return enabled;
   }

   /**
    * Get the jndiName.
    *
    * @return the jndiName.
    */

   public final String getJndiName()
   {
      return jndiName;
   }

}