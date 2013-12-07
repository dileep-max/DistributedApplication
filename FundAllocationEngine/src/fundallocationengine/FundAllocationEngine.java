package fundallocationengine;


import com.mongodb.DB;
import com.mongodb.MongoClient;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Abhinaya
 */
public class FundAllocationEngine {
   
    protected double allocatedAmount;
    protected double requestedAmount;
    
    public int requestId;
    public int stateId;
    public int departmentId;
    
    public MongoClient mongoClient;
    public DB dB;
    
    protected Connection connect = null;
    protected Statement statement = null;
    protected PreparedStatement preparedStatement = null;
    protected ResultSet resultSet = null; 
    
    // to get the request id of each request
    public int getRequestId() {
        return requestId;
    }
    
    // to set the request id of each request
    public void setRequestId(int requestId) {
        this.requestId = requestId;   
    }
    
    // to get the state id of each request
    public int getStateId() {
        return stateId;
    }
    
    // to set the state id of each request
    public void setStateId(int stateId) {
        this.stateId = stateId;
    }
    
    // to get the department id of each request
    public int getDepartmentId() {
        return departmentId;
    }
    
    // to set the department id of each request
    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }
    
    // constructor to set the request id, state id and requested amount for each request
    public FundAllocationEngine(int requestId,int stateId,double requestedAmount) {
        this.requestId = requestId;
        this.stateId = stateId;
        this.requestedAmount = requestedAmount;
        this.allocatedAmount = requestedAmount;
        try {
            // gets the my sql connection
        getRDBMSConnection();
       } catch(SQLException e) {
           System.out.println(e.getMessage());
       }
    }
    
    // to get the allocated amount of each request
    public double getAllocatedAmount() {
        return allocatedAmount;
    }

    // to set the allocated amount of each request
    public void setAllocatedAmount(double allocatedAmount) {
        DecimalFormat df = new DecimalFormat("#.##");
        allocatedAmount = Double.valueOf(df.format(allocatedAmount));
        this.allocatedAmount = allocatedAmount;
    }
    
    // to get the requested amount of each request
    public double getRequestedAmount() {
        return requestedAmount;
    }
    
    // to set the requested amount of each request
    public void setRequestedAmount(double requestedAmount) {
        this.requestedAmount = requestedAmount;
    }
    
    //  connect to the my sql db of fund
    public final void getRDBMSConnection() throws SQLException{
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://localhost/fund", "java","fundallocator");
        } catch(ClassNotFoundException | SQLException e) {
            System.out.println("DBConnection: " + e.getMessage());
        }
    }
    
    // executes all the rules of each department
    public double mainProcedure() {
        return 0.0;
        
    }
    
    // Update the request fund table with allocated amount
    public final void updateSQL() {
        try {
            preparedStatement = connect.prepareStatement("UPDATE FUND.REQUEST_HISTORY SET ALLOCATED_FUND = "+ getAllocatedAmount() + " WHERE REQUEST_ID = " + getRequestId());
            // , STATUS_ID = 2 set the status from servlet side
            preparedStatement.executeUpdate();
            System.out.println("Updated rows");
        //    preparedStatement = connect.prepareStatement("UPDATE FUND.FEDFUND SET FUND_AMOUNT = FUND_AMOUNT-"+ getAllocatedAmount() + " WHERE FUND_DESIGNATION = 'Federal Fund'");
        //    preparedStatement.executeUpdate();
        }catch(Exception e) {
            System.out.println(e.getMessage());
        }
        
    }
    
    public static void main(String[] args) {
        
    }  
    
}
