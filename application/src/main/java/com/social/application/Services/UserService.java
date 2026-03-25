package com.social.application.Services;

import com.social.application.DTOs.UserDTO;
import com.social.application.DTOs.UserRequestDTO;
import com.social.application.Models.User;
import com.social.application.Repositories.FollowRepository;
import com.social.application.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final PasswordEncoder passwordEncoder;


    @Value("${file.upload-dir}")
    private String uploadDir;

    public UserService(UserRepository userRepository, FollowRepository followRepository ,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.followRepository = followRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User insertUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    public List<UserDTO> fetchAllUser(){

            List<User> users = userRepository.findAll();

            return users.stream().map(user -> {

                long followers = followRepository.countByFollowerUser_Id(user.getId());
                long following = followRepository.countByFollowingUser_Id(user.getId());

                return new UserDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getBio(),
                        user.getProfileImage(),
                        followers,
                        following
                );

            }).toList();
        }

    public User fetchUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "User not found with id: " + id
                        )
                );
    }


    public UserDTO getUserDTO(Long userId) {
        User user = fetchUserById(userId);

        long followers = followRepository.countByFollowingUser_Id(userId);
        long following = followRepository.countByFollowerUser_Id(userId);

        return new UserDTO(
                userId,
                user.getUsername(),
                user.getBio(),
                user.getProfileImage(),
                followers,
                following
        );
    }

    public User registerUser(UserRequestDTO dto ) throws IOException {

        MultipartFile file = dto.getProfileImage();
        MultipartFile file1 = dto.getCoverImage();

        String fileName = null;
        String fileName1 = null;

        if (file != null && !file.isEmpty()) {
            // generate unique file name
            fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            fileName1 = UUID.randomUUID()+ "_" + file1.getOriginalFilename();

            File destination = new File(uploadDir + File.separator + fileName);
            File destination1 = new File(uploadDir + File.separator + fileName);

            file.transferTo(destination);
            file1.transferTo(destination1);
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setBio(dto.getBio());
        user.setProfileImage(fileName); // store only file name/path
        user.setCoverImage(fileName);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);

    }
}
