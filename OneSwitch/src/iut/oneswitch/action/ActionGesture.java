package iut.oneswitch.action;

import java.io.IOException;

import android.graphics.Point;

/**
 * Classe permettant de gèrer les actions de la popUp dévoilé par un clic normal.
 * @author OneSwitch B
 *
 */
public class ActionGesture {
	
	/**
	 * Permet d'effectuer un clic.
	 * @param x L'absisse du point où le clic doit être effectué.
	 * @param y L'ordonné du point où le clic doit être effectué.
	 */
	public static void click(int x, int y){
		try{
			Runtime.getRuntime().exec("su -c input tap " + x + " " + y);
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Permet d'effectuer un glisser.
	 * @param x L'absisse du premier point.
	 * @param y L'ordonné du premier point.
	 * @param x2 L'absisse du deuxième point.
	 * @param y2 L'ordonné du deuxième point.
	 */
	public static void swipe(int x, int y, int x2, int y2){
		try{
			Runtime.getRuntime().exec("su -c input swipe " + x + " " + y + " " + x2 + " " + y2);
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Effectuer un long clic.
	 * @param x L'absisse du point où le clic doit être effectué.
	 * @param y L'ordonné du point où le clic doit être effectué.
	 */
	public static void longClick(int x, int y){
		try{
			Runtime.getRuntime().exec("su -c input swipe " + x + " " + y + " " + x + " " + y + " 800");
			return;
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Permet d'effectuer un glisser vers le haut.
	 * @param x L'absisse du point à partir duquel le glisser doit être effectué.
	 * @param y L'ordonné du point à partir duquel le glisser doit être effectué.
	 * @param screenSize
	 */
	public static void pageUp(int x, int y, Point screenSize){
		try{
			Runtime.getRuntime().exec("su -c input swipe " + x + " " + y + " " + x + " " + screenSize.y);
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Permet d'effectuer un glisser vers le bas.
	 * @param x L'absisse du point à partir duquel le glisser doit être effectué.
	 * @param y L'ordonné du point à partir duquel le glisser doit être effectué.
	 * @param screenSize
	 */
	public static void pageDown(int x, int y, Point screenSize){
		try{
			Runtime.getRuntime().exec("su -c input swipe " + x + " " + y + " " + x + " " + 0);
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Permet d'effectuer un glisser vers la gauche.
	 * @param x L'absisse du point à partir duquel le glisser doit être effectué.
	 * @param y L'ordonné du point à partir duquel le glisser doit être effectué.
	 * @param screenSize
	 */
	public static void pageLeft(int x, int y, Point screenSize){
		try{
			Runtime.getRuntime().exec("su -c input swipe " + x + " " + y + " " + screenSize.x + " " + y);
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Permet d'effectuer un glisser vers la droite.
	 * @param x L'absisse du point à partir duquel le glisser doit être effectué.
	 * @param y L'ordonné du point à partir duquel le glisser doit être effectué.
	 * @param screenSize
	 */
	public static void pageRight(int x, int y, Point screenSize){
		try{
			Runtime.getRuntime().exec("su -c input swipe " + x + " " + y + " " + 0 + " " + y);
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
}
