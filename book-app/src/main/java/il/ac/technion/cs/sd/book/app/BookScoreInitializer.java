package il.ac.technion.cs.sd.book.app;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public interface BookScoreInitializer {
  /** Saves the XML data persistently, so that it could be run using BookScoreReader. */
  void setup(String xmlData) throws ParserConfigurationException, IOException, SAXException;
}