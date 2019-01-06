package tacos.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import tacos.Order;
import tacos.Taco;

import java.time.LocalDateTime;
import java.util.Map;

@Repository
public class JdbcOrderRepository implements OrderRepository {

    private final SimpleJdbcInsert orderInsert;
    private final SimpleJdbcInsert orderTacoInsert;
    private final ObjectMapper objectMapper;

    public JdbcOrderRepository(JdbcTemplate jdbcTemplate) {

        orderInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("Taco_Order")
                .usingGeneratedKeyColumns("id");

        orderTacoInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("Taco_Order_Tacos");

        objectMapper = new ObjectMapper();
    }

    @Override
    public Order save(Order order) {
        order.setPlacedAt(LocalDateTime.now());
        long orderId = saveOrderDetails(order);
        order.setId(orderId);

        order.getTacos()
                .forEach(taco -> saveTacoToOrder(taco, orderId));

        return order;
    }

    private long saveOrderDetails(Order order) {
        Map<String, Object> values =
                objectMapper.convertValue(order, Map.class);
        values.put("placedAt", order.getPlacedAt());

        long orderId = orderInsert
                .executeAndReturnKey(values)
                .longValue();

        return orderId;
    }

    private void saveTacoToOrder(Taco taco, long orderId) {
        Map<String, Object> values = Map.of(
                "tacoOrder", orderId,
                "taco", taco.getId()
        );

        orderTacoInsert
                .execute(values);
    }
}
