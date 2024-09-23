package groupwork.temperatursensor.service;

import groupwork.temperatursensor.communication.ArduinoSerialReader;
import groupwork.temperatursensor.modell.Temperature;
import groupwork.temperatursensor.repo.tempRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TemperatureService implements Runnable {

    private final tempRepo temperatureDataRepository;
    private final ArduinoSerialReader arduinoSerialReader;
    private volatile Float data = null; // Variabel för att hålla senaste temperaturen

    @Autowired
    public TemperatureService(tempRepo temperatureDataRepository, ArduinoSerialReader arduinoSerialReader) {
        this.temperatureDataRepository = temperatureDataRepository;
        this.arduinoSerialReader = arduinoSerialReader;

        // Starta en tråd för att läsa och spara temperaturdata
        Thread temperatureThread = new Thread(this);
        temperatureThread.start();
    }

    // Metod för att ta emot och lagra temperaturen
    public void receiveTemperature() {
        String data = arduinoSerialReader.readFromArduino();

        // Logga den råa datan
        System.out.println("Direkt från Arduino: " + data);

        // Check if data is valid
        if (!data.isEmpty()) {
            // Trimma datan för att ta bort eventuella extra mellanslag eller radbrytningar
            String trimmedData = data.trim().replaceAll("[\\r\\n]", "");
            System.out.println("TRIMMED DATA: " + trimmedData);

            // Dela strängen om det finns flera värden
            String[] values = trimmedData.split("\n"); // Dela på ett eller flera mellanslag

            // Loopar genom värdena för att spara giltiga temperaturer
            for (String value : values) {
                value = value.trim();
                // Kontrollera om värdet matchar förväntat format (ex. "49.00")
                if (value.matches("\\d+\\.\\d{2}")) {
                    try {
                        // Konvertera till Float
                        Float dataConvert = Float.parseFloat(value);
                        // Spara temperaturen
                        saveTemperature(dataConvert);
                        System.out.println("Temperatur uppdaterad: " + dataConvert);
                    } catch (NumberFormatException e) {
                        System.out.println("Ogiltig temperaturdata mottagen: " + value);
                    }
                } else {
                    System.out.println("Ogiltig temperaturdata mottagen (formatfel): " + value);
                }
            }
        }
    }

    // Spara temperaturen till databasen
    public void saveTemperature(Float temperature) {
        // Your logic to save the temperature String to the database
        // Example using JPA or JDBC, depending on your setup
        // Assume you have an entity for the temperature data
        Temperature temperatureData = new Temperature(temperature, LocalDateTime.now());
        temperatureDataRepository.save(temperatureData); // Adjust according to your repository
        System.out.println("Temperaturdata sparad: " + temperature);
    }

    // Tråden som kontinuerligt läser från Arduino och sparar till databasen
    @Override
    public void run() {
        while (true) {
            try {
                // Läser temperaturen från Arduino
                receiveTemperature();

                // Om vi har giltig temperaturdata, spara den till databasen
                if (data != null) {
                    saveTemperature(data);
                }

                // Vänta i 10 sekunder innan nästa mätning
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                System.err.println("Tråd avbruten: " + e.getMessage());
            }
        }
    }


    // Lägger till metoden i TemperatureService för att hämta senaste post från databasen
    public Temperature getLatestTemperatureFromDatabase() {
        return temperatureDataRepository.findTopByOrderByTimePointDesc(); // Hämtar senaste posten
    }

    public List<Temperature> getAllTemperatures() {
        return temperatureDataRepository.findAll();
    }
}
