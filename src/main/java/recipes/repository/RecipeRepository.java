package recipes.repository;

import org.springframework.data.repository.CrudRepository;
import recipes.entity.RecipeEntity;

import java.util.List;

public interface RecipeRepository extends CrudRepository<RecipeEntity, Long> {
    List<RecipeEntity> findByNameContainingIgnoreCaseOrderByDateDesc(String name);
    List<RecipeEntity> findByCategoryIgnoreCaseOrderByDateDesc(String category);
}
