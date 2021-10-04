package com.example.demo.merchant.controller;

import com.example.demo.common.ResponseDTO;
import com.example.demo.merchant.dto.MerchantDTO;
import com.example.demo.merchant.entity.Merchant;
import com.example.demo.merchant.service.MerchantService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;

@Controller
@RestController
@RequestMapping("/merchant")
public class MerchantController {
	
	private final MerchantService merchantService;

	public MerchantController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

	@GetMapping
	String getMerchant(Model model){

		model.addAttribute("something","Merchant Management");
		model.addAttribute("merchant", Arrays.asList(
				new Merchant("1",   "Ashik",   "01"),
				new Merchant("2",   "Mahmud",  "02")
		));
		return "merchant1";
	}

	@PostMapping("/addMerchant")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String addMerchant(@RequestBody Merchant model) {
		if(model == null) {
			return null;
		}
		try {

			merchantService.add(model);
			return "Save Successfully";
		}catch (Exception e) {
			e.printStackTrace();
			return "Save Failed. "+e.getMessage();
		}
		
	}
	
	@PostMapping("/getMerchantById")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDTO getMerchant(@RequestBody MerchantDTO model) {
		
		if(model == null) {
			return null;
		}
		
		ResponseDTO responseDTO = new ResponseDTO();
		
		try {
			MerchantDTO merchantDTO = merchantService.getMerchantByKey(merchantService.convertToEntity(model));
			responseDTO.setSuccess(true);
			responseDTO.setData(merchantDTO);
			return responseDTO;
		}catch (Exception e) {
			e.printStackTrace();
			responseDTO.setSuccess(false);
			responseDTO.setMessage(e.getMessage());
			return responseDTO;
		}
		
	}
	
	@PostMapping("/modifyMerchant")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDTO modifyMerchant(@RequestBody MerchantDTO model) {
		
		if(model == null) {
			return null;
		}
		
		ResponseDTO responseDTO = new ResponseDTO();
		
		try {
			MerchantDTO merchantDTO = merchantService.modifyMerchant(model);
			responseDTO.setSuccess(true);
			responseDTO.setData(merchantDTO);
			return responseDTO;
		}catch (Exception e) {
			e.printStackTrace();
			responseDTO.setSuccess(false);
			responseDTO.setMessage(e.getMessage());
			return responseDTO;
		}
		
	}
	
	@PostMapping("/deleteMerchantById")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDTO deleteMerchantById(@RequestBody MerchantDTO model) {
		
		if(model == null) {
			return null;
		}
		
		ResponseDTO responseDTO = new ResponseDTO();
		
		try {
			MerchantDTO merchantDTO = merchantService.deleteMerchantById(model);
			responseDTO.setSuccess(true);
			responseDTO.setData(merchantDTO);
			return responseDTO;
		}catch (Exception e) {
			e.printStackTrace();
			responseDTO.setSuccess(false);
			responseDTO.setMessage(e.getMessage());
			return responseDTO;
		}
	}
	
	@PostMapping("/getAllMerchant")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDTO getAllMerchant() {
		
		ResponseDTO responseDTO = new ResponseDTO();
		
		try {
			List<Merchant> merchantDTOList = merchantService.getAllMerchant();
			responseDTO.setSuccess(true);
			responseDTO.setData(merchantDTOList);
			return responseDTO;
		}catch (Exception e) {
			e.printStackTrace();
			responseDTO.setSuccess(false);
			responseDTO.setMessage(e.getMessage());
			return responseDTO;
		}
		
	}
	
	@PostMapping("/addMerchantUsingWebFlux")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mono<Boolean> addMerchantUsingWebFlux(@RequestBody Merchant model) {
		
		Mono<Boolean> merchantDTOList = merchantService.addMerchantUsingWebFlux(model);
		return merchantDTOList;

		
	}

	@PostMapping("/modMerchantUsingWebFlux")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mono<Boolean> modMerchantUsingWebFlux(@Valid @RequestBody Merchant model) {
		
		Mono<Boolean> modResult = merchantService.modMerchantUsingWebFlux(model);
		return modResult;

		
	}
	
	@PostMapping("/deleteMerchantUsingWebFlux")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mono<Boolean> deleteMerchantUsingWebFlux(@RequestBody Merchant model) {
		
		Mono<Boolean> merchantDTOlist = merchantService.deleteMerchantUsingWebFlux(model);
		return merchantDTOlist;

		
	}
	
	@PostMapping("/getMerchantByIdUsingWebFlux")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Mono<Merchant> getMerchantByIdUsingWebFlux(@Valid @RequestBody Merchant model){
		
		Mono<Merchant> merchantDTOlist = merchantService.getMerchantByIdUsingWebFlux(model);
		return merchantDTOlist;
		
	}
	
	@PostMapping("/getAllMerchantByIdUsingWebFlux")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Flux<Merchant> getAllMerchantByIdUsingWebFlux(){
		
		Flux<Merchant> merchantDTOlist = merchantService.getAllMerchantByIdUsingWebFlux();
		return merchantDTOlist;
		
	}
}
