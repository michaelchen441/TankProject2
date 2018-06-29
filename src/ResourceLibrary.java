import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.sun.javafx.tk.Toolkit;

import sun.audio.AudioData;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

public class ResourceLibrary
{

	//create all images
	public BufferedImage background;
	public BufferedImage crosshair;
	public BufferedImage projectile;
	public BufferedImage explosion;

	public BufferedImage indestructableWall;
	public BufferedImage destructableWall;

	public BufferedImage greenTank;
	public BufferedImage redTank;
	public BufferedImage blueTank;
	public BufferedImage blackTank;
	public BufferedImage whiteTank;
	public BufferedImage pinkTank;
	public BufferedImage yellowTank;

	public BufferedImage greenTurret;
	public BufferedImage redTurret;
	public BufferedImage blueTurret;
	public BufferedImage blackTurret;
	public BufferedImage whiteTurret;
	public BufferedImage pinkTurret;
	public BufferedImage yellowTurret;



	public int K_proj_to_wall = 0;
	public int K_projRicochet = 1;
	public int K_proj_to_proj = 2;
	public int K_proj_to_destructableWall = 3;
	public int K_tankFiring = 4;
	public int K_backgroundClassic = 5;
	public int K_backgroundSurvival = 6;
	public int K_gameOver = 7;
	public int K_proj_to_aiTank = 8;
	public int K_progressLevel = 9;
	public int K_Max = 10;

	Clip backgroundMus;

	private String[]	audioFileNameArr = new String[K_Max];


	// constructed once in tank panel and sent to other classes as an imput in draw methods
	public ResourceLibrary()
	{
		try
		{

			background = ImageIO.read(getClass().getResource("images/Background2.png"));	
			crosshair = ImageIO.read(getClass().getResource("images/crosshair.png"));		
			projectile = ImageIO.read(getClass().getResource("images/projectile.png"));	
			explosion = ImageIO.read(getClass().getResource("images/explosion.png"));

			indestructableWall = ImageIO.read(getClass().getResource("images/Metal_50x50.jpg"));	
			destructableWall = ImageIO.read(getClass().getResource("images/Wood_50x50.png"));

			greenTank = ImageIO.read(getClass().getResource("images/greenTank.png"));	
			redTank = ImageIO.read(getClass().getResource("images/redTank.png"));	 
			blueTank = ImageIO.read(getClass().getResource("images/blueTank.png"));	
			blackTank = ImageIO.read(getClass().getResource("images/blackTank.png"));	
			whiteTank = ImageIO.read(getClass().getResource("images/whiteTank.png"));	 
			pinkTank = ImageIO.read(getClass().getResource("images/pinkTank.png"));	
			yellowTank = ImageIO.read(getClass().getResource("images/yellowTank.png"));	

			greenTurret = ImageIO.read(getClass().getResource("images/20x50 turrets/greenTurret.png"));
			redTurret = ImageIO.read(getClass().getResource("images/20x50 turrets/redTurret.png"));
			blueTurret = ImageIO.read(getClass().getResource("images/20x50 turrets/blueTurret.png"));
			blackTurret = ImageIO.read(getClass().getResource("images/20x50 turrets/blackTurret.png"));
			whiteTurret = ImageIO.read(getClass().getResource("images/20x50 turrets/whiteTurret.png"));
			pinkTurret = ImageIO.read(getClass().getResource("images/20x50 turrets/pinkTurret.png"));
			yellowTurret = ImageIO.read(getClass().getResource("images/20x50 turrets/yellowTurret.png"));




		}
		catch (IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		audioFileNameArr[K_proj_to_wall]				= "audio/proj_to_wall.wav";
		audioFileNameArr[K_projRicochet]				= "audio/projRicochet.wav";
		audioFileNameArr[K_proj_to_proj]				= "audio/projRicochet.wav";
		audioFileNameArr[K_proj_to_destructableWall]	= "audio/proj_to_destructableWall.wav";
		audioFileNameArr[K_tankFiring]					= "audio/tankFiring.wav";
		audioFileNameArr[K_backgroundClassic]			= "audio/backgroundClassic.wav";
		audioFileNameArr[K_backgroundSurvival]			= "audio/backgroundSurvival.wav";
		audioFileNameArr[K_gameOver]					= "audio/gameOver.wav";
		audioFileNameArr[K_proj_to_aiTank]				= "audio/proj_to_aiTank.wav";
		audioFileNameArr[K_progressLevel]				= "audio/progressLevel.wav";



	}

	public void playClip(int i) 
	{
		try
		{
			AudioInputStream	theStream = AudioSystem.getAudioInputStream(this.getClass().getClassLoader().getResource(audioFileNameArr[i]));
			Clip clip = AudioSystem.getClip();
			if(!(i == 5|| i == 6)) {
				clip.open(theStream);
				clip.start();
			}
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void playBackground(int i)
	{
		try {
			backgroundMus.stop();
			AudioInputStream	theStream = AudioSystem.getAudioInputStream(this.getClass().getClassLoader().getResource(audioFileNameArr[i]));
			backgroundMus = AudioSystem.getClip();
			backgroundMus.open(theStream);
			backgroundMus.loop(Clip.LOOP_CONTINUOUSLY);

		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			// TODO Auto-generated catch bloc catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
