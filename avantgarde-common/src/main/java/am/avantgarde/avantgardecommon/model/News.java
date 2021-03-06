package am.avantgarde.avantgardecommon.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "news")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column
    private String title;
    @Column
    private String shortText;
    @Column
    private String text;
    @Column
    private String createdDate;
    @Column
    private String picUrl;
    @ManyToOne
    private User user;
    @ManyToOne
    private Car car;
    @ManyToOne
    private Brand brand;


}
