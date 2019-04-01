package tacos.web.api;

import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;
import tacos.Taco;

import java.time.LocalDateTime;
import java.util.List;

@Relation(value = "taco", collectionRelation = "tacos")
class TacoResource extends ResourceSupport {

    private static final IngredientResourceAssembler ingredientAssembler = new IngredientResourceAssembler();

    @Getter
    private final String name;

    @Getter
    private final LocalDateTime createdAt;

    @Getter
    private final List<IngredientResource> ingredients;

    TacoResource(Taco taco) {
        name = taco.getName();
        createdAt = taco.getCreatedAt();
        ingredients = ingredientAssembler.toResources(taco.getIngredients());
    }
}
