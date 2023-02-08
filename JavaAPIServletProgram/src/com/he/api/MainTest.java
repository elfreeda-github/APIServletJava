///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.he.api;
//
//import java.io.ByteArrayInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.PrintWriter;
//import java.io.StringReader;
//import java.io.StringWriter;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.Collections;
//import java.util.Random;
//
//import javax.json.Json;
//import javax.json.JsonArray;
//import javax.json.JsonObject;
//import javax.json.JsonReader;
//import javax.servlet.ServletException;
//import javax.servlet.ServletInputStream;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.xml.parsers.ParserConfigurationException;
//
//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import static org.mockito.Mockito.*;
//import static org.junit.Assert.*;
//import static org.mockito.Matchers.*;
//import org.mockito.invocation.InvocationOnMock;
//import org.mockito.stubbing.Answer;
//
//import com.he.api.implementation.UserDAOConcrete;
//
//
//
///**
// *
// * @author divyansh
// */
//public class MainTest {
//    
//    private final static String DATA_FILE_PATH = "src/com/he/api/data.xml";
//    private final static String TEST_FILE_PATH = "test/com/he/api/data.xml";
//    private static String  originalData;
//    private static String testData;
//    private PrintWriter origWriter;
//    private PrintWriter testWriter;
//    public MainTest() {
//        
//    }
//    
//    @BeforeClass
//    public static void setUpClass() throws ParserConfigurationException, IOException {
//        originalData = new String(Files.readAllBytes(Paths.get(DATA_FILE_PATH)), StandardCharsets.UTF_8);
//        testData = new String(Files.readAllBytes(Paths.get(TEST_FILE_PATH)), StandardCharsets.UTF_8);
//    }
//    
//    
//    @AfterClass
//    public static void tearDownClass() {
//    }
//    
//    @Before
//    public void setUp() throws FileNotFoundException {
//    	MockitoAnnotations.initMocks(this);
//        origWriter = new PrintWriter(DATA_FILE_PATH);   
//        testWriter = new PrintWriter(TEST_FILE_PATH); 
//        origWriter.write(testData);
//        testWriter.write(originalData);
//        origWriter.close();
//        testWriter.close();
//    }
//    
//    @After
//    public void tearDown() throws FileNotFoundException {
//        origWriter = new PrintWriter(DATA_FILE_PATH);   
//        testWriter = new PrintWriter(TEST_FILE_PATH); 
//        origWriter.write(originalData);
//        testWriter.write(testData);
//        origWriter.close();
//        testWriter.close();
//    }
//    
//    @Mock HttpServletRequest request;
//    @Mock HttpServletResponse response;
//    @Mock HttpServletRequest requestReference;
//    @Mock HttpServletResponse responseReference;
//    
//    StringWriter expected;
//    StringWriter result;
//    void initMock(String pathInfo) throws IOException {
//    	expected = new StringWriter();
//    	result = new StringWriter();
//    	when(response.getWriter()).thenReturn(new PrintWriter(result));
//    	when(responseReference.getWriter()).thenReturn(new PrintWriter(expected));
//    	String url = String.format("http://localhost:8080/API/users%s", pathInfo);
//    	String url2 =  String.format("http://localhost:8080/API/users/%s", pathInfo);
//		StringBuffer sbf =  null;
//		if(pathInfo == null || pathInfo.isEmpty() || pathInfo.charAt(0) != '/') {
//			sbf = new StringBuffer(url2);
//		} else {
//			sbf = new StringBuffer(url);
//		}
//		when(request.getRequestURL()).thenReturn(sbf);
//    	when(request.getRequestURI()).thenReturn(pathInfo);
//    	when(request.getPathInfo()).thenReturn(pathInfo);
//    	when(requestReference.getPathInfo()).thenReturn(pathInfo);
//    	doNothing().when(response).sendError(anyInt(), anyString());
//    	doNothing().when(response).sendError(anyInt());
//    	doNothing().when(responseReference).sendError(anyInt(), anyString());
//    	doNothing().when(responseReference).sendError(anyInt());
//    	doNothing().when(response).setContentType(anyString());
//    	doNothing().when(responseReference).setContentType(anyString());
//    }
//    
//    // tests Read of CRUD
//    @Test
//    public void testGet() throws IOException
//    {
//    	// GET /
//    	{
//    		String pathInfo = "/";
//	    	initMock(pathInfo);
//	    	try {
//	    		new APIServlet().doGet(request, response);
//	    		new com.he.api.APIServlet().doGet(requestReference, responseReference);
//	    		try {
//	    			expected.close();
//	    		} catch(IOException f) {
//	    			//
//	    		}
//	    		try {
//	    			result.close();
//	    		} catch(IOException f) {
//	    			//
//	    		}
//	    	} catch(AssertionError | Exception e) {
//	    		fail();
//	    	}
//	    	JsonReader jsonReader = Json.createReader(new StringReader(expected.toString()));
//	    	JsonArray expectedArray = jsonReader.readArray();
//	    	jsonReader = Json.createReader(new StringReader(result.toString()));
//	    	JsonArray resultArray = jsonReader.readArray();
//	    	assertTrue(expectedArray.size() == resultArray.size());
//	    	assertTrue(expectedArray.containsAll(resultArray) && resultArray.containsAll(expectedArray));
////	    	System.out.println(resultArray);
////	    	System.out.println(expectedArray);
////	    	assertTrue(expectedArray.equals(resultArray));
//	    	verify(response, times(0)).sendError(anyInt());
//	    	verify(responseReference, times(0)).sendError(anyInt());
//	    	verify(response, times(0)).sendError(anyInt(),anyString());
//	    	verify(responseReference, times(0)).sendError(anyInt(),anyString());
//	    }
//    	// Get /#id
//    	{
//	    	Random rand = new Random();
//	    	for(int i = 0;i < 10;i++)
//	    	{
//	    		String pathInfo = "/" + Integer.toString(Math.abs(rand.nextInt())%1000 + 1);
////	    		System.out.println(pathInfo);
//		    	initMock(pathInfo);
//		    	try {
//		    		new APIServlet().doGet(request, response);
//		    		new com.he.api.APIServlet().doGet(requestReference, responseReference);
//		    		try {
//		    			expected.close();
//		    		} catch(IOException f) {
//		    			//
//		    		}
//		    		try {
//		    			result.close();
//		    		} catch(IOException f) {
//		    			//
//		    		}
//		    	} catch(AssertionError | Exception e) {
//		    		fail();
//		    	}
//		    	JsonReader jsonReader = Json.createReader(new StringReader(expected.toString()));
//		    	JsonObject expectedObject = jsonReader.readObject();
//		    	jsonReader = Json.createReader(new StringReader(result.toString()));
//		    	JsonObject resultObject = jsonReader.readObject();
////		    	System.out.println(resultObject);
////		    	System.out.println(expectedObject);
//		    	assertTrue(expectedObject.equals(resultObject));
//		    	verify(response, times(0)).sendError(anyInt());
//		    	verify(responseReference, times(0)).sendError(anyInt());
//		    	verify(response, times(0)).sendError(anyInt(),anyString());
//		    	verify(responseReference, times(0)).sendError(anyInt(),anyString());
//	    	}
//	    }
//    	
//    }
//    // tests Read of CRUD
//    @Test
//    public void testGetSearch() throws IOException
//    {
//    	// GET /search/firstName/#
//    	{
//    		String firstNames[] = {"lAVj", "DyEt", "Jtvw", "uSgd", "iVKq"};
//    		for(String firstName : firstNames)
//    		{
//	    		String pathInfo = "/search/firstName/"+firstName;
//		    	initMock(pathInfo);
//		    	try {
//		    		new APIServlet().doGet(request, response);
//		    		new com.he.api.APIServlet().doGet(requestReference, responseReference);
//		    		try {
//		    			expected.close();
//		    		} catch(IOException f) {
//		    			//
//		    		}
//		    		try {
//		    			result.close();
//		    		} catch(IOException f) {
//		    			//
//		    		}
//		    	} catch(AssertionError | Exception e) {
//		    		fail();
//		    	}
//		    	JsonReader jsonReader = Json.createReader(new StringReader(expected.toString()));
//		    	JsonArray expectedArray = jsonReader.readArray();
//		    	jsonReader = Json.createReader(new StringReader(result.toString()));
//		    	JsonArray resultArray = jsonReader.readArray();
//		    	assertTrue(expectedArray.size() == resultArray.size());
//		    	assertTrue(expectedArray.containsAll(resultArray) && resultArray.containsAll(expectedArray));
////		    	System.out.println(resultArray);
////		    	System.out.println(expectedArray);
//		    	verify(response, times(0)).sendError(anyInt());
//		    	verify(responseReference, times(0)).sendError(anyInt());
//		    	verify(response, times(0)).sendError(anyInt(),anyString());
//		    	verify(responseReference, times(0)).sendError(anyInt(),anyString());
//    		}
//	    }
//    	// GET /search/lastName/#
//    	{
//    		String lastNames[] = {"ROHX", "DyEt", "ufmr", "uSgd", "anOX"};
//    		for(String lastName : lastNames)
//    		{
//	    		String pathInfo = "/search/lastName/"+lastName;
//		    	initMock(pathInfo);
//		    	try {
//		    		new APIServlet().doGet(request, response);
//		    		new com.he.api.APIServlet().doGet(requestReference, responseReference);
//		    		try {
//		    			expected.close();
//		    		} catch(IOException f) {
//		    			//
//		    		}
//		    		try {
//		    			result.close();
//		    		} catch(IOException f) {
//		    			//
//		    		}
//		    	} catch(AssertionError | Exception e) {
//		    		fail();
//		    	}
//		    	JsonReader jsonReader = Json.createReader(new StringReader(expected.toString()));
//		    	JsonArray expectedArray = jsonReader.readArray();
//		    	jsonReader = Json.createReader(new StringReader(result.toString()));
//		    	JsonArray resultArray = jsonReader.readArray();
//		    	assertTrue(expectedArray.size() == resultArray.size());
//		    	assertTrue(expectedArray.containsAll(resultArray) && resultArray.containsAll(expectedArray));
////		    	System.out.println(resultArray);
////		    	System.out.println(expectedArray);
//		    	verify(response, times(0)).sendError(anyInt());
//		    	verify(responseReference, times(0)).sendError(anyInt());
//		    	verify(response, times(0)).sendError(anyInt(),anyString());
//		    	verify(responseReference, times(0)).sendError(anyInt(),anyString());
//    		}
//	    }
//    	// GET /search/city/#
//    	{
//    		String cities[] = {"Rajasthan", "Mizoram", "Kerala", "Karnataka", "Jharkhand"};
//    		for(String city : cities)
//    		{
//	    		String pathInfo = "/search/city/"+city;
//		    	initMock(pathInfo);
//		    	try {
//		    		new APIServlet().doGet(request, response);
//		    		new com.he.api.APIServlet().doGet(requestReference, responseReference);
//		    		try {
//		    			expected.close();
//		    		} catch(IOException f) {
//		    			//
//		    		}
//		    		try {
//		    			result.close();
//		    		} catch(IOException f) {
//		    			//
//		    		}
//		    	} catch(AssertionError | Exception e) {
//		    		fail();
//		    	}
//		    	JsonReader jsonReader = Json.createReader(new StringReader(expected.toString()));
//		    	JsonArray expectedArray = jsonReader.readArray();
//		    	jsonReader = Json.createReader(new StringReader(result.toString()));
//		    	JsonArray resultArray = jsonReader.readArray();
//		    	assertTrue(expectedArray.size() == resultArray.size());
//		    	assertTrue(expectedArray.containsAll(resultArray) && resultArray.containsAll(expectedArray));
////		    	System.out.println(resultArray);
////		    	System.out.println(expectedArray);
//		    	verify(response, times(0)).sendError(anyInt());
//		    	verify(responseReference, times(0)).sendError(anyInt());
//		    	verify(response, times(0)).sendError(anyInt(),anyString());
//		    	verify(responseReference, times(0)).sendError(anyInt(),anyString());
//    		}
//	    }
//    }
//    
//    // Tests Create of CRUD
//    @Test
//    public void testPost() throws IOException 
//    {
//    	{
//	    	String firstNames[] = {"Kisame", "Itachi", "Hashirama", "Minato", "Kushina"};
//	    	String lastNames[] = {"Hoshigaki", "Uchiha", "Senju", "Namikaze", "Uzumaki"};
//	    	String cities[] = {"Uttarakhand", "Kerala", "Punjab", "Maharashtra", "Tripura"};
//	    	String cityIds[] = {"28","13","21","15","26"};
//	    	String parameters = "firstName=%s&lastName=%s&city=%s";
//	    	String pathInfo = "/";
//	    	for(int i = 0;i < 5;i++)
//	    	{
//	    		initMock(pathInfo);
//	    		when(request.getParameter("firstName")).thenReturn(firstNames[i]);
//	    		when(request.getParameter("lastName")).thenReturn(lastNames[i]);
//	    		when(request.getParameter("city")).thenReturn(cities[i]);
//	//    		InputStream inputStream = new ByteArrayInputStream();
//	//    		InputStream inputStreamReference = new ByteArrayInputStream(String.format(parameters, firstNames[i],lastNames[i],cities[i]).getBytes());
//	    		String body = String.format(parameters, firstNames[i],lastNames[i],cities[i]);
//	    		InputStream requestIS = new ByteArrayInputStream(body.getBytes());
//	    		ServletInputStream sis = mock(ServletInputStream.class);
//	    		when(request.getInputStream()).thenReturn(new ServletInputStream() {
//					@Override
//					public int read() throws IOException {
//						return requestIS.read();
//					}
//					@Override public int available() throws IOException {
//						return requestIS.available();
//					}
//	    			@Override public void close() throws IOException {
//	    				requestIS.close();
//	    			}
//	    			
//	    			@Override public void mark(int readLimit) {
//	    				requestIS.mark(readLimit);
//	    			}
//					
//	    			@Override public boolean markSupported() {
//	    				return requestIS.markSupported();
//	    			}
//	    			
//	    			@Override public int read(byte[] b) throws IOException {
//	    				return requestIS.read(b);
//	    			}
//	    			
//	    			@Override public int read(byte[] b, int off, int len) throws IOException {
//	    				return requestIS.read(b,off,len);
//	    			}
//	    			
//	    			@Override public void reset() throws IOException {
//	    				requestIS.reset();
//	    			}
//	    			
//	    			@Override public long skip(long n) throws IOException {
//	    				return requestIS.skip(n);
//	    			}
//	    		});
//	    		
//	    		try {
//					new APIServlet().doPost(request, response);
//				} catch (ServletException e) {
//					e.printStackTrace();
//				}
//	    		verify(response, times(0)).sendError(anyInt());
//		    	verify(responseReference, times(0)).sendError(anyInt());
//		    	verify(response, times(0)).sendError(anyInt(),anyString());
//		    	verify(responseReference, times(0)).sendError(anyInt(),anyString());
//	    		com.he.api.implementation.UserDAOConcrete userdao = (UserDAOConcrete) com.he.api.implementation.UserDAOConcrete.getInstance();
//	    		assertTrue(i+1001 == userdao.getId_auto_increment());
//	    	}
//	    	com.he.api.implementation.UserDAO userdao = com.he.api.implementation.UserDAOConcrete.getInstance();
//			for(int i = 0;i<5;i++) {
//				com.he.api.User user = userdao.searchById(Integer.toString(i + 1000));
//				assertNotNull(user);
//				assertTrue(user.getFirstName().equals(firstNames[i]));
//				assertTrue(user.getLastName().equals(lastNames[i]));
//				assertTrue(user.getCityId().equals(cityIds[i]));
//			}
//    	}
//    	// invalid
//    	{
//	    	String firstNames[] = {"", "Itachi", "Hashirama", "", "Kushina"};
//	    	String lastNames[] = {"Hoshigaki", "", "Senju", "", "Uzumaki"};
//	    	String cities[] = {"asdscv", "Kerala", "aasfas", "", "asdsf"};
//	    	String cityIds[] = {"28","13","21","15","26"};
//	    	String parameters = "firstName=%s&lastName=%s&city=%s";
//	    	String pathInfo = "/";
//	    	for(int i = 0;i < 5;i++)
//	    	{
//	    		initMock(pathInfo);
//	    		when(request.getParameter("firstName")).thenReturn(firstNames[i]);
//	    		when(request.getParameter("lastName")).thenReturn(lastNames[i]);
//	    		when(request.getParameter("city")).thenReturn(cities[i]);
//	//    		InputStream inputStream = new ByteArrayInputStream();
//	//    		InputStream inputStreamReference = new ByteArrayInputStream(String.format(parameters, firstNames[i],lastNames[i],cities[i]).getBytes());
//	    		String body = String.format(parameters, firstNames[i],lastNames[i],cities[i]);
////	    		System.out.println(body);
//	    		InputStream requestIS = new ByteArrayInputStream(body.getBytes());
//	    		ServletInputStream sis = mock(ServletInputStream.class);
//	    		when(request.getInputStream()).thenReturn(new ServletInputStream() {
//					@Override
//					public int read() throws IOException {
//						return requestIS.read();
//					}
//					@Override public int available() throws IOException {
//						return requestIS.available();
//					}
//	    			@Override public void close() throws IOException {
//	    				requestIS.close();
//	    			}
//	    			
//	    			@Override public void mark(int readLimit) {
//	    				requestIS.mark(readLimit);
//	    			}
//					
//	    			@Override public boolean markSupported() {
//	    				return requestIS.markSupported();
//	    			}
//	    			
//	    			@Override public int read(byte[] b) throws IOException {
//	    				return requestIS.read(b);
//	    			}
//	    			
//	    			@Override public int read(byte[] b, int off, int len) throws IOException {
//	    				return requestIS.read(b,off,len);
//	    			}
//	    			
//	    			@Override public void reset() throws IOException {
//	    				requestIS.reset();
//	    			}
//	    			
//	    			@Override public long skip(long n) throws IOException {
//	    				return requestIS.skip(n);
//	    			}
//	    		});
//	    		
//	    		try {
//					new APIServlet().doPost(request, response);
//				} catch (ServletException e) {
//					e.printStackTrace();
//				}
//	    		try {
//		    		try {
//		    			verify(response, atLeast(1)).sendError(400);
//		    		} catch(AssertionError e) {
//			    		verify(response, atLeast(1)).sendError(eq(400),anyString());
//		    		} 
//	    		} catch(AssertionError f) {
//	    			fail();
//	    		}
//	    		com.he.api.implementation.UserDAOConcrete userdao = (UserDAOConcrete) com.he.api.implementation.UserDAOConcrete.getInstance();
//	    		assertEquals(1005, userdao.getId_auto_increment());
//	    	}
//    	}
//    
//    }
// // Tests Update of CRUD
//    @Test
//    public void testPut() throws IOException 
//    {
//    	//valid
//    	{
//	    	String ids[] = {"1","41","512","65","564"};
//	    	String firstNames[] = {"Kisame", "Jtvw", "Hashirama", "Minato", "VxgS"};
//	    	String lastNames[] = {"Hoshigaki", "Uchiha", "UYve", "HRpf", "Fgvy"};
//	    	String cities[] = {"Uttarakhand", "Kerala", "Punjab", "Chhattisgarh", "Tripura"};
//	    	String cityIds[] = {"28","13","21","5","26"};
//	    	String parameters[] = {"firstName=Kisame\nlastName=Hoshigaki\ncity=Uttarakhand",
//	    			"lastName=Uchiha\ncity=Kerala",
//	    			"firstName=Hashirama\ncity=Punjab",
//	    			"firstName=Minato", 
//	    			"city=Tripura"};
//	    	for(int i = 0;i < 5;i++)
//	    	{
//	    		String pathInfo = "/"+ids[i];
//	    		initMock(pathInfo);
//	//    		InputStream inputStream = new ByteArrayInputStream();
//	//    		InputStream inputStreamReference = new ByteArrayInputStream(String.format(parameters, firstNames[i],lastNames[i],cities[i]).getBytes());
//	    		String body = parameters[i];
//	    		InputStream requestIS = new ByteArrayInputStream(body.getBytes());
//	    		when(request.getInputStream()).thenReturn(new ServletInputStream() {
//					@Override
//					public int read() throws IOException {
//						return requestIS.read();
//					}
//					@Override public int available() throws IOException {
//						return requestIS.available();
//					}
//	    			@Override public void close() throws IOException {
//	    				requestIS.close();
//	    			}
//	    			
//	    			@Override public void mark(int readLimit) {
//	    				requestIS.mark(readLimit);
//	    			}
//					
//	    			@Override public boolean markSupported() {
//	    				return requestIS.markSupported();
//	    			}
//	    			
//	    			@Override public int read(byte[] b) throws IOException {
//	    				return requestIS.read(b);
//	    			}
//	    			
//	    			@Override public int read(byte[] b, int off, int len) throws IOException {
//	    				return requestIS.read(b,off,len);
//	    			}
//	    			
//	    			@Override public void reset() throws IOException {
//	    				requestIS.reset();
//	    			}
//	    			
//	    			@Override public long skip(long n) throws IOException {
//	    				return requestIS.skip(n);
//	    			}
//	    		});
//	    		
//	    		try {
//					new APIServlet().doPut(request, response);
//				} catch (ServletException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//	    		verify(response, times(0)).sendError(anyInt());
//		    	verify(responseReference, times(0)).sendError(anyInt());
//		    	verify(response, times(0)).sendError(anyInt(),anyString());
//		    	verify(responseReference, times(0)).sendError(anyInt(),anyString());
//	    	}
//	    	
//	    	com.he.api.implementation.UserDAOConcrete userdao = (UserDAOConcrete) com.he.api.implementation.UserDAOConcrete.getInstance();
//			for(int i = 0;i<5;i++) {
//				try {
//				com.he.api.User user = userdao.searchById(ids[i].replaceAll("/", ""));
//				assertNotNull(user);
//				assertTrue(user.getFirstName().equals(firstNames[i]));
//				assertTrue(user.getLastName().equals(lastNames[i]));
//				assertTrue(user.getCityId().equals(cityIds[i]));
//				} catch(AssertionError e) {
//					fail();
//				}
//			}
//    	}
//		// invalid
//		{
//			String ids[] = {"2","123","412","abc","/search/"};
//	    	String parameters[] = 
//	    		{"firstName=\nlastName=Hoshigaki\ncity=Uttarakhand",
//	    			"lastName=Uchiha\ncity=abcdf",
//	    			"firstName=Hashirama\ncity=bdcd",
//	    			"firstName=Minato", 
//	    			"city=Tripura"};
//	    	for(int i = 0;i < 5;i++)
//	    	{
//	    		String pathInfo = "/"+ids[i];
//	    		initMock(pathInfo);
//	//    		InputStream inputStream = new ByteArrayInputStream();
//	//    		InputStream inputStreamReference = new ByteArrayInputStream(String.format(parameters, firstNames[i],lastNames[i],cities[i]).getBytes());
//	    		String body = parameters[i];
//	    		InputStream requestIS = new ByteArrayInputStream(body.getBytes());
//	    		ServletInputStream sis = mock(ServletInputStream.class);
//	    		when(request.getInputStream()).thenReturn(new ServletInputStream() {
//					@Override
//					public int read() throws IOException {
//						return requestIS.read();
//					}
//					@Override public int available() throws IOException {
//						return requestIS.available();
//					}
//	    			@Override public void close() throws IOException {
//	    				requestIS.close();
//	    			}
//	    			
//	    			@Override public void mark(int readLimit) {
//	    				requestIS.mark(readLimit);
//	    			}
//					
//	    			@Override public boolean markSupported() {
//	    				return requestIS.markSupported();
//	    			}
//	    			
//	    			@Override public int read(byte[] b) throws IOException {
//	    				return requestIS.read(b);
//	    			}
//	    			
//	    			@Override public int read(byte[] b, int off, int len) throws IOException {
//	    				return requestIS.read(b,off,len);
//	    			}
//	    			
//	    			@Override public void reset() throws IOException {
//	    				requestIS.reset();
//	    			}
//	    			
//	    			@Override public long skip(long n) throws IOException {
//	    				return requestIS.skip(n);
//	    			}
//	    		});
//	    		
//	    		try {
//					new APIServlet().doPut(request, response);
//				} catch (ServletException e) {
//					e.printStackTrace();
//				}
//	    		try {
//		    		try {
//		    			verify(response, atLeast(1)).sendError(anyInt());
//		    		} catch(AssertionError e) {
//			    		verify(response, atLeast(1)).sendError(anyInt(),anyString());
//		    		} 
//	    		} catch(AssertionError f) {
//	    			fail();
//	    		}
//	    	}
//		}
//    }
//    
//    
//    // tests Delete of CRUD
//    @Test
//    public void testDelete() throws IOException 
//    {
//    	String ids[] = {"2","123","24","12","512","643","251"};
//    	for(String id :ids ) {
//    		String pathInfo = "/"+id;
//    		initMock(pathInfo);
//    		try {
//				new APIServlet().doDelete(request, response);
//			} catch (ServletException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//    		com.he.api.implementation.UserDAO userdao = com.he.api.implementation.UserDAOConcrete.getInstance();
//    		assertNull(userdao.searchById(id));
//    		verify(response, times(0)).sendError(anyInt());
//	    	verify(responseReference, times(0)).sendError(anyInt());
//	    	verify(response, times(0)).sendError(anyInt(),anyString());
//	    	verify(responseReference, times(0)).sendError(anyInt(),anyString());
//    	}
//    	
//    	//invalid
//    	for(String id :ids ) {
//    		String pathInfo = "/"+id;
//    		initMock(pathInfo);
//    		try {
//				new APIServlet().doDelete(request, response);
//			} catch (ServletException e) {
//				e.printStackTrace();
//			}
//    		com.he.api.implementation.UserDAO userdao = com.he.api.implementation.UserDAOConcrete.getInstance();
//    		assertNull(userdao.searchById(id));
//    		try {
//	    		try {
//	    			verify(response, atLeast(1)).sendError(anyInt());
//	    		} catch(AssertionError e) {
//		    		verify(response, atLeast(1)).sendError(anyInt(),anyString());
//	    		} 
//    		} catch(AssertionError f) {
//    			fail();
//    		}
//    	}
//    }
//}
//
