package il.ac.technion.cs.sd.book.test;

import com.google.inject.AbstractModule;
import db_utils.DataBase;
import db_utils.DataBaseFactory;
import db_utils.DataBaseFactoryImpl;
import db_utils.DataBaseImpl;
import il.ac.technion.cs.sd.book.app.BookScoreInitializer;
import il.ac.technion.cs.sd.book.app.BookScoreInitializerImpl;
import il.ac.technion.cs.sd.book.app.BookScoreReader;
import il.ac.technion.cs.sd.book.app.BookScoreReaderImpl;

// This module is in the testing project, so that it could easily bind all dependencies from all levels.
class    BookScoreModule extends AbstractModule {
  @Override
  protected void configure() {
    this.bind(DataBaseFactory.class).to(DataBaseFactoryImpl.class);
    this.bind(DataBase.class).to(DataBaseImpl.class);
    this.bind(BookScoreInitializer.class).to(BookScoreInitializerImpl.class);
    this.bind(BookScoreReader.class).to(BookScoreReaderImpl.class);

  }
}
