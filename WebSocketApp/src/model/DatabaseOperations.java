package model;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class DatabaseOperations {
	
	static MongoDatabase database;
	static Gson gson = new Gson();
	private static DatabaseOperations instance = null;
	static MongoCollection<Document> collection = null;
	
	private DatabaseOperations() {
		MongoClientURI uri = new MongoClientURI(
				"mongodb+srv://cani:190189@cluster0-hlfgz.mongodb.net/test?retryWrites=true&w=majority");
		
		@SuppressWarnings("resource")
		MongoClient mongoClient = new MongoClient(uri);
		database = mongoClient.getDatabase("ChatApp");
		
	}
	
	public synchronized static DatabaseOperations getInstance() {
		if(instance == null)
        {
            instance  = new DatabaseOperations();
        }
        return instance;
	}
	
	public Boolean createDocument(@SuppressWarnings("rawtypes") WebSocketMessage message){
		
		String a = gson.toJson(message);
		Document doc = Document.parse(a);
		
		try {
			collection = database.getCollection("message");
		}
		catch (IllegalArgumentException e) {			
			database.createCollection("message");	
			collection = database.getCollection("message");
		}
        finally {
        	collection.insertOne(doc);
        	
        }
		
		return true;
		
	}
	
	public WebSocketMessage findDocument(String key, String value) {
		DBObject query = new BasicDBObject(key, value);
    	FindIterable<Document> messageList = collection.find((Bson) query);

        // Getting the iterator 
        MongoCursor<Document> it = messageList.iterator(); 
      
        while (it.hasNext()) {  
           System.out.println(it.next());  
        }
        
        return null;
	}

	

}
