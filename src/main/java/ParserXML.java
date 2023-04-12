import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class ParserXML {
    public ParserXML(){
        read(getDoc(), LOAD);
        read(getDoc(), SAVE);
        read(getDoc(), LOG);
    }
    private static final String LOAD = "load";
    private static final String SAVE = "save";
    private static final String LOG = "log";
    private boolean loadConfig;
    private boolean saveConfig;
    private boolean saveLog;
    private String fileNameLoad;
    private String fileNameSave;
    private String formatLoad;
    private String formatSave;
    private String fileLogName;


    private Document doc;
    private void read(Document doc, String element) {
        NodeList elements = doc.getElementsByTagName(element);
        for (int i = 0; i < elements.getLength(); i++) {
            Node node = elements.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                switch (element) {
                    case LOAD: {
                        boolean modLoad;
                        if (eElement.getElementsByTagName("enabled").item(0).getTextContent().equals("true")) {
                            modLoad = true;
                        } else {
                            modLoad = false;
                        }
                        fileNameLoad = eElement.getElementsByTagName("fileName").item(0).getTextContent();
                        formatLoad = eElement.getElementsByTagName("format").item(0).getTextContent();
                        loadConfig = modLoad;
                    }
                    break;
                    case SAVE: {
                        boolean modSave;
                        if (eElement.getElementsByTagName("enabled").item(0).getTextContent().equals("true")) {
                            modSave = true;
                        } else {
                            modSave = false;
                        }
                        fileNameSave = eElement.getElementsByTagName("fileName").item(0).getTextContent();
                        formatSave = eElement.getElementsByTagName("format").item(0).getTextContent();
                        saveConfig = modSave;
                    }
                    break;
                    case LOG: {
                        fileLogName = eElement.getElementsByTagName("fileName").item(0).getTextContent();
                        boolean modLog;
                        if (eElement.getElementsByTagName("enabled").item(0).getTextContent().equals("true")) {
                            modLog = true;
                        } else {
                            modLog = false;
                        }
                        saveLog = modLog;
                    }
                    break;
                }
            }
        }
    }
    private Document getDoc(){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        {
            try {
                builder = factory.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                throw new RuntimeException(e);
            }
        }
        {
            try {
                doc = builder.parse(new File("shop.xml"));
            } catch (SAXException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return doc;
    }
    public String getFileLogName(){
        return fileLogName;
    }
    public boolean loadFile(){
        return loadConfig;
    }
    public boolean getLoadTextOrJson(){
        if(formatLoad.equals("txt")){
            return true;
        }else{
            return false;
        }
    }
    public File getFileLoad(){
        return new File(fileNameLoad);
    }
    public boolean saveFile(){
        return saveConfig;
    }
    public boolean getSaveTextOrJson(){
        if(formatSave.equals("txt")){
            return true;
        }else{
            return false;
        }
    }
    public File getFileSave(){
        return new File(fileNameSave);
    }
    public boolean writeLog(){
        return saveLog;
    }
    public File getFileLog(){
        return new File(fileLogName);
    }
    public boolean getSaveLog(){
        return saveLog;
    }
}
