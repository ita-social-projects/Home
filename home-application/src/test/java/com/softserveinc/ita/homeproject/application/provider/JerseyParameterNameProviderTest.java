package com.softserveinc.ita.homeproject.application.provider;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

import java.lang.annotation.Annotation;
import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class JerseyParameterNameProviderTest {

    public static final String QUERY_PARAM = "query param";

    public static final String PATH_PARAM = "path param";

    public static final String HEADER_PARAM = "header param";

    public static final String COOKIE_PARAM = "cookie param";

    public static final String FORM_PARAM = "form param";

    public static final String CONTEXT = "context";

    public static final String MATRIX_PARAM = "matrix param";

    @ParameterizedTest
    @ValueSource(strings = {QUERY_PARAM, PATH_PARAM, HEADER_PARAM, COOKIE_PARAM, FORM_PARAM, CONTEXT, MATRIX_PARAM})
    void getParameterNameFromAnnotations(String expected) {
        Annotation annotation = mockAnnotation(expected);
        JerseyParameterNameProvider
            .getParameterNameFromAnnotations(new Annotation[]{annotation})
            .ifPresent(actual -> assertThat(actual).contains(expected));
    }

    private Annotation mockAnnotation(String value) {
        switch (value) {
            case (QUERY_PARAM):
                return mock(Annotation.class, withSettings().extraInterfaces(QueryParam.class));
            case (PATH_PARAM):
                return mock(Annotation.class, withSettings().extraInterfaces(PathParam.class));
            case (HEADER_PARAM):
                return mock(Annotation.class, withSettings().extraInterfaces(HeaderParam.class));
            case (COOKIE_PARAM):
                return mock(Annotation.class, withSettings().extraInterfaces(CookieParam.class));
            case (FORM_PARAM):
                return mock(Annotation.class, withSettings().extraInterfaces(FormParam.class));
            case (CONTEXT):
                return mock(Annotation.class, withSettings().extraInterfaces(Context.class));
            case (MATRIX_PARAM):
                return mock(Annotation.class, withSettings().extraInterfaces(MatrixParam.class));
            default:
                return null;
        }
    }
}
