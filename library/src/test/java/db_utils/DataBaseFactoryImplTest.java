package db_utils;

import com.google.inject.Guice;
import com.google.inject.Injector;
import il.ac.technion.cs.sd.book.ext.LineStorage;
import il.ac.technion.cs.sd.book.ext.LineStorageFactory;
import il.ac.technion.cs.sd.book.ext.LineStorageModule;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by benny on 19/05/2017.
 */
public class DataBaseFactoryImplTest {
    @Test
    public void setNum_of_keys() throws Exception {
    }

    @Test
    public void setNames_of_columns() throws Exception {
    }

    @Test
    public void build() throws Exception {

        ArrayList<String> names_of_columns = new ArrayList<>();
        names_of_columns.add("reviewer");
        names_of_columns.add("book");
        names_of_columns.add("grade");
        Injector injector= Guice.createInjector(new LineStorageModule());


        LineStorageFactory lineStorageFactory = new LineStorageFactory() {
            @Override
            public LineStorage open(String s) throws IndexOutOfBoundsException {
                return null;
            }
        };

        DataBaseImpl DB = new DataBaseImpl(2, names_of_columns,lineStorageFactory);
        DB.build_db("keyA1,keyB1,100\nkeyA2,keyB2,20\nkeyA1,keyB2,50\n");

    }

}