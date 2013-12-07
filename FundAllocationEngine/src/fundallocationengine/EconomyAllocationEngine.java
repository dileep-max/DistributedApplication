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

/**
 *
 * @author Abhinaya
 */
public class EconomyAllocationEngine extends FundAllocationEngine {

    protected DBCollection economyCollection;
    protected DBCollection economyRatingCollection;

    public EconomyAllocationEngine(int requestId, int stateId, double requestedAmount) {
        super(requestId, stateId, requestedAmount);
        try {
            mongoClient = new MongoClient("localhost", 27017);
            dB = mongoClient.getDB("mydb");
            System.out.println(dB.toString());
            economyCollection = dB.getCollection("economy");
            economyRatingCollection = dB.getCollection("economy_rating");
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        }
    }

    public void ifGreaterThanNormalDecisionScore() {

        DBObject dbObj = new BasicDBObject();
        dbObj.put("state_id", stateId);
        DBObject dbObject = QueryBuilder.start("state_id").in(dbObj).get();

        DBCursor dbCursor = economyRatingCollection.find(dbObject);
        if (Float.parseFloat(dbCursor.next().get("decision_score").toString()) > 750) {
            setAllocatedAmount(0.8 * (getAllocatedAmount()));
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

    public void checkMinimumWages() {
       int pastYear = Calendar.getInstance().get(Calendar.YEAR)-1;
       DBObject dbObj = BasicDBObjectBuilder.start("state_id", stateId).add("year", pastYear).get(); 
        
       DBCursor dbCursor = economyCollection.find(dbObj);

        if (Float.parseFloat(dbCursor.next().get("minimum_wages").toString()) > 15) {
            setAllocatedAmount(0.8 * (getAllocatedAmount()));
        } else {
            setAllocatedAmount(0.88 * (getAllocatedAmount()));
        }
    }

    public void checkPerCapitaIncome() {
       int pastYear = Calendar.getInstance().get(Calendar.YEAR)-1;
       DBObject dbObj = BasicDBObjectBuilder.start("state_id", stateId).add("year", pastYear).get(); 
        
       DBCursor dbCursor = economyCollection.find(dbObj);


        if (Float.parseFloat(dbCursor.next().get("per_capita_income").toString()) > 10000) {
            setAllocatedAmount(0.75 * (getAllocatedAmount()));
        } else {
            setAllocatedAmount(0.81 * (getAllocatedAmount()));
        }


    }

    public void checkGdp() {
         int pastYear = Calendar.getInstance().get(Calendar.YEAR)-1;
         DBObject dbObj = BasicDBObjectBuilder.start("state_id", stateId).add("year", pastYear).get(); 
        
       DBCursor dbCursor = economyCollection.find(dbObj);
        if (Float.parseFloat(dbCursor.next().get("gdp").toString()) > 30000) {
            setAllocatedAmount(0.72 * (getAllocatedAmount()));
        } else {
            setAllocatedAmount(0.80 * (getAllocatedAmount()));
        }
        
    }
    
    public void checkPopulation() {
        
        int pastYear = Calendar.getInstance().get(Calendar.YEAR)-1;
         DBObject dbObj = BasicDBObjectBuilder.start("state_id", stateId).add("year", pastYear).get(); 
        
       DBCursor dbCursor = economyCollection.find(dbObj);
        if (Float.parseFloat(dbCursor.next().get("population").toString()) > 15000) {
            setAllocatedAmount(0.75 * (getAllocatedAmount()));
        } else {
            setAllocatedAmount(0.78 * (getAllocatedAmount()));
        }
        
    }
    
    @Override
   public double mainProcedure() {
        ifGreaterThanNormalDecisionScore();
        checkMinimumWages();
        checkPerCapitaIncome();
        compareLastFunding();
        checkGdp();
        checkPopulation();
        checkGreaterThanAvailable();
        updateSQL();
        return getAllocatedAmount();
    }
    public static void main(String[] args) {

        EconomyAllocationEngine eAE = new EconomyAllocationEngine(6, 13,10000);
        eAE.mainProcedure();
        System.out.println(eAE.getAllocatedAmount());

    }
    
}
