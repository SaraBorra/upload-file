package services;

import dto.*;
import entity.*;
import model.*;
import repo.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    FileStorageService fileStorageService;

    public UserDTO createUser(CreateUserRequest createUserRequest) {
        UserModel userModel = new UserModel(createUserRequest.getName(), createUserRequest.getSurname(), createUserRequest.getEmail(), createUserRequest.getProfilePicture());
        UserModel anotherUserModel = UserModel.entityToModel(userRepo.save(UserModel.modelToEntity(userModel)));
        return UserModel.modelToDto(anotherUserModel);
    }

    public UserDTO getSingleUser(long userId) {
        Optional<User> result = userRepo.findById(userId);
        if (result.isPresent()) {
            UserModel userModel = UserModel.entityToModel(result.get());
            return UserModel.modelToDto(userModel);
        } else {
            return null;
        }
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepo.findAll();
        ArrayList<UserDTO> userDTOs = new ArrayList<>();
        for (User user : users) {
            UserModel userModel = UserModel.entityToModel(user);
            UserDTO userDTO = UserModel.modelToDto(userModel);
            userDTOs.add(userDTO);
        }
        return userDTOs;
    }


    public UserDTO updateUser(long userId, UpdateUserRequest updateUserRequest) {
        Optional<User> result = userRepo.findById(userId);
        if (result.isPresent()) {
            result.get().setName(updateUserRequest.getName() == null ? result.get().getName() : updateUserRequest.getName());
            result.get().setSurname(updateUserRequest.getSurname() == null ? result.get().getSurname() : updateUserRequest.getSurname());
            result.get().setEmail(updateUserRequest.getEmail() == null ? result.get().getEmail() : updateUserRequest.getEmail());
            result.get().setProfilePicture(updateUserRequest.getProfilePicture() == null ? result.get().getProfilePicture() : updateUserRequest.getProfilePicture());
            User savedUser = userRepo.saveAndFlush(result.get());
            UserModel savedUserModel = UserModel.entityToModel(savedUser);
            return UserModel.modelToDto(savedUserModel);
        }
        return null;
    }

    public boolean deleteUser(long id) {
        Optional<User> result = userRepo.findById(id);
        if (result.isPresent()) {
            try {
                userRepo.delete(result.get());
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public void uploadProfilePicture(Long userId, MultipartFile profilePicture) throws Exception {
        Optional<User> result = userRepo.findById(userId);
        if (result.isPresent()) {
            if (!profilePicture.isEmpty()) {
                fileStorageService.remove(String.valueOf(profilePicture));
                String fileName = fileStorageService.upload(profilePicture);
                result.get().setProfilePicture(fileName);
                User savedUser = userRepo.saveAndFlush(result.get());
                UserModel savedUserModel = UserModel.entityToModel(savedUser);
                UserModel.modelToDto(savedUserModel);
            }
        }
    }

    @SneakyThrows
    public DownloadProfilePictureDTO downloadProfilePicture(Long userId) {
        UserDTO userDTO = getSingleUser(userId);
        UserModel savedUserModel = UserModel.DTOtoModel(userDTO);
        User savedUser = UserModel.modelToEntity(savedUserModel);
        DownloadProfilePictureDTO dto = new DownloadProfilePictureDTO();
        dto.setUserEntity(savedUser);
        if (userDTO.getProfilePicture() == null) return dto;
        byte[] profilePictureBytes = fileStorageService.download(userDTO.getProfilePicture());
        dto.setProfileImage(profilePictureBytes);
        return dto;
    }
}
