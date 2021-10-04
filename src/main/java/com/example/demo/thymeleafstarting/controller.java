package com.example.demo.thymeleafstarting;

import com.example.demo.merchant.entity.Merchent;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;

@Controller
public class controller {

    @GetMapping
    String getMerchant(Model model){

        model.addAttribute("something","Merchant Management");
        model.addAttribute("merchant", Arrays.asList(
            new Merchent("1",   "Ashik",   "01"),
            new Merchent("2",   "Mahmud",  "02")
        ));
        return "merchant";
    }
}
