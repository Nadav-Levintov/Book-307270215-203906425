package il.ac.technion.cs.sd.book.app;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Created by Nadav on 15-May-17.
 */
public class BookScoreInitializerImpl implements BookScoreInitializer {
    @Override
    public void setup(String xmlData) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db=dbf.newDocumentBuilder();
        InputSource is = new InputSource((new StringReader(xmlData)));
        Document doc = db.parse(is);
        System.out.format("The node type:  element: %s uri: %s",  doc.getDocumentElement().toString(),doc.getDocumentURI());
        Node docson = doc.getChildNodes().item(1);
        System.out.format("The node type: %s element: %s", docson.getNodeType(), docson.getTextContent());

    }
}