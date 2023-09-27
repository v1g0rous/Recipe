package recipes.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import recipes.entity.UserEntity;
import recipes.exception.RecipeNotFoundException;
import recipes.dto.request.CreateRecipeRequest;
import recipes.dto.request.UpdateRecipeRequest;
import recipes.dto.response.CreateRecipeResponse;
import recipes.dto.response.GetRecipeResponse;
import recipes.entity.RecipeEntity;
import recipes.exception.UserIsNotAuthorException;
import recipes.mapper.RecipeMapper;
import recipes.repository.RecipeRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing recipes
 * <p>
 * This class provides methods for finding, creating, updating, and deleting recipes.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class RecipesService {
    private final RecipeRepository repository;
    private final UserService userService;
    private final RecipeMapper mapper;

    /**
     * Create a new recipe
     * Requires authentication
     * @param recipeRequest - incoming HTTP request with new recipe details
     * @param details - details of authenticated user
     * @return DTO representing HTTP response with recipe details
     */
    public CreateRecipeResponse createRecipe(CreateRecipeRequest recipeRequest, UserDetails details) {
        RecipeEntity recipeEntity = mapper.toEntity(recipeRequest);
        UserEntity userEntity = userService.findUserByUsername(details.getUsername());
        recipeEntity.setAuthor(userEntity);

        RecipeEntity savedRecipeEntity = repository.save(recipeEntity);
        return mapper.toCreateRecipeResponse(savedRecipeEntity);
    }

    /**
     * Convert recipe entity to DTO representing HTTP response
     * @param recipeEntity - recipe entity from DB
     * @return DTO representing HTTP response with recipe details
     */
    public GetRecipeResponse toGetRecipeResponse(RecipeEntity recipeEntity) {
        return mapper.toGetRecipeResponse(recipeEntity);
    }

    /**
     * Retrieve recipe by specified id
     * @param id - recipe id
     * @return recipe entity from DB
     * @throws RecipeNotFoundException if recipe with specified id doesn't exist
     */
    public RecipeEntity getRecipeById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException());
    }

    /**
     * Delete recipe by specified id from DB
     * Requires authentication
     * Only recipe author can delete recipe from DB
     * @param id - recipe id
     * @param details - details of authenticated user
     * @throws RecipeNotFoundException if recipe with specified id doesn't exist
     * @throws UserIsNotAuthorException if method called not by an author of recipe
     */
    public void deleteRecipeById(long id, UserDetails details) {
        validateRecipeExists(id);
        RecipeEntity existingRecipe = getRecipeById(id);
        validateUserIsAuthor(existingRecipe, details);
        repository.deleteById(id);
    }

    /**
     * Update recipes by specified id
     * Requires authentication
     * Only an author of recipe can update it.
     * @param request
     * @param id
     * @param details
     * @throws RecipeNotFoundException if recipe with specified id doesn't exist
     * @throws UserIsNotAuthorException if method called not by an author of recipe
     */
    public void updateRecipe(UpdateRecipeRequest request, long id, UserDetails details) {
        validateRecipeExists(id);
        RecipeEntity existingRecipe = getRecipeById(id);
        validateUserIsAuthor(existingRecipe, details);
        updateAndSaveRecipe(request, existingRecipe);
    }

    /**
     * Update recipe entity with changes from incoming HTTP request
     * Save updated recipe entity to DB
     * @param request - incoming HTTP request with modified recipe details
     * @param existingRecipe - existing recipe entity from DB
     */
    private void updateAndSaveRecipe(UpdateRecipeRequest request, RecipeEntity existingRecipe) {
        mapper.updateRecipeFromRequest(request, existingRecipe);
        repository.save(existingRecipe);
    }

    /**
     * Check that authenticates user is an author of specified recipe
     * @param existingRecipe - specified recipe
     * @param details - details of authenticated user
     * @throws UserIsNotAuthorException if method called not by an author of recipe
     */
    private void validateUserIsAuthor(RecipeEntity existingRecipe, UserDetails details) {
        if (!details.getUsername().equals(existingRecipe.getAuthor().getUsername())) {
            throw new UserIsNotAuthorException("User is not an author of current recipe");
        }
    }

    /**
     * Check that recipe with specified id exists in DB
     * @param id - recipe id
     * @throws RecipeNotFoundException if recipe with specified id doesn't exist
     */
    private void validateRecipeExists(long id) {
        if (!repository.existsById(id)) {
            throw new RecipeNotFoundException();
        }
    }

    /**
     * Retrieve list of recipes by recipe name
     * @param name
     */
    public List<GetRecipeResponse> searchRecipesByName(String name) {
        List<RecipeEntity> recipeEntities = repository.findByNameContainingIgnoreCaseOrderByDateDesc(name);
        List<GetRecipeResponse> recipesDto = recipeEntities.stream().map(mapper::toGetRecipeResponse).collect(Collectors.toList());
        return recipesDto;
    }

    /**
     * Retrieve list of recipes either by recipe category
     * @param category
     */
    public List<GetRecipeResponse> searchRecipesByCategory(String category) {
        List<RecipeEntity> recipeEntities = repository.findByCategoryIgnoreCaseOrderByDateDesc(category);
        List<GetRecipeResponse> recipesDto = recipeEntities.stream().map(mapper::toGetRecipeResponse).collect(Collectors.toList());
        return recipesDto;
    }
}
