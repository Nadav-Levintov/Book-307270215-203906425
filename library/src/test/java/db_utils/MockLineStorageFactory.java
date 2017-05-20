package db_utils;

import il.ac.technion.cs.sd.book.ext.LineStorage;
import il.ac.technion.cs.sd.book.ext.LineStorageFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by Nadav on 19-May-17.
 */
public class MockLineStorageFactory implements LineStorageFactory {
    private List<MockLineStorage> files = new ArrayList<>();
    @Override
    public LineStorage open(String s) throws IndexOutOfBoundsException {

        List<MockLineStorage> file_list = files.stream()
                .filter( file -> file.getMockedFileName().equals(s))
                .collect(Collectors.toList());
        MockLineStorage file;
        if(file_list.size() == 0)
        {
            file = new MockLineStorage(s);
            files.add(file);
        }
        else
        {
            file = file_list.get(0);
        }

        try {
            TimeUnit.MILLISECONDS.sleep(files.size()*100);
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }

        return file;
    }
}
