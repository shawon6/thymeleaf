package com.example.demo.merchant.service;

import com.example.demo.merchant.dto.MerchantDTO;
import com.example.demo.merchant.entity.Merchent;
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
	
	public void add(MerchantDTO model) throws Exception {
		if (model == null ) {
			throw new Exception("Model can't be Null");
		}
		
		Merchent m = convertToEntity(model);
//		MerchantDTO checkDTO = getMerchantByKey(m);
//		if (checkDTO!= null)
//		if(checkDTO.getId()!=null) {
//			throw new Exception("Already Exists");
//		}
		
		merchantReporsitory.save(m);
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

	public MerchantDTO getMerchantByKey(Merchent merchent) {
		Merchent m = merchantReporsitory.findByKey(merchent);
		MerchantDTO merchantDTO = new MerchantDTO();
		if(m!= null)
			 merchantDTO = convertToDTO(m);
		return merchantDTO;
	}

	public List<MerchantDTO> getAllMerchant() {
		List<MerchantDTO> merchantDTOlist = ConvertToDtoList(merchantReporsitory.getUsingQuery());
		return merchantDTOlist;
	}
	
	public Merchent convertToEntity(MerchantDTO model) {
		Merchent merchent = modelMapper.map(model, Merchent.class);
		return merchent;
	}

	private MerchantDTO convertToDTO(Merchent merchent) {
		MerchantDTO dto = modelMapper.map(merchent, MerchantDTO.class);
		return dto;
	}


	private List<MerchantDTO> ConvertToDtoList(List<Merchent> mrchentList) {
		List<MerchantDTO> MerchantDTOList = mrchentList.stream().map(entity -> convertToDTO(entity)).collect(Collectors.toList());
		return MerchantDTOList;
	}
	
	public Mono<Boolean> addMerchantUsingWebFlux(Merchent model) {
        return merchantReporsitory.addMerchantUsingWebFlux(model);
	}

	
	public Mono<Boolean> deleteMerchantUsingWebFlux(Merchent model) {
        return merchantReporsitory.deleteMerchantUsingWebFlux(model);
	}



	public Mono<Merchent> getMerchantByIdUsingWebFlux(Merchent model) {
		
		return merchantReporsitory.getMerchantByIdUsingWebFlux(model);
	}



	public Flux<Merchent> getAllMerchantByIdUsingWebFlux() {
		return merchantReporsitory.getAllMerchantByIdUsingWebFlux();
	}



	public Mono<Boolean> modMerchantUsingWebFlux( Merchent model) {
		if(ObjectUtils.isEmpty(model.getNumber())){
			return Mono.just(false);
		}

		return merchantReporsitory.getMerchantByIdUsingWebFlux(model).flatMap(merchent -> {
			log.info(merchent.toString());
			merchent.setNumber(model.getNumber());
			return merchantReporsitory.addMerchantUsingWebFlux(merchent);
		});
	}
	
}
