package am.avantgarde.avantgardecommon.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "announcement")
public class CarAnnouncement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column
    private String slogan;
    @Column
    private String model;
    @Column
    private String price;
    @Column
    private String picUrl;
}
