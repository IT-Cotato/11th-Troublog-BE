package troublog.backend.domain.tag.entity;

import jakarta.persistence.*;
import troublog.backend.domain.post.entity.PostTag;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tach_stack_id")
    private Long id;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostTag> postTags = new ArrayList<>();

    private String name;

    @Column(name = "tad_type")
    private String tagType;

    private String description;
}
