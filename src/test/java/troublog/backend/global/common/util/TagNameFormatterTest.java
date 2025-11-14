package troublog.backend.global.common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("트러블슈팅 문서 태그 네이밍 변환 테스트")
class TagNameFormatterTest {

    @DisplayName("표시용 태그 이름들을 정규화된 이름으로 변환한다")
    @ParameterizedTest
    @MethodSource("provideDisplayToNormalizedNames")
    void 표시용_태그_이름들을_정규화된_이름으로_변환한다(List<String> displayNames, List<String> expected) {
        // when
        List<String> result = TagNameFormatter.toNormalizedNames(displayNames);

        // then
        assertThat(result).containsExactlyElementsOf(expected);
    }

    @DisplayName("단일 태그 이름을 카멜케이스로 변환한다")
    @ParameterizedTest
    @MethodSource("provideCamelCaseNames")
    void 단일_태그_이름을_카멜케이스로_변환한다(String input, String expected) {
        // when
        String result = TagNameFormatter.toCamelCaseName(input);

        // then
        assertThat(result).isEqualTo(expected);
    }


    @Test
    @DisplayName("toNormalizedNames - 빈 리스트를 전달하면 빈 리스트를 반환한다")
    void toNormalizedNames_빈_리스트를_전달하면_빈_리스트를_반환한다() {
        // given
        List<String> emptyList = List.of();

        // when
        List<String> result = TagNameFormatter.toNormalizedNames(emptyList);

        // then
        assertThat(result).isEmpty();
    }


    @Test
    @DisplayName("toNormalizedNames - null 리스트를 전달하면 빈 List를 반환한다")
    void toNormalizedNames_null_리스트를_전달하면_빈_List를_반환한다() {
        // when
        List<String> result = TagNameFormatter.toNormalizedNames(null);

        // then
        assertThat(result).isEqualTo(List.of());
    }

    private static Stream<Arguments> provideDisplayToNormalizedNames() {
        return Stream.of(
                Arguments.of(List.of("Spring Boot", "Java Script", "React"), List.of("spring-boot", "java-script", "react")),
                Arguments.of(List.of("Type Script", "Node Js"), List.of("type-script", "node-js")),
                Arguments.of(List.of("Java"), List.of("java"))
        );
    }

    private static Stream<Arguments> provideCamelCaseNames() {
        return Stream.of(
                Arguments.of("spring-boot", "SpringBoot"),
                Arguments.of("java-script", "JavaScript"),
                Arguments.of("react", "React"),
                Arguments.of("node-js", "NodeJs"),
                Arguments.of("type-script", "TypeScript"),
                Arguments.of("java", "Java")
        );
    }
}
