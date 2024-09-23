package groupwork.temperatursensor.modell;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "data")
public class Temperature {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;

        @Column(name = "temperatur")
        private Float temp;

        @Column(name = "timepoint")
        private LocalDateTime timePoint;

        public Temperature() {}

        public Temperature(Float temp, LocalDateTime timePoint) {
                this.temp = temp;
                this.timePoint = timePoint;
        }
}
