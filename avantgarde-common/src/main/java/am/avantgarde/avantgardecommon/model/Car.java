package am.avantgarde.avantgardecommon.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "car")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column
    private String model;
    @Column
    private int year;
    @Column
    @Enumerated(EnumType.STRING)
    private Color color;
    @Column
    private String price;
    @Column
    @Enumerated(EnumType.STRING)
    private BodyType bodyType;
    @Column
    private String engine;
    @Column
    @Enumerated(EnumType.STRING)
    private DriveTrain driveTrain;
    @Column
    @Enumerated(EnumType.STRING)
    private Transmission transmission;
    @Column
    @Enumerated(EnumType.STRING)
    private FuelType fuelType;
    @Column
    private int kilometres;
    @Column
    private int HP;
    @ManyToOne
    private Brand brand;
    @Column(name = "pic_url")
    private String picUrl;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column
    private String description;

}
