package com.dlt.repository;

import com.dlt.model.Fund;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FundRepository extends MongoRepository<Fund, String> {
}
