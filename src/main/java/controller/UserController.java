package controller;

import dto.*;
import services.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    FileStorageService fileStorageService;

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest createUserRequest) {
        UserDTO result = userService.createUser(createUserRequest);
        if (result == null) {
            return ResponseEntity.status(420).body("utente null");
        } else {
            return ResponseEntity.ok(result);
        }

    }

    @PostMapping("/uploadPicture/{id}/profile")
    public ResponseEntity<?> uploadProfilePicture(@PathVariable Long id, @RequestParam MultipartFile profilePicture) throws Exception {
        UserDTO result = userService.getSingleUser(id);
        if (result == null) {
            return ResponseEntity.status(422).body("User not found");
        } else {
            userService.uploadProfilePicture(id, profilePicture);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<?> getAllUsers() {
        List<UserDTO> result = userService.getAllUsers();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getSingleUser/{id}")
    public ResponseEntity<?> getOneUser(@PathVariable Long id) {
        UserDTO result = userService.getSingleUser(id);
        if (result == null) {
            return ResponseEntity.status(422).body("User not found");
        } else {
            return ResponseEntity.ok(result);
        }
    }

    @SneakyThrows
    @GetMapping("/getProfilePicture/{id}/profile")
    public @ResponseBody byte[] getProfilePicture(@PathVariable Long id, HttpServletResponse response) {
        DownloadProfilePictureDTO downloadProfilePictureDTO = userService.downloadProfilePicture(id);
        String fileName = downloadProfilePictureDTO.getUserEntity().getProfilePicture();
        if (fileName == null) throw new Exception("User does not have a profile picture");
        String extention = FilenameUtils.getExtension(fileName);
        switch (extention) {
            case "gif":
                response.setContentType(MediaType.IMAGE_GIF_VALUE);
                break;
            case "jpg":
            case "jpeg":
                response.setContentType(MediaType.IMAGE_JPEG_VALUE);
                break;
            case "png":
                response.setContentType(MediaType.IMAGE_PNG_VALUE);
                break;
        }
        response.setHeader("Content-Disposition", "attachment; fileName=\"" + fileName + "\"");
        return downloadProfilePictureDTO.getProfileImage();

    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<?> updateSingleUser(@PathVariable Long id, @RequestBody UpdateUserRequest updateUserRequest) {
        UserDTO result = userService.updateUser(id, updateUserRequest);
        if (result == null) {
            return ResponseEntity.status(422).body("User not found");
        } else {
            return ResponseEntity.ok(result);
        }
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        boolean result = userService.deleteUser(id);
        if (result) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(421).body("impossible to delete the user");
        }

    }
}
