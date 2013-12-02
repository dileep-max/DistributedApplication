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
public class EconomyDecisionScoreMongo {
    private String connParam = "localhost";
    private String dbName = "mydb";
    private String coll_name = "economy";
    private String coll_name_rating = "economy_rating";
    
    
     public void readDataBase() {
           try{
               
                float sum_gdp =0;
                float sum_minimum_wages =0;
                float sum_per_capita_income =0;
                float sum_population=0;
                float sum_pastfund=0;
                

                float mean_gdp;
                float mean_minimum_wages;
                float mean_per_capita_income;
                float mean_population;
                float mean_pastfund;
                
                
                
		MongoClient mongo = new MongoClient( connParam , 27017 );
		DB db = mongo.getDB( dbName );
		Set<String> colls = db.getCollectionNames();
		        
                DBCollection collection = db.getCollection(coll_name);
                DBCursor edu_cursor = collection.find();

                int counter =0;
                ArrayList<Float> arr_gdp = new ArrayList<Float>();
                ArrayList<Float> arr_minimum_wages = new ArrayList<Float>();
                ArrayList<Float> arr_per_capita_income = new ArrayList<Float>();
                ArrayList<Float> arr_population = new ArrayList<Float>();
                ArrayList<Float> arr_pastfund = new ArrayList<Float>();
                
                while (edu_cursor.hasNext()) {

                      Object row_value= edu_cursor.next();
                      Object vc_value = ((BasicBSONObject) row_value).get("gdp");
                      Object texe_value = ((BasicBSONObject) row_value).get("minimum_wages");
                      Object prs_value = ((BasicBSONObject) row_value).get("per_capita_income");
                      Object gun_value = ((BasicBSONObject) row_value).get("population");
                      Object past_value = ((BasicBSONObject) row_value).get("past_funding");


                      arr_gdp.add(Float.parseFloat(vc_value.toString()));
                      arr_minimum_wages.add(Float.parseFloat(texe_value.toString()));
                      arr_per_capita_income.add(Float.parseFloat(prs_value.toString()));
                      arr_population.add(Float.parseFloat(gun_value.toString()));
                      arr_pastfund.add(Float.parseFloat(past_value.toString()));

                      sum_gdp += Float.parseFloat(vc_value.toString());
                      sum_minimum_wages += Float.parseFloat(texe_value.toString());
                      sum_per_capita_income+= Float.parseFloat(prs_value.toString());
                      sum_population += Float.parseFloat(gun_value.toString());
                      sum_pastfund += Float.parseFloat(past_value.toString());

                      counter++;
            }

		          
           // Mean of the gdp
              mean_gdp = sum_gdp /counter;

           // Mean of the minimum_wages 
              mean_minimum_wages = sum_minimum_wages/counter;

           // Mean of the per capita income
              mean_per_capita_income= sum_per_capita_income/counter;
              
           // Mean of the population
              mean_population= sum_population/counter;
              

           // Mean of the past fundings
              mean_pastfund = sum_pastfund/counter;

              writeDataBase(mean_gdp,mean_minimum_wages ,mean_per_capita_income,mean_population,mean_pastfund,arr_gdp,arr_minimum_wages,arr_per_capita_income,arr_population,arr_pastfund,db );

        }
catch (Exception ex){
               System.out.println(ex.toString());
        }

}
     public void writeDataBase(float mean_gdp,float mean_minimum_wages , float mean_per_capita_income,float mean_population,float mean_pastfund,ArrayList<Float> arr_gdp,ArrayList<Float> arr_minimum_wages,ArrayList<Float> arr_per_capita_income,ArrayList<Float> arr_population, ArrayList<Float> arr_pastfund,DB db ) {

	try {
	
              //Setting the value of desired parameter
              float param_gdp = 230;
              float param_minimum_wages = 350;
              float param_per_capita_income = 210;
              float param_population = 210;
              float param_pastfund = 10;
		       
           // array list to hold the decision score
              ArrayList<Float> arr_desc_score = new ArrayList<Float>();
              ArrayList<Integer> arr_past_rating = new ArrayList<Integer>();

              // calculating the product of mean-avg-difference and param
              int state_id =0;
              float avg_gdp;
              float avg_minimum_wages;
              float avg_per_capita_income;
              float avg_population;
              float avg_pastfund;

              float decision_score;
              int rating_pastfund;
              while(state_id!=50){
                    // taking average across all the years
                  avg_gdp = (Float.parseFloat(arr_gdp.get(state_id).toString())+ Float.parseFloat(arr_gdp.get(state_id+50).toString())+ Float.parseFloat(arr_gdp.get(state_id+100).toString()) + Float.parseFloat(arr_gdp.get(state_id+150).toString()) ) / 4;
                  avg_minimum_wages = (Float.parseFloat(arr_minimum_wages.get(state_id).toString())+ Float.parseFloat(arr_minimum_wages.get(state_id+50).toString())+ Float.parseFloat(arr_minimum_wages.get(state_id+100).toString()) + Float.parseFloat(arr_minimum_wages.get(state_id+150).toString()) ) / 4;                  
                  avg_per_capita_income = (Float.parseFloat(arr_per_capita_income.get(state_id).toString())+ Float.parseFloat(arr_per_capita_income.get(state_id+50).toString())+ Float.parseFloat(arr_per_capita_income.get(state_id+100).toString()) + Float.parseFloat(arr_per_capita_income.get(state_id+150).toString()) ) / 4;                  
                  avg_population = (Float.parseFloat(arr_population.get(state_id).toString())+ Float.parseFloat(arr_population.get(state_id+50).toString())+ Float.parseFloat(arr_population.get(state_id+100).toString()) + Float.parseFloat(arr_population.get(state_id+150).toString()) ) / 4;                  
                  avg_pastfund = (Float.parseFloat(arr_pastfund.get(state_id).toString())+ Float.parseFloat(arr_pastfund.get(state_id+50).toString())+ Float.parseFloat(arr_pastfund.get(state_id+100).toString()) + Float.parseFloat(arr_pastfund.get(state_id+150).toString()) ) / 4;

               // calculating the decision score based on the params
                  decision_score = ((avg_gdp / mean_gdp) * param_gdp) + ((avg_minimum_wages/ mean_minimum_wages) * param_minimum_wages) + ((avg_per_capita_income/ mean_per_capita_income) * param_per_capita_income) + ((avg_population/ mean_population) * param_population);
                  arr_desc_score.add(state_id,decision_score );

               // calculating the past funding rate based on the params
                  rating_pastfund = (int) Math.floor(((avg_pastfund / mean_pastfund ) * param_pastfund)) ;
                  arr_past_rating.add(state_id,rating_pastfund);

                  state_id++;
             }
              // Update the economy_rating table with decision_score
              DBCollection collection_economy_rating = db.getCollection(coll_name_rating);
              for(int i=0;i<50;i++){

                  BasicDBObject updateQuery1 = new BasicDBObject();
                  BasicDBObject updateQuery2 = new BasicDBObject();
                  
                  updateQuery1.append("$set", new BasicDBObject().append("decision_score",arr_desc_score.get(i)));
                  updateQuery2.append("$set", new BasicDBObject().append("prev_year_fund_rating",arr_past_rating.get(i)));

                  BasicDBObject searchQuery = new BasicDBObject().append("state_id", i+1);

                  collection_economy_rating.update(searchQuery, updateQuery1);
                  collection_economy_rating.update(searchQuery, updateQuery2);

                          }
            }
		catch (Exception ex){
			 System.out.println(ex.toString());
		 }

}
     
      public static void main(String[] args) {

        EconomyDecisionScoreMongo eDSC = new EconomyDecisionScoreMongo();
        eDSC.readDataBase();
        System.out.println("Economy Descision Rating table Populated - successfully");

    } 
    
    
}
