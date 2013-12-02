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
public class EmploymentAllocationEngine extends FundAllocationEngine {
    protected DBCollection employmentCollection;
    protected DBCollection employmentRatingCollection;
    
    // initializing the base class constructor with request id, state id and requested amount
    public EmploymentAllocationEngine(int requestId, int stateId, double requestedAmount) {
        super(requestId, stateId, requestedAmount);
        // Connecting to the mongo db..
        try {
            mongoClient = new MongoClient("localhost", 27017);
            dB = mongoClient.getDB("mydb");
            System.out.println(dB.toString());
            employmentCollection = dB.getCollection("employment");
            employmentRatingCollection = dB.getCollection("employment_rating");
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        }
    }
    
    // Check the decision score of the employment department
        public void ifGreaterThanNormalDecisionScore() {

        DBObject dbObj = new BasicDBObject();
        dbObj.put("state_id", stateId);
        DBObject dbObject = QueryBuilder.start("state_id").in(dbObj).get();

        DBCursor dbCursor = employmentRatingCollection.find(dbObject);
        if (Float.parseFloat(dbCursor.next().get("decision_score").toString()) > 750) {
            setAllocatedAmount(0.8 * (allocatedAmount));
        }
    }
        
   // Check the previous year fund rating score of the employment department
    public void ifGreaterThanNormalPrevYearFundRating() {

        DBObject dbObj = new BasicDBObject();
        dbObj.put("state_id", stateId);
        DBObject dbObject = QueryBuilder.start("state_id").in(dbObj).get();

        DBCursor dbCursor = employmentRatingCollection.find(dbObject);
        if (Float.parseFloat(dbCursor.next().get("prev_year_fund_rating").toString()) > 7) {
            setAllocatedAmount(0.7 * (allocatedAmount));
        }
    }
    
    
    // Check if the state id falls under top ten unemployment rating
    public void ifTopTenUnemployment() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        List<Float> arrayList = new ArrayList<>();
        float stateUnemploymentPercentage = 0;


        DBObject dbObj = new BasicDBObject();
        dbObj.put("year", currentYear);
        DBObject dbObject = QueryBuilder.start("year").in(dbObj).get();

        DBCursor dbCursor = employmentCollection.find(dbObject);
        
