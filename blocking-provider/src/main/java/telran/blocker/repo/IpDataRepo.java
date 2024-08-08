package telran.blocker.repo;

import java.util.List;

import java.util.Set;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import telran.blocker.model.IpDataDoc;



public interface IpDataRepo extends MongoRepository<IpDataDoc, String> {
	

}