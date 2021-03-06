package moviescraper.doctord.SiteParsingProfile;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import moviescraper.doctord.Movie;
import moviescraper.doctord.SearchResult;
import moviescraper.doctord.Thumb;
import moviescraper.doctord.dataitem.Actor;
import moviescraper.doctord.dataitem.Director;
import moviescraper.doctord.dataitem.Genre;
import moviescraper.doctord.dataitem.ID;
import moviescraper.doctord.dataitem.MPAARating;
import moviescraper.doctord.dataitem.OriginalTitle;
import moviescraper.doctord.dataitem.Outline;
import moviescraper.doctord.dataitem.Plot;
import moviescraper.doctord.dataitem.Rating;
import moviescraper.doctord.dataitem.Runtime;
import moviescraper.doctord.dataitem.Set;
import moviescraper.doctord.dataitem.SortTitle;
import moviescraper.doctord.dataitem.Studio;
import moviescraper.doctord.dataitem.Tagline;
import moviescraper.doctord.dataitem.Title;
import moviescraper.doctord.dataitem.Top250;
import moviescraper.doctord.dataitem.Votes;
import moviescraper.doctord.dataitem.Year;

public class JavLibraryParsingProfile extends SiteParsingProfile {

	private String siteLanguageToScrape;
	
	public static final String englishLanguageCode = "en";
	public static final String japaneseLanguageCode = "ja";
	public static final String taiwaneseLanguageCode = "tw";
	public static final String chineseLanguageCode = "cn";
	private static final boolean reverseAsianNameInEnglish = true;
	private String overrideURLJavLibrary;

	public String getOverrideURLJavLibrary() {
		return overrideURLJavLibrary;
	}

	public void setOverrideURLJavLibrary(String overrideURLJavLibrary) {
		this.overrideURLJavLibrary = overrideURLJavLibrary;
	}

	public JavLibraryParsingProfile(Document document) {
		super(document);
		siteLanguageToScrape = "en";
	}

	public JavLibraryParsingProfile() {
		siteLanguageToScrape = "en";
	}
	
	public JavLibraryParsingProfile(Document document, String siteLanguageToScrape)
	{
		super(document);
		this.siteLanguageToScrape = siteLanguageToScrape;
	}
	
	public JavLibraryParsingProfile(String siteLanguageToScrape) {
		this.siteLanguageToScrape = siteLanguageToScrape;
	}

	@Override
	public Title scrapeTitle() {

		Element titleElement = document
				.select("h3.post-title.text a")
				.first();
		//remove the ID number off beginning of the title, if it exists (and it usually always does on JavLibrary)
		if(titleElement != null)
		{
			String titleElementText = titleElement.text().trim();
			titleElementText = titleElementText.substring(StringUtils.indexOf(titleElementText," ")).trim();
			//sometimes this still leaves "- " at the start of the title, so we'll want to get rid of that too
			if(titleElementText.startsWith("- "))
			{
				titleElementText = titleElementText.replaceFirst(Pattern.quote("- "), "");
			}
			return new Title(titleElementText);
		}
		//this shouldn't really ever happen...
		else return new Title("");
	}

	@Override
	public OriginalTitle scrapeOriginalTitle() {
		//Does not have original japanese title, so don't return anything
		return new OriginalTitle("");
	}

	@Override
	public SortTitle scrapeSortTitle() {
		// we don't need any special sort title - that's usually something the
		// user provides
		return new SortTitle("");
	}

	@Override
	public Set scrapeSet() {
		// Site doesn't have any set information
		return new Set("");

	}

	@Override
	public Rating scrapeRating() {
		//JavLibrary uses a decimal value out of 10 for its rating
		Element ratingElement = document
				.select("span.score")
				.first();
		if(ratingElement != null)
		{
			String ratingText = ratingElement.text();
			//Found a match, get rid of surrounding parenthesis and use this as the rating
			if(ratingText.contains("("))
			{
				ratingText = ratingText.substring(1,ratingText.length()-1).trim();
			}
			return new Rating(10,ratingText);
		}
		else return new Rating(0,""); //No rating found on the page
	}

