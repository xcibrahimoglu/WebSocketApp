package model;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
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
	MongoCollection<Document> collection = null;

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

	public void createDocument(Message<?> message) {

		collection = database.getCollection("Message");

		Document document = new Document();
		document.put("sender", message.getSender());
		document.put("receiver", message.getReceiver());
		document.put("contentType", message.getContentType());
		document.put("content", message.getContent());
		document.put("receivedDate", message.getReceivedDate());

		collection.insertOne(document);

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Message<?>> findDocument(String[] keys, String value) {
		collection = database.getCollection("Message");

		List<Document> criterias = new ArrayList<Document>();

		for (String key : keys) {
			Document document = new Document(key,value);
			criterias.add(document);
		}

		Document query = new Document("$or",criterias);
		List<Document> documents = new ArrayList<Document>();
		collection.find(query).into(documents);
		
		List<Message<?>> messages = new ArrayList<Message<?>>();
		
		for(Document document: documents) {
			Message message = new Message();
			message.setSender(document.getString("sender"));
			message.setReceiver(document.getString("receiver"));
			message.setContentType(document.getString("contentType"));
			message.setContent(document.getString("content"));
			message.setReceivedDate(document.getString("receivedDate"));	
			
			messages.add(message);
		}
		
		return messages;

	}

}
