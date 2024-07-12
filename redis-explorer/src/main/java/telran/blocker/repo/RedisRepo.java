package telran.blocker.repo;

import org.springframework.data.repository.CrudRepository;

import telran.blocker.model.RedisModel;

public interface RedisRepo extends CrudRepository<RedisModel, String> {

}