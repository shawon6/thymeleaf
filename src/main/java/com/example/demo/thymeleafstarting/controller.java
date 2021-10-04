package com.example.demo.thymeleafstarting;

import com.example.demo.merchant.entity.Merchant;
import com.example.demo.merchant.service.MerchantService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class controller {

    private final MerchantService merchantService;

    public controller(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @GetMapping("/")
    String getMerchant(Model model){
        List<Merchant> merchant = merchantService.getAllMerchant();
        model.addAttribute("merchant",merchant);
        return "merchant";
    }

    @GetMapping("/newMerchantForm")
    String getMerchantFrom(Model model){

        Merchant merchant = new Merchant("2",   "Ashik",   "01");
        model.addAttribute("merchant", merchant);
        return "newMerchant";
    }

    @PostMapping("/addMerchant")
    String addMerchant(@ModelAttribute("merchant") Merchant merchant) throws Exception {

        merchantService.add(merchant);

        return "redirect:/";
    }
}
