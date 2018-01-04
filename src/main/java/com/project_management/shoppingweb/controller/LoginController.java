package com.project_management.shoppingweb.controller;

import com.project_management.shoppingweb.service.Seller.Seller_SellerService;
import com.project_management.shoppingweb.service.User.User_LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

/**
 * Created by sightmaple on 2017/12/10.
 */
@Controller
public class LoginController {

    @Autowired
    private User_LoginService userLoginService;

    @Autowired
    private Seller_SellerService seller_sellerService;

    @RequestMapping(value = "/")
    public String main(){

        return "redirect:/homepage";

    }



    //商家登录
    @RequestMapping(value = "/login")
    public String login(HttpServletRequest httpServletRequest, RedirectAttributes attributes) {
        String account = httpServletRequest.getParameter("account_login");
        String password = httpServletRequest.getParameter("password_login");
        String pattern = "[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+";
        long id = -1;


        //有bug,用户名和邮箱一样呢

        if (Pattern.matches(pattern, account)) {
            //以邮箱方式登录
            id = userLoginService.sellerLoginByEmail(account,password);
        } else {
            //用户名方式登录
            id = userLoginService.sellerLoginByUsername(account, password);
        }

        if(id < 0)
        {
            attributes.addAttribute("errorMessage",getErrorMessage(id));
           return "redirect:/error/errorHandler";
        }

        switch (seller_sellerService.getSellerById(id).getApplyState()) // 1 - 通过， 2 - 未通过, 3-拉黑
        {
            case 2:
                attributes.addAttribute("errorMessage","your apply not be passed, try login later!");
                return "redirect:/error/errorHandler";
            case 3:
                attributes.addAttribute("errorMessage","your account is in the black list!");
                return "redirect:/error/errorHandler";
            default:
                break;
        }

        attributes.addAttribute("SellerID",id);
        return "redirect:/Seller/Main";
    }


    //用户登录
    @RequestMapping(value = "/userlogin")
    public String userLogin(HttpServletRequest httpServletRequest, RedirectAttributes attributes) {
        String account = httpServletRequest.getParameter("account_login");
        String password = httpServletRequest.getParameter("password_login");
        String pattern = "[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+";
        long id = -1;
        //TODO:有bug,用户名和邮箱一样呢

        //以邮箱方式登录
        if (Pattern.matches(pattern, account)) {
            id = userLoginService.userLoginByEmail(account,password);
        } else {
            //用户名方式登录
            id = userLoginService.userLoginByUsername(account, password);
        }

        if(id < 0)
        {
            attributes.addAttribute("errorMessage",getErrorMessage(id));
            return "redirect:/error/errorHandler";
        }

        // 1 - 可用 ，2 - 黑名单 0 - 被删除
        if(seller_sellerService.getUser(id).getState() == 2)
        {
            attributes.addAttribute("errorMessage","Sorry,you are in the black list!");
            return "redirect:/error/errorHandler";
        }

        if(seller_sellerService.getUser(id).getState() == 0)
        {
            attributes.addAttribute("errorMessage","Sorry,your count has been delete by admin!");
            return "redirect:/error/errorHandler";
        }

        attributes.addAttribute("UserID",id);
        return "redirect:/User/Main";
    }

    String getErrorMessage(long id)
    {
        switch ((int)id)
        {
            case -1:
                return  "email or username not exist!";
            case -2:
                return  "password is wrong!";
            default:
                return "there are something wrong!";
        }
    }
}



