package JH.Board.Controller;


import JH.Board.dto.BoardDTO;
import JH.Board.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")

public class TestController {

    private final TestService testService;

    //게시판 리스트 조회
    @GetMapping("/good-ingredient")
    public List<BoardDTO> apiresponse() {
        testService.getNodeList()

    }

}
