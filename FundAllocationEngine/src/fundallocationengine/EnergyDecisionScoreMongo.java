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

public class EnergyDecisionScoreMongo {
    
     
private String connParam = "localhost";
    private String dbName = "mydb";
    private String coll_name = "energy";
    private String coll_name_rating = "energy_rating";
    
    
     public void readDataBase() {
           try{
               
                float sum_renewable_energy =0;
                float sum_petroleum_usage =0;
                float sum_crudeoil_prod =0;
                float sum_nat_gas_prod=0;
                float sum_pastfund=0;
                

                float mean_renewable_energy;
                float mean_petroleum_usage;
                float mean_crudeoil_prod;
                float mean_nat_gas_prod;
                float mean_pastfund;
                
                
                
		MongoClient mongo = new MongoClient( connParam , 27017 );
		DB db = mongo.getDB( dbName );
		Set<String> colls = db.getCollectionNames();
		        
                DBCollection collection = db.getCollection(coll_name);
                DBCursor edu_cursor = collection.find();

                int counter =0;
                ArrayList<Float> arr_renewable_energy = new ArrayList<Float>();
                ArrayList<Float> arr_petroleum_usage = new ArrayList<Float>();
                ArrayList<Float> arr_crudeoil_prod = new ArrayList<Float>();
                ArrayList<Float> arr_nat_gas_prod = new ArrayList<Float>();
                ArrayList<Float> arr_pastfund = new ArrayList<Float>();
                
                while (edu_cursor.hasNext()) {

                      Object row_value= edu_cursor.next();
                      Object vc_value = ((BasicBSONObject) row_value).get("renewable_energy");
                      Object texe_value = ((BasicBSONObject) row_value).get("petroleum_usage");
                      Object prs_value = ((BasicBSONObject) row_value).get("crudeoil_prod");
                      Object gun_value = ((BasicBSONObject) row_value).get("nat_gas_prod");
                      Object past_value = ((BasicBSONObject) row_value).get("past_funding");


                      arr_renewable_energy.add(Float.parseFloat(vc_value.toString()));
                      arr_petroleum_usage.add(Float.parseFloat(texe_value.toString()));
                      arr_crudeoil_prod.add(Float.parseFloat(prs_value.toString()));
                      arr_nat_gas_prod.add(Float.parseFloat(gun_value.toString()));
                      arr_pastfund.add(Float.parseFloat(past_value.toString()));

                      sum_renewable_energy += Float.parseFloat(vc_value.toString());
                      sum_petroleum_usage += Float.parseFloat(texe_value.toString());
                      sum_crudeoil_prod+= Float.parseFloat(prs_value.toString());
                      sum_nat_gas_prod += Float.parseFloat(gun_value.toString());
                      sum_pastfund += Float.parseFloat(past_value.toString());

                      counter++;
            }

		          
           // Mean of the renewable energy consumption
              mean_renewable_energy = sum_renewable_energy /counter;

           // Mean of the petroleum_usage 
              mean_petroleum_usage = sum_petroleum_usage/counter;

           // Mean of the crude oil production 
              mean_crudeoil_prod= sum_crudeoil_prod/counter;
              
           // Mean of the nat_gas_prod
              mean_nat_gas_prod= sum_nat_gas_prod/counter;
              

           // Mean of the past fundings
              mean_pastfund = sum_pastfund/counter;

              writeDataBase(mean_renewable_energy,mean_petroleum_usage ,mean_crudeoil_prod,mean_nat_gas_prod,mean_pastfund,arr_renewable_energy,arr_petroleum_usage,arr_crudeoil_prod,arr_nat_gas_prod,arr_pastfund,db );

        }
catch (Exception ex){
               System.out.println(ex.toString());
        }

}
     public void writeDataBase(float mean_renewable_energy,float mean_petroleum_usage , float mean_crudeoil_prod,float mean_nat_gas_prod,float mean_pastfund,ArrayList<Float> arr_renewable_energy,ArrayList<Float> arr_petroleum_usage,ArrayList<Float> arr_crudeoil_prod,ArrayList<Float> arr_nat_gas_prod, ArrayList<Float> arr_pastfund,DB db ) {

	try {
	
              //Setting the value of desired parameter
              float param_renewable_energy = 230;
              float param_petroleum_usage = 350;
              float param_crudeoil_prod = 210;
              float param_nat_gas_prod = 210;
              float param_pastfund = 10;
		       
           // array list to hold the decision score
              ArrayList<Float> arr_desc_score = new ArrayList<Float>();
              ArrayList<Integer> arr_past_rating = new ArrayList<Integer>();

              // calculating the product of mean-avg-difference and param
              int state_id =0;
              float avg_renewable_energy;
              float avg_petroleum_usage;
              float avg_crudeoil_prod;
              float avg_nat_gas_prod;
              float avg_pastfund;

              float decision_score;
              int rating_pastfund;
              while(state_id!=50){
                    // taking average across all the years
                  avg_renewable_energy = (Float.parseFloat(arr_renewable_energy.get(state_id).toString())+ Float.parseFloat(arr_renewable_energy.get(state_id+50).toString())+ Float.parseFloat(arr_renewable_energy.get(state_id+100).toString()) + Float.parseFloat(arr_renewable_energy.get(state_id+150).toString()) ) / 4;
                  avg_petroleum_usage = (Float.parseFloat(arr_petroleum_usage.get(state_id).toString())+ Float.parseFloat(arr_petroleum_usage.get(state_id+50).toString())+ Float.parseFloat(arr_petroleum_usage.get(state_id+100).toString()) + Float.parseFloat(arr_petroleum_usage.get(state_id+150).toString()) ) / 4;                  
                  avg_crudeoil_prod = (Float.parseFloat(arr_crudeoil_prod.get(state_id).toString())+ Float.parseFloat(arr_crudeoil_prod.get(state_id+50).toString())+ Float.parseFloat(arr_crudeoil_prod.get(state_id+100).toString()) + Float.parseFloat(arr_crudeoil_prod.get(state_id+150).toString()) ) / 4;                  
                  avg_nat_gas_prod = (Float.parseFloat(arr_nat_gas_prod.get(state_id).toString())+ Float.parseFloat(arr_nat_gas_prod.get(state_id+50).toString())+ Float.parseFloat(arr_nat_gas_prod.get(state_id+100).toString()) + Float.parseFloat(arr_nat_gas_prod.get(state_id+150).toString()) ) / 4;                  
                  avg_pastfund = (Float.parseFloat(arr_pastfund.get(state_id).toString())+ Float.parseFloat(arr_pastfund.get(state_id+50).toString())+ Float.parseFloat(arr_pastfund.get(state_id+100).toString()) + Float.parseFloat(arr_pastfund.get(state_id+150).toString()) ) / 4;

               // calculating the decision score based on the params
                  decision_score = ((avg_renewable_energy / mean_renewable_energy) * param_renewable_energy) + ((avg_petroleum_usage/ mean_petroleum_usage) * param_petroleum_usage) + ((avg_crudeoil_prod/ mean_crudeoil_prod) * param_crudeoil_prod) + ((avg_nat_gas_prod/ mean_nat_gas_prod) * param_nat_gas_prod);
                  arr_desc_score.add(state_id,decision_score );

               // calculating the past funding rate based on the params
                  rating_pastfund = (int) Math.floor(((avg_pastfund / mean_pastfund ) * param_pastfund)) ;
                  arr_past_rating.add(state_id,rating_pastfund);

                  state_id++;
             }
              // Update the energy_rating table with decision_score
              DBCollection collection_energy_rating = db.getCollection(coll_name_rating);
              for(int i=0;i<50;i++){

                  BasicDBObject updateQuery1 = new BasicDBObject();
                  BasicDBObject updateQuery2 = new BasicDBObject();
                  
                  updateQuery1.append("$set", new BasicDBObject().append("decision_score",arr_desc_score.get(i)));
                  updateQuery2.append("$set", new BasicDBObject().append("prev_year_fund_rating",arr_past_rating.get(i)));

                  BasicDBObject searchQuery = new BasicDBObject().append("state_id", i+1);

                  collection_energy_rating.update(searchQuery, updateQuery1);
                  collection_energy_rating.update(searchQuery, updateQuery2);

                          }
            }
		catch (Exception ex){
			 System.out.println(ex.toString());
		 }

}
     
      public static void main(String[] args) {

        EnergyDecisionScoreMongo enDSC = new EnergyDecisionScoreMongo();
        enDSC.readDataBase();
        System.out.println("Energy Descision Rating table Populated - successfully");

    } 
    
    
}



