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
public class CrimeDecisionScoreMongo {
        // Mongo DB parameters
    private String connParam = "localhost";
    private String dbName = "mydb";
    private String coll_name = "crime";
    private String coll_name_rating = "crime_rating";
    
    
    
     public void readDataBase() {
           try{
               
                float sum_violent_crime_rate =0;
                float sum_tot_executions =0;
                float sum_prisioners =0;
                float sum_gun_ownership=0;
                float sum_pastfund=0;
                

                float mean_violent_crime_rate;
                float mean_tot_executions;
                float mean_prisioners;
                float mean_gun_ownership;
                float mean_pastfund;
                
                
                
		MongoClient mongo = new MongoClient( connParam , 27017 );
		DB db = mongo.getDB( dbName );
		Set<String> colls = db.getCollectionNames();
		        

                DBCollection collection = db.getCollection(coll_name);
                DBCursor edu_cursor = collection.find();

                int counter =0;
                ArrayList<Float> arr_violent_crime_rate = new ArrayList<Float>();
                ArrayList<Float> arr_tot_executions = new ArrayList<Float>();
                ArrayList<Float> arr_prisioners = new ArrayList<Float>();
                ArrayList<Float> arr_gun_ownership = new ArrayList<Float>();
                ArrayList<Float> arr_pastfund = new ArrayList<Float>();
                
                while (edu_cursor.hasNext()) {

                      Object row_value= edu_cursor.next();
                      Object vc_value = ((BasicBSONObject) row_value).get("violent_crime_rate");
                      Object texe_value = ((BasicBSONObject) row_value).get("total_executions");
                      Object prs_value = ((BasicBSONObject) row_value).get("prisoners_per_100,000_population");
                      Object gun_value = ((BasicBSONObject) row_value).get("percentage_of_gun_ownership");
                      Object past_value = ((BasicBSONObject) row_value).get("past_funding");


                      arr_violent_crime_rate.add(Float.parseFloat(vc_value.toString()));
                      arr_tot_executions.add(Float.parseFloat(texe_value.toString()));
                      arr_prisioners.add(Float.parseFloat(prs_value.toString()));
                      arr_gun_ownership.add(Float.parseFloat(gun_value.toString()));
                      arr_pastfund.add(Float.parseFloat(past_value.toString()));

                      sum_violent_crime_rate += Float.parseFloat(vc_value.toString());
                      sum_tot_executions += Float.parseFloat(texe_value.toString());
                      sum_prisioners+= Float.parseFloat(prs_value.toString());
                      sum_gun_ownership += Float.parseFloat(gun_value.toString());
                      sum_pastfund += Float.parseFloat(past_value.toString());

                      counter++;
            }

		          
           // Mean of the violent crime rate 
              mean_violent_crime_rate = sum_violent_crime_rate /counter;

           // Mean of the tot_executions
              mean_tot_executions = sum_tot_executions/counter;

           // Mean of the prisoners 
              mean_prisioners= sum_prisioners/counter;
              
           // Mean of the gun ownership
              mean_gun_ownership= sum_gun_ownership/counter;
              

           // Mean of the past fundings
              mean_pastfund = sum_pastfund/counter;

              writeDataBase(mean_violent_crime_rate,mean_tot_executions ,mean_prisioners,mean_gun_ownership,mean_pastfund,arr_violent_crime_rate,arr_tot_executions,arr_prisioners,arr_gun_ownership,arr_pastfund,db );

        }
catch (Exception ex){
               System.out.println(ex.toString());
        }

}
     
