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


public class TransportationDecisionScoreMongo {
    
private String connParam = "localhost";
    private String dbName = "mydb";
    private String coll_name = "transportation";
    private String coll_name_rating = "transportation_rating";
    
    
     public void readDataBase() {
           try{
               
                float sum_tot_registd_vehicles =0;
                float sum_no_of_public_transportation =0;
                float sum_no_of_highways =0;
                float sum_monthly_vehicle_sales=0;
                float sum_pastfund=0;
                

                float mean_tot_registd_vehicles;
                float mean_no_of_public_transportation;
                float mean_no_of_highways;
                float mean_monthly_vehicle_sales;
                float mean_pastfund;
                
                
                
		MongoClient mongo = new MongoClient( connParam , 27017 );
		DB db = mongo.getDB( dbName );
		Set<String> colls = db.getCollectionNames();
		        
                DBCollection collection = db.getCollection(coll_name);
                DBCursor edu_cursor = collection.find();

                int counter =0;
                ArrayList<Float> arr_tot_registd_vehicles = new ArrayList<Float>();
                ArrayList<Float> arr_no_of_public_transportation = new ArrayList<Float>();
                ArrayList<Float> arr_no_of_highways = new ArrayList<Float>();
                ArrayList<Float> arr_monthly_vehicle_sales = new ArrayList<Float>();
                ArrayList<Float> arr_pastfund = new ArrayList<Float>();
                
                while (edu_cursor.hasNext()) {

                      Object row_value= edu_cursor.next();
                      Object vc_value = ((BasicBSONObject) row_value).get("tot_registd_vehicles");
                      Object texe_value = ((BasicBSONObject) row_value).get("no_of_public_transportation");
                      Object prs_value = ((BasicBSONObject) row_value).get("no_of_highways");
                      Object gun_value = ((BasicBSONObject) row_value).get("monthly_vehicle_sales");
                      Object past_value = ((BasicBSONObject) row_value).get("past_funding");


                      arr_tot_registd_vehicles.add(Float.parseFloat(vc_value.toString()));
                      arr_no_of_public_transportation.add(Float.parseFloat(texe_value.toString()));
                      arr_no_of_highways.add(Float.parseFloat(prs_value.toString()));
                      arr_monthly_vehicle_sales.add(Float.parseFloat(gun_value.toString()));
                      arr_pastfund.add(Float.parseFloat(past_value.toString()));

                      sum_tot_registd_vehicles += Float.parseFloat(vc_value.toString());
                      sum_no_of_public_transportation += Float.parseFloat(texe_value.toString());
                      sum_no_of_highways+= Float.parseFloat(prs_value.toString());
                      sum_monthly_vehicle_sales += Float.parseFloat(gun_value.toString());
                      sum_pastfund += Float.parseFloat(past_value.toString());

                      counter++;
            }

		          
           // Mean of the total registered vehicles
              mean_tot_registd_vehicles = sum_tot_registd_vehicles /counter;

           // Mean of the no_of_public_transportation 
              mean_no_of_public_transportation = sum_no_of_public_transportation/counter;

           // Mean of the highways 
              mean_no_of_highways= sum_no_of_highways/counter;
              
           // Mean of the monthly_vehicle_sales
              mean_monthly_vehicle_sales= sum_monthly_vehicle_sales/counter;
              

           // Mean of the past fundings
              mean_pastfund = sum_pastfund/counter;

              writeDataBase(mean_tot_registd_vehicles,mean_no_of_public_transportation ,mean_no_of_highways,mean_monthly_vehicle_sales,mean_pastfund,arr_tot_registd_vehicles,arr_no_of_public_transportation,arr_no_of_highways,arr_monthly_vehicle_sales,arr_pastfund,db );

        }
catch (Exception ex){
               System.out.println(ex.toString());
        }

}
     public void writeDataBase(float mean_tot_registd_vehicles,float mean_no_of_public_transportation , float mean_no_of_highways,float mean_monthly_vehicle_sales,float mean_pastfund,ArrayList<Float> arr_tot_registd_vehicles,ArrayList<Float> arr_no_of_public_transportation,ArrayList<Float> arr_no_of_highways,ArrayList<Float> arr_monthly_vehicle_sales, ArrayList<Float> arr_pastfund,DB db ) {

	try {
	
              //Setting the value of desired parameter
              float param_tot_registd_vehicles = 230;
              float param_no_of_public_transportation = 350;
              float param_no_of_highways = 210;
              float param_monthly_vehicle_sales = 210;
              float param_pastfund = 10;
		       
           // array list to hold the decision score
              ArrayList<Float> arr_desc_score = new ArrayList<Float>();
              ArrayList<Integer> arr_past_rating = new ArrayList<Integer>();

              // calculating the product of mean-avg-difference and param
              int state_id =0;
              float avg_tot_registd_vehicles;
              float avg_no_of_public_transportation;
              float avg_no_of_highways;
              float avg_monthly_vehicle_sales;
              float avg_pastfund;

              float decision_score;
              int rating_pastfund;
              while(state_id!=50){
                    // taking average across all the years
                  avg_tot_registd_vehicles = (Float.parseFloat(arr_tot_registd_vehicles.get(state_id).toString())+ Float.parseFloat(arr_tot_registd_vehicles.get(state_id+50).toString())+ Float.parseFloat(arr_tot_registd_vehicles.get(state_id+100).toString()) + Float.parseFloat(arr_tot_registd_vehicles.get(state_id+150).toString()) ) / 4;
                  avg_no_of_public_transportation = (Float.parseFloat(arr_no_of_public_transportation.get(state_id).toString())+ Float.parseFloat(arr_no_of_public_transportation.get(state_id+50).toString())+ Float.parseFloat(arr_no_of_public_transportation.get(state_id+100).toString()) + Float.parseFloat(arr_no_of_public_transportation.get(state_id+150).toString()) ) / 4;                  
                  avg_no_of_highways = (Float.parseFloat(arr_no_of_highways.get(state_id).toString())+ Float.parseFloat(arr_no_of_highways.get(state_id+50).toString())+ Float.parseFloat(arr_no_of_highways.get(state_id+100).toString()) + Float.parseFloat(arr_no_of_highways.get(state_id+150).toString()) ) / 4;                  
                  avg_monthly_vehicle_sales = (Float.parseFloat(arr_monthly_vehicle_sales.get(state_id).toString())+ Float.parseFloat(arr_monthly_vehicle_sales.get(state_id+50).toString())+ Float.parseFloat(arr_monthly_vehicle_sales.get(state_id+100).toString()) + Float.parseFloat(arr_monthly_vehicle_sales.get(state_id+150).toString()) ) / 4;                  
                  avg_pastfund = (Float.parseFloat(arr_pastfund.get(state_id).toString())+ Float.parseFloat(arr_pastfund.get(state_id+50).toString())+ Float.parseFloat(arr_pastfund.get(state_id+100).toString()) + Float.parseFloat(arr_pastfund.get(state_id+150).toString()) ) / 4;

               // calculating the decision score based on the params
                  decision_score = ((avg_tot_registd_vehicles / mean_tot_registd_vehicles) * param_tot_registd_vehicles) + ((avg_no_of_public_transportation/ mean_no_of_public_transportation) * param_no_of_public_transportation) + ((avg_no_of_highways/ mean_no_of_highways) * param_no_of_highways) + ((avg_monthly_vehicle_sales/ mean_monthly_vehicle_sales) * param_monthly_vehicle_sales);
                  arr_desc_score.add(state_id,decision_score );

               // calculating the past funding rate based on the params
                  rating_pastfund = (int) Math.floor(((avg_pastfund / mean_pastfund ) * param_pastfund)) ;
                  arr_past_rating.add(state_id,rating_pastfund);

                  state_id++;
             }
              // Update the transportation_rating table with decision_score
              DBCollection collection_transportation_rating = db.getCollection(coll_name_rating);
              for(int i=0;i<50;i++){

                  BasicDBObject updateQuery1 = new BasicDBObject();
                  BasicDBObject updateQuery2 = new BasicDBObject();
                  
                  updateQuery1.append("$set", new BasicDBObject().append("decision_score",arr_desc_score.get(i)));
                  updateQuery2.append("$set", new BasicDBObject().append("prev_year_fund_rating",arr_past_rating.get(i)));

                  BasicDBObject searchQuery = new BasicDBObject().append("state_id", i+1);

                  collection_transportation_rating.update(searchQuery, updateQuery1);
                  collection_transportation_rating.update(searchQuery, updateQuery2);

                          }
            }
		catch (Exception ex){
			 System.out.println(ex.toString());
		 }

}
     
      public static void main(String[] args) {

        TransportationDecisionScoreMongo tDSC = new TransportationDecisionScoreMongo();
        tDSC.readDataBase();
        System.out.println("Transportation Descision Rating table Populated - successfully");

    } 
    
    
}