	@Override
	public Year scrapeYear() {
		Element dateElement = document
				.select("div#video_date tr td.header + td.text")
				.first();
		String dateText = dateElement.text();
		//The dateText is in format YYYY-MM-DD and we just want the YYYY part
		if(dateText.length() > 0)
		{
			dateText = dateText.trim().substring(0,4);
			return new Year(dateText);
		}
		else return new Year("");
	}

	@Override
	public Top250 scrapeTop250() {
		// This type of info doesn't exist on JavLibrary
		return new Top250("");
	}

	@Override
	public Votes scrapeVotes() {
		return new Votes("");
	}

	@Override
	public Outline scrapeOutline() {
		return new Outline("");
	}

	@Override
	public Plot scrapePlot() {
		return new Plot("");
	}

	@Override
	public Tagline scrapeTagline() {
		return new Tagline("");
	}

	@Override
	public Runtime scrapeRuntime() {
		Element lengthElement = document
				.select("div#video_length tr td.header + td span.text")
				.first();
		String lengthText = lengthElement.text();
		if(lengthText.length() > 0)
		{
			return new moviescraper.doctord.dataitem.Runtime(lengthText);
		}
		else return new moviescraper.doctord.dataitem.Runtime("");
	}

	@Override
	public Thumb[] scrapePosters() {
		return scrapePostersAndFanart(true);
	}

	@Override
	public Thumb[] scrapeFanart() {
		return scrapePostersAndFanart(false);
	}

	private Thumb[] scrapePostersAndFanart(boolean doCrop) {
		Element posterElement = document
				.select("img#video_jacket_img")
				.first();
		Thumb[] posterThumbs = new Thumb[1];
		if(posterElement != null)
		{
			String posterLink = posterElement.attr("src").trim();
			try{
				if (doCrop)
					//posterThumbs[0] = new Thumb(posterLink, 52.7, 0, 0, 0);
					posterThumbs[0] = new Thumb(posterLink, true);
				else
					posterThumbs[0] = new Thumb(posterLink);
				return posterThumbs;
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new Thumb[0];
			}
		}
		else return new Thumb[0];
	}

	@Override
	public MPAARating scrapeMPAA() {
		return new MPAARating("XXX");
	}

	@Override
	public ID scrapeID() {
		Element idElement = document
				.select("div#video_id tr td.header + td.text")
				.first();
		String idText = idElement.text();
		if(idText.length() > 0)
		{
			return new ID(idText);
		}
		else return new ID("");
	}

	@Override
	public ArrayList<Genre> scrapeGenres() {
		Elements genreElements = document
				.select(".genre");
		ArrayList<Genre> genreList = new ArrayList<Genre>(genreElements.size());
		for (Element genreElement : genreElements)
		{
			String currentGenreText = genreElement.text().trim();
			//Sometimes javlibrary has junk genres like video sample. It's not really a genre, so get rid of it!
			if(acceptGenreText(currentGenreText))
				genreList.add(new Genre(currentGenreText));
		}
		return genreList;
	}
	
	private boolean acceptGenreText(String genreText){
		switch(genreText)
		{
		case "Video Sample":
			return false;
		case "Blu-ray":
			return false;
		case "With Gifts":
			return false;
		}
			
		return true;
	}

