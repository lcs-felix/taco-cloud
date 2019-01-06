package tacos.data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import tacos.Ingredient;
import tacos.Taco;

import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JdbcTacoRepository implements TacoRepository {

    private final JdbcTemplate jdbc;

    public JdbcTacoRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Taco save(Taco taco) {
        long tacoId = saveTacoInfo(taco);
        taco.setId(tacoId);
        saveIngredients(tacoId, taco.getIngredients());
        return taco;
    }

    private long saveTacoInfo(Taco taco) {
        taco.setCreatedAt(LocalDateTime.now());

        var psc = new PreparedStatementCreatorFactory("insert into Taco(name, createdAt) values(?, ?)",
                Types.VARCHAR, Types.TIMESTAMP)
                .newPreparedStatementCreator(
                        List.of(
                                taco.getName(), new Timestamp(taco.getCreatedInMillis())
                        )
                );

        var keyHolder = new GeneratedKeyHolder();
        return jdbc.update(psc, keyHolder);
    }

    private void saveIngredients(long tacoId, List<Ingredient> ingredients) {

        ingredients.forEach(ingredient ->
                jdbc.update("insert into Taco_Ingredients(taco, ingredient) values(?, ?)",
                        tacoId, ingredient.getId()));
    }
}
