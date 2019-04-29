package top.sunjiubo.springboot.Nsblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import top.sunjiubo.springboot.Nsblog.model.Catalog;
import top.sunjiubo.springboot.Nsblog.model.User;
import top.sunjiubo.springboot.Nsblog.service.CatalogService;
import top.sunjiubo.springboot.Nsblog.service.impl.CatalogServiceImpl;
import top.sunjiubo.springboot.Nsblog.util.ConstraintViolationExceptionHandler;
import top.sunjiubo.springboot.Nsblog.vo.CatalogVO;
import top.sunjiubo.springboot.Nsblog.vo.Response;

import javax.jws.WebParam;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Controller
@RequestMapping("/catalogs")
public class CatalogController {

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private UserDetailsService userDetailsService;

    @GetMapping
    public String listCommments(@RequestParam(value = "username",required = true) String username,
                                Model model){
        User user = (User)userDetailsService.loadUserByUsername(username);
        List<Catalog> catalogs = catalogService.listCatalogs(user);

        boolean isOwner = false;
        if(SecurityContextHolder.getContext().getAuthentication()!=null &&
        SecurityContextHolder.getContext().getAuthentication().isAuthenticated()&&
        !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")){
            User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(principal!=null&&user.getUsername().equals(principal.getUsername())){
                isOwner = true;
            }
        }

        model.addAttribute("isCatalogsOwner",isOwner);
        model.addAttribute("catalogs",catalogs);
        return "/userspace/u :: #catalogRepleace";
    }

    /**
     * 发表分类
     * @param catalogVO
     * @return
     */
    @PostMapping
    @PreAuthorize("authentication.name.equals(#catalogVO.username)")
    public ResponseEntity<Response> create(@RequestBody CatalogVO catalogVO){
        String username = catalogVO.getUsername();
        Catalog catalog = catalogVO.getCatalog();

        User user = (User)userDetailsService.loadUserByUsername(username);

        try{
            catalog.setUser(user);
            catalogService.saveCatalog(catalog);
        }catch (ConstraintViolationException e){
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
            }catch (Exception e){
            return  ResponseEntity.ok().body(new Response(false,e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true,"处理成功",null));
    }

    /**
     * 删除分类
     * @param username
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> delete(String username,
                                           @PathVariable("id") Long id){
        try {
            catalogService.removeCatalog(id);
        }catch (ConstraintViolationException e){
            return ResponseEntity.ok().body(new Response(false,ConstraintViolationExceptionHandler.getMessage(e)));
        }catch (Exception e){
            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
        }

        return ResponseEntity.ok().body(new Response(true,"处理成功",null));
    }

    /**
     * 获取分类编辑界面
     * @param model
     * @return
     */
    @GetMapping("/edit")
    public String getCatalogEdit(Model model){
        Catalog catalog = new Catalog(null,null);
        model.addAttribute(catalog);
        return "/userspace/catalogedit";
    }

    /**
     * 根据id获取分类信息
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/edit/{id}")
    public String getCatalogById(@PathVariable("id") Long id, Model model){
        Catalog catalog = catalogService.getCatalogById(id);
        model.addAttribute(catalog);
        return "/userspace/catalogedit";
    }

}
