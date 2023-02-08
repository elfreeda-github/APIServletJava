/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.he.api;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

class CityDAO {

	private static final String DATA_FILE_PATH = "src/com/he/api/data.xml";
    private static final File DATA_FILE = new File(DATA_FILE_PATH);
    private static HashMap<String, String> cities;
    private static HashMap<String, String> ids;
    private static CityDAO instance;
    private CityDAO(){
        cities = new HashMap<>();
        ids = new HashMap<>();
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            //System.out.println(Thread.currentThread().getContextClassLoader().getResource(DATA_FILE_PATH).toURI());
            Document document = builder.parse(DATA_FILE);
            String expression = "/dataset/cities/city";
            NodeList nodes = (NodeList)xpath.evaluate(expression,document,XPathConstants.NODESET);
            for(int i = 0;i<nodes.getLength();i++){
                Element element = (Element) nodes.item(i);
                String id = element.getAttribute("id");
                String city = element.getAttribute("name");
                cities.put(id,city);
                ids.put(city,id);
            }
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(UserDAOConcrete.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(CityDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static CityDAO getInstance(){
        if(instance == null)
            instance = new CityDAO();
        return instance;
    }
    public boolean validate(String city) {
        return ids.containsKey(city);
    }
    
    public String getId(String city){
        if(ids.containsKey(city)){
            return ids.get(city);
        }
        return null;
    }
    public String getCity(String id){
        if(cities.containsKey(id)){
            return cities.get(id);
        }
        return null;
    }
    
}
