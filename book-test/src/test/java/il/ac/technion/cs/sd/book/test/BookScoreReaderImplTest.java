package il.ac.technion.cs.sd.book.test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import db_utils.DataBase;
import db_utils.DataBaseFactory;
import db_utils.DataBaseFactoryImpl;
import db_utils.DataBaseModule;
import il.ac.technion.cs.sd.book.app.BookScoreInitializerImpl;
import il.ac.technion.cs.sd.book.app.BookScoreReader;
import il.ac.technion.cs.sd.book.app.BookScoreReaderImpl;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.*;

/**
 * Created by Nadav on 20-May-17.
 */
public class BookScoreReaderImplTest {

    //@Rule
    //public Timeout globalTimeout = Timeout.seconds(30);

    public BookScoreReader SetupAndBuildBookScoreReader(String xml_file) throws IOException, SAXException, ParserConfigurationException {

        Injector injector= Guice.createInjector(new DataBaseModule(),new MockedLineStorageModule(), new FakeBookScoreModule());
        FakeBookScoreInitializerImpl fakeBookScoreInitializer= injector.getInstance(FakeBookScoreInitializerImpl.class);

        String fileContents =
                new Scanner(new File(BookScoreInitializerImplTest.class.getResource(xml_file).getFile())).useDelimiter("\\Z").next();
        fakeBookScoreInitializer.setup(fileContents);

        BookScoreReader bookScoreReader = new BookScoreReaderImpl(fakeBookScoreInitializer.get_DataBaseFactory());

        return bookScoreReader;
    }

    @Test
    public void gaveReview() throws Exception {
        BookScoreReader bookScoreReader = SetupAndBuildBookScoreReader("small.xml");

        assertTrue(bookScoreReader.gaveReview("321","Boobar"));
        assertTrue(bookScoreReader.gaveReview("123","Foobar"));
        assertTrue(bookScoreReader.gaveReview("123","Moobar"));
        assertTrue(bookScoreReader.gaveReview("123","Boobar"));
        assertFalse(bookScoreReader.gaveReview("Nadav","Foobar2"));
        assertFalse(bookScoreReader.gaveReview("123","Nadav"));
        assertTrue(bookScoreReader.gaveReview("321","Foobar2"));


    }

    @Test
    public void getScore() throws Exception {
    }

    @Test
    public void getReviewedBooks() throws Exception {
    }

    @Test
    public void getAllReviewsByReviewer() throws Exception {
    }

    @Test
    public void getScoreAverageForReviewer() throws Exception {
    }

    @Test
    public void getReviewers() throws Exception {
    }

    @Test
    public void getReviewsForBook() throws Exception {
    }

    @Test
    public void getAverageReviewScoreForBook() throws Exception {
    }

}