package il.ac.technion.cs.sd.book.app;

import com.google.inject.Guice;
import com.google.inject.Injector;
import db_utils.DataBase;
import db_utils.DataBaseFactory;
import il.ac.technion.cs.sd.book.ext.LineStorageModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;

/**
 * Created by Nadav on 17-May-17.
 */
public class BookScoreReaderImpl implements BookScoreReader {
    private final DataBase DB;

    public BookScoreReaderImpl() {
        Injector injector = Guice.createInjector(new LineStorageModule());
        DataBaseFactory bdf = injector.getInstance(DataBaseFactory.class);
        Integer num_of_keys=2;
        List<String> names_of_columns = new ArrayList<>();
        names_of_columns.add("Reviewer");
        names_of_columns.add("Book");
        names_of_columns.add("Score");



        DB = bdf.setNames_of_columns(names_of_columns)
                .setNum_of_keys(num_of_keys)
                .build();
    }


    @Override
    public boolean gaveReview(String reviewerId, String bookId) {
        return false;
    }

    @Override
    public OptionalDouble getScore(String reviewerId, String bookId) {
        return null;
    }

    @Override
    public List<String> getReviewedBooks(String reviewerId) {
        return null;
    }

    @Override
    public Map<String, Integer> getAllReviewsByReviewer(String reviewerId) {
        return null;
    }

    @Override
    public OptionalDouble getScoreAverageForReviewer(String reviewerId) {
        return null;
    }

    @Override
    public List<String> getReviewers(String bookId) {
        return null;
    }

    @Override
    public Map<String, Integer> getReviewsForBook(String bookId) {
        return null;
    }

    @Override
    public OptionalDouble getAverageReviewScoreForBook(String bookId) {
        return null;
    }
}
