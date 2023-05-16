package com.lkm.menu.control;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Linkm on 2022/5/15.
 */
@Controller
@RequestMapping("/page")
public class PageController {

    @RequestMapping("/")
    public String page(){
        return "menu";
    }

    @RequestMapping("/siyuan")
    public String page2(){
        return "menu_siyuan";
    }

    @RequestMapping("/config")
    public String page3(){
        return "menu_config";
    }

    @RequestMapping("/console")
    public String console(){
        return "console";
    }

    @RequestMapping("/consoleUser")
    public String consoleUser(){
        return "consoleUser";
    }

    @RequestMapping("/daka")
    public String daka(){
        return "daka";
    }

}
