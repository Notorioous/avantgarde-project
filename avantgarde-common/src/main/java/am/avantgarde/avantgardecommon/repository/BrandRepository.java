package am.avantgarde.avantgardecommon.repository;

import am.avantgarde.avantgardecommon.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Integer> {

    Optional<Brand> findById(int id);

}
