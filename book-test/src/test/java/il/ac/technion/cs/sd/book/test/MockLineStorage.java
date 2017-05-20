package il.ac.technion.cs.sd.book.test;

import il.ac.technion.cs.sd.book.ext.LineStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Nadav on 19-May-17.
 */
public class MockLineStorage implements LineStorage {

    private List<String> mockedFile;
    private String mockedFileName;

    public MockLineStorage(String mockedFileName) {
        this.mockedFile = new ArrayList<>();
        this.mockedFileName = mockedFileName;
    }

    @Override
    public void appendLine(String s) {
        mockedFile.add(s);
    }

    @Override
    public String read(int i) throws InterruptedException {
        try {
            TimeUnit.MILLISECONDS.sleep(mockedFile.get(i).length());
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
        return mockedFile.get(i);
    }

    @Override
    public int numberOfLines() throws InterruptedException {
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
        return mockedFile.size();
    }

    public String getMockedFileName() {
        return mockedFileName;
    }
}
