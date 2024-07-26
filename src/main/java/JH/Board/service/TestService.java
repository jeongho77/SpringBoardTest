package JH.Board.service;


import JH.Board.repository.BoardFileRepository;
import JH.Board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class TestService {
    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;


    public void getNodeList() {
        boardRepository.findAll();
        return "list";
    }
}




























