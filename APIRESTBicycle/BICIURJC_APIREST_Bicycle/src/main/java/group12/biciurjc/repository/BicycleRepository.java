package group12.biciurjc.repository;

import group12.biciurjc.model.Bicycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BicycleRepository extends JpaRepository<Bicycle, Long> {
}