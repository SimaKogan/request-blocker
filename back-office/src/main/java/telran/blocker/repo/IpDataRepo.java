package telran.blocker.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import telran.blocker.model.IpDataDoc;

public interface IpDataRepo extends MongoRepository<IpDataDoc, String> {

	
}
