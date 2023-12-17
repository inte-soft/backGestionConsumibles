package com.intesoft.syncworks.interfaces.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class UploadRequestDto {

        private String folderName;
        private List<String> fileNames;

        // Getters and Setters

}
