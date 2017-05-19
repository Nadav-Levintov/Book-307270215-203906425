package db_utils;

import com.google.inject.Inject;
import il.ac.technion.cs.sd.book.ext.LineStorageFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nadav on 17-May-17.
 */
public interface DataBaseFactory {

    public DataBaseFactory setNum_of_keys(Integer num_of_keys);

    public DataBaseFactory setNames_of_columns(List<String> names_of_columns);

    public DataBase build();
}


