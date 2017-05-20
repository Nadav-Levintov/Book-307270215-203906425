package il.ac.technion.cs.sd.book.test;

import com.google.inject.Inject;
import db_utils.DataBase;
import db_utils.DataBaseFactory;
import il.ac.technion.cs.sd.book.app.BookScoreInitializer;
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
public class FakeBookScoreInitializerImpl implements BookScoreInitializer {
    private DataBaseFactory dataBaseFactory;

    @Inject
    public FakeBookScoreInitializerImpl(DataBaseFactory dataBaseFactory) {
        this.dataBaseFactory = dataBaseFactory;
    }


    @Override
    public void setup(String xmlData) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db=dbf.newDocumentBuilder();
        InputSource is = new InputSource((new StringReader(xmlData)));
        Document doc = db.parse(is);
        doc.getDocumentElement().normalize();
        String csv_file = new String();
        NodeList nListReviewers = doc.getElementsByTagName("Reviewer");
        for (int temp = 0; temp < nListReviewers.getLength(); temp++)
        {
            Node nNodeReviewers = nListReviewers.item(temp);
            String csv_key = new String();
            if (nNodeReviewers.getNodeType() == Node.ELEMENT_NODE)
            {
                Element eElementeviewer = (Element) nNodeReviewers;
                csv_key += (eElementeviewer).getAttribute("Id") + ",";
                NodeList nListReviews = (eElementeviewer).getElementsByTagName("Review");
                for (int temp2 = 0; temp2 < nListReviews.getLength(); temp2++)
                {
                    String csv_value = new String();
                    Node nNodeReview = nListReviews.item(temp2);
                    if (nNodeReview.getNodeType() == Node.ELEMENT_NODE){
                        Element eElementReview = (Element) nNodeReview;
                        csv_value += eElementReview.getElementsByTagName("Id").item(0).getTextContent() + ",";
                        csv_value += eElementReview.getElementsByTagName("Score").item(0).getTextContent() + "\n";
                        csv_file += csv_key + csv_value;
                    }
                }

            }
        }

        Integer num_of_keys=2;
        List<String> names_of_columns = new ArrayList<>();
        names_of_columns.add("Reviewer");
        names_of_columns.add("Book");
        names_of_columns.add("Score");



        DataBase DB = dataBaseFactory.setNames_of_columns(names_of_columns)
                .setNum_of_keys(num_of_keys)
                .build();

        DB.build_db(csv_file);
    }

    public DataBaseFactory get_DataBaseFactory()
    {
        return dataBaseFactory;
    }
}