/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package loginpkg;

import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.ArrayList;

import fundpkg.RequestHistory;
import fundpkg.Users;
import fundpkg.State;
import fundpkg.Fedfund;
import fundpkg.Department;
import fundpkg.Status;
import fundpkgfacade.RequestHistoryFacadeLocal;import fundpkgfacade.DepartmentFacadeLocal;
import fundpkgfacade.FedfundFacadeLocal;

import fundpkgfacade.StateFacadeLocal;
import fundpkgfacade.StatusFacadeLocal;
import fundpkgfacade.UsersFacadeLocal;
import javax.servlet.RequestDispatcher;
/**
 *
 * @author Dileep Kumar
 */
@WebServlet(name = "loginallocator", urlPatterns = {"/loginallocator"})
public class loginallocator extends HttpServlet {
    @EJB
    private StatusFacadeLocal statusFacade;
    @EJB
    private FedfundFacadeLocal fedfundFacade;
    @EJB
    private StateFacadeLocal stateFacade;
    @EJB
    private DepartmentFacadeLocal departmentFacade;
    @EJB
    private RequestHistoryFacadeLocal requestHistoryFacade;
    @EJB
    private UsersFacadeLocal usersFacade;

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            
            //Fetching from the users table
            Users userObj = new Users(); //create a user object
            userObj.setFirstName(request.getParameter("username")); //get the username from text input field
            Users dbObj = usersFacade.find(59); //59 is the user id for fund allocator
            
            //Fetch the fund
            Fedfund fedfundObj = new Fedfund();
            List<Fedfund> fundList = fedfundFacade.findAll();
            
