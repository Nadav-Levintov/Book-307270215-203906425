package il.ac.technion.cs.sd.book.app;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
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
        doc.getDocumentElement().normalize();
        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        NodeList nListReviewers = doc.getElementsByTagName("Reviewer");
        System.out.println("----------------------------");

        for (int temp = 0; temp < nListReviewers.getLength(); temp++) {

            Node nNodeReviewers = nListReviewers.item(temp);
            System.out.println("\nCurrent Element :" + nNodeReviewers.getNodeName());

            if (nNodeReviewers.getNodeType() == Node.ELEMENT_NODE) {

                Element eElementeviewer = (Element) nNodeReviewers;
                System.out.println("Reviewer id : " + ((Element) nNodeReviewers).getAttribute("Id"));

                NodeList nListReviews = ((Element) nNodeReviewers).getElementsByTagName("Review");

                for (int temp2 = 0; temp2 < nListReviews.getLength(); temp2++){

                    Node nNodeReview = nListReviews.item(temp2);
                    System.out.println("\n  Current Element :" + nNodeReview.getNodeName());
                    if (nNodeReview.getNodeType() == Node.ELEMENT_NODE){
                        Element eElementReview = (Element) nNodeReview;
                        System.out.println("        Id: " + eElementReview.getElementsByTagName("Id").item(0).getTextContent());
                        System.out.println("        Score: " + eElementReview.getElementsByTagName("Score").item(0).getTextContent());

                    }
                }

            }
        }


    }
}