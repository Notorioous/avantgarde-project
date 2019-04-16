package am.avantgarde.avantgardecommon.repository;

import am.avantgarde.avantgardecommon.model.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Integer> {
}
