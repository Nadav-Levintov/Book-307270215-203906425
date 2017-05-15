package db_utils;

import org.junit.Test;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Created by Nadav on 22-Apr-17.
 */
public class DataBaseTest {
    @Test
    public void build_db() throws Exception {

        List<String> col_names = new ArrayList<>();
        col_names.add("Name");
        col_names.add("Age");

        List<String> file = new LinkedList<String>();

        StorageInterface si = Mockito.mock(DefaultStorageImplament.class);


        Mockito.doAnswer(new Answer(){
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                file.add(invocationOnMock.getArgument(0));
                return null;
            }
        }).when(si).appendLine(Mockito.anyString());

        DataBase DB = new DataBase(col_names,si);

        String csvData = "Nadav,25\n" +
                "Benny,27\n" +
                "Zed,65\n";

        DB.build_db(csvData);

        Mockito.when(si.numberOfLines()).thenReturn(file.size());
        Mockito.when(si.read(Mockito.anyInt())).thenAnswer(i -> file.get(i.getArgument(0)));

        assertEquals(Optional.of(3),Optional.of(DB.size()));
        assertEquals(Optional.of("Benny,27"),DB.get_line_by_num(0));
        assertEquals(Optional.of("Zed,65"),DB.get_line_by_num(2));
        try{
            DB.get_line_by_num(4);
            fail("Exception was not thrown when expected");
        } catch (IllegalArgumentException e)
        {

        }
    }

    @Test
    public void size() throws Exception {
        List<String> col_names = new ArrayList<>();
        col_names.add("Name");
        col_names.add("Age");

        List<String> file = new LinkedList<String>();

        StorageInterface si = Mockito.mock(DefaultStorageImplament.class);


        Mockito.doAnswer(new Answer(){
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                file.add(invocationOnMock.getArgument(0));
                return null;
            }
        }).when(si).appendLine(Mockito.anyString());

        DataBase DB = new DataBase(col_names,si);

        String csvData = "Nadav,25\n" +
                "Benny,27\n" +
                "Zed,65\n";

        DB.build_db(csvData);
        Mockito.when(si.numberOfLines()).thenReturn(file.size());
        assertEquals(Optional.of(3),Optional.of(DB.size()));
    }


    @Test
    public void get_val_from_column_by_name() throws Exception {
        List<String> col_names = new ArrayList<>();
        col_names.add("Name");
        col_names.add("Age");

        List<String> file = new LinkedList<String>();

        StorageInterface si = Mockito.mock(DefaultStorageImplament.class);


        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                file.add(invocationOnMock.getArgument(0));
                return null;
            }
        }).when(si).appendLine(Mockito.anyString());
        Mockito.when(si.read(Mockito.anyInt())).thenAnswer(i -> file.get(i.getArgument(0)));

        DataBase DB = new DataBase(col_names,si);

        String csvData = "Nadav,25\n" +
                "Benny,27\n" +
                "Zed,65\n";

        DB.build_db(csvData);
        Mockito.when(si.numberOfLines()).thenReturn(file.size());
        assertEquals(Optional.of("25"),DB.get_val_from_column_by_name("Nadav",col_names.get(1)));
        assertEquals(Optional.of("Nadav"),DB.get_val_from_column_by_name("Nadav",col_names.get(0)));
        assertEquals(Optional.of("27"),DB.get_val_from_column_by_name("Benny",col_names.get(1)));
        assertEquals(Optional.of("Benny"),DB.get_val_from_column_by_name("Benny",col_names.get(0)));
        assertEquals(Optional.empty(),DB.get_val_from_column_by_name("Shalom",col_names.get(0)));
    }

    @Test
    public void get_val_from_column_by_colum_number() throws Exception {
        List<String> col_names = new ArrayList<>();
        col_names.add("Name");
        col_names.add("Age");

        List<String> file = new LinkedList<String>();

        StorageInterface si = Mockito.mock(DefaultStorageImplament.class);


        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                file.add(invocationOnMock.getArgument(0));
                return null;
            }
        }).when(si).appendLine(Mockito.anyString());
        Mockito.when(si.read(Mockito.anyInt())).thenAnswer(i -> file.get(i.getArgument(0)));

        DataBase DB = new DataBase(col_names,si);

        String csvData = "Nadav,25\n" +
                "Benny,27\n" +
                "Zed,65\n";

        DB.build_db(csvData);
        Mockito.when(si.numberOfLines()).thenReturn(file.size());
        assertEquals(Optional.of("25"),DB.get_val_from_column_by_colum_number("Nadav",1));
        assertEquals(Optional.of("Nadav"),DB.get_val_from_column_by_colum_number("Nadav",0));
        assertEquals(Optional.of("27"),DB.get_val_from_column_by_colum_number("Benny",1));
        assertEquals(Optional.of("Benny"),DB.get_val_from_column_by_colum_number("Benny",0));
        assertEquals(Optional.empty(),DB.get_val_from_column_by_colum_number("Shalom",0));
    }


    /*
    @Test
    public void getNum_of_columns() throws Exception {

        List<String> col_names = new ArrayList<>();
        col_names.add("Name");
        col_names.add("Age");

        DataBase DB = new DataBase(col_names);

        assertEquals(col_names.size(),DB.getNum_of_columns().intValue());
    }
*/

}