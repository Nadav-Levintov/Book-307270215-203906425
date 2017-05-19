package il.ac.technion.cs.sd.book.app;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import db_utils.DataBase;
import db_utils.DataBaseFactory;
import il.ac.technion.cs.sd.book.ext.LineStorageModule;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by Nadav on 17-May-17.
 */
public class BookScoreReaderImpl implements BookScoreReader {
    private final DataBase DB;

    @Inject
    public BookScoreReaderImpl(DataBaseFactory dataBaseFactory) {

        Integer num_of_keys=2;
        List<String> names_of_columns = new ArrayList<>();
        names_of_columns.add("Reviewer");
        names_of_columns.add("Book");
        names_of_columns.add("Score");



        this.DB = dataBaseFactory.setNames_of_columns(names_of_columns)
                .setNum_of_keys(num_of_keys)
                .build();
    }


    @Override
    public boolean gaveReview(String reviewerId, String bookId) {
        OptionalInt key_index = DB.get_num_of_column("Reviewer");
        List<String> reviews = null;
        reviews = DB.get_lines_for_key(reviewerId,key_index.getAsInt());
        Boolean res = reviews.stream()
                                .anyMatch(line -> line.substring(0,line.indexOf(',')).compareTo(bookId) == 0);

        return res;
    }

    @Override
    public OptionalDouble getScore(String reviewerId, String bookId) {
        if(!this.gaveReview(reviewerId,bookId))
        {
            return OptionalDouble.empty();
        }
        List<String> keys = new ArrayList<>();
        keys.add(reviewerId);
        keys.add(bookId);
        Optional<String> score=Optional.empty();
        score= DB.get_val_from_column_by_name(keys,"Score");

        return OptionalDouble.of(Double.parseDouble(score.get()));


    }

    @Override
    public List<String> getReviewedBooks(String reviewerId) {
        OptionalInt key_index = DB.get_num_of_column("Reviewer");
        List<String> reviews = null;
        reviews = DB.get_lines_for_key(reviewerId,key_index.getAsInt());

        List<String> res=reviews.stream()
                .map(line -> line.substring(0,line.indexOf(',')))
                .sorted()
                .collect(Collectors.toList());

        return res;

    }

    @Override
    public Map<String, Integer> getAllReviewsByReviewer(String reviewerId) {

        OptionalInt key_index = DB.get_num_of_column("Reviewer");
        List<String> reviews = null;
        reviews = DB.get_lines_for_key(reviewerId,key_index.getAsInt());

        Map<String,Integer> res=reviews.stream()
                .map(line -> line.split(","))
                .collect(Collectors.toMap(arr -> arr[0],arr -> Integer.parseInt(arr[1])));

        return res;
    }

    @Override
    public OptionalDouble getScoreAverageForReviewer(String reviewerId) {
        OptionalInt key_index = DB.get_num_of_column("Reviewer");
        List<String> reviews = null;
        reviews = DB.get_lines_for_key(reviewerId,key_index.getAsInt());

        List<String> res=reviews.stream()
                .map(line ->  line.split(",")[1] )
                .collect(Collectors.toList());

        if(res.isEmpty())
        {
            return OptionalDouble.empty();
        }

        Double sum = new Double(0);
        for (String val: res
             ) {
            sum+=Double.parseDouble(val);
        }

        Double avg = new Double(sum/res.size());


        return OptionalDouble.of(avg);
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
