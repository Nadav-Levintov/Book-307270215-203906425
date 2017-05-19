package il.ac.technion.cs.sd.book.test;

import il.ac.technion.cs.sd.book.ext.LineStorage;
import il.ac.technion.cs.sd.book.ext.LineStorageFactory;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.mock;

/**
 * Created by Nadav on 19-May-17.
 */
public class MockLineStorageFactory implements LineStorageFactory {
    private List<MockLineStorage> files = new ArrayList<>();
    @Override
    public LineStorage open(String s) throws IndexOutOfBoundsException {
        List<MockLineStorage> file_list = files.stream()
                .filter( file -> file.getMockedFileName() == s)
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
        return file;
    }
}
