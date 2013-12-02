/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.fund.business.FilterRuleEntity;
//import com.fund.business.StateEntity;
import com.fund.entity.State;
import com.fund.entity.Department;
import com.fund.util.ClientUtility;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.io.PrintWriter;
import javax.naming.InitialContext;


/**
 *
 * @author vis
 */
public class stateScreen extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        //List<State> states;
        try {
            //Setup JDBC
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/fund", "java", "fundallocator");
            
            
            //Query the database
            String selectSQL = "SELECT department_id, department_name FROM department";
            String selectSQL2 = "SELECT state_code, state_name FROM state";
            PreparedStatement preparedStatement = con.prepareStatement(selectSQL);
            ResultSet rs = preparedStatement.executeQuery(selectSQL);
            List<Department> departments = new ArrayList();
            while (rs.next()){
                String deptid = rs.getString("department_id");
                int deptId = Integer.parseInt(deptid);
                String deptname = rs.getString("department_name");
                Department d = new Department(deptId);
                d.setDepartmentName(deptname);
                departments.add(d);
            }
            preparedStatement = con.prepareStatement(selectSQL2);
            rs = preparedStatement.executeQuery(selectSQL2);
            List<State> states = new ArrayList();
            while (rs.next()){
                String stateid = rs.getString("state_code");
                int stateId = Integer.parseInt(stateid);
                String statename = rs.getString("state_name");
                State s = new State(stateId);
                s.setStateName(statename);
                states.add(s);
            }
            con.close();
            
            //forward to jsp page
            request.setAttribute("departments", departments);
            request.getSession().setAttribute("states", states);
            request.getRequestDispatcher("/WEB-INF/stateScreen.jsp").forward(request, response);               
            }
            catch (ClassNotFoundException e1) {
            // JDBC driver class not found, print error message to the console
            out.println(e1.toString());
            }
            catch (SQLException e2) {
            // Exception when executing java.sql related commands, print error message to the console
            out.println(e2.toString());
            }
            catch (Exception e3) {
             // other unexpected exception, print error message to the console
            out.println(e3.toString());
            }
//        try             
//            {
//                InitialContext initContext = ClientUtility.getContext();
//                StateEntity stateEntity = (StateEntity) initContext.doLookup(ClientUtility.getStateLookupName());
//                states = stateEntity.getAllStates();
//                int i = 0;
//                while (i < states.size()) {
//                    System.out.println(i + " StateCode: " + states.get(i).getStateCode() + " StateName: " + states.get(i++).getStateName() + "\n");
//                }
//                request.getSession().setAttribute("states", states);
//                request.getRequestDispatcher("/WEB-INF/stateScreen.jsp").forward(request, response);               
//            }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);       
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //forward to request confirmation page
        String state = request.getParameter("State");
        int stateValue = Integer.parseInt(state);
        String department = request.getParameter("Department");
        int departmentValue = Integer.parseInt(department);
        String amount = request.getParameter("quantity");
        float amountValue = Float.parseFloat(amount);
        request.setAttribute("amount", amount);   
        request.getSession().setAttribute("state", state);
        request.getSession().setAttribute("department", department);
        request.getRequestDispatcher("/WEB-INF/confirmation.jsp").forward(request, response);
        //TODO insert code to call bean
                try {
            {
                InitialContext initContext = ClientUtility.getContext();
                FilterRuleEntity filterRuleEntity = (FilterRuleEntity) initContext.doLookup(ClientUtility.getLookupName());
                filterRuleEntity.processRequest(stateValue , departmentValue, amountValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Main state fund request screen.";
    }// </editor-fold>
}
