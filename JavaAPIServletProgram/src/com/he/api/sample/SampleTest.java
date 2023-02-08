/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.he.api.sample;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.he.api.APIServlet;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SampleTest {
    
    
    private final static String DATA_FILE_PATH = "src/com/he/api/data.xml";
    private final static String TEST_FILE_PATH = "test/com/he/api/sample/data.xml";
    private static String  originalData;
    private static String testData;
    private PrintWriter origWriter;
    private PrintWriter testWriter;
    public SampleTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws ParserConfigurationException, IOException {
        originalData = new String(Files.readAllBytes(Paths.get(DATA_FILE_PATH)), StandardCharsets.UTF_8);
        testData = new String(Files.readAllBytes(Paths.get(TEST_FILE_PATH)), StandardCharsets.UTF_8);
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() throws FileNotFoundException {
        origWriter = new PrintWriter(DATA_FILE_PATH);   
        testWriter = new PrintWriter(TEST_FILE_PATH); 
        origWriter.write(testData);
        testWriter.write(originalData);
        origWriter.close();
        testWriter.close();
        
    }
    
    @After
    public void tearDown() throws FileNotFoundException {
        origWriter = new PrintWriter(DATA_FILE_PATH);   
        testWriter = new PrintWriter(TEST_FILE_PATH); 
        origWriter.write(originalData);
        testWriter.write(testData);
        origWriter.close();
        testWriter.close();
    }
    

    @Test
    public void testDoGetValidResponseCode() throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
    	HttpServletRequest request = mock(HttpServletRequest.class);
    	HttpServletResponse response = mock(HttpServletResponse.class);
    	PrintWriter printWriter = mock(PrintWriter.class);
    	when(response.getWriter()).thenReturn(printWriter);
    	doNothing().when(response).sendError(anyInt());
    	doNothing().when(response).sendError(anyInt(),anyString());
    	Class[] param = {HttpServletRequest.class, HttpServletResponse.class};
    	Method doGet = APIServlet.class.getDeclaredMethod("doGet", param);
    	doGet.setAccessible(true);
    	APIServlet instance = new APIServlet();
    	String pathInfos[] = {"/search/firstName/Hacker/", "/search/lastName/Hacker/","/","/1"};
    	for(int i = 0;i < pathInfos.length;i++) {
    		String pathInfo = pathInfos[i];
    		StringBuffer sbf =  new StringBuffer(String.format("http://localhost:8080/API/users%s", pathInfo));
    		when(request.getRequestURL()).thenReturn(sbf);
        	when(request.getRequestURI()).thenReturn(pathInfo);
        	when(request.getPathInfo()).thenReturn(pathInfo);
        	doGet.invoke(instance, request, response);
        	try {
    	    	verify(response, times(0)).sendError(anyInt());
    	    	verify(response, times(0)).sendError(anyInt(), anyString());
        	} catch(AssertionError e) {
        		fail("Response 200 OK expected");
        	}
    	}
    }
    
    @Test
    public void testDoGetInvalidResponseCode() throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
    	HttpServletRequest request = mock(HttpServletRequest.class);
    	HttpServletResponse response = mock(HttpServletResponse.class);
    	PrintWriter printWriter = mock(PrintWriter.class);
    	when(response.getWriter()).thenReturn(printWriter);
    	doNothing().when(response).sendError(anyInt());
    	doNothing().when(response).sendError(anyInt(),anyString());
    	Class[] param = {HttpServletRequest.class, HttpServletResponse.class};
    	Method doGet = APIServlet.class.getDeclaredMethod("doGet", param);
    	doGet.setAccessible(true);
    	APIServlet instance = new APIServlet();
    	String pathInfos[] = {"/users/123/14", "124/1234","/gasdr"};
    	for(int i = 0;i < pathInfos.length;i++) {
    		String pathInfo = pathInfos[i];
    		StringBuffer sbf =  new StringBuffer(String.format("http://localhost:8080/API/users%s", pathInfo));
    		when(request.getRequestURL()).thenReturn(sbf);
        	when(request.getRequestURI()).thenReturn(pathInfo);
        	when(request.getPathInfo()).thenReturn(pathInfo);
        	doGet.invoke(instance, request, response);
        	try {
        		try {
        			verify(response, atLeast(1)).sendError(400);
        		} catch(AssertionError f) {
        	    	verify(response, atLeast(1)).sendError(eq(400), anyString());
        		}
        	} catch(AssertionError e) {
        		fail("Response 400 Bad Request Error expected");
        	}
    	}
    } 
    
}
