package com.example.demo.merchant.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.demo.merchant.entity.Merchant;
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
	
	public void save(Merchant merchant) {
		dynamoDBMapper.save(merchant);
	} 
	
	public Merchant findByKey(Merchant merchant) {
		return dynamoDBMapper.load(Merchant.class, merchant.getId(), merchant.getName());
	} 
	
	public Merchant delete(Merchant model) {
		Merchant m = findByKey(model);
		dynamoDBMapper.delete(m);
		return m;
	} 
	
	public List<Merchant> getUsingQuery () {
		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":v1", new AttributeValue().withS("1"));
		//eav.put(":v2", new AttributeValue().withS("01948"));

		DynamoDBQueryExpression<Merchant> queryExpression = new DynamoDBQueryExpression<Merchant>()
		    .withKeyConditionExpression("id = :v1")
		    //.withFilterExpression("number = :v2")
		    .withExpressionAttributeValues(eav);

		List<Merchant> merchantList = dynamoDBMapper.query(Merchant.class, queryExpression);
		return merchantList;
	}

	public Mono<Boolean> addMerchantUsingWebFlux(Merchant model) {
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
	
	public Mono<Boolean> deleteMerchantUsingWebFlux(Merchant model) {
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
	
	public  Mono<Merchant> getMerchantByIdUsingWebFlux(Merchant model) {
        try {
            CompletableFuture<Merchant> future =
                    CompletableFuture.supplyAsync(() -> dynamoDBMapper.load(Merchant.class, model.getId(), model.getName()));

            return Mono.fromCompletionStage(future)
                    .onErrorMap(throwable -> new DynamoDbException(DYNAMO_ERROR, throwable));
        } catch (Exception ex) {
        	//Log.error("MerchantRepositoryDynamoDB - getMerchantByIdUsingWebFlux - onException:{} " , ex);
            return Mono.error(ex);
        }
    }

	public Flux<Merchant> getAllMerchantByIdUsingWebFlux() {
		 try {
	            CompletableFuture<List<Merchant>> future =
	                    CompletableFuture.supplyAsync(() -> dynamoDBMapper.scan(Merchant.class, new DynamoDBScanExpression()));

	            return Mono.fromCompletionStage(future)
	                    .flatMapMany(item -> Flux.fromStream(item.stream()))
	                    .onErrorMap(throwable -> new DynamoDbException(DYNAMO_ERROR, throwable));
	        } catch (Exception ex) {
	            return Flux.error(ex);
	        }
	}
}
