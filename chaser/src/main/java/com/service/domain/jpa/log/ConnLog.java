/*
 * 서버 접속할때의 로그인 로그
 * */

package com.service.domain.jpa.log;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;

import com.service.domain.jpa.CreateTimeEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@Entity
@Table(name="connlog")
public class ConnLog extends CreateTimeEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id ;
	@Column()
	String ipAddress ;
	
	@Column()
	private String session ;
	
	@CreatedDate
	private LocalDateTime createdDate;	

	
	@Builder()
	private ConnLog(String ipAddress, String session ) {
		this.ipAddress = ipAddress ;
		this.session = session;
	}
}
