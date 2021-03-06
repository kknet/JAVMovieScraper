package moviescraper.doctord.dataitem;

public class Plot extends MovieDataItem {

	private String plot;

	public String getPlot() {
		return plot;
	}

	public void setPlot(String plot) {
		this.plot = sanitizeString(plot);
	}

	@Override
	public String toString() {
		return "Plot [plot=" + plot + "]";
	}

	public Plot(String plot) {
		super();
		setPlot(plot);
	}

	@Override
	public String toXML() {
		// TODO Auto-generated method stub
		return null;
	}

}
