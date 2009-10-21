import java.io.*;

public class MyClassLoader extends ClassLoader
{
   public MyClassLoader( ClassLoader parent )
   {
      super( parent );
   }

   protected Class findClass(String name)
                   throws ClassNotFoundException
   {
      try
      {
         byte[] classData = myDataLoad( name );

         return defineClass( name, classData, 0, classData.length );
      }
      catch ( Exception ex )
      {
         throw new ClassNotFoundException();
      }
   }

   private byte[] myDataLoad( String name ) throws Exception
   {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      InputStream is = getClass().getResourceAsStream( name + ".clazz" );
      if ( is != null )
      {
         int nextByte;
         while ( ( nextByte = is.read() ) != -1 )
         {
            bos.write( (byte) nextByte );
         }
      } 
      return bos.toByteArray();
   }
}
