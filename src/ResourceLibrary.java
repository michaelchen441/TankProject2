import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.sun.javafx.tk.Toolkit;

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

	public AudioInputStream proj_to_wall;
	public AudioInputStream projRicochet;
	public AudioInputStream proj_to_proj;
	public AudioInputStream proj_to_destructableWall;
	public AudioInputStream tankFiring;
	public AudioInputStream backgroundClassic;
	public AudioInputStream backgroundSurvival;
	public AudioInputStream gameOver;
	public AudioInputStream proj_to_aiTank;
	public AudioInputStream progressLevel;
	

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

			//			File soundFile = new File("audio/proj_to_wall.wav.wav");
			//			audio1 = AudioSystem.getAudioInputStream(soundFile);

			//			
			//			URL url = this.getClass().getClassLoader().getResource("audio/proj_to_wall.wav");
			//			audio1 = AudioSystem.getAudioInputStream(url);
			


		}
		catch (IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void retrieveAudio(int i)
	{
		switch(i){
		case 1: 
			try
			{
				projRicochet = AudioSystem.getAudioInputStream(this.getClass().getClassLoader().getResource("audio/projRicochet.wav"));
			} catch (UnsupportedAudioFileException | IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 2: 
			try
			{
				proj_to_destructableWall = AudioSystem.getAudioInputStream(this.getClass().getClassLoader().getResource("audio/proj_to_destructableWall.wav"));
			} catch (UnsupportedAudioFileException | IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 3:
			try
			{
				tankFiring = AudioSystem.getAudioInputStream(this.getClass().getClassLoader().getResource("audio/tankFiring.wav"));
			} catch (UnsupportedAudioFileException | IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 4:
			try
			{
				backgroundClassic = AudioSystem.getAudioInputStream(this.getClass().getClassLoader().getResource("audio/backgroundClassic.wav"));
			} catch (UnsupportedAudioFileException | IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 5:
			try
			{
				backgroundSurvival = AudioSystem.getAudioInputStream(this.getClass().getClassLoader().getResource("audio/backgroundSurvival.wav"));
			} catch (UnsupportedAudioFileException | IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 6:
			try
			{
				progressLevel = AudioSystem.getAudioInputStream(this.getClass().getClassLoader().getResource("audio/progressLevel.wav"));
			} catch (UnsupportedAudioFileException | IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 7:
			try
			{
				proj_to_wall = AudioSystem.getAudioInputStream(this.getClass().getClassLoader().getResource("audio/proj_to_wall.wav"));
			} catch (UnsupportedAudioFileException | IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		}

	}

	public void playClip(int i)
	{
		Clip clip = null;
		retrieveAudio(i);
	
		try
		{
			clip = AudioSystem.getClip();
			switch(i){
			case 1: 
				clip.open(projRicochet);
				break;
			case 2:
				clip.open(proj_to_destructableWall);
				break;
			case 3:
				clip.open(tankFiring);
				break;
			case 4:
				clip.open(backgroundClassic);
				break;
			case 5:
				clip.open(backgroundSurvival);
				break;
			case 6:
				clip.open(progressLevel);
				break;
			case 7:
				clip.open(proj_to_wall);
				break;
			
			}
			clip.start();
		} catch (LineUnavailableException | IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	
}
