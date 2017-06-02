package il.ac.technion.cs.sd.book.app;

import com.google.inject.Inject;
import db_utils.DataBase;
import db_utils.DataBaseFactory;
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
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Nadav on 15-May-17.
 */
public class BookScoreInitializerImpl implements BookScoreInitializer {
    protected DataBaseFactory dataBaseFactory;

    @Inject
    public BookScoreInitializerImpl(DataBaseFactory dataBaseFactory) {
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
        LinkedList<String> file_list = new LinkedList<>();
        NodeList nListReviewers = doc.getElementsByTagName("Reviewer");

        Node nNodeReviewers = nListReviewers.item(0);
        String csv_key;
        String csv_value;
        NodeList nListReviews;
        Node nNodeReview;
        Element eElementeviewer;
        Element eElementReview;
        Integer lengthInternal;

        Integer length = nListReviewers.getLength();
        for (int temp = 0; temp < length;)
        {
           // Node nNodeReviewers = nListReviewers.item(temp);
            if (nNodeReviewers.getNodeType() == Node.ELEMENT_NODE)
            {
                temp++;
                eElementeviewer = (Element) nNodeReviewers;
                csv_key = (eElementeviewer).getAttribute("Id") + ",";
                nListReviews = (eElementeviewer).getElementsByTagName("Review");
                lengthInternal = nListReviews.getLength();
                nNodeReview = nListReviews.item(0);
                for (int temp2 = 0; temp2 < lengthInternal;)
                {
                    csv_value = "";
                    if (nNodeReview.getNodeType() == Node.ELEMENT_NODE){
                        temp2++;
                        eElementReview = (Element) nNodeReview;
                        csv_value += eElementReview.getElementsByTagName("Id").item(0).getTextContent() + ",";
                        csv_value += eElementReview.getElementsByTagName("Score").item(0).getTextContent();
                        file_list.add(csv_key + csv_value);
                    }
                    nNodeReview=nNodeReview.getNextSibling();
                }

            }
            nNodeReviewers = nNodeReviewers.getNextSibling();
        }

        Integer num_of_keys=2;
        List<String> names_of_columns = new ArrayList<>();
        names_of_columns.add("Reviewer");
        names_of_columns.add("Book");
        names_of_columns.add("Score");

        DataBase DB = dataBaseFactory.setNames_of_columns(names_of_columns)
                .setNum_of_keys(num_of_keys)
                .build();

        DB.build_db(file_list);
    }
}