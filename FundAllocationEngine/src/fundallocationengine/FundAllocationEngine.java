package fundallocationengine;


import com.mongodb.DB;
import com.mongodb.MongoClient;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
    

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;   
    }
    
    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }
    
    
    public FundAllocationEngine(int requestId,int stateId,double requestedAmount) {
        this.requestId = requestId;
        this.stateId = stateId;
        this.requestedAmount = requestedAmount;
        this.allocatedAmount = requestedAmount;
        try {
        getRDBMSConnection();
       } catch(SQLException e) {
           System.out.println(e.getMessage());
       }
    }
    
    public double getAllocatedAmount() {
        return allocatedAmount;
    }

    public void setAllocatedAmount(double allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
    }

    public double getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(double requestedAmount) {
        this.requestedAmount = requestedAmount;
    }
    
    public final void getRDBMSConnection() throws SQLException{
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://localhost/fund", "java","fundallocator");
        } catch(ClassNotFoundException | SQLException e) {
            System.out.println("DBConnection: " + e.getMessage());
        }
    }

    public void mainProcedure() {
        
    }
    
    public final void updateSQL() {
        try {
            preparedStatement = connect.prepareStatement("UPDATE FUND.REQUEST_HISTORY SET ALLOCATED_FUND = "+ getAllocatedAmount() + ", STATUS_ID = 2 WHERE REQUEST_ID = " + getRequestId());
            preparedStatement.executeUpdate();
        //    preparedStatement = connect.prepareStatement("UPDATE FUND.FEDFUND SET FUND_AMOUNT = FUND_AMOUNT-"+ getAllocatedAmount() + " WHERE FUND_DESIGNATION = 'Federal Fund'");
        //    preparedStatement.executeUpdate();
        }catch(Exception e) {
            System.out.println(e.getMessage());
        }
        
    }
    
    public static void main(String[] args) {
        
    }  
    
}
