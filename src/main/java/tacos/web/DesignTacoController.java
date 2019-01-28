package tacos.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tacos.Ingredient;
import tacos.Ingredient.Type;
import tacos.Order;
import tacos.Taco;
import tacos.User;
import tacos.data.IngredientRepository;
import tacos.data.TacoRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.StreamSupport;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignTacoController {

    private final IngredientRepository ingredientRepo;
    private final TacoRepository designRepo;

    public DesignTacoController(IngredientRepository ingredientRepo, TacoRepository designRepo) {
        this.ingredientRepo = ingredientRepo;
        this.designRepo = designRepo;
    }

    @ModelAttribute(name = "order")
    public Order order() {
        return new Order();
    }

    @ModelAttribute(name = "taco")
    public Taco taco() {
        return new Taco();
    }

    @GetMapping
    public String showDesignForm(Model model, @AuthenticationPrincipal User user) {
        final Iterable<Ingredient> ingredients = ingredientRepo.findAll();

        addIngredientsToModel(model, ingredients);
        model.addAttribute("user", user);

        return "design";
    }

    @PostMapping
    public String processDesign(@Valid Taco design, BindingResult result, @ModelAttribute Order order) {

        if(result.hasErrors()) {
            return "design";
        }

        Taco saved = designRepo.save(design);
        order.addDesign(saved);

        return "redirect:/orders/current";
    }

    private List<Ingredient> filterByType(Iterable<Ingredient> ingredients, Type type) {
        return StreamSupport.stream(ingredients.spliterator(), false)
                .filter(ingredient -> ingredient.getType().equals(type))
                .collect(toList());
    }

    private void addIngredientsToModel(Model model, Iterable<Ingredient> ingredients) {
        stream(Type.values())
                .forEach(type -> {
                    String typeName = type.toString().toLowerCase();
                    model.addAttribute(typeName, filterByType(ingredients, type));
                });
    }
}
