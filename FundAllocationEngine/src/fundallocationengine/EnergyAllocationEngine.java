/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fundallocationengine;

/**
 *
 * @author asifmansoor
 */

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

public class EnergyAllocationEngine extends FundAllocationEngine {
    
    protected DBCollection energyCollection;
    protected DBCollection energyRatingCollection;
    
     // initializing the base class constructor with request id, state id and requested amount
   
    public EnergyAllocationEngine(int requestId, int stateId, double requestedAmount) {
        super(requestId, stateId, requestedAmount);
        // Connecting to the mongo db..
        try {
            mongoClient = new MongoClient("localhost", 27017);
            dB = mongoClient.getDB("mydb");
            System.out.println(dB.toString());
            energyCollection = dB.getCollection("energy");
            energyRatingCollection = dB.getCollection("energy_rating");
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        }
    }
    // Check the decision score of the energy department
    public void ifGreaterThanNormalDecisionScore() {

        DBObject dbObj = new BasicDBObject();
        dbObj.put("state_id", stateId);
        DBObject dbObject = QueryBuilder.start("state_id").in(dbObj).get();

        DBCursor dbCursor = energyRatingCollection.find(dbObject);
        if (Float.parseFloat(dbCursor.next().get("decision_score").toString()) > 750) {
            setAllocatedAmount(0.8 * (allocatedAmount));
        }
    }
    
    // Check the previous year fund rating score of the energy department
    public void ifGreaterThanNormalPrevYearFundRating() {

        DBObject dbObj = new BasicDBObject();
        dbObj.put("state_id", stateId);
        DBObject dbObject = QueryBuilder.start("state_id").in(dbObj).get();

        DBCursor dbCursor = energyRatingCollection.find(dbObject);
        if (Float.parseFloat(dbCursor.next().get("prev_year_fund_rating").toString()) > 7) {
            setAllocatedAmount(0.7 * (allocatedAmount));
        }
    }
    
    
    // Check if the state id falls under top ten renewable energy usage
    public void ifTopTenRenewableEnergy() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        List<Float> arrayList = new ArrayList<>();
        float stateRenewableEnergyPercentage = 0;


        DBObject dbObj = new BasicDBObject();
        dbObj.put("year", currentYear);
        DBObject dbObject = QueryBuilder.start("year").in(dbObj).get();

        DBCursor dbCursor = energyCollection.find(dbObject);
        
