package groupwork.temperatursensor.repo;

import groupwork.temperatursensor.modell.Temperature;
import org.springframework.data.jpa.repository.JpaRepository;

public interface tempRepo extends JpaRepository<Temperature, Long> {
    // Hämta den senaste temperaturen
    // @Query("SELECT t FROM Temperature t ORDER BY t.timePoint DESC")
    Temperature findTopByOrderByTimePointDesc();
}
