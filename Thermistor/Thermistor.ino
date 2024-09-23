// Definiera pinnen där thermistor är ansluten
const int thermistorPin = A0;

// Konstant för att konvertera från spänning till temperatur
const float BETA = 3950; // B-värde för thermistor 63000
const float THERMISTOR_NOMINAL = 10000; // Resistance vid 25°C
const float TEMPERATURE_NOMINAL = 25.0; // Nominal temperatur vid thermistor

void setup() {
  Serial.begin(9600); // Starta seriekommunikation
}

void loop() {
  int adcValue = analogRead(thermistorPin); // Läs ADC-värdet från thermistor
  float resistance = (1023.0 / adcValue - 1.0) * THERMISTOR_NOMINAL; // Beräkna resistance
  float temperature = 1.0 / (log(resistance / THERMISTOR_NOMINAL) / BETA + 1.0 / (TEMPERATURE_NOMINAL + 273.15)) - 273.15; // Beräkna temperatur i °C

  Serial.println(temperature, 2);
  

  delay(3000); // Vänta en sekund innan nästa mätning
}