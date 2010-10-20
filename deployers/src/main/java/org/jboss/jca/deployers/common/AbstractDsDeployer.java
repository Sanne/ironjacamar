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
package org.jboss.jca.deployers.common;

import org.jboss.jca.common.api.metadata.common.CommonPool;
import org.jboss.jca.common.api.metadata.common.CommonTimeOut;
import org.jboss.jca.common.api.metadata.common.CommonValidation;
import org.jboss.jca.common.api.metadata.ds.DataSource;
import org.jboss.jca.common.api.metadata.ds.DataSources;
import org.jboss.jca.common.api.metadata.ds.XaDataSource;
import org.jboss.jca.common.api.metadata.ra.ConfigProperty;
import org.jboss.jca.common.api.metadata.ra.ConnectionDefinition;
import org.jboss.jca.common.api.metadata.ra.Connector;
import org.jboss.jca.common.api.metadata.ra.ResourceAdapter1516;
import org.jboss.jca.common.metadata.merge.Merger;
import org.jboss.jca.core.connectionmanager.ConnectionManager;
import org.jboss.jca.core.connectionmanager.ConnectionManagerFactory;
import org.jboss.jca.core.connectionmanager.pool.api.Pool;
import org.jboss.jca.core.connectionmanager.pool.api.PoolConfiguration;
import org.jboss.jca.core.connectionmanager.pool.api.PoolFactory;
import org.jboss.jca.core.connectionmanager.pool.api.PoolStrategy;
import org.jboss.jca.core.spi.mdr.MetadataRepository;
import org.jboss.jca.deployers.fungal.DsXmlDeployer;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.resource.spi.ManagedConnectionFactory;
import javax.resource.spi.TransactionSupport.TransactionSupportLevel;
import javax.transaction.TransactionManager;

import org.jboss.logging.Logger;

/**
 *
 * A AbstractDsDeployer.
 *
 * @author <a href="stefano.maestri@jboss.com">Stefano Maestri</a>
 *
 */
public abstract class AbstractDsDeployer
{
   /** log **/
   protected static Logger log = Logger.getLogger(DsXmlDeployer.class);

   /** jdbcLocal **/
   protected String jdbcLocal;

   /** jdbcXA **/
   protected String jdbcXA;

   /** The transaction manager */
   protected TransactionManager transactionManager;

   /** Metadata repository */
   protected MetadataRepository mdr;

   /**
    *
    * Create a new AbstractDsDeployer.
    *
    */
   public AbstractDsDeployer()
   {
      super();
   }

   /**
    * Set the name for the JDBC Local resource adapter
    * @param value The value
    */
   public void setJDBCLocal(String value)
   {
      jdbcLocal = value;
   }

   /**
    * Get the name for the JDBC Local resource adapter
    * @return The value
    */
   public String getJDBCLocal()
   {
      return jdbcLocal;
   }

   /**
    * Set the name for the JDBC XA resource adapter
    * @param value The value
    */
   public void setJDBCXA(String value)
   {
      jdbcXA = value;
   }

   /**
    * Get the name for the JDBC Xa resource adapter
    * @return The value
    */
   public String getJDBCXA()
   {
      return jdbcXA;
   }

   /**
    * Set the transaction manager
    * @param value The value
    */
   public void setTransactionManager(TransactionManager value)
   {
      transactionManager = value;
   }

   /**
    * Get the transaction manager
    * @return The value
    */
   public TransactionManager getTransactionManager()
   {
      return transactionManager;
   }

   /**
    * Set the metadata repository
    * @param value The value
    */
   public void setMetadataRepository(MetadataRepository value)
   {
      mdr = value;
   }

   /**
    * Get the metadata repository
    * @return The handle
    */
   public MetadataRepository getMetadataRepository()
   {
      return mdr;
   }