     public void writeDataBase(float mean_violent_crime_rate,float mean_tot_executions , float mean_prisioners,float mean_gun_ownership,float mean_pastfund,ArrayList<Float> arr_violent_crime_rate,ArrayList<Float> arr_tot_executions,ArrayList<Float> arr_prisioners,ArrayList<Float> arr_gun_ownership, ArrayList<Float> arr_pastfund,DB db ) {

	try {
	
              //Setting the value of desired parameter
              float param_violent_crime_rate = 230;
              float param_tot_executions = 350;
              float param_prisioners = 210;
              float param_gun_ownership = 210;
              float param_pastfund = 10;
		       
           // array list to hold the decision score
              ArrayList<Float> arr_desc_score = new ArrayList<Float>();
              ArrayList<Integer> arr_past_rating = new ArrayList<Integer>();

              // calculating the product of mean-avg-difference and param
              int state_id =0;
              float avg_violent_crime_rate;
              float avg_tot_executions;
              float avg_prisioners;
              float avg_gun_ownership;
              float avg_pastfund;

              float decision_score;
              int rating_pastfund;
              while(state_id!=50){
                    // taking average across all the years
                  avg_violent_crime_rate = (Float.parseFloat(arr_violent_crime_rate.get(state_id).toString())+ Float.parseFloat(arr_violent_crime_rate.get(state_id+50).toString())+ Float.parseFloat(arr_violent_crime_rate.get(state_id+100).toString()) + Float.parseFloat(arr_violent_crime_rate.get(state_id+150).toString()) ) / 4;
                  avg_tot_executions = (Float.parseFloat(arr_tot_executions.get(state_id).toString())+ Float.parseFloat(arr_tot_executions.get(state_id+50).toString())+ Float.parseFloat(arr_tot_executions.get(state_id+100).toString()) + Float.parseFloat(arr_tot_executions.get(state_id+150).toString()) ) / 4;                  
                  avg_prisioners = (Float.parseFloat(arr_prisioners.get(state_id).toString())+ Float.parseFloat(arr_prisioners.get(state_id+50).toString())+ Float.parseFloat(arr_prisioners.get(state_id+100).toString()) + Float.parseFloat(arr_prisioners.get(state_id+150).toString()) ) / 4;                  
                  avg_gun_ownership = (Float.parseFloat(arr_gun_ownership.get(state_id).toString())+ Float.parseFloat(arr_gun_ownership.get(state_id+50).toString())+ Float.parseFloat(arr_gun_ownership.get(state_id+100).toString()) + Float.parseFloat(arr_gun_ownership.get(state_id+150).toString()) ) / 4;                  
                  avg_pastfund = (Float.parseFloat(arr_pastfund.get(state_id).toString())+ Float.parseFloat(arr_pastfund.get(state_id+50).toString())+ Float.parseFloat(arr_pastfund.get(state_id+100).toString()) + Float.parseFloat(arr_pastfund.get(state_id+150).toString()) ) / 4;

               // calculating the decision score based on the params
                  decision_score = ((avg_violent_crime_rate / mean_violent_crime_rate) * param_violent_crime_rate) + ((avg_tot_executions/ mean_tot_executions) * param_tot_executions) + ((avg_prisioners/ mean_prisioners) * param_prisioners) + ((avg_gun_ownership/ mean_gun_ownership) * param_gun_ownership);
                  arr_desc_score.add(state_id,decision_score );

               // calculating the past funding rate based on the params
                  rating_pastfund = (int) Math.floor(((avg_pastfund / mean_pastfund ) * param_pastfund)) ;
                  arr_past_rating.add(state_id,rating_pastfund);

                  state_id++;
             }
              // Update the crime_rating table with decision_score
              DBCollection collection_crime_rating = db.getCollection(coll_name_rating);
              for(int i=0;i<50;i++){

                  BasicDBObject updateQuery1 = new BasicDBObject();
                  BasicDBObject updateQuery2 = new BasicDBObject();
                  
                  updateQuery1.append("$set", new BasicDBObject().append("decision_score",arr_desc_score.get(i)));
                  updateQuery2.append("$set", new BasicDBObject().append("prev_year_fund_rating",arr_past_rating.get(i)));

                  BasicDBObject searchQuery = new BasicDBObject().append("state_id", i+1);

                  collection_crime_rating.update(searchQuery, updateQuery1);
                  collection_crime_rating.update(searchQuery, updateQuery2);

                          }
            }
		catch (Exception ex){
			 System.out.println(ex.toString());
		 }

}
     
        public static void main(String[] args) {

        CrimeDecisionScoreMongo cDSC = new CrimeDecisionScoreMongo();
        cDSC.readDataBase();
        System.out.println("Crime Descision Rating table Populated - successfully");

    }
    
    
    
}
