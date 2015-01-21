package ai;

import de.szut.pongsim.ai.User;
import de.szut.pongsim.physics.PadMovement;
import de.szut.pongsim.physics.Point;

/**
 * Demo KI, um zu testen, ob das Laden der KIs funktioniert und ob alles korrekt berechnet + angezeigt wird
 * @author Alexander
 *
 */
public class DemoAI implements User {


	/**
	 * wichtigste Methode, die als Schnittstelle zum Rest des Programms dient und die KI koordiniert
	 */
	@Override
	public PadMovement nextStep(int ownPadBottomY, int enemyPadBottomY, Point BallPos, int BallSpeed, boolean isDefender) {
		return changePadMovement(ownPadBottomY + 2, BallPos.getY());
	}
	/**
	 * Berechnet die n�chste Bewegung des Pads -> Pad versucht immer auf H�he des Balls zu bleiben
	 * @param PadY ungef�hre Mitte des eigenen Pads
	 * @param BallY Y Koordinate des Balls
	 * @return Die n�chste Bewegung des Pads
	 */
	private PadMovement changePadMovement(int PadY, int BallY) {
		if(PadY > BallY) {
			return PadMovement.DOWN;
		} else if(PadY < BallY) {
			return PadMovement.UP;
		} else {
			return PadMovement.STOP;
		}
	}
	
	/**
	 * Setzt den Zustand der KI nach einer Runde zur�ck.
	 */
	public void reset() {}

}