   /**
   *
   * create objects and inject value for this depployment. it is a general method returning a {@link CommonDeployment}
   * to be used to exchange objects needed to real injection in the container
   *
   * @param url url
   * @param deploymentName deploymentName
   * @param parentClassLoader cl
   * @param raDeployments resource adapters deployments
   * @param dataSources datasources metadata defined in xml
   * @return return the exchange POJO with value useful for injection in the container (fungal or AS)
   * @throws DeployException DeployException
   */
   protected CommonDeployment createObjectsAndInjectValue(URL url, String deploymentName, Set<String> raDeployments,
      DataSources dataSources, ClassLoader parentClassLoader) throws DeployException
   {
      try
      {
         URL urlJdbcLocal = null;
         URL urlJdbcXA = null;

         for (String s : raDeployments)
         {
            if (s.endsWith(jdbcLocal))
            {
               urlJdbcLocal = new URL(s);
            }
            else if (s.endsWith(jdbcXA))
            {
               urlJdbcXA = new URL(s);
            }
         }

         List<Object> cfs = new ArrayList<Object>(1);
         List<String> jndis = new ArrayList<String>(1);

         if (urlJdbcLocal != null)
         {
            List<DataSource> ds = dataSources.getDataSource();
            if (ds != null)
            {
               ClassLoader jdbcLocalDeploymentCl = getDeploymentCl(urlJdbcLocal);

               for (DataSource dataSource : ds)
               {
                  try
                  {
                     String jndiName = dataSource.getJndiName();

                     if (dataSource.isUseJavaContext() != null &&
                         dataSource.isUseJavaContext().booleanValue() &&
                         !jndiName.startsWith("java:/"))
                     {
                        jndiName = "java:/" + jndiName;
                     }

                     Object cf = deployDataSource(dataSource, jndiName, urlJdbcLocal, jdbcLocalDeploymentCl);

                     bindConnectionFactory(deploymentName, jndiName, cf);

                     cfs.add(cf);
                     jndis.add(jndiName);
                  }
                  catch (Throwable t)
                  {
                     log.error("Error during the deployment of " + dataSource.getJndiName(), t);
                  }
               }
            }
         }
         else
         {
            if (dataSources.getDataSource() != null && dataSources.getDataSource().size() > 0)
               log.error("Deployment of datasources disabled since jdbc-local.rar couldn't be found");
         }

         if (urlJdbcXA != null)
         {
            List<XaDataSource> xads = dataSources.getXaDataSource();
            if (xads != null)
            {
               ClassLoader jdbcXADeploymentCl = getDeploymentCl(urlJdbcXA);

               for (XaDataSource xaDataSource : xads)
               {
                  try
                  {
                     String jndiName = xaDataSource.getJndiName();

                     if (xaDataSource.isUseJavaContext() != null &&
                         xaDataSource.isUseJavaContext().booleanValue() &&
                         !jndiName.startsWith("java:/"))
                     {
                        jndiName = "java:/" + jndiName;
                     }

                     Object cf = deployXADataSource(xaDataSource, jndiName, urlJdbcXA, jdbcXADeploymentCl);

                     bindConnectionFactory(deploymentName, jndiName, cf);

                     cfs.add(cf);
                     jndis.add(jndiName);
                  }
                  catch (Throwable t)
                  {
                     log.error("Error during the deployment of " + xaDataSource.getJndiName(), t);
                  }
               }
            }
         }
         else
         {
            if (dataSources.getXaDataSource() != null && dataSources.getXaDataSource().size() > 0)
               log.error("Deployment of XA datasources disabled since jdbc-xa.rar couldn't be found");
         }

         return new CommonDeployment(url, deploymentName, true, null, 
                                     cfs.toArray(new Object[cfs.size()]), jndis.toArray(new String[jndis.size()]),
                                     null, null, null,
                                     parentClassLoader, log);
      }
      catch (Throwable t)
      {
         throw new DeployException("Deployment " + url.toExternalForm() + " failed", t);
      }
   }

