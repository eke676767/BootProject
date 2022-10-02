package com.boot.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



import org.springframework.stereotype.Service;

import com.boot.dto.BoardRequestDto;
import com.boot.dto.BoardResponseDto;
import com.boot.entity.Board;
import com.boot.entity.BoardRepository;
import com.boot.exception.CustomException;
import com.boot.exception.ErrorCode;
import com.boot.paging.CommonParams;
import com.boot.paging.Pagination;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class BoardService {
	
	private final BoardRepository boardRepository;
	private final BoardMapper boardMapper;
	
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
    
    //게시글 리스트 조회 - with pagination,, infromaigion
    public Map<String, Object> findAll(CommonParams params){

    	//게시글 수 조회
    	int count = boardMapper.count(params);
    	
    	//등록된 게시글이 없는 경우, 로직 종료
    	if(count < 1) {
    		return Collections.emptyMap();
    	}
    	
    	//페이지네이션 정보 계산
    	Pagination pagination = new Pagination(count, params);
    	params.setPagination(pagination);
    	
    	//게시글 리스트 조회
    	List<BoardResponseDto> list = boardMapper.findAll(params);
    	
    	//데이터 변환
    	Map<String, Object> response = new HashMap<>();
    	response.put("params", params);
    	response.put("list", list);
    	return response;
    }
}
