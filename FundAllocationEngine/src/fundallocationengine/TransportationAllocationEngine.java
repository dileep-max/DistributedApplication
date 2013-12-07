/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fundallocationengine;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.QueryBuilder;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Abhinaya
 */
public class TransportationAllocationEngine extends FundAllocationEngine {

    protected DBCollection transportationCollection;
    protected DBCollection transportationRatingCollection;
    protected Map<Integer,Boolean> smallStates = new HashMap<>();
    protected Map<Integer,Boolean> largeStates = new HashMap<>();

    public TransportationAllocationEngine(int requestId, int stateId, double requestedAmount) {
        super(requestId, stateId, requestedAmount);
        try {
            mongoClient = new MongoClient("localhost", 27017);
            dB = mongoClient.getDB("mydb");
            System.out.println(dB.toString());
            transportationCollection = dB.getCollection("transportation");
            transportationRatingCollection = dB.getCollection("transportation_rating");
            populateSmallStates();
            populateLargeStates();
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        }
    }

    public void ifGreaterThanNormalDecisionScore() {

        DBObject dbObj = new BasicDBObject();
        dbObj.put("state_id", stateId);
        DBObject dbObject = QueryBuilder.start("state_id").in(dbObj).get();

        DBCursor dbCursor = transportationRatingCollection.find(dbObject);
        if (Float.parseFloat(dbCursor.next().get("decision_score").toString()) > 950) {
            setAllocatedAmount(0.8 * (getAllocatedAmount()));
        }
    }

    public void ifHighVehicleCount() {
        
        int pastYear = Calendar.getInstance().get(Calendar.YEAR)-1;
        DBObject dbObj = BasicDBObjectBuilder.start("state_id", stateId).add("year", pastYear).get();

        DBCursor dbCursor = transportationCollection.find(dbObj);
        if (Float.parseFloat(dbCursor.next().get("tot_registd_vehicles").toString()) > 200 ) {
            setAllocatedAmount(0.75 * (getAllocatedAmount()));
        }

    }
    

