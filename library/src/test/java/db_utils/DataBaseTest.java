package db_utils;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import static org.junit.Assert.assertEquals;

/**
 * Created by Nadav on 19-May-17.
 */
public class DataBaseTest {

    public DataBase SetupAndBuildDataBase(Integer num_of_keys,List<String> names_of_columns,String csv_data)
    {

        Injector injector= Guice.createInjector(new MockedLineStorageModule());
        DataBaseFactory dataBaseFactoryMock= injector.getInstance(DataBaseFactoryImpl.class);

        DataBase DB = dataBaseFactoryMock.setNames_of_columns(names_of_columns)
                .setNum_of_keys(num_of_keys)
                .build();

        DB.build_db(csv_data);

        return DB;
    }
    @Test
    public void build_db() throws Exception {

        Integer num_of_keys=2;
        List<String> names_of_columns = new ArrayList<>();
        names_of_columns.add("Reviewer");
        names_of_columns.add("Book");
        names_of_columns.add("Score");


        String csv =    "Nadav,Harry,8\n" +
                "Nadav,Harry2,3\n"+
                "Benny,Harry,9\n" +
                "Benny,Harry,9\n" +
                "Benny,Bla,8\n";

        DataBase DB = SetupAndBuildDataBase(num_of_keys,names_of_columns,csv);

        assertEquals(num_of_keys,DB.getNum_of_keys());
        assertEquals(OptionalInt.of(names_of_columns.size()),OptionalInt.of(DB.getNum_of_columns()));
        assertEquals(names_of_columns,DB.getNames_of_columns());
        assertEquals(OptionalInt.of(0),DB.get_num_of_column("Reviewer"));
        assertEquals(OptionalInt.of(1),DB.get_num_of_column("Book"));
        assertEquals(OptionalInt.of(2),DB.get_num_of_column("Score"));

    }

    @Test
    public void get_val_from_column_by_name() throws Exception {

        Integer num_of_keys=2;
        List<String> names_of_columns = new ArrayList<>();
        names_of_columns.add("Reviewer");
        names_of_columns.add("Book");
        names_of_columns.add("Score");


        String csv =    "Nadav,Harry,8\n" +
                "Nadav,Harry2,3\n"+
                "Benny,Harry,9\n" +
                "Benny,Harry,9\n" +
                "Benny,Bla,8\n";

        DataBase DB = SetupAndBuildDataBase(num_of_keys,names_of_columns,csv);
        List<String> keys1 = new ArrayList<>();
        keys1.add("Nadav");
        keys1.add("Harry");
        List<String> keys2 = new ArrayList<>();
        keys2.add("Nadav");
        keys2.add("Bible");

        assertEquals(Optional.of("8"),DB.get_val_from_column_by_name(keys1,"Score"));
        assertEquals(Optional.empty(),DB.get_val_from_column_by_name(keys2,"Score"));
        assertEquals(Optional.empty(),DB.get_val_from_column_by_name(keys1,"Bla"));

    }

    @Test
    public void get_lines_for_key() throws Exception {
    }

    @Test
    public void get_val_from_column_by_column_number() throws Exception {


        Integer num_of_keys=2;
        List<String> names_of_columns = new ArrayList<>();
        names_of_columns.add("Reviewer");
        names_of_columns.add("Book");
        names_of_columns.add("Score");


        String csv =    "Nadav,Harry,8\n" +
                "Nadav,Harry2,3\n"+
                "Benny,Harry,9\n" +
                "Benny,Harry,9\n" +
                "Benny,Bla,8\n";

        DataBase DB = SetupAndBuildDataBase(num_of_keys,names_of_columns,csv);
        List<String> keys1 = new ArrayList<>();
        keys1.add("Nadav");
        keys1.add("Harry");
        List<String> keys2 = new ArrayList<>();
        keys2.add("Nadav");
        keys2.add("Bible");

        assertEquals(Optional.of("8"),DB.get_val_from_column_by_column_number(keys1,2));
        assertEquals(Optional.empty(),DB.get_val_from_column_by_column_number(keys2,2));
        assertEquals(Optional.empty(),DB.get_val_from_column_by_column_number(keys1,4));
    }


    @Test
    public void get_line_by_num_and_key() throws Exception {


    }


}