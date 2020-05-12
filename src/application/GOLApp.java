package application;
	
import java.text.NumberFormat;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;

public class GOLApp extends Application {
	
	private boolean runningState = false;
	private Timeline timeLine;
	private int resolution = 700;
	private boolean drawALIVE = true;
	
	GameOfLife gol = new GameOfLife(new Board(70, 70));
	
	Affine affine;
	//Button
	Button clearButton;
	Button nextButton;
	Button playButton;
	Button randomButton;
	Button backButton;
	Button drawButton;
	//Slider
	Slider slider;
	Canvas canvas;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FlowPane root = initComponent();
			primaryStage.setTitle("The Game of Life");
			gol.random();
			primaryStage.setScene(new Scene(root));
			primaryStage.show();
			draw(gol.board);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * Initialize the component to show on the screen.
	 * @return FlowPane containing all components for Game of Life App.
	 */
	private FlowPane initComponent() {
		
		//Create new root for the simulation
		affine = new Affine();
		affine.appendScale( (resolution/gol.board.getRows()), (resolution/gol.board.getColumns()) );
		FlowPane root = new FlowPane();
		canvas = new Canvas(resolution, resolution);
		canvas.setOnMouseDragged( new DrawHandler() );
		canvas.setOnMousePressed( new DrawHandler() );
		
		VBox vbox = new VBox();
		HBox hbox = new HBox();
		
        //MenuBar setup in VBox
        Menu menu = new Menu("File");
        MenuItem random = new MenuItem("Random");
		random.setOnAction( new randomHandler() );
        MenuItem clear = new MenuItem("Clear");
        clear.setOnAction( new clearHandler() );
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(ActionEvent -> System.exit(0));
        menu.getItems().addAll(random, clear, new SeparatorMenuItem(), exit);
        MenuBar menuBar = new MenuBar(menu);
        vbox.getChildren().add(menuBar);
		
        //Setting the Alignment
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(10.0));
        hbox.setSpacing(10.0);
        
        vbox.setPrefWidth(resolution + 50);
        
		root.setPrefSize(resolution + 50, resolution + 100);
		root.setHgap(10);
		root.setVgap(10);
		root.alignmentProperty().set(Pos.CENTER);
			