        while (dbCursor.hasNext()) {
            float currentrenewableEnergyRate = (Float.parseFloat(dbCursor.next().get("renewable_energy").toString()));
            arrayList.add(currentrenewableEnergyRate);
            
           
            float topStateId = Float.parseFloat(dbCursor.curr().get("state_id").toString());
            
            if (topStateId == (float)getStateId()) {
                stateRenewableEnergyPercentage = currentrenewableEnergyRate;
            }
        }
        Collections.sort(arrayList);
        for (int i = arrayList.size() - 1; i > (arrayList.size() - 10); i--) {
            if (arrayList.get(i) == stateRenewableEnergyPercentage) {
                setAllocatedAmount(getAllocatedAmount() - 0.02 * getRequestedAmount());
            }
        }

    }
    
    // Check if the state falls under top ten petroleum_usage rating in the US
    public void ifTopTenPetroleumUsage() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        List<Float> arrayList = new ArrayList<>();
        float statePetroleumUsagePercentage = 0;


        DBObject dbObj = new BasicDBObject();
        dbObj.put("year", currentYear);
        DBObject dbObject = QueryBuilder.start("year").in(dbObj).get();

        DBCursor dbCursor = energyCollection.find(dbObject);
        
        while (dbCursor.hasNext()) {

            float currentPetroleumUsageRate = (Float.parseFloat(dbCursor.next().get("petroleum_usage").toString()));
            arrayList.add(currentPetroleumUsageRate);
            
            float topStateId = Float.parseFloat(dbCursor.curr().get("state_id").toString());
            if (topStateId == (float)getStateId()) {
                statePetroleumUsagePercentage = currentPetroleumUsageRate;
            }
        }
        Collections.sort(arrayList);
        for (int i = arrayList.size() - 1; i > (arrayList.size() - 10); i--) {
            if (arrayList.get(i) == statePetroleumUsagePercentage) {
                setAllocatedAmount(getAllocatedAmount() - 0.02 * getRequestedAmount());
            }
        }

    }
    
     // Check if the state id falls under top ten crude oil production rating
     public void ifTopTenCrudeOil() {
        
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        List<Float> arrayList = new ArrayList<>();
        float stateCrudeOil = 0;

       
        DBObject dbObj = new BasicDBObject();
        dbObj.put("year", currentYear);
       
        DBObject dbObject = QueryBuilder.start("year").in(dbObj).get();

        DBCursor dbCursor = energyCollection.find(dbObject);
        while (dbCursor.hasNext()) {

            float currentCrudeOilRate = (Float.parseFloat(dbCursor.next().get("crudeoil_prod").toString()));
            arrayList.add(currentCrudeOilRate);
            float topStateId = Float.parseFloat(dbCursor.curr().get("state_id").toString());
            if (topStateId == (float)getStateId()) {
                stateCrudeOil = currentCrudeOilRate;
            }
        }
        Collections.sort(arrayList);
        for (int i = arrayList.size() - 1; i > (arrayList.size() - 5); i--) {
            if (arrayList.get(i) == stateCrudeOil) {
                setAllocatedAmount(getAllocatedAmount() - 0.02 * getRequestedAmount());
            }
        }

    }
     
   // Check if the state's renewable energy rating was high compared to the previous year
    public void compareYearRatingRenewableEnergy() {

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int pastYear = currentYear - 1;

        DBObject dbObject1 = BasicDBObjectBuilder.start("state_id", stateId).add("year", currentYear).get();
        DBObject dbObject2 = BasicDBObjectBuilder.start("state_id", stateId).add("year", pastYear).get();


        DBCursor dbCursor1 = energyCollection.find(dbObject1);
        DBCursor dbCursor2 = energyCollection.find(dbObject2);

        if ((Float.parseFloat(dbCursor1.next().get("renewable_energy").toString())) - (Float.parseFloat(dbCursor2.next().get("renewable_energy").toString())) > 0.3) {
            setAllocatedAmount(getAllocatedAmount() - 0.02 * (requestedAmount));
        }
 
    }
   
     // Check if the state's total petroleum usage rating was high compared to the previous year
    public void compareYearRatingtotalPetroleumUsage() {

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int pastYear = currentYear - 1;

        DBObject dbObject1 = BasicDBObjectBuilder.start("state_id", stateId).add("year", currentYear).get();
        DBObject dbObject2 = BasicDBObjectBuilder.start("state_id", stateId).add("year", pastYear).get();

        DBCursor dbCursor1 = energyCollection.find(dbObject1);
        DBCursor dbCursor2 = energyCollection.find(dbObject2);

        if ((Float.parseFloat(dbCursor1.next().get("petroleum_usage").toString())) - (Float.parseFloat(dbCursor2.next().get("petroleum_usage").toString())) > 0.2) {
            setAllocatedAmount(getAllocatedAmount() - 0.05 * (requestedAmount));
        }
    }
    
    
    // Check if the state's crudeoil usage rating was high compared to the previous year
    public void compareYearRatingCrudeOil() {

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int pastYear = currentYear - 1;

        DBObject dbObject1 = BasicDBObjectBuilder.start("state_id", stateId).add("year", currentYear).get();
        DBObject dbObject2 = BasicDBObjectBuilder.start("state_id", stateId).add("year", pastYear).get();

        DBCursor dbCursor1 = energyCollection.find(dbObject1);
        DBCursor dbCursor2 = energyCollection.find(dbObject2);

        if ((Float.parseFloat(dbCursor1.next().get("crudeoil_prod").toString())) - (Float.parseFloat(dbCursor2.next().get("crudeoil_prod").toString())) > 0.2) {
            setAllocatedAmount(getAllocatedAmount() - 0.07 * (requestedAmount));
        }
    }
    
    // Check if the state's natural gas production rating was high compared to the previous year
    public void compareYearRatingNaturalGas() {

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int pastYear = currentYear - 1;

        DBObject dbObject1 = BasicDBObjectBuilder.start("state_id", stateId).add("year", currentYear).get();
        DBObject dbObject2 = BasicDBObjectBuilder.start("state_id", stateId).add("year", pastYear).get();

        DBCursor dbCursor1 = energyCollection.find(dbObject1);
        DBCursor dbCursor2 = energyCollection.find(dbObject2);

        if ((Float.parseFloat(dbCursor1.next().get("nat_gas_prod").toString())) - (Float.parseFloat(dbCursor2.next().get("nat_gas_prod").toString())) > 0.2) {
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
        ifTopTenRenewableEnergy();
        ifTopTenPetroleumUsage();
        ifTopTenCrudeOil();
        compareYearRatingRenewableEnergy();
        compareYearRatingtotalPetroleumUsage();
        compareYearRatingCrudeOil();
        compareYearRatingNaturalGas();
        compareLastFunding();
        checkGreaterThanAvailable();
        updateSQL();
        return getAllocatedAmount();
    }
    
    
    
    
    
    
   
}
