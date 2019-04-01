package tacos;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import tacos.data.IngredientRepository;
import tacos.data.TacoRepository;
import tacos.data.UserRepository;

import java.util.List;

import static tacos.Ingredient.Type.CHEESE;

@Profile("!prod")
@Configuration
public class DevelopmentData {

    @Bean
    public CommandLineRunner dataLoader(IngredientRepository repo) {
        return args -> {
            repo.save(new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP));
            repo.save(new Ingredient("COTO", "Corn Tortilla", Ingredient.Type.WRAP));
            repo.save(new Ingredient("GRBF", "Ground Beef", Ingredient.Type.PROTEIN));
            repo.save(new Ingredient("CARN", "Carnitas", Ingredient.Type.PROTEIN));
            repo.save(new Ingredient("TMTO", "Diced Tomatoes", Ingredient.Type.VEGGIES));
            repo.save(new Ingredient("LETC", "Lettuce", Ingredient.Type.VEGGIES));
            repo.save(new Ingredient("CHED", "Cheddar", CHEESE));
            repo.save(new Ingredient("JACK", "Monterrey Jack", CHEESE));
            repo.save(new Ingredient("SLSA", "Salsa", Ingredient.Type.SAUCE));
            repo.save(new Ingredient("SRCR", "Sour Cream", Ingredient.Type.SAUCE));
        };
    }

    @Bean
    public CommandLineRunner initalTacos(TacoRepository tacoRepository, IngredientRepository ingredientRepository) {
        return args -> {

            var flto = ingredientRepository.findById("FLTO").get();
            var carnitas = ingredientRepository.findById("CARN").get();
            var ched = ingredientRepository.findById("CHED").get();
            final Taco tacoCheddar = new Taco("I love cheddar taco", List.of(flto, carnitas, ched));
            tacoRepository.save(tacoCheddar);

            var cornTortilla = ingredientRepository.findById("COTO").get();
            var salsa = ingredientRepository.findById("SLSA").get();
            var jack = ingredientRepository.findById("JACK").get();
            final Taco tacoJack = new Taco("Monterrey Jack taco", List.of(cornTortilla, salsa, jack));
            tacoRepository.save(tacoJack);
        };
    }

    @Bean
    public CommandLineRunner initalUser(UserRepository userRepository, PasswordEncoder encoder) {
        return args -> {
            userRepository.save(
                    new User("lucas", encoder.encode("123"), "Lucas Félix",
                            "Rua Vergueiro", "São Paulo", "SP", "00000-111", "118776543535"));
        };
    }
}
