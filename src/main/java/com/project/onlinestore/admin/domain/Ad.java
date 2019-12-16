package com.project.onlinestore.admin.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ad implements Serializable {

    @NotBlank
    private String link;

    @NotNull
    private MultipartFile image;

    public MultipartFile getImage() {
        return image;
    }
}
