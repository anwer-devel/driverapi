package espita.client1.proj1.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class GoogleDriveFileDTO extends AbstractDTO<GoogleDriveFileDTO> implements Serializable {

    private String size;
    private String thumbnailLink;
    private boolean shared;
    
    public String getSize() {
    	return size;
    }
    public void setSize(String size) {
    	this.size = size;
    }
    
    public String getThumbnailLink() {
    	return thumbnailLink;
    }
    public void setThumbnailLink(String thumbnailLink) {
    	this.thumbnailLink = thumbnailLink;
    }
    
    public boolean getShared() {
    	return shared;
    }
    public void setShared(boolean shared) {
    	this.shared = shared;
    }
}