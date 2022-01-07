package com.cheeselevel.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.graphics.Camera;

public class CheeseLevel implements Screen {
	private Stage mainStage;
	private AnimatedActor pari;
	private BaseActor ikan;
	private BaseActor ikan1;
	private BaseActor shark;
	private BaseActor shark1;
	private BaseActor shark2;
	private BaseActor floor;
	private BaseActor winText;
	private BaseActor loseText;

	private float timeElapsed;
	private Label timeLabel;

	private Stage uiStage;

	private boolean win;
	private boolean lose;

	private final int SPEED = 250;

	private final int mapHeight = 800;
	private final int mapWidth = 800;

	private final int viewWidth = 640;
	private final int viewHeight = 480;

	public Game game;

	public CheeseLevel(Game g) {
		game = g;
		create();
	}

	public void create () {
		timeElapsed = 0;
		BitmapFont font = new BitmapFont();
		String text = "Time: 0";
		LabelStyle style = new LabelStyle(font, Color.NAVY);
		timeLabel = new Label(text, style);
		timeLabel.setFontScale(2);
		timeLabel.setPosition(500, 440);

		mainStage = new Stage();
		uiStage = new Stage();

		floor = new BaseActor();
		floor.setTexture(new Texture(Gdx.files.internal("tiles-800-800.jpg")));
		floor.setPosition(0, 0);
		mainStage.addActor(floor);

		pari = new AnimatedActor();

		TextureRegion[] frames = new TextureRegion[4];

		for (int n = 0; n < 4; n++) {
			Texture tex = new Texture(Gdx.files.internal("pari"+n+".png"));
			tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
			frames[n] = new TextureRegion(tex);
		}

		Array<TextureRegion> framesArray = new Array<TextureRegion>(frames);

		Animation anim = new Animation(0.1f, framesArray, Animation.PlayMode.LOOP_PINGPONG);

		pari.setAnimation(anim);
		pari.setOrigin(pari.getWidth() / 2, pari.getHeight() / 2);
		pari.setPosition(20,20);
		mainStage.addActor(pari);

		ikan = new BaseActor();
		ikan.setTexture(new Texture(Gdx.files.internal("ikan.png")));
		ikan.setOrigin(ikan.getWidth() / 2, ikan.getHeight() / 2);
		ikan.setPosition(400, 500);
		mainStage.addActor(ikan);

		ikan1 = new BaseActor();
		ikan1.setTexture(new Texture(Gdx.files.internal("ikan.png")));
		ikan1.setOrigin(ikan1.getWidth() / 2, ikan1.getHeight() / 2);
		ikan1.setPosition(400, 300);
		mainStage.addActor(ikan1);

		shark = new BaseActor();
		shark.setTexture(new Texture(Gdx.files.internal("shark.png")));
		shark.setOrigin(shark.getWidth() / 2, shark.getHeight() / 2);
		shark.setPosition(100, 300);
		mainStage.addActor(shark);

		shark1 = new BaseActor();
		shark1.setTexture(new Texture(Gdx.files.internal("shark.png")));
		shark1.setOrigin(shark1.getWidth() / 2, shark1.getHeight() / 2);
		shark1.setPosition(250, 350);
		mainStage.addActor(shark1);

		shark2 = new BaseActor();
		shark2.setTexture(new Texture(Gdx.files.internal("shark.png")));
		shark2.setOrigin(shark2.getWidth() / 2, shark2.getHeight() / 2);
		shark2.setPosition(50, 400);
		mainStage.addActor(shark2);

//		grass = new BaseActor();
//		grass.setTexture(new Texture(Gdx.files.internal("grass.png")));
//		grass.setOrigin(grass.getWidth() / 2, grass.getHeight() / 2);
//		grass.setPosition(20, 30);
//		mainStage.addActor(grass);

		winText = new BaseActor();
		winText.setTexture(new Texture(Gdx.files.internal("you-win.png")));
		winText.setPosition(170, 60);
		winText.setVisible(false);

		loseText = new BaseActor();
		loseText.setTexture(new Texture(Gdx.files.internal("you-lose.png")));
		loseText.setPosition(170, 60);
		loseText.setVisible(false);

		uiStage.addActor(winText);
		uiStage.addActor(loseText);
		uiStage.addActor(timeLabel);

		win = false;
		lose = false;
	}


	@Override
	public void show() {

	}

