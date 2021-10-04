package com.example.demo.merchant.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.demo.merchant.entity.Merchent;
import com.example.demo.merchant.exception.DynamoDbException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Repository
public class MerchantRepositoryDynamoDB {

	public static final String DYNAMO_ERROR = "DYNAMO_ERROR";
	
	@Autowired
	private DynamoDBMapper dynamoDBMapper;
	
	public void save(Merchent merchent) {
		dynamoDBMapper.save(merchent);
	} 
	
	public Merchent findByKey(Merchent merchent) {
		return dynamoDBMapper.load(Merchent.class, merchent.getId(), merchent.getName());
	} 
	
	public Merchent delete(Merchent model) {
		Merchent m = findByKey(model);
		dynamoDBMapper.delete(m);
		return m;
	} 
	
	public List<Merchent> getUsingQuery () {
		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":v1", new AttributeValue().withS("1"));
		//eav.put(":v2", new AttributeValue().withS("01948"));

		DynamoDBQueryExpression<Merchent> queryExpression = new DynamoDBQueryExpression<Merchent>()
		    .withKeyConditionExpression("id = :v1")
		    //.withFilterExpression("number = :v2")
		    .withExpressionAttributeValues(eav);

		List<Merchent> merchentList = dynamoDBMapper.query(Merchent.class, queryExpression);
		return merchentList;
	}

	public Mono<Boolean> addMerchantUsingWebFlux(Merchent model) {
		try {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> dynamoDBMapper.save(model));

            return Mono.fromCompletionStage(future)
                    .then(Mono.just(true))
                    .onErrorMap(throwable -> new DynamoDbException(DYNAMO_ERROR, throwable));
        } catch (Exception ex) {
        	ex.printStackTrace();
            return Mono.error(ex);
        }
	}
	
	public Mono<Boolean> deleteMerchantUsingWebFlux(Merchent model) {
		try {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> dynamoDBMapper.delete(model));

            return Mono.fromCompletionStage(future)
                    .then(Mono.just(true))
                    .onErrorMap(throwable -> new DynamoDbException(DYNAMO_ERROR, throwable));
        } catch (Exception ex) {
        	ex.printStackTrace();
            return Mono.error(ex);
        }
	}
	
	public  Mono<Merchent> getMerchantByIdUsingWebFlux(Merchent model) {
        try {
            CompletableFuture<Merchent> future =
                    CompletableFuture.supplyAsync(() -> dynamoDBMapper.load(Merchent.class, model.getId(), model.getName()));

            return Mono.fromCompletionStage(future)
                    .onErrorMap(throwable -> new DynamoDbException(DYNAMO_ERROR, throwable));
        } catch (Exception ex) {
        	//Log.error("MerchantRepositoryDynamoDB - getMerchantByIdUsingWebFlux - onException:{} " , ex);
            return Mono.error(ex);
        }
    }

	public Flux<Merchent> getAllMerchantByIdUsingWebFlux() {
		 try {
	            CompletableFuture<List<Merchent>> future =
	                    CompletableFuture.supplyAsync(() -> dynamoDBMapper.scan(Merchent.class, new DynamoDBScanExpression()));

	            return Mono.fromCompletionStage(future)
	                    .flatMapMany(item -> Flux.fromStream(item.stream()))
	                    .onErrorMap(throwable -> new DynamoDbException(DYNAMO_ERROR, throwable));
	        } catch (Exception ex) {
	            return Flux.error(ex);
	        }
	}
}
