package com.hdw.common.swagger2;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import com.hdw.common.constant.CommonConstant;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.configuration.Swagger2DocumentationConfiguration;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description Swagger2配置
 * @Author TuMingLong
 * @Date 2019/11/1 11:57
 */
@Slf4j
@Configuration
@EnableSwaggerBootstrapUI
@ConditionalOnProperty(prefix = "hdw.swagger2", name = "enabled", havingValue = "true")
@Import({Swagger2DocumentationConfiguration.class})
public class Swagger2Config {

    @Resource
    private Swagger2Properties swagger2Properties;

    public Swagger2Config() {
        log.debug("swagger2 [{}]", swagger2Properties);
    }

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(setHeaderToken());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 文档标题
                .title(swagger2Properties.getTitle())
                // 文档描述
                .description(swagger2Properties.getDescription())
                .version(swagger2Properties.getVersion())
                .license("MIT 协议")
                .licenseUrl("http://www.opensource.org/licenses/MIT")
                .build();
    }

    private List<Parameter> setHeaderToken() {
        List<Parameter> pars = new ArrayList<>();

        // token请求头
        String testTokenValue = "";
        ParameterBuilder tokenPar = new ParameterBuilder();
        Parameter tokenParameter = tokenPar
                .name(CommonConstant.JWT_DEFAULT_TOKEN_NAME)
                .description("Token Request Header")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .defaultValue(testTokenValue)
                .build();
        pars.add(tokenParameter);
        return pars;
    }

}
