package repository;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

//import java.util.logging.Level;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public class DatabaseConnection {
	static MongoDatabase database = null;

	private static DatabaseConnection instance = null;
	CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
			fromProviders(PojoCodecProvider.builder().automatic(true).build()));

	private DatabaseConnection() {

		MongoClientURI uri = new MongoClientURI(
				"mongodb+srv://cani:190189@cluster0-hlfgz.mongodb.net/test?retryWrites=true&w=majority");

		@SuppressWarnings("resource")
		MongoClient mongoClient = new MongoClient(uri);
		//java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);
		try {
			database = mongoClient.getDatabase("ChatApp").withCodecRegistry(pojoCodecRegistry);
		} catch (IllegalArgumentException e) {
			System.out.println("could not connect to DB");
		}

	}

	public synchronized static MongoDatabase getInstance() {
		if (instance == null) {
			instance = new DatabaseConnection();
		}
		return database;
	}
}
