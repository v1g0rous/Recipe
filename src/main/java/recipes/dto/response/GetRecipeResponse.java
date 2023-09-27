package recipes.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetRecipeResponse {
    String name;
    String category;
    LocalDateTime date;
    String description;
    List<String> ingredients;
    List<String> directions;
}
