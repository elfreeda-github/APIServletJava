package com.he.api;


import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public class UserDAOConcrete implements UserDAO{

	private static final String DATA_FILE_PATH = "src/com/he/api/data.xml";
    private static final File DATA_FILE = new File(DATA_FILE_PATH);
    private static UserDAOConcrete instance;
    
    private XPath xpath;
    private Document document;
    
    private UserDAOConcrete(){
        try {
            xpath = XPathFactory.newInstance().newXPath();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = builder.parse(DATA_FILE);
            //System.out.println(Thread.currentThread().getContextClassLoader().getResource(DATA_FILE_PATH).toURI());
//            document = builder.parse(new File(Thread.currentThread().getContextClassLoader().getResource(DATA_FILE_PATH).toURI()));
        } catch (ParserConfigurationException | SAXException | IOException  ex) {
            Logger.getLogger(UserDAOConcrete.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static UserDAO getInstance(){
//        if(instance == null)
        instance = new UserDAOConcrete();
        return instance;
    }
        
    @Override
    public List<User> searchByFirstName(String firstName) {
        String expression = "/dataset/users/user[@firstName='"+firstName+"']";
        return toList(expression);
    }

    @Override
    public List<User> searchByLastName(String lastName) {
        String expression = "/dataset/users/user[@lastName='"+lastName+"']";
        return toList(expression);
    }

    @Override
    public User searchById(String id) {
        String expression = "/dataset/users/user[@id='"+id+"']";
        List<User> users = toList(expression);
        if(users.size() != 1)
            return null;
        return users.get(0);
    }
    @Override
    public List<User> searchByCity(String city) {
    	CityDAO cityDAO = CityDAO.getInstance();
        String expression = "/dataset/users/user[@cityId='"+cityDAO.getId(city)+"']";
        return toList(expression);
    }
    public int getId_auto_increment() {
    	NodeList childs = document.getDocumentElement().getChildNodes();
        Element root = null;
        for(int i = 0;i<childs.getLength();i++){
            Node child = childs.item(i);
            if(child.getNodeName().equals("users")){
                root = (Element)child;
            }
        }
        return Integer.parseInt(root.getAttribute("id_auto_increment"));
    }
    @Override
    public void insertUser(User user) {
        NodeList childs = document.getDocumentElement().getChildNodes();
        Element root = null;
        for(int i = 0;i<childs.getLength();i++){
            Node child = childs.item(i);
            if(child.getNodeName().equals("users")){
                root = (Element)child;
            }
        }
        int id = Integer.parseInt(root.getAttribute("id_auto_increment"));
        Element element = document.createElement("user");
        element.setAttribute("id", Integer.toString(id));
        element.setAttribute("firstName", user.getFirstName());
        element.setAttribute("lastName", user.getLastName());
        element.setAttribute("cityId", user.getCityId());
        root.appendChild(element);
        root.setAttribute("id_auto_increment",Integer.toString(id+1));
        transform();
    }
    
    @Override
    public void deleteUser(String id) {
        String expression = "/dataset/users/user[@id='"+id+"']";
        try {
            NodeList childs = document.getDocumentElement().getChildNodes();
            Element root = null;
            for(int i = 0;i<childs.getLength();i++){
                Node child = childs.item(i);
                if(child.getNodeName().equals("users")){
                    root = (Element)child;
//                    System.out.println("root is users");
                }
            }
            NodeList nodes = (NodeList)xpath.evaluate(expression,document,XPathConstants.NODESET);
            for(int i = 0;i<nodes.getLength();i++){
                Element element = (Element) nodes.item(i);
                root.removeChild(element);
//                System.out.println("deleted");
            }
            transform();
        } catch (XPathExpressionException ex) {
            Logger.getLogger(UserDAOConcrete.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void updateUser(User user) {
        try {
            String expression = String.format("/dataset/users/user[@id='%d']",user.getId());
            Node node = (Node) xpath.evaluate(expression, document, XPathConstants.NODE);
            NamedNodeMap attr = node.getAttributes();
            Node firstName = attr.getNamedItem("firstName");
            firstName.setTextContent(user.getFirstName());
            Node lastName = attr.getNamedItem("lastName");
            lastName.setTextContent(user.getLastName());
            Node city = attr.getNamedItem("cityId");
            city.setTextContent(user.getCityId());
            transform();
        } catch (XPathExpressionException ex) {
            Logger.getLogger(UserDAOConcrete.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<User> getAllUsers() {
        String expression = "/dataset/users/user";
        return toList(expression);
    }
    
    private List<User> toList(String expression){
        CityDAO cityDAO = CityDAO.getInstance();
        List<User> users  = new ArrayList<>();
        try {
            NodeList nodes = (NodeList)xpath.evaluate(expression,document,XPathConstants.NODESET);
            for(int i = 0;i<nodes.getLength();i++){
                Element element = (Element) nodes.item(i);
                int id = Integer.parseInt(element.getAttribute("id"));
                String firstName = element.getAttribute("firstName");
                String lastName = element.getAttribute("lastName");
                String cityId = element.getAttribute("cityId");
                String city = element.getAttribute("city");
                User user = new User(id, firstName, lastName, cityId,city);
                users.add(user);
            }
        } catch (XPathExpressionException ex) {
            Logger.getLogger(UserDAOConcrete.class.getName()).log(Level.SEVERE, null, ex);
        }
        return users;
    }
    
    private void transform(){
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(DATA_FILE);
            transformer.transform(source, result);
        } catch (TransformerException  ex) {
            Logger.getLogger(UserDAOConcrete.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
