package tacos.web.api;

import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tacos.Ingredient;
import tacos.data.IngredientRepository;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Log
@RestController
@RequestMapping(path = "ingredients", produces = APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class IngredientController {

    private final IngredientRepository ingredientRepository;

    public IngredientController(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @GetMapping
    public Iterable<Ingredient> allIngredients() {
        return ingredientRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredientResource> getById(@PathVariable String id) {
        var possibleIngredient = ingredientRepository.findById(id);

        if(possibleIngredient.isEmpty()) {
            return ResponseEntity.status(NOT_FOUND).build();
        }

        IngredientResourceAssembler ingredientAssembler = new IngredientResourceAssembler();
        IngredientResource ingredientResource = ingredientAssembler.toResource(possibleIngredient.get());
        return ResponseEntity.ok(ingredientResource);
    }
}
