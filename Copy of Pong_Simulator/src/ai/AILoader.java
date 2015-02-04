package ai;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * L�dt KI Klassen w�hrend der Runtime in das Programm und erm�glicht das wechseln der KIs zur Runtime
 * @author Alexander
 */

public class AILoader {
	
	private String className;
	private File classFile;
	private Class<?> loadedClass;
	private Object classInstance;
	private URLClassLoader cl;
	/**
	 * Konstante, die das Package aller KIs, die geladen werden sollen bestimmt
	 */
	private static final String AI_PACKAGE_NAME = "ai";
	
	/**
	 * Erstellt ein Objekt von der Klasse, die geladen werden soll
	 * @param file Class Datei, die geladen werden soll
	 * @return Objekt der zu ladenden class Datei
	 */
	
	public User loadClassObject(File file) {
		classFile = file;
		//Da der URLClassloader als URL den Ordner haben will, indem man suchen will und in diesem Ordner der Packageordner
		//und dadrin dann die Class Datei sein soll, muss man zwei Ordner nach hinten gehen, damit man nicht den Packageordner
		//speichert
		File classDirectory = file.getParentFile().getParentFile();
		try {
			cl = new URLClassLoader(new URL[]{classDirectory.toURI().toURL()}, this.getClass().getClassLoader());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		//Aufrufen der loadClass Methode, um die Klasse zu laden und zur�ckgegeben zu bekommen
		loadedClass = loadClass();
		if(loadedClass != null) {
			try {
				//Erstellt Objekt der geladenen Klasse
				if(User.class.isAssignableFrom(loadedClass)) {
					classInstance = loadedClass.newInstance();
					//�berpr�fung, ob das Objekt korrekt erstellt wurde und ob die Klasse das User interface implementiert
					if(classInstance != null) {
						return (User) classInstance;
					}
				}
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} 
		}
		return null;
	}
	
	/**
	 * L�dt die Klasse, die zuvor in classFile gespeichert wurde
	 * @return die geladene Klasse
	 */
	 private Class<?> loadClass() {
		  className = classFile.getName();
		  //Entfernt die endung vom Klassennamen
		  className = className.substring(0, className.indexOf("."));
		  try {
			  	//Gibt die Klasse zur�ck und erstellt den bin�ren Namen der Klasse (Package + Class)
			  	//Nicht m�glich wenn der gleiche Klassenname mit dem gleichen Packagenamen in dem Projekt ist
		   		return cl.loadClass(AI_PACKAGE_NAME + "." + className);
		  } catch(ClassNotFoundException c) {
			  System.err.println("Cannot load Class: " + className);
		  }
		  return null;
	 }
}
