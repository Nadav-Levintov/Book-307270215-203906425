package il.ac.technion.cs.sd.book.test;

import com.google.inject.Inject;
import db_utils.DataBase;
import db_utils.DataBaseFactory;
import il.ac.technion.cs.sd.book.app.BookScoreInitializer;
import il.ac.technion.cs.sd.book.app.BookScoreInitializerImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nadav on 15-May-17.
 */
public class FakeBookScoreInitializerImpl extends BookScoreInitializerImpl implements BookScoreInitializer {

    @Inject
    public FakeBookScoreInitializerImpl(DataBaseFactory dataBaseFactory) {
        super(dataBaseFactory);
    }

    public DataBaseFactory get_DataBaseFactory()
    {
        return dataBaseFactory;
    }
}