package tacos;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@NoArgsConstructor
@Data
@Entity
@RestResource(rel="tacos", path="tacos")
public class Taco {

    @Id @GeneratedValue
    private Long id;

    private LocalDateTime createdAt;

    @NotBlank
    @Size(min = 5, message = "Name must be at least 5 characters long")
    private String name;

    @ManyToMany
    @NotNull(message = "You must choose at least 1 engredient")
    @Size(min = 1, message = "You must choose at least 1 engredient")
    private List<Ingredient> ingredients;

    public Taco(String name, List<Ingredient> ingredients) {
        this.name = name;
        this.ingredients = ingredients;
    }

    public long getCreatedInMillis() {
        return createdAt.atZone(ZoneId.systemDefault()).toEpochSecond();
    }

    @PrePersist
    void createdAt() {
        createdAt = LocalDateTime.now();
    }
}
