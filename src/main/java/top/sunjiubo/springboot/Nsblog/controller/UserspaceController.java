package top.sunjiubo.springboot.Nsblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import top.sunjiubo.springboot.Nsblog.model.Blog;
import top.sunjiubo.springboot.Nsblog.model.User;
import top.sunjiubo.springboot.Nsblog.service.BlogService;
import top.sunjiubo.springboot.Nsblog.service.UserService;
import top.sunjiubo.springboot.Nsblog.vo.Response;

import javax.persistence.ConstructorResult;

@Controller
@RequestMapping("/u")
public class UserspaceController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private BlogService blogService;

    @Value("${file.server.url}")
    private String fileServerUrl;

    @GetMapping("/{username}")
    public String userSpace(@PathVariable("username") String username, Model model){
        User user = (User)userDetailsService.loadUserByUsername(username);
        model.addAttribute(user);
        return "redirect:/u/"+username+"/blogs";
    }

    /**
     * 获取个人设置
     * @param username
     * @param model
     * @return
     */
    @GetMapping("/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView profile(@PathVariable("username") String username,Model model){
        User user = (User)userDetailsService.loadUserByUsername(username);
        model.addAttribute("user",user);
        model.addAttribute("fileServerUrl",fileServerUrl);
        return new ModelAndView("/userspace/profile","userModel",model);
    }

    /**
     * 保存个人设置
     * @param username
     * @param user
     * @return
     */
    @PostMapping("/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")
    public String saveProfile(@PathVariable("username") String username,User user){
        User originaUser = userService.getUserById(user.getId());
        originaUser.setEmail(user.getEmail());
        originaUser.setName(user.getName());

        String rawPassword = originaUser.getPassword();
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String encoderPassword = encoder.encode(user.getPassword());
        boolean isMatch = encoder.matches(rawPassword,encoderPassword);
        if(!isMatch){
            originaUser.setPassword(user.getPassword());
        }

        userService.saveorUpdateUser(originaUser);
        return "redirect:/u/" + username + "/profile";
    }

    /**
     * 获取编辑头像的界面
     * @param username
     * @param model
     * @return
     */
    @GetMapping("/{username}/avatar")
    @PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView avatar(@PathVariable("username") String username, Model model) {
        User  user = (User)userDetailsService.loadUserByUsername(username);
        model.addAttribute("user", user);
        return new ModelAndView("/userspace/avatar", "userModel", model);
    }


    /**
     *
     * @param username
     * @param user
     * @return
     */
    @PostMapping("/{username}/avatar")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> saveAvatar(
            @PathVariable("username") String username,
            @RequestBody User user) {
        String avatarUrl = user.getAvatar();

        User originalUser = userService.getUserById(user.getId());
        originalUser.setAvatar(avatarUrl);
        userService.saveorUpdateUser(originalUser);

        return ResponseEntity.ok().body(new Response(true, "处理成功", avatarUrl));
    }

    @GetMapping("/{username}/blogs")
    public String listBlogsByOrder(@PathVariable("username") String username,
                                   @RequestParam(value = "order",required = false,defaultValue = "new") String order,
                                   @RequestParam(value = "category",required = false) String category,
                                   @RequestParam(value = "keyword",required = false)String keyword){
        if(category!=null){
            System.out.println("category:"+category);
            System.out.println("selflink:"+"redirect:/u/"+username+"/blogs?category="+category);
            return "/u";
        }else if(keyword!=null&&keyword.isEmpty()==false){
            System.out.println("keyword:"+keyword);
            System.out.println("selflink:"+"redirect:/u/"+username+"/blogs?keyword="+keyword);
            return "/u";
        }

        System.out.println("order:"+order);
        System.out.println("selflink:"+"redirect:/u/"+username+"/blogs?order="+order);
        return "/u";
    }

    @GetMapping("/{username}/blogs/{id}")
    public String getBlogById(@PathVariable("username") String username,
                              @PathVariable("id") Long id,
                              Model model){
        User principal = null;
        Blog blog = blogService.getBlogById(id);

        //增加阅读量
        blogService.readingIncrease(id);

        //判断操作用户是不是博客所有者
        boolean isBlogOwner = false;
        if(SecurityContextHolder.getContext().getAuthentication()!=null&&SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                &&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")){
            principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal !=null && username.equals(principal.getUsername())) {
                isBlogOwner = true;
            }
        }

        model.addAttribute("isBlogOwner",isBlogOwner);
        model.addAttribute("blogModel",blog);

        return "/userspace/blogs";
    }

    @DeleteMapping("/{username}/blogs/{id}")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> deleteBlog(@PathVariable("username") String username,
                                               @PathVariable("id") Long id){
        try {
            blogService.removeBlog(id);
        }catch (Exception e){
            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
        }
        String redirectUrl = "/u/"+username+"/blogs";
        return ResponseEntity.ok().body(new Response(true,"处理成功",redirectUrl));
    }

    @GetMapping("/{username}/blogs/edit")
    public String editBlogs(){
        return "/userspace/blogedit";
    }
}
