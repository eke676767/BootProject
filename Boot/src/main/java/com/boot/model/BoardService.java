package com.boot.model;

import java.util.List;
import java.util.stream.Collectors;



import org.springframework.stereotype.Service;

import com.boot.dto.BoardRequestDto;
import com.boot.dto.BoardResponseDto;
import com.boot.entity.Board;
import com.boot.entity.BoardRepository;
import com.boot.exception.CustomException;
import com.boot.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class BoardService {
	
	private final BoardRepository boardRepository;
	
	//게시글 저장
	@Transactional
	public Long save(final BoardRequestDto params) {
		Board entity = boardRepository.save(params.toEndtity());
		return entity.getId();
	}
	
	//게시글 리스트 조회
	public List<BoardResponseDto> findAll(){
		Sort sort = Sort.by(Direction.DESC, "id", "createdDate");
		List<Board>list = boardRepository.findAll(sort);
		return list.stream().map(BoardResponseDto::new).collect(Collectors.toList());
	}
	
	/*  Stream API를 사용하지 않은 경우 
	public List<BoardResponseDto> findAll() {

	    Sort sort = Sort.by(Direction.DESC, "id", "createdDate");
	    List<Board> list = boardRepository.findAll(sort);
	    
	
	    List<BoardResponseDto> boardList = new ArrayList<>();
	    
	    for (Board entity : list) {
	        boardList.add(new BoardResponseDto(entity));
	    }
	    
	    return boardList;
	}
	*/
	
	//게시글 리스트 조회 (삭제 여부 기준)
	public List<BoardResponseDto> findAllByDeleteYn(final char deleteYn){
	    Sort sort = Sort.by(Direction.DESC, "id", "createdDate");
	    List<Board> list = boardRepository.findAllByDeleteYn(deleteYn, sort);
	    return list.stream().map(BoardResponseDto::new).collect(Collectors.toList());
	}
	
	
	//게시글 업데이트
    @Transactional
    public Long update(final Long id, final BoardRequestDto params) {

        Board entity = boardRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
        entity.update(params.getTitle(), params.getContent(), params.getWriter());
        return id;
    }
    
    /*
      @Transactional
		public Long update(final Long id, final BoardRequestDto params) {
		
		    Board entity = boardRepository.findById(id).orElse(null);
		
		    if (entity == null) {
		        throw new CustomException(ErrorCode.POSTS_NOT_FOUND);
		    }
		
		    entity.update(params.getTitle(), params.getContent(), params.getWriter());
		    return id;
		}
     */
    
    //게시글 삭제
    @Transactional
    public Long delete(final Long id) {
    	Board entity = boardRepository.findById(id).orElseThrow(()-> new CustomException(ErrorCode.POSTS_NOT_FOUND));
    	entity.delete();
    	return id;
    }
    
    //게시글 상세정보 조회
    @Transactional
    public BoardResponseDto findById(final Long id) {
        Board entity = boardRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
        entity.increaseHits();
        return new BoardResponseDto(entity);
    }
}
