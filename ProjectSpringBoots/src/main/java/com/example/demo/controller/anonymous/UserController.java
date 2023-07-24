package com.example.demo.controller.anonymous;

import com.example.demo.config.UploadForm;
import com.example.demo.entity.Order;
import com.example.demo.entity.User;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.dto.OrderInfoDto;
import com.example.demo.model.dto.UserDto;
import com.example.demo.model.mapper.UserMapper;
import com.example.demo.model.request.CreateUserReq;
import com.example.demo.model.request.LoginReq;
import com.example.demo.model.request.UpdateUserReq;
import com.example.demo.repository.OrderRepository;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.security.JwtTokenUtil;
import com.example.demo.service.OrderService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.util.List;

import static com.example.demo.config.Constant.MAX_AGE_COOKIE;



@Controller
public class UserController {

    private static String UPLOAD_DIR = System.getProperty("user.home") + "/upload";

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;
    @PostMapping("/api/register")
    public ResponseEntity<?> register(@Valid @RequestBody CreateUserReq req, HttpServletResponse response) {
        // Create user
        User result = userService.createUser(req);

        // Gen token
        UserDetails principal = new CustomUserDetails(result);
        String token = jwtTokenUtil.generateToken(principal);

        // Add token to cookie to login
        Cookie cookie = new Cookie("JWT_TOKEN",token);
        cookie.setMaxAge(MAX_AGE_COOKIE);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok(UserMapper.toUserDto(result));
    }
    @GetMapping("/file/{filename}")
    public ResponseEntity<?> download(@PathVariable String filename) {
        File file = new File(UPLOAD_DIR + "/" + filename);
        if (!file.exists()) {
            throw new NotFoundException("File not found");
        }

        UrlResource resource;
        try {
            resource = new UrlResource(file.toURI());
        } catch (MalformedURLException e) {
            throw new NotFoundException("File not found");
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(resource);
    }
    @PostMapping("/api/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginReq req, HttpServletResponse response) {
        // Authenticate
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            req.getEmail(),
                            req.getPassword()
                    )
            );

            // Gen token
            String token = jwtTokenUtil.generateToken((CustomUserDetails) authentication.getPrincipal());

            // Add token to cookie to login
            Cookie cookie = new Cookie("JWT_TOKEN", token);
            cookie.setMaxAge(MAX_AGE_COOKIE);
            cookie.setPath("/");
            response.addCookie(cookie);

            return ResponseEntity.ok(UserMapper.toUserDto(((CustomUserDetails) authentication.getPrincipal()).getUser()));
        } catch (Exception ex) {
            throw new BadRequestException("Email hoặc mật khẩu không chính xác");
        }
    }


//    @PostMapping("/api/register")
////    public ResponseEntity<?> register(@Valid @RequestBody CreateUserReq req, HttpServletResponse response) {
////        // Create user
////        User result = userService.createUser(req);
////
////        // Gen token
////        UserDetails principal = new CustomUserDetails(result);
////        String token = jwtTokenUtil.generateToken(principal);
////
////        // Add token to cookie to login
////        Cookie cookie = new Cookie("JWT_TOKEN",token);
////        cookie.setMaxAge(MAX_AGE_COOKIE);
////        cookie.setPath("/");
////        response.addCookie(cookie);
////
////        return ResponseEntity.ok(UserMapper.toUserDto(result));
////    }
////    @PostMapping("/api/login")
//    public ResponseEntity<?> login(@RequestBody CreateUserReq req, HttpServletResponse response) {
//        // Create user
//        UserDto result = userService.createUser(req);
//        System.out.println(result);
//
//        return ResponseEntity.ok("Thành công");
//    }

    @PostMapping("/api/update-profile")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserReq req){
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        user = userService.updateUser(req,user.getEmail());
        return ResponseEntity.ok(123);
    }
    @Autowired
    private OrderRepository orderRepository;
    @GetMapping("/tai-khoan/lich-su-giao-dich")
    public String getOrderHistory(Model model){
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        System.out.println(user);
        List<Order> orders = orderService.getListOrderById(user.getId());
        
//        orders.get(0).get

        model.addAttribute("orders",orders);
        return "shop/order_history";
    }
    private String fileName;
    //    @PostMapping("/upload")
//    public ResponseEntity<?> uploadFile(@ModelAttribute("uploadForm") UploadForm form) {
//        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
//        // Create folder to save file if not exist
//        File uploadDir = new File(UPLOAD_DIR);
//        if (!uploadDir.exists()) {
//            uploadDir.mkdirs();
//        }
//        System.out.println(uploadDir);
//        MultipartFile fileData = form.getFileData();
//        String name = fileData.getOriginalFilename();
//        if (name != null && name.length() > 0) {
//            try {
//                System.out.println(user.getId());
//
//                // Create file
//                File serverFile = new File(UPLOAD_DIR + "/" +name);
//
////                File serverFile = new File(UPLOAD_DIR + "/" + user.getId()+"/"+name);
//                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
//                stream.write(fileData.getBytes());
//                stream.close();
//                fileName = name;
//                User rs = userService.updateAvatar(user,name);
//                System.out.println(rs);
//                return ResponseEntity.ok("/file/"+name);
//            } catch (Exception e) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error when uploading");
//            }
//        }
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");
//    }
    // GET METHOD PAGE MY ACCOUNT
    @GetMapping("/tai-khoan")
    public String getAccount(Model model ) {
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        System.out.println(user.getId());

        File file = new File(UPLOAD_DIR + "/" + user.getAvatar());
        if (!file.exists()) {
            fileName= null;
        }

        UrlResource resource;
        try {
            resource = new UrlResource(file.toURI());
        } catch (MalformedURLException e) {
            throw new NotFoundException("File not found");
        }
        model.addAttribute("fileName",user.getAvatar());
        String rs = resource.toString();
        System.out.println(rs.substring(5,rs.length()-1));
        model.addAttribute("resource","/file/"+user.getAvatar());
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

//        AuthenticateReq req;
//        model.addAttribute("name",userDetails.getUser().getName());
//        model.addAttribute("email",userDetails.getUsername());
//        model.addAttribute("phone",userDetails.getUser().getPhone());
////        model.addAttribute("")
        return "account/account";
    }
    // POST METHOD PAGE UPLOAD AVATAR
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        UploadForm form = new UploadForm();
        form.setFileData(file);
        MultipartFile fileData = form.getFileData();
        String name = fileData.getOriginalFilename();
        if (name != null && name.length() > 0) {
            try {
                System.out.println(user.getId());

                // Create file
                File serverFile = new File(UPLOAD_DIR + "/" +name);

//                File serverFile = new File(UPLOAD_DIR + "/" + user.getId()+"/"+name);
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                stream.write(fileData.getBytes());
                stream.close();
                User rs = userService.updateAvatar(user,name);
                System.out.println(rs);
                return ResponseEntity.ok("/file/"+name);

            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error when uploading");
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");
    }


}