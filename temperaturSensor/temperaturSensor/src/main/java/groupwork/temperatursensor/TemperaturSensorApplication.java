package groupwork.temperatursensor;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import groupwork.temperatursensor.communication.ArduinoSerialReader;
import groupwork.temperatursensor.service.TemperatureService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableScheduling
public class TemperaturSensorApplication {

    @Autowired
    private TemperatureService temperatureService;

    public static void main(String[] args) {
        SpringApplication.run(TemperaturSensorApplication.class, args);
    }

    @PostConstruct
    public void startTemperatureReadingThread() {
        new Thread(() -> {
            while (true) {
                temperatureService.receiveTemperature(); // Kalla på metoden som läser från Arduino
                try {
                    Thread.sleep(5000); // Vänta 5 sekunder mellan varje läsning
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
