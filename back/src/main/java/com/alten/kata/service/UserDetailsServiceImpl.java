package com.alten.kata.service;

import com.alten.kata.config.JwtUtil;
import com.alten.kata.dto.RegisterDTO;
import com.alten.kata.entity.Role;
import com.alten.kata.entity.User;
import com.alten.kata.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var userDB= userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));


        return org.springframework.security.core.userdetails.User
                .withUsername( userDB.getUsername())
                .password(userDB.getPassword())
                .roles(getRoleDisplayNames(userDB.getAuthorities())).build();
    }


    public Object save(RegisterDTO registerDTO) {
        var user = new User();
        var roles = new ArrayList<Role>();

        roles.add(Role.ROLE_ADMIN);
        user.setAuthorities(roles);
        user.setFirstname(registerDTO.getFirstname());
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(registerDTO.getPassword())); // Hash the password
         var userCreated= userRepository.save(user);

        var generateToken = this.jwtUtil.generateToken(userCreated);
        var response = new HashMap<String,Object>();
        response.put("token",generateToken);
        response.put("User",userCreated);
        return response;




    }


    public static String[] getRoleDisplayNames( List<Role> authorities) {
        List<Role> rValues = authorities;
        var roleNames = new String[rValues.size()];

        for (int i = 0; i < rValues.size(); i++) {
            roleNames[i] = rValues.get(i).name();
        }

        return roleNames;
    }

}