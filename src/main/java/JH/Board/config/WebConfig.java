package JH.Board.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private String resourcePath = "/upload/**"; // view 에서 접근할 경로
    private String savePath = "file:///C:/springboot_img/"; // 실제 파일 저장 경로(win)
//    private String savePath = "file:///Users/사용자이름/springboot_img/"; // 실제 파일 저장 경로(mac)

    //동작 방식
    //클라이언트가 /images/some-image.png와 같은 URL로 요청을 보냅니다.
    //Spring은 /images/** 패턴을 C:/myapp/images/ 디렉토리와 매핑합니다.
    //C:/myapp/images/some-image.png 파일을 찾아 클라이언트에게 반환합니다.
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(resourcePath)
                .addResourceLocations(savePath);
    }

}