        while (dbCursor.hasNext()) {
            float currentUnemploymentRate = (Float.parseFloat(dbCursor.next().get("unemployment_rate").toString()));
            arrayList.add(currentUnemploymentRate);
            
           
            float topStateId = Float.parseFloat(dbCursor.curr().get("state_id").toString());
            
            if (topStateId == (float)getStateId()) {
                stateUnemploymentPercentage = currentUnemploymentRate;
            }
        }
        Collections.sort(arrayList);
        for (int i = arrayList.size() - 1; i > (arrayList.size() - 10); i--) {
            if (arrayList.get(i) == stateUnemploymentPercentage) {
                setAllocatedAmount(getAllocatedAmount() - 0.02 * getRequestedAmount());
            }
        }

    }
    
    // Check if the state id falls under top ten non farm payrolls rating
    public void ifTopTenNonFarm() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        List<Float> arrayList = new ArrayList<>();
        float stateNonFarmPercentage = 0;


        DBObject dbObj = new BasicDBObject();
        dbObj.put("year", currentYear);
        DBObject dbObject = QueryBuilder.start("year").in(dbObj).get();

        DBCursor dbCursor = employmentCollection.find(dbObject);
        
        while (dbCursor.hasNext()) {

            float currentNonFarmRate = (Float.parseFloat(dbCursor.next().get("nonfarm_payrolls").toString()));
            arrayList.add(currentNonFarmRate);
            
            float topStateId = Float.parseFloat(dbCursor.curr().get("state_id").toString());
            if (topStateId == (float)getStateId()) {
                stateNonFarmPercentage = currentNonFarmRate;
            }
        }
        Collections.sort(arrayList);
        for (int i = arrayList.size() - 1; i > (arrayList.size() - 10); i--) {
            if (arrayList.get(i) == stateNonFarmPercentage) {
                setAllocatedAmount(getAllocatedAmount() - 0.02 * getRequestedAmount());
            }
        }

    }
    
    // Check if the state id falls under top ten prisoners rating
     public void ifTopTenLaborForce() {
        
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        List<Float> arrayList = new ArrayList<>();
        float stateLaborForce = 0;

       
        DBObject dbObj = new BasicDBObject();
        dbObj.put("year", currentYear);
       
        DBObject dbObject = QueryBuilder.start("year").in(dbObj).get();

        DBCursor dbCursor = employmentCollection.find(dbObject);
        while (dbCursor.hasNext()) {

            float currentLaborForceRate = (Float.parseFloat(dbCursor.next().get("labor_force").toString()));
            arrayList.add(currentLaborForceRate);
            float topStateId = Float.parseFloat(dbCursor.curr().get("state_id").toString());
            if (topStateId == (float)getStateId()) {
                stateLaborForce = currentLaborForceRate;
            }
        }
        Collections.sort(arrayList);
        for (int i = arrayList.size() - 1; i > (arrayList.size() - 5); i--) {
            if (arrayList.get(i) == stateLaborForce) {
                setAllocatedAmount(getAllocatedAmount() - 0.02 * getRequestedAmount());
            }
        }

    }
     
    // Check if the state's unemployment rating was high compared to the previous year
    public void compareYearRatingUnemployment() {

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int pastYear = currentYear - 1;

        DBObject dbObject1 = BasicDBObjectBuilder.start("state_id", stateId).add("year", currentYear).get();
        DBObject dbObject2 = BasicDBObjectBuilder.start("state_id", stateId).add("year", pastYear).get();


        DBCursor dbCursor1 = employmentCollection.find(dbObject1);
        DBCursor dbCursor2 = employmentCollection.find(dbObject2);

        if ((Float.parseFloat(dbCursor1.next().get("unemployment_rate").toString())) - (Float.parseFloat(dbCursor2.next().get("unemployment_rate").toString())) > 0.3) {
            setAllocatedAmount(getAllocatedAmount() - 0.02 * (requestedAmount));
        }
 
    }
    
    // Check if the state's non farm payroll rating was high compared to the previous year
    public void compareYearRatingNonFarmPayroll() {

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int pastYear = currentYear - 1;

        DBObject dbObject1 = BasicDBObjectBuilder.start("state_id", stateId).add("year", currentYear).get();
        DBObject dbObject2 = BasicDBObjectBuilder.start("state_id", stateId).add("year", pastYear).get();

        DBCursor dbCursor1 = employmentCollection.find(dbObject1);
        DBCursor dbCursor2 = employmentCollection.find(dbObject2);

        if ((Float.parseFloat(dbCursor1.next().get("nonfarm_payrolls").toString())) - (Float.parseFloat(dbCursor2.next().get("nonfarm_payrolls").toString())) > 0.2) {
            setAllocatedAmount(getAllocatedAmount() - 0.05 * (requestedAmount));
        }
    }
    
    // Check if the state's labor force rating was high compared to the previous year
    public void compareYearRatingLaborForce() {

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int pastYear = currentYear - 1;

        DBObject dbObject1 = BasicDBObjectBuilder.start("state_id", stateId).add("year", currentYear).get();
        DBObject dbObject2 = BasicDBObjectBuilder.start("state_id", stateId).add("year", pastYear).get();

        DBCursor dbCursor1 = employmentCollection.find(dbObject1);
        DBCursor dbCursor2 = employmentCollection.find(dbObject2);

        if ((Float.parseFloat(dbCursor1.next().get("labor_force").toString())) - (Float.parseFloat(dbCursor2.next().get("labor_force").toString())) > 0.2) {
            setAllocatedAmount(getAllocatedAmount() - 0.07 * (requestedAmount));
        }
    }
    
    
     // Check if the state's Population rating was high compared to the previous year
    public void compareYearRatingPopulation() {

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int pastYear = currentYear - 1;

        DBObject dbObject1 = BasicDBObjectBuilder.start("state_id", stateId).add("year", currentYear).get();
        DBObject dbObject2 = BasicDBObjectBuilder.start("state_id", stateId).add("year", pastYear).get();

        DBCursor dbCursor1 = employmentCollection.find(dbObject1);
        DBCursor dbCursor2 = employmentCollection.find(dbObject2);

        if ((Float.parseFloat(dbCursor1.next().get("population").toString())) - (Float.parseFloat(dbCursor2.next().get("population").toString())) > 0.2) {
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
        ifTopTenUnemployment();
        ifTopTenNonFarm();
        ifTopTenLaborForce();
        compareLastFunding();
        compareYearRatingUnemployment();
        compareYearRatingNonFarmPayroll();
        compareYearRatingLaborForce();
        compareYearRatingPopulation();
        checkGreaterThanAvailable();
        updateSQL();
        return getAllocatedAmount();
    }
        
    public static void main(String[] args) {

        EmploymentAllocationEngine cAE = new EmploymentAllocationEngine(32, 13, 20000);
        cAE.mainProcedure();
        System.out.println(cAE.getAllocatedAmount());

    }
    
}
