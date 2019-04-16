package am.avantgarde.avantgardecommon.repository;

import am.avantgarde.avantgardecommon.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Integer> {

    List<Car> findTop10ByOrderByIdDesc();

    Car findFirstByOrderByIdDesc();
}
