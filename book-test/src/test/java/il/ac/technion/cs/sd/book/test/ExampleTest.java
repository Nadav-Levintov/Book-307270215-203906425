package il.ac.technion.cs.sd.book.test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import il.ac.technion.cs.sd.book.app.BookScoreInitializerImpl;
import il.ac.technion.cs.sd.book.app.BookScoreReader;
import il.ac.technion.cs.sd.book.ext.LineStorageModule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.OptionalDouble;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class ExampleTest {

  @Rule public Timeout globalTimeout = Timeout.seconds(30);

  private static BookScoreReader setupAndGetReader(String fileName) throws IOException, SAXException, ParserConfigurationException {
    String fileContents =
        new Scanner(new File(ExampleTest.class.getResource(fileName).getFile())).useDelimiter("\\Z").next();
    Injector injector = Guice.createInjector(new BookScoreModule(), new LineStorageModule());
    injector.getInstance(BookScoreInitializerImpl.class).setup(fileContents);
    return injector.getInstance(BookScoreReader.class);
  }

  @Test
  public void testSimple() throws Exception {
    BookScoreReader reader = setupAndGetReader("small.xml");
    assertEquals(Arrays.asList("Boobar", "Foobar", "Moobar"), reader.getReviewedBooks("123"));
    assertEquals(OptionalDouble.of(6.0), reader.getScoreAverageForReviewer("123"));
    assertEquals(OptionalDouble.of(10.0), reader.getAverageReviewScoreForBook("Foobar"));
  }
}
