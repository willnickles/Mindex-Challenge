package com.mindex.challenge.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mindex.challenge.data.Compensation;

@Repository
public interface CompensationRepository extends MongoRepository<Compensation, String> {
	Compensation save(Compensation compensation);
	Compensation findByEmployeeId(String employeeId);
}