package com.bendeutz.animation;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;


public class Animator implements ApplicationListener {

	private static final int FRAME_COLS = 6; 
	private static final int FRAME_ROWS = 5; 

	Animation walkAnimationF; 
	Animation currentWalkAnimation; 

	// collision
	Rectangle player;
	
	boolean collected;
	Animation walkAnimationB; 
	Texture walkSheet; // 
	Texture walkSheetB;
	Texture background;
	Sprite coin;
	TextureRegion[] walkFrames; 
	TextureRegion[] walkFramesB; 
	SpriteBatch spriteBatch; 
	TextureRegion currentFrame; 
	OrthographicCamera cam;
	ShapeRenderer sr;
    

    float x;
    float y;
    float stateTime;                                        
    
    float x_cam;
    float y_cam;

    @Override
    public void create() {
    	
    	
    	sr = new ShapeRenderer();
    	background = new Texture(Gdx.files.internal("background.jpg"));
    	coin = new Sprite(new Texture(Gdx.files.internal("coin.png")));
    	collected = false;
    	 x = 50;
    	 y = 50;
    	 float w = Gdx.graphics.getWidth();
         float h = Gdx.graphics.getHeight();
         
         cam = new OrthographicCamera(w,h);
         x_cam = cam.viewportWidth/2;
         y_cam = cam.viewportHeight/2;
         
         System.out.println("width: " + w);
         System.out.println("height: " + h);
         System.out.println("cam viewportWidth " + cam.viewportWidth);
         System.out.println("cam viewportHeight " + cam.viewportHeight);
         
         
         cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
         cam.update();
         
        
        walkSheet = new Texture(Gdx.files.internal("animation_sheet.png")); 
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth()/FRAME_COLS, walkSheet.getHeight()/FRAME_ROWS);              // #10
        TextureRegion[][] tmpB = TextureRegion.split(walkSheet, walkSheet.getWidth()/FRAME_COLS, walkSheet.getHeight()/FRAME_ROWS);              // #10
        walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        walkFramesB = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        walkAnimationF = new Animation(0.025f, walkFrames);      
        walkFramesB = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                tmpB[i][j].flip(true,false);
            }
        }
        
        index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFramesB[index++] = tmpB[i][j];
            }
        }
        walkAnimationF = new Animation(0.025f, walkFrames);      
        walkAnimationB = new Animation(0.025f, walkFramesB);   
        
        currentWalkAnimation = walkAnimationF;
        spriteBatch = new SpriteBatch();                
        stateTime = 0f;                         

        player = new Rectangle(x+5f, y, tmpB[0][0].getRegionWidth()-10f,tmpB[0][0].getRegionHeight()-8f);
       
    }

    @Override
    public void render() {
    	currentFrame = currentWalkAnimation.getKeyFrame(stateTime, true); 
    	player.setPosition(x, y);
//    	coin.rotate(0.2f);
    	coin.setPosition(350, 50);
    	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);    
    	handleInput();
    	sr.setProjectionMatrix(cam.combined);
        sr.begin(ShapeType.Filled);
        sr.setColor(255,255,255,0);
        sr.circle(50, 50, 20);
//        sr.rect(player.getX(),player.getY(),player.getWidth(),player.getHeight());
//        sr.rect(coin.getBoundingRectangle().x,coin.getBoundingRectangle().y,coin.getBoundingRectangle().width,coin.getBoundingRectangle().height);
        sr.end();
        
    	spriteBatch.setProjectionMatrix(cam.combined);
        stateTime += Gdx.graphics.getDeltaTime();           
         
        
        
        spriteBatch.begin();
//        if(x>background.getWidth()/2){
//        	spriteBatch.draw(background,background.getWidth()/2,0);	
//        }
//        else{
//        	spriteBatch.draw(background,0,0);	
//        }
        if(!collected)
        	coin.draw(spriteBatch);
        spriteBatch.draw(currentFrame, x, y);
        spriteBatch.end();
//        System.out.println("Aktuelle Position: x =  " + x + " | y = " + y);
        checkCollision();
    }
    
    
    public void checkCollision(){
    	if(player.overlaps(coin.getBoundingRectangle())){
    		System.out.println("collision");
    		collected = true;
    	}
    }

    public void handleInput(){
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
        	currentWalkAnimation = walkAnimationF;
            x+=4f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
        	currentWalkAnimation = walkAnimationB;
            x-=4f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
        	currentWalkAnimation = walkAnimationB;
            y+=4f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
        	currentWalkAnimation = walkAnimationB;
            y-=4f;
        }
        
        
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
        	x_cam -= 2f;
        	cam.position.set(x_cam, y_cam, 0);
        	cam.update();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
        	x_cam += 2f;
        	cam.position.set(x_cam, y_cam, 0);
        	cam.update();
        	
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
        	y_cam += 2f;
        	cam.position.set(x_cam, y_cam, 0);
        	cam.update();
        	
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
        	y_cam -= 2f;
        	cam.position.set(x_cam, y_cam, 0);
        	cam.update();
        	
        }
        if(Gdx.input.isTouched()){
        	System.out.println("mouse");
        }
    }
    
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}