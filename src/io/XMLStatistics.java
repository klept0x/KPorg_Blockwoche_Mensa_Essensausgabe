package io;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import controller.Simulation;
import model.MensaStationen;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.File;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 *
 * @author Crunchify.com modified by Team5
 * @version 2
 */

    public class XMLStatistics {

        public void theXMLspeichern() {
            DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder icBuilder = null;

            try {
                try {
                    icBuilder = icFactory.newDocumentBuilder();
                } catch (ParserConfigurationException e1) {
                    e1.printStackTrace();
                }

            Document doc = icBuilder.newDocument();
            Element mainRootElement = doc.createElement("daten");
            doc.appendChild(mainRootElement);

                int id = 1;

            for(MensaStationen s : MensaStationen.getAllMensaStation()) {

                // append child elements to root element
                mainRootElement.appendChild(getAppendChild(doc, String.valueOf(id), s.getLabel(), String.valueOf(s.getMeasurement().getIdleTime()), String.valueOf(s.getMeasurement().getInUseTime())));
                id++;
            }

            // output DOM XML to XML File
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);

                Path path = Paths.get("daten");
                path = path.toRealPath(LinkOption.NOFOLLOW_LINKS);

                File file = new File(String.valueOf(path));

            StreamResult console = new StreamResult("" + file.toString()+ "\\Station Kennzahlauswertung "+ Simulation.szeanrio  + ".xml");
            transformer.transform(source, console);


            System.out.println("\nXML DOM Created Successfully..");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private static Node getAppendChild(Document doc, String id, String name, String idelTime, String inUseTime) {
            Element kennzahlen = doc.createElement("kennzahlen");
            kennzahlen.setAttribute("id", id);
            kennzahlen.appendChild(getCompanyElements(doc, kennzahlen, "stationen", name));
            kennzahlen.appendChild(getCompanyElements(doc, kennzahlen, "idelTime", idelTime));
            kennzahlen.appendChild(getCompanyElements(doc, kennzahlen, "inUseTime", inUseTime));
            return kennzahlen;
        }

        // utility method to create text node
        private static Node getCompanyElements(Document doc, Element element, String name, String value) {
            Element node = doc.createElement(name);
            node.appendChild(doc.createTextNode(value));
            return node;
        }


}
