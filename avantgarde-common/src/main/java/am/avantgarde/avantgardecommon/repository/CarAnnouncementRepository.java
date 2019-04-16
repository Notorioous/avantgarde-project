package am.avantgarde.avantgardecommon.repository;

import am.avantgarde.avantgardecommon.model.CarAnnouncement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarAnnouncementRepository extends JpaRepository<CarAnnouncement, Integer> {

    CarAnnouncement findTop1ByOrderByIdDesc();

}
