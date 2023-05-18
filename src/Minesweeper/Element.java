package Minesweeper;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class Element {
	private String type;
	protected Image img;
	protected ImageView imgView;
	protected GameStage gameStage;
	protected int row, col;


	public final static Image FLAG_IMAGE = new Image("images/flag.png",GameStage.CELL_WIDTH,GameStage.CELL_WIDTH,false,false);
	public final static Image BOMB_IMAGE = new Image("images/bomb.gif",GameStage.CELL_WIDTH,GameStage.CELL_WIDTH,false,false);
	public final static Image LAND_IMAGE = new Image("images/land.png",GameStage.CELL_WIDTH,GameStage.CELL_WIDTH,false,false);
	public final static Image ONE_IMAGE = new Image("images/one.png",GameStage.CELL_WIDTH,GameStage.CELL_WIDTH,false,false);
	public final static Image TWO_IMAGE = new Image("images/two.png",GameStage.CELL_WIDTH,GameStage.CELL_WIDTH,false,false);
	public final static Image THREE_IMAGE = new Image("images/three.png",GameStage.CELL_WIDTH,GameStage.CELL_WIDTH,false,false);
	public final static Image FOUR_IMAGE = new Image("images/four.png",GameStage.CELL_WIDTH,GameStage.CELL_WIDTH,false,false);
	public final static int IMAGE_SIZE = 70;

	public final static String FLAG_TYPE = "flag";
	public final static String BOMB_TYPE = "bomb";
	public final static String LAND_TYPE = "land";
	public final static String LAND_FLAG_TYPE = "landToFlag";

	public Element(String type, GameStage gameStage) {
		this.type = type;
		this.gameStage = gameStage;

		// load image depending on the type
		switch(this.type) {
			case Element.FLAG_TYPE: this.img = Element.FLAG_IMAGE; break;
			case Element.LAND_TYPE: this.img = Element.LAND_IMAGE; break;
			case Element.BOMB_TYPE: this.img = Element.BOMB_IMAGE; break;
		}

		this.setImageView();
		this.setMouseHandler();
	}

	protected void loadImage(String filename,int width, int height){
		try{
			this.img = new Image(filename,width,height,false,false);
		} catch(Exception e){}
	}


	String getType(){
		return this.type;
	}

	int getRow() {
		return this.row;
	}

	int getCol() {
		return this.col;
	}


	protected ImageView getImageView(){
		return this.imgView;
	}

	void setType(String type){
		this.type = type;
	}

	void initRowCol(int i, int j) {
		this.row = i;
		this.col = j;
	}

	private void setImageView() {
		// initialize the image view of this element
		this.imgView = new ImageView();
		this.imgView.setImage(this.img);
		this.imgView.setLayoutX(0);
		this.imgView.setLayoutY(0);
		this.imgView.setPreserveRatio(true);

		if(this.type.equals(Element.FLAG_TYPE)) {
			this.imgView.setFitWidth(GameStage.FLAG_WIDTH);
			this.imgView.setFitHeight(GameStage.FLAG_HEIGHT);
		}else {
			this.imgView.setFitWidth(GameStage.CELL_WIDTH);
			this.imgView.setFitHeight(GameStage.CELL_HEIGHT);
		}
	}

	private void setMouseHandler(){
		Element element = this;
		//int neighboring_bomb = countNeighboringBomb();
		this.imgView.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e) {
                switch(element.getType()) {
	                case Element.FLAG_TYPE: 		// if flag, set flagClicked to true
								                	System.out.println("FLAG clicked!");
								    	            gameStage.setFlagClicked(true);
								    	            break;
	    			case Element.LAND_TYPE:
			    									System.out.println("LAND clicked!");
			    									int neighboring_bomb = 0;
			    									int num = 0 ;
			    										//checking for neighboring bombs
				    									if(element.gameStage.isIndexBomb(element.row+1, element.col)){
				    										neighboring_bomb += 1;
				    									}
				    									if(element.gameStage.isIndexBomb(element.row-1, element.col)){
				    										neighboring_bomb += 1;
				    									}
				    									if(element.gameStage.isIndexBomb(element.row, element.col+1)){
				    										neighboring_bomb += 1;
				    									}
				    									if(element.gameStage.isIndexBomb(element.row, element.col-1)){
				    										neighboring_bomb += 1;
				    									}
									    				if(!gameStage.isFlagClicked()) {
									    					if(gameStage.isBomb(element)){
									    						changeImage(element,Element.BOMB_IMAGE); // if bomb, change image to bomb
									    						gameStage.flashGameOver(num);
									    					}
									    					else if(neighboring_bomb==0){	// if no neighboring bombs
									    						clearImage(element);
									    						element.gameStage.increaseLandOpened();
									    					}
									    					else if(neighboring_bomb==1){
									    						changeImage(element,Element.ONE_IMAGE);
									    						element.gameStage.increaseLandOpened();
									    					}
									    					else if(neighboring_bomb==2){
									    						changeImage(element,Element.TWO_IMAGE);
									    						element.gameStage.increaseLandOpened();
									    					}
									    					else if(neighboring_bomb==3){
									    						changeImage(element,Element.THREE_IMAGE);
									    						element.gameStage.increaseLandOpened();
									    					}
									    					else if(neighboring_bomb==4){
									    						changeImage(element,Element.FOUR_IMAGE);
									    						element.gameStage.increaseLandOpened();
									    					}


									    	            } else {
									    	            	if(element.gameStage.isBomb(element)){
									    	            		element.gameStage.increaseFlaggedBombs();
									    	            	}
									    	            	changeImage(element,Element.FLAG_IMAGE);	// if flag was clicked before hand, change image to flag
									    	            	element.setType(LAND_FLAG_TYPE);			// change type to landToFlag
									    	            	gameStage.setFlagClicked(false);	    	// reset flagClicked to false

									    	            }
									    				//check if winner
									    				if(element.gameStage.getLandOpened() == 66 || element.gameStage.getFlaggedBombs() == 15){
									    					num = 1;
									    					gameStage.flashGameOver(num);
									    				}
								    				break;
	    			case Element.LAND_FLAG_TYPE:
								    				changeImage(element,Element.LAND_IMAGE);		// if flag is clicked, change image back to land
							    	            	element.setType(LAND_TYPE);
							    	            	break;
                }
			}	//end of handle()
		});
	}

	private void clearImage(Element element) {
		imgView.setImage(null);
	}

	private void changeImage(Element element, Image image) {
		this.imgView.setImage(image);

	}

}

