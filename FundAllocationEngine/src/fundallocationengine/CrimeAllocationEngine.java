/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author asifmansoor
 */
public class CrimeAllocationEngine extends FundAllocationEngine {
    
    protected DBCollection crimeCollection;
    protected DBCollection crimeRatingCollection;
    
    // initializing the base class constructor with request id, state id and requested amount
    public CrimeAllocationEngine(int requestId, int stateId, double requestedAmount) {
        super(requestId, stateId, requestedAmount);
        // Connecting to the mongo db..
        try {
            mongoClient = new MongoClient("localhost", 27017);
            dB = mongoClient.getDB("mydb");
            System.out.println(dB.toString());
            crimeCollection = dB.getCollection("crime");
            crimeRatingCollection = dB.getCollection("crime_rating");
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        }
    }
        
        // Check the decision score of the crime department
        public void ifGreaterThanNormalDecisionScore() {

        DBObject dbObj = new BasicDBObject();
        dbObj.put("state_id", stateId);
        DBObject dbObject = QueryBuilder.start("state_id").in(dbObj).get();

        DBCursor dbCursor = crimeRatingCollection.find(dbObject);
        if (Float.parseFloat(dbCursor.next().get("decision_score").toString()) > 750) {
            setAllocatedAmount(0.8 * (allocatedAmount));
        }
    }
     // Check the previous year fund rating score of the crime department
    public void ifGreaterThanNormalPrevYearFundRating() {

        DBObject dbObj = new BasicDBObject();
        dbObj.put("state_id", stateId);
        DBObject dbObject = QueryBuilder.start("state_id").in(dbObj).get();

        DBCursor dbCursor = crimeRatingCollection.find(dbObject);
        if (Float.parseFloat(dbCursor.next().get("prev_year_fund_rating").toString()) > 7) {
            setAllocatedAmount(0.7 * (allocatedAmount));
        }
    }
        // Check if the state id falls under top ten violent crime rating
    public void ifTopTenViolentCrime() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        List<Float> arrayList = new ArrayList<>();
        float stateViolentCrimePercentage = 0;


        DBObject dbObj = new BasicDBObject();
        dbObj.put("year", currentYear);
        DBObject dbObject = QueryBuilder.start("year").in(dbObj).get();

        DBCursor dbCursor = crimeCollection.find(dbObject);
        
