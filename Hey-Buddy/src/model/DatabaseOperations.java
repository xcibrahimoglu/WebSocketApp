package model;

import java.util.List;

import org.bson.codecs.configuration.CodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class DatabaseOperations {

	static MongoDatabase database = null;
	@SuppressWarnings("rawtypes")
	MongoCollection<Message> collection = null;

	private static DatabaseOperations instance = null;
	CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
			fromProviders(PojoCodecProvider.builder().automatic(true).build()));

	private DatabaseOperations() {

		MongoClientURI uri = new MongoClientURI(
				"mongodb+srv://cani:190189@cluster0-hlfgz.mongodb.net/test?retryWrites=true&w=majority");

		@SuppressWarnings("resource")
		MongoClient mongoClient = new MongoClient(uri);
		try {
			database = mongoClient.getDatabase("ChatApp").withCodecRegistry(pojoCodecRegistry);
		} catch (IllegalArgumentException e) {
			System.out.println("could not connect to DB");
		}

	}

	public synchronized static DatabaseOperations getInstance() {
		if (instance == null) {
			instance = new DatabaseOperations();
		}
		return instance;
	}

	public <T> void createDocument(T message) {

		/*try {
			collection = database.getCollection("Messages", Message.class);
		} catch (IllegalArgumentException e) {
			database.createCollection("Messages");
			collection = database.getCollection("Messages", Message.class);
		}*/

		@SuppressWarnings("unchecked")
		MongoCollection<T> collection = (MongoCollection<T>) database.getCollection(message.getClass().getSimpleName(), message.getClass());
        collection.insertOne(message);
        

	}

	@SuppressWarnings("rawtypes")
	public List<Message> findDocument(String[] keys, String value) {

		return null;

	}

}
