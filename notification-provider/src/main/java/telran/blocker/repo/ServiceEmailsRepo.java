package telran.blocker.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import telran.blocker.model.ServiceEmailsDoc;

public interface ServiceEmailsRepo extends MongoRepository<ServiceEmailsDoc, String> {

}