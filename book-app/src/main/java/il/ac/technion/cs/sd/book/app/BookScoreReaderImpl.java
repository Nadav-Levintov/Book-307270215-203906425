package il.ac.technion.cs.sd.book.app;

import com.google.inject.Inject;
import db_utils.DataBase;
import db_utils.DataBaseFactory;

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
        List<String> reviews = getReviews(reviewerId,"Reviewer");
        Boolean res = reviews.stream()
                                .anyMatch(line -> line
                                        .substring(0,line.indexOf(','))
                                        .compareTo(bookId) == 0);

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
        List<String> reviews = getReviews(reviewerId,"Reviewer");

        List<String> res=reviews.stream()
                .map(line -> line
                        .substring(0,line.indexOf(',')))
                .sorted()
                .collect(Collectors.toList());

        return res;

    }

    @Override
    public Map<String, Integer> getAllReviewsByReviewer(String reviewerId) {
        List<String> reviews = getReviews(reviewerId,"Reviewer");

        Map<String,Integer> res=reviews.stream()
                .map(line -> line.split(","))
                .collect(Collectors.toMap(
                        arr -> arr[0],
                        arr -> Integer.parseInt(arr[1])));

        return res;
    }

    @Override
    public OptionalDouble getScoreAverageForReviewer(String reviewerId) {
        List<String> reviews = getReviews(reviewerId,"Reviewer");

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
        List<String> reviews = getReviews(bookId,"Book");

        List<String> res=reviews.stream()
                .map(line -> line.substring(0,line.indexOf(',')))
                .sorted()
                .collect(Collectors.toList());

        return res;
    }

    @Override
    public Map<String, Integer> getReviewsForBook(String bookId) {

        List<String> reviews = getReviews(bookId,"Book");

        Map<String,Integer> res=reviews.stream()
                .map(line -> line.split(","))
                .collect(Collectors.toMap(arr -> arr[0],arr -> Integer.parseInt(arr[1])));

        return res;
    }

    @Override
    public OptionalDouble getAverageReviewScoreForBook(String bookId) {
        List<String> reviews = getReviews(bookId,"Book");

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

    private List<String> getReviews(String key,String key_name) {
        List<String> keys_names= new ArrayList<>();
        keys_names.add(key_name);
        List<String> keys= new ArrayList<>();
        keys.add(key);
        List<String> reviews = null;
        reviews = DB.get_lines_for_keys(keys_names,keys);
        return reviews;
    }
}
