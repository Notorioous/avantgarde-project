package am.avantgarde.avantgardecommon.repository;

import am.avantgarde.avantgardecommon.model.NewsImages;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsImageRepository extends JpaRepository<NewsImages, Integer> {
    List<NewsImages> findByNewsId(int id);
}
