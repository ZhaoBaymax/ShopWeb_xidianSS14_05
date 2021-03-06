package com.project_management.shoppingweb.controller;

import java.util.*;

import com.project_management.shoppingweb.domain.ProductAdvertisement;
import com.project_management.shoppingweb.domain.SellerAdvertisement;
import com.project_management.shoppingweb.service.User.User_ProductAdvertisementService;
import com.project_management.shoppingweb.service.User.User_SellerAdvertisementService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomepageController {


    @Autowired
    private User_SellerAdvertisementService userSellerAdvertisementService;
    @Autowired
    private User_ProductAdvertisementService userProductAdvertisementService;

    private Logger logger = Logger.getLogger(this.getClass());

    @RequestMapping("/User/Main")
    public String userMain(@ModelAttribute("UserID")long userId,Model model)
    {
        //登录逻辑处理
        return "redirect:/homepage";
    }

    @RequestMapping(value = "/homepage")
    public String homepage(Model model){
        List<SellerAdvertisement> seller_ads = userSellerAdvertisementService.findAllByStatus(0);
        boolean seller_ads_isnull = false;
        if(seller_ads == null || seller_ads.isEmpty())  seller_ads_isnull=true;
        model.addAttribute("shop_ads", seller_ads);//返回商铺广告的list，前端解析
        model.addAttribute("seller_ad_isnull", seller_ads_isnull);

        List<ProductAdvertisement> pro_ads_roll = userProductAdvertisementService.findAllByType(1);
        boolean pro_rollad_isnull = false;
        if(pro_ads_roll == null || pro_ads_roll.isEmpty())  pro_rollad_isnull=true;//如果为空设为true
        model.addAttribute("pro_ads_roll",pro_ads_roll);//动态轮转商品广告
        model.addAttribute("pro_rollad_isnull", pro_rollad_isnull);

        List<ProductAdvertisement> pro_ads_list = userProductAdvertisementService.findAllByType(2);
        boolean pro_listad_isnull = false;
        if(pro_ads_list == null || pro_ads_list.isEmpty())  pro_listad_isnull=true;//如果为空设为true
        model.addAttribute("pro_ads", pro_ads_list);//商品广告
        model.addAttribute("pro_listad_isnull", pro_listad_isnull);
        return "/Homepage/homepage";
    }
}
