package com.bendeutz.animation;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class Animator implements ApplicationListener {

    private static final int        FRAME_COLS = 6;         // #1
    private static final int        FRAME_ROWS = 5;         // #2

    Animation                       walkAnimationF;          // #3
    Animation                       currentWalkAnimation;          // #3
    
    Animation                       walkAnimationB;          // #3
    Texture                         walkSheet;              // #4
    Texture 		walkSheetB;
    TextureRegion[]                 walkFrames;             // #5
    TextureRegion[]                 walkFramesB;             // #5
    SpriteBatch                     spriteBatch;            // #6
    TextureRegion                   currentFrame;           // #7
    OrthographicCamera cam;

    float x;
    float stateTime;                                        // #8
    
    float x_cam;
    float y_cam;

    @Override
    public void create() {
    	
    	 x = 50;
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
         
         
        walkSheet = new Texture(Gdx.files.internal("animation_sheet.png")); // #9
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
        walkAnimationF = new Animation(0.025f, walkFrames);      // #11
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
        walkAnimationF = new Animation(0.025f, walkFrames);      // #11
        walkAnimationB = new Animation(0.025f, walkFramesB);      // #11
        currentWalkAnimation = walkAnimationF;
        spriteBatch = new SpriteBatch();                // #12
        stateTime = 0f;                         // #13
       
    }

    @Override
    public void render() {
    	handleInput();
//    	x_cam -= 1f;
//    	cam.position.set(x_cam, y_cam, 0);
//    	cam.update();
    	
    	spriteBatch.setProjectionMatrix(cam.combined);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);                        // #14
        stateTime += Gdx.graphics.getDeltaTime();           // #15
        currentFrame = currentWalkAnimation.getKeyFrame(stateTime, true);  // #16
        spriteBatch.begin();
        spriteBatch.draw(currentFrame, x, 50);
        spriteBatch.end();
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