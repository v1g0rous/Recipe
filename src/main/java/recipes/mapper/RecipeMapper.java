package recipes.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.security.core.userdetails.UserDetails;
import recipes.dto.request.CreateRecipeRequest;
import recipes.dto.request.UpdateRecipeRequest;
import recipes.dto.response.CreateRecipeResponse;
import recipes.dto.response.GetRecipeResponse;
import recipes.entity.RecipeEntity;

@Mapper(componentModel = "spring")
public interface RecipeMapper {
    @Mapping(target = "id", ignore = true)
    RecipeEntity toEntity(CreateRecipeRequest recipeRequest);

    CreateRecipeResponse toCreateRecipeResponse(RecipeEntity entity);
    GetRecipeResponse toGetRecipeResponse(RecipeEntity entity);
    void updateRecipeFromRequest(UpdateRecipeRequest recipeRequest, @MappingTarget RecipeEntity entity);

}
