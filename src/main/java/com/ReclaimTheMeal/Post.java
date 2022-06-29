package com.ReclaimTheMeal;

import java.util.*;

import javax.persistence.*;

@Entity
@Table(name="posts")
public class Post {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true, length = 45)
	private String title;
	
	@Column(nullable = false, length = 120)
	private String description;
	
	@Column(name = "start_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date start_time;
	
	@Column(name = "end_time")
		@Temporal(TemporalType.TIMESTAMP)
		private Date end_time;

    @Column(nullable = false)
    private Boolean state;

    @Column(nullable = false, length = 45)
    private String location;

    @Transient
    private String time_start;

    @Transient
    private String time_end;

	
	public Long getId() {
		return id;
	}
	public void setId(Long i) {
		this.id = i;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
    public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getStartTime() {
		return start_time;
	}
	public void setStartTime(Date start_time) {
		this.start_time = start_time;
	}
	public Date getEndTime() {
		return end_time;
	}
	public void setEndTime(Date end_time) {
		this.end_time = end_time;
	}
    public Boolean getState() {
		return state;
	}
	public void setState(Boolean state) {
		this.state = state;
	}
    public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
    public String getTimeStart() {
		return time_start;
	}
	public void setTimeStart(String time_start) {
		this.time_start = time_start;
	}
    public String getTimeEnd() {
		return time_end;
	}
	public void setTimeEnd(String time_end) {
		this.time_end = time_end;
	}
	@ManyToOne(fetch=FetchType.EAGER)  
    @JoinColumn(name = "user_id")
    private User user;
    public User getUser()   
    {  
        return user;  
    }    
    public void setUser(User user)   
    {  
        this.user = user;  
    }    
}
