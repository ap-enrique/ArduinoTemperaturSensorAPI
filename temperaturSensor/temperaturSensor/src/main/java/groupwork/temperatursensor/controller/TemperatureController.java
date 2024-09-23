package groupwork.temperatursensor.controller;
import groupwork.temperatursensor.modell.Temperature;

import groupwork.temperatursensor.service.TemperatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/temperature")
public class TemperatureController {

    private final TemperatureService temperatureService;

    @Autowired
    public TemperatureController(TemperatureService temperatureService) {
        this.temperatureService = temperatureService;
    }

    // Endpoint för att hämta den senaste temperaturen från databasen
    @GetMapping("/latest")
    public ResponseEntity<Temperature> getLatestTemperature() {
        Temperature latestTemperature = temperatureService.getLatestTemperatureFromDatabase(); // Från databasen
        if (latestTemperature != null) {
            return new ResponseEntity<>(latestTemperature, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    // Endpoint för att hämta alla temperaturer
    @GetMapping("/all")
    public ResponseEntity<List<Temperature>> getAllTemperatures() {
        List<Temperature> allTemperatures = temperatureService.getAllTemperatures();
        if (!allTemperatures.isEmpty()) {
            return new ResponseEntity<>(allTemperatures, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
