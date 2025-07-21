package troublog.backend.domain.trouble.service.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.trouble.entity.TechStack;
import troublog.backend.domain.trouble.repository.TechStackRepository;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.PostException;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TechStackQueryService {

    private final TechStackRepository techStackRepository;

    public TechStack findTechStackByName(String name) {
        return techStackRepository.findByName(name)
            .orElseThrow(() -> new PostException(ErrorCode.INVALID_VALUE));
    }
}