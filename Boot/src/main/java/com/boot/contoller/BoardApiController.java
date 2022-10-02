package com.boot.contoller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.boot.dto.BoardRequestDto;
import com.boot.dto.BoardResponseDto;
import com.boot.exception.CustomException;
import com.boot.exception.ErrorCode;
import com.boot.model.BoardService;
import com.boot.paging.CommonParams;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardApiController {
	
	private final BoardService boardService;
	
	//게시글 생성
	@PostMapping("/boards")
	public Long save(@RequestBody final BoardRequestDto params) {
		return boardService.save(params);
	}
	
	//게시글 리스트 조회
	@GetMapping("/boards")
	public Map<String, Object> findAll(final CommonParams params){
		return boardService.findAll(params);
	}
	
	//게시글 수정
	@PatchMapping("/boards/{id}")
	public Long save(@PathVariable final Long id, @RequestBody final BoardRequestDto params) {
		return boardService.update(id, params);
	}
	
	//게시글 삭제
	@DeleteMapping("/boards/{id}")
	public Long delete(@PathVariable final Long id) {
		return boardService.delete(id);
	}
	
    /**
     * 게시글 상세정보 조회
     */
    @GetMapping("/boards/{id}")
    public BoardResponseDto findById(@PathVariable final Long id) {
        return boardService.findById(id);
    }

	@GetMapping("/test")
	public String test() {
		throw new CustomException(ErrorCode.POSTS_NOT_FOUND);
	}

}
