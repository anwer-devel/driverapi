package espita.client1.proj1.model;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;

public class PDFResponse {
	
	private long id;
	private String email;
	private String phone;
	private String fileDownloadUrl;
	  private String fileViewUrl;
	  private String driveFileId;
	  
	  public PDFResponse( String email, String phone, String fileViewUrl, String driveFileId,
				String text) {
			super();
			this.email = email;
			this.phone = phone;
			this.fileViewUrl = fileViewUrl;
			this.driveFileId = driveFileId;
			this.text = text;
		}

	public PDFResponse(long id, String email, String phone, String fileDownloadUrl, String fileViewUrl, String driveFileId,
			String text) {
		super();
		this.id = id;
		this.email = email;
		this.phone = phone;
		this.fileDownloadUrl = fileDownloadUrl;
		this.fileViewUrl = fileViewUrl;
		this.driveFileId = driveFileId;
		this.text = text;
	}

	public String getDriveFileId() {
		return driveFileId;
	}

	public void setDriveFileId(String driveFileId) {
		this.driveFileId = driveFileId;
	}

	@Lob
	 @Column(name = "large_text_field", columnDefinition = "LONGTEXT")
	private String text;
	
	public String getFileDownloadUrl() {
		return fileDownloadUrl;
	}

	public void setFileDownloadUrl(String fileDownloadUrl) {
		this.fileDownloadUrl = fileDownloadUrl;
	}

	public String getFileViewUrl() {
		return fileViewUrl;
	}

	public void setFileViewUrl(String fileViewUrl) {
		this.fileViewUrl = fileViewUrl;
	}

	

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public PDFResponse() {
	}

	

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}