	@Override
	public ArrayList<Actor> scrapeActors() {
		Elements castElements = document
				.select("span.cast");
		ArrayList<Actor> actorList = new ArrayList<Actor>(castElements.size());
		for (Element castElement : castElements) {
			String actressName = castElement.select("span.star a").text().trim();
			Elements aliasElements = castElement.select("span.alias");
			String [] aliasNames = new String[aliasElements.size()];
			int i = 0; //index of loop iteration
			for(Element aliasElement : aliasElements)
			{
				String currentAlias = aliasElement.text().trim();
				//we might need to reverse the alias name from lastname, firstname to firstname lastname, if we're scraping in english and
				//we specify in options
				if(reverseAsianNameInEnglish && siteLanguageToScrape == englishLanguageCode && currentAlias.contains(" "))
					currentAlias = StringUtils.reverseDelimited(currentAlias, ' ');
				aliasNames[i] = currentAlias;
				i++;
			}
			//String aliasName = castElement.select("span.alias").text().trim();
			
			//JavLibrary has asian names in Lastname, first format. Reverse it, if we specify it with the option to do so
			//but only do this if we're scraping in english
			if(reverseAsianNameInEnglish && siteLanguageToScrape == englishLanguageCode && actressName.contains(" "))
			{
				actressName = StringUtils.reverseDelimited(actressName, ' ');
				
			}
			/*if(reverseAsianNameInEnglish && siteLanguageToScrape == englishLanguageCode && aliasName.contains(" "))
			aliasName = StringUtils.reverseDelimited(aliasName, ' ');
			
			if(aliasName.length() > 0)
				actressName += " (" + aliasName + ")";*/
			if(aliasNames.length > 0)
			{
				for(int j = 0; j < aliasNames.length; j++)
				{
					actressName = actressName + " (" + aliasNames[j] + ")";
				}
			}
			actorList.add(new Actor(actressName,"",null));
		}
		
		//If we got some actors, let's try to get the thumbnails of the actors from DMM.co.jp
		if(actorList.size() > 0)
		{
			/*JavLibrary Doesn't have thumbnail images for the actors, so we're going to cross-reference the
			 * list of actors from DMM.co.jp and JavLibrary's japanese language list of actors
			 * If an actor has the same name in these two lists, we'll replace our current actor list's thumbnail
			 * with the same one from DMM
			 */

			//make sure the ID has been scraped, because we need it for the search string
			ID movieID = scrapeID();
			if(movieID != null)
			{
				try {
					//Scrape the Movie from dmm.co.jp without translation
					Movie dmmMovie;
					//This condition is used for when user picks URL to scrape from
					if(getOverrideURLDMM() != null && getOverrideURLDMM().length() > 0)
					{
						dmmMovie = Movie.scrapeMovie(new File(movieID.getId()), new DmmParsingProfile(false, false), getOverrideURLDMM(), true);
					}
					else //when using automatic scraping mode
					{
						dmmMovie = Movie.scrapeMovie(new File(movieID.getId()), new DmmParsingProfile(false, false), getOverrideURLDMM(), false);
					}
					if(dmmMovie != null)
					{
						//System.out.println("DMM movie in scrapeActors of JavLib" + dmmMovie);
						
						//make a new URL based on the current one, but use the japanese language page instead
						//System.out.println("Document location: " + document.location());
						String japaneseLangVersionURLOfCurrentPage = document.location().replaceFirst(Pattern.quote("/" + siteLanguageToScrape), "/" + japaneseLanguageCode);
						
						Document japaneseLangDocumentOfCurrentMovie = Jsoup.connect(japaneseLangVersionURLOfCurrentPage).userAgent("Mozilla").timeout(0).get();
						Elements japaneseActressElements = japaneseLangDocumentOfCurrentMovie
								.select("span.cast");
						if(japaneseActressElements != null)
							{
							ArrayList<Actor> japaneseActressListJavLibrary = new ArrayList<Actor>(japaneseActressElements.size());
							for (Element japaneseCastElement : japaneseActressElements) {
								String japanseActressName = japaneseCastElement.select("span.star a").text().trim();
								japaneseActressListJavLibrary.add(new Actor(japanseActressName,"",null));
							}
							
							ArrayList<Actor> dmmActor = dmmMovie.getActors();
							
							//since we're going to be making the assumption that each element is listed in the size order for all, we better
							//make sure all the lists are the same size first
							if(dmmActor.size() == actorList.size() && dmmActor.size() == japaneseActressListJavLibrary.size())
							for(int i = 0; i < japaneseActressListJavLibrary.size(); i++)
							{
								
								for(int j = 0; j < dmmActor.size(); j++)
								{
									//System.out.println("Thumb compare: " + dmmActor.get(i).getThumb());
									//two Japanese names have matched
									if(japaneseActressListJavLibrary.get(i).getName().contains(dmmActor.get(j).getName()) || dmmActor.get(j).getName().contains(japaneseActressListJavLibrary.get(i).getName()))
									{
										//set the english thumb to the dmm thumb
										//System.out.println("Setting " + actorList.get(i) + " thumb to " + dmmActor.get(j).getThumb());
										actorList.get(i).setThumb(dmmActor.get(j).getThumb());
										//continue;
									}
								}
							}
						}
						
						//System.out.println(document.baseUri());
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return actorList;
	}

	@Override
	public ArrayList<Director> scrapeDirectors() {
		Elements directorElements = document
				.select(".director a");
		ArrayList<Director> directorList = new ArrayList<Director>(directorElements.size());
		for (Element currentDirectorElement : directorElements)
		{
			String currentDirectorName = currentDirectorElement.text().trim();
			directorList.add(new Director(currentDirectorName,null));
		}
		return directorList;
	}

	@Override
	public Studio scrapeStudio() {
		Element studioElement = document
				.select(".maker a")
				.first();
		if(studioElement != null)
		{
			return new Studio(studioElement.text().trim());
		}
		else return new Studio("");
	}

	@Override
	public String createSearchString(File file) {
		String fileNameNoExtension = findIDTagFromFile(file);
		
		//return fileNameNoExtension;
		URLCodec codec = new URLCodec();
		try {
			String fileNameURLEncoded = codec.encode(fileNameNoExtension);
			String searchTerm = "http://www.javlibrary.com/" + siteLanguageToScrape + "/vl_searchbyid.php?keyword=" + fileNameURLEncoded;
			
			return searchTerm;
					
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		

	}

	@Override
	public SearchResult[] getSearchResults(String searchString) throws IOException {
		
		ArrayList<SearchResult> linksList = new ArrayList<SearchResult>();
		String websiteURLBegin = "http://www.javlibrary.com/" + siteLanguageToScrape;
		try{
		Document doc = Jsoup.connect(searchString).userAgent("Mozilla").ignoreHttpErrors(true).timeout(0).get();
		//The search found the page directly
		if(doc.baseUri().contains("/?v="))
		{
			String linkTitle = doc.title().replaceAll(Pattern.quote(" - JAVLibrary"), "");
			Element posterElement = doc
					.select("img#video_jacket_img")
					.first();
			//the page does not have the small version on it, but by replacing the last character of the string with an t, we will get the tiny preview
			if(posterElement != null)
			{
				String posterURLSmall = posterElement.attr("src");
				posterURLSmall = posterURLSmall.substring(0, posterURLSmall.lastIndexOf('l')) + "t.jpg";
				linksList.add(new SearchResult(doc.baseUri(), linkTitle, new Thumb(posterURLSmall)));
			}
			else 
			{
				linksList.add(new SearchResult(doc.baseUri(), linkTitle));
			}
			//System.out.println("Added " + doc.baseUri());
			
			return linksList.toArray(new SearchResult[linksList.size()]);
		}
		else
		{
			//The search didn't find an exact match and took us to the search results page
			Elements videoLinksElements = doc.select("div.video");
			for(Element videoLink : videoLinksElements)
			{
				String currentLink = videoLink.select("a").attr("href");
				String currentLinkLabel = videoLink.select("a").attr("title").trim();
				String currentLinkImage = videoLink.select("img").attr("src");
				if(currentLink.length() > 1)
				{
					String fullLink = websiteURLBegin + currentLink.substring(1);
					linksList.add(new SearchResult(fullLink,currentLinkLabel,new Thumb(currentLinkImage)));
					//System.out.println("Added " + fullLink);
				}
			}
			return linksList.toArray(new SearchResult[linksList.size()]);
		}
		}
	 catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
	}
	}

	@Override
	public Thumb[] scrapeExtraFanart() {
		//No extra Fanart on this site is supported, for now
		return new Thumb[0];
	}
	
	public String toString(){
		return "JavLibrary";
	}

	@Override
	public SiteParsingProfile newInstance() {
		return new JavLibraryParsingProfile();
	}


}

