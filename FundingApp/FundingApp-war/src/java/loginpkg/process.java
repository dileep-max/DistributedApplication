/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package loginpkg;

import fundpkg.Fedfund;
import fundpkg.RequestHistory;
import fundpkg.Status;
import fundpkgfacade.FedfundFacadeLocal;
import fundpkgfacade.RequestHistoryFacadeLocal;
import fundpkgfacade.StatusFacadeLocal;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import fundallocationengine.*;
/**
 *
 * @author Dileep Kumar
 */
@WebServlet(name = "process", urlPatterns = {"/process"})
public class process extends HttpServlet {
    @EJB
    private StatusFacadeLocal statusFacade;
    @EJB
    private FedfundFacadeLocal fedfundFacade;
    @EJB
    private RequestHistoryFacadeLocal requestHistoryFacade2;

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
        try {

            /* TODO output your page here. You may use following sample code. */
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet process</title>");            
            out.println("</head>");
            out.println("<body>");
            //out.println("<h1>Servlet process at " + request.getContextPath() + "</h1>");
            //out.println("<h1>Servlet process at " + request.getParameter("checked") + "</h1>");
            //http://www.coderanch.com/t/490572/Servlets/java/Call-servlet-servlet
            /*
            RequestDispatcher disp = request.getRequestDispatcher("/loginallocator");  
            disp.forward(request, response);  
            */
            
            if(request.getParameter("checked")!=null && request.getParameter("process")!=null)
            {
                //request_id,state_id,requested_amount,department_id
                RequestHistory updReqObj = new RequestHistory();
                RequestHistory remainingValObj = requestHistoryFacade2.find(Integer.parseInt(request.getParameter("checked")));
                Status statusObj = new Status();

                
                EducationAllocationEngine eAE = new EducationAllocationEngine(Integer.parseInt(request.getParameter("checked")),
                        remainingValObj.getStateId().getStateCode(),remainingValObj.getRequestedFund());
                double allocated_amt = eAE.mainProcedure();
                System.out.println("Processed successfully");
                
                RequestHistory remainingValObj1 = requestHistoryFacade2.find(Integer.parseInt(request.getParameter("checked")));
                statusObj.setStatusId(2);
                statusObj.setStatusValue("processed");
                
                updReqObj.setRequestId(Integer.parseInt(request.getParameter("checked")));
                updReqObj.setAllocatedFund(allocated_amt);//remainingValObj1.getAllocatedFund());
                updReqObj.setDepartmentId(remainingValObj1.getDepartmentId());
                updReqObj.setRequestedFund(remainingValObj1.getRequestedFund());
                updReqObj.setStateId(remainingValObj1.getStateId());
                updReqObj.setStatusId(statusObj);
                requestHistoryFacade2.edit(updReqObj); //Updating the request status
                
            }
            //approving the request
            else if(request.getParameter("checkedP")!=null && request.getParameter("approve")!=null)
            {
                RequestHistory updReqObj = new RequestHistory();
                RequestHistory remainingValObj = requestHistoryFacade2.find(Integer.parseInt(request.getParameter("checkedP")));
                
                //Fetch the fund
                Fedfund fedfundObj = new Fedfund();
                List<Fedfund> fundList = fedfundFacade.findAll();
                Integer fundId = -1;
                for(Fedfund f : fundList)
                {
                    if(f.getFundDesignation().equals("Federal Fund"))
                    {
                        fundId = f.getFundId();
                    }
                }
                
                Fedfund balObj = fedfundFacade.find(fundId);
                Fedfund setBalObj = new Fedfund();
                //Subtracting fund amount with allocated amount
                BigInteger b1 = balObj.getFundAmount(); //total fund amount
                BigInteger b2 =(BigInteger.valueOf(remainingValObj.getAllocatedFund().longValue())); //allocated amount
                BigInteger b3 = b1.subtract(b2);
                setBalObj.setFundAmount(b3);
                setBalObj.setFundDesignation("Federal Fund");
                setBalObj.setFundId(fundId);
                fedfundFacade.edit(setBalObj);
                Status statusObj = new Status();
                statusObj.setStatusId(Integer.valueOf(1));
                statusObj.setStatusValue("approved");
                
                //Set Values
                updReqObj.setRequestId(Integer.parseInt(request.getParameter("checkedP")));
                updReqObj.setAllocatedFund(remainingValObj.getAllocatedFund());
                updReqObj.setDepartmentId(remainingValObj.getDepartmentId());
                updReqObj.setRequestedFund(remainingValObj.getRequestedFund());
                updReqObj.setStateId(remainingValObj.getStateId());
                //System.out.println("I m here");
                updReqObj.setStatusId(statusObj);
                requestHistoryFacade2.edit(updReqObj); //Updating the request status
            }
            //reject the request
            else if(request.getParameter("checkedP")!=null && request.getParameter("reject")!=null)
            {
                RequestHistory updReqObj = new RequestHistory();
                RequestHistory remainingValObj = requestHistoryFacade2.find(Integer.parseInt(request.getParameter("checkedP")));
                Status statusObj = new Status();
                statusObj.setStatusId(3);
                statusObj.setStatusValue("discarded");
                
                updReqObj.setRequestId(Integer.parseInt(request.getParameter("checkedP")));
                updReqObj.setAllocatedFund(remainingValObj.getAllocatedFund());
                updReqObj.setDepartmentId(remainingValObj.getDepartmentId());
                updReqObj.setRequestedFund(remainingValObj.getRequestedFund());
                updReqObj.setStateId(remainingValObj.getStateId());
                updReqObj.setStatusId(statusObj);
                requestHistoryFacade2.edit(updReqObj); //Updating the request status
            }
            
            
            response.sendRedirect(request.getContextPath()+"/loginallocator?username=fund&direct=1");
            
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