            String fundAmount = "NA";
           
            
            for(Fedfund f : fundList)
            {
                if(f.getFundDesignation().equals("Federal Fund"))
                {
                    fundAmount = f.getFundAmount().toString();
                }
            }
            
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Fund Requests</title>");            
            out.println("</head>");
            out.println("<body>");
            if(dbObj.getPassword().equals(request.getParameter("password")) || (request.getParameter("username").equals("fund") && request.getParameter("direct").equals("1"))) //checking the password
            {
                out.println("<br>");
                out.println("<center><h2>Fund Allocation page</h2>");
                out.println("<h3>Federal Amount available in $: "+fundAmount+"</h3>");
                
                
                RequestHistory requestObj = new RequestHistory();
                //Fetching all the requests
                List<RequestHistory> requestList = new ArrayList<RequestHistory>();
                
                List<RequestHistory> pendingList = new ArrayList<RequestHistory>();
                List<RequestHistory> processedList = new ArrayList<RequestHistory>();
                List<RequestHistory> approvedList = new ArrayList<RequestHistory>();
                List<RequestHistory> discardedList = new ArrayList<RequestHistory>();
                
                requestList = requestHistoryFacade.findAll();
                
                State stateObj = new State();
                Department deptObj = new Department();
                
                for(RequestHistory iVar : requestList)
                {
                    
                    if(iVar.getStatusId().getStatusValue().equals("approved")) //approved
                    {
                        approvedList.add(iVar);
                    }
                    else if(iVar.getStatusId().getStatusValue().equals("processed")) //processed
                    {
                        processedList.add(iVar);
                    }
                    else if(iVar.getStatusId().getStatusValue().equals("discarded")) //discarded
                    {
                        discardedList.add(iVar);
                    }
                    else if(iVar.getStatusId().getStatusValue().equals("pending")) //pending
                    {
                        /* Pending Requests */
                        pendingList.add(iVar);
                        
                    }
                    
                } 
                
                
                /* Pending Requests */
                
                if(pendingList.size()>0)
                {
                        out.println("<h4>Pending Requests</h4>");
                        out.println("<form action='process' method ='POST'>");
                        out.println("<table border='1'>");
                        out.println("<thead>");
                        out.println("<td>Select</td>");
                        out.println("<td>Request Id</td>");
                        out.println("<td>State</td>");
                        out.println("<td>Department</td>");
                        out.println("<td>Fund Requested</td>");
                        out.println("<td>Request Status</td>");                
                        out.println("</thead>");

                        out.println("<tbody>");
                    
                    for(RequestHistory iVar : pendingList)
                    {
                        //stateObj = stateFacade.find((iVar.getStateId()));
                        deptObj = departmentFacade.find(iVar.getDepartmentId());
                        
                        out.println("<tr>");
                        out.println("<td><input type='checkbox' name= 'checked' value="+iVar.getRequestId()+" /></td>");
                        out.println("<td>"+iVar.getRequestId()+"</td>");
                        out.println("<td>"+iVar.getStateId().getStateName()+"</td>");
                        out.println("<td>"+deptObj.getDepartmentName()+"</td>");
                        out.println("<td>"+iVar.getRequestedFund()+"</td>");
                        out.println("<td>"+iVar.getStatusId().getStatusValue()+"</td>");
                        out.println("</tr>");
                    }
                        out.println("</tbody>");
                        out.println("</table>");
                        out.print("<input type=\"submit\" name=\"process\" value=\"Process\" />");
                        out.println("</form>");                    
                }
                    
                
                
                /* Processed Requests */
                
                //out.println("<br>");
                if(processedList.size()>0)
                {
                    out.println("<h4>Processed Requests</h4>");

                    out.println("<form action='process' method ='POST'>");
                    out.println("<table border='1'>");
                    out.println("<thead>");
                    out.println("<td>Select</td>");
                    out.println("<td>Request Id</td>");
                    out.println("<td>State</td>");
                    out.println("<td>Department</td>");
                    out.println("<td>Fund Requested</td>");
                    out.println("<td>Allocated Fund</td>");
                    out.println("<td>Request Status</td>");                
                    out.println("</thead>");

                    out.println("<tbody>");

                    for(RequestHistory iVar : processedList)
                    {

                        //stateObj = stateFacade.find((iVar.getStateId()));
                        deptObj = departmentFacade.find(iVar.getDepartmentId());
                        
                        out.println("<tr>");
                        out.println("<td><input type='checkbox' name= 'checkedP' value="+iVar.getRequestId()+" /></td>");
                        out.println("<td>"+iVar.getRequestId()+"</td>");
                        out.println("<td>"+iVar.getStateId().getStateName()+"</td>");
                        out.println("<td>"+deptObj.getDepartmentName()+"</td>");
                        out.println("<td>"+iVar.getRequestedFund()+"</td>");
                        out.println("<td>"+iVar.getAllocatedFund()+"</td>");
                        out.println("<td>"+iVar.getStatusId().getStatusValue()+"</td>");
                        out.println("</tr>");

                    }

                    //out.println("<tr>");

                    //out.println("</tr>");
                    out.println("</tbody>");
                    out.println("</table>");

                    out.println("<table>");
                    out.println("<tr>");
                    out.println("<td><input type=\"submit\" name=\"approve\" value=\"Approve\" /></td>");
                    out.println("<td><input type=\"submit\" name=\"reject\" value=\"Reject\" /></td>");
                    out.println("</tr>");
                    out.println("</table>");
                    out.println("</form>");

                }
                
                
                 /* Approved Requests */

                if(approvedList.size()>0)
                {
                    out.println("<h4>Approved Requests</h4>");
                    out.println("<table border='1'>");
                    out.println("<thead>");
                    out.println("<td>Request Id</td>");
                    out.println("<td>State</td>");
                    out.println("<td>Department</td>");
                    out.println("<td>Fund Requested</td>");
                    out.println("<td>Allocated Fund</td>");
                    out.println("<td>Request Status</td>");                
                    out.println("</thead>");

                    out.println("<tbody>");

                    for(RequestHistory iVar : approvedList)
                    {

                        //stateObj = stateFacade.find((iVar.getStateId()));
                        deptObj = departmentFacade.find(iVar.getDepartmentId());
                        
                        out.println("<tr>");
                        out.println("<td>"+iVar.getRequestId()+"</td>");
                        out.println("<td>"+iVar.getStateId().getStateName()+"</td>");
                        out.println("<td>"+deptObj.getDepartmentName()+"</td>");
                        out.println("<td>"+iVar.getRequestedFund()+"</td>");
                        out.println("<td>"+iVar.getAllocatedFund()+"</td>");
                        out.println("<td>"+iVar.getStatusId().getStatusValue()+"</td>");
                        out.println("</tr>");

                    }

                    out.println("</tbody>");
                    out.println("</table>");

                    /* Discarded Requests */

                    //out.println("<br>");
                }
                
                
                
                
                 if(discardedList.size()>0)
                 {
                    out.println("<h4>Discarded Requests</h4>");
                    out.println("<table border='1'>");
                    out.println("<thead>");
                    out.println("<td>Request Id</td>");
                    out.println("<td>State</td>");
                    out.println("<td>Department</td>");
                    out.println("<td>Fund Requested</td>");
                    out.println("<td>Allocated Fund</td>");
                    out.println("<td>Request Status</td>");                
                    out.println("</thead>");
                    out.println("<tbody>");
                    for(RequestHistory iVar : discardedList)
                    {

                        //stateObj = stateFacade.find((iVar.getStateId()));
                        deptObj = departmentFacade.find(iVar.getDepartmentId());
                        
                        out.println("<tr>");
                        out.println("<td>"+iVar.getRequestId()+"</td>");
                        out.println("<td>"+iVar.getStateId().getStateName()+"</td>");
                        out.println("<td>"+deptObj.getDepartmentName()+"</td>");
                        out.println("<td>"+iVar.getRequestedFund()+"</td>");
                        out.println("<td>"+iVar.getAllocatedFund()+"</td>");
                        out.println("<td>"+iVar.getStatusId().getStatusValue()+"</td>");
                        out.println("</tr>");

                    }

                    out.println("</tbody>");
                    out.println("</table>");

                }
            }
            else
            {
                out.println("<center><h2>Login unsuccessful</h2>");
                out.println("<h3>The username or password is incorrect</h3>");
            }
            out.println("</center>");
            out.println("</body>");
            out.println("</html>");
        } finally {            
            out.close();
        }
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
        
        
        //http://stackoverflow.com/questions/14723812/how-do-i-call-a-java-method-on-button-click-event-of-jsp-or-html
        
        /*if(request.getParameter("process")!=null)
        {
            //request.getRequestDispatcher("/process").forward(request, response);  
            RequestDispatcher disp = request.getRequestDispatcher("/process");  
            disp.forward(request, response);  
        }
        else*/
        processRequest(request, response);
        
        
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
