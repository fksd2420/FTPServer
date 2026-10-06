package com.example.mylibrary;

import android.content.Context;

import androidx.preference.PreferenceDataStore;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class Settings {

    public static Document XmlDocument;
    public static Context context = null;

    static String settingsFilePath;

    public static LinkedHashMap<String, Object> HashMap = new LinkedHashMap<>();
    public static InMemoryDataStore PreferencesStore = new InMemoryDataStore();



    public static void Load(String filePath) throws Exception {
        settingsFilePath = filePath;
        File file = new File(filePath);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        XmlDocument = db.parse(file);
        XmlDocument.getDocumentElement().normalize();

        NodeList nodeList = XmlDocument.getDocumentElement().getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element)childNode;
                if (element.getNodeName().equals("string"))
                    HashMap.put(element.getAttribute("Key"), element.getAttribute("Value"));
                else if (element.getNodeName().equals("int"))
                    HashMap.put(element.getAttribute("Key"), Integer.parseInt(element.getAttribute("Value")));
                else if (element.getNodeName().equals("bool"))
                    HashMap.put(element.getAttribute("Key"), Boolean.parseBoolean(element.getAttribute("Value")));
            }
        }
    }
    public static void Save() throws Exception {

        //NodeList sharedPrefsNL = XmlDocument.getDocumentElement().getElementsByTagName("SharedPrefs");
//        Element sharedPrefsE;
//        if (sharedPrefsNL.getLength() == 0) {
//            sharedPrefsE = XmlDocument.createElement("SharedPrefs");
//            XmlDocument.getDocumentElement().appendChild(sharedPrefsE);
//        } else {
//            sharedPrefsE = (Element) sharedPrefsNL.item(0);
//            while (sharedPrefsE.hasChildNodes()) sharedPrefsE.removeChild(sharedPrefsE.getFirstChild());
//
//        }

        boolean deleted = false;
        NodeList nodeList = XmlDocument.getDocumentElement().getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element)childNode;
                if (element.getNodeName().equals("string") || element.getNodeName().equals("int") ||
                element.getNodeName().equals("bool")) {
                    XmlDocument.getDocumentElement().removeChild(element);

                    if (i > 0 && nodeList.item(i - 1).getNodeType() == Node.TEXT_NODE) {
                        XmlDocument.getDocumentElement().removeChild(nodeList.item(i - 1));
                    }
                }
            }
        }
        if (HashMap.size() > 0) {
            NodeList nodeList_ = XmlDocument.getDocumentElement().getChildNodes();
            if (nodeList_.getLength() > 0 && nodeList_.item(nodeList_.getLength() - 1).getNodeType() == Node.TEXT_NODE)
                XmlDocument.getDocumentElement().removeChild(nodeList_.item(nodeList_.getLength() - 1));
            XmlDocument.getDocumentElement().appendChild(XmlDocument.createTextNode("\n\n\n"));
        }
        for (Map.Entry<String, Object> entry : HashMap.entrySet()) {
            Element element = null;
            if (entry.getValue() instanceof String) {
                element = XmlDocument.createElement("string");
                element.setAttribute("Key", entry.getKey());
                element.setAttribute("Value", (String)entry.getValue());
            } else if (entry.getValue() instanceof Integer) {
                element = XmlDocument.createElement("int");
                element.setAttribute("Key", entry.getKey());
                element.setAttribute("Value", String.valueOf(entry.getValue()));
            } else if (entry.getValue() instanceof Boolean) {
                element = XmlDocument.createElement("bool");
                element.setAttribute("Key", entry.getKey());
                element.setAttribute("Value", String.valueOf(entry.getValue()));
            }
            XmlDocument.getDocumentElement().appendChild(element);
            element.getParentNode().insertBefore(Settings.XmlDocument.createTextNode("    "), element);
            element.getParentNode().appendChild(Settings.XmlDocument.createTextNode("\n"));
        }
        XmlDocument.getDocumentElement().appendChild(XmlDocument.createTextNode("\n"));


        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        //transformerFactory.setAttribute("indent-number", 4);
        Transformer transformer = transformerFactory.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        //DOMSource source = new DOMSource(XmlDocument);
        //StreamResult result = new StreamResult(new File(settingsFilePath));
        //transformer.transform(source, result);

        XmlDocument.getDocumentElement().normalize();
        StringWriter writer_ = new StringWriter();
        transformer.transform(new DOMSource(XmlDocument), new StreamResult(writer_));
        String xmlString = writer_.getBuffer().toString();
        xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+xmlString;
        File file = new File(settingsFilePath);
        FileOutputStream stream = new FileOutputStream(file);
        stream.write(xmlString.getBytes("UTF-8"));
        stream.close();

    }
    public static void XmlDocumentClearNodes(Element element) {
        while(element.hasChildNodes()) element.removeChild(element.getFirstChild());
    }
    public static void XmlDocumentFix0(Element parent, List<Element> elementList, String prefix, String midfix, String suffix) {

        String name = elementList.get(0).getNodeName();
        NodeList nodeList = parent.getChildNodes();

        int last_node = -1;
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                if (element.getNodeName().equals(name)) {
                    if (i > last_node) last_node = i;

                    parent.removeChild(node);
                    if (i > 0 && nodeList.item(i - 1).getNodeType() == Node.TEXT_NODE)
                        parent.removeChild(nodeList.item(i - 1));

                }
            }
        }
        if (nodeList.getLength() > last_node + 1 && nodeList.item(last_node + 1).getNodeType() == Node.TEXT_NODE)
            parent.removeChild(nodeList.item(last_node + 1));


        int space = 4;
        Element testElement = XmlDocument.createElement("Test");
        parent.appendChild(testElement);
        Node node = testElement;
        while (node.getParentNode() != XmlDocument.getDocumentElement()) {
            space += 4;
            node = node.getParentNode();
        }
        parent.removeChild(testElement);

        parent.appendChild(XmlDocument.createTextNode(prefix));

        for (int i = 0 ; i < elementList.size(); i++) {
            Element e = elementList.get(i);
            parent.appendChild(XmlDocument.createTextNode(" ".repeat(space)));
            parent.appendChild(e);
            if (midfix != null && i != elementList.size() - 1) parent.appendChild(XmlDocument.createTextNode(midfix));
        }
        parent.appendChild(XmlDocument.createTextNode(suffix));
    }
    public static void XmlDocumentFix1(Element parent, Element child) {

    }


    public static void CopyFromAssets(Context context, String filePath, String assetsFilePath) throws Exception {
        InputStream is = context.getAssets().open(assetsFilePath);
        byte[] buffer = new byte[is.available()];
        is.read(buffer, 0, buffer.length);
        FileOutputStream stream = new FileOutputStream(filePath);
        stream.write(buffer);
        stream.close();
    }

    public static class InMemoryDataStore extends PreferenceDataStore {


        @Override
        public void putString(String key, String value) {
            if (value == null) {
                HashMap.remove(key);
                return;
            }
            HashMap.put(key, value);
        }

        @Override
        public String getString(String key, String defaultValue) {
            return HashMap.containsKey(key) ? (String) HashMap.get(key) : defaultValue;
        }

        @Override
        public void putBoolean(String key, boolean value) {
            HashMap.put(key, value);
        }

        @Override
        public boolean getBoolean(String key, boolean defaultValue) {
            return (boolean)HashMap.getOrDefault(key, defaultValue);
        }


        @Override
        public void putInt(String key, int value) {
            HashMap.put(key, value);
        }

        @Override
        public int getInt(String key, int defaultValue) {
            return (int)HashMap.getOrDefault(key, defaultValue);
        }


        // Override putInt, getInt, putSet, getSet similarly if your UI uses them
    }











    static String encryption_keyName = "WakeOnLAN_Key0";
    static String encryption_IV = "HyWikwyDvshNjuA2";

}
