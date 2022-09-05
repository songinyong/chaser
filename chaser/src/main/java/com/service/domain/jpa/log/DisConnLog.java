package com.service.domain.jpa.log;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
@Entity
@Table(name="disconnlog")
public class DisConnLog {
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
	private DisConnLog(String ipAddress, String session ) {
		this.ipAddress = ipAddress ;
		this.session = session;
	}
}
