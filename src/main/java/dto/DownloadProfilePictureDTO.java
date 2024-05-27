package dto;

import entity.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DownloadProfilePictureDTO {

    private User userEntity;

    private byte[] profileImage;
}
