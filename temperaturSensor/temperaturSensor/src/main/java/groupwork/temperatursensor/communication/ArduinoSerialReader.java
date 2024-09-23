package groupwork.temperatursensor.communication;

import com.fazecast.jSerialComm.SerialPort;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.*;

@Service
public class ArduinoSerialReader {

    private SerialPort serialPort;

    public ArduinoSerialReader() {
        // Öppna seriell port
        serialPort = SerialPort.getCommPort("COM7"); // Justera port om det behövs
        serialPort.setBaudRate(9600);
        if (serialPort.openPort()) {
            System.out.println("Öppnade porten: COM7");
        } else {
            System.err.println("Kunde inte öppna porten: COM7!");
        }
    }

    // Metod för att läsa temperaturdata från Arduino
    public String readFromArduino() {
        StringBuilder sb = new StringBuilder();

        if (serialPort.isOpen()) {
            InputStream inputStream = serialPort.getInputStream();
            byte[] buffer = new byte[1024];
            int numRead;

            try {
                // Attempt to read data from the input stream
                numRead = inputStream.read(buffer);

                // If data was read
                if (numRead > 0) {
                    for (int i = 0; i < numRead; i++) {
                        sb.append((char) buffer[i]);
                    }

                    // Trim and log the result
                    String result = sb.toString().trim();
                    System.out.println("Lästa data från Arduino: " + result);

                    // Return the result if it's a valid temperature reading
                    if (!result.isEmpty()) {
                        return result; // Return the non-empty string
                    } else {
                        System.out.println("Ingen giltig temperatur mottagen från Arduino.");
                    }
                } else {
                    System.out.println("Inga data lästa från Arduino.");
                }
            } catch (IOException e) {
                System.err.println("Fel vid läsning från Arduino: " + e.getMessage());
            }
        } else {
            System.err.println("Seriell port är inte öppen");
        }

        return ""; // Return an empty string if no valid data was read
    }



    public void closePort() {
        if (serialPort.isOpen()) {
            serialPort.closePort();
            System.out.println("Porten stängdes.");
        }
    }
}
