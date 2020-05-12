# Game Of Life.

##### The Rules
###### For a space that is populated:
* Each cell with one or no neighbors dies, as if by solitude.
* Each cell with four or more neighbors dies, as if by overpopulation.
* Each cell with two or three neighbors survives.

###### For a space that is empty or unpopulated
* Each cell with three neighbors becomes populated.

#### The Controls
![Control](src/image/golControl.jpg)

1. Draw Button: What this button do is to change between Erase and Draw mode
    1. Draw : Click or Dragged mouse on to turn the grid on the canvas to Alive(Yellow Tile).
    1. Erase: Click or Dragged mouse on to turn the grid on the canvas to Dead (Grey Tile).
1. Next Button: Move on to the next generation according to rules of the game above.
1. Start Button: Automatically play or move on to the next generation over times.
1. Speed Slider: Change the speed of the "Start Button" to be faster or slower.
1. Clear Button: Clear the board and start over
1. Random Button: Random the Alive tile to add on the board for new experience
1. Back Button: Go back to the previous generation or restored the board after Next, Start and Clear.
	
## TO RUN THIS PROGRAM

you can double click the Executable Jar file. 

if you can't then:
1. Open java IDE and open the project
2. Enter this command line in Terminal or cmd

###### set and export is in case you don't have PATH_TO_FX in the environment variable

window

    set PATH_TO_FX="path\to\javafx-sdk-14\lib"
    java --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml -jar UnitConverter.jar

Linux/Mac

    export PATH_TO_FX=path/to/javafx-sdk-14/lib
    java --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.fxml -jar UnitConverter.jar