   /**
    * Deploy a datasource
    * @param ds The datasource
    * @param jndiName The JNDI name
    * @param ra The resource adapter
    * @param cl The class loader
    * @return The connection factory
    * @exception Throwable Thrown if an error occurs during deployment
    */
   private Object deployDataSource(DataSource ds, String jndiName, URL ra, ClassLoader cl) throws Throwable
   {
      log.debug("DataSource=" + ds);

      Merger merger = new Merger();

      Connector md = mdr.getResourceAdapter(ra.toExternalForm());
      md = merger.mergeConnectorAndDs(ds, md);

      // Get the first connection definition as there is only one
      ResourceAdapter1516 ra1516 = (ResourceAdapter1516) md.getResourceadapter();
      List<ConnectionDefinition> cds = ra1516.getOutboundResourceadapter().getConnectionDefinitions();
      ConnectionDefinition cd = cds.get(0);

      // ManagedConnectionFactory
      ManagedConnectionFactory mcf = (ManagedConnectionFactory) initAndInject(cd.getManagedConnectionFactoryClass()
         .getValue(), cd.getConfigProperties(), cl);
      // Create the pool
      PoolConfiguration pc = createPoolConfiguration(ds.getPool(), ds.getTimeOut(), ds.getValidation());

      PoolFactory pf = new PoolFactory();
      Pool pool = pf.create(PoolStrategy.ONE_POOL, mcf, pc, false);

      // Connection manager properties
      Integer allocationRetry = null;
      Long allocationRetryWaitMillis = null;

      if (ds.getTimeOut() != null)
      {
         allocationRetry = ds.getTimeOut().getAllocationRetry();
         allocationRetryWaitMillis = ds.getTimeOut().getAllocationRetryWaitMillis();
      }

      // Select the correct connection manager
      TransactionSupportLevel tsl = TransactionSupportLevel.LocalTransaction;
      ConnectionManagerFactory cmf = new ConnectionManagerFactory();
      ConnectionManager cm = cmf.createTransactional(tsl, pool, allocationRetry, allocationRetryWaitMillis,
         getTransactionManager(), null, null, null, null, null);

      cm.setJndiName(jndiName);

      String poolName = null;
      if (ds.getPoolName() != null)
      {
         poolName = ds.getPoolName();
      }
 
      if (poolName == null)
         poolName = jndiName;

      pool.setName(poolName);

      // ConnectionFactory
      return mcf.createConnectionFactory(cm);
   }

   /**
    * Deploy an XA datasource
    * @param ds The datasource
    * @param jndiName The JNDI name
    * @param ra The resource adapter
    * @param cl The class loader
    * @return The connection factory
    * @exception Throwable Thrown if an error occurs during deployment
    */
   private Object deployXADataSource(XaDataSource ds, String jndiName, URL ra, ClassLoader cl) throws Throwable
   {
      log.debug("XaDataSource=" + ds);

      Merger merger = new Merger();

      Connector md = mdr.getResourceAdapter(ra.toExternalForm());
      md = merger.mergeConnectorAndDs(ds, md);

      // Get the first connection definition as there is only one
      ResourceAdapter1516 ra1516 = (ResourceAdapter1516) md.getResourceadapter();
      List<ConnectionDefinition> cds = ra1516.getOutboundResourceadapter().getConnectionDefinitions();
      ConnectionDefinition cd = cds.get(0);

      // ManagedConnectionFactory
      ManagedConnectionFactory mcf = (ManagedConnectionFactory) initAndInject(cd.getManagedConnectionFactoryClass()
         .getValue(), cd.getConfigProperties(), cl);
      // Create the pool
      PoolConfiguration pc = createPoolConfiguration(ds.getXaPool(), ds.getTimeOut(), ds.getValidation());

      Boolean noTxSeparatePool = Boolean.FALSE;

      if (ds.getXaPool() != null && ds.getXaPool().isNoTxSeparatePool() != null)
         noTxSeparatePool = ds.getXaPool().isNoTxSeparatePool();

      PoolFactory pf = new PoolFactory();
      Pool pool = pf.create(PoolStrategy.ONE_POOL, mcf, pc, noTxSeparatePool.booleanValue());

      // Connection manager properties
      Integer allocationRetry = null;
      Long allocationRetryWaitMillis = null;
      Boolean interleaving = null;
      Integer xaResourceTimeout = null;
      Boolean isSameRMOverride = null;
      Boolean wrapXAResource = null;
      Boolean padXid = null;

      if (ds.getTimeOut() != null)
      {
         allocationRetry = ds.getTimeOut().getAllocationRetry();
         allocationRetryWaitMillis = ds.getTimeOut().getAllocationRetryWaitMillis();
         xaResourceTimeout = ds.getTimeOut().getXaResourceTimeout();
      }

      if (ds.getXaPool() != null)
      {
         interleaving = ds.getXaPool().isInterleaving();
         isSameRMOverride = ds.getXaPool().isSameRmOverride();
         wrapXAResource = ds.getXaPool().isWrapXaDataSource();
         padXid = ds.getXaPool().isPadXid();
      }

      // Select the correct connection manager
      TransactionSupportLevel tsl = TransactionSupportLevel.XATransaction;
      ConnectionManagerFactory cmf = new ConnectionManagerFactory();
      ConnectionManager cm = cmf.createTransactional(tsl, pool, allocationRetry, allocationRetryWaitMillis,
         getTransactionManager(), interleaving, xaResourceTimeout, isSameRMOverride, wrapXAResource, padXid);

      cm.setJndiName(jndiName);

      String poolName = null;
      if (ds.getPoolName() != null)
      {
         poolName = ds.getPoolName();
      }
 
      if (poolName == null)
         poolName = jndiName;

      pool.setName(poolName);

      // ConnectionFactory
      return mcf.createConnectionFactory(cm);
   }

