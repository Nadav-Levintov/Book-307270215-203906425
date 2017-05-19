package il.ac.technion.cs.sd.book.test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import db_utils.DataBase;
import db_utils.DataBaseFactory;
import db_utils.DataBaseFactoryImpl;
import db_utils.DataBaseModule;
import il.ac.technion.cs.sd.book.app.BookScoreInitializerImpl;
import il.ac.technion.cs.sd.book.ext.LineStorageModule;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.*;

/**
 * Created by Nadav on 19-May-17.
 */
public class DataBaseTest {
    @Test
    public void build_db() throws Exception {

        Injector injector= Guice.createInjector(new MockedLineStorageModule());
        DataBaseFactory dataBaseFactoryMock= injector.getInstance(DataBaseFactoryImpl.class);

        Integer num_of_keys=2;
        List<String> names_of_columns = new ArrayList<>();
        names_of_columns.add("Reviewer");
        names_of_columns.add("Book");
        names_of_columns.add("Score");



        DataBase DB = dataBaseFactoryMock.setNames_of_columns(names_of_columns)
                .setNum_of_keys(num_of_keys)
                .build();
        String csv =    "Nadav,Harry,8\n" +
                        "Nadav,Harry2,3\n"+
                        "Benny,Harry,9\n" +
                        "Benny,Harry,9\n" +
                        "Benny,Bla,8\n";
        DB.build_db(csv);



    }

    @Test
    public void get_val_from_column_by_name() throws Exception {
    }

    @Test
    public void get_lines_for_key() throws Exception {
    }

    @Test
    public void get_val_from_column_by_column_number() throws Exception {
    }

    @Test
    public void getNum_of_columns() throws Exception {
    }

    @Test
    public void get_line_by_num_and_key() throws Exception {
    }

    @Test
    public void getNames_of_columns() throws Exception {
    }

    @Test
    public void getNum_of_keys() throws Exception {
    }

    @Test
    public void get_num_of_column() throws Exception {
    }

}