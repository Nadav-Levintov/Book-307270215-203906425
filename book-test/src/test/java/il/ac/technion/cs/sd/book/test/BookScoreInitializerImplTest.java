package il.ac.technion.cs.sd.book.test;

import il.ac.technion.cs.sd.book.app.BookScoreInitializerImpl;
import org.junit.Test;

import java.io.File;
import java.util.Scanner;

/**
 * Created by Nadav on 15-May-17.
 */
public class BookScoreInitializerImplTest {
    @Test
    public void setup() throws Exception {
        BookScoreInitializerImpl imp= new BookScoreInitializerImpl();
        String fileContents =
                new Scanner(new File(BookScoreInitializerImplTest.class.getResource("small.xml").getFile())).useDelimiter("\\Z").next();
        imp.setup(fileContents);
    }

}