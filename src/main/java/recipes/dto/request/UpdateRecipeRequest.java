package recipes.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateRecipeRequest {
    @NotBlank(message = "Name shouldn't be blank")
    String name;
    @NotBlank(message = "Category shouldn't be blank")
    String category;
    @NotBlank(message = "Description shouldn't be blank")
    String description;
    @NotEmpty(message = "Ingredients shouldn't be empty")
    List<String> ingredients;
    @NotEmpty(message = "Directions shouldn't be empty")
    List<String> directions;
}