   /**
    * Create an instance of the pool configuration based on the input
    * @param pp The pool parameters
    * @param tp The timeout parameters
    * @param vp The validation parameters
    * @return The configuration
    */
   private PoolConfiguration createPoolConfiguration(CommonPool pp, CommonTimeOut tp, CommonValidation vp)
   {
      PoolConfiguration pc = new PoolConfiguration();

      if (pp != null)
      {
         if (pp.getMinPoolSize() != null)
            pc.setMinSize(pp.getMinPoolSize().intValue());

         if (pp.getMaxPoolSize() != null)
            pc.setMaxSize(pp.getMaxPoolSize().intValue());

         if (pp.isPrefill() != null)
            pc.setPrefill(pp.isPrefill());

         if (pp.isUseStrictMin() != null)
            pc.setStrictMin(pp.isUseStrictMin());
      }

      if (tp != null)
      {
         if (tp.getBlockingTimeoutMillis() != null)
            pc.setBlockingTimeout(tp.getBlockingTimeoutMillis().longValue());

         if (tp.getIdleTimeoutMinutes() != null)
            pc.setIdleTimeout(tp.getIdleTimeoutMinutes().longValue());
      }

      if (vp != null)
      {
         if (vp.isBackgroundValidation() != null)
            pc.setBackgroundValidation(vp.isBackgroundValidation().booleanValue());

         if (vp.getBackgroundValidationMinutes() != null)
            pc.setBackgroundValidationMinutes(vp.getBackgroundValidationMinutes().intValue());

         if (vp.isUseFastFail() != null)
            pc.setUseFastFail(vp.isUseFastFail());
      }

      return pc;
   }

   /**
    *
    * provide classloader of passed deployment url
    *
    * @param urlJdbcXA the url used to identify deployment
    * @return the classloader used by this deployment
    */
   protected abstract ClassLoader getDeploymentCl(URL urlJdbcXA);

   /**
    * Bind connection factory into JNDI
    * @param deployment The deployment name
    * @param cf The connection factory
    * @param jndi passed jndi name
    * @return The JNDI names bound
    * @exception Throwable Thrown if an error occurs
    */
   protected abstract String[] bindConnectionFactory(String deployment, String jndi, Object cf) throws Throwable;

   /**
    * Initialize and inject configuration properties
    * @param className The fully qualified class name
    * @param configs The configuration properties
    * @param cl The class loader
    * @return The object
    * @throws DeployException Thrown if the object cant be initialized
    */
   protected abstract Object initAndInject(String className, List<? extends ConfigProperty> configs, ClassLoader cl)
      throws DeployException;

}