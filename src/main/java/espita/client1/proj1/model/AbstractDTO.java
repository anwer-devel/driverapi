package espita.client1.proj1.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractDTO <T>{
    private String id;
    private String name;
    private String link;
    
    public String getId() {
    	return id;
    }
    public void setId(String id) {
    	this.id = id;
    }
    
    public String getName() {
    	return name;
    }
    public void setName(String name) {
    	this.name = name;
    }
    
    public String getLink() {
    	return link;
    }
    public void setLink(String link) {
    	this.link = link;
    }
}

