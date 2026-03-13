package troublog.backend.domain.trouble.service.facade;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.trouble.entity.PostSummary;
import troublog.backend.domain.trouble.entity.SummaryContent;

@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostSummaryRelationFacadeService {

	public void setRelation(PostSummary postSummary, List<SummaryContent> summaryContents) {
		if (!CollectionUtils.isEmpty(summaryContents)) {
			summaryContents.forEach(postSummary::addSummaryContents);
		}
	}
}
