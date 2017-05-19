package il.ac.technion.cs.sd.book.test;

import com.google.inject.*;
import db_utils.DataBaseModule;
import il.ac.technion.cs.sd.book.app.BookScoreInitializer;
import il.ac.technion.cs.sd.book.app.BookScoreInitializerImpl;
import il.ac.technion.cs.sd.book.ext.LineStorageModule;
import org.junit.Test;

import java.io.File;
import java.util.Scanner;

/**
 * Created by Nadav on 15-May-17.
 */
public class BookScoreInitializerImplTest {
    @Test
    public void setup() throws Exception {
        Injector injector= Guice.createInjector(new DataBaseModule(),new LineStorageModule());
        BookScoreInitializerImpl imp= injector.getInstance(BookScoreInitializerImpl.class);

        String fileContents =
                new Scanner(new File(BookScoreInitializerImplTest.class.getResource("small.xml").getFile())).useDelimiter("\\Z").next();
        imp.setup(fileContents);


    }

}