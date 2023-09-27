package recipes.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "RECIPE")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecipeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    Long id;
    @Column(name = "CATEGORY")
    String category;
    @UpdateTimestamp
    @Column(name = "DATE")
    LocalDateTime date;
    @Column(name = "NAME")
    String name;
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    UserEntity author;
    @Column(name = "DESCRIPTION")
    String description;
    @ElementCollection
    List<String> ingredients;
    @ElementCollection
    List<String> directions;
}
