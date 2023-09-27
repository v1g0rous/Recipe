package recipes.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import recipes.dto.request.UpdateRecipeRequest;
import recipes.dto.response.CreateRecipeResponse;
import recipes.dto.request.CreateRecipeRequest;
import recipes.dto.response.GetRecipeResponse;
import recipes.entity.RecipeEntity;
import recipes.exception.InvalidParametersCountException;
import recipes.exception.RecipeNotFoundException;
import recipes.exception.UserIsNotAuthorException;
import recipes.service.RecipesService;

import java.util.List;

/**
 * REST controller
 *
 * <p>
 * This controller provides CRUD operations to work with recipes
 * <p/>
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/recipe")
public class RecipesController {
    RecipesService recipesService;

    /**
     * Create new recipe
     * Requires authentication
     * @param request - incoming HTTP request with new recipe details
     * @param details - details of authenticated user
     */
    @PostMapping("/new")
    public ResponseEntity<CreateRecipeResponse> createRecipe(@Valid @RequestBody CreateRecipeRequest request,
                                                             @AuthenticationPrincipal UserDetails details) {
        long recipeId = recipesService.createRecipe(request, details).getId();
        CreateRecipeResponse response = new CreateRecipeResponse();
        response.setId(recipeId);

        return ResponseEntity.ok(response);
    }

    /**
     * Retrieve recipe by its id
     * Requires authentication
     * @param id - recipe id
     * @throws RecipeNotFoundException if recipe with specified id doesn't exist
     */
    @GetMapping("/{id}")
    public GetRecipeResponse getRecipe(@PathVariable long id) {
        RecipeEntity recipeEntity = recipesService.getRecipeById(id);
        GetRecipeResponse response = recipesService.toGetRecipeResponse(recipeEntity);
        return response;
    }

    /**
     * Retrieve list of recipes either by recipe category OR by recipe name
     * Requires authentication
     * @param name
     * @param category
     * @throws InvalidParametersCountException if either category or name not provided or both of them provided at the same time
     */
    @GetMapping("/search")
    public List<GetRecipeResponse> searchRecipe(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category) {

        boolean invalidParametersCount = (name == null && category == null) ||
                (name != null && category != null);

        if (invalidParametersCount) {
            throw new InvalidParametersCountException("Either 'category' or 'name' must be provided.");
        }

        List<GetRecipeResponse> response = null;

        if (name != null) {
            response = recipesService.searchRecipesByName(name);
        } else if (category != null) {
            response = recipesService.searchRecipesByCategory(category);
        }

        return response;
    }

    /**
     * Update recipes by specified id
     * Requires authentication
     * Only an author of recipe can update it.
     * @param request - incoming HTTP request with modified recipe details
     * @param id - recipe id
     * @param details - details of authenticated user
     * @throws RecipeNotFoundException if recipe with specified id doesn't exist
     * @throws UserIsNotAuthorException if method called not by an author of recipe
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateRecipe(@Valid @RequestBody UpdateRecipeRequest request,
                                             @PathVariable long id,
                                             @AuthenticationPrincipal UserDetails details) {
        recipesService.updateRecipe(request, id, details);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Delete a recipe by specified id
     * Requires authentication
     * Only an author of recipe can delete it.
     * @param id - recipe id
     * @param details - details of authenticated user
     * @throws RecipeNotFoundException if recipe with specified id doesn't exist
     * @throws UserIsNotAuthorException if method called not by an author of recipe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable long id,
                                             @AuthenticationPrincipal UserDetails details) {
        recipesService.deleteRecipeById(id, details);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

