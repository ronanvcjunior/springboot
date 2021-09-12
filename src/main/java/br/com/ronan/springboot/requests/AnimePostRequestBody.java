package br.com.ronan.springboot.requests;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;

// import org.hibernate.validator.constraints.URL;

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
    @Schema(description = "This is Anime's name", example = "Tensei Shittare Slime Datta Ken", required = true)
    private String name;

    // @URL(message = "The URL is not valid")
    // private String url;
}
