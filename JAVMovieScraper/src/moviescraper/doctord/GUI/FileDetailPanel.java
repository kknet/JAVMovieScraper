package moviescraper.doctord.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import moviescraper.doctord.Movie;
import moviescraper.doctord.Thumb;
import moviescraper.doctord.GUI.renderer.ActressListRenderer;
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
import moviescraper.doctord.dataitem.Trailer;
import moviescraper.doctord.dataitem.Votes;
import moviescraper.doctord.dataitem.Year;
import moviescraper.doctord.preferences.MoviescraperPreferences;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class FileDetailPanel extends JPanel {

	private static final long serialVersionUID = 7088761619568387476L;
	
	private JComboBox<String> comboBoxMovieTitleText;
	private JLabel lblOriginalTitleText;
	private JLabel lblScrapedYearText;
	private JLabel lblIDCurrentMovie;
	private JTextField txtFieldStudio;
	private JTextField txtFieldMovieSet;
	private JTextArea moviePlotTextField;
	private JList<Actor> actorList;
	private JList<String> genreList;
	
	protected Movie currentMovie = getEmptyMovie();
	MoviescraperPreferences preferences;

	private ArtWorkPanel artWorkPanel;
	
	GUIMain gui;

	/**
	 * Create the panel.
	 */
	public FileDetailPanel(MoviescraperPreferences preferences, GUIMain gui) {
		this.preferences = preferences;
		this.gui = gui;
		JPanel fileDetailsPanel = this;
		fileDetailsPanel.setBorder(BorderFactory.createLineBorder(Color.black));

		FormLayout formLayout = new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC, // 1 - empty space
				FormFactory.DEFAULT_COLSPEC, //2 - label for each of the form items
				FormFactory.RELATED_GAP_COLSPEC,//3 - empty space
				ColumnSpec.decode("default:grow"), // 4 - Form text items
				FormFactory.RELATED_GAP_COLSPEC,//5 - empty space
				FormFactory.DEFAULT_COLSPEC},// 6 - artwork panel
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC, //1 - empty space
				FormFactory.DEFAULT_ROWSPEC, //2 - Title and artwork panel
				FormFactory.RELATED_GAP_ROWSPEC,//3 - empty space
				FormFactory.DEFAULT_ROWSPEC,//4 - original title
				FormFactory.RELATED_GAP_ROWSPEC,//5 - empty space
				FormFactory.DEFAULT_ROWSPEC,//6 - Year
				FormFactory.RELATED_GAP_ROWSPEC,//7 - empty space
				FormFactory.DEFAULT_ROWSPEC,//8 - ID
				FormFactory.RELATED_GAP_ROWSPEC,//9 - empty space
				FormFactory.DEFAULT_ROWSPEC,//10 - Studio
				FormFactory.RELATED_GAP_ROWSPEC,//11 - empty space
				FormFactory.DEFAULT_ROWSPEC,//12 - Movie set
				FormFactory.RELATED_GAP_ROWSPEC,//13 - empty space
				FormFactory.DEFAULT_ROWSPEC,//14 - Plot
				FormFactory.RELATED_GAP_ROWSPEC,//15 - empty space
				RowSpec.decode("default:grow"),//16 - actors
				FormFactory.RELATED_GAP_ROWSPEC,//17 - empty space
				RowSpec.decode("default:grow"),//18 - genres
				FormFactory.RELATED_GAP_ROWSPEC//19 - empty space
				});
		
		
		//formLayout.setColumnGroups(new int[][]{{4, 6}});
		
		fileDetailsPanel.setLayout(formLayout);


		JLabel lblTitle = new JLabel("Title:");
		fileDetailsPanel.add(lblTitle, "2, 2");
		
		//using this workaround for JComboBox constructor for problem with generics in WindowBuilder as per this stackoverflow thread: https://stackoverflow.com/questions/8845139/jcombobox-warning-preventing-opening-the-design-page-in-eclipse
		comboBoxMovieTitleText = new JComboBox<String>();
		comboBoxMovieTitleText.setModel( new TitleListModel() );
		comboBoxMovieTitleText.addActionListener(new ActionListener(){
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            
	            if(currentMovie != null)
	            {
	            	String newValue = (String) comboBoxMovieTitleText.getSelectedItem();
	            	if(newValue != null)
	            	{
	            		currentMovie.setTitle(new Title(newValue));
	            	}
	            }
	        }
	    });
		comboBoxMovieTitleText.getEditor().getEditorComponent().addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				String newValue = (String) comboBoxMovieTitleText.getSelectedItem();
            	if(newValue != null)
            	{
            		currentMovie.setTitle(new Title(newValue));
            	}
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
			
		});
		fileDetailsPanel.add(comboBoxMovieTitleText, "4, 2");



		JLabel lblOriginalTitle = new JLabel("Original Title:");
		fileDetailsPanel.add(lblOriginalTitle, "2, 4");



		lblOriginalTitleText = new JLabel("");
		fileDetailsPanel.add(lblOriginalTitleText, "4, 4");


		JLabel lblYear = new JLabel("Year:");
		fileDetailsPanel.add(lblYear, "2, 6");
		

		lblScrapedYearText = new JLabel("");
		fileDetailsPanel.add(lblScrapedYearText, "4, 6");
		
		JLabel lblID = new JLabel("ID:");
		fileDetailsPanel.add(lblID, "2, 8");
		
		lblIDCurrentMovie = new JLabel("");
		fileDetailsPanel.add(lblIDCurrentMovie,"4, 8");
		
		JLabel lblStudio = new JLabel("Studio:");
		txtFieldStudio = new JTextField("");
		txtFieldStudio.addActionListener(new ActionListener(){
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            
	            if(currentMovie != null)
	            {
	            	String newValue = (String) txtFieldStudio.getText();
	            	if(newValue != null)
	            	{
	            		currentMovie.setStudio(new Studio(newValue));
	            	}
	            }
	        }
	    });
		txtFieldStudio.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				String newValue = (String) txtFieldStudio.getText();
            	if(newValue != null)
            	{
            		currentMovie.setStudio(new Studio(newValue));
            	}
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
			
		});
		
		fileDetailsPanel.add(lblStudio,"2,10");
		fileDetailsPanel.add(txtFieldStudio,"4,10");
		
		JLabel lblSet = new JLabel("Movie Set:");
		fileDetailsPanel.add(lblSet,"2, 12");
		txtFieldMovieSet = new JTextField("");
		txtFieldMovieSet.addActionListener(new ActionListener(){
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            
	            if(currentMovie != null)
	            {
	            	String newValue = (String) txtFieldMovieSet.getText();
	            	if(newValue != null)
	            	{
	            		currentMovie.setSet(new Set(newValue));
	            	}
	            }
	        }
	    });
		txtFieldMovieSet.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				String newValue = (String) txtFieldMovieSet.getText();
            	if(newValue != null)
            	{
            		currentMovie.setSet(new Set(newValue));
            	}
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
			
		});
		fileDetailsPanel.add(txtFieldMovieSet,"4,12");
		
		JLabel lblPlot = new JLabel("Plot:");
		fileDetailsPanel.add(lblPlot, "2,14");
		

		moviePlotTextField = new JTextArea(3,35);
		moviePlotTextField.setLineWrap(true);
		moviePlotTextField.setWrapStyleWord(true);
		moviePlotTextField.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				String newValue = (String) moviePlotTextField.getText();
            	if(newValue != null)
            	{
            		currentMovie.setPlot(new Plot(newValue));
            	}
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
			
		});

		JScrollPane plotPanelScrollPane = new JScrollPane(moviePlotTextField, 
				   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		fileDetailsPanel.add(plotPanelScrollPane,"4,14");
		

		JLabel lblActors = new JLabel("Actors:");
		fileDetailsPanel.add(lblActors, "2, 16");

		actorList = new JList<Actor>(new ActorItemListModel());
		List<File> currentlySelectedActorsFolderList = new ArrayList<File>();
		if(gui != null)
			currentlySelectedActorsFolderList = gui.getCurrentlySelectedActorsFolderList();
		actorList.setCellRenderer(new ActressListRenderer(currentlySelectedActorsFolderList));
		actorList.setComponentPopupMenu(new FileDetailPanelPopup(new FileDetailPanelActorEditor(this)));
		actorList.addMouseListener(new MouseAdapter() {

		     @Override
		     public void mousePressed(MouseEvent e) {
		    	 actorList.setSelectedIndex(actorList.locationToIndex(e.getPoint()));
		     }
		});


		JScrollPane actorListScroller = new JScrollPane(actorList);
		actorListScroller.setPreferredSize(new Dimension(250, 250));
		actorList.setSize(new Dimension(250, 250));
		fileDetailsPanel.add(actorListScroller, "4, 16");

		JLabel lblGenres = new JLabel("Genres:");
		fileDetailsPanel.add(lblGenres, "2, 18");

		genreList = new JList<String>(new GenreItemListModel());
		JScrollPane listScrollerGenres = new JScrollPane(genreList);
		genreList.setComponentPopupMenu(new FileDetailPanelPopup(new FileDetailPanelGenreEditor(this)));
		
		genreList.setSize(new Dimension(200, 200));
		fileDetailsPanel.add(listScrollerGenres, "4, 18");
		
		artWorkPanel = new ArtWorkPanel();
		//frmMoviescraper.getContentPane().add(artworkPanelScrollPane, BorderLayout.EAST);
		fileDetailsPanel.add(artWorkPanel,"6,2,1,18");
	}
	/**
	 * Sets a new movie and updates a view
	 * @param newMovie the movie this fileDetailPanel will show
	 * @param forcePosterUpdate whether to force a redownload from the poster defined in the Thumb url
	 */
	
	public void setNewMovie(Movie newMovie, boolean forcePosterUpdate) {
		System.out.println("Setting new movie: " + newMovie);
		setCurrentMovie(newMovie);
		updateView(forcePosterUpdate, true);
	}
	
	public void clearView() {
		artWorkPanel.clearPictures();
		setTitleEditable(false);
		this.currentMovie = getEmptyMovie();
	}

	//Updates the view for the current movie
	public void updateView(boolean forcePosterUpdate, boolean newMovieWasSet) {
		
		List<Movie> movieToWriteToDiskList = gui.getMovieToWriteToDiskList();
		if(newMovieWasSet && movieToWriteToDiskList.size() == 0)
		{
			movieToWriteToDiskList.add(currentMovie);
		}
		//begin
		if ((movieToWriteToDiskList == null || movieToWriteToDiskList.size() == 0) && !newMovieWasSet) {
			clearView();
		} else if (movieToWriteToDiskList != null && movieToWriteToDiskList.get(0) != null) {
			if(!newMovieWasSet)
				clearView();
			if(!newMovieWasSet)
				this.setCurrentMovie(movieToWriteToDiskList.get(0));

			//All the titles from the various versions scraped of this movie from the different sites
			if(movieToWriteToDiskList != null)
				this.getCurrentMovie().getAllTitles().add( getCurrentMovie().getTitle() );
			if(gui.getCurrentlySelectedMovieDMM() != null)
				this.getCurrentMovie().getAllTitles().add( gui.getCurrentlySelectedMovieDMM().getTitle() );
			if(gui.getCurrentlySelectedMovieJavLibrary() != null)
				this.getCurrentMovie().getAllTitles().add( gui.getCurrentlySelectedMovieJavLibrary().getTitle() );
			if(gui.getCurrentlySelectedMovieSquarePlus() != null)
				this.getCurrentMovie().getAllTitles().add( gui.getCurrentlySelectedMovieSquarePlus().getTitle() );
			if(gui.getCurrentlySelectedMovieActionJav() != null)
				this.getCurrentMovie().getAllTitles().add( gui.getCurrentlySelectedMovieActionJav().getTitle() );
			if(gui.getCurrentlySelectedMovieJavZoo() != null)
				this.getCurrentMovie().getAllTitles().add( gui.getCurrentlySelectedMovieJavZoo().getTitle() );
			if(this.getCurrentMovie().getAllTitles().size() > 0)
				this.setTitleEditable(true);
		//end
		}
		comboBoxMovieTitleText.setModel( new TitleListModel() );
		comboBoxMovieTitleText.setEditable(true);
		lblOriginalTitleText.setText( currentMovie.getOriginalTitle().getOriginalTitle() );
		lblScrapedYearText.setText( currentMovie.getYear().getYear() );
		lblIDCurrentMovie.setText( currentMovie.getId().getId() );
		txtFieldStudio.setText( currentMovie.getStudio().getStudio() );
		txtFieldMovieSet.setText( currentMovie.getSet().getSet() );
		moviePlotTextField.setText( currentMovie.getPlot().getPlot() );
		
		//select first Title
		if ( comboBoxMovieTitleText.getItemCount() > 0 )
			comboBoxMovieTitleText.setSelectedIndex(0);
		
		//Actors and Genres are automatically generated
		actorList.updateUI();
		genreList.updateUI();
		comboBoxMovieTitleText.updateUI();
		
		artWorkPanel.updateView(forcePosterUpdate, gui);
	}

	public Movie getCurrentMovie() {
		return currentMovie;
	}
	public Movie getEmptyMovie() {

		ArrayList<Actor> actors = new ArrayList<Actor>();
		ArrayList<Director> directors = new ArrayList<>();
		ArrayList<Genre> genres = new ArrayList<Genre>();
		
		Thumb[] fanart = new Thumb[0]; 
		Thumb[] extraFanart = new Thumb[0]; 
		Thumb[] posters = new Thumb[0]; 
		
		ID id = new ID("");
		MPAARating mpaa = new MPAARating("");
		OriginalTitle originalTitle = new OriginalTitle("");
		Outline outline = new Outline("");
		Plot plot = new Plot("");
		Rating rating = new Rating(0.0, "");
		Runtime runtime = new Runtime("");
		Set set = new Set("");
		SortTitle sortTitle= new SortTitle("");
		Studio studio = new Studio("");
		Tagline tagline = new Tagline("");
		Title title = new Title("");
		Top250 top250 = new Top250("");
		Trailer trailer = new Trailer(null);
		Votes votes = new Votes("");
		Year year = new Year("");
		
		return new Movie(actors, directors, fanart, extraFanart, genres, id, mpaa, originalTitle, outline, plot, posters, rating, runtime, set, sortTitle, studio, tagline, title, top250, trailer, votes, year);
	}
	
	public void setTitleEditable(boolean value) {
		comboBoxMovieTitleText.setEditable(value);
	}
	
	public ArtWorkPanel getArtWorkPanel() {
		return artWorkPanel;
	}
	
	class GenreItemListModel extends AbstractListModel<String> {
		
		private static final long serialVersionUID = 973741706455659871L;

		@Override
		public String getElementAt(int index) {
			Genre genre = currentMovie.getGenres().get(index);
			return (genre != null) ? genre.getGenre() : "";
		}
		
		@Override
		public int getSize() {
			return currentMovie.getGenres().size();
		}
	}
	
	class ActorItemListModel extends AbstractListModel<Actor> {

		private static final long serialVersionUID = 276453659002862686L;

		@Override
		public Actor getElementAt(int index) {
			return currentMovie.getActors().get(index);
		}

		@Override
		public int getSize() {
			return currentMovie.getActors().size();
		}
		
	}
	
	class TitleListModel extends DefaultComboBoxModel<String> {

		private static final long serialVersionUID = -8954125792857066062L;

		@Override
		public int getSize() {
			return currentMovie.getAllTitles().size();
		}

		@Override
		public String getElementAt(int index) {
			return currentMovie.getAllTitles().get(index).getTitle();
		}
		
	}

	public JList<Actor> getActorList() {
		return actorList;
	}

	public void setActorList(JList<Actor> actorList) {
		this.actorList = actorList;
	}

	public JList<String> getGenreList() {
		return genreList;
	}

	public void setGenreList(JList<String> genreList) {
		this.genreList = genreList;
	}

	public void setCurrentMovie(Movie currentMovie) {
		this.currentMovie = currentMovie;
	}
	
	public void hideArtworkPanel()
	{
		artWorkPanel.setVisible(false);
	}
	
}

