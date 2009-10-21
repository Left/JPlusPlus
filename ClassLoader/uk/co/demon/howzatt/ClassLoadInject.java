/*
NAME
    ClassLoadInject

DESCRIPTION
    Class load injector.


COPYRIGHT
    Copyright (C) 2005 by Roger Orr <rogero@howzatt.demon.co.uk>

    This software is distributed in the hope that it will be useful, but
    without WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

    Permission is granted to anyone to make or distribute verbatim
    copies of this software provided that the copyright notice and
    this permission notice are preserved, and that the distributor
    grants the recipent permission for further distribution as permitted
    by this notice.

    Comments and suggestions are always welcome.
    Please report bugs to rogero@howzatt.demon.co.uk.

    $Id: ClassLoadInject.java,v 1.1 2005/06/21 22:57:54 Roger Exp $
*/

package uk.co.demon.howzatt;

/**
 * Class load injector.
 *
 * This class is designed to be install the class load tracer.
 *
 * This can be done by setting the property "java.system.class.loader" to the class name,
 * or by calling thr static method 'inject()'.
 *
 * All calls to the system class loader are then logged to a 'ClassLoadTracer' object.
 * This can assist in resolving class loading issues, particularly inside Tomcat.
 */
public class ClassLoadInject extends ClassLoader
{
   static
   {
      System.out.println( "Loaded 'ClassLoadInject'." );
   }

   /**
    * Creates a new class loader using the specified parent class loader for delegation.
    *
    * For this class the constructor is designed to be invoked by ClassLoader.getSystemClassLoader().
    * 
    * @param  parent
    *         The parent class loader
    *
    * @throws  SecurityException
    *          If a security manager exists and its
    *          <tt>checkCreateClassLoader</tt> method doesn't allow creation
    *          of a new class loader.
    */
   public ClassLoadInject( ClassLoader parent )
   {
      super( parent );

      inject( parent );
   }

   /**
    * This method injects a ClassLoadTracer object into the current class loader chain.
    *
    * @param parent the current active class loader
    * @return the new (or existing) tracer object.
    */
   public static synchronized ClassLoadTracer inject( ClassLoader parent )
   {
      // get the current topmost class loader -- apart from the native one of course.
      ClassLoader root = parent;
      while ( root.getParent() != null )
         root = root.getParent();

      if ( root instanceof ClassLoadTracer )
         return (ClassLoadTracer)root;

      ClassLoadTracer newRoot = new ClassLoadTracer( parent );

      // reflect on the topmost classloader to install the ClassLoadTracer ...
      try
      {
         // we want root->parent = newRoot;
         java.lang.reflect.Field field = ClassLoader.class.getDeclaredField( "parent" );
         field.setAccessible( true );
         field.set( root, newRoot );
      }
      catch ( Exception ex )
      {
         ex.printStackTrace();
         System.out.println( "Could not install ClassLoadTracer: " + ex );
      }
      return newRoot;
   }

   /**
    * This method injects a ClassLoadTracer object into the current class loader chain.
    *
    * @return the new (or existing) tracer object.
    */
   public static synchronized ClassLoadTracer inject()
   {
      return inject( getSystemClassLoader() );
   }
}
