package db_utils;

import com.google.inject.AbstractModule;
import il.ac.technion.cs.sd.book.ext.LineStorageFactory;

/**
 * Created by Nadav on 19-May-17.
 */
public class MockedLineStorageModule extends AbstractModule {
    public MockedLineStorageModule() {
    }

    protected void configure() {
        this.bind(LineStorageFactory.class).to(MockLineStorageFactory.class);
    }
}