    public void compareLastFunding() {
        boolean percentageFlag = false;

        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery("SELECT ALLOCATED_FUND FROM FUND.REQUEST_HISTORY WHERE STATE_ID = " + getStateId() + " AND DEPARTMENT_ID = " + getDepartmentId() + " AND STATUS_ID = 1");
            while (resultSet.next()) {
                double amount = resultSet.getDouble(1);
                if (amount > 0.65 * (getAllocatedAmount())) {
                    percentageFlag = true;
                }
            }

            if (percentageFlag == true) {
                setAllocatedAmount(0.65 * (getAllocatedAmount()));
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void checkGreaterThanAvailable() {
        double amount = 0;
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery("SELECT FUND_AMOUNT FROM FUND.FEDFUND WHERE FUND_DESIGNATION = 'Federal Fund'");
            while (resultSet.next()) {
                amount = resultSet.getDouble(1);
            }
            if (getAllocatedAmount() > 0.5 * amount) {
                setAllocatedAmount(0.15 * (getAllocatedAmount()));
            } else if (getAllocatedAmount() < 0.5 * amount && getAllocatedAmount() > 0.3 * amount) {
                setAllocatedAmount(0.18 * (getAllocatedAmount()));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void checkPublicTransport() {
       int pastYear = Calendar.getInstance().get(Calendar.YEAR)-1;
       DBObject dbObj = BasicDBObjectBuilder.start("state_id", stateId).add("year", pastYear).get();

        DBCursor dbCursor = transportationCollection.find(dbObj);
        float pub_trans =  Float.parseFloat(dbCursor.next().get("no_of_public_transportation").toString());
        if (pub_trans > 200 && (smallStates.containsKey(stateId))) {
            setAllocatedAmount(0.8 * (getAllocatedAmount()));
        }
        
        else if ( pub_trans  > 300 && (largeStates.containsKey(stateId))) {
            setAllocatedAmount(0.75 * (getAllocatedAmount()));
        }
        else {
            setAllocatedAmount(0.78 * (getAllocatedAmount()));
        }
    }
    
    public void checkHighWays() {
        
       int pastYear = Calendar.getInstance().get(Calendar.YEAR)-1;
       DBObject dbObj = BasicDBObjectBuilder.start("state_id", stateId).add("year", pastYear).get();
       
        DBCursor dbCursor = transportationCollection.find(dbObj);
        float no_high = Float.parseFloat(dbCursor.next().get("no_of_highways").toString());
        if (no_high > 10000 && (smallStates.containsKey(stateId))) {
            setAllocatedAmount(0.8 * (getAllocatedAmount()));
        }
        
        else if (no_high > 20000 && (largeStates.containsKey(stateId))) {
            setAllocatedAmount(0.75 * (getAllocatedAmount()));
        }
       
        else {
            setAllocatedAmount(0.78 * (getAllocatedAmount()));
        }    
    }
    
    public void checkMonthlySales() {
        
       int pastYear = Calendar.getInstance().get(Calendar.YEAR)-1;
       DBObject dbObj = BasicDBObjectBuilder.start("state_id", stateId).add("year", pastYear).get();
       
        DBCursor dbCursor = transportationCollection.find(dbObj);
        float mon_veh = Float.parseFloat(dbCursor.next().get("monthly_vehicle_sales").toString());
        if (mon_veh  > 10000 && (smallStates.containsKey(stateId))) {
            setAllocatedAmount(0.8 * (getAllocatedAmount()));
        }
        
        else if (mon_veh  > 15000 && (largeStates.containsKey(stateId))) {
            setAllocatedAmount(0.75 * (getAllocatedAmount()));
        }
       
        else {
            setAllocatedAmount(0.78 * (getAllocatedAmount()));
        }
        
    }
    
    public final void populateSmallStates() {
        
        smallStates.put(1,true);
        smallStates.put(2,true);
        smallStates.put(7,true);
        smallStates.put(8,true);
        smallStates.put(12,true);
        smallStates.put(19,true);
        smallStates.put(20,true);
        smallStates.put(21,true);
        smallStates.put(26,true);
        smallStates.put(28,true);
        smallStates.put(29,true);
        smallStates.put(31,true);
        smallStates.put(45,true);
        smallStates.put(50,true);
      
    }
   
    
    public final void populateLargeStates() {
        
        smallStates.put(3,true);
        smallStates.put(4,true);
        smallStates.put(5,true);
        smallStates.put(6,true);
        smallStates.put(9,true);
        smallStates.put(10,true);
        smallStates.put(11,true);
        smallStates.put(13,true);
        smallStates.put(14,true);
        smallStates.put(15,true);
        smallStates.put(16,true);
        smallStates.put(17,true);
        smallStates.put(18,true);
        smallStates.put(22,true);
        smallStates.put(23,true);
        smallStates.put(24,true);
        smallStates.put(25,true);
        smallStates.put(27,true);
        smallStates.put(30,true);
        smallStates.put(32,true);
        smallStates.put(33,true);
        smallStates.put(34,true);
        smallStates.put(41,true);
        smallStates.put(48,true);
        smallStates.put(49,true);
      
    }
    
    
    @Override
    public double mainProcedure() {
        ifGreaterThanNormalDecisionScore();
        ifHighVehicleCount();
        checkPublicTransport();
        checkMonthlySales();
        checkHighWays();
        compareLastFunding();
        checkGreaterThanAvailable();
        updateSQL();
        return getAllocatedAmount();
    }
    
    public static void main(String[] args) {

        TransportationAllocationEngine eAE = new TransportationAllocationEngine(6, 13, 10000);
        eAE.mainProcedure();
        System.out.println(eAE.getAllocatedAmount());

    }
    
}
