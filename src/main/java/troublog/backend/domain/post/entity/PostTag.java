package troublog.backend.domain.post.entity;

import jakarta.persistence.*;
import troublog.backend.domain.tag.entity.Tag;

@Entity
@Table(name = "post_tags")
public class PostTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_tag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_tag_id")
    private Tag tag;

    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "post_id")
    //private Post post;
}

