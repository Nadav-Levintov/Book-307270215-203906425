package il.ac.technion.cs.sd.book.test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import db_utils.DataBaseModule;
import il.ac.technion.cs.sd.book.app.BookScoreReader;
import il.ac.technion.cs.sd.book.app.BookScoreReaderImpl;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Created by Nadav on 20-May-17.
 */
public class BookScoreReaderImplTest {

        @Rule
        public Timeout globalTimeout = Timeout.seconds(30);

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

        assertTrue(bookScoreReader.gaveReview("321","Foobar2"));
        assertTrue(bookScoreReader.gaveReview("123","Foobar"));
        assertTrue(bookScoreReader.gaveReview("123","Moobar"));
        assertTrue(bookScoreReader.gaveReview("123","Boobar"));
        assertFalse(bookScoreReader.gaveReview("Nadav","Foobar2"));
        assertFalse(bookScoreReader.gaveReview("123","Nadav"));
    }

    @Test
    public void getScore() throws Exception {
        BookScoreReader bookScoreReader = SetupAndBuildBookScoreReader("small.xml");

        assertEquals(OptionalDouble.of(5),bookScoreReader.getScore("123","Boobar"));
        assertEquals(OptionalDouble.of(10),bookScoreReader.getScore("123","Foobar"));
        assertEquals(OptionalDouble.of(3),bookScoreReader.getScore("123","Moobar"));
        assertEquals(OptionalDouble.empty(),bookScoreReader.getScore("123","Toobar"));
        assertEquals(OptionalDouble.empty(),bookScoreReader.getScore("543","Foobar"));
        assertEquals(OptionalDouble.of(32),bookScoreReader.getScore("321","Foobar2"));
        assertEquals(OptionalDouble.of(5),bookScoreReader.getScore("322","Boobar"));
    }

    @Test
    public void getReviewedBooks() throws Exception {
        BookScoreReader bookScoreReader = SetupAndBuildBookScoreReader("small.xml");

        List<String> list1 = new ArrayList<>();
        list1.add("Boobar");
        list1.add("Foobar");
        list1.add("Moobar");

        List<String> list2 = new ArrayList<>();
        list2.add("Foobar2");

        List<String> list3 = new ArrayList<>();
        list3.add("Boobar");

        assertEquals(list1,bookScoreReader.getReviewedBooks("123"));
        assertEquals(list2,bookScoreReader.getReviewedBooks("321"));
        assertEquals(list3,bookScoreReader.getReviewedBooks("322"));
        assertEquals(Collections.EMPTY_LIST,bookScoreReader.getReviewedBooks("456"));

    }

    @Test
    public void getAllReviewsByReviewer() throws Exception {
        BookScoreReader bookScoreReader = SetupAndBuildBookScoreReader("small.xml");

        List<String> list1 = new ArrayList<>();
        list1.add("Boobar,5");
        list1.add("Foobar,10");
        list1.add("Moobar,3");

        Map<String,Integer> map1 = list1.stream()
                .map(line -> line.split(","))
                .collect(Collectors.toMap(
                        arr -> arr[0],
                        arr -> Integer.parseInt(arr[1])));

        List<String> list2 = new ArrayList<>();
        list2.add("Foobar2,32");

        Map<String,Integer> map2 = list2.stream()
                .map(line -> line.split(","))
                .collect(Collectors.toMap(
                        arr -> arr[0],
                        arr -> Integer.parseInt(arr[1])));

        List<String> list3 = new ArrayList<>();
        list3.add("Boobar,5");

        Map<String,Integer> map3 = list3.stream()
                .map(line -> line.split(","))
                .collect(Collectors.toMap(
                        arr -> arr[0],
                        arr -> Integer.parseInt(arr[1])));

        assertEquals(map1,bookScoreReader.getAllReviewsByReviewer("123"));
        assertEquals(map2,bookScoreReader.getAllReviewsByReviewer("321"));
        assertEquals(map3,bookScoreReader.getAllReviewsByReviewer("322"));
        assertEquals(Collections.EMPTY_MAP,bookScoreReader.getAllReviewsByReviewer("456"));

    }

    @Test
    public void getScoreAverageForReviewer() throws Exception {
        BookScoreReader bookScoreReader = SetupAndBuildBookScoreReader("small.xml");

        Double avg1 = new Double(5+10+3)/3;

        Double avg2 = new Double(32);

        Double avg3 = new Double(5);

        assertEquals(OptionalDouble.of(avg1),bookScoreReader.getScoreAverageForReviewer("123"));
        assertEquals(OptionalDouble.of(avg2),bookScoreReader.getScoreAverageForReviewer("321"));
        assertEquals(OptionalDouble.of(avg3),bookScoreReader.getScoreAverageForReviewer("322"));
        assertEquals(OptionalDouble.empty(),bookScoreReader.getScoreAverageForReviewer("456"));
    }

    @Test
    public void getReviewers() throws Exception {

        BookScoreReader bookScoreReader = SetupAndBuildBookScoreReader("small.xml");

        List<String> list1 = new ArrayList<>();
        list1.add("123");
        list1.add("322");

        List<String> list2 = new ArrayList<>();
        list2.add("123");

        List<String> list3 = new ArrayList<>();
        list3.add("123");

        List<String> list4 = new ArrayList<>();
        list4.add("321");

        assertEquals(list1,bookScoreReader.getReviewers("Boobar"));
        assertEquals(list2,bookScoreReader.getReviewers("Foobar"));
        assertEquals(list3,bookScoreReader.getReviewers("Moobar"));
        assertEquals(list4,bookScoreReader.getReviewers("Foobar2"));
        assertEquals(Collections.EMPTY_LIST,bookScoreReader.getReviewedBooks("456"));
    }

    @Test
    public void getReviewsForBook() throws Exception {
        BookScoreReader bookScoreReader = SetupAndBuildBookScoreReader("small.xml");

        List<String> list1 = new ArrayList<>();
        list1.add("123,5");
        list1.add("322,5");

        Map<String,Integer> map1 = list1.stream()
                .map(line -> line.split(","))
                .collect(Collectors.toMap(
                        arr -> arr[0],
                        arr -> Integer.parseInt(arr[1])));

        List<String> list2 = new ArrayList<>();
        list2.add("123,10");

        Map<String,Integer> map2 = list2.stream()
                .map(line -> line.split(","))
                .collect(Collectors.toMap(
                        arr -> arr[0],
                        arr -> Integer.parseInt(arr[1])));

        List<String> list3 = new ArrayList<>();
        list3.add("123,3");

        Map<String,Integer> map3 = list3.stream()
                .map(line -> line.split(","))
                .collect(Collectors.toMap(
                        arr -> arr[0],
                        arr -> Integer.parseInt(arr[1])));

        List<String> list4 = new ArrayList<>();
        list4.add("321,32");

        Map<String,Integer> map4 = list4.stream()
                .map(line -> line.split(","))
                .collect(Collectors.toMap(
                        arr -> arr[0],
                        arr -> Integer.parseInt(arr[1])));

        assertEquals(map1,bookScoreReader.getReviewsForBook("Boobar"));
        assertEquals(map2,bookScoreReader.getReviewsForBook("Foobar"));
        assertEquals(map3,bookScoreReader.getReviewsForBook("Moobar"));
        assertEquals(map4,bookScoreReader.getReviewsForBook("Foobar2"));
        assertEquals(Collections.EMPTY_MAP,bookScoreReader.getReviewsForBook("456"));


    }

    @Test
    public void getAverageReviewScoreForBook() throws Exception {
        BookScoreReader bookScoreReader = SetupAndBuildBookScoreReader("small.xml");

        Double avg1 = new Double(5+5)/2;

        Double avg2 = new Double(10);

        Double avg3 = new Double(3);

        Double avg4 = new Double(32);

        assertEquals(OptionalDouble.of(avg1),bookScoreReader.getAverageReviewScoreForBook("Boobar"));
        assertEquals(OptionalDouble.of(avg2),bookScoreReader.getAverageReviewScoreForBook("Foobar"));
        assertEquals(OptionalDouble.of(avg3),bookScoreReader.getAverageReviewScoreForBook("Moobar"));
        assertEquals(OptionalDouble.of(avg4),bookScoreReader.getAverageReviewScoreForBook("Foobar2"));
        assertEquals(OptionalDouble.empty(),bookScoreReader.getAverageReviewScoreForBook("456"));

    }
}