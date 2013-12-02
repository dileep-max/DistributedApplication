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

public class HealthDecisionScoreMongo {
    
     
private String connParam = "localhost";
    private String dbName = "mydb";
    private String coll_name = "health";
    private String coll_name_rating = "health_rating";
    
    
     public void readDataBase() {
           try{
               
                float sum_health_spending =0;
                float sum_health_coverage =0;
                float sum_fertility_rate =0;
                float sum_mortality_rate=0;
                float sum_pastfund=0;
                

                float mean_health_spending;
                float mean_health_coverage;
                float mean_fertility_rate;
                float mean_mortality_rate;
                float mean_pastfund;
                
                
                
		MongoClient mongo = new MongoClient( connParam , 27017 );
		DB db = mongo.getDB( dbName );
		Set<String> colls = db.getCollectionNames();
		        
                DBCollection collection = db.getCollection(coll_name);
                DBCursor edu_cursor = collection.find();

                int counter =0;
                ArrayList<Float> arr_health_spending = new ArrayList<Float>();
                ArrayList<Float> arr_health_coverage = new ArrayList<Float>();
                ArrayList<Float> arr_fertility_rate = new ArrayList<Float>();
                ArrayList<Float> arr_mortality_rate = new ArrayList<Float>();
                ArrayList<Float> arr_pastfund = new ArrayList<Float>();
                
                while (edu_cursor.hasNext()) {

                      Object row_value= edu_cursor.next();
                      Object vc_value = ((BasicBSONObject) row_value).get("health_spending");
                      Object texe_value = ((BasicBSONObject) row_value).get("health_coverage");
                      Object prs_value = ((BasicBSONObject) row_value).get("fertility_rate");
                      Object gun_value = ((BasicBSONObject) row_value).get("mortality_rate");
                      Object past_value = ((BasicBSONObject) row_value).get("past_funding");


                      arr_health_spending.add(Float.parseFloat(vc_value.toString()));
                      arr_health_coverage.add(Float.parseFloat(texe_value.toString()));
                      arr_fertility_rate.add(Float.parseFloat(prs_value.toString()));
                      arr_mortality_rate.add(Float.parseFloat(gun_value.toString()));
                      arr_pastfund.add(Float.parseFloat(past_value.toString()));

                      sum_health_spending += Float.parseFloat(vc_value.toString());
                      sum_health_coverage += Float.parseFloat(texe_value.toString());
                      sum_fertility_rate+= Float.parseFloat(prs_value.toString());
                      sum_mortality_rate += Float.parseFloat(gun_value.toString());
                      sum_pastfund += Float.parseFloat(past_value.toString());

                      counter++;
            }

		          
           // Mean of the health spending
              mean_health_spending = sum_health_spending /counter;

           // Mean of the health coverage
              mean_health_coverage = sum_health_coverage/counter;

           // Mean of the fertility rate 
              mean_fertility_rate= sum_fertility_rate/counter;
              
           // Mean of the mortality_rate
              mean_mortality_rate= sum_mortality_rate/counter;
              

           // Mean of the past fundings
              mean_pastfund = sum_pastfund/counter;

              writeDataBase(mean_health_spending,mean_health_coverage ,mean_fertility_rate,mean_mortality_rate,mean_pastfund,arr_health_spending,arr_health_coverage,arr_fertility_rate,arr_mortality_rate,arr_pastfund,db );

        }
catch (Exception ex){
               System.out.println(ex.toString());
        }

}
     public void writeDataBase(float mean_health_spending,float mean_health_coverage , float mean_fertility_rate,float mean_mortality_rate,float mean_pastfund,ArrayList<Float> arr_health_spending,ArrayList<Float> arr_health_coverage,ArrayList<Float> arr_fertility_rate,ArrayList<Float> arr_mortality_rate, ArrayList<Float> arr_pastfund,DB db ) {

	try {
	
              //Setting the value of desired parameter
              float param_health_spending = 230;
              float param_health_coverage = 350;
              float param_fertility_rate = 210;
              float param_mortality_rate = 210;
              float param_pastfund = 10;
		       
           // array list to hold the decision score
              ArrayList<Float> arr_desc_score = new ArrayList<Float>();
              ArrayList<Integer> arr_past_rating = new ArrayList<Integer>();

              // calculating the product of mean-avg-difference and param
              int state_id =0;
              float avg_health_spending;
              float avg_health_coverage;
              float avg_fertility_rate;
              float avg_mortality_rate;
              float avg_pastfund;

              float decision_score;
              int rating_pastfund;
              while(state_id!=50){
                    // taking average across all the years
                  avg_health_spending = (Float.parseFloat(arr_health_spending.get(state_id).toString())+ Float.parseFloat(arr_health_spending.get(state_id+50).toString())+ Float.parseFloat(arr_health_spending.get(state_id+100).toString()) + Float.parseFloat(arr_health_spending.get(state_id+150).toString()) ) / 4;
                  avg_health_coverage = (Float.parseFloat(arr_health_coverage.get(state_id).toString())+ Float.parseFloat(arr_health_coverage.get(state_id+50).toString())+ Float.parseFloat(arr_health_coverage.get(state_id+100).toString()) + Float.parseFloat(arr_health_coverage.get(state_id+150).toString()) ) / 4;                  
                  avg_fertility_rate = (Float.parseFloat(arr_fertility_rate.get(state_id).toString())+ Float.parseFloat(arr_fertility_rate.get(state_id+50).toString())+ Float.parseFloat(arr_fertility_rate.get(state_id+100).toString()) + Float.parseFloat(arr_fertility_rate.get(state_id+150).toString()) ) / 4;                  
                  avg_mortality_rate = (Float.parseFloat(arr_mortality_rate.get(state_id).toString())+ Float.parseFloat(arr_mortality_rate.get(state_id+50).toString())+ Float.parseFloat(arr_mortality_rate.get(state_id+100).toString()) + Float.parseFloat(arr_mortality_rate.get(state_id+150).toString()) ) / 4;                  
                  avg_pastfund = (Float.parseFloat(arr_pastfund.get(state_id).toString())+ Float.parseFloat(arr_pastfund.get(state_id+50).toString())+ Float.parseFloat(arr_pastfund.get(state_id+100).toString()) + Float.parseFloat(arr_pastfund.get(state_id+150).toString()) ) / 4;

               // calculating the decision score based on the params
                  decision_score = ((avg_health_spending / mean_health_spending) * param_health_spending) + ((avg_health_coverage/ mean_health_coverage) * param_health_coverage) + ((avg_fertility_rate/ mean_fertility_rate) * param_fertility_rate) + ((avg_mortality_rate/ mean_mortality_rate) * param_mortality_rate);
                  arr_desc_score.add(state_id,decision_score );

               // calculating the past funding rate based on the params
                  rating_pastfund = (int) Math.floor(((avg_pastfund / mean_pastfund ) * param_pastfund)) ;
                  arr_past_rating.add(state_id,rating_pastfund);

                  state_id++;
             }
              // Update the health_rating table with decision_score
              DBCollection collection_health_rating = db.getCollection(coll_name_rating);
              for(int i=0;i<50;i++){

                  BasicDBObject updateQuery1 = new BasicDBObject();
                  BasicDBObject updateQuery2 = new BasicDBObject();
                  
                  updateQuery1.append("$set", new BasicDBObject().append("decision_score",arr_desc_score.get(i)));
                  updateQuery2.append("$set", new BasicDBObject().append("prev_year_fund_rating",arr_past_rating.get(i)));

                  BasicDBObject searchQuery = new BasicDBObject().append("state_id", i+1);

                  collection_health_rating.update(searchQuery, updateQuery1);
                  collection_health_rating.update(searchQuery, updateQuery2);

                          }
            }
		catch (Exception ex){
			 System.out.println(ex.toString());
		 }

}
     
      public static void main(String[] args) {

        HealthDecisionScoreMongo hDSC = new HealthDecisionScoreMongo();
        hDSC.readDataBase();
        System.out.println("Health Descision Rating table Populated - successfully");

    } 
    
    
}




