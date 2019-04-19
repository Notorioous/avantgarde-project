package am.avantgarde.avantgardecommon.repository;

import am.avantgarde.avantgardecommon.model.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NewsRepository extends JpaRepository<News, Integer> {

    Optional<News> findById(int id);

    Page<News> findAllByOrderByIdDesc(Pageable pageable);

    List<News> findTop5ByOrderById();
}
