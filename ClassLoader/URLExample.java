/**
 * This is a trivial example of a class loader.
 * It loads an object from a class on my own Web site.
 */
public class URLExample
{
   private static final String defaultURL = "http://www.howzatt.demon.co.uk/";
   private static final String defaultClass = "articles.java.Welcome";

   public static void main( String args[] ) throws Exception
   {
      final String targetURL = ( args.length < 1 ) ? defaultURL : args[0];
      final String targetClass = ( args.length < 2 ) ? defaultClass : args[1];

      // Step 1: create the URL class loader.
      System.out.println( "Creating class loader for: " + targetURL );
      java.net.URL[] urls = { new java.net.URL( targetURL ) };
      ClassLoader newClassLoader = new java.net.URLClassLoader( urls );
      Thread.currentThread().setContextClassLoader( newClassLoader );

      // Step 2: load the class and create an instance of it.
      System.out.println( "Loading: " + targetClass );
      Class urlClass = newClassLoader.loadClass( targetClass );
      Object obj = urlClass.newInstance();
      System.out.println( "Object is: \"" + obj.toString() + "\"" );

      // Step 3: check the URL of the loaded class.
      java.net.URL url = obj.getClass().getResource( "Welcome.class" );
      if ( url != null )
      {
         System.out.println( "URL used: " + url.toExternalForm() );
      }
   }
}
