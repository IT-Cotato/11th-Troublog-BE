package troublog.backend.domain.like.entity;

import jakarta.persistence.*;
import troublog.backend.domain.user.entity.User;

@Entity
@Table(name = "likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likes_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "post_id")
    //private Post post;
}
