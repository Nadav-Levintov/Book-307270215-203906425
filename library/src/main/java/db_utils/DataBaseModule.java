package db_utils;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import il.ac.technion.cs.sd.book.ext.LineStorageModule;

/**
 * Created by Nadav on 19-May-17.
 */
public class DataBaseModule  extends AbstractModule {
    public DataBaseModule() {
    }

    protected void configure() {
    this.bind(DataBaseFactory.class).to(DataBaseFactoryImpl.class);
    }
}
