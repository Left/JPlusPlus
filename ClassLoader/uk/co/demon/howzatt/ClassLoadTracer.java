/*
NAME
    ClassLoadTracer

DESCRIPTION
    Class load tracer.


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

    $Id: ClassLoadTracer.java,v 1.1 2005/06/21 22:57:55 Roger Exp $
*/

package uk.co.demon.howzatt;

/**
 * Class load tracer.
 *
 * This class is installed by ClassLoadInject which my be loaded by setting the
 * property "java.system.class.loader" to the class name (uk.co.demon.howzatt.ClassLoadInject).
 *
 * All known classes are then logged to 'ClassLoad.txt' as soon as their class loader is known.
 * This can assist in resolving class loading issues, particularly inside Tomcat.
 *
 * @see ClassLoadInject
 */
public class ClassLoadTracer extends ClassLoader
{
   static
   {
      System.out.println( "Loaded 'ClassLoadTracer'" );
   }

   /**
    * Creates a new class loader using the bootstrap parent class loader for delegation.
    *
    * The constructor is designed to be invoked by ClassLoadInject.
    */
   public ClassLoadTracer( ClassLoader parent )
   {
      super( null );

      try
      {
         streamer = new java.io.PrintStream( new java.io.FileOutputStream( fileName ) );
         System.out.println( "Created class loader logging to : " + fileName );
      }
      catch ( java.io.IOException ex )
      {
         streamer = System.out;
         streamer.println( "Can't open output file: " + ex );
      }

      checkLoader( parent );

      // Call our internal methods _before_ we are installed - this prevents ClassCircularityError exceptions later on.
      checkLoader();
   }

   /**
    * Loads the class with the specified name.  The default implementation
    * of this method searches for classes in the following order:
    *
    * <p><ol>
    *
    *   <li><p> Invoke {@link #findLoadedClass(String)} to check if the class
    *   has already been loaded.  </p></li>
    *
    *   <li><p> Invoke the {@link #loadClass(String) <tt>loadClass</tt>} method
    *   on the parent class loader.  If the parent is <tt>null</tt> the class
    *   loader built-in to the virtual machine is used, instead.  </p></li>
    *
    *   <li><p> Invoke the {@link #findClass(String)} method to find the
    *   class.  </p></li>
    *
    * </ol>
    *
    * <p> If the class was found using the above steps, and the
    * <tt>resolve</tt> flag is true, this method will then invoke the {@link
    * #resolveClass(Class)} method on the resulting <tt>Class</tt> object.
    *
    * <p> Subclasses of <tt>ClassLoader</tt> are encouraged to override {@link
    * #findClass(String)}, rather than this method.  </p>
    *
    * @param  name
    *         The name of the class
    *
    * @param  resolve
    *         If <tt>true</tt> then resolve the class
    *
    * @return  The resulting <tt>Class</tt> object
    *
    * @throws  ClassNotFoundException
    *          If the class could not be found
    */
   protected synchronized Class loadClass(String name,
                                          boolean resolve)
                   throws ClassNotFoundException
   {
      checkLoader();

      Class ret = super.loadClass( name, resolve );
      showClass( ret );
      return ret;
   }

   // Set of known loaders
   private static java.util.Set seenLoaders = new java.util.HashSet();

   // check we have captured the current loader
   private void checkLoader()
   {
      checkLoader( Thread.currentThread().getContextClassLoader() );
   }

   /**
    * Check the current loader is being traced by this instance.
    */
   public synchronized void checkLoader( ClassLoader currLoader )
   {
      for ( ; currLoader != null; currLoader = currLoader.getParent() )
      {
         if ( ! seenLoaders.add( currLoader ) )
            break;

         hookClassLoader( currLoader );
      }
   }

   // Add a hook to a class loader (using reflection).
   private void hookClassLoader( final ClassLoader currLoader )
   {
      try
      {
         java.lang.reflect.Field field = ClassLoader.class.getDeclaredField( "classes" );
         field.setAccessible( true );
         final java.util.Vector currClasses = (java.util.Vector)field.get( currLoader );
         field.set( currLoader, new java.util.Vector() {
            public void addElement( Object o ) {
               showClass( (Class)o );
               currClasses.addElement(o);
            }
         });

         logLoader( streamer, "Class loader", currLoader );

         if ( ! currClasses.isEmpty() )
         {
            streamer.println( "Existing classes:" );
            for ( java.util.Iterator iter = currClasses.iterator(); iter.hasNext(); )
            {
               showClass( (Class) iter.next() );
            }
         }
         streamer.println();
      }
      catch ( java.lang.Exception ex )
      {
         streamer.println( "Can't hook " + currLoader + ": " + ex );
      }
   }

   // log details for each class loader
   private static void logLoader( java.io.PrintStream printer, String title, ClassLoader loader )
   {
      printer.println( title + ": " + loader );

      String delim = "";
      for ( ; loader != null; loader = loader.getParent() )
      {
         printer.println( delim + loader.getClass().toString() );
         if ( delim == "" )
            delim = "-> ";
         delim = "  " + delim;
      }
   }

   // helper to show a single class
   private void showClass( Class theClass )
   {
      ClassLoader loader = theClass.getClassLoader();
      String loaderClass = ( loader == null ) ? "(bootstrap)" : loader.getClass().getName();
      streamer.println( loaderClass + " => " + theClass.getName() );
   }

   private java.io.PrintStream streamer; // output for messages
   private static String fileName = "ClassLoad.txt"; // default output filename
}
