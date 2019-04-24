package top.sunjiubo.springboot.Nsblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Blog控制器
 */
@Controller
@RequestMapping("/blogs")
public class BlogController {

    @GetMapping
    public String listBlogs(@RequestParam(value = "order",required = false,defaultValue ="new" ) String order,//排序，默认按照最新
                            @RequestParam(value = "keyword",required = false,defaultValue = "") String keyword){//关键字
        System.out.println("order:"+order+";keyword:"+keyword);
        return "redirect:/index?order="+order+"&keyword="+keyword;
    }


}
