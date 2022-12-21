package com.example.springsecuritywithjwt.controller;
import com.example.springsecuritywithjwt.service.UserDetailsServiceImpl;
import com.example.springsecuritywithjwt.model.AuthenticateRequest;
import com.example.springsecuritywithjwt.model.Users;
import com.example.springsecuritywithjwt.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
//@RequestMapping(value = "/")
public class ResourceController {

@Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UserDetailsServiceImpl userDetailsService;


    @GetMapping("/user")
    public String getUser()
    {
        return "<h1>Hi user Accessible</h2>>";
    }

    @GetMapping( "/admin")
    public String getAdmin()
    {
        return "<h1>Hi Admin Accessible</h2>>";
    }


    @GetMapping("/")
    public String home() {
        return ("<h1>Welcome</h1>");
    }
    @PostMapping("/saveUser")
    public ResponseEntity<Users> saveUser(@RequestBody Users users)
    {
        Users user=userDetailsService.saveUser(users);

        if(!ObjectUtils.isEmpty(users))
        return new ResponseEntity<>(user,HttpStatus.CREATED);
        else
            return new ResponseEntity<>(user,HttpStatus.NOT_FOUND);

    }


    /**
     * to issue jwt token for valid user , then that token will come in every subsequent request ...until token is expire
     * @param authenticateRequest
     * @return
     */
    @PostMapping("/Authenticate")
    public ResponseEntity<?>  AuthenticateAndIssueJwtToken(@RequestBody AuthenticateRequest authenticateRequest) throws Exception
    {
        try {

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticateRequest.getUserName(), authenticateRequest.getPassword()));
        }
        catch (BadCredentialsException ex)
        {
            log.error("user not registered "+ " "+ex.getMessage());
            throw new Exception("incorrect user name and passwor");
        }
        UserDetails userDetails= userDetailsService.loadUserByUsername(authenticateRequest.getUserName());

        if(!ObjectUtils.isEmpty(userDetails))
        return new ResponseEntity<>(jwtUtil.generateToken(userDetails),HttpStatus.CREATED);
        else
            return  new ResponseEntity<>("not found ",HttpStatus.NOT_FOUND);
    }
}
