package JH.Board.service;

import JH.Board.dto.BoardDTO;
import JH.Board.entity.BoardEntity;
import JH.Board.entity.BoardFileEntity;
import JH.Board.repository.BoardFileRepository;
import JH.Board.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//서비스 클래스에서 하는것들
//컨트롤러에서 dto로 파일을 받음 인설트 이런거는
//DTO --> ENTITY (Entity Class)에서 할거고 만들어서 repository 주면됨
//컨트롤러에게 반환해주어야 할때는
//ENTITY --> DTO (DTO Class)

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;

    public void save(BoardDTO boardDTO) throws IOException {
        //파일 첨부에 따라 로직 분리
        if(boardDTO.getBoardFile().isEmpty()) {
            //첨부파일없음
            BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO); //entity로 리턴을 주기로 되어있음
            boardRepository.save(boardEntity);
        }else{
            // 첨부 파일 있음.
            /*
                1. DTO에 담긴 파일을 꺼냄
                2. 파일의 이름 가져옴
                3. 서버 저장용 이름을 만듦
                // 내사진.jpg => 839798375892_내사진.jpg
                4. 저장 경로 설정
                5. 해당 경로에 파일 저장
                6. board_table에 해당 데이터 save 처리
                7. board_file_table에 해당 데이터 save 처리
             */
            BoardEntity boardEntity = BoardEntity.toSaveFileEntity(boardDTO);
            Long savedId = boardRepository.save(boardEntity).getId(); //자식은 부모가 어떤 아이디인지 알기위해
            BoardEntity board = boardRepository.findById(savedId).get();
            for(MultipartFile boardFile: boardDTO.getBoardFile()){
//                MultipartFile boardFile = boardDTO.getBoardFile();
                String originalFilename = boardFile.getOriginalFilename();
                String storedFileName = System.currentTimeMillis() + "_" + originalFilename;
                String savePath = "C:/springboot_img/" + storedFileName;
                boardFile.transferTo(new File(savePath));

                BoardFileEntity boardFileEntity = BoardFileEntity.toBoardFileEntity(board,originalFilename,storedFileName);
                boardFileRepository.save(boardFileEntity);
            }
        }
    }

    @Transactional
    public List<BoardDTO> findAll() {
        List<BoardEntity> boardEntityList = boardRepository.findAll();
        List<BoardDTO> boardDTOList = new ArrayList<>();

        for (BoardEntity boardEntity : boardEntityList) { //리스트를 하나씩 뽑기위해 boardEntity 객체를 만들어 사용함
            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));
        }

        return boardDTOList;
    }

    @Transactional
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }

    @Transactional
    public BoardDTO findById(Long id) {
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
        if (optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();
            return BoardDTO.toBoardDTO(boardEntity);
        }else {
            return null;
        }
    }

    public BoardDTO update(BoardDTO boardDTO) {
        BoardEntity boardEntity = BoardEntity.toUpdateEntity(boardDTO);
        boardRepository.save(boardEntity);
        return findById(boardDTO.getId());

    }

    public void delete(Long id) {
        boardRepository.deleteById(id);

    }

    public Page<BoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1; // 페이지 0부터 시작해서 -1 해줘야함
        int pageLimit = 3; // 한페이지에 보여줄 글 갯수
        //한 페이지당 3개씩 글을 보여주고 정렬 기준은 id 기준으로 내림차순 정렬
        Page<BoardEntity> boardEntities
                = boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC,"id")));

        System.out.println("boardEntities.getContent() = " + boardEntities.getContent()); // 요청 페이지에 해당하는 글
        System.out.println("boardEntities.getTotalElements() = " + boardEntities.getTotalElements()); // 전체 글갯수
        System.out.println("boardEntities.getNumber() = " + boardEntities.getNumber()); // DB로 요청한 페이지 번호
        System.out.println("boardEntities.getTotalPages() = " + boardEntities.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardEntities.getSize() = " + boardEntities.getSize()); // 한 페이지에 보여지는 글 갯수
        System.out.println("boardEntities.hasPrevious() = " + boardEntities.hasPrevious()); // 이전 페이지 존재 여부
        System.out.println("boardEntities.isFirst() = " + boardEntities.isFirst()); // 첫 페이지 여부
        System.out.println("boardEntities.isLast() = " + boardEntities.isLast()); // 마지막 페이지 여부

        //MAP은 board 변수를 하나씩 꺼내서 DTO 객체에 연결해줌
        //목록 : ID, WRITER , TITLE , HITS, createdTime
        Page<BoardDTO> boardDTOS = boardEntities.map(board -> new BoardDTO(
                                                board.getId(),
                                                board.getBoardWriter(),
                                                board.getBoardTitle(),
                                                board.getBoardHits(),
                                                board.getCreatedTime()));

        return boardDTOS;
    }
}
