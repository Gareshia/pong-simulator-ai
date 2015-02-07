package ai;

import de.szut.pongsim.ai.User;
import de.szut.pongsim.physics.PadMovement;
import de.szut.pongsim.physics.Point;

public class AI implements User {

	private Point firstBallPosition;
	private boolean isFirstStep;
	private boolean isLeftPlayer;
	private boolean alreadyCollided;
	private Point padCollisionPoint;

	public AI() {
		isFirstStep = true;
		alreadyCollided = false;
		padCollisionPoint = new Point(0, 27);
	}

	// @Override
	// public PadMovement nextStep(int ownPadBottomY, int enemyPadBottomY,
	// Point ballPos, int ballSpeed, boolean isDefender) {
	//
	// if(isDefender) {
	// if(isFirstStep) {
	// firstBallPosition = ballPos;
	// isFirstStep = false;
	// } else {
	// LinearFunction flightRoute = generateLinearFunction(firstBallPosition,
	// ballPos);
	// //System.out.println(flightRoute.getM());
	//
	// if(ballPos.getX() > firstBallPosition.getX()) {
	// System.out.println((Math.abs(flightRoute.getF(64)) % 120)%60);
	// if((Math.abs(flightRoute.getF(64)) % 120)%60 < ownPadBottomY+2) {
	// return PadMovement.DOWN;
	// }
	// return PadMovement.UP;
	// // if(flightRoute.getM() > 0) {
	// // //oben
	// // } else {
	// // //unten
	// // }
	// } else {
	// System.out.println((Math.abs(flightRoute.getF(0)) % 120)%60);
	// if((Math.abs(flightRoute.getF(0)) % 120)%60 < ownPadBottomY+2) {
	// return PadMovement.DOWN;
	// }
	// return PadMovement.UP;
	// // if(flightRoute.getM() > 0) {
	// // //unten
	// // } else {
	// // //oben
	// // }
	// }
	// }
	// } else {
	//
	// }
	// return PadMovement.STOP;
	// }

	@Override
	public void reset() {
		firstBallPosition = null;
		isFirstStep = true;

	}

	@Override
	public void setLeftSide() {
		isLeftPlayer = true;

	}

	@Override
	public void setRightSide() {
		isLeftPlayer = false;

	}

	@Override
	public PadMovement nextStep(int ownPadBottomY, int enemyPadBottomY) {
		if (padCollisionPoint.getY() < ownPadBottomY + 1) {
			return PadMovement.DOWN;
		} else if (padCollisionPoint.getY() > ownPadBottomY + 1) {
			return PadMovement.UP;
		}
		return PadMovement.STOP;
	}

	// Padcollision beachten! firstpoint zur�cksetzen!
	@Override
	public void updateBallPos(Point ballPos) {
		if (isFirstStep) {
			firstBallPosition = ballPos;
			isFirstStep = false;
		} else {
			if (collisionDetected(ballPos)) {
				isFirstStep = true;
			} else {
				LinearFunction flightRoute = generateLinearFunction(
						firstBallPosition, ballPos);
				if (isLeftPlayer) {
					// is left player
					if (ballPos.getX() < firstBallPosition.getX()) {
						// is defender
						double functionLength = flightRoute.getF(1);
						if (calculateAmountOfCollisions(functionLength) % 2 == 1) {
							padCollisionPoint = new Point(0,
									60 - Math.abs((int) functionLength % 60));
						} else {
							padCollisionPoint = new Point(0,
									Math.abs((int) functionLength % 60));
						}
					} else {
						// is not defender
						padCollisionPoint = new Point(0, 27);
					}
				} else {
					// is right player
					if (ballPos.getX() > firstBallPosition.getX()) {
						// is defender
						double functionLength = flightRoute.getF(63);
						if (calculateAmountOfCollisions(functionLength) % 2 == 1) {
							padCollisionPoint = new Point(63,
									60 - Math.abs((int) functionLength % 60));
						} else {
							padCollisionPoint = new Point(63,
									Math.abs((int) functionLength % 60));
						}
					} else {
						// is not defender
						padCollisionPoint = new Point(63, 27);
					}
				}
			}
		}
	}

	private LinearFunction generateLinearFunction(
			Point firstBallPosition, Point currentBallPosition) {
		double m = ((double) currentBallPosition.getY() - (double) firstBallPosition
				.getY())
				/ ((double) currentBallPosition.getX() - (double) firstBallPosition
						.getX());
		double b = (double) currentBallPosition.getY() - m
				* (double) currentBallPosition.getX();
		return new LinearFunction(m, b);
	}

	private int calculateAmountOfCollisions(double functionLength) {
		if (functionLength < 0 && functionLength / 60 > -1 && functionLength / 60 < -2) {
			return (int) (functionLength / 60) + 1;
		}
		return (int) functionLength / 60;
	}
	
	private boolean collisionDetected(Point currentBallPosition) {
		if((currentBallPosition.getX() == 1 || currentBallPosition.getX() == 63 || currentBallPosition.getY() == 0 || currentBallPosition.getY() == 59) && !alreadyCollided) {
			alreadyCollided = true;
			return true;
		}
		if(currentBallPosition.getY() > 0 && currentBallPosition.getY() < 59) {
			alreadyCollided = false;
		}
		return false;
	}
}