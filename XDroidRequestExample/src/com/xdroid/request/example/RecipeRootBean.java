package com.xdroid.request.example;

import java.io.Serializable;
/**
 * 测试用 食谱实体类
 * @author Robin
 * @since 2015-11-27 11:45:55
 *
 */
import java.util.List;
public class RecipeRootBean<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String status;
	private List<T> tngou;

	public static class RecipeBean implements Serializable{
		private static final long serialVersionUID = 1L;
		
		private String count;
		private String description;
		private String fcount;
		private String food;
		private String id;
		private String images;
		private String img;
		private String keywords;
		private String message;
		private String name;
		private String rcount;

		public String getCount() {
			return count;
		}

		public void setCount(String count) {
			this.count = count;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getFcount() {
			return fcount;
		}

		public void setFcount(String fcount) {
			this.fcount = fcount;
		}

		public String getFood() {
			return food;
		}

		public void setFood(String food) {
			this.food = food;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getImages() {
			return images;
		}

		public void setImages(String images) {
			this.images = images;
		}

		public String getImg() {
			return img;
		}

		public void setImg(String img) {
			this.img = img;
		}

		public String getKeywords() {
			return keywords;
		}

		public void setKeywords(String keywords) {
			this.keywords = keywords;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getRcount() {
			return rcount;
		}

		public void setRcount(String rcount) {
			this.rcount = rcount;
		}

		@Override
		public String toString() {
			return "CategoryRootBean [count=" + count + ", description=" + description + ", fcount=" + fcount + ", food="
					+ food + ", id=" + id + ", images=" + images + ", img=" + img + ", keywords=" + keywords + ", message="
					+ message + ", name=" + name + ", rcount=" + rcount + "]";
		}
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<T> getTngou() {
		return tngou;
	}

	public void setTngou(List<T> tngou) {
		this.tngou = tngou;
	}

	@Override
	public String toString() {
		return "RecipeRootBean [status=" + status + ", tngou=" + tngou + "]";
	}
	

}
