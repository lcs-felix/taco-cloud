package tacos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Data
public class Taco {

    private Long id;

    private LocalDateTime createdAt;

    @NotBlank
    @Size(min = 5, message = "Name must be at least 5 characters long")
    private String name;

    @NotNull(message = "You must choose at least 1 engredient")
    @Size(min = 1, message = "You must choose at least 1 engredient")
    private List<Ingredient> ingredients;

    public long getCreatedInMillis() {
        return createdAt.atZone(ZoneId.systemDefault()).toEpochSecond();
    }
}
