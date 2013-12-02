/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fundallocationengine;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.mongodb.ServerAddress;
import java.util.*;
import org.bson.BasicBSONObject;


/**
 *
 * @author asifmansoor
 */
public class EmploymentDecisionScoreMongo {
    
 private String connParam = "localhost";
    private String dbName = "mydb";
    private String coll_name = "employment";
    private String coll_name_rating = "employment_rating";
    
    
     public void readDataBase() {
           try{
               
                float sum_unemployment_rate =0;
                float sum_nonfarm_payrolls =0;
                float sum_labor_force =0;
                float sum_population=0;
                float sum_pastfund=0;
                

                float mean_unemployment_rate;
                float mean_nonfarm_payrolls;
                float mean_labor_force;
                float mean_population;
                float mean_pastfund;
                
                
                
		MongoClient mongo = new MongoClient( connParam , 27017 );
		DB db = mongo.getDB( dbName );
		Set<String> colls = db.getCollectionNames();
		        
                DBCollection collection = db.getCollection(coll_name);
                DBCursor edu_cursor = collection.find();

                int counter =0;
                ArrayList<Float> arr_unemployment_rate = new ArrayList<Float>();
                ArrayList<Float> arr_nonfarm_payrolls = new ArrayList<Float>();
                ArrayList<Float> arr_labor_force = new ArrayList<Float>();
                ArrayList<Float> arr_population = new ArrayList<Float>();
                ArrayList<Float> arr_pastfund = new ArrayList<Float>();
                
                while (edu_cursor.hasNext()) {

                      Object row_value= edu_cursor.next();
                      Object vc_value = ((BasicBSONObject) row_value).get("unemployment_rate");
                      Object texe_value = ((BasicBSONObject) row_value).get("nonfarm_payrolls");
                      Object prs_value = ((BasicBSONObject) row_value).get("labor_force");
                      Object gun_value = ((BasicBSONObject) row_value).get("population");
                      Object past_value = ((BasicBSONObject) row_value).get("past_funding");


                      arr_unemployment_rate.add(Float.parseFloat(vc_value.toString()));
                      arr_nonfarm_payrolls.add(Float.parseFloat(texe_value.toString()));
                      arr_labor_force.add(Float.parseFloat(prs_value.toString()));
                      arr_population.add(Float.parseFloat(gun_value.toString()));
                      arr_pastfund.add(Float.parseFloat(past_value.toString()));

                      sum_unemployment_rate += Float.parseFloat(vc_value.toString());
                      sum_nonfarm_payrolls += Float.parseFloat(texe_value.toString());
                      sum_labor_force+= Float.parseFloat(prs_value.toString());
                      sum_population += Float.parseFloat(gun_value.toString());
                      sum_pastfund += Float.parseFloat(past_value.toString());

                      counter++;
            }

		          
           // Mean of the unemployment rate
              mean_unemployment_rate = sum_unemployment_rate /counter;

           // Mean of the nonfarm_payrolls 
              mean_nonfarm_payrolls = sum_nonfarm_payrolls/counter;

           // Mean of the labor force 
              mean_labor_force= sum_labor_force/counter;
              
           // Mean of the population
              mean_population= sum_population/counter;
              

           // Mean of the past fundings
              mean_pastfund = sum_pastfund/counter;

              writeDataBase(mean_unemployment_rate,mean_nonfarm_payrolls ,mean_labor_force,mean_population,mean_pastfund,arr_unemployment_rate,arr_nonfarm_payrolls,arr_labor_force,arr_population,arr_pastfund,db );

        }
catch (Exception ex){
               System.out.println(ex.toString());
        }

}
     public void writeDataBase(float mean_unemployment_rate,float mean_nonfarm_payrolls , float mean_labor_force,float mean_population,float mean_pastfund,ArrayList<Float> arr_unemployment_rate,ArrayList<Float> arr_nonfarm_payrolls,ArrayList<Float> arr_labor_force,ArrayList<Float> arr_population, ArrayList<Float> arr_pastfund,DB db ) {

	try {
	
              //Setting the value of desired parameter
              float param_unemployment_rate = 230;
              float param_nonfarm_payrolls = 350;
              float param_labor_force = 210;
              float param_population = 210;
              float param_pastfund = 10;
		       
           // array list to hold the decision score
              ArrayList<Float> arr_desc_score = new ArrayList<Float>();
              ArrayList<Integer> arr_past_rating = new ArrayList<Integer>();

              // calculating the product of mean-avg-difference and param
              int state_id =0;
              float avg_unemployment_rate;
              float avg_nonfarm_payrolls;
              float avg_labor_force;
              float avg_population;
              float avg_pastfund;

              float decision_score;
              int rating_pastfund;
              while(state_id!=50){
                    // taking average across all the years
                  avg_unemployment_rate = (Float.parseFloat(arr_unemployment_rate.get(state_id).toString())+ Float.parseFloat(arr_unemployment_rate.get(state_id+50).toString())+ Float.parseFloat(arr_unemployment_rate.get(state_id+100).toString()) + Float.parseFloat(arr_unemployment_rate.get(state_id+150).toString()) ) / 4;
                  avg_nonfarm_payrolls = (Float.parseFloat(arr_nonfarm_payrolls.get(state_id).toString())+ Float.parseFloat(arr_nonfarm_payrolls.get(state_id+50).toString())+ Float.parseFloat(arr_nonfarm_payrolls.get(state_id+100).toString()) + Float.parseFloat(arr_nonfarm_payrolls.get(state_id+150).toString()) ) / 4;                  
                  avg_labor_force = (Float.parseFloat(arr_labor_force.get(state_id).toString())+ Float.parseFloat(arr_labor_force.get(state_id+50).toString())+ Float.parseFloat(arr_labor_force.get(state_id+100).toString()) + Float.parseFloat(arr_labor_force.get(state_id+150).toString()) ) / 4;                  
                  avg_population = (Float.parseFloat(arr_population.get(state_id).toString())+ Float.parseFloat(arr_population.get(state_id+50).toString())+ Float.parseFloat(arr_population.get(state_id+100).toString()) + Float.parseFloat(arr_population.get(state_id+150).toString()) ) / 4;                  
                  avg_pastfund = (Float.parseFloat(arr_pastfund.get(state_id).toString())+ Float.parseFloat(arr_pastfund.get(state_id+50).toString())+ Float.parseFloat(arr_pastfund.get(state_id+100).toString()) + Float.parseFloat(arr_pastfund.get(state_id+150).toString()) ) / 4;

               // calculating the decision score based on the params
                  decision_score = ((avg_unemployment_rate / mean_unemployment_rate) * param_unemployment_rate) + ((avg_nonfarm_payrolls/ mean_nonfarm_payrolls) * param_nonfarm_payrolls) + ((avg_labor_force/ mean_labor_force) * param_labor_force) + ((avg_population/ mean_population) * param_population);
                  arr_desc_score.add(state_id,decision_score );

               // calculating the past funding rate based on the params
                  rating_pastfund = (int) Math.floor(((avg_pastfund / mean_pastfund ) * param_pastfund)) ;
                  arr_past_rating.add(state_id,rating_pastfund);

                  state_id++;
             }
              // Update the employment_rating table with decision_score
              DBCollection collection_employment_rating = db.getCollection(coll_name_rating);
              for(int i=0;i<50;i++){

                  BasicDBObject updateQuery1 = new BasicDBObject();
                  BasicDBObject updateQuery2 = new BasicDBObject();
                  
                  updateQuery1.append("$set", new BasicDBObject().append("decision_score",arr_desc_score.get(i)));
                  updateQuery2.append("$set", new BasicDBObject().append("prev_year_fund_rating",arr_past_rating.get(i)));

                  BasicDBObject searchQuery = new BasicDBObject().append("state_id", i+1);

                  collection_employment_rating.update(searchQuery, updateQuery1);
                  collection_employment_rating.update(searchQuery, updateQuery2);

                          }
            }
		catch (Exception ex){
			 System.out.println(ex.toString());
		 }

}
     
      public static void main(String[] args) {

        EmploymentDecisionScoreMongo emDSC = new EmploymentDecisionScoreMongo();
        emDSC.readDataBase();
        System.out.println("Employment Descision Rating table Populated - successfully");

    } 
    
    
}