	public void render (float dt) {
		pari.velocityX = 0;
		pari.velocityY = 0;

		if (Gdx.input.isKeyPressed(Keys.M))
			game.setScreen(new CheeseMenu(game));

		if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT))
			pari.velocityX -= SPEED;
		else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT))
			pari.velocityX += SPEED;
		else if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Keys.UP))
			pari.velocityY += SPEED;
		else if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Keys.DOWN))
			pari.velocityY -= SPEED;

		mainStage.act(dt);
		uiStage.act(dt);

		pari.setX(MathUtils.clamp(pari.getX(), 0, mapWidth - pari.getWidth()));
		pari.setY(MathUtils.clamp(pari.getY(), 0, mapHeight - pari.getHeight()));

		Rectangle ikanRectangle
				= ikan.getBoundingRectangle();
		Rectangle ikan1Rectangle
				= ikan1.getBoundingRectangle();
		Rectangle sharkRectangle
				= shark.getBoundingRectangle();
		Rectangle shark1Rectangle
				= shark1.getBoundingRectangle();
		Rectangle shark2Rectangle
				= shark2.getBoundingRectangle();
		Rectangle pariRectangle
				= pari.getBoundingRectangle();

		if ( !lose && ikanRectangle.contains(pariRectangle) ) {
			win = true;

			Action spinShrinkFadeOut = Actions.parallel(
					Actions.alpha(1),
					Actions.rotateBy(360, 1),
					Actions.scaleTo(0, 0, 1),
					Actions.fadeOut(1)
			);
			ikan.addAction(spinShrinkFadeOut);

			Action fadeInColorCycleForever = Actions.sequence(
					Actions.alpha(0),
					Actions.show(),
					Actions.fadeIn(2),
					Actions.forever(
							Actions.sequence(
									Actions.color(new Color(1,0,0,1), 1),
									Actions.color(new Color(0,0,1,1), 1)
							)
					)

			);

			winText.addAction(fadeInColorCycleForever);

			if (Gdx.input.isKeyPressed(Keys.P))
				game.setScreen(new CheeseLevel(game));
		}

		if ( !lose && ikan1Rectangle.contains(pariRectangle) ) {
			win = true;

			Action spinShrinkFadeOut = Actions.parallel(
					Actions.alpha(1),
					Actions.rotateBy(360, 1),
					Actions.scaleTo(0, 0, 1),
					Actions.fadeOut(1)
			);
			ikan1.addAction(spinShrinkFadeOut);

			Action fadeInColorCycleForever = Actions.sequence(
					Actions.alpha(0),
					Actions.show(),
					Actions.fadeIn(2),
					Actions.forever(
							Actions.sequence(
									Actions.color(new Color(1,0,0,1), 1),
									Actions.color(new Color(0,0,1,1), 1)
							)
					)

			);

			winText.addAction(fadeInColorCycleForever);

			if (Gdx.input.isKeyPressed(Keys.P))
				game.setScreen(new CheeseLevel(game));
		}

		if ( !win && sharkRectangle.contains(pariRectangle) ) {
			lose = true;

			Action spinShrinkFadeOut = Actions.parallel(
					Actions.alpha(1),
					Actions.rotateBy(360, 1),
					Actions.scaleTo(0, 0, 1),
					Actions.fadeOut(1)
			);
			pari.addAction(spinShrinkFadeOut);

			Action fadeInColorCycleForever = Actions.sequence(
					Actions.alpha(0),
					Actions.show(),
					Actions.fadeIn(2),
					Actions.forever(
							Actions.sequence(
									Actions.color(new Color(1,0,0,1), 1),
									Actions.color(new Color(0,0,1,1), 1)
							)
					)
			);

			loseText.addAction(fadeInColorCycleForever);

			if (Gdx.input.isKeyPressed(Keys.P))
				game.setScreen(new CheeseLevel(game));
		}

		if ( !win && shark1Rectangle.contains(pariRectangle) ) {
			lose = true;

			Action spinShrinkFadeOut = Actions.parallel(
					Actions.alpha(1),
					Actions.rotateBy(360, 1),
					Actions.scaleTo(0, 0, 1),
					Actions.fadeOut(1)
			);
			pari.addAction(spinShrinkFadeOut);

			Action fadeInColorCycleForever = Actions.sequence(
					Actions.alpha(0),
					Actions.show(),
					Actions.fadeIn(2),
					Actions.forever(
							Actions.sequence(
									Actions.color(new Color(1,0,0,1), 1),
									Actions.color(new Color(0,0,1,1), 1)
							)
					)
			);

			loseText.addAction(fadeInColorCycleForever);

			if (Gdx.input.isKeyPressed(Keys.P))
				game.setScreen(new CheeseLevel(game));
		}

		if ( !win && shark2Rectangle.contains(pariRectangle) ) {
			lose = true;

			Action spinShrinkFadeOut = Actions.parallel(
					Actions.alpha(1),
					Actions.rotateBy(360, 1),
					Actions.scaleTo(0, 0, 1),
					Actions.fadeOut(1)
			);
			pari.addAction(spinShrinkFadeOut);

			Action fadeInColorCycleForever = Actions.sequence(
					Actions.alpha(0),
					Actions.show(),
					Actions.fadeIn(2),
					Actions.forever(
							Actions.sequence(
									Actions.color(new Color(1,0,0,1), 1),
									Actions.color(new Color(0,0,1,1), 1)
							)
					)
			);

			loseText.addAction(fadeInColorCycleForever);

			if (Gdx.input.isKeyPressed(Keys.P))
				game.setScreen(new CheeseLevel(game));
		}


		if (!win && !lose)
			timeElapsed += dt;
		timeLabel.setText("Time : " + (int)timeElapsed );

		Gdx.gl.glClearColor(0.8f, 0.8f, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		Camera cam = mainStage.getCamera();
		cam.position.set(shark.getX() + shark.getOriginX(), shark.getY() + shark.getOriginY(), 0);

		cam.position.x = MathUtils.clamp(cam.position.x, viewWidth / 2, mapWidth - viewWidth / 2);
		cam.position.y = MathUtils.clamp(cam.position.y, viewHeight / 2, mapHeight - viewHeight / 2);
		cam.update();

		mainStage.draw();
		uiStage.draw();

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	public void dispose () {

	}
}
