package espita.client1.proj1.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;

@Entity
public class CV {
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    
	    @Column(name = "filename")
	    private String filename;
	    @Column(name = "titre")
	    private String titre;
	    
	    @Column(name = "text")
	    private String text;
	    
	    @Column(name = "share_link")
	    private String shareLink;
	    
	    @Column(name = "download_link")
	    private String downloadLink;
	    
	    @Column(name = "thumbnail_link")
	    private String thumbnailLink;
	    
	    @Column(name = "phone")
	    private String phone;
	    
	    @Column(name = "email")
	    private String email;

	    private String folder;
	    private String subfolder;
	    private String subsubfolder;
	    
	    @OneToMany(mappedBy = "cv", cascade = CascadeType.ALL)
	    private List<CVSkills> skills;
	    
	  

		public CV() {}

		public CV(Long id, String filename, String text,String email,String phone, String shareLink, 
				String downloadLink, String thumbnailLink,String titre) {
			super();
			this.id = id;
			this.filename = filename;
			this.text = text;
			this.shareLink = shareLink;
			this.downloadLink = downloadLink;
			this.thumbnailLink = thumbnailLink;
			this.email =email;
			this.phone =phone ;
			this.titre=titre;
		}

		public String getTitre() {
			return titre;
		}

		public void setTitre(String titre) {
			this.titre = titre;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getFilename() {
			return filename;
		}

		public void setFilename(String filename) {
			this.filename = filename;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public String getShareLink() {
			return shareLink;
		}

		public void setShareLink(String shareLink) {
			this.shareLink = shareLink;
		}

		public String getDownloadLink() {
			return downloadLink;
		}

		public void setDownloadLink(String downloadLink) {
			this.downloadLink = downloadLink;
		}

		public String getThumbnailLink() {
			return thumbnailLink;
		}

		public void setThumbnailLink(String thumbnailLink) {
			this.thumbnailLink = thumbnailLink;
		}
	    
	    
		  
	    public List<CVSkills> getSkills() {
			return skills;
		}

		public void setSkills(List<CVSkills> skills) {
			this.skills = skills;
		}

		public CV(Long id, String filename, String titre, String text, String shareLink, String downloadLink,
				String thumbnailLink, String phone, String email, String folder, String subfolder, String subsubfolder,
				List<CVSkills> skills) {
			super();
			this.id = id;
			this.filename = filename;
			this.titre = titre;
			this.text = text;
			this.shareLink = shareLink;
			this.downloadLink = downloadLink;
			this.thumbnailLink = thumbnailLink;
			this.phone = phone;
			this.email = email;
			this.folder = folder;
			this.subfolder = subfolder;
			this.subsubfolder = subsubfolder;
			this.skills = skills;
		}

		public String getFolder() {
			return folder;
		}

		public void setFolder(String folder) {
			this.folder = folder;
		}

		public String getSubfolder() {
			return subfolder;
		}

		public void setSubfolder(String subfolder) {
			this.subfolder = subfolder;
		}

		public String getSubsubfolder() {
			return subsubfolder;
		}

		public void setSubsubfolder(String subsubfolder) {
			this.subsubfolder = subsubfolder;
		}

		

}