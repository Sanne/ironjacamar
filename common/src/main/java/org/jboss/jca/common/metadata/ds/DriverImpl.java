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

import org.jboss.jca.common.api.metadata.ds.Driver;
import org.jboss.jca.common.api.validator.ValidateException;

/**
 *
 * A DriverImpl.
 *
 * @author <a href="stefano.maestri@jboss.com">Stefano Maestri</a>
 *
 */
public class DriverImpl implements Driver
{

   /** The serialVersionUID */
   private static final long serialVersionUID = 6228505574424288182L;

   private final String name;

   private final Integer minorVersion;

   private final Integer majorVersion;


   private final String module;

   private final String driverClass;

   private final String xaDataSourceClass;

   /**
    * Create a new DriverImpl.
    *
    * @param name name
    * @param majorVersion majorVersion
    * @param minorVersion minorVersion
    * @param module module
    * @param driverClass driverClass
    * @param xaDataSourceClass xaDataSourceClass
    * @throws ValidateException in case name is not specified
    */
   public DriverImpl(String name, Integer majorVersion, Integer minorVersion, String module, String driverClass,
      String xaDataSourceClass)
      throws ValidateException
   {
      super();
      this.name = name;
      this.majorVersion = majorVersion;
      this.minorVersion = minorVersion;
      this.module = module;
      this.driverClass = driverClass;
      this.xaDataSourceClass = xaDataSourceClass;
      this.validate();
   }

   /**
    * Get the name.
    *
    * @return the name.
    */
   @Override
   public final String getName()
   {
      return name;
   }

   /**
    * Get the version.
    *
    * @return the version.
    */

   /**
    * Get the module.
    *
    * @return the module.
    */
   @Override
   public final String getModule()
   {
      return module;
   }

   /**
    * Get the driverClass.
    *
    * @return the driverClass.
    */
   @Override
   public final String getDriverClass()
   {
      return driverClass;
   }

   /**
    * Get the xsDataSourceClass.
    *
    * @return the xsDataSourceClass.
    */
   @Override
   public final String getXaDataSourceClass()
   {
      return xaDataSourceClass;
   }

   @Override
   public void validate() throws ValidateException
   {
      if (this.name == null || this.name.trim().length() == 0)
         throw new ValidateException("name (xml attribute " + Attribute.NAME + ") is required in " +
                                     this.getClass().getCanonicalName());

   }

   @Override
   public Integer getMajorVersion()
   {
      return majorVersion;
   }

   @Override
   public Integer getMinorVersion()
   {
      return minorVersion;
   }

   @Override
   public String toString()
   {
      return "DriverImpl [name=" + name + ", minorVersion=" + minorVersion + ", majorVersion=" + majorVersion +
             ", module=" + module + ", driverClass=" + driverClass + ", xaDataSourceClass=" + xaDataSourceClass + "]";
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((driverClass == null) ? 0 : driverClass.hashCode());
      result = prime * result + ((majorVersion == null) ? 0 : majorVersion.hashCode());
      result = prime * result + ((minorVersion == null) ? 0 : minorVersion.hashCode());
      result = prime * result + ((module == null) ? 0 : module.hashCode());
      result = prime * result + ((name == null) ? 0 : name.hashCode());
      result = prime * result + ((xaDataSourceClass == null) ? 0 : xaDataSourceClass.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (!(obj instanceof DriverImpl))
         return false;
      DriverImpl other = (DriverImpl) obj;
      if (driverClass == null)
      {
         if (other.driverClass != null)
            return false;
      }
      else if (!driverClass.equals(other.driverClass))
         return false;
      if (majorVersion == null)
      {
         if (other.majorVersion != null)
            return false;
      }
      else if (!majorVersion.equals(other.majorVersion))
         return false;
      if (minorVersion == null)
      {
         if (other.minorVersion != null)
            return false;
      }
      else if (!minorVersion.equals(other.minorVersion))
         return false;
      if (module == null)
      {
         if (other.module != null)
            return false;
      }
      else if (!module.equals(other.module))
         return false;
      if (name == null)
      {
         if (other.name != null)
            return false;
      }
      else if (!name.equals(other.name))
         return false;
      if (xaDataSourceClass == null)
      {
         if (other.xaDataSourceClass != null)
            return false;
      }
      else if (!xaDataSourceClass.equals(other.xaDataSourceClass))
         return false;
      return true;
   }

}

