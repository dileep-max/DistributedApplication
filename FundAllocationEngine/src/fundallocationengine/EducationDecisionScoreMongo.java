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
public class EducationDecisionScoreMongo {
    
    // Mongo DB parameters
    private String connParam = "localhost";
    private String dbName = "mydb";
    private String coll_edu = "education";
    private String coll_edu_rating = "education_rating";
    
    
    public void readDataBase() {
           try{
               
                float sum_highschool =0;
                float sum_bachelor =0;
                float sum_advcdeg =0;
                float sum_pastfund=0;

                float mean_highschool;
                float mean_bachelor;
                float mean_advcdeg;
                float mean_pastfund;
                
                
                
		MongoClient mongo = new MongoClient( connParam , 27017 );
		DB db = mongo.getDB( dbName );
		Set<String> colls = db.getCollectionNames();
		        
                // Available Collection Names
                for (String s : colls) {
                        System.out.println(s);
                }
                DBCollection collection = db.getCollection(coll_edu);
                DBCursor edu_cursor = collection.find();

                int counter =0;
                ArrayList<Float> arr_highschool = new ArrayList<Float>();
                ArrayList<Float> arr_bachelor = new ArrayList<Float>();
                ArrayList<Float> arr_advcdeg = new ArrayList<Float>();
                ArrayList<Float> arr_pastfund = new ArrayList<Float>();

                while (edu_cursor.hasNext()) {

                      Object row_value= edu_cursor.next();
                      Object hs_value = ((BasicBSONObject) row_value).get("high_school");
                      Object bach_value = ((BasicBSONObject) row_value).get("bachelors");
                      Object advnc_value = ((BasicBSONObject) row_value).get("advanced_degree");
                      Object past_value = ((BasicBSONObject) row_value).get("past_funding");

                      //System.out.println("Highscool");
    //		              System.out.println(hs_value.toString());
    //		              System.out.println(bach_value.toString());
    //		              System.out.println(advnc_value.toString());
    //		              System.out.println(past_value .toString());

                      arr_highschool.add(Float.parseFloat(hs_value.toString()));
                      arr_bachelor.add(Float.parseFloat(bach_value.toString()));
                      arr_advcdeg.add(Float.parseFloat(advnc_value.toString()));
                      arr_pastfund.add(Float.parseFloat(past_value.toString()));

                      sum_highschool += Float.parseFloat(hs_value.toString());
                      sum_bachelor += Float.parseFloat(bach_value.toString());
                      sum_advcdeg += Float.parseFloat(advnc_value.toString());
                      sum_pastfund += Float.parseFloat(past_value.toString());

                      counter++;
            }
		          
           // Mean of the high school enrollment
              mean_highschool = sum_highschool/counter;

           // Mean of the bachelor enrollment
              mean_bachelor = sum_bachelor/counter;

           // Mean of the advanced degree enrollment
              mean_advcdeg = sum_advcdeg/counter;

           // Mean of the past fundings
              mean_pastfund = sum_pastfund/counter;

              writeDataBase(mean_highschool,mean_bachelor,mean_advcdeg,mean_pastfund,arr_highschool,arr_bachelor,arr_advcdeg,arr_pastfund,db );

        }
catch (Exception ex){
               System.out.println(ex.toString());
        }

}

public void writeDataBase(float mean_highschool, float mean_bachelor, float mean_advcdeg,float mean_pastfund, ArrayList<Float> arr_highschool, ArrayList<Float> arr_bachelor, ArrayList<Float> arr_advcdeg, ArrayList<Float> arr_pastfund, DB db  ){

	try {
	
              //Setting the value of desired parameter
              float param_highschool = 230;
              float param_bachelor = 350;
              float param_advcdeg = 420;

              float param_pastfund = 10;
		       
           // array list to hold the decision score
              ArrayList<Float> arr_desc_score = new ArrayList<Float>();
              ArrayList<Integer> arr_past_rating = new ArrayList<Integer>();

              // calculating the product of mean-avg-difference and param
              int state_id =0;
              float avg_highschool;
              float avg_bachelor;
              float avg_advcdeg;
              float avg_pastfund;

              float decision_score;
              int rating_pastfund;
              while(state_id!=50){
                    // taking average across all the years
                  avg_highschool = (Float.parseFloat(arr_highschool.get(state_id).toString())+ Float.parseFloat(arr_highschool.get(state_id+50).toString())+ Float.parseFloat(arr_highschool.get(state_id+100).toString()) + Float.parseFloat(arr_highschool.get(state_id+150).toString()) ) / 4;
                  avg_bachelor = (Float.parseFloat(arr_bachelor.get(state_id).toString())+ Float.parseFloat(arr_bachelor.get(state_id+50).toString())+ Float.parseFloat(arr_bachelor.get(state_id+100).toString()) + Float.parseFloat(arr_bachelor.get(state_id+150).toString()) ) / 4;                  
                  avg_advcdeg = (Float.parseFloat(arr_advcdeg.get(state_id).toString())+ Float.parseFloat(arr_advcdeg.get(state_id+50).toString())+ Float.parseFloat(arr_advcdeg.get(state_id+100).toString()) + Float.parseFloat(arr_advcdeg.get(state_id+150).toString()) ) / 4;                  
                  avg_pastfund = (Float.parseFloat(arr_pastfund.get(state_id).toString())+ Float.parseFloat(arr_pastfund.get(state_id+50).toString())+ Float.parseFloat(arr_pastfund.get(state_id+100).toString()) + Float.parseFloat(arr_pastfund.get(state_id+150).toString()) ) / 4;

               // calculating the decision score based on the params
                  decision_score = ((avg_highschool / mean_highschool) * param_highschool) + ((avg_bachelor/ mean_bachelor) * param_bachelor) + ((avg_advcdeg/ mean_advcdeg) * param_advcdeg);
                  arr_desc_score.add(state_id,decision_score );

               // calculating the past funding rate based on the params
                  rating_pastfund = (int) Math.floor(((avg_pastfund / mean_pastfund ) * param_pastfund)) ;
                  arr_past_rating.add(state_id,rating_pastfund);

                  state_id++;
             }
              // Update the education_rating table with decision_score
              DBCollection collection_education_rating = db.getCollection(coll_edu_rating);
              for(int i=0;i<50;i++){

                  BasicDBObject updateQuery = new BasicDBObject();
                  updateQuery.append("$set", new BasicDBObject().append("decision_score",arr_desc_score.get(i)));
                  updateQuery.append("$set", new BasicDBObject().append("prev_year_fund_rating",arr_past_rating.get(i)));

                  BasicDBObject searchQuery = new BasicDBObject().append("state_id", i+1);

                  collection_education_rating.update(searchQuery, updateQuery);

                          }
            }
		catch (Exception ex){
			 System.out.println(ex.toString());
		 }

}

    public static void main(String[] args) {

        EducationDecisionScoreMongo eDSC = new EducationDecisionScoreMongo();
        eDSC.readDataBase();
        System.out.println("Education Descision Rating table Populated - successfully");

    }
 
    
}


