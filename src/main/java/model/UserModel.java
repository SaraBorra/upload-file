package model;

import dto.*;
import entity.*;
import lombok.Data;

@Data
public class UserModel {

    private Long id;
    private String name;
    private String surname;
    private String email;
    private String profilePicture;

    public UserModel(Long id, String name, String surname, String email, String profilePicture) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.profilePicture = profilePicture;
    }

    public UserModel(String name, String surname, String email, String profilePicture) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.profilePicture = profilePicture;
    }

    public static UserDTO modelToDto(UserModel userModel) {
        return new UserDTO(userModel.getId(), userModel.getName(), userModel.getSurname(), userModel.getEmail(), userModel.getProfilePicture());
    }

    public static User modelToEntity(UserModel userModel) {
        return new User(userModel.getId(), userModel.getName(), userModel.getSurname(), userModel.getEmail(), userModel.getProfilePicture());
    }

    public static UserModel entityToModel(User user) {
        return new UserModel(user.getId(), user.getName(), user.getSurname(), user.getEmail(), user.getProfilePicture());
    }

    public static UserModel DTOtoModel(UserDTO userDTO) {
        return new UserModel((userDTO.getId()), userDTO.getName(), userDTO.getSurname(), userDTO.getEmail(), userDTO.getProfilePicture());
    }
}
