package br.com.ronan.springboot.requests;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.URL;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimePostRequestBody {
    @NotBlank(message = "The anime name cannot be blanck")
    private String name;

    @URL(message = "The URL is not valid")
    private String url;
}
