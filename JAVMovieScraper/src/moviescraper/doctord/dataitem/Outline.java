package moviescraper.doctord.dataitem;

public class Outline extends MovieDataItem {

	private String outline;

	public String getOutline() {
		return outline;
	}

	public void setOutline(String outline) {
		this.outline = sanitizeString(outline);
	}

	@Override
	public String toString() {
		return "Outline [outline=" + outline + "]";
	}

	public Outline(String outline) {
		super();
		setOutline(outline);
	}

	@Override
	public String toXML() {
		// TODO Auto-generated method stub
		return null;
	}

}
