package game;

//Imports

import static org.lwjgl.opengl.GL11.*;
import java.util.ArrayList;
import org.lwjgl.opengl.*;
import org.lwjgl.*;
import org.lwjgl.input.Keyboard;

//Classes

class Fruit{
	int x,y;
}

class SnakeMove{
	int x,y,direction;
}

class SnakeBodyPart{
	int x,y,direction;
	void move(){
		if(direction == 0){
			y -= 20;
		}
		if(direction == 1){
			x += 20;
		}
		if(direction == 2){
			y += 20;
		}
		if(direction == 3){
			x -= 20;
		}
	}
}

public class Snake {
	
	public Snake(){
		
		//Init
		
		try{
			
			Display.setDisplayMode(new DisplayMode(640, 480));
			Display.setTitle("Snake");
			Display.create();
			
		}
		
		catch (LWJGLException e){
			
			e.printStackTrace();
			
		}
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, 640, 480, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		
		ArrayList<SnakeBodyPart> snakeBodyParts = new ArrayList<SnakeBodyPart>();
		ArrayList<SnakeMove> snakeMove = new ArrayList<SnakeMove>();
		ArrayList<Fruit> fruits = new ArrayList<Fruit>();
		boolean dDown = Keyboard.isKeyDown(Keyboard.KEY_D);
		boolean aDown = Keyboard.isKeyDown(Keyboard.KEY_A);
		int fpsCooldown = 30;
		int points = 0;
		
		while (!Display.isCloseRequested()){
			
			SnakeBodyPart SnakeBodyPart = new SnakeBodyPart();
			SnakeMove SnakeMove = new SnakeMove();
			Fruit fruit = new Fruit();
			dDown = Keyboard.isKeyDown(Keyboard.KEY_D);
			aDown = Keyboard.isKeyDown(Keyboard.KEY_A);
			
			glClear(GL_COLOR_BUFFER_BIT);
			
			//Creating new 15-tile-long snake, do not eat fruit until snake is finished elongating
			//or else you want the side-effects, I.E. Smaller starting snake, snake run into self more often.
			
			if(snakeBodyParts.size() >= 0 && snakeBodyParts.size() < 15 && fpsCooldown == 0){
				snakeBodyParts.add(SnakeBodyPart);
				snakeBodyParts.get(snakeBodyParts.size() - 1).direction = 0;
				snakeBodyParts.get(snakeBodyParts.size() - 1).x = 320;
				snakeBodyParts.get(snakeBodyParts.size() - 1).y = 240;
			}
			
			//Changing directions of snake with snakeMoves
			
			if(snakeBodyParts.size() > 0 && ((aDown && !dDown) || (dDown && !aDown)) && fpsCooldown == 0){
				snakeMove.add(SnakeMove);
				snakeMove.get(snakeMove.size() - 1).x = snakeBodyParts.get(0).x;
				snakeMove.get(snakeMove.size() - 1).y = snakeBodyParts.get(0).y;
				if(aDown){
					snakeMove.get(snakeMove.size() - 1).direction = snakeBodyParts.get(0).direction - 1;
					if(snakeMove.get(snakeMove.size() - 1).direction == -1){
						snakeMove.get(snakeMove.size() - 1).direction = 3;
					}
				}
				if(dDown){
					snakeMove.get(snakeMove.size() - 1).direction = snakeBodyParts.get(0).direction + 1;
					if(snakeMove.get(snakeMove.size() - 1).direction == 4){
						snakeMove.get(snakeMove.size() - 1).direction = 0;
					}
				}
			}
			
			//Deleting snakeMoves when last tile reaches it
			
			for(int i = 0; i < snakeBodyParts.size() && fpsCooldown == 0; i++){
				for(int i2 = 0; i2 < snakeMove.size(); i2++){
					if(snakeBodyParts.get(i).x == snakeMove.get(i2).x && snakeBodyParts.get(i).y == snakeMove.get(i2).y){
						snakeBodyParts.get(i).direction = snakeMove.get(i2).direction;
						if(i == snakeBodyParts.size() - 1){
							snakeMove.remove(i2);
							i2--;
						}
					}
				}
			}
			
			//Drawing snakeBodyParts
			
			for(int i = 0; i < snakeBodyParts.size(); i++){
				if(fpsCooldown == 0){
					snakeBodyParts.get(i).move();
				}
				
				if(i == 0){
					glColor3d(0,1,0);
				}
				else{
					glColor3d(1,0,0);
				}
				
				glBegin(GL_QUADS);
				glVertex2d(snakeBodyParts.get(i).x - 7, snakeBodyParts.get(i).y - 7);
				glVertex2d(snakeBodyParts.get(i).x + 7, snakeBodyParts.get(i).y - 7);
				glVertex2d(snakeBodyParts.get(i).x + 7, snakeBodyParts.get(i).y + 7);
				glVertex2d(snakeBodyParts.get(i).x - 7, snakeBodyParts.get(i).y + 7);
				glEnd();
			}
			
			//Eating fruits, elongating snake by 1 tile
			
			if(fruits.size() > 0 && snakeBodyParts.size() > 0){
				for(int i = 0; i < fruits.size(); i++){
					if(snakeBodyParts.get(0).x == fruits.get(i).x && snakeBodyParts.get(0).y == fruits.get(i).y){
						points++;
						fruits.remove(i);
						SnakeBodyPart snakeBodyPart = new SnakeBodyPart();
						snakeBodyParts.add(snakeBodyPart);
						snakeBodyParts.get(snakeBodyParts.size() - 1).direction = snakeBodyParts.get(snakeBodyParts.size() - 2).direction;
						if(snakeBodyParts.get(snakeBodyParts.size() - 2).direction == 0){
							snakeBodyParts.get(snakeBodyParts.size() - 1).y = snakeBodyParts.get(snakeBodyParts.size() - 2).y + 20;
							snakeBodyParts.get(snakeBodyParts.size() - 1).x = snakeBodyParts.get(snakeBodyParts.size() - 2).x;
						}
						else if(snakeBodyParts.get(snakeBodyParts.size() - 2).direction == 1){
							snakeBodyParts.get(snakeBodyParts.size() - 1).x = snakeBodyParts.get(snakeBodyParts.size() - 2).x - 20;
							snakeBodyParts.get(snakeBodyParts.size() - 1).y = snakeBodyParts.get(snakeBodyParts.size() - 2).y;
						}
						else if(snakeBodyParts.get(snakeBodyParts.size() - 2).direction == 2){
							snakeBodyParts.get(snakeBodyParts.size() - 1).y = snakeBodyParts.get(snakeBodyParts.size() - 2).y - 20;
							snakeBodyParts.get(snakeBodyParts.size() - 1).x = snakeBodyParts.get(snakeBodyParts.size() - 2).x;
						}
						else if(snakeBodyParts.get(snakeBodyParts.size() - 2).direction == 3){
							snakeBodyParts.get(snakeBodyParts.size() - 1).x = snakeBodyParts.get(snakeBodyParts.size() - 2).x + 20;
							snakeBodyParts.get(snakeBodyParts.size() - 1).y = snakeBodyParts.get(snakeBodyParts.size() - 2).y;
						}
					}
				}
			}
			
			//Drawing fruits
			
			for(int i = 0; i < fruits.size(); i++){
				
				glColor3d(1,1,0);
				
				glBegin(GL_QUADS);
				glVertex2d(fruits.get(i).x - 7, fruits.get(i).y - 7);
				glVertex2d(fruits.get(i).x + 7, fruits.get(i).y - 7);
				glVertex2d(fruits.get(i).x + 7, fruits.get(i).y + 7);
				glVertex2d(fruits.get(i).x - 7, fruits.get(i).y + 7);
				glEnd();
				
			}
			
			//Crashing into wall/self
			
			if(snakeBodyParts.size() > 0){			
				if(snakeBodyParts.get(0).x >= 640 || snakeBodyParts.get(0).x <= 0 || snakeBodyParts.get(0).y >= 480 || snakeBodyParts.get(0).y <= 0){
					System.out.println("You ran into a wall!");
					break;
				}
				boolean runIntoSelf = false;
				for(int i = 1; i < snakeBodyParts.size(); i++){
					if(snakeBodyParts.get(i).x == snakeBodyParts.get(0).x && snakeBodyParts.get(i).y == snakeBodyParts.get(0).y){
						System.out.println("You ran into yourself!");
						runIntoSelf = true;
						break;
					}
				}
				if(runIntoSelf){
					break;
				}
			}
			
			//FPS stuff
			
			if(fpsCooldown > 0){
				fpsCooldown--;
			}
			else{
				fpsCooldown = 30;
			}
			
			//Adding fruit if there are none
			
			if(fruits.size() == 0){
				fruits.add(fruit);
				fruits.get(fruits.size() - 1).x = (int) Math.round(Math.random() * 600 + 20);
				fruits.get(fruits.size() - 1).y = (int) Math.round(Math.random() * 440 + 20);
				fruits.get(fruits.size() - 1).x -= fruits.get(fruits.size() - 1).x % 20;
				fruits.get(fruits.size() - 1).y -= fruits.get(fruits.size() - 1).y % 20;
			}
			
			//Syncing FPS with real time
			
			Display.update();
			Display.sync(60);
			
		}
		
		//Output score
		
		System.out.println("Your score is " + points + "!");
		
		Display.destroy();
		
	}
	
	//Main

	public static void main(String[] args) {
		
		new Snake();

	}

}
