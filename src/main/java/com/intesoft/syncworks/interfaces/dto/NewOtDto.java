package com.intesoft.syncworks.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
public class NewOtDto {
    private String ot;
    private String name;
    private List<String> names;
    private MultipartFile[] files;

    public NewOtDto() {
    }

    public NewOtDto(String ot, String name, List<String> names, MultipartFile[] files) {
        this.ot = ot;
        this.name = name;
        this.names = names;
        this.files = files;
    }

    public String getOt() {
        return ot;
    }

    public void setOt(String ot) {
        this.ot = ot;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public MultipartFile[] getFiles() {
        return files;
    }

    public void setFiles(MultipartFile[] files) {
        this.files = files;
    }
}
