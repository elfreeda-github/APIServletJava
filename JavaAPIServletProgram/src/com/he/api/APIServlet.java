package com.he.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.he.api.*;


@WebServlet(urlPatterns = {"/users/*"}) // do not change this 
public class APIServlet extends HttpServlet {
	
    // private static final String DATA_FILE_PATH = "src/com/he/api/data.xml";
	// File datafile = new File(DATA_FILE_PATH);
	
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter output = response.getWriter();
        response.setContentType("application/json;charset=UTF-8");
        String path = request.getPathInfo();        
        
        
        // parse path and write JSON output to PrintWriter 'output'.
        UserDAO userDAO = UserDAOConcrete.getInstance();
            //System.out.println(path);
            if(path == null || path.equals("/")){
                output.write(JSON.toJSONArray(userDAO.getAllUsers()));
                return;
            }
            path = path.replaceAll("^/|/$", "");
            String comp[] = path.split("/");
            if(comp.length == 1){
                User user = userDAO.searchById(comp[0]);
                if(user == null ){
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Invalid id");
                }else {
                    output.write(JSON.toJSONObject(user));
                }
                return;
            }
            if(comp.length != 3) 
            {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Invalid request");
                return;
            }
            String action = comp[0];
            if(!action.equals("search"))
            {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Invalid action");
                return;
            }
            String criteria = comp[1];
            switch(criteria){
                case "city":
                    output.write(JSON.toJSONArray(userDAO.searchByCity(comp[2])));
                    break;
                case "firstName":
                    output.write(JSON.toJSONArray(userDAO.searchByFirstName(comp[2])));
                    break;
                case "lastName":
                    output.write(JSON.toJSONArray(userDAO.searchByLastName(comp[2])));
                    break;
                default :
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Invalid search criteria");
            }
        
        
        output.flush();
        output.close();
        
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getPathInfo();
        String firstName = request.getParameter("firstName");
        if(firstName.isEmpty()){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Invalid city");
            return;
        }
        String lastName = request.getParameter("lastName");
        if(lastName.isEmpty()){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Invalid city");
            return;
        }
        String city = request.getParameter("city");
        
        // validate City and make entry to data.xml
        if(path != null && !path.equals("/")){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Invalid request");
            return;
        }
        CityDAO cityDAO = CityDAO.getInstance();
        if(!cityDAO.validate(city)){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Invalid city");
            return;
        }
        UserDAO userDAO = UserDAOConcrete.getInstance();
        userDAO.insertUser(new User(0, firstName, lastName, cityDAO.getId(city),city));
                
    }
    
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getPathInfo();
        String firstName = request.getParameter("firstName");
        if(firstName.isEmpty()){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Invalid city");
            return;
        }
        String lastName = request.getParameter("lastName");
        if(lastName.isEmpty()){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Invalid city");
            return;
        }
        String city = request.getParameter("city");       
        // validate city and get cityId
        // insert new user in data.xml 
        CityDAO cityDAO = CityDAO.getInstance();
        UserDAO userDAO = UserDAOConcrete.getInstance();
        User user = userDAO.searchById(firstName);
        if(user == null){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Invalid request");
            return;
        }
        if(request.getParameter("city") != ""){
            if(!cityDAO.validate(request.getParameter("city")))
            {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Invalid city");
                return;
            }
            else 
            	user.setCityId(cityDAO.getId(request.getParameter("city")));
        }
        if(request.getParameter("firstName") != "")
            user.setFirstName(request.getParameter("firstName"));
        if(request.getParameter("lastName") != "")
            user.setLastName(request.getParameter("lastName"));
        if(request.getParameter("cityId") != "")
            user.setCityId(request.getParameter("cityId"));
        userDAO.updateUser(user);
                
    }
    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getPathInfo();
        if(path == null)
        {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Invalid request");
            return;
        }
        path = path.replaceAll("^/|/$", "");
        if(path.equals(""))
        {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Invalid request");
            return;
        }
        String id = path;
        // delete user with 'id' from data.xml 
        // if( user id is invalid  ) 
        // {
        //     response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Invalid id");
        //     return ;
        // }
        UserDAO userDAO = UserDAOConcrete.getInstance();
        User user = userDAO.searchById(id);
        if(user == null)
        {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Invalid id");
            return ;
        }
        userDAO.deleteUser(id);
                
    }
}
