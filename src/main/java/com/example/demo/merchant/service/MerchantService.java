package com.example.demo.merchant.service;

import com.example.demo.merchant.dto.MerchantDTO;
import com.example.demo.merchant.entity.Merchant;
import com.example.demo.merchant.repository.MerchantRepositoryDynamoDB;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MerchantService {
	
	private final MerchantRepositoryDynamoDB merchantReporsitory;

    public MerchantService(MerchantRepositoryDynamoDB merchantReporsitory) {
        this.merchantReporsitory = merchantReporsitory;
    }

	@Autowired
	private ModelMapper modelMapper;
	
//	@Autowired
//	private MerchantRepoReactive merchantRepoReactive;
	
	public void add(Merchant model) throws Exception {
		if (model == null ) {
			throw new Exception("Model can't be Null");
		}
		merchantReporsitory.save(model);
	}

	
	
	public MerchantDTO modifyMerchant(MerchantDTO model) throws Exception {
		
		if (model == null ) {
			throw new Exception("Model can't be Null");
		}
		
		MerchantDTO checkDTO = getMerchantByKey(convertToEntity(model));
		
		if(checkDTO.getId() != null) {
			throw new Exception("No data found to update");
		}
		
		merchantReporsitory.save(convertToEntity(model));
		
		return model;
	}


	public MerchantDTO deleteMerchantById(MerchantDTO model) throws Exception {
		if (model == null ) {
			throw new Exception("Model can't be Null");
		}
		
		MerchantDTO checkDTO = getMerchantByKey(convertToEntity(model));
		
		if(checkDTO.getId() == "") {
			throw new Exception("No data found to delete");
		}
		
		merchantReporsitory.delete(convertToEntity(model));
		return model;
	}

	public MerchantDTO getMerchantByKey(Merchant merchant) {
		Merchant m = merchantReporsitory.findByKey(merchant);
		MerchantDTO merchantDTO = new MerchantDTO();
		if(m!= null)
			 merchantDTO = convertToDTO(m);
		return merchantDTO;
	}

	public List<Merchant> getAllMerchant() {
		List<Merchant> merchantDTOlist = merchantReporsitory.getUsingQuery();
		return merchantDTOlist;
	}
	
	public Merchant convertToEntity(MerchantDTO model) {
		Merchant merchant = modelMapper.map(model, Merchant.class);
		return merchant;
	}

	private MerchantDTO convertToDTO(Merchant merchant) {
		MerchantDTO dto = modelMapper.map(merchant, MerchantDTO.class);
		return dto;
	}


	private List<MerchantDTO> ConvertToDtoList(List<Merchant> mrchentList) {
		List<MerchantDTO> MerchantDTOList = mrchentList.stream().map(entity -> convertToDTO(entity)).collect(Collectors.toList());
		return MerchantDTOList;
	}
	
	public Mono<Boolean> addMerchantUsingWebFlux(Merchant model) {
        return merchantReporsitory.addMerchantUsingWebFlux(model);
	}

	
	public Mono<Boolean> deleteMerchantUsingWebFlux(Merchant model) {
        return merchantReporsitory.deleteMerchantUsingWebFlux(model);
	}



	public Mono<Merchant> getMerchantByIdUsingWebFlux(Merchant model) {
		
		return merchantReporsitory.getMerchantByIdUsingWebFlux(model);
	}



	public Flux<Merchant> getAllMerchantByIdUsingWebFlux() {
		return merchantReporsitory.getAllMerchantByIdUsingWebFlux();
	}



	public Mono<Boolean> modMerchantUsingWebFlux( Merchant model) {
		if(ObjectUtils.isEmpty(model.getNumber())){
			return Mono.just(false);
		}

		return merchantReporsitory.getMerchantByIdUsingWebFlux(model).flatMap(merchant -> {
			log.info(merchant.toString());
			merchant.setNumber(model.getNumber());
			return merchantReporsitory.addMerchantUsingWebFlux(merchant);
		});
	}
	
}