        while (dbCursor.hasNext()) {
            float currentViolentCrimeRate = (Float.parseFloat(dbCursor.next().get("violent_crime_rate").toString()));
            arrayList.add(currentViolentCrimeRate);
            
           
            float topStateId = Float.parseFloat(dbCursor.curr().get("state_id").toString());
            
            if (topStateId == (float)getStateId()) {
                stateViolentCrimePercentage = currentViolentCrimeRate;
            }
        }
        Collections.sort(arrayList);
        for (int i = arrayList.size() - 1; i > (arrayList.size() - 10); i--) {
            if (arrayList.get(i) == stateViolentCrimePercentage) {
                setAllocatedAmount(getAllocatedAmount() - 0.02 * getRequestedAmount());
            }
        }

    }
        
    // Check if the state id falls under top ten total execution rating
    public void ifTopTenTotalExecution() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        List<Float> arrayList = new ArrayList<>();
        float stateTotalExecutionPercentage = 0;


        DBObject dbObj = new BasicDBObject();
        dbObj.put("year", currentYear);
        DBObject dbObject = QueryBuilder.start("year").in(dbObj).get();

        DBCursor dbCursor = crimeCollection.find(dbObject);
        
        while (dbCursor.hasNext()) {

            float currentTotalExecutionRate = (Float.parseFloat(dbCursor.next().get("total_executions").toString()));
            arrayList.add(currentTotalExecutionRate);
            
            float topStateId = Float.parseFloat(dbCursor.curr().get("state_id").toString());
            if (topStateId == (float)getStateId()) {
                stateTotalExecutionPercentage = currentTotalExecutionRate;
            }
        }
        Collections.sort(arrayList);
        for (int i = arrayList.size() - 1; i > (arrayList.size() - 10); i--) {
            if (arrayList.get(i) == stateTotalExecutionPercentage) {
                setAllocatedAmount(getAllocatedAmount() - 0.02 * getRequestedAmount());
            }
        }

    }
    
    // Check if the state id falls under top ten prisoners rating
     public void ifTopTenPrisoners() {
        
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        List<Float> arrayList = new ArrayList<>();
        float statePrisoners = 0;

       
        DBObject dbObj = new BasicDBObject();
        dbObj.put("year", currentYear);
       
        DBObject dbObject = QueryBuilder.start("year").in(dbObj).get();

        DBCursor dbCursor = crimeCollection.find(dbObject);
        while (dbCursor.hasNext()) {

            float currentPrisonersRate = (Float.parseFloat(dbCursor.next().get("prisoners_per_100,000_population").toString()));
            arrayList.add(currentPrisonersRate);
            float topStateId = Float.parseFloat(dbCursor.curr().get("state_id").toString());
            if (topStateId == (float)getStateId()) {
                statePrisoners = currentPrisonersRate;
            }
        }
        Collections.sort(arrayList);
        for (int i = arrayList.size() - 1; i > (arrayList.size() - 5); i--) {
            if (arrayList.get(i) == statePrisoners) {
                setAllocatedAmount(getAllocatedAmount() - 0.02 * getRequestedAmount());
            }
        }

    }
     
    // Check if the state's violent crime rating was high compared to the previous year
    public void compareYearRatingViolentCrime() {

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int pastYear = currentYear - 1;

        DBObject dbObject1 = BasicDBObjectBuilder.start("state_id", stateId).add("year", currentYear).get();
        DBObject dbObject2 = BasicDBObjectBuilder.start("state_id", stateId).add("year", pastYear).get();


        DBCursor dbCursor1 = crimeCollection.find(dbObject1);
        DBCursor dbCursor2 = crimeCollection.find(dbObject2);

        if ((Float.parseFloat(dbCursor1.next().get("violent_crime_rate").toString())) - (Float.parseFloat(dbCursor2.next().get("violent_crime_rate").toString())) > 0.3) {
            setAllocatedAmount(getAllocatedAmount() - 0.02 * (requestedAmount));
        }
 
    }
    
    // Check if the state's total execution rating was high compared to the previous year
    public void compareYearRatingtotalExecutions() {

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int pastYear = currentYear - 1;

        DBObject dbObject1 = BasicDBObjectBuilder.start("state_id", stateId).add("year", currentYear).get();
        DBObject dbObject2 = BasicDBObjectBuilder.start("state_id", stateId).add("year", pastYear).get();

        DBCursor dbCursor1 = crimeCollection.find(dbObject1);
        DBCursor dbCursor2 = crimeCollection.find(dbObject2);

        if ((Float.parseFloat(dbCursor1.next().get("total_executions").toString())) - (Float.parseFloat(dbCursor2.next().get("total_executions").toString())) > 0.2) {
            setAllocatedAmount(getAllocatedAmount() - 0.05 * (requestedAmount));
        }
    }
    
    // Check if the state's prisoners rating was high compared to the previous year
    public void compareYearRatingPrisoners() {

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int pastYear = currentYear - 1;

        DBObject dbObject1 = BasicDBObjectBuilder.start("state_id", stateId).add("year", currentYear).get();
        DBObject dbObject2 = BasicDBObjectBuilder.start("state_id", stateId).add("year", pastYear).get();

        DBCursor dbCursor1 = crimeCollection.find(dbObject1);
        DBCursor dbCursor2 = crimeCollection.find(dbObject2);

        if ((Float.parseFloat(dbCursor1.next().get("prisoners_per_100,000_population").toString())) - (Float.parseFloat(dbCursor2.next().get("prisoners_per_100,000_population").toString())) > 0.2) {
            setAllocatedAmount(getAllocatedAmount() - 0.07 * (requestedAmount));
        }
    }
    
    
    // Check if the state's gun ownership rating was high compared to the previous year
    public void compareYearRatingGunOwners() {

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int pastYear = currentYear - 1;

        DBObject dbObject1 = BasicDBObjectBuilder.start("state_id", stateId).add("year", currentYear).get();
        DBObject dbObject2 = BasicDBObjectBuilder.start("state_id", stateId).add("year", pastYear).get();

        DBCursor dbCursor1 = crimeCollection.find(dbObject1);
        DBCursor dbCursor2 = crimeCollection.find(dbObject2);

        if ((Float.parseFloat(dbCursor1.next().get("percentage_of_gun_ownership").toString())) - (Float.parseFloat(dbCursor2.next().get("percentage_of_gun_ownership").toString())) > 0.2) {
            setAllocatedAmount(getAllocatedAmount() - 0.03 * (requestedAmount));
        }
    }
    
    // Check if the state's funding was high compared to the previous year
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
        // Check if the state's funding request greater than the fed budget amount for the department
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
    // main function which executes all of the above methods/ rule for a specific request 
    @Override
    public double mainProcedure() {
        ifGreaterThanNormalDecisionScore();
        ifGreaterThanNormalPrevYearFundRating();
        ifTopTenViolentCrime();
        ifTopTenTotalExecution();
        compareYearRatingViolentCrime();
        compareLastFunding();
        ifTopTenPrisoners();
        compareYearRatingtotalExecutions();
        compareYearRatingPrisoners();
        compareYearRatingGunOwners();
        checkGreaterThanAvailable();
        updateSQL();
        return getAllocatedAmount();
    }
        
    public static void main(String[] args) {

        CrimeAllocationEngine cAE = new CrimeAllocationEngine(6, 13, 6748399);
        cAE.mainProcedure();
        System.out.println(cAE.getAllocatedAmount());

    }
    
    
}
