package articles.java;

public class Welcome
{
   private WelcomeImpl impl = new WelcomeImpl();

   public String toString()
   {
      return impl.toString();
   }
}
