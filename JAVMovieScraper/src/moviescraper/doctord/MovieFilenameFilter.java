package moviescraper.doctord;

import java.io.File;
import java.io.FilenameFilter;

import org.apache.commons.io.FilenameUtils;

public class MovieFilenameFilter implements FilenameFilter {
	
	public static final String[] acceptedMovieExtensions = { "avi", "mpeg",
			"mpg", "wmv", "asf", "flv", "mkv", "mka", "mov", "qt", "mp4",
			"m4v", "m4a", "aac", "nut", "ogg", "ogm", "rmvb", "rm", "ram",
			"ra", "3gp", "vivo", "pva", "nuv", "nsa", "fli", "flc", "dvr-ms",
			"wtv", "iso", "vob" };

	@Override
	public boolean accept(File dir, String name) {
		if(allowedSuffix(FilenameUtils.getExtension(name)))
			return true;
		else return false;
	}
	
	private boolean allowedSuffix(String suffix)
	{
		for(String currentSuffix : acceptedMovieExtensions)
		{
			if(suffix.toLowerCase().equals(currentSuffix))
				return true;
		}
		return false;
	}

}
