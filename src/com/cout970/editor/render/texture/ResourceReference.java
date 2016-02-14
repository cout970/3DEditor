package com.cout970.editor.render.texture;

import com.cout970.editor.Editor;

import java.io.File;

public class ResourceReference {

	private String domain;
	private String path;
	private String fileName;
	
	public ResourceReference(String domain, String path, String fileName){
		this.domain = domain;
		this.path = path;
		this.fileName = fileName;
	}
	
	public ResourceReference(String domain, String path){
		this.domain = domain;
		this.path = path;
	}
	
	public ResourceReference(String path){
		this.domain = Editor.EDITOR_NAME.toLowerCase();
		this.path = path;
	}
	
	public String getDomain(){
		return domain;
	}
	
	public String getPath(){
		return path;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public ResourceReference setFileName(String name){
		fileName = name;
		return this;
	}

	public String toString(){
		return domain+":"+path;
	}

	public File getFile() {
		return new File(getCompletePath());
	}

	public String getCompletePath() {
		return "./domains/"+domain+"/"+path;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((domain == null) ? 0 : domain.hashCode());
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResourceReference other = (ResourceReference) obj;
		if (domain == null) {
			if (other.domain != null)
				return false;
		} else if (!domain.equals(other.domain))
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}
	
	
}
