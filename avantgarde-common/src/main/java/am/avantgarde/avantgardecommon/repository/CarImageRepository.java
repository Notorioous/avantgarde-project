package am.avantgarde.avantgardecommon.repository;

import am.avantgarde.avantgardecommon.model.CarImages;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarImageRepository extends JpaRepository<CarImages, Integer> {

    List<CarImages> findByCarId(int id);
}