		//add Button
		clearButton = new Button("Clear");
		clearButton.setOnAction( new clearHandler() );
		nextButton = new Button("Next");
		nextButton.setOnAction( ActionEvent -> {
			if (runningState) {
				draw(gol.board);
			} else {
				gol.saveBoard();
				gol.nextGeneration();
				draw(gol.board);
			}
		});
		randomButton = new Button("Random");
		randomButton.setOnAction( new randomHandler() );
		playButton = new Button("Start");
		playButton.setOnAction( new PlayHandler() );
		backButton = new Button("Back");
		backButton.setOnAction( new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if (runningState) {
					draw(gol.board);
				} else {
					gol.loadBoard();
					draw(gol.board);				
				}
			}
			
		});
		drawButton = new Button("Draw");
		drawButton.setOnAction( new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if (drawALIVE) {
					drawALIVE = false;
					drawButton.setText("Erase");
				} else {
					drawALIVE = true;
					drawButton.setText("Draw");
				}
			}
			
		});
		
		//add Slider
		slider = new Slider();
		slider.setValue(0.5);
		slider.setMin(0.5);
		slider.setMax(100);
		slider.setPrefWidth(200);
		slider.setOnMouseDragged(new playSpeedHandler());
		//add Label
		Label label = new Label("Speed");
		Label playSpeedText = new Label("1");

		playSpeedText.setPrefWidth(35);
		playSpeedText.textProperty().bindBidirectional(slider.valueProperty(), NumberFormat.getNumberInstance());
		hbox.getChildren().addAll(vbox, canvas, drawButton, nextButton, playButton, label, playSpeedText, slider, clearButton, randomButton, backButton);
		root.getChildren().addAll(vbox, canvas, hbox);
		
		return root;
	}
	
	/**
	 * Handle the AutoPlay option in Game of life in Start and Pause Button
	 *
	 * @author Kongtapp Veerawattananun
	 */
	class PlayHandler implements EventHandler<ActionEvent> {
		
		@Override
		public void handle(ActionEvent event) {
			if (playButton.getText().equalsIgnoreCase("Start")) {
				gol.saveBoard();
				runningState = true;
				timeLineStart();
				playButton.setText("Pause");
			}
			else {					
				runningState = false;
				timeLinePause();
				playButton.setText("Start");
			}   
		}
	}
	
	/**
	 * Handler for Reset or Change the playing speed in Game of Life.
	 * @author Kongtapp Veerawattananun
	 *
	 */
	class playSpeedHandler implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent arg0) {
			if (runningState) {
				timeLinePause();
				timeLineStart();
			}
		}
		
	}
	
	/**
	 * Handle the mouse Clicked action in the canvas to edit the population in GameOfLife
	 * Action: if that location have someone already living kill it, otherwise put some people into that location.
	 * 
	 * @author Kongtapp Veerawattananun
	 */
	class DrawHandler implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			double posX = event.getX();
			double posY = event.getY();
			
			try {
				Point2D coor = affine.inverseTransform(posX, posY);
				int x = (int)coor.getX();
				int y = (int)coor.getY();
				
				if ( runningState ) {
					//this mean if Play Button is enable you can't edit the board
					draw(gol.board);
				} else if (drawALIVE) {
					gol.board.setAlive(x, y);
					draw(gol.board);
				} else {
					gol.board.setDead(x, y);
					draw(gol.board);
				}
				
			} catch (NonInvertibleTransformException e) {
				System.out.println("Couldn't inverse transform");
			}
		}
		
	}
	
	/**
	 * Handle the clear action for Game of Life.
	 * Action: Clear the playing board if application runningState is false.
	 * 
	 * @author Kongtapp Veerawattananun
	 *
	 */
	class clearHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent arg0) {
			if (runningState) {
				draw(gol.board);
			} else {
				gol.saveBoard();
				Board board = new Board(gol.board.getRows(), gol.board.getColumns());
				gol.board = board;
				draw(gol.board);				
			}
		}
		
	}
	
	/**
	 * Handle the random action for Game of Life
	 * Action: Randomly add the alive cell into the board if application runningState is false.
	 * 
	 * @author Kongtapp Veerawattananun
	 *
	 */
	class randomHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent arg0) {
			if (runningState) {
				draw(gol.board);
			} else {
				gol.random();
				draw(gol.board);			
			}
		}
		
	}
	
	/**
	 * Draw the Game of Life into the canvas
	 * YellowBox: Alive
	 * DarkGray:  Dead
	 */
	private void draw(Board board) {
		GraphicsContext context = canvas.getGraphicsContext2D();
		context.setTransform(affine);
		
		context.setFill(Color.DARKGRAY);
		context.fillRect(0, 0, 500, 500);
		
		context.setFill(Color.YELLOW);
		//Fill in the Alive Grid
		for (int x = 0; x < board.getRows(); x++) {
			for (int y = 0; y < board.getColumns(); y++) {
				if (board.getValue(x, y) == board.ALIVE) {
					context.fillRect(x, y, 1, 1);
				}
			}
		}
		context.setLineWidth(0.075);
		context.setStroke(Color.BLACK);
		//Draw Horizontal GridLine
		for (int x = 0; x <= board.getRows(); x++) {
			context.strokeLine(x, 0, x, 500);
		}
		//Draw Vertical GridLine
		for (int y = 0; y <= board.getColumns(); y++) {
			context.strokeLine(0, y, 500, y);
		}
	}
	
	/**
	 * Move on to the nextGenaration in game of life and draw the result
	 */
	private void nextGen() {
		gol.nextGeneration();
		draw(gol.board);
	}
	
	/**
	 * Start the playing simulation in Game of Life
	 */
	private void timeLineStart() {
		timeLine = new Timeline(new KeyFrame(Duration.millis(500/slider.valueProperty().get()), ActionEvent -> nextGen()));
		timeLine.setCycleCount(Timeline.INDEFINITE);
		this.timeLine.play();
	}
	
	/**
	 * Pause the playing simulation in Game of Life
	 */
	private void timeLinePause() {
		this.timeLine.stop();
	}
	
}
