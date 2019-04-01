package tacos.web.api;

import org.springframework.hateoas.EntityLinks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tacos.Taco;
import tacos.data.TacoRepository;

import static org.springframework.http.HttpStatus.OK;

@RestController("apiController")
@RequestMapping(path = "/design", produces = "application/json")
@CrossOrigin(origins = "*")
public class DesignTacoController {

    private final TacoRepository tacoRepository;

    public DesignTacoController(TacoRepository tacoRepository, EntityLinks entityLinks) {
        this.tacoRepository = tacoRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Taco> tacoById(@PathVariable Long id) {
        var optTaco = tacoRepository.findById(id);
        return optTaco
                .map(taco -> new ResponseEntity<>(taco, OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }
}
