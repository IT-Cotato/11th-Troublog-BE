package troublog.backend.domain.trouble.service.facade.relation.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import troublog.backend.domain.ai.summary.entity.SummaryTask;
import troublog.backend.domain.project.entity.Project;
import troublog.backend.domain.project.service.query.ProjectQueryService;
import troublog.backend.domain.trouble.converter.ContentConverter;
import troublog.backend.domain.trouble.converter.TagConverter;
import troublog.backend.domain.trouble.dto.request.PostReqDto;
import troublog.backend.domain.trouble.dto.request.common.ContentDto;
import troublog.backend.domain.trouble.entity.*;
import troublog.backend.domain.trouble.service.command.ContentCommandService;
import troublog.backend.domain.trouble.service.command.PostTagCommandService;
import troublog.backend.domain.trouble.service.command.TagCommandService;
import troublog.backend.domain.trouble.service.facade.relation.PostRelationFacade;
import troublog.backend.domain.trouble.service.factory.PostFactory;
import troublog.backend.domain.trouble.service.query.PostTagQueryService;
import troublog.backend.domain.trouble.service.query.TagQueryService;
import troublog.backend.domain.user.entity.User;
import troublog.backend.domain.user.service.query.UserQueryService;
import troublog.backend.global.common.util.TagNameFormatter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostRelationFacadeImpl implements PostRelationFacade {

    private final PostFactory postFactory;

    private final PostTagCommandService postTagCommandService;
    private final ContentCommandService contentCommandService;
    private final TagCommandService tagCommandService;

    private final UserQueryService userQueryService;
    private final ProjectQueryService projectQueryService;
    private final PostTagQueryService postTagQueryService;
    private final TagQueryService tagQueryService;

    public void establishRequireRelations(final Post createdPost, final Long userId, final PostReqDto postReqDto) {
        setUserRelations(createdPost, userId);
        setProjectRelations(createdPost, postReqDto.projectId());
        setErrorTagRelations(createdPost, postReqDto.errorTagName());
    }

    public void setUserRelations(final Post post, final Long userId) {
        User user = userQueryService.findUserById(userId);
        user.addPost(post);
    }

    public void setProjectRelations(final Post post, final Long projectId) {
        Project project = projectQueryService.findById(projectId);
        project.addPost(post);
    }

    public void setErrorTagRelations(final Post post, final String tagName) {
        PostTag postTag = saveErrorPostTag(tagName, post);
        post.addPostTag(postTag);
    }

    public void establishSecondaryRelations(final Post post, final PostReqDto postReqDto) {
        setContentRelations(post, postReqDto.contentDtoList());
        setTechStackTagRelations(post, postReqDto.postTags());
    }

    public void setContentRelations(final Post post, final List<ContentDto> contentDtoList) {
        if (!CollectionUtils.isEmpty(contentDtoList)) {
            List<Content> contents = saveAllContent(contentDtoList);
            contents.forEach(post::addContent);
        }
    }

    private void setTechStackTagRelations(final Post post, final List<String> techStackTags) {
        if (!CollectionUtils.isEmpty(techStackTags)) {
            List<PostTag> techStackPostTags = saveTechStackPostTags(techStackTags, post);
            techStackPostTags.forEach(post::addPostTag);
        }
    }

    private PostTag saveErrorPostTag(final String tagName, final Post post) {
        Tag errorTag = tagQueryService.findErrorTagByName(tagName);
        PostTag postTag = postFactory.createPostTag(errorTag, post, errorTag.getName());
        return postTagCommandService.save(postTag);
    }

    private List<Content> saveAllContent(final List<ContentDto> contentDtoList) {
        List<Content> newContentList = contentDtoList.stream()
                .map(ContentConverter::toEntity)
                .toList();
        return contentCommandService.saveAll(newContentList);
    }

    public void updateRelationsIfChanged(final PostReqDto postReqDto, final Post foundPost) {
        updateCommonInfo(postReqDto, foundPost);
        updateProjectRelations(foundPost, postReqDto.projectId());
        updateContentRelations(foundPost, postReqDto.contentDtoList());
        updatePostTagRelations(foundPost, postReqDto.errorTagName(), postReqDto.postTags());
    }

    private void updateCommonInfo(final PostReqDto postReqDto, final Post foundPost) {
        foundPost.updateCommonInfo(postReqDto);
    }

    private void updateProjectRelations(final Post post, final Long newProjectId) {
        if (post.getProject().getId().equals(newProjectId)) {
            return;
        }
        setProjectRelations(post, newProjectId);
    }

    private void updateContentRelations(final Post post, final List<ContentDto> contentDtoList) {
        post.getContents().clear();
        setContentRelations(post, contentDtoList);
    }

    private void updatePostTagRelations(final Post post, final String errorTagName, final List<String> techStackTags) {
        post.getPostTags().clear();
        deleteAllTagByPostId(post.getId());
        setErrorTagRelations(post, errorTagName);
        setTechStackTagRelations(post, techStackTags);
    }

    private List<PostTag> saveTechStackPostTags(final List<String> displayNames, final Post post) {
        List<String> normalizedNames = TagNameFormatter.toNormalizedNames(displayNames);
        Map<String, Tag> tagNameMap = matchWithTagName(normalizedNames);

        List<PostTag> postTags = tagNameMap.entrySet().stream()
                .map(entry -> postFactory.createPostTag(entry.getValue(), post, entry.getKey()))
                .toList();

        return postTagCommandService.saveAll(postTags);
    }

    private Map<String, Tag> matchWithTagName(final List<String> normalizedNames) {
        return normalizedNames.stream()
                .collect(Collectors.toMap(
                        name -> name,
                        this::findOrCreateTechStackTag
                ));
    }

    private Tag findOrCreateTechStackTag(final String normalizedName) {
        return tagQueryService.findTechStackTagByName(normalizedName)
                .orElseGet(() -> {
                    Tag newTag = TagConverter.toEntity(TagNameFormatter.toCamelCaseName(normalizedName), normalizedName);
                    return tagCommandService.save(newTag);
                });
    }

    private void deleteAllTagByPostId(final Long postId) {
        List<PostTag> postTags = postTagQueryService.findAllByPostId(postId);
        postTagCommandService.deleteAll(postTags);
    }

    public void setPostSummaryRelation(final Post post, final PostSummary postSummary, final SummaryTask summaryTask) {
        summaryTask.updatePostSummaryId(postSummary.getId());
        post.addPostSummary(postSummary);
        post.registerAsSummarized();
    }
}