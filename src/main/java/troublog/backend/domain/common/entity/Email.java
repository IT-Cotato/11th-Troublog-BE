package troublog.backend.domain.common.entity;

import org.hibernate.annotations.Comment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import troublog.backend.global.common.entity.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "email")
public class Email extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Comment("이메일아이디")
	@Column(name = "email_id")
	private Long emailId;

	@NotNull
	@Comment("메일 제목")
	@Column(name = "email_title")
	private String emailTitle;

	@NotNull
	@Comment("메일 내용")
	@Column(name = "email_body", columnDefinition = "TEXT")
	private String emailBody;

	@NotNull
	@Comment("발신자 메일 주소")
	@Column(name = "sender_email_adr")
	private String senderEmailAdr;

	@NotNull
	@Comment("수신자 메일 주소")
	@Column(name = "rcvr_email_adr")
	private String rcvrEmailAdr;

	@Comment("수신자명")
	@Column(name = "rcvr_name")
	private String rcvrName;
}
