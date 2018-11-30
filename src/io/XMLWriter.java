//package io;
//
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.transform.OutputKeys;
//import javax.xml.transform.Transformer;
//import javax.xml.transform.TransformerFactory;
//import javax.xml.transform.dom.DOMSource;
//import javax.xml.transform.stream.StreamResult;
//
///**
// * Beschreibung der Klasse XMLWriter.
// * Weiter Beschreibung
// * <p>
// * Die Klasse wurde am 30.November.2018 um 14:57 Uhr erstellt.
// *
// * @author Team5
// * @version 1.0
// */
//
//public class XMLWriter {
//    String namesspaceURI = "NamesspaceURI";
//    String qulifiedName = "QulifiedName";
//    String tagName = "TagName";
//    String theNameForIDTagName = "id";
//    //String theIDTagName = "";
//
//    DocumentBuilderFactory icFactory;
//    DocumentBuilder icBuilder;
//    Element mainRootElement;
//    Document doc;
//    int id;
//
//
//    public XMLWriter() {
//
//    }
//
//
//    public void theXMLRootElement(String namesspaceURI, String qulifiedName, String tagName, String theNameForIDTagName){
//        this.namesspaceURI = namesspaceURI;
//        this.qulifiedName = qulifiedName;
//        this.tagName = tagName;
//        this.theNameForIDTagName = theNameForIDTagName;
//        icFactory = DocumentBuilderFactory.newInstance();
//
//        try {
//            icBuilder = icFactory.newDocumentBuilder();
//            doc = icBuilder.newDocument();
//            mainRootElement = doc.createElementNS(namesspaceURI, qulifiedName);
//            doc.appendChild(mainRootElement);
//
//
//
//            // output DOM XML to console
//            Transformer transformer = TransformerFactory.newInstance().newTransformer();
//            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//            DOMSource source = new DOMSource(doc);
//            StreamResult console = new StreamResult(System.out);
//            transformer.transform(source, console);
//
//            System.out.println("\nXML DOM Created Successfully..");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void theXMLappendChild(String ersterEintrag, String zweiterEintrag, String dritterEintrag){
//        try{
//            // append child elements to root element
//            mainRootElement.appendChild(getCompany(doc, String.valueOf(id), ersterEintrag, zweiterEintrag, dritterEintrag));
//            id ++;
//
//        } catch (Exception e) {
//        e.printStackTrace();
//        }
//    }
//
//    private void theXMLOutput(){
//
//        try {
//            // output DOM XML to console
//            Transformer transformer = TransformerFactory.newInstance().newTransformer();
//            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//            DOMSource source = new DOMSource(doc);
//            StreamResult console = new StreamResult(System.out);
//            transformer.transform(source, console);
//
//            System.out.println("\nXML DOM Created Successfully..");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    private Node getCompany(Document doc, String id, String name, String age, String role) {
//        Element company = doc.createElement(tagName);
//        company.setAttribute(theNameForIDTagName, id);
//        company.appendChild(getCompanyElements(doc, company, "Name", name));
//        company.appendChild(getCompanyElements(doc, company, "Type", age));
//        company.appendChild(getCompanyElements(doc, company, "Employees", role));
//        return company;
//    }
//
//    // utility method to create text node
//    private Node getCompanyElements(Document doc, Element element, String name, String value) {
//        Element node = doc.createElement(name);
//        node.appendChild(doc.createTextNode(value));
//        return node;
//    }
//}
