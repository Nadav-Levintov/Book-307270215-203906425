package il.ac.technion.cs.sd.book.test;

import com.google.inject.Inject;
import db_utils.DataBaseFactory;
import il.ac.technion.cs.sd.book.app.BookScoreInitializer;
import il.ac.technion.cs.sd.book.app.BookScoreInitializerImpl;

/**
 * Created by Nadav on 15-May-17.
 */
public class FakeBookScoreInitializerImpl extends BookScoreInitializerImpl implements BookScoreInitializer {

    @Inject
    public FakeBookScoreInitializerImpl(DataBaseFactory dataBaseFactory) {
        super(dataBaseFactory);
    }

    public DataBaseFactory get_DataBaseFactory()
    {
        return dataBaseFactory;
    }
}