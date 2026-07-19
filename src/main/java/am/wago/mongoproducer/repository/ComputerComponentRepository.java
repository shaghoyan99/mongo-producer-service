package am.wago.mongoproducer.repository;

import am.wago.mongoproducer.model.ComputerComponent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComputerComponentRepository extends MongoRepository<ComputerComponent, String> {
}
