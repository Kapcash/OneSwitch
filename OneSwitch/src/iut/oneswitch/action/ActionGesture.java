package iut.oneswitch.action;

import java.io.IOException;

import android.graphics.Point;

/**
 * Classe permettant de gèrer les actions de la popUp sur un clic normal.
 * @author OneSwitch B
 *
 */
public class ActionGesture {
	
	/**
	 * Permet d'effectuer un clic
	 * @param x absisses
	 * @param y ordonné
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
	 * @param x absisses
	 * @param y ordonné
	 * @param x2 absisses du deuxième clic
	 * @param y2 ordonné du deuxième clic
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
	 * Effectuer un long clic
	 * @param x absisses
	 * @param y ordonné
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
	 * @param x absisse
	 * @param y ordonné
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
	 * @param x absisse
	 * @param y ordonné
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
	 * @param x absisse
	 * @param y ordonné
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
	 * @param x absisse
	 * @param y ordonné
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
