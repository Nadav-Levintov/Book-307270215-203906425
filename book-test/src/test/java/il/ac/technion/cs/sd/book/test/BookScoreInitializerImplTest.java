package il.ac.technion.cs.sd.book.test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import db_utils.DataBaseModule;
import il.ac.technion.cs.sd.book.app.BookScoreInitializerImpl;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.File;
import java.util.Scanner;

import static org.junit.Assert.fail;

/**
 * Created by Nadav on 15-May-17.
 */
public class BookScoreInitializerImplTest {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(30);

    @Test
    public void setup() throws Exception {
        Injector injector= Guice.createInjector(new DataBaseModule(),new MockedLineStorageModule(), new BookScoreModule());
        BookScoreInitializerImpl imp= injector.getInstance(BookScoreInitializerImpl.class);

        String fileContents =
                new Scanner(new File(BookScoreInitializerImplTest.class.getResource("small.xml").getFile())).useDelimiter("\\Z").next();
        try {
            imp.setup(fileContents);
        }catch (Exception e)
        {
            fail("Should not throw any exceptions");
        }

    }

}