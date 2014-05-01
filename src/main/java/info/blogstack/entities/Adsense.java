package info.blogstack.entities;

import info.blogstack.misc.Globals;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "adsense", schema = Globals.SCHEMA)
public class Adsense implements Serializable {

	private static final long serialVersionUID = 9016075842562971963L;

	public static final Adsense BLOGSTACK;
	
	static {
		BLOGSTACK = new Adsense();
		BLOGSTACK.setAdsenseAdClient("ca-pub-3533636310991304");
		BLOGSTACK.setAdsenseSlotBigRectangle("2873752587");
		BLOGSTACK.setAdsenseSlotWideSkycraper("1896546988");
		BLOGSTACK.setAdsenseSlotHorizontalSkycraper("1397019380");
	}
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Basic
	@NotNull
	private String adsenseAdClient;
	
	@Basic
	private String adsenseSlotBigRectangle;
	
	@Basic
	private String adsenseSlotWideSkycraper;
	
	@Basic
	private String adsenseSlotHorizontalSkycraper;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getAdsenseAdClient() {
		return adsenseAdClient;
	}

	public void setAdsenseAdClient(String adsenseAdClient) {
		this.adsenseAdClient = adsenseAdClient;
	}

	public String getAdsenseSlotBigRectangle() {
		return adsenseSlotBigRectangle;
	}

	public void setAdsenseSlotBigRectangle(String adsenseSlotBigRectangle) {
		this.adsenseSlotBigRectangle = adsenseSlotBigRectangle;
	}

	public String getAdsenseSlotWideSkycraper() {
		return adsenseSlotWideSkycraper;
	}

	public void setAdsenseSlotWideSkycraper(String adsenseSlotWideSkycraper) {
		this.adsenseSlotWideSkycraper = adsenseSlotWideSkycraper;
	}

	public String getAdsenseSlotHorizontalSkycraper() {
		return adsenseSlotHorizontalSkycraper;
	}

	public void setAdsenseSlotHorizontalSkycraper(String adsenseSlotHorizontalSkycraper) {
		this.adsenseSlotHorizontalSkycraper = adsenseSlotHorizontalSkycraper;
	}
}