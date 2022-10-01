package com.boot.dto;

import com.boot.entity.Board;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardRequestDto {
	private String title; //제목
	private String content;
	private String writer;
	private char deleteYn;
	
	public Board toEndtity() {
		return Board.builder()
				.title(title)
				.content(content)
				.writer(writer)
				.hits(0)
				.deleteYn(deleteYn)
				.build(); 
	}
	
	
}
